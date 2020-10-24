package com.grailslab.teacher

import com.grailslab.command.ClassRoutineCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.hr.Employee
import com.grailslab.settings.ClassRoutine
import grails.converters.JSON

class TeacherController {
    def classSubjectsService
    def classNameService
    def sectionService
    def classRoutineService
    def schoolService

    def index() {
        render(view: '/common/dashboard', layout: 'adminLayout')
    }

    def createRoutine() {
        ClassRoutine classRoutine
        List sectionList
        List subjectList
        if (params.id) {
            classRoutine = ClassRoutine.get(params.getLong('id'))
            if (classRoutine) {
                sectionList = sectionService.classNameWiseSectionDropDownList(classRoutine.className, classRoutine.academicYear)
                subjectList = classSubjectsService.subjectDropDownList(classRoutine.className, classRoutine.section.groupName)
            }
        }
        def classNameList = classNameService.allClassNames()
        Employee employee = schoolService.loggedInEmployee()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: 'createRoutine', model: [academicYearList: academicYearList, employeeId: employee?.id, classNameList: classNameList, classRoutine: classRoutine, sectionList: sectionList, subjectList: subjectList])
    }

    def listRoutine() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = classRoutineService.teacherRoutineList(params)
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

    def deleteRoutine(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = classRoutineService.deleteRoutine(id)
        String outPut = result as JSON
        render outPut
    }

    def saveRoutine(ClassRoutineCommand command) {
        if (!request.method.equals('POST')) {
            flash.message = "Request Method not allow here."
            return
        }
        LinkedHashMap result = classRoutineService.saveRoutine(command)
        String outPut = result as JSON
        render outPut
    }
    def myAttendance() {
        Employee employee = schoolService.loggedInEmployee()
        render(view: 'myAttendance', model: [employeeId: employee?.id])
    }
}
