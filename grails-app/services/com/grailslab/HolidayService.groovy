package com.grailslab

import com.grailslab.command.HolidayCommand
import com.grailslab.enums.EventType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.CalenderEvent
import com.grailslab.settings.Holiday
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime

@Transactional
class HolidayService {
    def springSecurityService
    def messageSource
    def schoolService
    def calenderService
    def grailsApplication

    static final String[] sortColumns = ['id', 'name','eventType', 'startDate', 'endDate']

    LinkedHashMap holidayNamePaginateList(GrailsParameterMap params) {
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

        AcademicYear workingYear = schoolService.workingYear(null)
        def c = Holiday.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("academicYear", workingYear)
                eq("activeStatus",ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
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
            results.each { Holiday holiday ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: holiday.id, 0: serial, 1: holiday.name,2:holiday.eventType.value, 3: CommonUtils.getUiDateStr(holiday.startDate), 4: CommonUtils.getUiDateStr(holiday.endDate), 5: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    @Transactional
    Holiday saveHoliday(Holiday newHoliday, boolean isEdit = false) {
        Holiday savedHoliday = newHoliday.save(flush: true)
        if(savedHoliday){
            def calEvents = calenderService.saveHolidayEvents(savedHoliday,isEdit)
        }
        return savedHoliday
    }

    def getByDate(DateTime theDate) {
        if (!theDate) return null
        Date searchDate = theDate.toDate()
        String name
        def results = Holiday.createCriteria().list {
            eq("eventType", EventType.HOLIDAY)
            eq("activeStatus", ActiveStatus.ACTIVE)
            'le'('startDate', searchDate)
            'ge'('endDate', searchDate)
        }
        if (results){
            def holidays = results.collect{it.name}
            return [name: holidays.join(", ")]
        }
        return null
    }

    def isHoliday(Date searchDate) {
        String name
        Boolean isHoliday=false
        Long holidayId
        def results = Holiday.createCriteria().list {
            eq("eventType", EventType.HOLIDAY)
            eq("activeStatus", ActiveStatus.ACTIVE)
            'le'('startDate', searchDate)
            'ge'('endDate', searchDate)
        }
        if (results){
            results.each {Holiday holiday->
                name+=holiday.name
                isHoliday=true
                holidayId=holiday.id
            }
            return [name:name,isHoliday:isHoliday,id:holidayId]
        }
        return null
    }

    def isExamDay(Date searchDate) {
        String name
        Boolean isHoliday=false
        def results = Holiday.createCriteria().list {
            eq("eventType", EventType.Exam_Days)
            eq("activeStatus", ActiveStatus.ACTIVE)
            'le'('startDate', searchDate)
            'ge'('endDate', searchDate)
        }
        if (results){
            results.each {Holiday holiday->
                name=holiday.name
                isHoliday=true
            }
            return [name:name,isHoliday:isHoliday]
        }
        return false
    }
    int isNoClassDay(Date theDay) {
        def results = Holiday.createCriteria().count() {
            eq("activeStatus", ActiveStatus.ACTIVE)
            eq("eventType", EventType.HOLIDAY)
            'le'('startDate', theDay)
            'ge'('endDate', theDay)
        } as Integer
        return results
    }

    def saveOrUpdate(HolidayCommand holidayCommand) {
        String createOrUpdateBy = springSecurityService.principal.username
        AcademicYear academicYear = springSecurityService?.principal.academicYear
        LinkedHashMap result = new LinkedHashMap()
        if (holidayCommand.hasErrors()) {
            def errorList = holidayCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        Holiday holiday
        String message
        Boolean isEdit = false
        if (holidayCommand.id) {
            holiday = Holiday.get(holidayCommand.id)
            if (!holiday) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            holiday.properties = holidayCommand.properties
            holiday.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Holiday Updated successfully'
            isEdit = true
        } else {
            holiday = new Holiday(holidayCommand.properties)
            holiday.createdBy = createOrUpdateBy
            holiday.academicYear = academicYear
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Holiday Added successfully'
            isEdit = false
        }
        Holiday savedCurr = saveHoliday(holiday, isEdit)
        if (!savedCurr) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            def errorList = holiday?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            message = errorList?.join('\n')
            result.put(CommonUtils.MESSAGE, message)
            return result
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        Holiday holiday = Holiday.get(id)
        CalenderEvent calenderEvent = CalenderEvent.findByHoliday(holiday)
        if (!holiday) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (!calenderEvent) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        holiday.activeStatus = ActiveStatus.INACTIVE
        calenderEvent.activeStatus = ActiveStatus.INACTIVE
        calenderEvent.updatedBy = createOrUpdateBy
        holiday.updatedBy = createOrUpdateBy
        holiday.save(flush: true)
        calenderEvent.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Deleted Successfully')
        return result
    }
}
