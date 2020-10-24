package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.HrPeriodCommand
import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.dao.DataIntegrityViolationException

class HrPeriodController {
    def springSecurityService
    def hrPeriodService
    def messageSource

    def index() {
        def resultMap = hrPeriodService.periodList()

        if (!resultMap) {
            render(view: '/hr/hrPeriod', model: [dataReturn: null])
            return
        }
        render(view: '/hr/hrPeriod', model: [dataReturn: resultMap])
    }

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        HrPeriod hrPeriod = HrPeriod.read(id)
        if (!hrPeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,hrPeriod)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = hrPeriodService.delete(id)
        String outPut = result as JSON
        render outPut
    }

    def save(HrPeriodCommand command) {

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

        result = hrPeriodService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }
}
