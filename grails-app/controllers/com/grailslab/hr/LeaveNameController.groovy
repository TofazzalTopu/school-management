package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.LeaveNameCommand
import grails.converters.JSON

class LeaveNameController {

    def springSecurityService
    def messageSource
    def leaveNameService

    def index() {
        render(view: '/leaveMgmt/leaveName')

    }

    def create(long id) {
        render(view: '/leaveMgmt/leaveNameCreateOrEdit')

    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = leaveNameService.leaveNamePaginateList(params)

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

    def save(LeaveNameCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            render(view: '/leaveMgmt/leaveNameCreateOrEdit', model: [command: command])
            return
        }
        LinkedHashMap result = leaveNameService.saveOrUpdate(command)
        if (Boolean.parseBoolean(result.get("isError").toString())) {
            render(view: '/leaveMgmt/leaveNameCreateOrEdit', model: [flash.message = result.get("message")])
            return
        }
        redirect(action: 'index')
    }


    def edit(Long id) {
        LeaveName leaveName = LeaveName.read(id)
        if (!leaveName) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/leaveMgmt/leaveNameCreateOrEdit', model: [leaveName:leaveName])
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = leaveNameService.inactive(id)
        String outPut = result as JSON
        render outPut
    }
}
