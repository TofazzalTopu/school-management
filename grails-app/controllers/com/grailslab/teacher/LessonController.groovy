package com.grailslab.teacher

import com.grailslab.CommonUtils
import com.grailslab.command.LessonCommand
import com.grailslab.command.LessonUpdateCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.ExamTerm
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.settings.*
import grails.converters.JSON

class LessonController {

    def lessonService
    def classSubjectsService
    def sectionService
    def employeeService
    def messageSource
    def springSecurityService
    def classNameService
    def schoolService

    def index(){
        render (view: '/common/dashboard',layout: 'moduleLessonAndFeedbackLayout')
    }

    def lessonDetail() {
        AcademicYear workingYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/teacher/lesson/lesson', model: [workingYear:workingYear,
                                                       academicYearList:academicYearList,
                                                       classList:classList])
    }

    def add(){
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/teacher/lesson/add', model: [classNameList: classNameList])
    }

    def report(){
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        def lessonWeekList = lessonService.lessonWeeksDropDownList(null)
        render(view: '/teacher/lesson/report', model: [classNameList:classNameList, lessonWeekList:lessonWeekList])
    }

    def feedback(Long id) {
        LessonDetails lessonDetails = LessonDetails.read(id)
        if (!lessonDetails) {
            redirect(controller: 'lesson',action: 'index')
            return
        }
        Lesson lessonObj = lessonDetails?.lesson
        String lessonDate = CommonUtils.getUiDateStr(lessonObj.lessonDate)
        Section section = lessonObj.section
        render(view: '/teacher/lesson/feedback', model: [lessonDate:lessonDate, subjectName:lessonDetails?.subject?.name])
    }

    def save(LessonCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        result = lessonService.saveLesson(params, command)
        outPut = result as JSON
        render outPut

    }
    
    def delete(Long id, Long lessonDetailId) {
        LinkedHashMap result = lessonService.deleteLessonPlan(id, lessonDetailId)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        LessonDetails lessonDetails = LessonDetails.read(id)
        if (!lessonDetails) {
            redirect(controller: 'lesson',action: 'index')
            return
        }
        Lesson lessonObj = lessonDetails?.lesson
        ClassName className = lessonObj.className
        Section section = lessonObj.section
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        def sectionList = sectionService.classNameWiseSectionDropDownList(className, section?.academicYear)
        def subjectList = classSubjectsService.subjectDropDownList(className)
        render(view: '/teacher/lesson/editLesson',model: [section: section,
                                                          className: className,
                                                          subjectList:subjectList,
                                                          lessonObj:lessonObj,
                                                          lessonDetails:lessonDetails,
                                                          classNameList:classNameList,
                                                          sectionList:sectionList])

    }
    
    def create() {
        Long sectionId = params.getLong("sectionId")
        Long classNameId = params.getLong("classNameId")

        ClassName className
        Section section
        if (classNameId) {
            className = ClassName.read(classNameId)
        }
        if (sectionId) {
            section = Section.read(sectionId)
        }
        if (section && !className) {
            className = section?.className
        }

        if (!className) {
            redirect(controller: 'lesson', action: 'index')
            return
        }

        ExamTerm examTerm = null
        if(params.examTerm){
            examTerm = ExamTerm.valueOf(params.examTerm)
        }
        SubjectName subject
        if(params.subject){
            subject = SubjectName.read(params.getLong('subject'))
        }
        Date lessonDate = new Date()
        if (params.lessonDate) {
            lessonDate = params.date('lessonDate','dd/MM/yyyy')
        }
        def principal = springSecurityService.principal

        Employee workingTeacher= employeeService.findByEmpId(principal?.userRef)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        def sectionList = sectionService.classNameWiseSectionDropDownList(className, section?.academicYear)
        def subjectList = classSubjectsService.subjectDropDownList(className, section?.groupName)
        def result = lessonService.todayWorkingLessonList(className, section, principal?.username)
        render(view: '/teacher/lesson/createLesson', model: [workingTeacher :workingTeacher,
                                                             className: className,
                                                             section: section,
                                                             subject: subject,
                                                             lessonList: result,
                                                             subjectList: subjectList,
                                                             examTerm:examTerm,
                                                             sectionList:sectionList,
                                                             classNameList:classNameList,
                                                             lessonDate: lessonDate])
    }

    def update(LessonUpdateCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        result = lessonService.updateLesson(command)
        outPut = result as JSON
        render outPut
    }
    
    def lessonPlan() {
        Long sectionId = params.getLong("sectionId")
        Long classNameId = params.getLong("classNameId")

        ClassName className
        Section section
        if (classNameId) {
            className = ClassName.read(classNameId)
        }
        if (sectionId) {
            section = Section.read(sectionId)
        }
        if (section && !className) {
            className = section?.className
        }
        if (!className) {
            redirect(controller: 'lesson', action: 'index')
            return
        }

        def lessonWeekList = lessonService.lessonWeeksList(className, section)
        Date startDate
        Date endDate
        LessonWeek lessonWeek
        if(params.weekNo){
            lessonWeek = lessonService.getWeekByWeekNo(params.getInt("weekNo"))
            if (lessonWeek) {
                startDate = lessonWeek.startDate
                endDate = lessonWeek.endDate
            }
        } else {
            lessonWeek = lessonService.getWeek(new Date().clearTime())
            if (lessonWeek) {
                startDate = lessonWeek.startDate
                endDate = lessonWeek.endDate
            } else {
                startDate = new Date().clearTime()
                endDate = new Date().plus(7)
            }
        }

        SubjectName loadSubject = null
        if(params.subjectId){
            loadSubject = SubjectName.read(params.getLong('subjectId'))
        }
        String lessonDateRange = CommonUtils.getDateRange(startDate, endDate)
        def subjectList = classSubjectsService.subjectDropDownList(className)
        def result = lessonService.manageLessonList(section, startDate, endDate, loadSubject?.id)

        render(view: '/teacher/lesson/lessonList', model: [lessonList: result, weekNumber: lessonWeek?.weekNumber, className: className, section: section, subjectList: subjectList, loadSubject:loadSubject, lessonWeekList:lessonWeekList, lessonDateRange: lessonDateRange])
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap =sectionService.sectionPaginateList(params)

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
}


