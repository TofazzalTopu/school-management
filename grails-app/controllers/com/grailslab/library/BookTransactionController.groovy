package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.BookIssueCommand
import com.grailslab.command.BookReceiveCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.viewz.BookTransactionView
import grails.converters.JSON

class BookTransactionController {

    def schoolService
    def messageSource
    def bookTransactionService

    def index() {
        redirect(action: 'bookIssue')
    }

    def bookIssue() {
        def workingYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: '/library/bookIssue', model: [workingYearList: workingYearList])
    }

    def bookIssueReturn() {
        def workingYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: '/library/bookIssueAndReturn', model: [workingYearList: workingYearList])
    }

    def bookIssueList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = bookTransactionService.issuePaginateList(params)

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

    def bookIssueSave(BookIssueCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = bookTransactionService.bookIssueSave(command)
        String outPut = result as JSON
        render outPut
    }

    def bookReturnSave(BookReceiveCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = bookTransactionService.bookReturnSave(command)
        String outPut = result as JSON
        render outPut
    }

    def bookReturn() {

        Book book = Book.read(params.getLong('id'))
        BookTransaction bookTransaction = BookTransaction.findByBookAndReturnTypeAndActiveStatus(book, PayType.DUE, ActiveStatus.ACTIVE)
        String issueTo
        if (bookTransaction)

            LinkedHashMap result = new LinkedHashMap()
        String outPut

        result.put("book", params)

        outPut = result as JSON
        render outPut

    }

    def issueEdit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String borrowerType = ''
        String borrowerName = ''
        String borrowerId

        BookTransaction transaction = BookTransaction.read(id)
        BookTransactionView transactionView
        if (!transaction) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        transactionView = BookTransactionView.findById(transaction.id)
        if (transactionView) {
            borrowerType = transactionView.referenceType
            borrowerId = transactionView.refId
            borrowerName = transactionView.refId + ' - ' + transactionView.refName
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, transaction)
        result.put('borrowerType', borrowerType)
        result.put('borrowerName', borrowerName)
        result.put('borrowerId', borrowerId)
        result.put('borrowerBookName', transactionView.bookDetail)
        result.put('borrowingYear', transaction.academicYear)
        outPut = result as JSON
        render outPut
    }

    def issueReturn(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String borrowerName
        BookTransaction transaction = BookTransaction.read(id)
        if (!transaction) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        BookTransactionView transactionView = BookTransactionView.findById(transaction.id)
        if (transactionView) {
            borrowerName = transactionView?.refId + ' - ' + transactionView?.refName
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, transaction)
        result.put('borrowerName', borrowerName)
        result.put('borrowerBookName', transactionView.bookDetail)
        result.put('bookIssueDate', transaction.issueDate)
        result.put('bookReturnDate', transaction.returnDate)
        outPut = result as JSON
        render outPut
    }

    def issueDelete(Long id) {
        LinkedHashMap result = bookTransactionService.issueDelete(id)
        String outPut = result as JSON
        render outPut
    }

    def getBookByBarcode(String barcode) {
        if (!request.method.equals('POST')) {
            redirect(action: 'bookIssue')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String bookIssue
        String borrowerType
        String borrowerName
        Book book = Book.findByBarcode(barcode)
        if (!book) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Book Not Found.')
            outPut = result as JSON
            render outPut
            return
        }

        BookTransaction transaction = BookTransaction.findByBookAndReturnType(book, PayType.DUE)
        BookTransactionView transactionView = BookTransactionView.findByIdAndReturnType(transaction.id, PayType.DUE.name())
        if (transaction && transactionView) {
            borrowerType = transactionView.referenceType
            borrowerName = transactionView.refId + ' - ' + transactionView.refName
            bookIssue = 'issue'
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('book', book)
        result.put('transaction', transaction)
        result.put('borrowerType', borrowerType)
        result.put('borrowerName', borrowerName)
        result.put('bookIssue', bookIssue)
        outPut = result as JSON
        render outPut
    }
}





