package com.grailslab

import com.grailslab.command.HrPeriodCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrPeriod
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class HrPeriodService {
    def hrPeriodService
    def springSecurityService

    def periodList() {
        def c = HrPeriod.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("startTime", CommonUtils.SORT_ORDER_DESC)
            order("duration", CommonUtils.SORT_ORDER_DESC)
        }
        return results
    }

    def periodDropDownList() {
        List dataReturns = new ArrayList()
        def c = HrPeriod.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("startTime", CommonUtils.SORT_ORDER_ASC)
            order("duration", CommonUtils.SORT_ORDER_DESC)
        }
        results.each { HrPeriod period ->
            dataReturns.add([id: period.id, name: "${period.name} [${period.periodRange}]"])
        }
        return dataReturns
    }

    Boolean isLateEntry(Date entryTime, Date expectedTime, Integer lateTolarance = 0) {
        if (!entryTime || !expectedTime) return false

        long t1 = entryTime.getHours()*60*60+entryTime.getMinutes()*60;
        long t2 = expectedTime.getHours()*60*60+expectedTime.getMinutes()*60 + lateTolarance * 60;

        return t1 > t2
    }

    def saveOrUpdate(HrPeriodCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        HrPeriod hrPeriod
        String message
        String createOrUpdateBy = springSecurityService.principal.username
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
        LocalTime startTime = timeFormatter.parseLocalTime(command.startOn)
        LocalTime endTime = startTime.plusMinutes(command.duration)
        DateTime dummyDateTime = new DateTime().withYear(2014).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(startTime.getHourOfDay()).withMinuteOfHour(startTime.getMinuteOfHour())
        if (command.id) {
            hrPeriod = HrPeriod.get(command.id)
            if (!hrPeriod) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            hrPeriod.properties['name', 'startOn', 'duration', 'lateTolerance'] = command.properties
            hrPeriod.updatedBy = createOrUpdateBy
            message = "Period updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)

        } else {
            hrPeriod = new HrPeriod(command.properties)
            hrPeriod.createdBy = createOrUpdateBy
            message = "Period added successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }

        hrPeriod.periodRange = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
        hrPeriod.startTime = dummyDateTime.toDate()
        hrPeriod.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        HrPeriod hrPeriod = HrPeriod.get(id)
        if (!hrPeriod) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        try {
            hrPeriod.delete()
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
