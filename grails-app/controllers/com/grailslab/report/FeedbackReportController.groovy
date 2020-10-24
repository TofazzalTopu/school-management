package com.grailslab.report

import com.grailslab.CommonUtils
import com.grailslab.command.report.AverageFeedbackCommand
import com.grailslab.command.report.FeedbackByStudentCommand
import com.grailslab.command.report.StudentFeedbackCommand
import com.grailslab.command.report.SubWiseFeedbackCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.settings.LessonWeek
import com.grailslab.stmgmt.Student

class FeedbackReportController {
    def classNameService
    def schoolService
    def jasperService
    def baseService

    private static final String FEEDBACK_REPORT_JASPER_FILE = 'subjectWiseFeedback'
    private static final String STUDENT_RATING_REPORT_JASPER_FILE = 'studentsFeedback'
    private static final String AVERAGE_REPORT_JASPER_FILE = 'averageFeedback'
    private static final String STUDENT_FEEDBACK_REPORT_JASPER_FILE = 'feedbackByStudent'
    def index() {
        render (view: '/common/dashboard', layout:'moduleLessonAndFeedbackLayout')
    }
    def report(){
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL);
        render (view: '/teacher/lesson/feedbackReport', model: [classNameList: classNameList])
    }

    def subjectFeedback(SubWiseFeedbackCommand command) {

        String sqlParam = " lfd.active_status = 'ACTIVE' and std.active_status = 'ACTIVE' and sub.active_status = 'ACTIVE' and cls.active_status =  'ACTIVE' and sec.active_status = 'ACTIVE' and lfedbk.active_status = 'ACTIVE' and lw.active_status = 'ACTIVE'"
       String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_FEEDBACK, FEEDBACK_REPORT_JASPER_FILE)
        if (command.className) {
            sqlParam += " and cls.id='"+ command.className +"' "
        }
        if (command.sectionName) {
            sqlParam += " and sec.id='"+ command.sectionName +"' "
        }
        if (command.subjectName) {
            sqlParam += " and sub.id ='"+ command.subjectName +"' "
        }
        if (command.weekNo) {
            sqlParam += " and lw.week_number ='"+ command.weekNo +"' "
        }
        if (command.sortedBy) {
            sqlParam += " order by "+ command.sortedBy+";"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "subjectwise_feedback"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.feedbackPrintType)
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
    def studentsFeedback(StudentFeedbackCommand command){
        String sqlParam = " std.active_status = 'ACTIVE' and cls.active_status =  'ACTIVE' and sec.active_status = 'ACTIVE' and lf.active_status = 'ACTIVE' and lw.active_status = 'ACTIVE'"
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_FEEDBACK, STUDENT_RATING_REPORT_JASPER_FILE)
        if (command.stdClassName) {
            sqlParam += " and cls.id='"+ command.stdClassName +"' "
        }
        if (command.stdSectionName) {
            sqlParam += " and sec.id='"+ command.stdSectionName +"' "
        }
        if (command.stdWeekNo) {
            sqlParam += " and lw.week_number ='"+ command.stdWeekNo +"' "
        }
        if (command.stdRating) {
            sqlParam += " and lfa.average <='"+ command.stdRating +"' "
        }
        if (command.stdSortedBy) {
            sqlParam += " order by "+ command.stdSortedBy
        }
        LessonWeek lessonWeek = LessonWeek.findByWeekNumber(command.stdWeekNo)
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put("weekStart", lessonWeek?.startDate)
        paramsMap.put("weekEnd", lessonWeek?.endDate)
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "students_feedback"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.stdPrintType)
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
    def averageFeedback(AverageFeedbackCommand command) {
        String sqlParam = " std.active_status = 'ACTIVE' and cn.active_status =  'ACTIVE' and sec.active_status = 'ACTIVE' and lf.active_status = 'ACTIVE' and lw.active_status = 'ACTIVE'"
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_FEEDBACK, AVERAGE_REPORT_JASPER_FILE)
        if (command.avgClassName) {
            sqlParam += " and cn.id='"+ command.avgClassName +"' "
        }
        if (command.avgSectionName) {
            sqlParam += " and sec.id='"+ command.avgSectionName +"' "
        }
        if (command.avgWeekNo) {
            sqlParam += " and lw.week_number ='"+ command.avgWeekNo +"' "
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "average_feedback"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.avgPrintType)
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
    def feedbackByStudent(FeedbackByStudentCommand command) {
        Student student=Student.read(command.studentName)
        if(!student){
            flash.message="Student not found."
            redirect(action: 'index')
            return
        }
        String sqlParam = " lfd.active_status = 'ACTIVE' and sub.active_status = 'ACTIVE' and lfedbk.active_status = 'ACTIVE' and lw.active_status = 'ACTIVE'"
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_FEEDBACK, STUDENT_FEEDBACK_REPORT_JASPER_FILE)
        if (command.studentName) {
            sqlParam += " and lfd.student_id='"+ student.id +"' "
        }
        if (command.stuSubjectName) {
            sqlParam += " and sub.id='"+ command.stuSubjectName +"' "
        }
        if (command.stuWeekNo) {
            sqlParam += " and lw.week_number ='"+ command.stuWeekNo +"' "
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('studentName', student.name)
        paramsMap.put('studentClass', student.className?.name)
        paramsMap.put('studentSection', student.section?.name)
        paramsMap.put('studentRollNo', student.rollNo)
        paramsMap.put('stdId', student.studentID)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "feedback_by_student"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.stuPrintType)
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
}
