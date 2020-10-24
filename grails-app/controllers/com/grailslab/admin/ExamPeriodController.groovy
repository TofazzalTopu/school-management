package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.ExamPeriodCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ExamPeriod
import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ExamPeriodController {
    def examPeriodService
    def messageSource
    def schoolService

    def index() {
        def resultMap = examPeriodService.periodList()

        if (!resultMap) {
            render(view: '/admin/examPeriod/examPeriod', model: [dataReturn: null])
            return
        }
        render(view: '/admin/examPeriod/examPeriod', model: [dataReturn: resultMap])
    }

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ExamPeriod examPeriod = ExamPeriod.read(id)
        if (!examPeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,examPeriod)
        outPut = result as JSON
        render outPut
    }

   /* def delete(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ExamPeriod examPeriod = ExamPeriod.get(id)
        if (!examPeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        try {
            examPeriod.delete()
        }
        catch(DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,"Period already in use. Please delete reference first")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,"Period deleted successfully.")
        outPut = result as JSON
        render outPut

    }*/

    def inactive(Long id) {
        LinkedHashMap result = examPeriodService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def save(ExamPeriodCommand command) {
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

        result = examPeriodService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }
}
