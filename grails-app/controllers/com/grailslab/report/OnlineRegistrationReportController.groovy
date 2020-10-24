package com.grailslab.report

import com.grailslab.CommonUtils
import com.grailslab.command.OnlineRegistrationRecordCommand
import com.grailslab.enums.ApplicantStatus
import com.grailslab.enums.PrintOptionType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegForm
import com.grailslab.stmgmt.RegOnlineRegistration

class OnlineRegistrationReportController {

    def messageSource
    def schoolService
    def jasperService
    def regAdmissionFormService
    def uploadService
    def baseService
    def onlineRegistrationService

    def applicantFormReport(OnlineRegistrationRecordCommand command) {

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        ApplicantStatus applicantStatus = command.applicantStatus
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        String reportFileName
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('admissionYear',  academicYear.value)
        paramsMap.put('applicantStatus',  applicantStatus.value)

        CusJasperReportDef jasperReport = new CusJasperReportDef()

        Date fromDate = command.fromDate
        Date toDate = command.toDate
        ClassName className = command.className

        if (command.reportType != "applyCount"){
            if (command.reportType == "details") {
                reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, APPLICANT_DETAIL_LIST_REPORT_JASPER_FILE)
            } else if (command.reportType == 'short'){
                reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, APPLICANT_LIST_REPORT_JASPER_FILE)
            } else if (command.reportType == 'listWithBroSis'){
                reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, APPLICANT_LIST_WITH_BRO_SIS_JASPER_FILE)
            }
            List<RegOnlineRegistration> onlineApplicatList= regAdmissionFormService.regOnlineRegistrationList(className, applicantStatus, fromDate, toDate, academicYear)
            jasperReport.reportData = onlineApplicatList
        } else {
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, APPLICANT_LIST_COUNT_REPORT_JASPER_FILE)
            String sqlParams = ' and reg.academic_year = "'+academicYear.key+'" and reg.applicant_status= "'+applicantStatus.key+'"'
            if (className) {
                sqlParams += ' and reg.class_name_id = '+className.id
            }
            paramsMap.put('sqlParams', sqlParams)
        }

        jasperReport.outputFilename = "ApplicantList"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
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

    def collectionReport(OnlineRegistrationRecordCommand command) {

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        Date fromDate = command.fromDate
        if (!fromDate) {
            fromDate = new Date()
        }

        Date toDate = command.toDate
        if (! toDate) {
            toDate = toDate
        }
        ApplicantStatus applicantStatus = command.applicantStatus
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, APPLICANT_COLLECTION_REPORT_JASPER_FILE)
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('admissionYear',  academicYear.value)
        paramsMap.put('applicantStatus',  applicantStatus.value)
        paramsMap.put('reportDate',  CommonUtils.getDateRange(fromDate, toDate))


        ClassName className = command.className

        List<RegOnlineRegistration> onlineApplicatList= regAdmissionFormService.regOnlineCollectionList(className, applicantStatus, fromDate.clearTime(), toDate.plus(1).clearTime(), academicYear)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "ApplicantList"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.reportData = onlineApplicatList
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

    def printAdmit(String serialNo) {
        if (!serialNo) {
            redirect(controller: 'onlineRegistration', action: 'index')
            return
        }
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        RegOnlineRegistration registration = RegOnlineRegistration.findBySerialNoAndAcademicYearAndActiveStatus(serialNo, academicYear, ActiveStatus.ACTIVE)
        if (!registration) {
            redirect(controller: 'onlineRegistration', action: 'index')
            return
        }
        RegForm regForm = RegForm.findByIdAndAcademicYear(registration.regFormId, academicYear)
        if (regForm && !registration.applyNo) {
            registration.applyNo = onlineRegistrationService.nextOnlineRegistrationApplyNo(regForm.academicYear.value, regForm.applicationPrefix)
            registration.save(flush: true)
        }


        String  reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, ADMIT_COLLECTION_REPORT_JASPER_FILE)
        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        String extension = CommonUtils.PDF_EXTENSION
        String mimeType = CommonUtils.REPORT_FILE_FORMAT_PDF
        def exportFormat = JasperExportFormat.PDF_FORMAT
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('serialNo', registration.serialNo)
        paramsMap.put('imageFile', uploadService.getImageInputStream(registration.imagePath))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "ApplicantList"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
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

    def admitCard(Long regOnlineId) {
        if (!regOnlineId) {
            redirect(controller: 'onlineRegistration', action: 'index')
            return
        }

        RegOnlineRegistration registration = RegOnlineRegistration.read(regOnlineId)
        RegForm regForm = RegForm.read(registration.regFormId)
        if (!registration || !regForm) {
            redirect(controller: 'onlineRegistration', action: 'index')
            return
        }

        if (registration.applicantStatus != ApplicantStatus.AdmitCard) {
            registration.feeAmount = regForm.formPrice
            registration.payment = regForm.formPrice
            registration.invoiceDate = new Date()
            registration.applyNo = onlineRegistrationService.nextOnlineRegistrationApplyNo(regForm.academicYear.value, regForm.applicationPrefix)
            registration.applicantStatus = ApplicantStatus.AdmitCard
            registration.save(flush: true)
        }

        /* String reportFileName = ADMIT_COLLECTION_REPORT_JASPER_FILE*/

        String  reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ONLINE, ADMIT_COLLECTION_REPORT_JASPER_FILE)
        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('serialNo', registration.serialNo)
        paramsMap.put('regOnlineId', registration.id)
        paramsMap.put('imageFile', uploadService.getImageInputStream(registration.imagePath))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "ApplicantList"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
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


    //Applicant  jasper report

    private static final String ADMIT_COLLECTION_REPORT_JASPER_FILE = 'admitCard'

    private static final String APPLICANT_LIST_REPORT_JASPER_FILE = 'applicantsList'// applicant list report
    private static final String APPLICANT_LIST_WITH_BRO_SIS_JASPER_FILE = 'applicantsListWithBroSis'// applicant list report
    private static final String APPLICANT_COLLECTION_REPORT_JASPER_FILE = 'applicantsColection'// applicant Collection report
    private static final String APPLICANT_DETAIL_LIST_REPORT_JASPER_FILE = 'applicantsDetailtList'// applicant list report
    private static final String APPLICANT_LIST_COUNT_REPORT_JASPER_FILE = 'applicantsCountList'// applicant count report
}
