package com.grailslab


import com.grailslab.enums.EventType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.CalenderEvent
import com.grailslab.settings.Holiday
import grails.gorm.transactions.Transactional
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants

@Transactional
class CalenderService {

    def grailsApplication
    def messageSource
    def holidayService
    def schoolService
    def springSecurityService

    List getPublicEvents(Date sStartFrom, Date sStartEnd) {
        List dataReturns = new ArrayList()
        List holidayDates = new ArrayList()

        DateTime startDt = new DateTime(sStartFrom)
        DateTime endDt = new DateTime(sStartEnd)
        DateTime tempDate = new DateTime(startDt.getMillis());
        def weeklyList = schoolService.weeklyHolidays()
        int dayOfWeek
        while(tempDate.compareTo(endDt) <=0 ){
            dayOfWeek = tempDate.getDayOfWeek()
            if(dayOfWeek ==  DateTimeConstants.FRIDAY && weeklyList.contains("Fri")){
                holidayDates.add(tempDate);
            } else if(dayOfWeek ==  DateTimeConstants.SATURDAY && weeklyList.contains("Sat")){
                holidayDates.add(tempDate);
            } else if(dayOfWeek ==  DateTimeConstants.SUNDAY && weeklyList.contains("Sun")){
                holidayDates.add(tempDate);
            }
            tempDate = tempDate.plusDays(1);
        }
        holidayDates.each {DateTime holiday ->
            dataReturns.add([title:"Weekly holiday", start: CommonUtils.getEventDateTimeStr2(holiday.toDate()),startEditable:false,durationEditable:false,className:'other-event weekly-holiday',allDay:true,tooltip:"Weekly holiday",rendering:'background'])
        }
        def cEvents = CalenderEvent.createCriteria()
        def results = cEvents.list() {
            eq("activeStatus", ActiveStatus.ACTIVE)
                or {
                    between('startDate', sStartFrom, sStartEnd)
                    between('endDate', sStartFrom, sStartEnd)
                }
        }
        if (results) {
            boolean startEditable = false
            boolean durationEditable = false
            boolean allDay = true
            String titleStr
            results.each { CalenderEvent calenderEvent ->
                if(calenderEvent.eventType==EventType.HOLIDAY){
                    titleStr =calenderEvent.name
                    dataReturns.add([ title:titleStr, start: CommonUtils.getEventDateTimeStr2(calenderEvent.startDate),end:CommonUtils.getEventDateTimeStr2(calenderEvent.endDate),startEditable:startEditable,durationEditable:durationEditable,className:'other-event holiday',allDay:allDay,tooltip:titleStr])
                }else if(calenderEvent.eventType==EventType.Annual_Program){
                    titleStr =calenderEvent.name
                    dataReturns.add([ title:titleStr, start: CommonUtils.getEventDateTimeStr2(calenderEvent.startDate),end:CommonUtils.getEventDateTimeStr2(calenderEvent.endDate),startEditable:startEditable,durationEditable:durationEditable,className:'other-event annual-program',allDay:allDay,tooltip:titleStr])
                }else if(calenderEvent.eventType==EventType.Exam_Days){
                    titleStr =calenderEvent.name
                    dataReturns.add([ title:titleStr+" Start", start: CommonUtils.getEventDateTimeStr2(calenderEvent.startDate),end:CommonUtils.getEventDateTimeStr2(calenderEvent.startDate),startEditable:startEditable,durationEditable:durationEditable,className:'other-event exam-days',allDay:allDay,tooltip:titleStr])
                    dataReturns.add([ title:titleStr+" End", start: CommonUtils.getEventDateTimeStr2(calenderEvent.endDate.minus(1)),end:CommonUtils.getEventDateTimeStr2(calenderEvent.endDate.minus(1)),startEditable:startEditable,durationEditable:durationEditable,className:'other-event exam-days',allDay:allDay,tooltip:titleStr])
                    dataReturns.add([ title:titleStr, start: CommonUtils.getEventDateTimeStr2(calenderEvent.startDate),end:CommonUtils.getEventDateTimeStr2(calenderEvent.endDate),startEditable:startEditable,durationEditable:durationEditable,className:'other-event exam-days-background',allDay:allDay,tooltip:titleStr,rendering:'background'])
                }
            }
        }
        return dataReturns
    }

