package com.grailslab.attn

import com.grailslab.CommonUtils
import com.grailslab.command.AttendancePeriodCommand
import com.grailslab.enums.Shift
import grails.converters.JSON

class AttnPeriodController {

    def attnPeriodService

    def index() {
        params.attnType = "STUDENT"
        params.attnTypeName = "ALL"
        LinkedHashMap resultMap = attnPeriodService.periodPaginateList(params)
        def shift = Shift.values()
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/attnPeriod/index', model: [dataReturn: null, shift: shift, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/attnPeriod/index', model: [dataReturn: resultMap.results, shift: shift, totalCount: totalCount])
    }

    def teacher() {
        params.attnType = "TEACHER"
        params.attnTypeName = "ALL"
        LinkedHashMap resultMap = attnPeriodService.periodPaginateList(params)
        def teacherType = attnPeriodService.teacherType()

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/attnPeriod/teacher', model: [dataReturn: null, teacherType: teacherType, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/attnPeriod/teacher', model: [dataReturn: resultMap.results, teacherType: teacherType, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =attnPeriodService.periodPaginateList(params)

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

    def save(AttendancePeriodCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnPeriodService.saveOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        AttendancePeriod attendancePeriod = AttendancePeriod.read(id)
        if (!attendancePeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,attendancePeriod)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id){
        LinkedHashMap result = attnPeriodService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
