package com.grailslab.attn

import com.grailslab.CommonUtils
import com.grailslab.command.HolidayCommand
import com.grailslab.settings.Holiday
import grails.converters.JSON

class HolidayController {

    def holidayService

    def index() {
//        render (view: '/admin/holiday')

          LinkedHashMap resultMap = holidayService.holidayNamePaginateList(params)

          if (!resultMap || resultMap.totalCount == 0) {
              render(view: '/attn/holiday/holiday', model: [dataReturn: null, totalCount: 0])
              return
          }
          int totalCount = resultMap.totalCount
          render(view: '/attn/holiday/holiday', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def save(HolidayCommand holidayCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = holidayService.saveOrUpdate(holidayCommand)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = holidayService.holidayNamePaginateList(params)

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

    def edit(Long id) {
        if (!request.method == 'POST') {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Holiday holiday = Holiday.read(id)
        if (!holiday) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        String startDate = CommonUtils.getUiDateStrForCalenderDateEdit(holiday.startDate);
        String endDate = CommonUtils.getUiDateStrForCalenderDateEdit(holiday.endDate);
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('startDate', startDate)
        result.put('endDate', endDate)
        result.put(CommonUtils.OBJ, holiday)
        outPut = result as JSON
        render outPut
    }

    /*def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Holiday holiday = Holiday.get(id)
        CalenderEvent calenderEvent = CalenderEvent.findByHoliday(holiday)
        if (!holiday) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        if (!calenderEvent) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        calenderEvent.delete()
        holiday.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Holiday Deleted Successfully')
        outPut = result as JSON
        render outPut
    }*/


    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = holidayService.inactive(id)
        String output = result as JSON
        render output
    }
}

