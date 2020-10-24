package com.grailslab.admin


import com.grailslab.command.SchoolCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.settings.School
import grails.converters.JSON

class SchoolController {

    def schoolService

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        LinkedHashMap resultMap = schoolService.schoolPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/school', model: [academicYearList: academicYearList, dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/school', model: [academicYearList: academicYearList, dataReturn: resultMap.results, totalCount: totalCount])

    }

    def save(SchoolCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = schoolService.saveOrUpdate(command)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =schoolService.schoolPaginateList(params)

        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
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
        result.put('isError',true)
        String outPut
        School school = School.read(id)
        if (!school) {
            result.put('message','School not found')
            outPut = result as JSON
            render outPut
            return
        }
        result.put('isError',false)
        result.put('obj',school)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        LinkedHashMap result = schoolService.inactive(id)
        String output = result as JSON
        render output
    }
}
