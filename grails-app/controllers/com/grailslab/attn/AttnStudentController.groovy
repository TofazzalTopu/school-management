package com.grailslab.attn

import com.grailslab.CommonUtils
import com.grailslab.command.AttnRecordDayCommand
import com.grailslab.command.AttnStudentCommand
import com.grailslab.command.AttnStudentLateEntryCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.Student
import grails.converters.JSON

class AttnStudentController {

    def classNameService
    def attnStudentService
    def recordDayService

    def index(Long id) {
        def classList=classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        AttnRecordDay lastRecord
        if(id) {
            lastRecord = AttnRecordDay.read(id)
        } else {
            def lastStdPresents = AttnStdRecord.last()
            if (lastStdPresents) {
                lastRecord = lastStdPresents.recordDay
            }
        }
        if (!lastRecord) {
            render(view: '/attn/student/stdAttendanceList', model: [dataReturn: null, totalCount: 0, lastRecord:  CommonUtils.getUiDateStrForPicker(new Date()), classList:classList])
            return
        }
        params.put("recordDateId", lastRecord.id)
        LinkedHashMap resultMap = attnStudentService.stdPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/attn/student/stdAttendanceList', model: [dataReturn: null, totalCount: 0,classList:classList, lastRecord: CommonUtils.getUiDateStrForPicker(lastRecord.recordDate)])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/attn/student/stdAttendanceList', model: [dataReturn: resultMap.results, totalCount: totalCount,classList:classList, lastRecord: CommonUtils.getUiDateStrForPicker(lastRecord.recordDate)])
    }

    def manualAttendList(Long id){
        ClassName className = ClassName.read(id)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/attn/student/manualAttendanceList', model: [classNameList:classNameList, className: className])
    }

    def loadStudentManualAttend(AttnRecordDayCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Date attenDate = command.attendDate
           if (!attenDate) {
                attenDate = new Date().clearTime()
            }

        AttnRecordDay recordDay = recordDayService.recordDayForDevice(attenDate)
        if (!recordDay){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Date is not valid")
            outPut = result as JSON
            render outPut
            return
        }
        def studentList = attnStudentService.manualAttendanceList(command.sectionName, recordDay)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('studentList', studentList)
        outPut = result as JSON
        render outPut
    }

    def saveManualAttendence(AttnRecordDayCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnStudentService.saveManualAttendence(command, params)
        String output = result as JSON
        render output
    }

    def updateLateEntry(AttnStudentLateEntryCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnStudentService.updateLateEntry(command)
        String output = result as JSON
        render output
    }

    def saveIndividualAttendance(AttnStudentCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnStudentService.saveIndividualAttendance(command)
        String output = result as JSON
        render output
    }


    def list() {
        LinkedHashMap gridData
        String result
        if (!params.attnRecordDate) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        Date recordDate = Date.parse('dd/MM/yyyy', params.attnRecordDate)
        AttnRecordDay recordDay = AttnRecordDay.findByActiveStatusAndRecordDate(ActiveStatus.ACTIVE, recordDate.clearTime())
        if(!recordDay) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        params.put("recordDateId", recordDay.id)
        LinkedHashMap resultMap = attnStudentService.stdPaginateList(params)

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

    def edit(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        AttnStdRecord attnStdRecord = AttnStdRecord.read(id)
        if (!attnStdRecord) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Student student = Student.read(attnStdRecord.studentId)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ, attnStdRecord)
        result.put('inTime', CommonUtils.getUiTimeForEdit(attnStdRecord.inTime))
        result.put('outTime', CommonUtils.getUiTimeForEdit(attnStdRecord.outTime))
        result.put('studentName', student.studentID+" - "+student.name)
        result.put('attnDate', attnStdRecord.recordDay?.recordDate)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = attnStudentService.delete(id)
        String output = result as JSON
        render output
    }

}
