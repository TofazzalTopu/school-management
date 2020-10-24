package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.SuccessStoryCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Image
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class SuccessStoryController {

    def springSecurityService
    def messageSource
    def successStoryService
    def uploadService

    def index() {
        LinkedHashMap resultMap = successStoryService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/success/successList', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/success/successList', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = successStoryService.paginateList(params)

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

    def create(){
        render(view: '/open/success/successCreateOrEdit')
    }

    def edit(Long id) {
        OpenSuccessStory successStory = OpenSuccessStory.read(id)
        if (!successStory) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/open/success/successCreateOrEdit', model: [successStory:successStory])
    }

    def save(SuccessStoryCommand command) {
        if (!request.method.equals('POST')) {
            flash.message="Request Method not allow here."
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message=errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        OpenSuccessStory successStory
        if (command.id) {
            successStory = OpenSuccessStory.get(command.id)
            if (!successStory) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            successStory.properties = command.properties
            successStory.updatedBy = createOrUpdateBy
            flash.message = 'Content Updated Successfully.'
        } else {
            successStory = new OpenSuccessStory(command.properties)
            successStory.createdBy = createOrUpdateBy
            successStory.academicYear = academicYear
            flash.message = 'Content Added Successfully.'
        }

        //code for save image
        HttpServletRequest request = request
        def f = request.getFile("pImage")
        if (!f.empty) {
            if (successStory.imagePath) {
                try {
                    uploadService.deleteImage(successStory.imagePath)
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
            try {
                Image image = uploadService.uploadImageWithThumb(request, "pImage", "successStory")
                successStory.imagePath=image?.identifier
            } catch (Exception e) {
                log.error("Upload MgmgVoice Image"+ e)
                flash.message=e.getMessage()
                redirect(action: 'index')
                return
            }
        }
        //code for save image
        if (successStory.hasErrors() || !successStory.save(flush: true)) {
            def errorList = successStory?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        redirect(action: 'index')
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = successStoryService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = successStoryService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
