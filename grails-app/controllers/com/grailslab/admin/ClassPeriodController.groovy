package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.ClassPeriodCommand
import com.grailslab.settings.ClassPeriod
import grails.converters.JSON

class ClassPeriodController {
    def classPeriodService
    def messageSource
    def springSecurityService

    def index() {
        def resultMap = classPeriodService.periodList()

        if (!resultMap) {
            render(view: '/admin/classPeriod/classPeriod', model: [dataReturn: null])
            return
        }
        render(view: '/admin/classPeriod/classPeriod', model: [dataReturn: resultMap])
    }

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassPeriod classPeriod = ClassPeriod.read(id)
        if (!classPeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,classPeriod)
        outPut = result as JSON
        render outPut
    }
    /*def delete(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassPeriod classPeriod = ClassPeriod.get(id)
        if (!classPeriod) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        try {
            def clsPeriods = ClassPeriod.findAllByActiveStatusAndShiftAndSortPositionGreaterThan(ActiveStatus.ACTIVE, classPeriod.shift,classPeriod.sortPosition)
            if(clsPeriods){
                result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
                result.put(CommonUtils.MESSAGE,"You only can delete last period of ${classPeriod.shift.value}")
                outPut = result as JSON
                render outPut
                return
            }
            classPeriod.delete()
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
        LinkedHashMap result = classPeriodService.inactive(id)
        String output = result as JSON
        render output
    }

    def save(ClassPeriodCommand command) {

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
        result = classPeriodService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }
}
