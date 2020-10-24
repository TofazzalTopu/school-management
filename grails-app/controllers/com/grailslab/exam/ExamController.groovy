package com.grailslab.exam

import com.grailslab.CommonUtils
import com.grailslab.command.ExamCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.ExamType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.ShiftExam
import grails.converters.JSON

class ExamController {
    def schoolService
    def examService
    def classNameService
    def shiftExamService

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        AcademicYear workingYear = schoolService.schoolWorkingYear()
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL);
        render(view: 'index', model: [resultType: 'school', dataReturn: null, totalCount: 0, classNameList:classNameList, workingYear: workingYear, academicYearList: academicYearList])
    }

    def college() {
        AcademicYear workingYear = schoolService.collegeWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.COLLEGE);
        render(view: 'index', model: [resultType: 'college', dataReturn: null, totalCount: 0,classNameList:classNameList, workingYear: workingYear, academicYearList: academicYearList])
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = shiftExamService.examPaginateList(params)
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

    def classExams(Long id) {
        ShiftExam shiftExam = ShiftExam.read(id)
        if (!shiftExam) {
            redirect(action: 'index')
            return
        }
        def classNameList = classNameService.classNameInIdListDD(shiftExam.classIds);
        Boolean isHtScheduleAdded
        List classExams = new ArrayList()

        for (clsName in classNameList) {
            isHtScheduleAdded = examService.isExamSchedulePublished(shiftExam, clsName.id)
            classExams.add([clasId: clsName.id, name: clsName.name,  isHtScheduleAdded: isHtScheduleAdded])
        }
        render(view: 'classExams', model: [classExams:classExams, shiftExam: shiftExam])
    }

    def editExams(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ShiftExam shiftExam = ShiftExam.read(id)
        if (!shiftExam) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,shiftExam)
        outPut = result as JSON
        render outPut

    }

    def entry() {
        def examNameList = shiftExamService.examNameDropDown()
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/exam/markEntrySelection', model: [classNameList:classNameList, examNameList: examNameList])
    }

    def save(ExamCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = shiftExamService.saveOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }

    def deleteShiftExam(Long id) {
        LinkedHashMap result = shiftExamService.deleteShiftExam(id)
        String outPut = result as JSON
        render outPut
    }
    def lockShiftExam(Long id) {
        LinkedHashMap result = shiftExamService.lockShiftExams(id)
        String outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        LinkedHashMap result = examService.inactive(id)
        String outPut = result as JSON
        render outPut
    }
}

