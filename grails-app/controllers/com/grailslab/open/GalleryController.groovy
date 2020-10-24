package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.GalleryItemCommand
import com.grailslab.command.OpenGalleryCommand
import com.grailslab.enums.OpenContentType
import com.grailslab.settings.Image
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class GalleryController {

    def springSecurityService
    def galleryService
    def messageSource
    def uploadService

    def index() {
        redirect(action: 'picture')
    }

    def picture(){
        params.albumType = OpenContentType.PICTURE.key
        LinkedHashMap resultMap = galleryService.albumPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/gallery/admin/pictureAlbum', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/gallery/admin/pictureAlbum', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def video(){
        params.albumType = OpenContentType.VIDEO.key
        LinkedHashMap resultMap = galleryService.albumPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/gallery/admin/videoAlbum', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/gallery/admin/videoAlbum', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def albumList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = galleryService.albumPaginateList(params)

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

    def saveGallery(OpenGalleryCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = galleryService.saveGallery(command)
        String outPut = result as JSON
        render outPut
    }

    def editGallery(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenGallery openGallery = OpenGallery.read(id)
        if (!openGallery) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, openGallery)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = galleryService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def pictureItem(){
        LinkedHashMap resultMap = galleryService.itemPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/gallery/admin/pictureItem', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/gallery/admin/pictureItem', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def videoItem(){
        LinkedHashMap resultMap = galleryService.itemPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/gallery/admin/videoItem', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/gallery/admin/videoItem', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def itemList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = galleryService.itemPaginateList(params)

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

    def saveItem(GalleryItemCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'imageGallery')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut=result as JSON
            render outPut
            return
        }
        OpenGalleryItem galleryItem
        String message
        Image image

        if (command.id) {
            galleryItem = OpenGalleryItem.get(command.id)
            if (!galleryItem) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                outPut=result as JSON
                render outPut
                return
            }
            galleryItem.properties['openGallery','name','description','sortOrder'] = command.properties
            galleryItem.updatedBy = springSecurityService.principal?.username
            if(command.galleryType==OpenContentType.PICTURE){
                HttpServletRequest request = request
                def f = request.getFile("pImage")
                message ='Picture Updated successfully'
                if (!f.empty) {
                    if (galleryItem.itemPath) {
                        try {
                            Boolean deleteStatus = galleryService.deleteImage(galleryItem.itemPath)
                        } catch (Exception e) {
                            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                            result.put(CommonUtils.MESSAGE, e.toString())
                            outPut = result as JSON
                            render outPut
                            return
                        }
                    }
                    try {
                        image = uploadService.uploadImageWithThumb(request, "pImage", "galleryItem")
                        galleryItem.itemPath = image?.identifier
                    } catch (Exception e) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, e.toString())
                        outPut = result as JSON
                        render outPut
                        return
                    }
                }
            }else {
                message ='Video Link Updated successfully'
                galleryItem.itemPath = command.itemPath
            }
            galleryItem.save(flush: true)
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)

        } else {
            galleryItem = new OpenGalleryItem(command.properties)
            galleryItem.createdBy = springSecurityService.principal?.username
            if(command.galleryType==OpenContentType.PICTURE){
                HttpServletRequest request = request
                message ='Picture Added successfully'
                def f = request.getFile("pImage")
                if (!f.empty) {
                    try {
                        image = uploadService.uploadImageWithThumb(request, "pImage", "galleryItem")
                        galleryItem.itemPath = image?.identifier

                    } catch (Exception e) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, e.toString())
                        outPut = result as JSON
                        render outPut
                        return
                    }
                }
            }else {
                message ='Video Link Added successfully'
                galleryItem.itemPath = command.itemPath
            }
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        }

        if(galleryItem.hasErrors() || !galleryItem.save(flush: true)){
            def errorList = galleryItem?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            message = errorList?.join('\n')
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        }
        result.put(CommonUtils.MESSAGE,message)
        outPut=result as JSON
        render outPut
    }

    def editItem(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenGalleryItem galleryItem = OpenGalleryItem.read(id)
        if (!galleryItem) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, galleryItem)
        outPut = result as JSON
        render outPut
    }

    def inactiveItem(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = galleryService.inactiveItem(id)
        String outPut = result as JSON
        render outPut
    }

    /*def deleteItem(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenGalleryItem galleryItem = OpenGalleryItem.get(id)
        if (!galleryItem) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            Boolean deleteStatus = galleryService.deleteImage(galleryItem.imagePath)
            galleryItem.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Data Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Data already in use. You Can Inactive Reference")
        }
        outPut = result as JSON
        render outPut
    }*/
}
