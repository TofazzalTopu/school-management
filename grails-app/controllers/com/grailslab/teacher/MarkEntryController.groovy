package com.grailslab.teacher

import com.grailslab.CommonUtils
import com.grailslab.command.CtExamMarkCommand
import com.grailslab.command.ExamMarkPrintCommand
import com.grailslab.command.HallExamMarkCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.report.CusJasperReportDef
import com.grailslab.settings.*
import com.grailslab.stmgmt.ExamMark
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

class MarkEntryController {

    def shiftExamService
    def examMarkService
    def studentService
    def studentSubjectsService
    def messageSource
    def springSecurityService
    def schoolService
    def jasperService
    def baseService

    def index() {
        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        render (view: 'index', layout: layoutName, model: [academicYearList: academicYearList])
    }
    def addCtMark(Long id) {
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if(!examSchedule){
            flash.message = "Exam Schedule Not Fount. Please contact admin."
            redirect(action: 'index')
            return
        }
        Exam exam = examSchedule.exam
        if(exam.examStatus != ExamStatus.NEW){
            flash.message = "Mark Entry not Allowed. Please contact admin."
            redirect(action: 'index')
            return
        }
        if(exam.isLocked == 1){
            flash.message = "Mark Entry locked. Please contact admin."
            redirect(action: 'index')
            return
        }
        String ctMarkFor= params.ctMarkFor
        if (!ctMarkFor) {
            ctMarkFor= "CLASS_TEST1"
        }

        ClassName className = exam.className
        Section section = exam.section
        SubjectName subject = examSchedule.subject

        String viewName = "ctMarkEntry"
        LinkedHashMap resultMap
        if (ctMarkFor == "CLASS_TEST1") {
            resultMap = examMarkService.getCtMarkEntryList(params, examSchedule, subject)
        }else if (ctMarkFor == "CLASS_TEST2") {
            viewName = "ct2MarkEntry"
            resultMap = examMarkService.getCt2MarkEntryList(params, examSchedule, subject)
        } else if (ctMarkFor == "CLASS_TEST3") {
            viewName = "ct3MarkEntry"
            resultMap = examMarkService.getCt3MarkEntryList(params, examSchedule, subject)
        }

        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }

        def examNameList = shiftExamService.examNameDropDown()

        int totalCount = resultMap.totalCount
        def markEntryResult = resultMap.results
        def studentList
        def ctMarkInputStudentIds
        if(examSchedule.subjectType == SubjectType.COMPULSORY){
            if (totalCount == 0) {
                studentList = studentService.allStudent(section)
                render(view: viewName, layout: layoutName, model: [ctMarkFor: ctMarkFor, examNameList:examNameList,  examSchedule:examSchedule, className:className.name, section:section.name, examTerm:exam.shiftExam.examTerm.value, subject:subject.name, 'studentList': studentList, dataReturn: null, totalCount: 0])
                return
            }
            ctMarkInputStudentIds = examMarkService.getAlreadyCtMarkStudents(examSchedule, subject, ctMarkFor)
            studentList = studentService.allStudentNotInList(section,ctMarkInputStudentIds)
            render(view: viewName, layout: layoutName, model: [ctMarkFor: ctMarkFor, examNameList:examNameList, examSchedule:examSchedule,className:className.name,section:section.name, examTerm:exam.shiftExam.examTerm.value,subject:subject.name, 'studentList': studentList, dataReturn:markEntryResult , totalCount: totalCount])
            return
        }

