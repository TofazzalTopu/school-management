package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.OpenTimeLineCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON

class TimeLineController {
    def messageSource
    def timeLineService

    def index() {
        LinkedHashMap resultMap = timeLineService.timeLinePaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/timeLine/timeLine', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/timeLine/timeLine', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = timeLineService.timeLinePaginateList(params)

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

    def create(){
        render(view: '/open/timeLine/createOrEdit')
    }

    def edit(Long id) {
        OpenTimeLine timeLine = OpenTimeLine.read(id)
        if (!timeLine) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/open/timeLine/createOrEdit', model: [timeLine:timeLine])
    }

    def save(OpenTimeLineCommand command) {
        if (!request.method.equals('POST')) {
            flash.message = "Request Method not allow here."
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = timeLineService.saveOrUpdate(command)
        if (Boolean.parseBoolean(result.get("isError").toString())) {
            redirect(action: 'index', model: [flash.message = result.get("message")])
            return
        }
        redirect(action: 'index')
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = timeLineService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = timeLineService.delete(id)
        String outPut = result as JSON
        render outPut
    }


}
