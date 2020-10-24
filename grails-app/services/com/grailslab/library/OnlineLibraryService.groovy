package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.library.OnlineBookCategoryCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class OnlineLibraryService {

    def messageSource

    static final String[] sortColumns = ['id', 'categoryName','sortOrder', 'activeStatus', 'description']

    LinkedHashMap libraryList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)

        List dataReturns = new ArrayList()
        def c = OnlineLibraryCategory.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            if (sSearch) {
                or {
                    ilike('categoryName', sSearch)
                    ilike('description', sSearch)
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
            results.each { OnlineLibraryCategory onlineLibraryCategory ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([
                        DT_RowId: onlineLibraryCategory.id,
                        0: serial,
                        1: onlineLibraryCategory.categoryName,
                        2: onlineLibraryCategory.sortOrder,
                        3: onlineLibraryCategory.activeStatus?.value,
                        4: onlineLibraryCategory.description,
                        5: ''
                ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    static final String[] sortColumnsBook = ['bookName']
    LinkedHashMap itemPaginateList(GrailsParameterMap params) {
        int iDisplayStart = CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = CommonUtils.MAX_PAGINATION_LENGTH
        String sSortDir = CommonUtils.SORT_ORDER_ASC
        int iSortingCol = CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        OnlineLibraryCategory onlineLibraryCategory
        if(params.categoryId){
            onlineLibraryCategory = OnlineLibraryCategory.read(params.getLong('categoryId'))
        }
        if(!onlineLibraryCategory){
            return null
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsBook, iSortingCol)
        List dataReturns = new ArrayList()
        def c = OnlineLibrary.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("categoryId", onlineLibraryCategory.id)
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { OnlineLibrary onlineLibrary ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([
                        DT_RowId: onlineLibrary.id,
                        0: serial,
                        1: onlineLibrary.bookName,
                        2: onlineLibrary.link,
                        3: onlineLibrary.activeStatus?.value,
                        4: onlineLibrary.description,
                        5: ''
                ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdateBookCategory(OnlineBookCategoryCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        OnlineLibraryCategory onlineLibraryCategory
        String message
        def refAlreadyExist
        if (command.id) {
            refAlreadyExist = OnlineLibraryCategory.findByCategoryNameAndIdNotEqual(command.categoryName, command.id)
        } else {
            refAlreadyExist = OnlineLibraryCategory.findByCategoryName(command.categoryName)
        }
        if (refAlreadyExist) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Category already exist.")
            return result
        }

        if (command.id) {
            onlineLibraryCategory = OnlineLibraryCategory.get(command.id)
            if (!onlineLibraryCategory) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            onlineLibraryCategory.properties = command.properties
            onlineLibraryCategory.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Category Updated successfully'
        } else {
            onlineLibraryCategory = new OnlineLibraryCategory(command.properties)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Category Added successfully'
        }
        onlineLibraryCategory.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OnlineLibraryCategory onlineLibraryCategory = OnlineLibraryCategory.get(id)
        if (!onlineLibraryCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        String successMsg
        if (onlineLibraryCategory.activeStatus.equals(ActiveStatus.ACTIVE)) {
            onlineLibraryCategory.activeStatus = ActiveStatus.INACTIVE
            successMsg = "Category Inactive Successfully."
        } else if (onlineLibraryCategory.activeStatus.equals(ActiveStatus.INACTIVE)) {
            onlineLibraryCategory.activeStatus = ActiveStatus.ACTIVE
            successMsg = "Category Active Successfully"
        }
        onlineLibraryCategory.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, successMsg)
        return result
    }

    def inactiveBook(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OnlineLibrary onlineLibrary = OnlineLibrary.get(id)
        if (!onlineLibrary) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        String successMsg
        if (onlineLibrary.activeStatus.equals(ActiveStatus.ACTIVE)) {
            onlineLibrary.activeStatus = ActiveStatus.INACTIVE
            successMsg = "Book Inactive Successfully."
        } else if (onlineLibrary.activeStatus.equals(ActiveStatus.INACTIVE)) {
            onlineLibrary.activeStatus = ActiveStatus.ACTIVE
            successMsg = "Book Active Successfully"
        }
        onlineLibrary.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, successMsg)
        return result
    }
}
