package com.grailslab.account

import com.grailslab.CommonUtils
import com.grailslab.accounting.FeeItems
import com.grailslab.accounting.StudentDiscount

import com.grailslab.command.StudentScholarshipCommand
import com.grailslab.command.report.StudentBirthDayListCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.report.CusJasperReportDef
import com.grailslab.stmgmt.Student
import grails.converters.JSON

class DiscountController {

    def studentDiscountService
    def studentService
    def schoolService
    def collectionsService
    def classNameService
    def feeItemsService
    def baseService
    def jasperService

    def index() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/collection/discount/discount', model: [classList:classList, academicYearList:academicYearList, workingYear:academicYear])
    }

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        StudentDiscount discount = StudentDiscount.read(id)
        if (!discount) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        FeeItems feeItems = discount.feeItems
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,discount)
        result.put("stdName",discount.student?.name)
        result.put("feeItemsId",feeItems?.id)
        result.put("feeItemsName","${feeItems.code} - ${feeItems.name} [amount: ${feeItems.amount}, discount: ${feeItems.discount}]")
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = studentDiscountService.delete(id)
        String output = result as JSON
        render output
    }

    def save() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()

        Double amount = params.getDouble('amount')
        if (!amount || amount == 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please insert discount amount.")
            String output = result as JSON
            render output
            return
        }

        Long discountId = params.getLong('id')
        if (discountId) {
            result = studentDiscountService.saveOrUpdate(discountId, null, null, amount)
        } else {
            Student student = studentService.read(params.getLong('student'))
            FeeItems feeItems = feeItemsService.read(params.getLong('feeItems'))
            if (!student || !feeItems) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Please select both Student and fee Item for discount")
                String output = result as JSON
                render output
                return
            }
            result = studentDiscountService.saveOrUpdate(discountId, student, feeItems, amount)
        }
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =studentDiscountService.studentDiscountPaginateList(params)

        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def loadFee(Long studentId){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Student student = Student.read(studentId)
        if(!student){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        def feeList=collectionsService.allFeeItemsByStudentDD(student)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('feeList', feeList)
        outPut = result as JSON
        render outPut
        return
    }

    //student scholarship manage
    def scholarship() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/collection/discount/scholarship', model: [classList:classList, academicYearList:academicYearList, workingYear:academicYear])
    }

    def scholarshipList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = studentService.studentScholarshipList(params)

        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveScholarship(StudentScholarshipCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = studentDiscountService.saveScholarship(command)
        String output = result as JSON
        render output
    }

    def scholarshipDelete(Long id) {
        LinkedHashMap result = studentDiscountService.scholarshipDelete(id)
        String output = result as JSON
        render output
    }

    def discountStudentList(){
        String academicYear = params.academicYear
        Long classNameId = params.getLong("className")
        Long sectionId = params.getLong("sectionName")
        String sqlParam = " select cls.name className, sec.name sectionName, st.studentid studentid, st.name stdName, st.roll_no, fi.name feeName, fi.net_payable, sd.amount, sd.discount_percent from student_discount sd inner join student st on sd.student_id = st.id inner join fee_items fi on sd.fee_items_id = fi.id inner join class_name cls on st.class_name_id = cls.id inner join section sec on st.section_id = sec.id where sd.academic_year = '${academicYear}' and sd.active_status = 'ACTIVE' "
        if (classNameId) {
            sqlParam += " and cls.id=" + classNameId
        }
        if (sectionId) {
            sqlParam += " and sec.id=" + sectionId
        }
        sqlParam += " order by cls.id, st.roll_no asc;"

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('year', academicYear.replaceAll("Y",""))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "discount_student_list"
        jasperReport.reportName = STUDENT_DISCOUNT_LIST_JASPER_FILE
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
    private static final String STUDENT_DISCOUNT_LIST_JASPER_FILE = 'studentDiscountList'
}
