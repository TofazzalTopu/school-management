package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.ClassNameCommand
import com.grailslab.settings.ClassName
import grails.converters.JSON

class ClassNameController {

    def messageSource
    def classNameService
    def hrPeriodService

    def index() {
        def officeHourList = hrPeriodService.periodDropDownList()
        LinkedHashMap resultMap = classNameService.classNamePaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/className/classNameView', model: [dataReturn: null, totalCount: 0, officeHourList: officeHourList])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/className/classNameView', model: [dataReturn: resultMap.results, totalCount: totalCount, officeHourList: officeHourList])
    }

    def save(ClassNameCommand classNameCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (classNameCommand.hasErrors()) {
            def errorList = classNameCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        result = classNameService.saveOrUpdate(classNameCommand)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        LinkedHashMap result = classNameService.inactive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =classNameService.classNamePaginateList(params)

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
        ClassName className = ClassName.read(id)
        if (!className) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,className)
        outPut = result as JSON
        render outPut
    }
}
