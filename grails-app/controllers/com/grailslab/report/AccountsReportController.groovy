package com.grailslab.report

import com.grailslab.CommonUtils
import com.grailslab.FeePaymentReference
import com.grailslab.MonthWiseCollectionReference
import com.grailslab.MonthlyFeeReference
import com.grailslab.accounting.*
import com.grailslab.command.ChartPrintCommand
import com.grailslab.command.CollectionByHeadCommand
import com.grailslab.command.CollectionSummaryPrintCommand
import com.grailslab.command.InvoiceSummaryCommand
import com.grailslab.core.BaseService
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.DateRangeType
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegOnlineRegistration
import org.joda.time.DateTime
import org.joda.time.LocalDate

class AccountsReportController {

    def uploadService
    def schoolService
    def jasperService
    def messageSource
    def collectionsServiceprintInvoice
    def studentService
    def classNameService
    def chartOfAccountService
    BaseService baseService
    def chart(ChartPrintCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = CHART_OF_ACC_JASPER_FILE
        String sqlParam
        if (command.accountType) {
            sqlParam = ' account_type ="' + command.accountType.key + '"'
        }
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('accountType', command.accountType?.value)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "CHART_OF_ACCOUNT"
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

    def report() {
        String toDayStr = CommonUtils.getUiDateStrForPicker(new Date())
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        render(view: '/collection/accountReports', model: [toDayStr:toDayStr, classNameList: classNameList])
    }

    def stdPayReports() {
        String toDayStr = CommonUtils.getUiDateStrForPicker(new Date())
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        render(view: '/collection/stdPayReports', model: [toDayStr:toDayStr, classNameList: classNameList])
    }

    def printInvoice(String invoiceNo) {
        Invoice invoice = Invoice.findByInvoiceNo(invoiceNo)
        if (!invoice || invoice.activeStatus == ActiveStatus.DELETE) {
            flash.message = "No invoice found."
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(INVOICE_REPORT_JASPER_FILE)

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('invoiceId', invoice.id)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('SUBREPORT_DIR', schoolService.schoolSubreportDir())

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = invoiceNo
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

    def printFormInvoice(String invoiceNo) {
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        RegOnlineRegistration formApply = RegOnlineRegistration.findByApplyNoAndAcademicYearAndActiveStatus(invoiceNo, academicYear, ActiveStatus.ACTIVE)
        if (!formApply) {
            flash.message = "No invoice found."
            render(view: '/baseReport/reportNotFound')
            return
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('invoiceId', formApply.id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = formApply.applyNo
        jasperReport.reportName = REG_FORM_INVOICE_REPORT_JASPER_FILE
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

    def invoiceSummary(InvoiceSummaryCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        Date rStartDate
        Date rEndDate

        if (command.pDateRange) {
            LocalDate nowDate = new LocalDate();
            if (command.pDateRange == DateRangeType.TODAY) {
                rStartDate = nowDate.toDate()
                rEndDate = nowDate.toDate()
            } else if (command.pDateRange == DateRangeType.All) {
                rStartDate = nowDate.withDayOfYear(1).toDate()
                rEndDate = nowDate.toDate()
            }
        } else {
            rStartDate = command.startDate
            rEndDate = command.endDate
        }

        String reportFileName
        if (command.discount.equals('without')) {
            if (command.reportSortType == "byClassName") {
                reportFileName = INVOICE_SUMMARY_BY_CLASS_NO_DISCOUNT_JASPER_FILE
            } else {
                reportFileName = INVOICE_SUMMARY_BY_DATE_NO_DISCOUNT_JASPER_FILE
            }
        } else {
            if (command.reportSortType == "byClassName") {
                reportFileName = INVOICE_SUMMARY_BY_CLASS_JASPER_FILE
            } else {
                reportFileName = INVOICE_SUMMARY_BY_DATE_JASPER_FILE
            }
        }

        String sqlParam = ""
        if (command.pClassName) {
            sqlParam = " and std.class_name_id = ${command.pClassName?.id} "
        }
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('fromDate', rStartDate)
        paramsMap.put('fromDateStr', CommonUtils.getMysqlQueryDateStr(rStartDate))
        paramsMap.put('toDate', rEndDate)
        paramsMap.put('toDateStr', CommonUtils.getMysqlQueryDateStr(rEndDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "collection_summary"
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

    def feeCollectionSummaryByClass(InvoiceSummaryCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        Date rStartDate = command.startDate
        Date rEndDate = command.endDate

        String reportFileName
        if (command.reportSortType == "byClassName") {
            reportFileName = FEE_COLLECTION_SUMMERY_BY_CLASS_HEAD
        } else {
            reportFileName = FEE_COLLECTION_SUMMERY_BY_FEE_NAME
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('fromDate', rStartDate)
        paramsMap.put('fromDateStr', CommonUtils.getMysqlQueryDateStr(rStartDate))
        paramsMap.put('toDate', rEndDate)
        paramsMap.put('toDateStr', CommonUtils.getMysqlQueryDateStr(rEndDate))


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "collection_summary"
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

    def studentFeePay(InvoiceSummaryCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = STUDENT_TOTAL_COLLECTION_WITH_FEENAME
        if (command.reportType == "withoutFeeHead") {
            reportFileName = STUDENT_TOTAL_COLLECTION_WITHOUT_FEENAME
        }

        String sqlParam = " "
        String sectionName = "All"
        String className = "All"
        if (command.section) {
            sqlParam = " and std.section_id = ${command.section?.id}"
            sectionName = command.section?.name
        }

        if (command.pClassName) {
            sqlParam += " and std.class_name_id = ${command.pClassName?.id}"
            className = command.pClassName?.name
        }

        Date rStartDate = command.startDate
        Date rEndDate = command.endDate

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('sectionName', sectionName)
        paramsMap.put('className', className)
        paramsMap.put('fromDate', rStartDate)
        paramsMap.put('fromDateStr', CommonUtils.getMysqlQueryDateStr(rStartDate))
        paramsMap.put('toDate', rEndDate)
        paramsMap.put('toDateStr', CommonUtils.getMysqlQueryDateStr(rEndDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "collection_summary"
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

    def collectionSummary(CollectionSummaryPrintCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "collection_summary"
        jasperReport.reportName = COLLECTION_SUMMARY_JASPER_FILE
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

    def reports() {

    }


    def feePayReport() {
        if (!params.className || !params.feeItems) {
            flash.message = "Please input valid parameter"
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ACCOUNTS, FEE_PAY_JASPER_FILE)
        List<FeePaymentReference> feePayList = new ArrayList<FeePaymentReference>()
        def resultMap
        FeePaymentReference paymentReference
        if(params.payStatus && params.payStatus=="paid"){
            resultMap =   collectionsService.feePayPaginationList(params)
            resultMap.results.each {item ->
                paymentReference = new FeePaymentReference(item[0], item[1], item[2], item[3])
                paymentReference.amount = item[4]
                paymentReference.discount = item[5]
                paymentReference.netPayment = item[6]
                paymentReference.invoiceNo = item[7]
                paymentReference.invoiceDate = item[8]
                feePayList.add(paymentReference)
            }
        }else {
            List<Long> feeItemPaidStudentIdsList = collectionsService.feeItemPaidStudentIdsList(params)
            resultMap =   studentService.feeItemNotPaidPaginateList(params,feeItemPaidStudentIdsList)
            resultMap.results.each {item ->
                paymentReference = new FeePaymentReference(item[0], item[1], item[2], item[3])
                feePayList.add(paymentReference)
            }
        }
        ClassName className = ClassName.read(params.getLong('className'))
        FeeItems feeItems = FeeItems.read(params.getLong('feeItems'))
        Double feeAmount = feeItems.amount
        String feeItemName = feeItems.name

        ItemsDetail itemsDetail
        Long itemDetailId = params.getLong('itemDetail')
        if(itemDetailId){
            itemsDetail = ItemsDetail.read(itemDetailId)
            if (itemsDetail){
                feeItemName += ' - '+itemsDetail.name
                feeAmount = itemsDetail.amount
            }
        }
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('payStatus', params.payStatus)
        paramsMap.put('className', className.name)
        paramsMap.put('feeItemName', feeItemName)
        paramsMap.put('feeAmount', feeAmount)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "feepay"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = feePayList
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

    def monthWise() {
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ACCOUNTS, MONTHWISE_COLLECTION_JASPER_FILE)
        List<MonthWiseCollectionReference> feePayList = new ArrayList<MonthWiseCollectionReference>()

        ClassName selectedCls
        if (params.className){
            selectedCls = ClassName.read(params.getLong('className'))
        }
        AcademicYear academicYear
        if (params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }

        MonthWiseCollectionReference paymentReference
        Double january = 0
        Double february = 0
        Double march = 0
        Double april = 0
        Double may = 0
        Double june = 0
        Double july = 0
        Double august = 0
        Double september = 0
        Double october = 0
        Double november = 0
        Double december = 0
        DateTime toDay = new DateTime()
        int currMonth = toDay.monthOfYear
        Date startDate
        Date endDate
        ChartOfAccount cAccount
        if(params.payStatus && params.payStatus=="feeItem"){
            def chartOfAccountList = chartOfAccountService.feesListForCollectionReport()
            for (chart in chartOfAccountList){
                cAccount = chart.account //ChartOfAccount.read(chart.id)
                startDate = toDay.withMonthOfYear(1).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(1).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                january = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 1)

                startDate = toDay.withMonthOfYear(2).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(2).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                february = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 2)

                startDate = toDay.withMonthOfYear(3).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(3).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                march = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 3)

                startDate = toDay.withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(4).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                april = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 4)

                startDate = toDay.withMonthOfYear(5).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(5).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                may = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 5)

                startDate = toDay.withMonthOfYear(6).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(6).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                june = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 6)

                startDate = toDay.withMonthOfYear(7).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(7).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                july = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 7)

                startDate = toDay.withMonthOfYear(8).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(8).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                august = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 8)

                startDate = toDay.withMonthOfYear(9).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(9).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                september = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 9)

                startDate = toDay.withMonthOfYear(10).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(10).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                october = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 10)

                startDate = toDay.withMonthOfYear(11).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(11).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                november = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 11)

                startDate = toDay.withMonthOfYear(12).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(12).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                december = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 12)
                paymentReference = new MonthWiseCollectionReference(chart.name, january, february, march, april, may, june, july, august, september, october, november, december)
                feePayList.add(paymentReference)
            }
        } else if(params.payStatus && params.payStatus=="feeItemWithCount"){
            def chartOfAccountList = chartOfAccountService.feesListForCollectionReport()
            for (chart in chartOfAccountList){
                cAccount = chart.account //ChartOfAccount.read(chart.id)
                startDate = toDay.withMonthOfYear(1).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(1).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                january = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 1)

                startDate = toDay.withMonthOfYear(2).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(2).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                february = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 2)

                startDate = toDay.withMonthOfYear(3).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(3).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                march = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 3)

                startDate = toDay.withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(4).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                april = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 4)

                startDate = toDay.withMonthOfYear(5).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(5).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                may = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 5)

                startDate = toDay.withMonthOfYear(6).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(6).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                june = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 6)

