package com.grailslab.attn

import com.grailslab.CommonUtils
import com.grailslab.command.AttnRecordDayCommand
import grails.converters.JSON

class RecordDayController {

    def messageSource
    def recordDayService

    def index() {
        AttnRecordDay lastRecord = AttnRecordDay.last()
        Date lastRecordDate
        if (lastRecord) {
            lastRecordDate = lastRecord.recordDate
        } else {
            lastRecordDate = new Date()
        }
        render(view: '/attn/recordDay/recordDayList', model: [lastRecordDate: lastRecordDate])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = recordDayService.paginateList(params)

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

    def save(AttnRecordDayCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = recordDayService.save(command)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        AttnRecordDay attnRecordDay = AttnRecordDay.read(id)
        if(!attnRecordDay){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, attnRecordDay)
        result.put('attendanceDate', attnRecordDay.recordDate)
        outPut = result as JSON
        render outPut
    }

    def update(){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = recordDayService.update(params)
        String outPut = result as JSON
        render outPut
    }

    def reloadData(Long id) {
        if (!request.method == 'POST') {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = recordDayService.reloadData(id)
        String outPut = result as JSON
        render outPut
    }
}