    def saveHolidayEvents(Holiday holiday, isEdit = false) {
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        if (isEdit) {
            def oldEvents = CalenderEvent.findAllByHoliday(holiday)
            oldEvents.each { CalenderEvent calenderEvent ->
                calenderEvent.delete()
            }
        }
        try {
            DateTime endDateTime = new DateTime(holiday.endDate).plusDays(1).withHourOfDay(23).withMinuteOfHour(59)
            CalenderEvent holidayEvents = new CalenderEvent(
                    name: holiday.name,
                    startDate: holiday.startDate,
                    endDate: endDateTime.toDate(),
                    eventType: holiday.eventType,
                    holiday: holiday,
                    createdBy: createOrUpdateBy,
                    academicYear: academicYear
            )
            if (holidayEvents.hasErrors() || !holidayEvents.save(failOnError: true)) {
                def errorList = holidayEvents?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                println("Unable to save Holiday event due to :" + errorList?.join('\n'))
            }
        } catch (Exception ex) {
            println(ex.getMessage())
        }
    }

    boolean checkIfOpenDay(DateTime theDate) {
        Date theDay = theDate.toDate()
        def weeklyList = schoolService.weeklyHolidays()
        String theDayStr = CommonUtils.getHolidayStr(theDay)
        if (weeklyList.contains(theDayStr)) return false
        if (holidayService.isNoClassDay(theDay) > 0) return false
        return true
    }

    /*G
    def saveCtScheduleEvents(Exam exam, ExamSchedule schedule, isEdit = false){
        if(isEdit){
            def oldEvents = CalenderEvent.findAllByExamScheduleAndEventType(schedule,EventType.CT_SCHEDULE)
            oldEvents.each { CalenderEvent calenderEvent ->
                calenderEvent.delete()
            }
        }
        try{
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
            LocalTime startTime =timeFormatter.parseLocalTime(schedule?.ctPeriod?.startOn)
            DateTime startDateTime = new DateTime(schedule.ctExamDate?:new Date()).plusHours(startTime.hourOfDay).plusMinutes(startTime.minuteOfHour);
            DateTime endDateTime = startDateTime.plusMinutes(schedule?.ctPeriod?.duration) //

            String name = schedule.subject.name
            String description = exam.name

            CalenderEvent holidayEvents =new CalenderEvent(name: name, startDate:startDateTime.toDate(), endDate:endDateTime.toDate(),eventType: EventType.CT_SCHEDULE, examSchedule:schedule,description:description)
            if(holidayEvents.hasErrors() || !holidayEvents.save(failOnError:true)){
                def errorList = holidayEvents?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
                println("Unable to save Holiday event due to :"+ errorList?.join('\n'))
            }
        } catch (Exception ex){
            println(ex.getMessage())
        }
    }


    def saveHallScheduleEvents(Exam exam, ExamSchedule schedule, isEdit = false){
        if(isEdit){
            def oldEvents = CalenderEvent.findAllByExamScheduleAndEventType(schedule,EventType.HALL_SCHEDULE)
            oldEvents.each { CalenderEvent calenderEvent ->
                calenderEvent.delete()
            }
        }
        try{
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
            LocalTime startTime =timeFormatter.parseLocalTime(schedule?.hallPeriod?.startOn)
            DateTime startDateTime = new DateTime(schedule.hallExamDate?:new Date()).plusHours(startTime.hourOfDay).plusMinutes(startTime.minuteOfHour);
            DateTime endDateTime = startDateTime.plusMinutes(schedule?.hallPeriod?.duration) //

            String name = schedule.subject.name
            String description = exam.name

            CalenderEvent holidayEvents =new CalenderEvent(name: name, startDate:startDateTime.toDate(), endDate:endDateTime.toDate(),eventType: EventType.HALL_SCHEDULE, examSchedule:schedule,description:description)
            if(holidayEvents.hasErrors() || !holidayEvents.save(failOnError:true)){
                def errorList = holidayEvents?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
                println("Unable to save Holiday event due to :"+ errorList?.join('\n'))
            }
        } catch (Exception ex){
            println(ex.getMessage())
        }
    }*/
}
