package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.*
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.SelectionTypes
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.messaging.SmsDraft
import com.grailslab.settings.ClassName
import com.grailslab.settings.ShiftExam
import grails.converters.JSON

class MessagingController {
    def springSecurityService
    def schoolService
    def studentService
    def sectionService
    def classNameService
    def messagingService
    def messageSource
    def employeeService
    def hrCategoryService
    def examService
    def tabulationService
    def shiftExamService


    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        LinkedHashMap resultMap = messagingService.messagePaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/messaging/index', model: [academicYearList:academicYearList, dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/messaging/index', model: [academicYearList:academicYearList, dataReturn: resultMap.results, totalCount: totalCount])
    }

    def smsList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =messagingService.messagePaginateList(params)

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

    def quickSms() {
        render (view: '/admin/messaging/quickSms')
    }

    def step1() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        SelectionTypes selectionType = SelectionTypes.BY_CLASS
        if(params.selectionType){
            selectionType = SelectionTypes.valueOf(params.selectionType)
        }
        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        }

        List dataList
        if (selectionType == SelectionTypes.BY_SHIFT) {
            def shiftList = Arrays.asList(Shift.values())
            List dataReturns = new ArrayList()
            shiftList.each { Shift shift ->
                dataReturns.add([id: shift.key, name: shift.value])
            }
            render(view: '/admin/messaging/step1', model: [academicYear: academicYear, draftSmsId:params.draftSmsId, dataList: dataReturns, selectionType: selectionType.key, title: "Select Shift(s)"])
            return
        } else if (selectionType == SelectionTypes.BY_CLASS) {
            dataList = classNameService.classNameDropDownList(AcaYearType.ALL)
            render(view: '/admin/messaging/step1', model: [academicYear: academicYear, draftSmsId:params.draftSmsId, dataList: dataList, selectionType: selectionType.key, title: "Select Class(s)"])
            return
        } else if (selectionType == SelectionTypes.BY_SECTION) {
            dataList = sectionService.sectionDropDownList(academicYear)
            render(view: '/admin/messaging/step1', model: [academicYear: academicYear, draftSmsId:params.draftSmsId, dataList: dataList, selectionType: selectionType.key, title: "Select Section(s)"])
            return
        } else if (selectionType == SelectionTypes.BY_TEACHER) {
            dataList = hrCategoryService.hrCategoryDropDownList()
            render(view: '/admin/messaging/step1', model: [draftSmsId:params.draftSmsId, dataList: dataList, selectionType: selectionType.key, title: "Select Category(s)"])
            return
        } else if (selectionType == SelectionTypes.BY_STUDENT) {
            render(view: '/admin/messaging/step1', model: [academicYear: academicYear, draftSmsId:params.draftSmsId, selectionType: selectionType.key, title: "Select Student(s)"])
            return
        }
        flash.message = 'Selection Type Not Found'
        redirect(action: 'index')
    }

    def step2() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        List studentList
        List<Long> selectionIds
        List checkedList=new ArrayList()
        if (params.checkedId){
            if (params.checkedId instanceof String){
                checkedList.add(params.checkedId)
            }else {
                checkedList = params.checkedId
            }
        }else {
            flash.message = 'Invalid selection'
            redirect(action: 'index')
            return
        }
        if(!params.selectionType){
            flash.message = 'Please select any categoty'
            redirect(action: 'index')
        }
        SelectionTypes selectionType = SelectionTypes.valueOf(params.selectionType)
        if(!selectionType){
            flash.message = 'Please select any categoty'
            redirect(action: 'index')
        }
        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        }

        String draftMessage
        if(params.draftSmsId){
            Long draftId = params.getLong('draftSmsId')
            SmsDraft smsDraft = SmsDraft.read(draftId)
            draftMessage = smsDraft?.message
        }

        String selectListHeader="Select Student(s)"

        if (selectionType == SelectionTypes.BY_SHIFT) {
            List<Shift> shiftList = checkedList.collect { it as Shift }
            selectionIds = sectionService.sectionIdsByShift(shiftList, academicYear)
            studentList =studentService.step2StudentListForMessage(selectionIds)
            render(view: '/admin/messaging/step2', model: [draftMessage:draftMessage, studentList:studentList,selectListHeader:selectListHeader,selectionType:selectionType])
            return
        } else if (selectionType == SelectionTypes.BY_CLASS) {
            List<Long> classIdList = checkedList.collect { it as Long }
            selectionIds = sectionService.sectionIdsByClassId(classIdList, academicYear)
            studentList =studentService.step2StudentListForMessage(selectionIds)
            render(view: '/admin/messaging/step2', model: [draftMessage:draftMessage, studentList:studentList,selectListHeader:selectListHeader,selectionType:selectionType])
            return
        } else if (selectionType == SelectionTypes.BY_SECTION) {
            selectionIds = checkedList.collect { it as Long }
            studentList =studentService.step2StudentListForMessage(selectionIds)
            render(view: '/admin/messaging/step2', model: [draftMessage:draftMessage, studentList:studentList,selectListHeader:selectListHeader,selectionType:selectionType])
            return
        } else if (selectionType == SelectionTypes.BY_TEACHER) {
            selectListHeader="Select Teacher & Other(s)"
            List<Long> deptIdList = checkedList.collect { it as Long }
            studentList =employeeService.step2EmpListForMessage(deptIdList,null)
            render(view: '/admin/messaging/step2', model: [draftMessage:draftMessage, studentList:studentList,selectListHeader:selectListHeader,selectionType:selectionType])
            return
        } else if (selectionType == SelectionTypes.BY_STUDENT) {
            selectionIds = checkedList.collect { it as Long }
            studentList =studentService.step2StudentListForMessage(selectionIds, SelectionTypes.BY_STUDENT)
            render(view: '/admin/messaging/step2', model: [draftMessage:draftMessage, studentList:studentList,selectListHeader:selectListHeader,selectionType:selectionType])
            return
        }
        flash.message = 'Selection Type Not Found'
        redirect(action: 'index')
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = studentService.studentPaginateList(params)

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

    def drafts() {
        LinkedHashMap resultMap = messagingService.draftPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/messaging/draftList', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/messaging/draftList', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def draftSms(){
        render(view: '/admin/messaging/draftMessage')
    }

    def draftList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =messagingService.draftPaginateList(params)

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

    def save(SmsDraftCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message= errorList?.join(', ')
            redirect(action: 'index')
            return
        }
        SmsDraft smsDraft
        if (command.id) {
            smsDraft = SmsDraft.get(command.id)
            if (!smsDraft) {
                flash.message= CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            smsDraft.properties = command.properties
            smsDraft.updatedBy = createOrUpdateBy
            flash.message ='Draft updated Successfully'
        } else {
            smsDraft = new SmsDraft(command.properties)
            smsDraft.createdBy = createOrUpdateBy
            flash.message ='Draft Added Successfully'
        }
        smsDraft.numOfSms = messagingService.smsLength(command.message)

        if(smsDraft.hasErrors() || !smsDraft.save(flush: true)){
            def errorList = smsDraft?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message= errorList?.join(',')
            redirect(action: 'index')
            return
        }
        redirect(action: 'drafts')
    }

    def edit(Long id) {
        SmsDraft smsDraft = SmsDraft.read(id)
        if (!smsDraft) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'drafts')
            return
        }
        render(view: '/admin/messaging/draftMessage', model: [smsDraft:smsDraft])
    }

    def delete(Long id) {
        LinkedHashMap result = messagingService.delete(id)
        String output = result as JSON
        render output
    }

    def sendMessage(SmsCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = messagingService.sendMessage(command, params)
        String output = result as JSON
        render output
    }

    def sendQuickSms(QuickSmsCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = messagingService.sendQuickSms(command)
        String output = result as JSON
        render output
    }

    def result(Long id){
        ShiftExam shiftExam
        ClassName className
        def classNameList
        def sectionExamList
        def examNameList = shiftExamService.examNameDropDown()
        if (id) {
            shiftExam = ShiftExam.read(id)
        }
        if (params.class) {
            className = ClassName.read(params.getLong('class'))
        }
        if (shiftExam) {
            classNameList = classNameService.classNameInIdListDD(shiftExam.classIds);
        }
        if (shiftExam) {
            sectionExamList = examService.examsForSmsResult(shiftExam, className);
        }

        render(view: '/admin/messaging/result', model: [examNameList: examNameList, shiftExam: shiftExam, className: className, classNameList: classNameList, sectionExamList: sectionExamList])
    }

    def students(){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        List<Long> examIds
        List checkedList=new ArrayList()
        if (params.checkedId){
            if (params.checkedId instanceof String){
                checkedList.add(params.checkedId)
            }else {
                checkedList = params.checkedId
            }
        }else {
            flash.message = 'Invalid selection'
            redirect(action: 'index')
            return
        }

        examIds = checkedList.collect { it as Long }
        List studentList =tabulationService.tabulationListForSms(examIds)
        if(!studentList){
            flash.message = 'No student available'
            redirect(action: 'index')
            return
        }
        render(view: '/admin/messaging/resultStdList', model: [studentList:studentList])
    }

    def sendResultMessage(ResultMsgCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = messagingService.sendResultMessage(command)
        String output = result as JSON
        render output
    }

    def smsPurchase(SmsPurchaseCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = messagingService.smsPurchase(command)
        String output = result as JSON
        render output
    }
}

