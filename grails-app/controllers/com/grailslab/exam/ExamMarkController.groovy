package com.grailslab.exam

import com.grailslab.CommonUtils
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.ExamStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.ShiftExam
import grails.converters.JSON

class ExamMarkController {
    def schoolService
    def examMarkService
    def examScheduleService
    def examService
    def classNameService
    def shiftExamService

    def index() {}

    def markEntry(Long id) {
        def examNameList
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)


        Exam exam=Exam.read(id)
        if (!exam) {
            examNameList = shiftExamService.examNameDropDown()
            render(view: '/exam/examMark/markEntry', model: [academicYearList: academicYearList, dataReturn: null, examNameList: examNameList])
            return
        }
        ShiftExam shiftExam = exam.shiftExam
        examNameList = shiftExamService.examNameDropDown(shiftExam.academicYear)
        LinkedHashMap resultMap = examScheduleService.examScheduleList(exam)
        def classNameList = classNameService.classNameInIdListDD(shiftExam.classIds);
        def sectionNameList = examService.classExamsDropDownAsSectionName(shiftExam, exam.className, null, 'working');
        render(view: '/exam/examMark/markEntry', model: [academicYearList: academicYearList, dataReturn: resultMap.results, exam:exam, classNameList: classNameList, examNameList: examNameList, shiftExam:shiftExam, sectionNameList:sectionNameList])
    }

    def specialMarkEntry(Long id) {
        if (!request.method.equals('POST')) {
            redirect(controller: 'exam', action: 'index')
            return
        }

        LinkedHashMap result = examMarkService.specialMarkEntry(id, params)
        String outPut = result as JSON
        render outPut
    }

    def deleteHallMark(Long id){
        LinkedHashMap result = examMarkService.deleteHallMark(id)
        String outPut = result as JSON
        render outPut
    }



    def editCtMark(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if(!examSchedule){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Exam Schedule Not found')
            outPut = result as JSON
            render outPut
            return
        }
        Exam exam = examSchedule.exam
        if(exam.examStatus==ExamStatus.PUBLISHED){
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Result Already Published')
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'CT mark allowed to edit successfully')
        outPut = result as JSON
        render outPut
    }

    def editHallMark(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if(!examSchedule){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Exam Schedule Not found')
            outPut = result as JSON
            render outPut
            return
        }
        Exam exam = examSchedule.exam
        if(exam.examStatus==ExamStatus.PUBLISHED){
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Result Already Published')
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Hall mark allowed to edit successfully')
        outPut = result as JSON
        render outPut
    }

}


