package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import grails.converters.JSON

class SalPcController {

    def messageSource
    def salPcService


    def index() {
        render(view: '/salary/salPc')
    }

    def save() {
        LinkedHashMap result = salPcService.saveOrUpdate(params)
        String outPut = result as JSON
        render outPut
    }

    def setupPcList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = salPcService.paginateList(params)
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
        SalPc salPc = SalPc.read(id)
        if (!salPc) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salPc.employee.id)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,salPc)
        result.put('employeeName',employee.empID +"-"+ employee.name +"-"+ employee.hrDesignation.name)
        outPut = result as JSON
        render outPut

    }

    def delete(Long id){
        LinkedHashMap result = salPcService.delete(id)
        String outPut = result as JSON
        render outPut
    }

}
