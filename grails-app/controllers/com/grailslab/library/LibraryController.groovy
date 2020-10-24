package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.BookDetailsCommand
import grails.converters.JSON

class LibraryController {
    def libraryService
    def springSecurityService

    def index() {
        render (view: '/common/dashboard', layout:'moduleLibraryLayout')
    }

    def bookDetail() {
        def categoryList=libraryService.allBookCategoryDropDownList()
        render(view: 'bookDetail', model: [categoryList:categoryList])
    }

    def bookDetailList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = libraryService.bookDetailPaginateList(params)

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

    def bookDetailSave(BookDetailsCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'book')
            return
        }

        LinkedHashMap result = libraryService.bookDetailSave(command)
        String outPut = result as JSON
        render outPut
    }

    def bookDetailEdit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'book')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        BookDetails bookDetails = BookDetails.read(id)
        if (!bookDetails) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('bookAuthor', bookDetails.author.name)
        result.put('bookPublisher', bookDetails.bookPublisher?.name)
        result.put(CommonUtils.OBJ, bookDetails)
        outPut = result as JSON
        render outPut
    }

    def bookDetailInactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'book')
            return
        }

        LinkedHashMap result = libraryService.bookDetailInactive(id)
        String outPut = result as JSON
        render outPut
    }

    def categoryDelete(Long id) {
        LinkedHashMap result = libraryService.categoryDelete(id)
        String outPut = result as JSON
        render outPut
    }

    def authorDelete(Long id) {
        LinkedHashMap result = libraryService.authorDelete(id)
        String outPut = result as JSON
        render outPut
    }
}


