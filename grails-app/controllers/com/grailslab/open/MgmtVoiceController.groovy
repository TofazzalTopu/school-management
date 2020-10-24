package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.MgmtVoiceCommand
import com.grailslab.enums.OpenContentType
import com.grailslab.settings.Image
import grails.converters.JSON
import org.apache.commons.lang.StringUtils

import javax.servlet.http.HttpServletRequest

class MgmtVoiceController {

    def springSecurityService
    def messageSource
    def mgmtVoiceService
    def uploadService

    def index() {
        def openContentTypes = OpenContentType.mgmtVoiceContents()
        LinkedHashMap resultMap = mgmtVoiceService.featurePaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/mgmtVoice/voice', model: [dataReturn: null, totalCount: 0, openContentTypes: openContentTypes])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/mgmtVoice/voice', model: [dataReturn: resultMap.results, totalCount: totalCount, openContentTypes: openContentTypes])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = mgmtVoiceService.featurePaginateList(params)

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
        if(!params.featureType){
            redirect(action: 'index')
            return
        }
        OpenContentType openContentType = OpenContentType.valueOf(params.featureType)
        if(!openContentType){
            redirect(action: 'index')
            return
        }
        render(view: '/open/mgmtVoice/createOrEdit', model: [openContentType:openContentType.key])
    }

    def edit(Long id) {
        OpenMgmtVoice mgmtVoice = OpenMgmtVoice.read(id)
        if (!mgmtVoice) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/open/mgmtVoice/createOrEdit', model: [mgmtVoice:mgmtVoice])
    }

    def save(MgmtVoiceCommand command) {

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
        if(command.description){
            command.description = StringUtils.removeStart(command.description, '<p>')
            command.description = StringUtils.removeEnd(command.description,'</p>')
        }
        String createOrUpdateBy = springSecurityService?.principal.username
        OpenMgmtVoice mgmtVoice
        if (command.id) {
            mgmtVoice = OpenMgmtVoice.get(command.id)
            if (!mgmtVoice) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            mgmtVoice.properties['title', 'name','sortOrder','iconClass','description'] = command.properties
            mgmtVoice.updatedBy = createOrUpdateBy
            flash.message = 'Content Updated Successfully.'
        } else {
            mgmtVoice = new OpenMgmtVoice(command.properties)
            mgmtVoice.createdBy = createOrUpdateBy
            flash.message = 'Content Added Successfully.'
        }

        //code for save image
        HttpServletRequest request = request
        def f = request.getFile("pImage")
        if (!f.empty) {
            if (mgmtVoice.imagePath) {
                try {
                    Boolean deleteStatus = uploadService.deleteImage(mgmtVoice.imagePath)
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
            try {
                Image image = uploadService.uploadImageWithThumb(request, "pImage", "mgmtVoice")
                mgmtVoice.imagePath=image?.identifier
            } catch (Exception e) {
                log.error("Upload MgmgVoice Image"+ e)
                flash.message=e.getMessage()
                redirect(action: 'index')
                return
            }
        }
        //code for save image
        if (mgmtVoice.hasErrors() || !mgmtVoice.save(flush: true)) {
            def errorList = mgmtVoice?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
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
        LinkedHashMap result = mgmtVoiceService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = mgmtVoiceService.delete(id)
        String outPut = result as JSON
        render outPut
    }

}
