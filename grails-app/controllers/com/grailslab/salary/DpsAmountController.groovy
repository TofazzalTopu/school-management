package com.grailslab.salary

import grails.converters.JSON

class DpsAmountController {

    def dpsAmountService

    def index () {
        render( view: '/salary/salaryReport/dps')
    }

    def save() {
        LinkedHashMap result = dpsAmountService.saveOrUpdate(params)
        String outPut = result as JSON
        render outPut
    }

    def dpsAmountList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = dpsAmountService.paginateList(params)
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

    def delete(Long id) {
        LinkedHashMap result = dpsAmountService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
