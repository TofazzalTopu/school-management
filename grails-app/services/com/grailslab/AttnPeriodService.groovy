package com.grailslab

import com.grailslab.attn.AttendancePeriod
import com.grailslab.command.AttendancePeriodCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class AttnPeriodService {

    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['id']

    LinkedHashMap periodPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = AttendancePeriod.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            eq("attnType", params.attnType)
            if (params.attnTypeName != "ALL") {
                eq("attnTypeName", params.attnTypeName)
            }
            if (sSearch) {
                or {
                    ilike('attnTypeName', sSearch)
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
            results.each { AttendancePeriod attnPeriod ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: attnPeriod.id, 0: serial, 1: attnPeriod.attnTypeName,
                                 2: attnPeriod.workingStatus, 3: attnPeriod.dayName, 4: attnPeriod.startTime,
                                 5: attnPeriod.endTime, 6: attnPeriod.lateTolerance, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    List teacherType() {
        List list = new ArrayList()
        def c = AttendancePeriod.createCriteria()

        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("attnType", "TEACHER")
            }
            projections {
                distinct('attnTypeName')
            }
        }
        results.each {
            list.add([key: it, value: it])
        }
        return list
    }

    def saveOrUpdate(AttendancePeriodCommand command){

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        AttendancePeriod attnPeriod
        String createOrUpdate = springSecurityService.principal?.username
        String message
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
        LocalTime startTime =timeFormatter.parseLocalTime(command.startTime)
        LocalTime endTime = timeFormatter.parseLocalTime(command.endTime)
        DateTime dummyDate = new DateTime()
        DateTime dummyDateStartTime = dummyDate.withYear(dummyDate.getYear()).withMonthOfYear(dummyDate.getMonthOfYear()).withDayOfMonth(dummyDate.getDayOfMonth()).withHourOfDay(startTime.getHourOfDay()).withMinuteOfHour(startTime.getMinuteOfHour())
        DateTime dummyDateEndTime = dummyDate.withYear(dummyDate.getYear()).withMonthOfYear(dummyDate.getMonthOfYear()).withDayOfMonth(dummyDate.getDayOfMonth()).withHourOfDay(endTime.getHourOfDay()).withMinuteOfHour(endTime.getMinuteOfHour())
        if (command.id) {
            attnPeriod = AttendancePeriod.get(command.id)
            if (!attnPeriod) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            attnPeriod.properties['attnType', 'attnTypeName', 'dayName', 'lateTolerance', 'startTime', 'endTime', 'workingStatus'] = command.properties
            attnPeriod.dummyStartTime = dummyDateStartTime.toDate()
            attnPeriod.dummyEndTime = dummyDateEndTime.toDate()
            attnPeriod.updatedBy = createOrUpdate
            message = "Period updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)

        } else {
            attnPeriod = new AttendancePeriod(command.properties)
            attnPeriod.createdBy = createOrUpdate
            message = "Period added successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }

        attnPeriod.dummyStartTime = dummyDateStartTime.toDate()
        attnPeriod.dummyEndTime = dummyDateEndTime.toDate()
        attnPeriod.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        AttendancePeriod attendancePeriod = AttendancePeriod.get(id)
        if (!attendancePeriod) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        try {
            attendancePeriod.delete()
        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Period already in use. Please delete reference first")
            return result
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Period deleted successfully.")
        return result
    }
}
