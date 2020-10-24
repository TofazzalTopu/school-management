package com.grailslab

import com.grailslab.command.SalAreaSetupCommand
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.salary.SalArea
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class AreaSetupService {

    def messageSource
    def schoolService
    def springSecurityService

    static final String[] sortColumns = ['id']

    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumns)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        YearMonths yearMonths  = YearMonths.JANUARY
        if(params.yearMonths){
            yearMonths = YearMonths.valueOf(params.yearMonths)
        }

        List dataReturns = new ArrayList()
        def c = SalArea.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
                eq("yearMonths",yearMonths)
            }
            if (serverParams.sSearch) {
                or {
                    ilike('emp.empID', serverParams.sSearch)
                    ilike('emp.name', serverParams.sSearch)
                }
            }
            order(serverParams.sortColumn, serverParams.sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SalArea salArea ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salArea.id, 0: serial, 1: salArea.employee.empID, 2: salArea.employee.name,3:salArea.amount , 4: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(SalAreaSetupCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        String message
        SalArea salArea

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        if (params.id) {
            salArea = SalArea.get(params.id)
            salArea.academicYear = command.academicYear
            salArea.yearMonths = command.yearMonths
            salArea.amount = command.amount
            message = "Edit Successfully"
            salArea.updatedBy = createOrUpdateBy
        } else {
            salArea = SalArea.findByEmployeeAndAcademicYearAndYearMonthsAndActiveStatus(command.employee, command.academicYear, command.yearMonths, ActiveStatus.ACTIVE)
            if (salArea) {
                message = "Already Added"
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            salArea = new SalArea(command.properties)
            salArea.createdBy = createOrUpdateBy
            message = "Save Successfully"
        }

        salArea.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalArea salArea = SalArea.get(id)
        if (!salArea) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        salArea.activeStatus = ActiveStatus.DELETE
        salArea.updatedBy = springSecurityService.principal?.username
        salArea.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Area Setup deleted successfully.")
        return result
    }

}
