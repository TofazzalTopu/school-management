package com.grailslab.exam


import com.grailslab.command.EasMarkCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.AvailableRoles
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.settings.Section
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import com.grailslab.gschoolcore.ActiveStatus

class EasController {
    def classNameService
    def studentService
    def easService
    def schoolService
    def employeeService
    def springSecurityService

    def index() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        String layoutName = "moduleExam&ResultLayout"
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
        }
        render(view: '/exam/easMark',layout: layoutName,model: [academicYearList:academicYearList, workingYear:academicYear, classList:classList])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =easService.easPaginateList(params)

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

    def easEntry(Long id) {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        Section section = Section.read(id)

        String layoutName = "moduleExam&ResultLayout"
        def employeeList
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())){
            layoutName = "adminLayout"
            def principal = springSecurityService.principal
            Employee workingTeacher= employeeService.findByEmpId(principal?.userRef)
            employeeList = new ArrayList()
            employeeList.add([id: workingTeacher.id, name: "${workingTeacher.empID} - ${workingTeacher.name}"])
        } else {
            employeeList = employeeService.allEmployeeDropDownList()
        }
        if(!section){
            render(view: '/exam/easMarkEntry',layout: layoutName,model: [employeeList: employeeList, classNameList: classNameList])
            return
        }
        def studentList = studentService.allStudent(section)
        render(view: '/exam/easMarkEntry',layout: layoutName,model: [employeeList: employeeList, classNameList: classNameList, section:section, studentList:studentList])
    }

    def easList(Long id){
        LinkedHashMap gridData
        String result
        Section section = Section.read(id)
        if(!section){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }

        LinkedHashMap resultMap = easService.getEasMarkList(params, section)

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

    def saveEas(EasMarkCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = easService.saveOrUpdateEas(command)
        String outPut = result as JSON
        render outPut
    }
    def delete(Long id) {
        LinkedHashMap result = easService.inactive(id)
        String outPut = result as JSON
        render outPut
    }
}
