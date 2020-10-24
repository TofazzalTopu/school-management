package com.grailslab

import com.grailslab.command.SalAttendanceCommand
import com.grailslab.enums.LeavePayType
import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.salary.SalAttendance
import com.grailslab.salary.SalMaster
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime

@Transactional
class SalAttendanceService {

    def schoolService
    def messageSource
    def recordDayService
    def attnEmployeeService
    def salarySetUpService
    def leaveService
    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']
        LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
            AcademicYear academicYear = schoolService.schoolAdmissionYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
            YearMonths yearMonths  = YearMonths.JANUARY
            if(params.yearMonths){
                yearMonths = YearMonths.valueOf(params.yearMonths)
            }

            List dataReturns = new ArrayList()
        def c = SalAttendance.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
                eq("yearMonths",yearMonths)

            }
            if (serverParams.sSearch) {
                or {
                    ilike('emp.empID', serverParams.sSearch)
                    ilike('emp.name', serverParams.sSearch)
                    }
            }
            order(serverParams.sortColumn, serverParams.sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SalAttendance salAttendance ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salAttendance.id, 0: serial, 1 : salAttendance.employee.id, 2: salAttendance.employee.empID, 3: salAttendance.employee.name,4: salAttendance.employee.hrDesignation.name,5:salAttendance.workingDays ,6: salAttendance.holidays, 7: salAttendance.presentDays,8: salAttendance.lateDays,9: salAttendance.absentDays,10: salAttendance.leaveDays, 11: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(SalAttendanceCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        SalAttendance salAttendance
        if (params.id) {
            salAttendance = SalAttendance.get(params.id)
            salAttendance.properties = command.properties
            salAttendance.updatedBy = createOrUpdateBy
            message = "Update Successfully"
        } else {
            salAttendance = SalAttendance.findByEmployeeAndAcademicYearAndYearMonthsAndActiveStatus(command.employee, command.academicYear, command.yearMonths, ActiveStatus.ACTIVE)
            if (salAttendance) {
                message = "Already Added, You can Edit It."
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }

            salAttendance = new SalAttendance(command.properties)
            salAttendance.academicYear = academicYear
            salAttendance.createdBy = createOrUpdateBy
            message = "Save Successfully"
        }
        salAttendance.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalAttendance salAttnRecord = SalAttendance.get(id)
        if (!salAttnRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        SalMaster salMaster = SalMaster.findByAcademicYearAndYearMonthsAndSalaryStatusAndActiveStatus(salAttnRecord.academicYear, salAttnRecord.yearMonths, SalaryStatus.Disbursement, ActiveStatus.ACTIVE)
        if (salMaster) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Salary already disbursed. You can't delete now")
            return result
        }

        salAttnRecord.activeStatus = ActiveStatus.DELETE
        salAttnRecord.updatedBy = springSecurityService.principal?.username
        salAttnRecord.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, " Deleted successfully.")
        return result
    }

    def loadAttendance(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        String message

        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
        YearMonths yearMonths = YearMonths.valueOf(params.yearMonths)
        int yearVal = Integer.parseInt(academicYear.value)
        DateTime dateTime = CommonUtils.getDateTime(yearVal, yearMonths.serial)

        if (dateTime.afterNow) {
            message = "Invalid Date"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        int employeeAttn = SalAttendance.countByAcademicYearAndYearMonthsAndActiveStatus(academicYear, yearMonths, ActiveStatus.ACTIVE)

        if (employeeAttn > 0) {
            message = "Already Loaded. Please delete those first"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        def dateMap = CommonUtils.getFirstAndLastDate(yearVal, yearMonths.serial)
        Date startDate = dateMap.firstDay.clearTime()
        Date endDate = dateMap.lastDate.clearTime()

        Integer pCount
        Integer lateDays
        Integer absentDays
        Integer leaveDays
        Integer unPaidleaveDays
        Integer workingDayCount = recordDayService.workingDay(startDate, endDate)
        if (workingDayCount == 0) {
            message = "Academic Calender not generated or no working day found"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        Integer holiDayCount = recordDayService.holidayCount(startDate, endDate)
        def empNameList = salarySetUpService.salSetupEmpList()
        message = "Save Successfully"
        for (employee in empNameList) {
            pCount = attnEmployeeService.employeePresentCount(employee.empID, startDate, endDate)
            lateDays = attnEmployeeService.employeeLateCount(employee.empID, startDate, endDate)
            leaveDays = leaveService.leaveCount(employee.id, startDate, endDate, LeavePayType.PAID_LEAVE.key)
            unPaidleaveDays = leaveService.leaveCount(employee.id, startDate, endDate, LeavePayType.UN_PAID_LEAVE.key)
            absentDays = workingDayCount - (pCount + leaveDays)
            absentDays = absentDays < 0 ? 0 : absentDays
            new SalAttendance(workingDays: workingDayCount,
                    holidays: holiDayCount,
                    yearMonths: yearMonths,
                    employee: employee,
                    presentDays: pCount,
                    lateDays: lateDays, leaveDays: leaveDays,
                    absentDays: absentDays + unPaidleaveDays,
                    createdBy: createOrUpdateBy,
                    academicYear: academicYear
            ).save(flush: true)
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}
