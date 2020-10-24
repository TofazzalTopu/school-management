package com.grailslab

import com.grailslab.enums.OlympiadType
import com.grailslab.festival.FesProgram
import com.grailslab.festival.FesRegistration
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class FesRegistrationService {
    def grailsApplication
    def sequenceGeneratorService

    static final String[] sortColumns = ['id','name','olympiadTopics','serialNo','instituteName','className']
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
        def c = FesRegistration.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('instituteName', sSearch)
                    ilike('serialNo', sSearch)
                    ilike('contactNo', sSearch)
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
                    programTopics = null
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
                dataReturns.add([DT_RowId: obj.id, 0: serial, 1: obj.name, 2: programTopics, 3:obj.serialNo, 4: obj.instituteName, 5: obj.className.name, 6: obj.rollNo, 7: obj.contactNo, 8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    def registrationTopicList(FesProgram fesProgram){
        def results = FesRegistration.withCriteria {
            eq("fesProgram", fesProgram)
            eq("activeStatus", ActiveStatus.ACTIVE)
            isNotNull("olympiadTopics")
            projections {
                distinct("olympiadTopics")
            }
        }
        List dataReturns = new ArrayList()
        OlympiadType.values().each { type ->
            dataReturns.add([id: type.key, name: type.value])
        }
        results.each { obj ->
            if (!OlympiadType.values().find{ it.key == obj}) {
                dataReturns.add([id: obj, name: obj.replaceAll(OlympiadType.gKnowledge.key, OlympiadType.gKnowledge.value).replaceAll(OlympiadType.lCompitition.key, OlympiadType.lCompitition.value).replaceAll(OlympiadType.science.key, OlympiadType.science.value).replaceAll(OlympiadType.mathematics.key, OlympiadType.mathematics.value)])
            }
        }
        return dataReturns
    }

    String nextSerialNo(){
        String serialNo = '2' //sequenceGeneratorService.nextNumber('fesRegi2016')
    }


}
