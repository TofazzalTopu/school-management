package com.grailslab.report

import com.grailslab.ClassNameService
import com.grailslab.CommonUtils
import com.grailslab.StudentAnalysisReference
import com.grailslab.StudentService
import com.grailslab.command.report.*
import com.grailslab.core.BaseService
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.Gender
import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.Religion
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

class StudentReportController {
    def messageSource
    def schoolService
    def sectionService
    def jasperService
    def classSubjectsService
    ClassNameService classNameService
    StudentService studentService
    BaseService baseService


    def studentList(StudentListCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        AcademicYear academicYear = command.academicYear?:schoolService.workingYear(command.className)

        String sectionIds
        List sectionIdList
        def classSections
        if(command.section){
            sectionIds = command.section.id.toString()
        } else if (command.className){
            classSections = sectionService.classSections(command.className)
            sectionIdList = classSections.collect {it.id}
            if (sectionIdList.size()>0){
                sectionIds = sectionIdList.join(',')
            }
        }else {
            classSections = sectionService.allSections(params)
            sectionIdList = classSections.collect {it.id}
            if (sectionIdList.size()>0){
                sectionIds = sectionIdList.join(',')
            }
        }
        if (!sectionIds) {
            sectionIds = "0"
        }
        StudentStatus studentStatus = command.studentStatus?:StudentStatus.NEW

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('schoolId', CommonUtils.DEFAULT_SCHOOL_ID)
        paramsMap.put('academicYear', academicYear.key)
        paramsMap.put('year', academicYear.value)
        paramsMap.put('studentStatus', studentStatus.key)
        paramsMap.put('sectionIDs', sectionIds)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Student_LIST"
        jasperReport.reportName = STUDENT_LIST_JASPER_FILE
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

    def studentSingleList(){
        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)

        String sectionIds
        List sectionIdList
        def classSections
        if(params.section){
            sectionIds = params.section
        } else if (params.className){
            ClassName className = ClassName.read(params.getLong('className'))
            classSections = sectionService.classSections(className)
            sectionIdList = classSections.collect {it.id}
            if (sectionIdList.size()>0){
                sectionIds = sectionIdList.join(',')
            }
        }else {
            classSections = sectionService.allSections(null, academicYear)
            sectionIdList = classSections.collect {it.id}
            if (sectionIdList.size()>0){
                sectionIds = sectionIdList.join(',')
            }
        }
        if (!sectionIds) {
            sectionIds = "0"
        }

        String reportFileName = STUDENT_SINGLE_PAGE_JASPER_FILE
        String studentStatus = params.studentStatus
        if (studentStatus == "NameRoll") {
            studentStatus = "NEW"
            reportFileName = STUDENT_SINGLE_PAGE_NAME_ROLL_JASPER_FILE
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('schoolId', CommonUtils.DEFAULT_SCHOOL_ID)
        paramsMap.put('academicYear', academicYear.key)
        paramsMap.put('year', academicYear.value)
        paramsMap.put('studentStatus', studentStatus)
        paramsMap.put('sectionIDs', sectionIds)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Student_LIST"
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
    def studentDynamicList(StudentDynamicListCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        AcademicYear academicYear = command.academicYear ?: schoolService.workingYear(null)
        String sqlParam = ' and std.academic_year = "' + academicYear.key + '"'
        if (command.className) {
            sqlParam += ' and cName.id=' + command.className.id
        }
        if (command.section) {
            sqlParam += ' and sec.id=' + command.section.id
        }
        if (command.gender) {
            sqlParam += ' and regis.gender = "' + command.gender.key + '"'
        }
        if (command.religion) {
            sqlParam += ' and regis.religion = "' + command.religion.key + '"'
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('academicYear', academicYear.key)
        paramsMap.put('year', academicYear.value)
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Student_LIST"
        jasperReport.reportName = STUDENT_DYNAMIC_LIST_JASPER_FILE
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
    def studentContactList(StudentContactListCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        AcademicYear academicYear = command.academicYear ?: schoolService.workingYear(null)
        String sqlParam = ""
        if (command.className) {
            sqlParam += ' and cName.id= ' + command.className.id
        }
        if (command.section) {
            sqlParam += ' and sec.id= ' + command.section.id
        }

        String reportFileName
        if (command.reportType == 'ADDRESS'){
            reportFileName = STUDENT_ADDRESS_LIST_JASPER_FILE
        } else if (command.reportType == 'INVAILD'){
            reportFileName = STUDENT_CONTACT_LIST_JASPER_FILE
        } else {
            reportFileName = STUDENT_CONTACT_LIST_JASPER_FILE
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('year', academicYear.value)
        paramsMap.put('academicYear', academicYear.key)
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Student_LIST"
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

    def studentBirthDayList(StudentBirthDayListCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        AcademicYear academicYear = command.academicYear ?: schoolService.workingYear(null)
        String sqlParam = ' std.student_status = "NEW" and regis.active_status = "ACTIVE" and std.academic_year = "' + academicYear.key + '"'
        if (command.className) {
            sqlParam += ' and cName.id=' + command.className.id
        }
        if (command.section) {
            sqlParam += ' and sec.id=' + command.section.id
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('startDate', "2018-$command.startDate")
        paramsMap.put('endDate', "2018-$command.endDate")

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Student_LIST"
        jasperReport.reportName = STUDENT_BDAY_LIST_JASPER_FILE
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
    def classSubject(Long id){
        if (!id) {
            flash.message ="Class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName className = ClassName.read(id)
        if (!className) {
            flash.message ="Class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('clsName', className.name)
        paramsMap.put('classId', className.id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "${className.name}_subject"
        jasperReport.reportName = CLASS_SUBJECT_JASPER_FILE
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
    def studentSubject(Long id){

        if (!id) {
            flash.message ="Section not found"
            render(view: '/baseReport/reportNotFound')
            return
        }
        Section section = Section.read(id)
        if (!section) {
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }
        def compulsorySubjectStr = classSubjectsService.subjectTypeCompulsoryListStr(section.className, section.groupName)

        String reportFileName = STUDENT_SUBJECT_JASPER_FILE

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sectionId', section.id)
        paramsMap.put('academicYear', schoolService.workingYear(null).key)
        paramsMap.put('compulsorySubjectStr', compulsorySubjectStr)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "${section.name}_subject"
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

    def fesRegistration(Long id){
        if (!id) {
            flash.message ="Class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName className = ClassName.read(id)
        if (!className) {
            flash.message ="Class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportFileName = FES_REGISTRATIION_LIST_JASPER_FILE

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('clsName', className.name)
        paramsMap.put('classId', className.id)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "${className.name}_subject"
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

    def analysis(StudentAnalysisCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportFileName

        List referenceList = new ArrayList()
        StudentAnalysisReference analysisReference

        Integer maleStudentCount = 0
        Integer femaleStudentCount = 0
        Integer muslimCount = 0
        Integer hinduCount = 0
        Integer christanCount = 0
        Integer buddhistCount = 0
        Integer admissionCount = 0
        Integer promotionCount = 0
        int sortOrder = 0
        if (command.analysisReportType == "byClass") {
            reportFileName = STUDENT_ANALYSIS_SUMMERY_BY_CLASS
            List<ClassName> classNameList = classNameService.allClassNames()

            String classNameStr
            for (ClassName className: classNameList) {
                classNameStr = className.name
                maleStudentCount = studentService.numberOfStudentByGender(command.academicYear, className, null, Gender.MALE)
                femaleStudentCount = studentService.numberOfStudentByGender(command.academicYear, className, null, Gender.FEMALE)
                muslimCount = studentService.numberOfStudentByReligion(command.academicYear, className, null, Religion.ISLAM)
                hinduCount = studentService.numberOfStudentByReligion(command.academicYear, className, null, Religion.HINDU)
                christanCount = studentService.numberOfStudentByReligion(command.academicYear, className, null, Religion.CHRISTIAN)
                buddhistCount = studentService.numberOfStudentByReligion(command.academicYear, className, null, Religion.BUDDHIST)
                admissionCount = studentService.numberOfStudentByAdmission(command.academicYear, className, null, "admission")
                promotionCount = studentService.numberOfStudentByAdmission(command.academicYear, className, null, "promotion")

                analysisReference = new StudentAnalysisReference(classNameStr, null, sortOrder++, maleStudentCount, femaleStudentCount,
                        muslimCount, hinduCount, christanCount, buddhistCount, admissionCount, promotionCount)
                referenceList.add(analysisReference)
            }
        } else {
            reportFileName = STUDENT_ANALYSIS_SUMMERY_BY_SECTION
            List<Section> listOfSections = sectionService.allSections()

            String classNameStr
            String sectionStr
            for (Section section: listOfSections) {
                classNameStr = section.className.name
                sectionStr = section.name
                maleStudentCount = studentService.numberOfStudentByGender(command.academicYear, null, section, Gender.MALE)
                femaleStudentCount = studentService.numberOfStudentByGender(command.academicYear, null, section, Gender.FEMALE)
                muslimCount = studentService.numberOfStudentByReligion(command.academicYear, null, section, Religion.ISLAM)
                hinduCount = studentService.numberOfStudentByReligion(command.academicYear, null, section, Religion.HINDU)
                christanCount = studentService.numberOfStudentByReligion(command.academicYear, null, section, Religion.CHRISTIAN)
                buddhistCount = studentService.numberOfStudentByReligion(command.academicYear, null, section, Religion.BUDDHIST)
                admissionCount = studentService.numberOfStudentByReligion(command.academicYear, null, null, Religion.BUDDHIST)
                promotionCount = studentService.numberOfStudentByReligion(command.academicYear, null, null, Religion.BUDDHIST)

                analysisReference = new StudentAnalysisReference(classNameStr, null, sortOrder++, maleStudentCount, femaleStudentCount,
                        muslimCount, hinduCount, christanCount, buddhistCount, admissionCount, promotionCount)
                referenceList.add(analysisReference)
            }
        }

        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('academicYear', command.academicYear?.value)
        paramsMap.put('className', command.className?.name)
        paramsMap.put('groupName', command.groupName?.value)
        paramsMap.put('shiftName', command.shift?.value)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "STUDENT_ANALYSIS"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = referenceList
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

    def printTransferCertificate(Long id){
        if (!id) {
            flash.message ="TC not found"
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportFileName = STUDENT_TC_JASPER_FILE

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
//        paramsMap.put('clsName', className.name)
        paramsMap.put('tcId', id)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "tc"
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

    private static final String STUDENT_ANALYSIS_SUMMERY_BY_CLASS = 'studentAnalysisByClass'
    private static final String STUDENT_ANALYSIS_SUMMERY_BY_SECTION = 'studentAnalysisBySection'
    //student List Jasper report

    private static final String STUDENT_LIST_JASPER_FILE = 'studentList'
    private static final String STUDENT_SINGLE_PAGE_JASPER_FILE = 'studentSingleList'
    private static final String STUDENT_SINGLE_PAGE_NAME_ROLL_JASPER_FILE = 'studentSingleNameRollList'
    private static final String STUDENT_DYNAMIC_LIST_JASPER_FILE = 'studentDynamicList'
    private static final String STUDENT_CONTACT_LIST_JASPER_FILE = 'studentContactList'
    private static final String STUDENT_ADDRESS_LIST_JASPER_FILE = 'studentAddressList'
    private static final String STUDENT_BDAY_LIST_JASPER_FILE = 'studentListBirthDay'
    private static final String CLASS_SUBJECT_JASPER_FILE = 'classSubject'
    private static final String FES_REGISTRATIION_LIST_JASPER_FILE = 'fesRegistrationList'
    private static final String STUDENT_SUBJECT_JASPER_FILE = 'studentSubject'
    private static final String STUDENT_TC_JASPER_FILE = 'transferCertificate'

    private static final String APPLICANT_FORM_LIST_REPORT_JASPER_FILE = 'applicantsFormReportList'// applicant list report
}
