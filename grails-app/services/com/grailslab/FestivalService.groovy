package com.grailslab

import com.grailslab.enums.OlympiadType
import com.grailslab.festival.FesProgram
import com.grailslab.festival.FesRegistration
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class FestivalService {
    def springSecurityService

    static final String[] sortColumns = ['id','name','regOpenDate','startDate']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = FesProgram.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        String programTopics
        OlympiadType olympiadType
        String[] topics
        if (totalCount > 0) {
            int serial = iDisplayStart;
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            for (obj in results) {
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                if (obj.olympiadTopics) {
                    topics = obj.olympiadTopics.split(",")
                    topics.each { topic ->
                        olympiadType = OlympiadType.valueOf(topic)
                        if(olympiadType) {
                            if(programTopics) {
                                programTopics += ", "+ olympiadType.value
                            } else {
                                programTopics = olympiadType.value
                            }
                        }
                    }
                }
                dataReturns.add([DT_RowId: obj.id, 0: serial, 1: obj.name, 2: CommonUtils.getDateRangeStr(obj.regOpenDate, obj.regCloseDate), 3: CommonUtils.getDateRangeStr(obj.startDate, obj.endDate), 4: programTopics, 5: obj.helpContact, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def festivalDDList() {
        def fesReportList = FesProgram.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: 10, sort: "id", order: "desc"])
    }

    def fesRegDelete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        FesRegistration fesRegistration = FesRegistration.get(id)
        if(!fesRegistration){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if(fesRegistration.activeStatus.equals(ActiveStatus.DELETE)){
            fesRegistration.activeStatus=ActiveStatus.ACTIVE
        } else {
            fesRegistration.activeStatus=ActiveStatus.DELETE
        }
        fesProgram.updatedBy = springSecurityService.principal?.username
        fesRegistration.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Registration Deleted successfully.")
        return result

    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        FesProgram fesProgram = FesProgram.get(id)
        if (!fesProgram) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (fesProgram.activeStatus.equals(ActiveStatus.DELETE)) {
            fesProgram.activeStatus = ActiveStatus.ACTIVE
        } else {
            fesProgram.activeStatus = ActiveStatus.DELETE
        }
        fesProgram.updatedBy = springSecurityService.principal?.username
        fesProgram.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Program Deleted successfully.")
        return result
    }

}
