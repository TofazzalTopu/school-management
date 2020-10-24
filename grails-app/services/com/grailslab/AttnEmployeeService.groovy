package com.grailslab

import com.grailslab.attn.AttnEmpRecord
import com.grailslab.attn.AttnRecordDay
import com.grailslab.command.AttnEmployeeCommand
import com.grailslab.command.AttnEmployeeLateEntryCommand
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.viewz.AttnEmpPresentView
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.apache.commons.lang.exception.ExceptionUtils
import org.joda.time.DateTime

@Transactional
class AttnEmployeeService {
    def employeeService
    def recordDayService
    def hrPeriodService

    static final String[] sortColumnsEmp = ['recordDayId','empid','name','designation','inTime','outTime']
    LinkedHashMap empPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        Long recordDateId =params.getLong('recordDateId')
        Long hrCategoryId=params.getLong('hrCategoryType')
        String sortColumn = CommonUtils.getSortColumn(sortColumnsEmp, iSortingCol)
        List dataReturns = new ArrayList()
        def c = AttnEmpPresentView.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("recordDayId",recordDateId)
                if(hrCategoryId){
                    eq("hrCategoryId",hrCategoryId)
                }
            }
            if (sSearch) {
                or {
                    ilike('designation', sSearch)
                    ilike('name', sSearch)
                    ilike('empid', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { AttnEmpPresentView dailyRecord ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: dailyRecord.objId, 0: serial, 1: dailyRecord.empid, 2: dailyRecord.name, 3: dailyRecord.designation, 4: CommonUtils.getUiTimeStr12HrLocal(dailyRecord.inTime),5: CommonUtils.getUiTimeStr12HrLocal(dailyRecord.outTime), 6: dailyRecord.isLate? "Late": "Present", 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    int numberOfTotalAttnEmployee(AttnRecordDay recordDay, HrCategory hrCategory = null){
        if (hrCategory){
            def empIdList = employeeService.employeeIdListByCategory(hrCategory)
            return AttnEmpRecord.countByRecordDayAndEmployeeIdInList(recordDay, empIdList)
        } else {
            return AttnEmpRecord.countByRecordDay(recordDay)
        }
    }
    int numberOfTotalLateEmployee(AttnRecordDay recordDay, HrCategory hrCategory = null){
        if (hrCategory){
            def empIdList = employeeService.employeeIdListByCategory(hrCategory)
            return AttnEmpRecord.countByRecordDayAndIsLateEntryAndEmployeeIdInList(recordDay, true, empIdList)
        } else {
            return AttnEmpRecord.countByRecordDayAndIsLateEntry(recordDay, true)
        }
    }

    int employeePresentCount(String employeeNo, Date startDate, Date endDate){
        def c = AttnEmpPresentView.createCriteria()
        def count = c.list() {
            and {
                eq("empid", employeeNo)
                eq("dayType", "Open_Day")
                between("recordDate", startDate, endDate)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int employeeLateCount(String employeeNo, Date startDate, Date endDate){
        def c = AttnEmpPresentView.createCriteria()
        def count = c.list() {
            and {
                eq("empid", employeeNo)
                eq("dayType", "Open_Day")
                eq("isLate", true)
                between("recordDate", startDate, endDate)
            }
            projections {
                count()
            }
        }
        return count[0]
    }

    def saveManualAttendance(AttnEmployeeCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        String message
        AttnEmpRecord attnEmpRecord
        Employee employee
        Boolean isLateEntry

        Date inTimeDate
        Date outTimeDate
        Date recordDate

        if (command.id) {
            attnEmpRecord = AttnEmpRecord.get(command.id)
            employee = Employee.read(attnEmpRecord.employeeId)
            recordDate = attnEmpRecord.recordDay.recordDate
            inTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.inTime)
            if (command.outTime) {
                outTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.outTime)
            }
            isLateEntry = hrPeriodService.isLateEntry(inTimeDate, employee.hrPeriod?.startTime, employee.hrPeriod?.lateTolerance)
            attnEmpRecord.inTime = inTimeDate
            attnEmpRecord.outTime = outTimeDate
            attnEmpRecord.isLateEntry = isLateEntry

            message = "Attendance for $employee.name updated successfully"
        } else {
            recordDate = command.recordDate
            if (!recordDate) recordDate = new Date().clearTime()
            AttnRecordDay attnRecordDay = recordDayService.recordDayForDevice(recordDate)
            employee = Employee.read(command.employeeId)
            attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(attnRecordDay, employee.empID)
            if (attnEmpRecord) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, "$employee.name Attendance for ${CommonUtils.getUiDateStr(recordDate)} already added. You can modify that if require.")
                return result
            }

            inTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.inTime)
            if (command.outTime) {
                outTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.outTime)
            }

            isLateEntry = hrPeriodService.isLateEntry(inTimeDate, employee.hrPeriod?.startTime, employee.hrPeriod?.lateTolerance)
            attnEmpRecord = new AttnEmpRecord(recordDay: attnRecordDay, recordDate: attnRecordDay.recordDate, employeeId: employee.id, empNo: employee.empID, inTime: inTimeDate, outTime: outTimeDate, remarks: command.reason, isLateEntry: isLateEntry)
            message = "Attendance for $employee.name added successfully"
        }
        attnEmpRecord.save(flush: true)
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def updateLateEntry(AttnEmployeeLateEntryCommand command) {

        def employeeList
        if (command.employeeId) {
            employeeList = new ArrayList()
            employeeList.add(Employee.read(command.employeeId))
        } else {
            employeeList = employeeService.employeeList(command.hrCategory)
        }

        DateTime startDateTime
        DateTime endDateTime = new DateTime(command.endDate).withHourOfDay(23)
        DateTime thisDate
        Date effectiveDate

        AttnEmpRecord attnEmpRecord
        for (employee in employeeList) {
            startDateTime = new DateTime(command.startDate)
            while (startDateTime.isBefore(endDateTime)) {
                thisDate = startDateTime
                effectiveDate = thisDate.toDate()
                attnEmpRecord = AttnEmpRecord.findByRecordDateAndEmpNo(effectiveDate, employee.empID)
                if (attnEmpRecord) {
                    attnEmpRecord.isLateEntry = hrPeriodService.isLateEntry(attnEmpRecord.inTime, employee.hrPeriod?.startTime, employee.hrPeriod?.lateTolerance)
                    attnEmpRecord.save(flush: true)
                }
                startDateTime = startDateTime.plusDays(1)
            }
        }

        LinkedHashMap result = new LinkedHashMap()
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE, "Late Attendance Updated Successfully")
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        AttnEmpRecord attnEmpRecord = AttnEmpRecord.get(id)
        if (!attnEmpRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        try {
            attnEmpRecord.delete()
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "deleted successfully.")
        } catch (Exception e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, ExceptionUtils.getRootCauseMessage(e))
        }
        return result
    }

}
