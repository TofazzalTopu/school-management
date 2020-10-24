package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.LessonPlanCommand
import com.grailslab.report.CusJasperReportDef
import com.grailslab.settings.ClassName
import com.grailslab.settings.LessonWeek
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate

import static com.grailslab.CommonUtils.getStringToDate

class LessonPlanController {
    def jasperService
    def messageSource
    def sectionService
    def lessonService
    def schoolService
    def classSubjectsService
    def baseService
    def classNameService

    def download(LessonPlanCommand command) {
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }

        Integer weekNum = command.weekNo
        Date startDate
        Date endDate
        if(weekNum){
            LessonWeek lessonWeek=lessonService.getWeekByWeekNo(weekNum)
            startDate = lessonWeek.startDate
            endDate = lessonWeek.endDate
        } else {
            startDate = command.startDate
            endDate = command.endDate
        }
        if (!startDate || !endDate) {
            flash.message = "Please select lesson week or startDate and endDate"
            render(view: '/baseReport/reportNotFound')
            return
        }

        ClassName className = command.className
        Section section = command.section
        if (!className && section) {
            className = section.className
        }

        String reportName = LESSON_PLAN_JASPER_FILE
        String reportLogo = schoolService.reportLogoPath()
        String sqlParam = getLessonPlanQueryStr(className.id, startDate, endDate, command.subject?.id, section?.id)
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('className', className.name)
        paramsMap.put('weekNum', weekNum)
        paramsMap.put('lessonDateRange', CommonUtils.getDateRange(startDate, endDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "lesson_plan"
        jasperReport.reportName = reportName
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
    private String getLessonPlanQueryStr(Long classNameId, Date startDate, Date endDate, Long subjectId, Long sectionId) {
        String startDateStr = CommonUtils.getMysqlQueryDateStr(startDate)
        String endDateStr = CommonUtils.getMysqlQueryDateStr(endDate)
        String sqlParam = " and lesson.active_status = 'ACTIVE' "
        if(subjectId){
            sqlParam+=" and subject.id=${subjectId}"
        }
        if(sectionId){
            sqlParam+=" and lesson.section_id=${sectionId}"
        } else {

        }
        String querySql = """
            SELECT lesson.exam_term, lesson.week_start_date, lesson.week_end_date,  section.name as sectionName, lesson.week_number as weekNumber,
                lesson.lesson_date as lessonDate, subject.name as subjectName,
                lesson_details.topics, lesson_details.home_work, lesson_details.exam_date, lesson_details.oral_home_work, lesson_details.written_home_work
                FROM lesson
                join lesson_details on lesson.id=lesson_details.lesson_id
                inner join section on lesson.section_id=section.id
                inner join subject_name subject on lesson_details.subject_id=subject.id
                where lesson.class_name_id = ${classNameId} AND lesson_details.active_status = 'ACTIVE' and lesson.lesson_date between '${startDateStr}' and '${endDateStr}' ${sqlParam}
                order by section.name, lesson.lesson_date asc, subject.sort_position asc
        """
        return querySql
    }

    //lesson Plan
    private static final String LESSON_PLAN_JASPER_FILE = 'lessonPlan'
}
