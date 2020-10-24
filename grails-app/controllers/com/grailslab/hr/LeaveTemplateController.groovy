package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.LeaveApplicationTemplateCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON

class LeaveTemplateController {
    def springSecurityService
    def leaveTemplateService
    def messageSource

    def index() {
        render(view: '/leaveMgmt/leaveTemplate')
    }

    def create() {
        render(view: '/leaveMgmt/leaveTemplateCreateOrEdit')
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = leaveTemplateService.leaveApplicationPaginateList(params)

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

    def save(LeaveApplicationTemplateCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            render(view: '/leaveMgmt/leaveTemplateCreateOrEdit', model: [command: command])
            return
        }

        LinkedHashMap result = leaveTemplateService.saveOrUpdate(command)
        if (Boolean.parseBoolean(result.get("isError").toString())) {
            render(view: '/leaveMgmt/leaveNameCreateOrEdit', model: [flash.message = result.get("message")])
            return
        }
        redirect(action: 'index')
    }


    def edit(Long id) {
        LeaveTemplate leaveTemplate = LeaveTemplate.read(id)
        if (!leaveTemplate) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/leaveMgmt/leaveTemplateCreateOrEdit', model: [leaveTemplate:leaveTemplate])
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = leaveTemplateService.inactive(id)
        String outPut = result as JSON
        render outPut
    }
}
