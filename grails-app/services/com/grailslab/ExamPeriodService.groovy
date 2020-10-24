package com.grailslab

import com.grailslab.command.ExamPeriodCommand
import com.grailslab.enums.ExamType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ExamPeriod
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import grails.gorm.transactions.Transactional

@Transactional
class ExamPeriodService {
    def springSecurityService

    def periodList() {
        def c = ExamPeriod.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("examType", CommonUtils.SORT_ORDER_DESC)
            order("startTime", CommonUtils.SORT_ORDER_DESC)
            order("duration", CommonUtils.SORT_ORDER_DESC)
        }
        return results
    }

    def periodDropDownList(ExamType examType = null) {
        List dataReturns = new ArrayList()
        def c = ExamPeriod.createCriteria()
        def results = c.list() {
            and {
                if(examType){
                    eq("examType", examType)
                    eq("activeStatus", ActiveStatus.ACTIVE)
                }
            }
            order("startTime", CommonUtils.SORT_ORDER_ASC)
            order("duration", CommonUtils.SORT_ORDER_DESC)
        }
        results.each { period ->
            dataReturns.add([id: period.id, name: period.name])
        }
        return dataReturns
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        ExamPeriod examPeriod = ExamPeriod.get(id)
        if (!examPeriod) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (examPeriod.activeStatus.equals(ActiveStatus.INACTIVE)) {
            examPeriod.activeStatus = ActiveStatus.ACTIVE
        } else {
            examPeriod.activeStatus = ActiveStatus.INACTIVE
        }
        examPeriod.updatedBy = springSecurityService.principal.username
        examPeriod.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Period deleted successfully.")
        return result
    }

    def saveOrUpdate(ExamPeriodCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        ExamPeriod examPeriod
        String message
        String createOrUpdateBy = springSecurityService.principal.username
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
        LocalTime startTime = timeFormatter.parseLocalTime(command.startOn)
        LocalTime endTime = startTime.plusMinutes(command.duration)
        DateTime dummyDateTime = new DateTime().withYear(2014).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(startTime.getHourOfDay()).withMinuteOfHour(startTime.getMinuteOfHour())
        if (command.id) {
            examPeriod = ExamPeriod.get(command.id)
            if (!examPeriod) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            examPeriod.properties['examType', 'startOn', 'duration'] = command.properties
            examPeriod.updatedBy = createOrUpdateBy
            message = "Period updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        } else {
            examPeriod = new ExamPeriod(command.properties)
            examPeriod.createdBy = createOrUpdateBy
            message = "Period added successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }
        examPeriod.name = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
        examPeriod.startTime = dummyDateTime.toDate()
        examPeriod.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

}
