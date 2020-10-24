package com.grailslab.common


import com.grailslab.CommonUtils
import com.grailslab.command.LeaveApplyCommand
import com.grailslab.enums.AvailableRoles
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.hr.LeaveApply
import com.grailslab.report.CusJasperReportDef
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import org.joda.time.DateTime

class LeaveController {
    def leaveService
    def schoolService
    def jasperService
    def baseService

    def index() {
        def leaveList = leaveService.allLeaveDropDownList()
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.SUPER_ADMIN.value())) {
            render(view: 'index', layout: 'adminLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.ADMIN.value())) {
            render(view: 'index', layout: 'adminLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.ACCOUNTS.value())) {
            render(view: 'index', layout: 'moduleCollectionLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.LIBRARY.value())) {
            render(view: 'index', layout: 'moduleLibraryLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.HR.value())) {
            render(view: 'index', layout: 'moduleHRLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.TEACHER.value())) {
            render(view: 'index', layout: 'adminLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.ORGANIZER.value())) {
            render(view: 'index', layout: 'moduleWebLayout', model: [leaveList: leaveList])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.SCHOOL_HEAD.value())) {
            render(view: 'index', layout: 'adminLayout', model: [leaveList: leaveList])
            return
        }
    }

    def save(LeaveApplyCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = leaveService.savOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }


    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = leaveService.leaveEmployeePaginateList(params)

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
        LeaveApply apply = LeaveApply.read(id)
        if (!apply) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        Employee employee = apply.employee
        Employee replacement
        if (apply.supportedBy) {
            replacement = Employee.findByEmpID(apply.supportedBy)
        }
        String employeeDetails = employee?.empID + ' - ' + employee?.name
        String replacementBy = replacement?.empID + ' - ' + replacement?.name
        String leaveName = apply?.leaveName?.name
        String reason = apply?.reason
        String argentContactNo = apply?.argentContactNo
        result.put('employeeDetails', employeeDetails)
        result.put('replacementBy', replacementBy)
        result.put('employee', employee?.name)
        result.put('supportedBy', employee?.name)

        /*result.put('places', places)*/
        result.put('leaveName', leaveName)
        result.put('reason', reason)
        result.put('argentContactNo', argentContactNo)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, apply)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = leaveService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def leaveBalance(){
        AcademicYear workingYear = schoolService.workingYear(null)

        int year = workingYear.value.toInteger()
        DateTime reportDate = new DateTime().withYear(year)
        Date startDate = reportDate.dayOfYear().withMinimumValue().toDate()
        Date endDate = reportDate.dayOfYear().withMaximumValue().toDate()

        List employeeLeaveList = new ArrayList()
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_LEAVE, EMPLOYEE_LEAVE_BALANCE_JASPER_FILE)

        Employee employee = schoolService.loggedInEmployee()
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
        jasperReport.outputFilename = "employee_leave_balance"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
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
    private static final String EMPLOYEE_LEAVE_BALANCE_JASPER_FILE = 'employeeLeaveBalance'
}
