package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.command.SalAreaSetupCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import grails.converters.JSON

class AreaSetupController {

    def areaSetupService
    def schoolService

    def index() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: '/salary/areaSetup', model: [academicYearList:academicYearList, workingYear:academicYear])
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = areaSetupService.paginateList(params)
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

    def save(SalAreaSetupCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = areaSetupService.saveOrUpdate(command, params)
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
         SalArea salArea = SalArea.read(id)
        if (!salArea) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salArea.employee.id)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,salArea)
        result.put('employeeName',employee.empID +"-"+ employee.name +"-"+ employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = areaSetupService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
