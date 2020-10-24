package com.grailslab

import com.grailslab.command.BookIssueCommand
import com.grailslab.command.BookReceiveCommand
import com.grailslab.enums.BookStockStatus
import com.grailslab.enums.PayType
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.library.Book
import com.grailslab.library.BookTransaction
import com.grailslab.library.LibraryMember
import com.grailslab.stmgmt.Student
import com.grailslab.viewz.BookTransactionView
import com.grailslab.viewz.LibraryMemberView
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class BookTransactionService {

    def studentService
    def schoolService
    def messageSource
    def employeeService
    def classNameService
    def springSecurityService

    static final String[] sortColumns = ['refName', 'bId', 'bookDetail', 'authorName', 'issueDate', 'returnDate']

    LinkedHashMap issuePaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = BookTransactionView.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("returnType", PayType.DUE.name())
            }
            if (sSearch) {
                or {
                    ilike('bId', sSearch)
                    ilike('refId', sSearch)
                    ilike('refName', sSearch)
                    ilike('bookDetail', sSearch)
                    ilike('authorName', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }

        String issueDate
        String returnDate
        String borrower = ''
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            for (transaction in results) {
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                borrower = transaction?.refId + ' - ' + transaction?.refName
                issueDate = transaction.issueDate ? CommonUtils.getUiDateStr(transaction.issueDate) : ''
                returnDate = transaction.returnDate ? CommonUtils.getUiDateStr(transaction.returnDate) : ''
                dataReturns.add([DT_RowId: transaction.id, 0: serial, 1: borrower, 2: transaction.bId, 3: transaction.bookDetail, 4: transaction.authorName, 5: issueDate, 6: returnDate, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def bookIssueSave(BookIssueCommand command) {
        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        BookTransaction transaction
        String message
        LibraryMemberView bookMemberView
        Student student
        Employee employee
        LibraryMember libraryMember
        Book book
        if (command.id) {
            transaction = BookTransaction.get(command.id)
            if (!transaction) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            transaction.book = command.book
            if (!command.issueDate) {
                transaction.issueDate = new Date()
            } else {
                transaction.issueDate = command.issueDate
            }
            transaction.returnDate = command.returnDate
            transaction.updatedBy = createOrUpdateBy
            transaction.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Issue Updated successfully")
            return result
        } else {
            if (command.book) {
                book = command.book
            } else if (command.bookBarcode) {
                book = Book.findByBarcode(command.bookBarcode)
            }
            if (!book) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Please select book for issue")
                return result
            }

            bookMemberView = LibraryMemberView.findByStdEmpNo(command.stdEmpId)
            if (bookMemberView.objType == "student") {
                student = Student.findByStudentIDAndAcademicYearAndStudentStatus(command.stdEmpId, command.stdEmpAcademicYr, StudentStatus.NEW)
                if (!student) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "$command.stdEmpId not found as student in $command.stdEmpAcademicYr.value. Please select year correctly or contact with admin")
                    return result
                }

            } else if (bookMemberView.objType == "employee") {
                employee = Employee.read(bookMemberView.objId)
                if (!employee) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "$command.stdEmpId not found as teacher")
                    return result
                }
            } else if (bookMemberView.objType == "Guardian") {
                libraryMember = LibraryMember.findByIdAndActiveStatus(bookMemberView.objId, ActiveStatus.ACTIVE)
                if (!libraryMember) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "$command.stdEmpId not found as member")
                    return result
                }
            }

            Date issueDate = command.issueDate
            Date returnDate = command.returnDate
            if (!issueDate) {
                issueDate = new Date()
            }
            if (!returnDate) {
                returnDate = issueDate.plus(7)
            }

            transaction = new BookTransaction(
                    referenceType: bookMemberView.objType,
                    referenceId: bookMemberView.objId,
                    book: book,
                    issueDate: issueDate,
                    returnDate: returnDate,
                    createdBy: createOrUpdateBy,
                    academicYear: academicYear
            )

            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Book Issue Save successfully'
            transaction.save(flush: true)
            book.stockStatus = BookStockStatus.OUT
            book.save(flush: true)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }
    }

    def bookReturnSave(BookReceiveCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        BookTransaction transaction
        String message
        transaction = BookTransaction.get(command.id)
        if (!transaction) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (command.submissionDate) {
            transaction.submissionDate = command.submissionDate
            transaction.fine = command.fine
        } else {
            transaction.submissionDate = new Date()
            transaction.fine = 0
        }
        transaction.returnType = PayType.PAID
        transaction.updatedBy = springSecurityService.principal?.username
        if (transaction.save(flush: true)) {
            Book returnBook = transaction.book
            returnBook.stockStatus = BookStockStatus.ADDED
            returnBook.save(flush: true)
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        message = 'Book Return successfully'
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def issueDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BookTransaction transaction = BookTransaction.get(id)
        String createOrUpdateBy = springSecurityService.principal?.username
        if (!transaction) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        transaction.book.stockStatus = BookStockStatus.ADDED
        transaction.activeStatus = ActiveStatus.INACTIVE
        transaction.updatedBy = createOrUpdateBy
        transaction.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Book Issue Deleted Successfully.")
        return result
    }
}
