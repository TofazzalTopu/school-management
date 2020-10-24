package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.NoticeBoardCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.FileLibrary
import com.grailslab.settings.NoticeBoard
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class NoticeBoardController {
    def springSecurityService
    def noticeBoardService
    def messageSource
    def uploadService

    def index() {
        LinkedHashMap resultMap = noticeBoardService.noticeBoardPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/notice/noticeBoard', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/notice/noticeBoard', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = noticeBoardService.noticeBoardPaginateList(params)

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

    def save(NoticeBoardCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        NoticeBoard noticeBoard
        String createOrUpdateBy = springSecurityService.principal.username
        AcademicYear academicYear = springSecurityService.principal.academicYear
        //code for save File
        HttpServletRequest request = request
        def f = request.getFile("pdfFile")

        if (command.id) {
            noticeBoard = NoticeBoard.get(command.id)
            if (!noticeBoard) {
                flash.message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            if (!f.empty) {
                try {
                    if (noticeBoard.fileLink) {
                        uploadService.deleteFile(noticeBoard.fileLink)
                    }
                    FileLibrary fileLibrary = uploadService.uploadFile(request, "pdfFile", "notice")
                    noticeBoard.fileLink = fileLibrary?.identifier
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
            noticeBoard.updatedBy = createOrUpdateBy
            noticeBoard.properties = command.properties
            flash.message='Data Updated successfully'
        } else {
            noticeBoard = new NoticeBoard(command.properties)
            noticeBoard.academicYear = academicYear
            noticeBoard.createdBy = createOrUpdateBy
            if (!f.empty) {
                try {
                    FileLibrary fileLibrary = uploadService.uploadFile(request, "pdfFile", "notice")
                    noticeBoard.fileLink = fileLibrary?.identifier
                } catch (Exception e) {
                    log.error("Upload Slider Image" + e)
                    flash.message = e.getMessage()
                    redirect(action: 'index')
                    return
                }
            }
            flash.message ='Data Added successfully'
        }
        if(noticeBoard.hasErrors() || !noticeBoard.save(flush: true)){
            def errorList = noticeBoard?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        redirect(action: 'index')
        return
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        NoticeBoard noticeBoard = NoticeBoard.read(id)
        if (!noticeBoard) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, noticeBoard)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        LinkedHashMap result = noticeBoardService.inactive(id)
        String output = result as JSON
        render output
    }

    def delete(Long id) {
        LinkedHashMap result = noticeBoardService.delete(id)
        String output = result as JSON
        render output
    }
}
