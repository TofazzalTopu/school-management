package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.OpenContentCommand
import com.grailslab.enums.OpenContentType
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON

class FeatureController {
    def springSecurityService
    def messageSource
    def featureService

    def index() {
        def openContentTypes = OpenContentType.featureContents()
        LinkedHashMap resultMap = featureService.featurePaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/open/feature/allfeature', model: [dataReturn: null, totalCount: 0, openContentTypes: openContentTypes])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/open/feature/allfeature', model: [dataReturn: resultMap.results, totalCount: totalCount, openContentTypes: openContentTypes])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = featureService.featurePaginateList(params)

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
        if(!params.featureType){
            redirect(action: 'index')
            return
        }
        OpenContentType openContentType = OpenContentType.valueOf(params.featureType)
        if(!openContentType){
            redirect(action: 'index')
            return
        }
        render(view: '/open/feature/createOrEdit', model: [openContentType:openContentType.key])
    }

    def edit(Long id) {
        OpenContent openContent = OpenContent.read(id)
        if (!openContent) {
            flash.message="Feature not found"
            redirect(action: 'index')
            return
        }
        render(view: '/open/feature/createOrEdit', model: [openContent:openContent])
    }

    def save(OpenContentCommand command) {
        if (!request.method.equals('POST')) {
            flash.message = "Request Method not allow here."
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = featureService.saveOrUpdate(command)
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
        LinkedHashMap result = featureService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = featureService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
