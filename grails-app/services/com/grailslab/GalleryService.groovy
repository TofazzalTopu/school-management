package com.grailslab


import com.grailslab.command.OpenGalleryCommand
import com.grailslab.enums.OpenContentType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenGallery
import com.grailslab.open.OpenGalleryItem
import com.grailslab.settings.Image
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class GalleryService {

    def springSecurityService
    def messageSource
    def schoolService

    static final String[] sortColumns = ['id', 'name','sortOrder', 'activeStatus', 'description']

    LinkedHashMap albumPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)

        OpenContentType galleryType = OpenContentType.PICTURE
        if(params.albumType){
            galleryType = OpenContentType.valueOf(params.albumType)
        }
        List dataReturns = new ArrayList()
        def c = OpenGallery.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("galleryType", galleryType)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('description', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { OpenGallery openGallery ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: openGallery.id, 0: serial, 1: openGallery.name, 2: openGallery.sortOrder, 3: openGallery.activeStatus?.value, 4: openGallery.description, 5: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    static final String[] sortColumnsImg = ['sortOrder', 'imageTitle']
    LinkedHashMap itemPaginateList(GrailsParameterMap params) {
        int iDisplayStart = CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = CommonUtils.MAX_PAGINATION_LENGTH
        String sSortDir = CommonUtils.SORT_ORDER_ASC
        int iSortingCol = CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        OpenGallery openGallery
        if(params.album){
            openGallery = OpenGallery.read(params.getLong('album'))
        }
        if(!openGallery){
            return null
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsImg, iSortingCol)
        List dataReturns = new ArrayList()
        def c = OpenGalleryItem.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("openGallery", openGallery)
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { OpenGalleryItem galleryItem ->
                dataReturns.add([DT_RowId: galleryItem.id, 0: galleryItem.itemPath, 1: galleryItem.name, 2: galleryItem.sortOrder, 3: galleryItem.activeStatus?.value, 4:galleryItem.description, 5: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    Boolean deleteImage(String imagePath) {
        Image image = Image.findByIdentifier(imagePath)
        Boolean successStatus = Boolean.FALSE
        if (!image) {
            return successStatus
        }

        def absolutePath = schoolService.storageImagePath(image.imagePath)
        def absolutePathThumb = schoolService.storageImagePath(image.imagePathThumb)
        try {
            File fileImage = new File(absolutePath)
            File fileImageThumb = new File(absolutePathThumb)

            if (fileImage.delete() && fileImageThumb.delete()) {
                image.delete()
                successStatus = Boolean.TRUE
            } else {
                return successStatus
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return successStatus
    }

    def imageList() {
        def c = OpenGallery.createCriteria()
        def results = c.list() {
            and {
                eq("galleryType", OpenContentType.PICTURE)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        OpenGalleryItem imageItem
        results.each { OpenGallery gallery ->
            imageItem = firstImageItem(gallery)
            if(imageItem){
                dataReturns.add([DT_RowId: gallery.id, imagePath: imageItem.itemPath, name: gallery.name, description:gallery.description])
            }
        }
        return dataReturns
    }

    OpenGalleryItem firstImageItem(OpenGallery openGallery) {
        OpenGalleryItem galleryItem = OpenGalleryItem.findByActiveStatusAndOpenGallery(ActiveStatus.ACTIVE, openGallery)
        return galleryItem
    }

    def videoList() {
        def c = OpenGallery.createCriteria()
        def results = c.list() {
            and {
                eq("galleryType", OpenContentType.VIDEO)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        OpenGalleryItem videoItem
        results.each { OpenGallery gallery ->
            videoItem = firstVideoItem(gallery)
            if(videoItem){
                dataReturns.add([DT_RowId: gallery.id, imagePath: videoItem.itemPath, name: gallery.name, description:gallery.description])
            }
        }
        return dataReturns
    }

    OpenGalleryItem firstVideoItem(OpenGallery openGallery) {
        OpenGalleryItem galleryItem = OpenGalleryItem.findByActiveStatusAndOpenGallery(ActiveStatus.ACTIVE, openGallery)
        return galleryItem
    }

    def saveGallery(OpenGalleryCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        OpenGallery openGallery
        String message
        def refAlreadyExist
        if (command.id) {
            refAlreadyExist = OpenGallery.findByNameAndIdNotEqual(command.name, command.id)
        } else {
            refAlreadyExist = OpenGallery.findByName(command.name)
        }

        if (refAlreadyExist) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Album already exist.")
            return result
        }

        if (command.id) {
            openGallery = OpenGallery.get(command.id)
            if (!openGallery) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            openGallery.properties = command.properties
            openGallery.updatedBy = createOrUpdateBy
            openGallery.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Album Updated successfully'
        } else {
            openGallery = new OpenGallery(command.properties)
            openGallery.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Album Added successfully'
        }
        openGallery.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenGallery openGallery = OpenGallery.get(id)
        if (!openGallery) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        String successMsg
        if (openGallery.activeStatus.equals(ActiveStatus.ACTIVE)) {
            openGallery.activeStatus = ActiveStatus.INACTIVE
            successMsg = "Album Inactive Successfully."
        } else if (openGallery.activeStatus.equals(ActiveStatus.INACTIVE)) {
            openGallery.activeStatus = ActiveStatus.ACTIVE
            successMsg = "Album Active Successfully"
        }
        openGallery.updatedBy = springSecurityService.principal?.username
        openGallery.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, successMsg)
        return result
    }

    def inactiveItem(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenGalleryItem galleryItem = OpenGalleryItem.get(id)
        if (!galleryItem) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        String message
        if (galleryItem.activeStatus.equals(ActiveStatus.ACTIVE)) {
            galleryItem.activeStatus = ActiveStatus.INACTIVE
            message = "Item Inactivate successfully"
        } else if (galleryItem.activeStatus.equals(ActiveStatus.INACTIVE)) {
            galleryItem.activeStatus = ActiveStatus.ACTIVE
            message = "Item activated successfully"
        }
        galleryItem.updatedBy = springSecurityService.principal?.username
        galleryItem.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}
