package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.StudentSubjectCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import grails.converters.JSON

class StudentSubjectsController {

    def sectionService
    def classSubjectsService
    def studentSubjectsService
    def classNameService
    def schoolService

    def index() {
        def classNameList = classNameService.classNamesList(AcaYearType.SCHOOL)
        if (!classNameList) {
            render (view: '/admin/studentSubject/studentSubjectPage', model: [dataReturns:null])
            return
        }
        AcademicYear academicYear= null
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        def allSection = sectionService.allSections(AcaYearType.SCHOOL, academicYear)
        List dataReturns = new ArrayList()
        List subjectNames
        String subjects
        int totalCount = 0
        allSection.each { Section section ->
            totalCount++
            subjectNames =  classSubjectsService.subjectsNameListForClassSubject(section.className, section.groupName)
            if(subjectNames){
                subjects = subjectNames.join(', ')
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:subjects, 4:''])
            } else {
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:'No Subject', 4:''])
            }
        }
        render (view: '/admin/studentSubject/studentSubjectPage', model: [classNameList:classNameList, dataReturns:dataReturns, totalCount:totalCount])
    }

    def college() {
        def classNameList = classNameService.classNamesList(AcaYearType.COLLEGE)
        if (!classNameList) {
            render (view: '/admin/studentSubject/studentSubjectPage', model: [dataReturns:null])
            return
        }
        AcademicYear academicYear= null
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        def allSection = sectionService.allSections(AcaYearType.COLLEGE, academicYear)
        List dataReturns = new ArrayList()
        List subjectNames
        String subjects
        int totalCount = 0
        allSection.each { Section section ->
            totalCount++
            subjectNames =  classSubjectsService.subjectsNameListForClassSubject(section.className, section.groupName)
            if(subjectNames){
                subjects = subjectNames.join(', ')
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:subjects, 4:''])
            } else {
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:'No Subject', 4:''])
            }
        }
        render (view: '/admin/studentSubject/studentSubjectPage', model: [classNameList:classNameList, dataReturns:dataReturns, totalCount:totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        ClassName className
        if (params.className){
            className = ClassName.read(params.getLong('className'))
        }
        def allSection
        if ( className) {
            allSection = sectionService.classSections(className)
        } else {
            allSection = sectionService.allSections(params)
        }

        List dataReturns = new ArrayList()
        List subjectNames
        String subjects
        int totalCount = 0
        allSection.each { Section section ->
            totalCount++
            subjectNames =  classSubjectsService.subjectsNameListForClassSubject(section.className, section.groupName)
            if(subjectNames){
                subjects = subjectNames.join(', ')
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:subjects, 4:''])
            } else {
                dataReturns.add([DT_RowId:section.id, 0: totalCount, 1: section.className?.name, 2:section.name, 3:'No Subject', 4:''])
            }
        }
        if( totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: dataReturns]
        result = gridData as JSON
        render result
    }

    def subjects(Long id) {
        Section section = Section.read(id)
        if(!section){
            redirect(action: 'index')
        }
        def resultMap =  studentSubjectsService.studentSubjectsList(section)
        def compulsorySubjectStr = classSubjectsService.subjectTypeCompulsoryListStr(section.className, section.groupName)
        render (view: '/admin/studentSubject/studentSubjectMapping', model: [dataReturn: resultMap, className:section.className?.name, sectionName: section.name, sectionId: section.id, compulsorySubjectStr: compulsorySubjectStr])
    }

    def edit(Long id) {
        Section section = Section.read(id)
        if (!section) {
            redirect(action: 'index')
            return
        }
        Long studentId = params.getLong('studentId')

        def studentSubjectList = studentSubjectsService.studentSubjectsListForEdit(section, studentId)
        ClassName className = section.className
        def optComOptions = classSubjectsService.subjectChooseOptComDDList(className, section.groupName)
        def compulsorySubjectStr = classSubjectsService.subjectTypeCompulsoryListStr(className, section.groupName)
        render(view: '/admin/studentSubject/editSubjectMapping', model: [optComOptions:optComOptions, className:className?.name, allowOptionalSubject: className.allowOptionalSubject, sectionName: section.name, sectionId: section.id, compulsorySubjectStr: compulsorySubjectStr, studentSubjectList: studentSubjectList])
    }

    def saveAlternative() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentSubjectsService.saveOrUpdateAlternative(params)
        String outPut = result as JSON
        render outPut
    }

    def commonMapping(Long id){
        ClassName className = ClassName.read(id)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        def sectionList = sectionService.classSectionsDDList(className)
        render(view: '/admin/studentSubject/commonSubjectMapping', model: [classNameList:classNameList, className: className, sectionList: sectionList])
    }

    def loadStudentCommonMapping(StudentSubjectCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassName className = command.className
        if (!className || className.activeStatus != ActiveStatus.ACTIVE) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
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
        AcademicYear academicYear = schoolService.workingYear(className)
        def studentSubjectsList = studentSubjectsService.studentSubjectsListCommonMapping(academicYear, className, command.sectionName, command.gender, command.religion)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('studentList', studentSubjectsList)
        result.put('optComOptions', optComOptions)
        result.put('allowOptionalSubject', className.allowOptionalSubject)
        outPut = result as JSON
        render outPut
    }

    def saveCommonMapping(StudentSubjectCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = studentSubjectsService.saveStudentSubjectCommonMapping(command)
        String outPut = result as JSON
        render outPut
    }
}