        //code to get optional subject students
        def subjectStudents
        if (totalCount == 0) {
            subjectStudents = studentSubjectsService.subjectStudentsExcludingList(section,subject,null)
            studentList = studentService.allStudentInList(subjectStudents)
            render(view: viewName, layout: layoutName, model: [ctMarkFor: ctMarkFor, examNameList:examNameList, examSchedule:examSchedule,className:className.name,section:section.name, examTerm:exam.shiftExam.examTerm.value,subject:subject.name, 'studentList': studentList, dataReturn: null, totalCount: 0])
            return
        }
        ctMarkInputStudentIds = examMarkService.getAlreadyCtMarkStudents(examSchedule,subject, ctMarkFor)
        subjectStudents = studentSubjectsService.subjectStudentsExcludingList(section, subject, ctMarkInputStudentIds)
        studentList = studentService.allStudentInList(subjectStudents)
        render(view: viewName, layout: layoutName, model: [ctMarkFor: ctMarkFor, examNameList:examNameList, examSchedule:examSchedule,className:className.name,section:section.name, examTerm:exam.shiftExam.examTerm.value,subject:subject.name, 'studentList': studentList, dataReturn:markEntryResult , totalCount: totalCount])
    }
    def listCtMark(Long examScheduleId) {
        LinkedHashMap gridData
        String result

        ExamSchedule examSchedule = ExamSchedule.read(examScheduleId)
        if(!examSchedule){
            gridData.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result = gridData as JSON
            render result
        }
        Exam exam = examSchedule.exam
        String ctMarkFor= params.ctMarkFor
        if (!ctMarkFor) {
            ctMarkFor= "CLASS_TEST1"
        }
        SubjectName subject = examSchedule.subject

        LinkedHashMap resultMap
        if (ctMarkFor == "CLASS_TEST1") {
            resultMap = examMarkService.getCtMarkEntryList(params, examSchedule, subject)
        }else if (ctMarkFor == "CLASS_TEST2") {
            resultMap = examMarkService.getCt2MarkEntryList(params, examSchedule, subject)
        } else if (ctMarkFor == "CLASS_TEST3") {
            resultMap = examMarkService.getCt3MarkEntryList(params, examSchedule, subject)
        }
        int totalCount = resultMap.totalCount
        if (totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveCtMark(CtExamMarkCommand markCommand) {
        if (!request.method.equals('POST')) {
            redirect(controller: 'exam', action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (markCommand.hasErrors()) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            def errorList = markCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        result = examMarkService.saveCtMark(markCommand)
        outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ExamMark examMark = ExamMark.read(id)
        if (!examMark) {
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        def principal = springSecurityService.principal
        if (!(SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN, ROLE_ADMIN,ROLE_SHIFT_INCHARGE")  || principal.username == examMark.createdBy || principal.username == examMark.updatedBy)){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You are not authenticated to edit this mark entry. Either ADMIN or ${examMark.createdBy} or ${examMark.updatedBy} can edit this. Please contact with admin.")
            outPut = result as JSON
            render outPut
            return
        }
        Exam exam = examMark.exam
        if (exam.isLocked == 1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Mark Entry locked. Please contact admin for allow mark entry")
            outPut = result as JSON
            render outPut
            return
        }

        String studentID= examMark.student.studentID+" - "+examMark.student.name+" - [Roll: "+examMark.student.rollNo+"]"
        result.put(CommonUtils.IS_ERROR,false)
        String ctMarkFor= params.ctMarkFor
        if (ctMarkFor == "CLASS_TEST1") {
            result.put('objid',examMark.id)
            result.put('stdid',examMark.student.id)
            result.put('attendStatus',examMark.ctAttendStatus.key)
            result.put('markObtain',examMark.ctObtainMark)
        }else if (ctMarkFor == "CLASS_TEST2") {
            result.put('objid',examMark.id)
            result.put('stdid',examMark.student.id)
            result.put('attendStatus',examMark.ct2Status.key)
            result.put('markObtain',examMark.ct2ObtainMark)
        } else if (ctMarkFor == "CLASS_TEST3") {
            result.put('objid',examMark.id)
            result.put('stdid',examMark.student.id)
            result.put('attendStatus',examMark.ct3Status.key)
            result.put('markObtain',examMark.ct3ObtainMark)
        } else {
            result.put('obj',examMark)
        }
        result.put('studentID',studentID)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        String examType = params.examType
        String ctMarkFor = params.ctMarkFor
        LinkedHashMap result = examMarkService.delete(id, examType, ctMarkFor)
        String outPut = result as JSON
        render outPut
    }
    def deleteCtMark(Long id) {
        LinkedHashMap result = examMarkService.deleteCtMark(id, params)
        String outPut = result as JSON
        render outPut
    }

    //hall Mark Entry
    def addHallMark(Long id){
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if(!examSchedule){
            flash.message = "Exam Schedule Not Fount. Please contact admin."
            redirect(action: 'index')
            return
        }
        Exam exam = examSchedule.exam
        if(exam.examStatus != ExamStatus.NEW){
            flash.message = "Mark Entry not allowed. Please contact admin."
            redirect(action: 'index')
            return
        }
        if(exam.isLocked == 1){
            flash.message = "Mark Entry locked. Please contact admin."
            redirect(action: 'index')
            return
        }

        ClassName className = exam.className
        Section section = exam.section
        SubjectName subject = examSchedule.subject

        String viewName = "hallMarkEntry"
        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }

        if (examSchedule.isHallPractical) {
            viewName = "hallMarkWithPracticalEntry"
        }

        def examNameList = shiftExamService.examNameDropDown()

        LinkedHashMap resultMap = examMarkService.getHallMarkEntryList(params, section, examSchedule, subject)
        int totalCount = resultMap.totalCount
        def markEntryResult = resultMap.results
        def studentList
        def ctMarkInputStudentIds

        if(examSchedule.isCompulsory){
            if (totalCount == 0) {
                studentList = studentService.allStudent(section)
                render(view: viewName, layout: layoutName, model: [examNameList:examNameList, hallWrittenMark: examSchedule.hallWrittenMark, hallObjectiveMark: examSchedule.hallObjectiveMark, hallPracticalMark: examSchedule.hallPracticalMark,hallSbaMark: examSchedule.hallSbaMark, hallInput5: examSchedule.hallInput5, className: className.name, examSchedule:examSchedule, section:section?.name, examTerm:exam.shiftExam.examTerm.value, subject:subject?.name, examDate:CommonUtils.getUiDateStr(examSchedule.hallExamDate),'studentList': studentList, dataReturn: null, totalCount: 0])
                return
            }
            ctMarkInputStudentIds = examMarkService.getAlreadyHallMarkStudents(examSchedule, subject)
            studentList = studentService.allStudentNotInList(section,ctMarkInputStudentIds)
            render(view: viewName, layout: layoutName, model: [examNameList:examNameList, hallWrittenMark: examSchedule.hallWrittenMark, hallObjectiveMark: examSchedule.hallObjectiveMark, hallPracticalMark: examSchedule.hallPracticalMark,hallSbaMark: examSchedule.hallSbaMark,hallInput5: examSchedule.hallInput5, className: className.name, examSchedule:examSchedule, section:section?.name, examTerm:exam.shiftExam?.examTerm.value, subject:subject?.name, examDate:CommonUtils.getUiDateStr(examSchedule.hallExamDate),'studentList': studentList, dataReturn:markEntryResult , totalCount: totalCount])
            return
        }
        //code to get optional subject students
        def subjectStudents
        if (totalCount == 0) {
            subjectStudents = studentSubjectsService.subjectStudentsExcludingList(section, subject, null)
            studentList = studentService.allStudentInList(subjectStudents)
            render(view: viewName, layout: layoutName, model: [examNameList:examNameList, hallWrittenMark: examSchedule.hallWrittenMark, hallObjectiveMark: examSchedule.hallObjectiveMark, hallPracticalMark: examSchedule.hallPracticalMark,hallSbaMark: examSchedule.hallSbaMark,hallInput5: examSchedule.hallInput5, className: className.name, examSchedule:examSchedule, section:section?.name, examTerm:exam.shiftExam?.examTerm.value, subject:subject?.name, examDate:CommonUtils.getUiDateStr(examSchedule.hallExamDate),'studentList': studentList, dataReturn: null, totalCount: 0])
            return
        }
        ctMarkInputStudentIds = examMarkService.getAlreadyHallMarkStudents(examSchedule,subject)
        subjectStudents = studentSubjectsService.subjectStudentsExcludingList(section,subject,ctMarkInputStudentIds)
        studentList = studentService.allStudentInList(subjectStudents)
        render(view: viewName, layout: layoutName, model: [examNameList:examNameList, hallWrittenMark: examSchedule.hallWrittenMark, hallObjectiveMark: examSchedule.hallObjectiveMark, hallPracticalMark: examSchedule.hallPracticalMark, hallSbaMark: examSchedule.hallSbaMark,hallInput5: examSchedule.hallInput5, className: className.name, examSchedule:examSchedule,section:section?.name, examTerm:exam.shiftExam?.examTerm.value, subject:subject?.name, examDate:CommonUtils.getUiDateStr(examSchedule.hallExamDate),'studentList': studentList, dataReturn:markEntryResult , totalCount: totalCount])
    }

    def listHallMark(Long examScheduleId) {
        LinkedHashMap gridData
        String result

        ExamSchedule examSchedule = ExamSchedule.read(examScheduleId)
        if(!examSchedule){
            gridData.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result = gridData as JSON
            render result
        }
        Exam exam = examSchedule.exam
        ClassName className = exam.className
        Section section = exam.section
        SubjectName subject = examSchedule.subject
        LinkedHashMap resultMap = examMarkService.getHallMarkEntryList(params, section, examSchedule, subject)
        int totalCount = resultMap.totalCount
        if (totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveHallMark(HallExamMarkCommand markCommand) {
        if (!request.method.equals('POST')) {
            redirect(controller: 'exam', action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        if (markCommand.hasErrors()) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            def errorList = markCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }

        result = examMarkService.saveHallMark(markCommand)
        outPut = result as JSON
        render outPut
    }

    def examMarkPrint(ExamMarkPrintCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }
        ExamSchedule examSchedule = command.examSchedule
        String reportFileName
        Exam exam = examSchedule.exam
        String querySql = """
        SELECT std.name as stdName, std.studentid, std.roll_no, 
            eMark.ct_attend_status, eMark.ct_mark, eMark.ct_obtain_mark, eMark.hall_attend_status, eMark.hall_mark, eMark.hall_obtain_mark,
            eMark.full_mark, eMark.g_point,eMark.l_grade,eMark.result_status,
            cs.ct_mark ct_exam_mark, cs.hall_mark hall_exam_mark, eSch.highest_mark,
            cs.class_name className, section.name as sectionName, cs.subject_name subjectName
            FROM exam_mark eMark
            inner join student std on eMark.student_id=std.id
            inner join exam exam on eMark.exam_id=exam.id
            inner join exam_schedule eSch on eMark.exam_schedule_id=eSch.id
            inner join section section on exam.section_id=section.id
            inner join v_std_class_subject cs on cs.class_name_id =exam.class_name_id and cs.subject_id=eSch.subject_id
            where eMark.active_status='ACTIVE' and eMark.exam_id in($exam.id) and eMark.exam_schedule_id in ($examSchedule.id) and std.student_status="NEW" order by section.name asc, std.roll_no asc;
        """

        if(command.inputType =='ctMark') {
            reportFileName = EXAM_MARK_JASPER_FILE
            if(examSchedule.isHallMarkInput){
                reportFileName = EXAM_MARK_DETAIL_VIEW_JASPER_FILE
            }
        } else {
            reportFileName = EXAM_MARK_JASPER_FILE
            if(examSchedule.isHallPractical) {
                reportFileName = EXAM_MARK_WITH_HALL_PRAC_JASPER_FILE
            }
            if(examSchedule.isHallMarkInput){
                reportFileName = EXAM_MARK_DETAIL_VIEW_JASPER_FILE
                if(examSchedule.isHallPractical) {
                    reportFileName = EXAM_MARK_WITH_Hall_PRAC_DETAIL_JASPER_FILE
                }
            }
        }


        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('scheduleIds', examSchedule.id.toString())
        paramsMap.put('examIds', exam.id.toString())
        paramsMap.put('examName', exam.name)
        paramsMap.put('year', exam.academicYear.value)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Exam_Mark"
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
//for Exam Mark report
    private static final String EXAM_MARK_JASPER_FILE = 'examMark'
    private static final String EXAM_MARK_WITH_HALL_PRAC_JASPER_FILE = 'examMarkWithHallPrac'
    private static final String EXAM_MARK_DETAIL_VIEW_JASPER_FILE = 'examMarkDetailView'
    private static final String EXAM_MARK_WITH_Hall_PRAC_DETAIL_JASPER_FILE = 'examMarkWithHallPracDetail'
}
