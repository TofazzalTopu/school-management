package com.grailslab

import com.grailslab.enums.SliderType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.open.OpenSliderImage
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SliderImageService {
    def springSecurityService
    def uploadService
    def messageSource
    def grailsApplication

    static final String[] sortColumns = ['sortIndex','title','activeStatus']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = OpenSliderImage.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            if (sSearch) {
                or {
                    ilike('title', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        String imagePathThumb
        if (totalCount > 0) {
            def g = grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
            results.each { OpenSliderImage obj ->
                imagePathThumb = '<img src="'+g.createLink(controller: 'image', action: 'streamImageFromIdentifierThumb', params: ['imagePath': obj?.imagePath], absolute: true)+'" alt="Blank" style="width:45px;height:45px;">'
                dataReturns.add([DT_RowId: obj.id, 0: obj.sortIndex,1:imagePathThumb, 2: obj.title, 3: obj.description, 4: obj.activeStatus?.value, 5: obj.type, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    List<OpenSliderImage> sliderImageList(SliderType sliderType) {
        List<OpenSliderImage> sliderImageList = OpenSliderImage.findAllByActiveStatusAndType(ActiveStatus.ACTIVE, sliderType, [max: 8])
        if (!sliderImageList) {
            sliderImageList = OpenSliderImage.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: 8])
        }
        return sliderImageList
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        OpenSliderImage sliderImage = OpenSliderImage.get(id)
        if (!sliderImage) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (sliderImage.activeStatus.equals(ActiveStatus.INACTIVE)) {
            sliderImage.activeStatus = ActiveStatus.ACTIVE
        } else {
            sliderImage.activeStatus = ActiveStatus.INACTIVE
        }
        sliderImage.updatedBy = springSecurityService.principal?.username
        sliderImage.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenSliderImage sliderImage = OpenSliderImage.get(id)
        if (!sliderImage) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        uploadService.deleteImage(sliderImage.imagePath)
        sliderImage.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Content Deleted Successfully')
        return result
    }
}


