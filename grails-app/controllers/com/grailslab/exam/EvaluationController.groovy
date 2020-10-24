package com.grailslab.exam

import com.grailslab.CommonUtils
import com.grailslab.command.EvaluationCommand
import com.grailslab.command.EvaluationScheduleCommand
import com.grailslab.command.ListExamClassCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Evaluation
import com.grailslab.settings.EvaluationSchedule
import com.grailslab.settings.Section
import grails.converters.JSON

class EvaluationController {

    def schoolService
    def classNameService
    def evaluationService
    def messageSource
    def sectionService
    def studentService

    def schedule() {
        AcademicYear workingYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: 'schedule', model: [
                dataReturn: null,
                totalCount: 0,
                workingYear: workingYear,
                classNameList: classNameList,
                academicYearList: academicYearList
        ])
    }

    def scheduleList() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = evaluationService.scheduleList(params)
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

    def saveSchedule(EvaluationScheduleCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'schedule')
            return
        }
        LinkedHashMap result = evaluationService.saveOrUpdateSchedule(command)
        String outPut = result as JSON
        render outPut
    }

    def editSchedule(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        EvaluationSchedule evaluationSchedule = EvaluationSchedule.read(id)
        if (!evaluationSchedule) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, evaluationSchedule)
        outPut = result as JSON
        render outPut
    }

    def deleteSchedule(Long id) {
        LinkedHashMap result = evaluationService.deleteSchedule(id)
        String outPut = result as JSON
        render outPut
    }

    def evaluation() {
        Long scheduleId = params.getLong('examName')
        EvaluationSchedule schedule = EvaluationSchedule.read(scheduleId)
        if (schedule) {
            ClassName className = ClassName.read(params.getLong('className'))
            Section section = Section.read(params.getLong('section'))
            def studentList = studentService.allStudent(section)
            List dataReturn = new ArrayList()
            render (view: 'evaluation', model: [
                    schedule: schedule,
                    className: className,
                    section: section,
                    studentList: studentList,
                    dataReturn: dataReturn
            ])
        }
    }

    def entry() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render (view: 'entry', model: [academicYearList: academicYearList])
    }

    def saveEvaluation(EvaluationCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'entry')
            return
        }
        LinkedHashMap result = evaluationService.saveEvaluation(command)
        String outPut = result as JSON
        render outPut
    }

    def report() { }

    def yearExamNameList(ListExamClassCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        def examNameList = evaluationService.examNameDropDown(command.academicYear)

        if(!examNameList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Exam added")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('examNameList', examNameList)
        outPut = result as JSON
        render outPut
        return
    }
    //Class list for a given shift exam
    def examClassList(ListExamClassCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        def classNameList = classNameService.classNameInIdListDD(command.scheduleName?.classIds);

        if(!classNameList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class added")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('classNameList', classNameList)
        outPut = result as JSON
        render outPut
        return
    }
    //List all exam for a class given Shift exam and class name
    def sectionExamList(ListExamClassCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        def sectionNameList = sectionService.classSectionsDDList(command.className, command.groupName, command.academicYear);
        if(!sectionNameList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section schedule created or published. Please contact admin")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('sectionNameList', sectionNameList)
        outPut = result as JSON
        render outPut
        return
    }

    def listEvaluation(Long ScheduleId) {
        LinkedHashMap gridData
        String result
        Evaluation evaluation = Evaluation.read(ScheduleId)
        if(!evaluation){
            gridData.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result = gridData as JSON
            render result
        }
        LinkedHashMap resultMap = evaluationService.getEvaluationList(params)
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

}
