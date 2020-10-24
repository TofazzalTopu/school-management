package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.hr.Employee
import grails.converters.JSON

class SalAdvanceController {

    def messageSource
    def salAdvanceService

    def index() {
        render(view: '/salary/salAdvanceAmount')
    }

    def save() {
        LinkedHashMap result = salAdvanceService.saveOrUpdate(params)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        SalAdvance salAdvance = SalAdvance.read(id)
        if (!salAdvance) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salAdvance.employee.id)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,salAdvance)
        result.put('employeeName',employee.empID +"-"+ employee.name +"-"+ employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def setupList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = salAdvanceService.paginateList(params)
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

    def delete(Long id) {
        LinkedHashMap result = salAdvanceService.delete(id)
        String outPut = result as JSON
        render outPut
    }

}
