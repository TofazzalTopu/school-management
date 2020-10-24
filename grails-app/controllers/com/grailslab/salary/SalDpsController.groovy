package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.hr.Employee
import grails.converters.JSON

class SalDpsController {

    def salDpsService

    def index() {
        render(view: '/salary/salaryReport/dps')
    }

    def save() {
        LinkedHashMap result = salDpsService.saveOrUpdate(params)
        String outPut = result as JSON
        render outPut
    }

    def dpsAmountList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = salDpsService.paginateList(params)
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

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        SalDps salDps = SalDps.read(id)
        if (!salDps) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salDps.employee.id)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, salDps)
        result.put('employeeName', employee.empID + "-" + employee.name + "-" + employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = salDpsService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
