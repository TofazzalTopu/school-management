package com.grailslab

import com.grailslab.command.OpenContentCommand
import com.grailslab.enums.OpenContentType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenContent
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class FeatureService {

    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['sortOrder','title','iconClass','activeStatus']
    LinkedHashMap featurePaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        OpenContentType openContentType = OpenContentType.Feature_Content
        if(params.openContentType){
            openContentType = OpenContentType.valueOf(params.openContentType)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = OpenContent.createCriteria()
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
            results.each { OpenContent obj ->
                dataReturns.add([DT_RowId: obj.id,  0: obj.sortOrder, 1: obj.title, 2:obj.iconClass, 3:obj.activeStatus.value, 4:obj.description, 5: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def saveOrUpdate(OpenContentCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            message = errorList?.join('\n')
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        OpenContent openContent
        if (command.id) {
            openContent = OpenContent.get(command.id)
            if (!openContent) {
                message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }

            openContent.properties['title', 'sortOrder', 'iconClass', 'description'] = command.properties
            openContent.updatedBy = createOrUpdateBy
            message = 'Content Updated Successfully.'
        } else {
            openContent = new OpenContent(command.properties)
            openContent.createdBy = createOrUpdateBy
            message = 'Content Added Successfully.'
        }
        openContent.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenContent openContent = OpenContent.get(id)
        if (!openContent) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        openContent.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Content Deleted Successfully')
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenContent openContent = OpenContent.get(id)
        if (!openContent) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (openContent.activeStatus.equals(ActiveStatus.INACTIVE)) {
            openContent.activeStatus = ActiveStatus.ACTIVE
        } else {
            openContent.activeStatus = ActiveStatus.INACTIVE
        }
        openContent.updatedBy = springSecurityService.principal?.username
        openContent.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }
}
