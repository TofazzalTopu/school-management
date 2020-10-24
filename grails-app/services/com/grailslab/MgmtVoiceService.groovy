package com.grailslab


import com.grailslab.enums.OpenContentType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenMgmtVoice
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class MgmtVoiceService {

    def springSecurityService

    static final String[] sortColumns = ['sortOrder','title','name','iconClass','activeStatus']
    LinkedHashMap featurePaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        OpenContentType openContentType = OpenContentType.voice
        if(params.openContentType){
            openContentType = OpenContentType.valueOf(params.openContentType)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = OpenMgmtVoice.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
                if(openContentType){
                    eq("openContentType", openContentType)
                }
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
            results.each { OpenMgmtVoice obj ->
                dataReturns.add([DT_RowId: obj.id,  0: obj.sortOrder, 1: obj.title, 2:obj.name, 3:obj.iconClass, 4:obj.activeStatus.value, 5:obj.description, 6: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenMgmtVoice mgmtVoice = OpenMgmtVoice.get(id)
        if (!mgmtVoice) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (mgmtVoice.activeStatus.equals(ActiveStatus.INACTIVE)) {
            mgmtVoice.activeStatus = ActiveStatus.ACTIVE
        } else {
            mgmtVoice.activeStatus = ActiveStatus.INACTIVE
        }
        mgmtVoice.updatedBy = springSecurityService.principal?.username
        mgmtVoice.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenMgmtVoice mgmtVoice = OpenMgmtVoice.get(id)
        if (!mgmtVoice) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        mgmtVoice.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Content Deleted Successfully')
        return result
    }
}
