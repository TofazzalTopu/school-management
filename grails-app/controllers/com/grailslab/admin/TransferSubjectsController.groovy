package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.AddTransferSubjectCommand
import com.grailslab.command.TransferSubjectCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.settings.ClassName
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class TransferSubjectsController {
    def schoolService
    def classNameService
    def classSubjectsService
    def studentSubjectsService
    def studentService

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentSubject/transferSubjectPage', model: [classNameList:classNameList, academicYearList: academicYearList])

    }
    def college() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        render(view: '/admin/studentSubject/transferSubjectPage', model: [classNameList:classNameList, academicYearList: academicYearList])

    }

    def loadSubjects(TransferSubjectCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassName className = command.className
        if (!command.academicYear || !className) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select Academic year class name to load student subjects")
            outPut = result as JSON
            render outPut
            return
        }

        def optComOptions = classSubjectsService.subjectChooseOptComDDList(className, command.sectionName?.groupName)
        if (!optComOptions) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No Optional/Alternative subject to map.")
            outPut = result as JSON
            render outPut
            return
        }
        def studentList = studentService.listStudent(command.academicYear, className, command.sectionName, command.gender, command.religion)
        List studentSubjectsList = new ArrayList()
        String subjects
        def results
        int serial = 1
        for (student in studentList) {
            results = studentSubjectsService.alternativeSubject(student)
            subjects = results? results.collect {it.subject.name}.join(", "):""
            studentSubjectsList.add([id: student.id, serial: serial++, name: student.name, studentId: student.studentID, rollNo: student.rollNo, subjects: subjects])
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('studentList', studentSubjectsList)
        result.put('optComOptions', optComOptions)
        result.put('allowOptionalSubject', className.allowOptionalSubject)
        outPut = result as JSON
        render outPut
    }

    def saveStuTransferedSubject(AddTransferSubjectCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if(!command.selectedStdId){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select student to transfer subject")
            outPut = result as JSON
            render outPut
            return
        }

        result = studentSubjectsService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }
}
