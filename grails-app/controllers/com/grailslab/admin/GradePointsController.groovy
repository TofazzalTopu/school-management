package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.enums.AcaYearType
import com.grailslab.settings.GradePoint
import grails.converters.JSON

class GradePointsController {
    def gradePointService
    def classNameService
    def messageSource

    def index() {
        LinkedHashMap resultMap = gradePointService.gradePointPaginateList(params)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/gradePoint', model: [classNameList: classNameList, dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/gradePoint', model: [classNameList: classNameList, dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =gradePointService.gradePointPaginateList(params)

        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        GradePoint gradePoint = GradePoint.read(id)
        if (!gradePoint) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ, gradePoint)
        outPut = result as JSON
        render outPut
    }

    def update() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = gradePointService.updateGradePoint(params)
        String outPut = result as JSON
        render outPut
    }

}
