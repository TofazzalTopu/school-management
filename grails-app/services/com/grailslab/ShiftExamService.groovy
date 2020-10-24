package com.grailslab

import com.grailslab.command.ExamCommand
import com.grailslab.enums.ExamStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Exam
import com.grailslab.settings.ShiftExam
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ShiftExamService {
    def schoolService
    def springSecurityService
    def examService
    def messageSource
    def sectionService

    static final String[] sortColumns = ['id','examTerm','resultPublishOn']
    LinkedHashMap examPaginateList(GrailsParameterMap params){
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
        } else if (params.resultType && params.resultType == "college") {
            academicYear = schoolService.collegeWorkingYear()
        }

        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = ShiftExam.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
                eq("examStatus", ExamStatus.NEW)
            }
            if (sSearch) {
                or {
                    ilike('examName', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount

        if (totalCount > 0) {
            String publishDate
            String examPeriod
            String ctExam
            String weightOnResult
            String lockStatus
            for (shiftExam in results) {
                publishDate = CommonUtils.getUiDateStr(shiftExam.resultPublishOn)
                examPeriod = CommonUtils.getDateRangeStr(shiftExam.periodStart, shiftExam.periodEnd)
                ctExam = shiftExam.ctExam > 0 ? "${shiftExam.ctExam} CT":""
                lockStatus = shiftExam.isLocked ? "Locked":"Open"
                weightOnResult = shiftExam.weightOnResult? "${shiftExam.weightOnResult}%":"0%"
                dataReturns.add([DT_RowId: shiftExam.id, 0: shiftExam.examName, 1: ctExam, 2: lockStatus, 3: examPeriod, 4:weightOnResult, 5:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def examNameDropDown(AcademicYear academicYear = null) {
        def workingYears = schoolService.workingYears()
        def examList
        if (academicYear) {
            examList = ShiftExam.findAllByActiveStatusAndExamStatusAndAcademicYear(ActiveStatus.ACTIVE, ExamStatus.NEW, academicYear, [max: 10, sort: "id", order: "desc"])
        } else {
            examList = ShiftExam.findAllByActiveStatusAndExamStatusAndAcademicYearInList(ActiveStatus.ACTIVE, ExamStatus.NEW, workingYears, [max: 10, sort: "id", order: "desc"])
        }
        List dataReturns = new ArrayList()
        for (shiftExam in examList) {
            dataReturns.add([id: shiftExam.id, name: shiftExam.examName])
        }
        dataReturns
    }
    def saveOrUpdate(ExamCommand command) {
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
            result.put(CommonUtils.MESSAGE, "No Class found for ${command.examName}.")
            return result
        }
        List sectionIds = examSectionList.collect { it.id }
        String idsListStr = sectionIds.join(',')

        String createOrUpdateBy = springSecurityService.principal.username
        String message
        ShiftExam shiftExam
        ShiftExam savedExam
        if (command.id) {
            shiftExam = ShiftExam.get(command.id)
            shiftExam.examName = command.examName
            shiftExam.ctExam = command.ctExam
            shiftExam.examTerm = command.examTerm
            shiftExam.weightOnResult = command.weightOnResult
            shiftExam.classIds = command.classIds
            shiftExam.sectionIds = idsListStr
            shiftExam.examStatus = ExamStatus.NEW
            shiftExam.resultPublishOn = command.resultPublishOn
            shiftExam.periodStart = command.periodStart
            shiftExam.periodEnd = command.periodEnd
            shiftExam.updatedBy = createOrUpdateBy
            shiftExam.save(flush: true)
            Exam aExam
            for (section in examSectionList) {
                aExam = examService.getExam(shiftExam, section)
                if (aExam) {
                    aExam.name = command.examName
                    aExam.examTerm = shiftExam.examTerm
                    aExam.updatedBy = createOrUpdateBy
                } else {
                    aExam = new Exam(shiftExam: shiftExam, name: command.examName,
                            examTerm: shiftExam.examTerm, section: section,
                            className: section.className, academicYear: command.academicYear,
                            createdBy: createOrUpdateBy)
                }
                aExam.save()
            }

            List<Exam> deletedExams = examService.examsNotInSectionIdList(shiftExam, sectionIds)
            for (delExam in deletedExams) {
                delExam.updatedBy = createOrUpdateBy
                delExam.activeStatus = ActiveStatus.DELETE
                delExam.save()
            }

            message = "Exam Updated Successfully"
        } else {
            message = "Exam Created Successfully for selected Classes"
            shiftExam = new ShiftExam(examName: command.examName, ctExam: command.ctExam, examTerm: command.examTerm,
                    weightOnResult: command.weightOnResult, classIds: command.classIds, createdBy: createOrUpdateBy,
                    sectionIds: idsListStr, examStatus: ExamStatus.NEW, resultPublishOn: command.resultPublishOn, periodStart: command.periodStart, periodEnd: command.periodEnd, academicYear: command.academicYear)
            savedExam = shiftExam.save()
            if (savedExam) {
                for (section in examSectionList) {
                    new Exam(shiftExam: savedExam, name: command.examName, examTerm: savedExam.examTerm, section: section,
                            className: section.className, academicYear: command.academicYear, createdBy: createOrUpdateBy).save()
                }
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
    def deleteShiftExam(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        ShiftExam exam = ShiftExam.get(id)

        if(!exam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (exam.examStatus != ExamStatus.NEW) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Can't delete. Result Already Processing or Published.")
            return result
        }

        if (exam.examStatus.equals(ExamStatus.NEW)) {
            exam.activeStatus = ActiveStatus.INACTIVE
            exam.updatedBy = createOrUpdateBy
            exam.save(flush: true)
            result.put(CommonUtils.IS_ERROR, false)
            result.put(CommonUtils.MESSAGE, "Exam Deleted successfully.")
            return result
        }
        result.put(CommonUtils.IS_ERROR,true)
        result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
        return result
    }
    def lockShiftExams(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        ShiftExam shiftExam = ShiftExam.get(id)

        if(!shiftExam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        Integer isLocked = 1
        String message = "Mark entry Locked successfully"
        if (shiftExam.isLocked == 1) {
            message = "Mark entry open successfully"
            isLocked = 0
        }
        List<Exam> shiftExamList = examService.shiftExamList(shiftExam)
        shiftExamList.each {lockExam ->
            lockExam.updatedBy = createOrUpdateBy
            lockExam.isLocked = isLocked
            lockExam.save(flush: true)
        }
        shiftExam.isLocked = isLocked
        shiftExam.updatedBy = createOrUpdateBy
        shiftExam.save(flush: true)

        result.put(CommonUtils.IS_ERROR,false)
        result.put(CommonUtils.MESSAGE,message)
        return result
    }
}
