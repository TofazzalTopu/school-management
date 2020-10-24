package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.library.OnlineBookCategoryCommand
import com.grailslab.command.library.OnlineBookCommand
import com.grailslab.library.OnlineLibrary
import com.grailslab.library.OnlineLibraryCategory
import com.grailslab.settings.FileLibrary
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class OnlineLibraryController {

    def messageSource
    def uploadService
    def onlineLibraryService

    def index() {}

    def bookCategory() {
        LinkedHashMap resultMap = onlineLibraryService.libraryList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: 'bookCategory', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: 'bookCategory', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def bookCategoryList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = onlineLibraryService.libraryList(params)

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

    def categoryBookList() {
        LinkedHashMap resultMap = onlineLibraryService.itemPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: 'categoryBookList', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: 'categoryBookList', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def sveBookCategory(OnlineBookCategoryCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = onlineLibraryService.saveOrUpdateBookCategory(command)
        String outPut = result as JSON
        render outPut
    }

    def editCategory(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OnlineLibraryCategory onlineLibraryCategory = OnlineLibraryCategory.read(id)
        if (!onlineLibraryCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, onlineLibraryCategory)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = onlineLibraryService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def saveBook(OnlineBookCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
            return
        }
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('\n')
            redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
            return
        }
        OnlineLibrary onlineLibrary
        if (command.id) {
            onlineLibrary = OnlineLibrary.get(command.id)
            if (!onlineLibrary) {
                flash.message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
                return
            }
        }
        //code for save File
        HttpServletRequest request = request
        def f = request.getFile("pdfFile")
        if(f.empty) {
            flash.message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
            return
        }
        if (!f.empty) {
            if (onlineLibrary?.link) {
                try {
                    uploadService.deleteFile(onlineLibrary?.link)
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
        }
        if (command.id) {
            onlineLibrary.properties = command.properties
        } else {
            onlineLibrary = new OnlineLibrary(command.properties)
        }
        if (!f.empty) {
            try {
                FileLibrary fileLibrary = uploadService.uploadFile(request, "pdfFile", "online_library")
                onlineLibrary.link = fileLibrary?.identifier
            } catch (Exception e) {
                log.error("Upload File Image" + e)
                flash.message = e.getMessage()
                redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
                return
            }
        }
        if(command.id) {
            flash.message='Data Updated successfully'
            redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
            return
        }
        if(onlineLibrary.hasErrors() || !onlineLibrary.save(flush: true)){
            def errorList = onlineLibrary?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('\n')
            redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
            return
        }
        flash.message ='Book Added successfully'
        redirect(action: 'categoryBookList', params: [categoryId: command.categoryId])
        return
    }

    def inactiveBook(Long id, Long categoryId) {
        if (!request.method.equals('POST')) {
            redirect(action: 'categoryBookList', params: [categoryId: categoryId])
            return
        }
        LinkedHashMap result = onlineLibraryService.inactiveBook(id)
        String outPut = result as JSON
        render outPut
    }

    def editBook(Long id, Long categoryId) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OnlineLibrary onlineLibrary = OnlineLibrary.read(id)
        if (!onlineLibrary) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, onlineLibrary)
        outPut = result as JSON
        render outPut
    }

    def downloadFile() {
        if ( params.identifier == null) {
            flash.message = "File not found."
            redirect (action:'index')
        } else {
            byte[] buffer = uploadService.getFileInByte(params.identifier, response)
            if(buffer){
                response.outputStream << buffer
                response.outputStream.flush()
            }  else {
                flash.message = "File not found."
                redirect (action:'index')
            }
        }
    }

}
