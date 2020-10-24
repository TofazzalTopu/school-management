package com.grailslab.report

import com.grailslab.CommonUtils
import com.grailslab.command.ExtraClassReportCommand
import com.grailslab.command.SalAttendanceReportCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrCategory
import com.grailslab.salary.BonusMaster
import com.grailslab.salary.SalConfiguration
import com.grailslab.salary.SalMaster
import grails.converters.JSON

class SalaryReportController {
    def classNameService
    def messageSource
    def schoolService
    def sectionService
    def jasperService
    def studentService
    def employeeService
    def baseService


    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
        render(view: '/salary/salaryReport/salarySetEmpReport',model: [hrCategoryList: hrCategoryList, academicYearList: academicYearList])
    }

    def setUp(){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_SET_UP_JASPER_FILE)

        String sqlParam = "emp.active_status='ACTIVE' and sal.active_status='ACTIVE'"
        if (params.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${params.hrCategory}"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_setup_data"
        jasperReport.reportName = reportFileName
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

    def extraClass(ExtraClassReportCommand command){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_EXTRA_CLASS_JASPER_FILE)

        //show error if year and month not select

        String sqlParam = " emp.active_status='ACTIVE' and extra.active_status='ACTIVE' "

        if (command.academicYear) {
            sqlParam += " and extra.academic_year='${command.academicYear.key}' "
        }
        if (command.yearMonths) {
            sqlParam += " and extra.year_months='${command.yearMonths.key}' "
        }

        if (command.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${command.hrCategory.id}"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "${command.yearMonths.value}, ${command.academicYear.value}")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_setup_data"
        jasperReport.reportName = reportFileName
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
    def salIncrement(ExtraClassReportCommand command){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_INCREMENT_ALL_JASPER_FILE)

        //show error if year and month not select

        String sqlParam = " emp.active_status='ACTIVE' and inc.active_status='ACTIVE' "

        if (command.academicYear) {
            sqlParam += " and inc.academic_year='${command.academicYear.key}' "
        }
        if (command.yearMonths) {
            sqlParam += " and inc.year_months='${command.yearMonths.key}' "
        }

        if (command.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${command.hrCategory.id}"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "${command.yearMonths.value}, ${command.academicYear.value}")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_setup_data"
        jasperReport.reportName = reportFileName
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

    def attendance(SalAttendanceReportCommand command){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_ATTENDANCE_JASPER_FILE)

        String sqlParam = " emp.active_status='ACTIVE' "
        if (command.academicYear) {
            sqlParam += " and attn.academic_year='${command.academicYear.key}' "
        }
        if (command.yearMonths) {
            sqlParam += " and attn.year_months='${command.yearMonths.key}' "
        }
        if (command.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${command.hrCategory.id}"
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "${command.yearMonths.value}, ${command.academicYear.value}")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_attn_data"
        jasperReport.reportName = reportFileName
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

    def salarySheet(Long id){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_MASTER_JASPER_FILE)
        SalMaster salMaster = SalMaster.read(id)
        if (!salMaster) {
            redirect(controller: 'salarySetup', action: 'salMasterSetup')
            return
        }
        String monthName = salMaster.yearMonths.value
        String year = salMaster.academicYear.value

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "$monthName, $year")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('pfContribution', salMaster.pfContribution)
        paramsMap.put('pfCalField', salMaster.pfCalField)
        paramsMap.put('salMasterid', id)
        paramsMap.put('footNote', salMaster.footNote)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_master_data"
        jasperReport.reportName = reportFileName
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

    def bonusReport(Long id){
        BonusMaster bonusMaster = BonusMaster.get(id)
        if (!bonusMaster) {
            redirect(controller: 'salarySetup', action: 'salMasterSetup')
            return
        }

        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, 'salBonusStatement')
        if(params.statementType == 'bankStatement'){
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, 'salBonusBankStatement')
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('masterId', bonusMaster.id)
        paramsMap.put('festivalName', bonusMaster.festivalName)
        paramsMap.put('academicYear', bonusMaster.academicYear)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "bonus_bank_statement"
        jasperReport.reportName = reportFileName
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
    def payslip(Long id){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_Payslip_JASPER_FILE)
        SalMaster salMaster = SalMaster.read(id)
        if (!salMaster) {
            redirect(controller: 'salarySetup', action: 'salMasterSetup')
            return
        }
        String monthName = salMaster.yearMonths.value
        String year = salMaster.academicYear.value

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "$monthName, $year")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('salMasterid', id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_master_data"
        jasperReport.reportName = reportFileName
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
    def bankStatement(Long id){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_BANK_STATEMENT_JASPER_FILE)
        SalMaster salMaster = SalMaster.read(id)
        if (!salMaster) {
            redirect(controller: 'salarySetup', action: 'salMasterSetup')
            return
        }
        String monthName = salMaster.yearMonths.value
        String year = salMaster.academicYear.value

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('yearMonth', "$monthName, $year")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('salMasterid', id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_master_data"
        jasperReport.reportName = reportFileName
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

    def pfStatement(Long id){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_PF_STATEMENT_JASPER_FILE)
        SalMaster salMaster = SalMaster.read(id)
        if (!salMaster) {
            redirect(controller: 'salarySetup', action: 'salMasterSetup')
            return
        }
        String monthName = salMaster.yearMonths.value
        String year = salMaster.academicYear.value

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('yearMonth', "$monthName, $year")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolHead', grailsApplication.config.getProperty("app.school.head"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('salMasterid', id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "pf_statement"
        jasperReport.reportName = reportFileName
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

    def advance(){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_ADVANCE_AMOUNT_JASPER_FILE)
        String paymentStatus = params.advancePaymentStatus

        String sqlParam = "emp.active_status='ACTIVE'"
        if (paymentStatus == "PAID") {
            sqlParam += " and sal.pay_type = 'PAID'"
        } else if (paymentStatus == "DUE") {
            sqlParam += " and sal.pay_type = 'DUE'"
        }
        if (params.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${params.hrCategory}"
        }
        if (params.employee) {
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_ADVANCE_AMOUNT_EMP_JASPER_FILE)
            sqlParam += " and emp.id = ${params.employee}"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir(CommonUtils.MODULE_SALARY))
        paramsMap.put('sqlParam', sqlParam)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_setup_data"
        jasperReport.reportName = reportFileName
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

    def dps(){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_DPS_LIST_JASPER_FILE)

        String sqlParam = "emp.active_status='ACTIVE' and emp.active_status='ACTIVE'"
        if (params.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${params.hrCategory}"
        }
        if (params.employee) {
            sqlParam += " and emp.id = ${params.employee}"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_setup_data"
        jasperReport.reportName = reportFileName
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

    def salReports() {
        String reportName = params.reportName
        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        YearMonths yearMonths
        if (params.yearMonths) {
            yearMonths = YearMonths.valueOf(params.yearMonths)
        }
        SalMaster salMaster = SalMaster.findByAcademicYearAndYearMonthsAndActiveStatus(academicYear, yearMonths, ActiveStatus.ACTIVE)
        if(reportName =='ss') {
            redirect(action: 'salarySheet', id: salMaster?.id, params: params)
        } else if(reportName =='bs') {
            redirect(action: 'bankStatement', id: salMaster?.id, params: params)
        } else if(reportName =='pf') {
            redirect(action: 'pfStatement', id: salMaster?.id, params: params)
        } else if(reportName =='paySlip') {
            redirect(action: 'payslip', id: salMaster?.id, params: params)
        } else if(reportName =='area') {
            redirect(action: 'area', params: params)
        } else if(reportName =='xc') {
            redirect(action: 'extraClass', params: params)
        } else if(reportName =='attn') {
            redirect(action: 'attendance', params: params)
        } else if(reportName =='increment') {
            redirect(action: 'salIncrement', params: params)
        }
    }


    def advanceSalaryReport(){
        render(view: '/salary/advanceSalaryReport/salaryAdvanceReport')

    }

    def pcReport(){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_PC_AMOUNT_JASPER_FILE)
        String sqlParam = "emp.active_status='ACTIVE' and emp.active_status='ACTIVE'"
        if (params.hrCategory) {
            sqlParam += " and emp.hr_category_id = ${params.hrCategory}"
        }
        if (params.employee) {
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_SALARY_PC_AMOUNT_EMP_JASPER_FILE)
            sqlParam += " and emp.id = ${params.employee}"
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir(CommonUtils.MODULE_SALARY))
        paramsMap.put('sqlParam', sqlParam)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "salary_pc_data"
        jasperReport.reportName = reportFileName
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

    def employeeList() {

        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_SALARY, EMP_EMPLOYEE_LIST_JASPER_FILE)

        String reportQuery = """
        SELECT 
            emp.name, 
            emp.mobile, 
            emp.present_address, 
            emp.birth_date,
            emp.joining_date,
            hrd.name as designation
        FROM employee emp
        INNER JOIN hr_designation hrd ON emp.hr_designation_id = hrd.id 
        WHERE emp.active_status = 'Active';
        """
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('reportQuery', reportQuery)
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_list"
        jasperReport.reportName = reportFileName
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




    private static final String EMP_SALARY_SET_UP_JASPER_FILE = 'setupRecord' //  salary setup  report
    private static final String EMP_SALARY_ADVANCE_AMOUNT_JASPER_FILE = 'loanAmount' //  Sal Advance   report
    private static final String EMP_SALARY_ADVANCE_AMOUNT_EMP_JASPER_FILE = 'loanAmountEmployee' //  Sal Advance   report
    private static final String EMP_SALARY_DPS_AMOUNT_JASPER_FILE = 'dps' //  Sal dps amount   report
    private static final String EMP_SALARY_EXTRA_CLASS_JASPER_FILE = 'extraClass' //   Extra Clas record
    private static final String EMP_SALARY_INCREMENT_ALL_JASPER_FILE = 'salaryIncrement' //   Extra Clas record
    private static final String EMP_SALARY_ATTENDANCE_JASPER_FILE = 'attandance' //    Salary attendance record
    private static final String EMP_SALARY_MASTER_JASPER_FILE = 'salarySheet' //    Salary Sheet record
    private static final String EMP_SALARY_Payslip_JASPER_FILE = 'paySlip' //    Salary Sheet record
    private static final String EMP_SALARY_BANK_STATEMENT_JASPER_FILE = 'bankStatement' //    Salary Bank Statement report record
    private static final String EMP_SALARY_PF_STATEMENT_JASPER_FILE = 'pfStatement' //    Salary Bank Statement report record
    private static final String EMP_SALARY_PC_AMOUNT_JASPER_FILE = 'pcAmount' //    Salary PC report record
    private static final String EMP_SALARY_PC_AMOUNT_EMP_JASPER_FILE = 'pcAmountEmployee' //    Salary PC report record
    private static final String EMP_SALARY_DPS_LIST_JASPER_FILE = 'dpsListAmount' //    Salary Dps  report record
    private static final String EMP_EMPLOYEE_LIST_JASPER_FILE = 'employeeList' //
   /* private static final String EMP_ADVANCE_LOAN_LIST_JASPER_FILE = 'loanAmount' //    Salary Loan  report record*/

}
