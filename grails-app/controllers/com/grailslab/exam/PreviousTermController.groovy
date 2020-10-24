package com.grailslab.exam

import com.grailslab.CommonUtils
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.AvailableRoles
import com.grailslab.enums.ExamTerm
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.report.CusJasperReportDef
import com.grailslab.settings.*
import com.grailslab.stmgmt.AttnStudentSummery
import com.grailslab.stmgmt.PreviousTermMark
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

class PreviousTermController {

    def studentService
    def previousTermService
    def messageSource
    def shiftExamService
    def classNameService
    def studentSubjectsService
    def schoolService
    def jasperService
    def examService
    def recordDayService
    def attnStudentService
    def baseService

    def index(Long id) {
        Section section = Section.read(id)
        if(!section){
            redirect(controller: 'exam',action: 'entry')
            return
        }
        ExamTerm examTerm = ExamTerm.valueOf(params.examTerm)
        if(!examTerm){
            redirect(controller: 'exam',action: 'entry')
            return
        }
        String breadStr = "${examTerm.value} Total Mark Entry"
        String inputLabel = "Total Term Mark"
        def studentList = studentService.allStudent(section)
        render(view: '/exam/termMark',model: [inputLabel:inputLabel,section:section,examTerm:examTerm,breadStr:breadStr, studentList:studentList,dataReturn: null, totalCount: 0])
    }

    def listTermMark(Long id){
        LinkedHashMap gridData
        String result
        Section section = Section.read(id)
        if(!section){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        Exam exam = Exam.read(params.getLong("examId"))

        LinkedHashMap resultMap = previousTermService.getPreviousTermList(params,section, exam.examTerm)

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

    def save(PreviousTermCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = previousTermService.saveOrUpdate(command)
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
        AttnStudentSummery attnStudentSummery = AttnStudentSummery.read(id)
        if (!attnStudentSummery) {
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Exam exam = Exam.read(params.getLong("examId"))

        Integer obtainDay
        if (exam.examTerm == ExamTerm.FINAL_TEST) {
            obtainDay = attnStudentSummery.attendDay
        } else if (exam.examTerm == ExamTerm.SECOND_TERM) {
            obtainDay = attnStudentSummery.term2attenDay
        } else {
            obtainDay = attnStudentSummery.term1attenDay
        }


        String studentID=attnStudentSummery.student.name+"("+attnStudentSummery.student.studentID+")"
        result.put(CommonUtils.IS_ERROR,false)
        result.put('obtainDay',obtainDay)
        result.put('studentID',studentID)
//        result.put('stdId',previousTermMark.student.id)
        result.put('preId',attnStudentSummery.id)
        outPut = result as JSON
        render outPut
    }

    def attendance(Long id) {
        def examNameList = shiftExamService.examNameDropDown()

        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }
        Exam exam=Exam.read(id)
        if(!exam){
            render(view: '/exam/attendanceEntry', layout: layoutName, model: [examNameList: examNameList])
            return
        }
        Section section = exam.section
        ClassName className = exam.className
        int workingDays = className.workingDays?:0

        def entryStd = previousTermService.attendanceEntryStdIds(section, exam.examTerm)
        def studentList = studentService.allStudentNotInList(section,entryStd)

        LinkedHashMap resultMap = previousTermService.getPreviousTermList(params, section, exam.examTerm)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/exam/attendanceEntry',layout: layoutName,model: [examId: exam.id, examNameList: examNameList, className: className.name, workingDays: workingDays, section:section, studentList:studentList,dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/exam/attendanceEntry',layout: layoutName, model: [examId: exam.id, examNameList: examNameList, className: className.name, workingDays: workingDays, section:section, studentList:studentList, dataReturn: resultMap.results, totalCount: totalCount])
    }

    def ctMark(Long id, Long subjectId) {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        Section section = Section.read(id)
        SubjectName subjectName = SubjectName.read(subjectId)

        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }
        if(!section || !subjectName){
            render(view: '/exam/attendanceCtEntry',layout: layoutName,model: [classNameList: classNameList])
            return
        }
        def subjectStdIds = studentSubjectsService.subjectStudentIdsList(section, subjectName)
        def entryStd = previousTermService.ctEntryStdIds(section, subjectName, subjectStdIds)
        def studentList = studentSubjectsService.subjectStudentsNotInList(section, subjectName, entryStd)

        render(view: '/exam/attendanceCtEntry',layout: layoutName,model: [classNameList: classNameList, section:section, subjectName:subjectName, studentList:studentList])
    }

    def saveCt(PreviousTermCtCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = previousTermService.saveOrUpdateCt(command)
        String outPut = result as JSON
        render outPut
    }

