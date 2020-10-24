package com.grailslab

import com.grailslab.command.OpenQuickLinkCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenQuickLink
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class QuickLinkService {

    def springSecurityService
    def messageSource

    static final String[] sortColumns = ['id','displayName','urlType','linkUrl','sortIndex','iconClass','activeStatus']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)

        List dataReturns = new ArrayList()
        def c = OpenQuickLink.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            /*and {
                eq("galleryType", galleryType)
            }*/
            if (sSearch) {
                or {
                    ilike('displayName', sSearch)
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
            results.each { OpenQuickLink obj ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: obj.id, 0: serial, 1: obj.displayName,2: obj.urlType, 3:obj.linkUrl, 4: obj.sortIndex, 5:obj.iconClass, 6: obj.activeStatus?.value, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def allHomeQuickLinks(){
        def c = OpenQuickLink.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("urlType", "HOME")
            }
            order('sortIndex', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def allGalleryQuickLinks(){
        def c = OpenQuickLink.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("urlType", "GALLERY")
            }
            order('sortIndex', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def saveOrUpdate(OpenQuickLinkCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        OpenQuickLink quickLink
        String message
        if (command.id) {
            quickLink = OpenQuickLink.get(command.id)
            if (!quickLink) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            quickLink.properties = command.properties
            quickLink.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Quick Link Updated successfully'
        } else {
            quickLink = new OpenQuickLink(command.properties)
            quickLink.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Quick Link Added successfully'
        }

        quickLink.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenQuickLink quickLink = OpenQuickLink.get(id)
        if (!quickLink) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (quickLink.activeStatus.equals(ActiveStatus.INACTIVE)) {
            quickLink.activeStatus = ActiveStatus.ACTIVE
        } else {
            quickLink.activeStatus = ActiveStatus.INACTIVE
        }
        quickLink.updatedBy = springSecurityService.principal?.username
        quickLink.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Quick Link Status Changed Successfully')
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenQuickLink quickLink = OpenQuickLink.get(id)
        if (!quickLink) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        quickLink.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Quick Link Deleted Successfully')
        return result
    }

}
