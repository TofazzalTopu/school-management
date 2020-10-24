package com.grailslab.attn

import com.grailslab.CommonUtils
import com.grailslab.command.AttnEmployeeCommand
import com.grailslab.command.AttnEmployeeLateEntryCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import grails.converters.JSON

class AttnEmployeeController {

    def attnEmployeeService
    def employeeService

    def index(Long id) {
        AttnRecordDay lastRecord
        if (id) {
            lastRecord = AttnRecordDay.read(id)
        } else {
            def lastEmpPresents = AttnEmpRecord.last()
            if (lastEmpPresents) {
                lastRecord = lastEmpPresents.recordDay
            }
        }

        def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: 'sortOrder'])
        if (!lastRecord) {
            render(view: '/attn/employee/empAttendanceList', model: [dataReturn: null, totalCount: 0, lastRecord: CommonUtils.getUiDateStrForPicker(new Date()), hrCategoryList: hrCategoryList])
            return
        }
        params.put("recordDateId", lastRecord.id)
        LinkedHashMap resultMap = attnEmployeeService.empPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/attn/employee/empAttendanceList', model: [dataReturn: null, totalCount: 0, lastRecord: CommonUtils.getUiDateStrForPicker(lastRecord.recordDate), hrCategoryList: hrCategoryList])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/attn/employee/empAttendanceList', model: [dataReturn: resultMap.results, totalCount: totalCount, lastRecord: CommonUtils.getUiDateStrForPicker(lastRecord.recordDate), hrCategoryList: hrCategoryList])
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
        AttnRecordDay recordDay = AttnRecordDay.findByActiveStatusAndRecordDate(ActiveStatus.ACTIVE, recordDate.clearTime(), [sort: "recordDate", order: "desc"])
        if (!recordDay) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        params.put("recordDateId", recordDay.id)
        LinkedHashMap resultMap = attnEmployeeService.empPaginateList(params)
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

    def saveManualAttendance(AttnEmployeeCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnEmployeeService.saveManualAttendance(command)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        AttnEmpRecord attnEmpRecord = AttnEmpRecord.read(id)
        if (!attnEmpRecord) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        Employee employee = Employee.read(attnEmpRecord.employeeId)

        result.put(CommonUtils.OBJ, attnEmpRecord)
        result.put('attnDate', attnEmpRecord.recordDay?.recordDate)
        result.put('inTime', CommonUtils.getUiTimeForEdit(attnEmpRecord.inTime))
        result.put('outTime', CommonUtils.getUiTimeForEdit(attnEmpRecord.outTime))
        result.put('employeeName', employee.empID + " - " + employee.name + " - " + employee.hrDesignation?.name)
        outPut = result as JSON
        render outPut
    }

    def updateLateEntry(AttnEmployeeLateEntryCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = attnEmployeeService.updateLateEntry(command)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = attnEmployeeService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}