                startDate = toDay.withMonthOfYear(7).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(7).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                july = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 7)

                startDate = toDay.withMonthOfYear(8).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(8).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                august = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 8)

                startDate = toDay.withMonthOfYear(9).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(9).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                september = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 9)

                startDate = toDay.withMonthOfYear(10).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(10).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                october = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 10)

                startDate = toDay.withMonthOfYear(11).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(11).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                november = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 11)

                startDate = toDay.withMonthOfYear(12).dayOfMonth().withMinimumValue().toDate()
                endDate = toDay.withMonthOfYear(12).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                december = collectionsService.chartTotalAmount(academicYear, cAccount, selectedCls, startDate, endDate, 12)
                paymentReference = new MonthWiseCollectionReference(chart.name, january, february, march, april, may, june, july, august, september, october, november, december)
                feePayList.add(paymentReference)
            }
        } else {
            def classNameList = classNameService.classNamesList(selectedCls?.id)
            for (className in classNameList) {
                if(params.payStatus && params.payStatus=="dueDate"){
                    startDate = toDay.withMonthOfYear(1).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(1).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    january = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 1)

                    startDate = toDay.withMonthOfYear(2).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(2).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    february = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 2)

                    startDate = toDay.withMonthOfYear(3).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(3).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    march = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 3)

                    startDate = toDay.withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(4).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    april = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate,4)

                    startDate = toDay.withMonthOfYear(5).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(5).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    may = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 5)

                    startDate = toDay.withMonthOfYear(6).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(6).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    june = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 6)

                    startDate = toDay.withMonthOfYear(7).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(7).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    july = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 7)

                    startDate = toDay.withMonthOfYear(8).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(8).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    august = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate,8)

                    startDate = toDay.withMonthOfYear(9).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(9).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    september = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 9)

                    startDate = toDay.withMonthOfYear(10).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(10).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    october = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 10)

                    startDate = toDay.withMonthOfYear(11).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(11).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    november = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 11)

                    startDate = toDay.withMonthOfYear(12).dayOfMonth().withMinimumValue().toDate()
                    endDate = toDay.withMonthOfYear(12).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                    december = collectionsService.collectionDueDateAmount(academicYear, className, startDate, endDate, 12)
                }else if (params.payStatus && params.payStatus=="paidDate") {
                    if (currMonth >= 1) {
                        startDate = toDay.withMonthOfYear(1).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(1).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        january = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 2) {
                        startDate = toDay.withMonthOfYear(2).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(2).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        february = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 3) {
                        startDate = toDay.withMonthOfYear(3).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(3).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        march = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 4) {
                        startDate = toDay.withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(4).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        april = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 5) {
                        startDate = toDay.withMonthOfYear(5).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(5).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        may = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 6) {
                        startDate = toDay.withMonthOfYear(6).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(6).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        june = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 7) {
                        startDate = toDay.withMonthOfYear(7).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(7).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        july = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 8) {
                        startDate = toDay.withMonthOfYear(8).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(8).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        august = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 9) {
                        startDate = toDay.withMonthOfYear(9).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(9).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        september = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 10) {
                        startDate = toDay.withMonthOfYear(10).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(10).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        october = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 11) {
                        startDate = toDay.withMonthOfYear(11).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(11).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        november = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                    if (currMonth >= 12) {
                        startDate = toDay.withMonthOfYear(12).dayOfMonth().withMinimumValue().toDate()
                        endDate = toDay.withMonthOfYear(12).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).toDate()
                        december = collectionsService.collectionAmount(academicYear, className, startDate, endDate)
                    }
                }
                paymentReference = new MonthWiseCollectionReference(className.name, january, february, march, april, may, june, july, august, september, october, november, december)
                feePayList.add(paymentReference)
            }
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('academicYear', academicYear.value)
        paramsMap.put('payStatus', params.payStatus)
        paramsMap.put('className', selectedCls?.name)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "feepay"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = feePayList
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

    def monthlyFeePayReport() {
        if (!params.className || !params.feeItems) {
            flash.message = "Please input valid parameter"
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName className = ClassName.read(params.getLong('className'))
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ACCOUNTS, MONTHLY_FEE_PAY_JASPER_FILE)
        List<MonthlyFeeReference> feePayList = new ArrayList<MonthlyFeeReference>()
        MonthlyFeeReference monthlyFeeReference
        def studentList = studentService.byClassName(className, schoolService.workingYear(className))
        FeeItems feeItems = FeeItems.read(params.getLong('feeItems'))
        YearMonths sMonth = YearMonths.JANUARY
        YearMonths eMonth = YearMonths.DECEMBER
        if (params.sMonth) {
            sMonth = YearMonths.valueOf(params.sMonth)
        }
        if (params.eMonth) {
            eMonth = YearMonths.valueOf(params.eMonth)
        }
        int lowMonth = sMonth.getSerial()
        int highMonth = eMonth.getSerial()
        Integer range =  (highMonth - lowMonth) + 1
        Integer paidMonth = 0
        def sPaymentList

        Double feeAmount = feeItems.netPayable
        Double payAmount = feeAmount * range
        String paidMonths
        Double paidAmount = 0
        Integer dueMonth
        Double dueAmount
        for (student in studentList) {
            sPaymentList = FeePayments.findAllByStudentAndFeeItemsAndActiveStatusAndItemDetailSortPositionBetween(student, feeItems, ActiveStatus.ACTIVE, lowMonth, highMonth, [sort: "itemDetailSortPosition", order: "asc"])
            paidMonth = sPaymentList.size()
            if (paidMonth > 0) {
                paidMonths = sPaymentList.first().itemDetailName
                if (paidMonth > 1) {
                    paidMonths += " - "+sPaymentList.last().itemDetailName
                }
                paidAmount = sPaymentList.collect {it.totalPayment as Double}.sum()
            } else {
                paidMonths = "Not Paid"
                paidAmount = 0
            }
            dueMonth = range - paidMonth
            dueAmount =  dueMonth * feeAmount
            if (dueAmount > 0) {
                monthlyFeeReference = new MonthlyFeeReference(student.section.name, student.studentID, student.name, student.rollNo, payAmount, paidMonths, paidAmount, dueMonth, dueAmount)
                feePayList.add(monthlyFeeReference)
            }
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('className', className.name)
        paramsMap.put('feeItemName', feeItems.name)
        paramsMap.put('feeAmount', feeItems.netPayable)
        paramsMap.put('sMonth', sMonth.value)
        paramsMap.put('eMonth', eMonth.value)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "fee_pay_report"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = feePayList
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

    private static final String COLLECTION_SUMMARY_JASPER_FILE = 'collectionSummaryReport'
    private static final String COLLECTION_BY_HEAD_JASPER_FILE = 'collectionByHead.jasper'
    private static final String CHART_OF_ACC_JASPER_FILE = 'chartOfAccount'
    //invoice report
    private static final String INVOICE_REPORT_JASPER_FILE = 'invoiceReport'
    private static final String REG_FORM_INVOICE_REPORT_JASPER_FILE = 'regFormInvoiceReport'
    //invoiceSummary
    private static final String INVOICE_SUMMARY_BY_DATE_JASPER_FILE = 'invoiceSummaryByDate'
    private static
    final String INVOICE_SUMMARY_BY_DATE_NO_DISCOUNT_JASPER_FILE = 'invoiceSummaryByDateNoDiscount'
    private static final String INVOICE_SUMMARY_BY_CLASS_JASPER_FILE = 'invoiceSummaryByClass'
    private static
    final String INVOICE_SUMMARY_BY_CLASS_NO_DISCOUNT_JASPER_FILE = 'invoiceSummaryByClassNoDiscount'
    private static final String INVOICE_SUMMARY_WITH_DETAIL_JASPER_FILE = 'invoiceSummaryWithDetail.jasper'
    private static final String FEE_COLLECTION_SUMMERY_BY_CLASS_HEAD = 'feeCollectionSummaryByClassHeader'
    private static final String FEE_COLLECTION_SUMMERY_BY_FEE_NAME = 'feeCollectionSummaryByFeeName'
    private static final String STUDENT_TOTAL_COLLECTION_WITH_FEENAME = 'studentTotalCollectionWithFeeName'
    private static final String STUDENT_TOTAL_COLLECTION_WITHOUT_FEENAME = 'studentTotalCollectionWithOutFeeName'
    //Applicants Collection
    private static final String FEE_PAY_JASPER_FILE = 'feePay'
    private static final String MONTHLY_FEE_PAY_JASPER_FILE = 'monthlyFeePay'
    private static final String MONTHWISE_COLLECTION_JASPER_FILE = 'monthWiseCollection'
}

