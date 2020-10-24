package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.BookCategoryCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class CategoryService {

    def springSecurityService
    def messageSource

    static final String[] sortColumns = ['name']

    LinkedHashMap categoryPaginateList(GrailsParameterMap params) {
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
        def c = BookCategory.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { BookCategory category ->
                dataReturns.add([DT_RowId: category.id, 0: category.name, 1: category.description ?: "", 2: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(BookCategoryCommand command) {

        LinkedHashMap result = new LinkedHashMap()

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        BookCategory bookCategory
        String message
        if (command.id) {
            bookCategory = BookCategory.get(command.id)
            if (!bookCategory) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            bookCategory.properties = command.properties
            bookCategory.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'BookDetails Category Updated successfully'
        } else {
            bookCategory = new BookCategory(command.properties)
            bookCategory.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'BookDetails Category Added successfully'
        }
        bookCategory.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        result.put("bookCategory", bookCategory)
        return result

    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BookCategory category = BookCategory.get(id)
        if (!category) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        category.activeStatus = ActiveStatus.INACTIVE
        category.updatedBy = springSecurityService.principal?.username
        category.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Category deleted successfully')
        return result
    }
}
