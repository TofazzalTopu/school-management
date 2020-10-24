package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.BookCategoryCommand
import grails.converters.JSON

class BookCategoryController {

    def categoryService

    def index() {
        LinkedHashMap resultMap = categoryService.categoryPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/library/category', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/library/category', model: [dataReturn: resultMap.results, totalCount: totalCount])

    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = categoryService.categoryPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def save(BookCategoryCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = categoryService.saveOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        BookCategory category = BookCategory.read(id)
        if (!category) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, category)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = categoryService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
