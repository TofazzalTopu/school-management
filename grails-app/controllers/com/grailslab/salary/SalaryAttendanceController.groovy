package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.command.SalAttendanceCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import grails.converters.JSON

class SalaryAttendanceController {
    def salaryMasterSetupService
    def salAttendanceService
    def schoolService

    def index() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def salMonthList = YearMonths.values()
        render(view: '/salary/salaryAttendance', model: [salMonthList: salMonthList, academicYearList:academicYearList, workingYear:academicYear])
    }

    def save(SalAttendanceCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = salAttendanceService.saveOrUpdate(command, params)
        String outPut = result as JSON
        render outPut
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = salAttendanceService.paginateList(params)
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
        SalAttendance salAttendance = SalAttendance.read(id)
        if (!salAttendance) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        SalMaster salMaster = SalMaster.findByAcademicYearAndYearMonthsAndSalaryStatusAndActiveStatus(salAttendance.academicYear, salAttendance.yearMonths, SalaryStatus.Disbursement, ActiveStatus.ACTIVE)
        if(salMaster){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,"Salary already disbursed. You can't edit now")
            outPut=result as JSON
            render outPut
            return
        }

        Employee employee = Employee.read(salAttendance.employee.id)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,salAttendance)
        result.put('employeeName',employee.empID +"-"+ employee.name +"-"+ employee.hrDesignation.name)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = salAttendanceService.delete(id)
        String outPut = result as JSON
        render outPut
    }

    def loadAttendance(){
        LinkedHashMap result = salAttendanceService.loadAttendance(params)
        String outPut = result as JSON
        render outPut
    }

    def attnDeleteAll(){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String message

        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
        YearMonths yearMonths = YearMonths.valueOf(params.yearMonths)
        SalMaster salMaster = SalMaster.findByAcademicYearAndYearMonthsAndSalaryStatusAndActiveStatus(academicYear, yearMonths, SalaryStatus.Disbursement, ActiveStatus.ACTIVE)
        if(salMaster){
            message="Salary already disbursed. You can't delete now"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,message)
            outPut=result as JSON
            render outPut
            return
        }
       int deleteFlag = SalAttendance.where {academicYear == academicYear && yearMonths == yearMonths}.deleteAll()
       if(!deleteFlag){
           message = "No Attendance record found for this month"
           result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
           result.put(CommonUtils.MESSAGE,message)
           outPut=result as JSON
           render outPut
           return
       }
        message=" Delete successfully"
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,message)
        outPut=result as JSON
        render outPut

    }
}
