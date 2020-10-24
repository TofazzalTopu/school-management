package com.grailslab

import com.grailslab.command.OpenTimeLineCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenTimeLine
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class TimeLineService {

    def messageSource
    def springSecurityService


    static final String[] sortColumns = ['sortOrder','title','name','iconClass','activeStatus']
    LinkedHashMap timeLinePaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = OpenTimeLine.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('title', sSearch)
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { OpenTimeLine obj ->
                dataReturns.add([DT_RowId: obj.id,  0: obj.sortOrder, 1: obj.title, 2:obj.name, 3:obj.iconClass, 4:obj.activeStatus.value, 5:obj.description, 6: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def showTimeLine(){
        int iDisplayStart = CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = CommonUtils.MAX_PAGINATION_LENGTH
        String sSortDir = CommonUtils.SORT_ORDER_DESC
        String sortColumn = "sortOrder"
        def c = OpenTimeLine.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
            }
            order(sortColumn, sSortDir)
        } as List
    }

    def saveOrUpdate(OpenTimeLineCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            message = errorList?.join('\n')
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        OpenTimeLine timeLine
        if (command.id) {
            timeLine = OpenTimeLine.get(command.id)
            if (!timeLine) {
                message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            timeLine.properties['title', 'name', 'sortOrder', 'iconClass', 'description'] = command.properties
            timeLine.updatedBy = createOrUpdateBy
                    message = 'TimeLine Updated Successfully.'
        } else {
            timeLine = new OpenTimeLine(command.properties)
            timeLine.createdBy = createOrUpdateBy
            timeLine.academicYear = academicYear
            message = 'TimeLine Added Successfully.'
        }
        timeLine.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenTimeLine timeLine = OpenTimeLine.get(id)
        if (!timeLine) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (timeLine.activeStatus.equals(ActiveStatus.INACTIVE)) {
            timeLine.activeStatus = ActiveStatus.ACTIVE
        } else {
            timeLine.activeStatus = ActiveStatus.INACTIVE
        }
        timeLine.updatedBy = springSecurityService.principal?.username
        timeLine.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenTimeLine timeLine = OpenTimeLine.get(id)
        if (!timeLine) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        timeLine.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'TimeLine Deleted Successfully')
        return result
    }
}
