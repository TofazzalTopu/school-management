package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.LessonWeekCommand
import com.grailslab.settings.LessonWeek
import grails.converters.JSON

class LessonWeekController {

    def lessonService
    def messageSource

    def index() {

        redirect(action: 'week')
    }

    def week(){
        LinkedHashMap resultMap = lessonService.lessonWeekPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/teacher/lesson/lessonWeek', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/teacher/lesson/lessonWeek', model: [dataReturn: resultMap.results, totalCount: totalCount])

    }

    def weekList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = lessonService.lessonWeekPaginateList(params)

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

    def editWeek(Long id) {
        if (!request.method == 'POST') {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        LessonWeek lessonWeek = LessonWeek.read(id)
        if (!lessonWeek) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        String startDate = CommonUtils.getUiDateStrForCalenderDateEdit(lessonWeek.startDate);
        String endDate = CommonUtils.getUiDateStrForCalenderDateEdit(lessonWeek.endDate);
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('startDate', startDate)
        result.put('endDate', endDate)
        result.put(CommonUtils.OBJ, lessonWeek)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = lessonService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def saveWeek(LessonWeekCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }

        result = lessonService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }
}
