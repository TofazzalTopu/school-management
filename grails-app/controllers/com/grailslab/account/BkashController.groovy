package com.grailslab.account

import com.grailslab.CommonUtils
import com.grailslab.accounting.FeePayments
import com.grailslab.accounting.Invoice
import com.grailslab.accounting.InvoiceDetails
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON
import org.grails.web.json.JSONObject

class BkashController {

    def classNameService
    def bkashService
    def portalService
    def springSecurityService

    def index() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        String toDateStr = CommonUtils.getUiDateStrForPicker(new Date())
        if (!classNameList) {
            render(view: '/bkash/bKashTransaction', model: [dataReturn: null, totalCount: 0, classNameList: null, toDateStr: toDateStr])
            return
        }
        LinkedHashMap resultMap = bkashService.manageCollectionPaginationList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/bkash/bKashTransaction', model: [dataReturn: null, totalCount: 0, classNameList: classNameList, toDateStr: toDateStr])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/bkash/bKashTransaction', model: [dataReturn: resultMap.results, totalCount: totalCount, classNameList: classNameList, toDateStr: toDateStr])
    }

    def listManage() {
        LinkedHashMap gridData
        String result
        println(params)
        LinkedHashMap resultMap = bkashService.manageCollectionPaginationList(params)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def onlinePayment() {
        def invoiceId = params.getInt('invoiceId')
        Invoice invoice = Invoice.findById(invoiceId)
        List invoiceShowList = new ArrayList()
        def invoiceDetailses = InvoiceDetails.findAllByInvoice(invoice)
        for (invoiceDetail in invoiceDetailses) {
            invoiceShowList.add([feeItem: invoiceDetail.feeItems.name, description: invoiceDetail.description, quantity: invoiceDetail.quantity, amount: invoiceDetail.amount, discount: invoiceDetail.discount, netPayment: invoiceDetail.netPayment, totalPayment: invoiceDetail.totalPayment])
        }
        render(view: '/bkash/onlinePaymentAccount', model: [hasPending: true, student: null, invoiceId: invoice?.id, invoiceAmount: invoice.payment, invoiceShowList: invoiceShowList, isAccount: true])
    }

    def deleteOnlinePayment() {
        def invoiceId = params.invoiceId
        portalService.deleteInvoice(invoiceId)
        redirect(action: 'index')
    }

    def verifyOnlinePayment() {
        Double bkashAmount = 0.0
        JSONObject jsonTransaction
        String invoiceId = params.get('invoiceId')
        String transactionId = params.get('transactionId')
        Invoice invoiceExist = Invoice.findByTransactionId(transactionId)
        String createOrUpdateBy = springSecurityService.principal?.username
        if (invoiceExist) {
            flash.message = "Already used this Transaction Id."
            redirect(action: 'onlinePayment', params: [invoiceId: invoiceId])
        } else {
            Invoice invoice = Invoice.findById(invoiceId)
            jsonTransaction = portalService.getTransactionDetails(transactionId)
            if (jsonTransaction != null) {
                String transactionStatus = "" + jsonTransaction.get("trxStatus");
                println(transactionStatus)
                //check if transaction status is Transaction Successful
                if (transactionStatus == '0000') {
                    String amount = "" + jsonTransaction.get("amount")
                    println(amount)
                    if (amount != "") {
                        bkashAmount = Double.parseDouble(amount)
                    }
                } else {
                    flash.message = "Somehow your payment is pending, Contact with Administrator."
                    redirect(action: 'onlinePayment', params: [invoiceId: invoiceId])
                }
                if (bkashAmount >= invoice.payment) {
                    def feePaymentsList = FeePayments.findAllByInvoice(invoice)
                    feePaymentsList.each { FeePayments feePayments ->
                        feePayments.activeStatus = ActiveStatus.ACTIVE
                        feePayments.updatedBy = createOrUpdateBy
                        feePayments.save(flush: true)
                    }
                    invoice.activeStatus = ActiveStatus.ACTIVE
                    invoice.updatedBy = createOrUpdateBy
                    invoice.transactionId = transactionId
                    invoice.save(flush: true)
                    flash.message = "Congratulation You have successfully completed your payment."
                    redirect(action: 'index')
                }
            } else {
                flash.message = "Somehow your payment is pending, Contact with Administrator."
                redirect(action: 'onlinePayment', params: [invoiceId: invoiceId])
            }
        }
    }

}
