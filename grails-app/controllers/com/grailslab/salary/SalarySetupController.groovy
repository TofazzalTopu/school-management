package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.SalAdvanceService
import com.grailslab.command.SalConfigurationCommand
import com.grailslab.command.SalarySetupCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import grails.converters.JSON

class SalarySetupController {

    def salarySetUpService
    def salaryMasterSetupService
    SalAdvanceService salAdvanceService
    def schoolService


    def index() {
        render(view: '/salary/salarySetup')
    }

    def salMasterSetup() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: '/salary/masterSetup', model: [academicYearList:academicYearList, workingYear:academicYear])
    }

    def masterSetupList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = salaryMasterSetupService.paginateList(params)
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

    def footNoteEdit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String footNote
        SalMaster master = SalMaster.read(id)
        if (!master) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        if (master.footNote) {
            footNote = master?.footNote
        } else {
            footNote = ""
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, master)
        result.put('footNote', footNote)
        result.put('ID', id)
        outPut = result as JSON
        render outPut
    }

    def saveFootNote() {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = salarySetUpService.saveFootNote(params)
        String outPut = result as JSON
        render outPut
    }

    def masterSetupSave() {
        if (!request.method.equals('POST')) {
            redirect(action: 'salMasterSetup')
            return
        }
        LinkedHashMap result = salarySetUpService.masterSetupSave(params)
        String outPut = result as JSON
        render outPut
    }

    def disburseSalary() {
        if (!request.method.equals('POST')) {
            redirect(action: 'salMasterSetup')
            return
        }
        LinkedHashMap result = salarySetUpService.disburseSalary(params)
        String outPut = result as JSON
        render outPut
    }

    def masterDelete(Long id) {
        LinkedHashMap result = salarySetUpService.masterDelete(id)
        String outPut = result as JSON
        render outPut
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = salarySetUpService.paginateList(params)
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

    def save(SalarySetupCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = salarySetUpService.saveOrUpdate(command, params)
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
        SalSetup salSetup = SalSetup.read(id)
        if (!salSetup) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(salSetup.employee.id)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, salSetup)
        result.put('employeeName', employee.empID + "-" + employee.name + "-" + employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = salarySetUpService.delete(id)
        String outPut = result as JSON
        render outPut
    }



    def empSalarySetupData(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        SalSetup salSetup = salarySetUpService.salSetupByEmployee(id)
        if (!salSetup) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Employee Salary setup not added. Please add employee salary setup first")
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, salSetup)
        outPut = result as JSON
        render outPut
    }

    def salConfigData() {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        SalConfiguration salConfiguration = SalConfiguration.first()
        if (!salConfiguration) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, salConfiguration)
        outPut = result as JSON
        render outPut
    }

    def saveConfig(SalConfigurationCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = salarySetUpService.saveConfig(command, params)
        String outPut = result as JSON
        render outPut
    }
}
