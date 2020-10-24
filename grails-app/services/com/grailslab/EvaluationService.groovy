package com.grailslab

import com.grailslab.command.EvaluationCommand
import com.grailslab.command.EvaluationScheduleCommand
import com.grailslab.enums.ExamStatus
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Evaluation
import com.grailslab.settings.EvaluationSchedule
import com.grailslab.settings.Exam
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class EvaluationService {
    def springSecurityService
    def messageSource
    def sectionService
    def schoolService

    static final String[] sortColumns = ['id','scheduleName']
    LinkedHashMap scheduleList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = EvaluationSchedule.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
            }
            if (sSearch) {
                or {
                    ilike('scheduleName', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            String publishDate
            String examType
            String weightOnResult
            for (evaluationSchedule in results) {
                dataReturns.add([
                        DT_RowId: evaluationSchedule.id,
                        0: evaluationSchedule.scheduleName,
                        1: evaluationSchedule.periodStart.format('dd-MMM-yyyy') + ' - ' + evaluationSchedule.periodEnd.format('dd-MMM-yyyy'),
                        2: evaluationSchedule.classIds,
                        3:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def examNameDropDown(AcademicYear academicYear = null) {
        def workingYears = schoolService.workingYears()
        def examList
        if (academicYear) {
            examList = EvaluationSchedule.findAllByActiveStatusAndAcademicYear(ActiveStatus.ACTIVE, academicYear, [max: 10, sort: "id", order: "desc"])
        } else {
            examList = EvaluationSchedule.findAllByActiveStatusAndAcademicYear(ActiveStatus.ACTIVE, workingYears, [max: 10, sort: "id", order: "desc"])
        }
        List dataReturns = new ArrayList()
        for (shiftExam in examList) {
            dataReturns.add([id: shiftExam.id, name: shiftExam.scheduleName])
        }
        dataReturns
    }

    def classExamsDropDownAsSectionName(GrailsParameterMap params){

        def c = Exam.createCriteria()
        def results = c.list() {
            if (groupName) {
                createAlias('section','sec')
            }
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                if (examType == 'new') {
                    eq("examStatus", ExamStatus.NEW)
                } else if(examType == 'working') {
                    'in'("examStatus", ExamStatus.resultWorkingList())
                } else if (examType == 'publishing'){
                    'in'("examStatus", ExamStatus.resultPublishingList())
                } else if (examType == 'published') {
                    eq("examStatus", ExamStatus.PUBLISHED)
                }
            }
        }
        List dataReturns = new ArrayList()
        for (exam in results) {
            dataReturns.add([id: exam.id, name: exam.section.name])
        }
        return dataReturns
    }

    LinkedHashMap getEvaluationList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 1
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsAuthor,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Evaluation.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("name", name )
                eq("rollNo", rollNo )
                eq("std.studentStatus", StudentStatus.NEW )
            }
            if (sSearch) {
                or {

                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        Student student
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { Evaluation evaluation ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: evaluation.studentId, 0: serial, 1: student.name, 2: student.rollNo, 3:evaluation.scheduleId,4:evaluation.sectionId,5:" "])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def saveOrUpdateSchedule(EvaluationScheduleCommand command) {

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        LinkedHashMap result = new LinkedHashMap()

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        def examSectionList = sectionService.sectionsByClassIds(command.classIds)
        if (!examSectionList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class found for ${command.scheduleName}.")
            return result
        }

        List sectionIds = examSectionList.collect { it.id }
        String idsListStr = sectionIds.join(',')
        String message
        EvaluationSchedule evaluationSchedule
        if (command.id) {
            evaluationSchedule = EvaluationSchedule.get(command.id)
            evaluationSchedule.scheduleName = command.scheduleName
            evaluationSchedule.classIds = command.classIds
            evaluationSchedule.periodEnd = command.periodEnd
            evaluationSchedule.periodStart = command.periodStart
            evaluationSchedule.sectionIds = idsListStr
            evaluationSchedule.updatedBy = createOrUpdateBy
            if (evaluationSchedule.save(flush: true)) {
                message = "Evaluation Schedule updated Successfully for selected Classes"
            } else {
                message = "Error Occur in updating Evaluation Schedule creation"
            }
        } else {
            evaluationSchedule = new EvaluationSchedule(command.properties)
            evaluationSchedule.createdBy = createOrUpdateBy
            evaluationSchedule.academicYear = academicYear
            evaluationSchedule.sectionIds = idsListStr
            if (evaluationSchedule.save(flush: true)) {
                message = "Evaluation Schedule Created Successfully for selected Classes"
            } else {
                message = "Error Occur in saving Evaluation Schedule creation"
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def deleteSchedule(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        EvaluationSchedule evaluationSchedule = EvaluationSchedule.get(id)
        if (!evaluationSchedule) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        evaluationSchedule.activeStatus = ActiveStatus.INACTIVE
        evaluationSchedule.updatedBy = springSecurityService.principal?.username
        if (evaluationSchedule.save(flush: true)) {
            result.put(CommonUtils.IS_ERROR, false)
            result.put(CommonUtils.MESSAGE, "Schedule Deleted successfully.")
            return result
        }
        result.put(CommonUtils.IS_ERROR, true)
        result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
        return result
    }

    def saveEvaluation(EvaluationCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        String message
        Evaluation evaluation = new Evaluation(command.properties)
        if (evaluation.save(flush: true)) {
            message = 'success'
        } else {
            message = 'Error in save'
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}
