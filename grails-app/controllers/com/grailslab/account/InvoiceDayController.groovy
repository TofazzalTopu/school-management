package com.grailslab.account

import com.grailslab.CommonUtils
import com.grailslab.accounting.InvoiceDay
import com.grailslab.command.InvoiceDayCommand
import grails.converters.JSON

class InvoiceDayController {

    def invoiceDayService

    def index() {
        LinkedHashMap resultMap = invoiceDayService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/collection/invoiceDay/invoiceDay', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/collection/invoiceDay/invoiceDay', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = invoiceDayService.paginateList(params)

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

    def save(InvoiceDayCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = invoiceDayService.saveOrUpdate(command)
        String output = result as JSON
        render output
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        InvoiceDay invoiceDay = InvoiceDay.read(id)
        if (!invoiceDay) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, invoiceDay)
        outPut = result as JSON
        render outPut
    }

    def reOpen(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = invoiceDayService.reOpen(id)
        String output = result as JSON
        render output
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = invoiceDayService.delete(id)
        String output = result as JSON
        render output
    }
}
