package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.command.ExtraClassCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import grails.converters.JSON

class ExtraClassController {

    def extraClassService
    def schoolService

    def index() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        SalExtraClass salExtraClass = SalExtraClass.first()
        Double rate = salExtraClass?.rate
        render(view: '/salary/extraClass', model: [extraClassRate: rate, academicYearList:academicYearList, workingYear:academicYear])
    }

    def save(ExtraClassCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = extraClassService.saveOrUpdate(command, params)
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
        SalExtraClass salExtraClass = SalExtraClass.read(id)
        if (!salExtraClass) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salExtraClass.employee.id)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,salExtraClass)
        result.put('employeeName',employee.empID +"-"+ employee.name +"-"+ employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = extraClassService.delete(id)
        String outPut = result as JSON
        render outPut
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = extraClassService.paginateList(params)
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

}
