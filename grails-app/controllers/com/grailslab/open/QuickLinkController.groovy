package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.OpenQuickLinkCommand
import com.grailslab.enums.LinkType
import grails.converters.JSON

class QuickLinkController {

    def quickLinkService

    def index() {
        LinkedHashMap resultMap = quickLinkService.paginateList(params)
        def quickLinkType = LinkType.linkTypeContents()
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/quickLink/quickLink', model: [dataReturn: null, totalCount: 0, quickLinkType:quickLinkType])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/quickLink/quickLink', model: [dataReturn: resultMap.results, totalCount: totalCount, quickLinkType:quickLinkType])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = quickLinkService.paginateList(params)

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

    def save(OpenQuickLinkCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = quickLinkService.saveOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        OpenQuickLink quickLink = OpenQuickLink.read(id)
        if (!quickLink) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, quickLink)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = quickLinkService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = quickLinkService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
