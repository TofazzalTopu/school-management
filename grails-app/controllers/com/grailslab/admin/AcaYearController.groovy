package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.AcaYearCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.AcaYear
import grails.converters.JSON

class AcaYearController {

    def acaYearService

    def index() {
        params.acaYearType = AcaYearType.ALL.value
        LinkedHashMap resultMap = acaYearService.acaYearPaginateList(params)
        String defaultYearType = AcaYearType.ALL.value
        def acaYearTypeList = AcaYearType.values()
        def academicYearList = AcademicYear.values()
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/acaYear', model: [dataReturn: null, acaYearTypeList: acaYearTypeList, academicYearList: academicYearList, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/acaYear', model: [dataReturn: resultMap.results, acaYearTypeList: acaYearTypeList, academicYearList: academicYearList, defaultYearType: defaultYearType, totalCount: totalCount])
    }

    def save(AcaYearCommand command) {

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Please fill the form correctly')
            outPut = result as JSON
            render outPut
            return
        }

        result = acaYearService.saveAcaYear(command, params)
        outPut = result as JSON
        render outPut
    }

    def update(AcaYearCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Please fill the form correctly')
            outPut = result as JSON
            render outPut
            return
        }

        result = acaYearService.updateAcaYear(command, params)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = acaYearService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

    def updateWorkingYear(Long id) {
        LinkedHashMap result = acaYearService.updateWorkingYear(id)
        String outPut = result as JSON
        render outPut
    }

    def updateAdmissionYear(Long id) {
        LinkedHashMap result = acaYearService.updateAdmissionYear(id)
        String outPut = result as JSON
        render outPut
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = acaYearService.acaYearPaginateList(params)

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
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        AcaYear acaYear = acaYearService.read(id)
        if (!acaYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, acaYear)
        outPut = result as JSON
        render outPut
    }

}