    def editCt(Long id) {

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = new LinkedHashMap()
        String outPut
        PreviousTermMark previousTermMark = PreviousTermMark.read(id)
        if (!previousTermMark) {
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        def obtainMark = previousTermMark.ctMark

        String studentID=previousTermMark.student.name+"("+previousTermMark.student.studentID+")"
        result.put(CommonUtils.IS_ERROR,false)
        result.put('obtainMark',obtainMark)
        result.put('studentID',studentID)
//        result.put('stdId',previousTermMark.student.id)
        result.put('preId',previousTermMark.id)
        outPut = result as JSON
        render outPut
    }

    def ctListTermMark(Long id,Long subjectId){
        LinkedHashMap gridData
        String result
        Section section = Section.read(id)

        if(!section){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }

        SubjectName subjectName = SubjectName.read(subjectId)
        def subjectStdIds = studentSubjectsService.subjectStudentIdsList(section, subjectName)
        LinkedHashMap resultMap = previousTermService.getPreviousTermCtList(params, section, subjectName, subjectStdIds)

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

    def ctWorkDays(){
        def classNameList = classNameService.allClassNames()
        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }
        switch (request.method) {
            case 'GET':
                render(view: '/exam/worksDayEntry',layout: layoutName, model: [classNameList: classNameList])
                break
            case 'POST':
                ClassName className
                params.workingDay.each{ classNameId ->
                    try {
                        className = ClassName.get(Long.parseLong(classNameId.key))
                        className.workingDays = Integer.parseInt(classNameId.value)
                        className.save(flush: true)
                    }catch (e){
                        log.error(e.message)
                    }
                }
                flash.message = "Working Days updated Successfully"
                redirect(action: 'ctMark')
                break
        }

    }

    def workDays() {
        def classNameList = classNameService.allClassNames()
        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())) {
            layoutName = "adminLayout"
        }
        switch (request.method) {
            case 'GET':
                render(view: '/exam/worksDayEntry', layout: layoutName, model: [classNameList: classNameList])
                break
            case 'POST':
                ClassName className
                params.workingDay.each { classNameId ->
                    try {
                        className = ClassName.get(Long.parseLong(classNameId.key))
                        className.workingDays = Integer.parseInt(classNameId.value)
                        className.save(flush: true)
                    } catch (e) {
                        log.error(e.message)
                    }
                }
                flash.message = "Working Days updated Successfully"
                redirect(action: 'attendance')
                break
        }

    }

    def countAttendanceDays(CountAttendanceDayCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        ClassName className = command.className
        ShiftExam shiftExam = command.examName
        Integer workingDayCount = recordDayService.workingDay(command.fromDate, command.toDate)

        AcademicYear academicYear = shiftExam.academicYear
        Integer pCount
        AttnStudentSummery attnStudentSummery
        String attField = 'term1attenDay'
        if (shiftExam.examTerm == ExamTerm.SECOND_TERM) {
            attField = 'term2attenDay'
        } else if (shiftExam.examTerm == ExamTerm.FINAL_TEST) {
            attField = 'attendDay'
        }
        def stdNameList = studentService.classSectionStudentList(className, command.sectionExam.section, academicYear)
        for (student in stdNameList){
            pCount = attnStudentService.studentPresentCount(student.studentID, command.fromDate, command.toDate)
            attnStudentSummery = AttnStudentSummery.findByStudentAndAcademicYear(student, academicYear)
            if (!attnStudentSummery) {
                attnStudentSummery = new AttnStudentSummery(student: student, academicYear: academicYear)
            }
            attnStudentSummery."$attField" = pCount
            attnStudentSummery.save(flush: true)
        }
        className.workingDays = workingDayCount
        className.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Attendance updated successfully")
        outPut=result as JSON
        render outPut
        return

    }

    def ctMarkReport(Long id){
        if (!id || !params.subjectName){
            redirect(controller: 'previousTerm', action: 'index')
            return
        }
        Section section = Section.read(id)
        SubjectName subjectName = SubjectName.read(params.getLong("subjectName"))
        //subjectName
        def subjectStdIds = studentSubjectsService.subjectStudentIdsList(section, subjectName)

        String sqlParams = " sec.id = ${section.id} AND sb.id = ${subjectName.id} "
        if (subjectStdIds) {
            sqlParams += " and st.id in ("+subjectStdIds.join(",")+") "
        }


        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParams', sqlParams)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "previous_term_ct"
        jasperReport.reportName = CT_MARK_JASPER_FILE
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

    def attendanceReport(Long id){
        //ShiftExam shiftExam= ShiftExam.read(id)
        Exam exam = Exam.read(id)
        if (!exam){
            redirect(controller: 'previousTerm', action: 'attendance')
            return
        }

        String sqlParams = " sec.id = ${exam.section.id}"

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParams', sqlParams)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "period_attendance_report"
        jasperReport.reportName = ATTENDANCE_JASPER_FILE
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
    private static final String CT_MARK_JASPER_FILE = 'previousTermCtMark'
    private static final String ATTENDANCE_JASPER_FILE = 'previousTermAttendance'
}

class PreviousTermCommand {
    Long id
    Long examId
    Student student
    Integer attendDay

    static constraints = {
        id nullable: true
        student nullable: true
        attendDay nullable: true
        examId nullable: true

    }
}

class PreviousTermCtCommand {
    Long id
    Student student
    Section section
    Double ctMark
    SubjectName subjectName

    static constraints = {
        id nullable: true
        student nullable: true
        ctMark nullable: true
        subjectName nullable: true


    }
}
class CountAttendanceDayCommand {
    ClassName className
    ShiftExam examName
    Exam sectionExam
    Date fromDate
    Date toDate

    static constraints = {
        sectionExam nullable: true
    }
}