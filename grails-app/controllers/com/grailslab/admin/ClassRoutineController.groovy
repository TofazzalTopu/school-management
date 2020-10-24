package com.grailslab.admin

import com.grailslab.command.ClassRoutineCommand
import com.grailslab.enums.AcaYearType
import grails.converters.JSON

class ClassRoutineController {

    def studentService
    def schoolService
    def classRoutineService
    def uploadService
    def messageSource
    def classNameService

    def index() {
        def classNameList = classNameService.allClassNames()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        render(view: '/classRoutine/classRoutine', model: [classNameList: classNameList, academicYearList: academicYearList])
    }
    def teacherRoutine() { }

    def list() {
        LinkedHashMap gridData
        String result
        String routineType = params.routineType
        LinkedHashMap resultMap
        if(routineType == "teacherRoutine"){
            resultMap = classRoutineService.teacherRoutineList(params)
        }else{
            resultMap = classRoutineService.classRoutineList(params)
        }

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def classRoutineCreate(){
        def classNameList = classNameService.allClassNames()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        render(view: '/classRoutine/classRoutineCreateOrEdit', model: [academicYearList: academicYearList, classNameList: classNameList])
    }

    def teacherRoutineCreate(){
        def classNameList = classNameService.allClassNames()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        render(view: '/classRoutine/teacherRoutineCreateOrEdit', model: [academicYearList: academicYearList, classNameList: classNameList])
    }

    def save(ClassRoutineCommand command) {
        if (!request.method.equals('POST')) {
            flash.message="Request Method not allow here."
            return
        }
        LinkedHashMap result = classRoutineService.saveRoutine(command)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = classRoutineService.deleteRoutine(id)
        String outPut = result as JSON
        render outPut
    }

}
