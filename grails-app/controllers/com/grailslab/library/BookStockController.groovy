package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.AddBooksCommand
import com.grailslab.enums.BookStockStatus
import grails.converters.JSON

class BookStockController {
    def bookStockService
    def libraryService

    def index() {
        def categoryList = libraryService.allBookCategoryDropDownList()
        def bookStatusList = BookStockStatus.values()
        render(view: '/library/addBook', model: [categoryList: categoryList, bookStatusList: bookStatusList])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = bookStockService.addBooksPaginateList(params)

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

    def save(AddBooksCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = bookStockService.saveOrUpdate(command)
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
        Book book = Book.read(id)
        if (!book) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('bookName', book.bookDetails.name)
        result.put(CommonUtils.OBJ, book)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = bookStockService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = bookStockService.delete(id)
        String outPut = result as JSON
        render outPut
    }

}


