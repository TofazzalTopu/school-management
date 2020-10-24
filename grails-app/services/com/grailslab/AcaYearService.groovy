package com.grailslab

import com.grailslab.command.AcaYearCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.AcaYear
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.transaction.annotation.Transactional

@Transactional
class AcaYearService {

    def acaYearService

    static final String[] sortColumns = ['sortOrder', 'id', 'name', 'yearType','isAdmissionYear','isWorkingYear']

    LinkedHashMap acaYearPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = AcaYear.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            if (params.acaYearType != AcaYearType.ALL.value) {
                eq("yearType", params.acaYearType)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)

                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { AcaYear acaYear ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: acaYear.id, 0: serial, 1: acaYear.name, 2: acaYear.yearType, 3: acaYear.isWorkingYear? 'Yes':'No', 4: acaYear.isAdmissionYear? 'Yes':'No', 5: acaYear.sortOrder, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    AcaYear read(Long id) {
        return AcaYear.read(id)
    }
    AcaYear get(Long id) {
        return AcaYear.get(id)
    }
    AcaYear findByName(String name) {
        return AcaYear.findByNameAndActiveStatus(name, ActiveStatus.ACTIVE)
    }
    AcaYear findAdmissionYear(String yearType) {
        return AcaYear.findByYearTypeAndActiveStatusAndIsAdmissionYear(yearType, ActiveStatus.ACTIVE, Boolean.TRUE)
    }
    AcaYear findAdmissionYear() {
        return findAdmissionYear("School")
    }
    AcaYear findWorkingYear(String yearType) {
        return AcaYear.findByYearTypeAndActiveStatusAndIsWorkingYear(yearType, ActiveStatus.ACTIVE, Boolean.TRUE)
    }
    AcaYear findWorkingYear() {
        return findWorkingYear("School")
    }

    def updateAcaYearStatus(Long acaYearId, String yearType, String fieldName) {
        if (fieldName == "workingYear") {
            AcaYear.executeUpdate("update AcaYear ay set ay.isWorkingYear = false " +
                    "where ay.id !=:acaYearId and ay.yearType =:yearType ",[acaYearId: acaYearId, yearType: yearType])
        } else {
            AcaYear.executeUpdate("update AcaYear ay set ay.isAdmissionYear = false " +
                    "where ay.id !=:acaYearId and ay.yearType =:yearType ",[acaYearId: acaYearId, yearType: yearType])
        }

    }

    def saveAcaYear(AcaYearCommand command, GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()

        AcaYear ifExistAcaYear = acaYearService.findByName(command.name)
        if (ifExistAcaYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Academic year already added.')
            return result
        }

        AcaYear acaYear = new AcaYear(command.properties)
        acaYear.name = command.name.replace("Y", "")
        acaYear.activeStatus = ActiveStatus.ACTIVE
        acaYear.isAdmissionYear = false
        acaYear.isWorkingYear = false
        acaYear.sortOrder = params.sortOrder ? params.getInt("sortOrder") : 0
        AcaYear savedCurr = acaYear.save(flush: true)

        if (!savedCurr) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Please fill the form correctly')
            return result
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Academic Year created successfully')
        return result
    }

    def updateAcaYear(AcaYearCommand command, GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()

        AcaYear acaYear = acaYearService.get(command.id)
        if (command.name != acaYear.name) {
            AcaYear ifExistAcaYear = acaYearService.findByName(command.name)
            if (ifExistAcaYear) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Academic year already added.')
                return result
            }
        }

        acaYear.properties = command.properties
        acaYear.name = command.name.replace("Y", "")
        acaYear.sortOrder = params.sortOrder ? params.getInt("sortOrder") : 0
        AcaYear savedCurr = acaYear.save(flush: true)

        if (!savedCurr) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Please fill the form correctly')
            return result
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Academic Year updated successfully')
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        AcaYear acaYear = acaYearService.get(id)
        if (!acaYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        acaYear.activeStatus = ActiveStatus.DELETE
        acaYear.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Academic Year deleted successfully.")
        return result
    }

    def updateWorkingYear(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        AcaYear acaYear = acaYearService.get(id)
        if (!acaYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        updateAcaYearStatus(acaYear.id, acaYear.yearType, "workingYear")
        acaYear.isWorkingYear = Boolean.TRUE
        acaYear.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Working year updated successfully.")
        return result
    }

    def updateAdmissionYear(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        AcaYear acaYear = acaYearService.get(id)
        if (!acaYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        updateAcaYearStatus(acaYear.id, acaYear.yearType, "admissionYear")
        acaYear.isAdmissionYear = Boolean.TRUE
        acaYear.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Admission year updated successfully.")
        return result
    }

}
