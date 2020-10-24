package com.grailslab

import com.grailslab.command.LeaveApplyCommand
import com.grailslab.enums.EventType
import com.grailslab.enums.LeaveApproveStatus
import com.grailslab.enums.LeaveDayCount
import com.grailslab.enums.LeavePayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.hr.LeaveApply
import com.grailslab.hr.LeaveBalance
import com.grailslab.hr.LeaveName
import com.grailslab.settings.Holiday
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime

import java.text.DateFormat
import java.text.SimpleDateFormat

@Transactional
class LeaveService {

    def messageSource
    def jasperService
    def schoolService
    def employeeService
    def springSecurityService

    static final String[] sortColumns = ['name']
    static final String[] sortColumnsLeaveApprove = ['id', 'leaveName','fromDate','numberOfDay','reason','approveStatus','applyDate']
    static final String[] sortColumnsLeaveApply = ['argentContactNo']


    LinkedHashMap leaveApplyPaginateList(GrailsParameterMap params) {

        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        LeaveApproveStatus approveStatus = params.approveStatus ? LeaveApproveStatus.valueOf(params.approveStatus) : LeaveApproveStatus.Draft
        def principal = springSecurityService.principal
        def user = springSecurityService.currentUser
        if (!user) {
            return [totalCount: 0, results: null]
        }
        String userRefStr = principal?.userRef
        Employee employee = Employee.findByEmpID(userRefStr)

        String sortColumn = CommonUtils.getSortColumn(sortColumnsLeaveApply, iSortingCol)
        List dataReturns = new ArrayList()
        def c = LeaveApply.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (approveStatus) {
                    eq("approveStatus", approveStatus)
                }
                eq("employee", employee)
            }
            if (sSearch) {
                or {
                    ilike('argentContactNo', sSearch)
                    ilike('reason', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String employeeName
        Employee emp
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1)  iDisplayStart
            }
            results.each { LeaveApply apply ->
                emp = apply?.employee
                employeeName = emp?.empID + '  ' + emp?.name
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: apply.id, DT_Status: apply.approveStatus?.key, 0: serial, 1: employeeName, 2: apply?.leaveName?.name, 3: CommonUtils.getUiDateStr(apply?.applyDate), 4: CommonUtils.getUiDateStr(apply?.fromDate), 5: CommonUtils.getUiDateStr(apply?.toDate), 6: apply?.numberOfDay, 7: apply?.approveStatus?.value, 8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap leaveEmployeePaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def principal = springSecurityService.principal

        LeaveApproveStatus approveStatus = params.approveStatus ? LeaveApproveStatus.valueOf(params.approveStatus) : LeaveApproveStatus.Applied

        String sortColumn = CommonUtils.getSortColumn(sortColumnsLeaveApprove, iSortingCol)
        List dataReturns = new ArrayList()
        def c = LeaveApply.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'em')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("approveStatus", approveStatus)
                eq("em.empID", principal.userRef)
            }
            if (sSearch) {
                or {
                    ilike('em.empID', sSearch)
                    ilike('em.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String employeeName
        String leaveRange
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { LeaveApply apply ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                leaveRange = CommonUtils.getDateRange(apply.fromDate, apply.toDate)
                employeeName = apply.employee?.empID + '  ' + apply.employee?.name
                dataReturns.add([DT_RowId: apply.id, DT_Status: apply.approveStatus?.key, 0: serial, 1: apply.leaveName?.name,  2: leaveRange, 3: apply.numberOfDay+"("+apply.payType?.value+")", 4: apply.reason, 5: apply.approveStatus?.value, 6: CommonUtils.getUiDateStr(apply.applyDate),  7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def listLeave(Long leaveId) {
        List dataReturns = new ArrayList()
        def c = LeaveName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (leaveId){
                    eq("id", leaveId)
                }
            }
            order("id", CommonUtils.SORT_ORDER_ASC)
        }
    }

    def allLeaveDropDownList() {
        List dataReturns = new ArrayList()
        def c = LeaveName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("id", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { LeaveName leaveName ->
            dataReturns.add([id: leaveName.id, name: leaveName.name])
        }
        return dataReturns
    }

    Integer totalLeaveDays(Date fromDate, Date toDate) {
        List<String> weeklyHolidayList = schoolService.weeklyHolidays()

        LeaveDayCount leaveDayCount = LeaveDayCount.Flat

        DateTime startDateTime = new DateTime(fromDate)
        DateTime endDateTime = new DateTime(toDate).withHourOfDay(23)
        DateTime thisDate
        Date effectiveDate
        DateFormat formatForDay = new SimpleDateFormat("EEE");
        String dayOfWeekStr
        int leaveDates = 0
        if (leaveDayCount.equals(LeaveDayCount.All_Holiday)) {
            while (startDateTime.isBefore(endDateTime)) {
                thisDate = startDateTime
                effectiveDate = thisDate.toDate()
                dayOfWeekStr = formatForDay.format(effectiveDate)
                if (!weeklyHolidayList.contains(dayOfWeekStr)) {
                    if (!isHoliday(thisDate.toDate())) {
                        leaveDates++
                    }
                }
                startDateTime = startDateTime.plusDays(1)
            }
        } else if (leaveDayCount.equals(LeaveDayCount.Weekly_Holiday)) {
            while (startDateTime.isBefore(endDateTime)) {
                thisDate = startDateTime
                effectiveDate = thisDate.toDate()
                dayOfWeekStr = formatForDay.format(effectiveDate)
                if (!weeklyHolidayList.contains(dayOfWeekStr)) {
                    leaveDates++
                }
                startDateTime = startDateTime.plusDays(1)
            }
        } else if (leaveDayCount.equals(LeaveDayCount.Flat)) {
            while (startDateTime.isBefore(endDateTime)) {
                thisDate = startDateTime
                effectiveDate = thisDate.toDate()
                dayOfWeekStr = formatForDay.format(effectiveDate)
                leaveDates++
                startDateTime = startDateTime.plusDays(1)
            }
        }
        return leaveDates
    }
    Boolean isLeaveDay(Date leaveSeeking) {
        List<String> weeklyHolidayList = schoolService.weeklyHolidays()

        LeaveDayCount leaveDayCount = LeaveDayCount.Flat

        DateFormat formatForDay = new SimpleDateFormat("EEE");
        String dayOfWeekStr = formatForDay.format(leaveSeeking)
        if (leaveDayCount.equals(LeaveDayCount.All_Holiday)) {
            if (!weeklyHolidayList.contains(dayOfWeekStr)) {
                if (!isHoliday(leaveSeeking)) {
                    return true
                }
            }
        } else if (leaveDayCount.equals(LeaveDayCount.Weekly_Holiday)) {
            if (!weeklyHolidayList.contains(dayOfWeekStr)) {
                return true
            }
        } else if (leaveDayCount.equals(LeaveDayCount.Flat)) {
            return true
        }
        return false
    }

    def isHoliday(Date searchDate) {
        Holiday.createCriteria().list {
            eq("eventType", EventType.HOLIDAY)
            eq("activeStatus", ActiveStatus.ACTIVE)
            'le'('startDate', searchDate)
            'ge'('endDate', searchDate)
        } as boolean
    }

    int leaveCount(Long employeeId, Date startDate, Date endDate, String LeavePayType = null) {
        def totalLeave = LeaveBalance.createCriteria().count() {
            and {
                eq("employeeId", employeeId)
                between("leaveOn", startDate, endDate)
                if (LeavePayType) {
                    eq("payType", LeavePayType )
                }
            }
        }
        return totalLeave
    }

    boolean isOnLeaveByDate(Long employeeId, Date theDate, String LeavePayType = null) {
        def totalLeave = LeaveBalance.createCriteria().count() {
            and {
                eq("employeeId", employeeId)
                eq("leaveOn", theDate)
                if (LeavePayType) {
                    eq("payType", LeavePayType )
                }
            }
        }
        return totalLeave == 0 ? false : true
    }

    int leaveCountByDate(Date leaveOn, HrCategory hrCategory, String leavePayType = null) {
        def empIdList
        if (hrCategory){
            empIdList = employeeService.employeeIdListByCategory(hrCategory)
        }

        def totalLeave = LeaveBalance.createCriteria().count() {
            and {
                eq("leaveOn", leaveOn)
                if (empIdList){
                    'in'("employeeId", empIdList)
                }
                if (leavePayType) {
                    eq("payType", leavePayType )
                }
            }
        }
        return totalLeave
    }
    int leaveConsume(Long employeeId, Long leaveId, Date fromDate, Date toDate) {
        def consumeCount = LeaveBalance.createCriteria().count() {
            and {
                eq("employeeId", employeeId)
                eq("leaveNameId", leaveId)
                eq("payType", LeavePayType.PAID_LEAVE.key)
                between("leaveOn", fromDate, toDate)
            }
        }
    }

    def saveLeaveApplication(LeaveApply leaveApply){
        DateTime reportDate = new DateTime(leaveApply.fromDate)
        Date startDate = reportDate.dayOfYear().withMinimumValue().toDate()
        Date endDate = reportDate.dayOfYear().withMaximumValue().toDate()

        int consumeCount = leaveConsume(leaveApply.employee.id, leaveApply.leaveName.id, startDate, endDate)
        Integer totalLeaveDays = totalLeaveDays(leaveApply.fromDate, leaveApply.toDate)
        if (leaveApply.payType == LeavePayType.PAID_LEAVE && consumeCount + totalLeaveDays > leaveApply.leaveName.numberOfDay) {
            return false
        }
        leaveApply.numberOfDay = totalLeaveDays
        LeaveApply savedApply = leaveApply.save(flush: true)
        if (savedApply) {
            updateLeaveBalance(leaveApply)
        }
        return true
    }
    def updateLeaveBalance(LeaveApply leaveApply){
        if (!leaveApply || leaveApply.approveStatus != LeaveApproveStatus.Approved){
            return false
        }
        DateTime startDateTime = new DateTime(leaveApply.fromDate)
        DateTime endDateTime = new DateTime(leaveApply.toDate).withHourOfDay(23)
        DateTime thisDate
        Date effectiveDate
        while (startDateTime.isBefore(endDateTime)) {
            thisDate = startDateTime
            effectiveDate = thisDate.toDate()
            if (isLeaveDay(effectiveDate)) {
                new LeaveBalance(employeeId: leaveApply.employee.id, leaveApplyId: leaveApply.id,
                        leaveNameId: leaveApply.leaveName.id, leaveOn:effectiveDate, payType: leaveApply.payType.key).save(flush: true)
            }
            startDateTime = startDateTime.plusDays(1)
        }
        return true
    }

    def savOrUpdate(LeaveApplyCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Boolean isSaveSuccess = false
        def principal = springSecurityService.principal
        Employee employee = Employee.findByEmpID(principal.userRef)

        if (!employee) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        LeaveApply leaveApply
        if (command.id) {
            leaveApply = LeaveApply.get(command.id)
            if (!leaveApply) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            leaveApply.properties['applyDate', 'leaveName', 'applyType', 'approveStatus', 'payType', 'fromDate', 'toDate', 'argentContactNo', 'supportedBy','reason'] = command.properties
            leaveApply.employee = employee
            leaveApply.updatedBy = createOrUpdateBy
            isSaveSuccess = saveLeaveApplication(leaveApply)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Application updated successfully')
        } else {
            leaveApply = new LeaveApply(command.properties)
            leaveApply.employee = employee
            leaveApply.createdBy = createOrUpdateBy
            leaveApply.academicYear = academicYear
            isSaveSuccess = saveLeaveApplication(leaveApply)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Application added successfully')
        }

        if (!isSaveSuccess) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Your paid leave quota for this year exceeded. Please try unpaid or contact admin")
            return result
        }
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        LeaveApply apply = LeaveApply.get(id)
        if (!apply) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        apply.activeStatus = ActiveStatus.INACTIVE
        apply.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        return result

    }

}
