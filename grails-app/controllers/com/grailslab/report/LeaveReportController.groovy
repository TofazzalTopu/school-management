package com.grailslab.report

import com.grailslab.CommonUtils
import com.grailslab.EmployeeLeaveReference
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.LeaveApplyType
import com.grailslab.enums.LeaveApproveStatus
import com.grailslab.enums.LeavePayType
import com.grailslab.enums.PayType
import com.grailslab.enums.PrintOptionType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrDesignation
import com.grailslab.hr.LeaveApply
import com.grailslab.hr.LeaveBalance
import com.grailslab.hr.LeaveName
import org.joda.time.DateTime

class LeaveReportController{

    def schoolService
    def jasperService
    def leaveService
    def employeeService
    def baseService

    private static final String LEAVE_APPLICATION_JASPER_FILE = 'leaveApplication'
    private static final String LEAVE_BY_EMPLOYEE_JASPER_FILE = 'leaveByEmployee'
    private static final String LEAVE_BY_ALL_EMPLOYEE_JASPER_FILE = 'leaveByAllEmployee'

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def leaveList = leaveService.allLeaveDropDownList()
        def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
        render(view: '/leave/leaveReport', model: [hrCategoryList: hrCategoryList, leaveList: leaveList, academicYearList: academicYearList])
    }

    def printApplication(Long id){
        LeaveApply apply = LeaveApply.read(id)
        if (!apply || apply.activeStatus != ActiveStatus.ACTIVE) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(controller: 'leaveApproval', action: 'index')
            return
        }
        String applicationBody = apply.application

        Map paramsMap = new LinkedHashMap()

        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('applicationBody', applicationBody)
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Application"
        jasperReport.reportName = LEAVE_APPLICATION_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def byEmployee(){
        AcademicYear workingYear
        if (params.year) {
            workingYear = AcademicYear.valueOf(params.year)
        } else {
            workingYear = schoolService.workingYear(null)
        }
        int year = workingYear.value.toInteger()
        DateTime reportDate = new DateTime().withYear(year)
        Date startDate = reportDate.dayOfYear().withMinimumValue().toDate()
        Date endDate = reportDate.dayOfYear().withMaximumValue().toDate()

        List employeeLeaveList = new ArrayList()
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_LEAVE, LEAVE_BY_EMPLOYEE_JASPER_FILE)
        Employee employee = Employee.read(params.getLong("employee"))
        if(employee){
            def leaveList = leaveService.listLeave(params.getLong('leaveName'))
            int leaveObtained = 0
            int leaveDue = 0
            for (leaveName in leaveList) {
                leaveObtained = leaveService.leaveConsume(employee.id, leaveName.id, startDate, endDate)
                leaveDue = leaveName.numberOfDay - leaveObtained
                employeeLeaveList.add([leaveName: leaveName.name, allowedDay: leaveName.numberOfDay, obtainLeave: leaveObtained, leaveDue: leaveDue < 0 ? 0:leaveDue])
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('employeeId', employee.empID)
        paramsMap.put('employeeName', employee.name)
        paramsMap.put('empDesignation', employee.hrDesignation?.name)
        paramsMap.put('workingYear', year)
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
//        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "leave_By_Employee"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = employeeLeaveList
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }
    def allEmpLeaveReport(){
        AcademicYear workingYear
        if (params.year) {
            workingYear = AcademicYear.valueOf(params.year)
        } else {
            workingYear = schoolService.workingYear(null)
        }
        int year = workingYear.value.toInteger()
        DateTime reportDate = new DateTime().withYear(year)
        Date startDate = reportDate.dayOfYear().withMinimumValue().toDate()
        Date endDate = reportDate.dayOfYear().withMaximumValue().toDate()

        List employeeLeaveList = new ArrayList()
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_LEAVE, LEAVE_BY_ALL_EMPLOYEE_JASPER_FILE)

        int consumeCount = 0
        int dueCount = 0
        LeaveName leaveName = LeaveName.read(params.getLong('leaveName'))
        int numberOfDay = leaveName?.numberOfDay
        HrCategory hrCategory = HrCategory.read(params.getLong('hrCategory'))
        def employeeList = employeeService.employeeList(hrCategory)
        for (employee in employeeList) {
            consumeCount = leaveService.leaveConsume(employee.id, leaveName.id, startDate, endDate)
            dueCount = numberOfDay - consumeCount
            employeeLeaveList.add([empId: employee.empID, name: employee.name, designation: employee.hrDesignation?.name, consumeCount: consumeCount, dueCount: dueCount < 0 ? 0:dueCount])
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('leaveName', leaveName.name)
        paramsMap.put('paidLeave', numberOfDay)
        paramsMap.put('workingYear', year)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "leave_By_Employee"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = employeeLeaveList
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

}
