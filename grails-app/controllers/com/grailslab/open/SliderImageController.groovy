package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.SliderImageCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Image
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class SliderImageController {

    def springSecurityService
    def sliderImageService
    def uploadService
    def messageSource

    def index() {
        LinkedHashMap resultMap = sliderImageService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/sliderImage/sliderList', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/sliderImage/sliderList', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = sliderImageService.paginateList(params)

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
        render(view: '/open/sliderImage/sliderCreateOrEdit')
    }
    def edit(Long id) {
        OpenSliderImage sliderImage = OpenSliderImage.read(id)
        if (!sliderImage) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/open/sliderImage/sliderCreateOrEdit', model: [sliderImage:sliderImage])
    }

    def save(SliderImageCommand command) {
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
        OpenSliderImage sliderImage
        if (command.id) {
            sliderImage = OpenSliderImage.get(command.id)
            if (!sliderImage) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            sliderImage.properties = command.properties
            sliderImage.updatedBy = createOrUpdateBy
            flash.message = 'Content Updated Successfully.'
        } else {
            sliderImage = new OpenSliderImage(command.properties)
            sliderImage.createdBy = createOrUpdateBy
            flash.message = 'Content Added Successfully.'
        }

        //code for save image
        HttpServletRequest request = request
        def f = request.getFile("pImage")
        if (!f.empty) {
            if (sliderImage.imagePath) {
                try {
                    uploadService.deleteImage(sliderImage.imagePath)
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
            try {
                Image image = uploadService.uploadImageWithThumb(request, "pImage", "homeSlider")
                sliderImage.imagePath=image?.identifier
            } catch (Exception e) {
                log.error("Upload Slider Image"+ e)
                flash.message=e.getMessage()
                redirect(action: 'index')
                return
            }
        }
        //code for save image
        if (sliderImage.hasErrors() || !sliderImage.save(flush: true)) {
            def errorList = sliderImage?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
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
        LinkedHashMap result = sliderImageService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = sliderImageService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
