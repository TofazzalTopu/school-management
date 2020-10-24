package com.grailslab

import com.grailslab.command.ClassPeriodCommand
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassPeriod
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.transaction.annotation.Transactional

@Transactional
class ClassPeriodService {
    def springSecurityService

    def periodList() {
        def c = ClassPeriod.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("shift", CommonUtils.SORT_ORDER_ASC)
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
    def periodDropDownList(Shift shift = null) {
        List dataReturns = new ArrayList()
        def c = ClassPeriod.createCriteria()
        def results = c.list() {
            and {
                if (shift) {
                    eq("shift", shift)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("shift", CommonUtils.SORT_ORDER_ASC)
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { ClassPeriod classPeriod ->
            dataReturns.add([id: classPeriod.id, name: "$classPeriod.name ($classPeriod.period)"])
        }
        return dataReturns
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        ClassPeriod classPeriod = ClassPeriod.get(id)
        if (!classPeriod) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        def clsPeriods = ClassPeriod.findAllByActiveStatusAndShiftAndSortPositionGreaterThan(ActiveStatus.ACTIVE, classPeriod.shift, classPeriod.sortPosition)
        if (clsPeriods) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You only can delete last period of ${classPeriod.shift.value}")
            return result
        }
        if (classPeriod.activeStatus.equals(ActiveStatus.INACTIVE)) {
            classPeriod.activeStatus = ActiveStatus.ACTIVE
        } else {
            classPeriod.activeStatus = ActiveStatus.INACTIVE
        }
        classPeriod.updatedBy = springSecurityService.principal.username
        classPeriod.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Period deleted successfully.")
        return result

    }

    def saveOrUpdate(ClassPeriodCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username

        ClassPeriod classPeriod

        String message
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
        LocalTime startTime = timeFormatter.parseLocalTime(command.startOn)
        LocalTime endTime = startTime.plusMinutes(command.duration)
        DateTime dummyDateTime = new DateTime().withYear(2014).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(startTime.getHourOfDay()).withMinuteOfHour(startTime.getMinuteOfHour())
        if (command.id) {
            classPeriod = ClassPeriod.get(command.id)
            if (!classPeriod) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            classPeriod.properties['name', 'startOn', 'duration', 'sortPosition', 'isPlayTime'] = command.properties
            classPeriod.updatedBy = createOrUpdate
            message = "Period updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)

        } else {
            classPeriod = new ClassPeriod(command.properties)
            classPeriod.createdBy = createOrUpdate
            message = "Period added successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }
        classPeriod.period = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
        classPeriod.startTime = dummyDateTime.toDate()
        classPeriod.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result

    }
}
