package com.grailslab.viewz

class BookTransactionView {

    long id
    long bookId

    Long schoolId
    long referenceId                // studentId, employeeId, memberId
    String academicYear
    String referenceType            // Student, Teacher, Member
    Date issueDate

    String bId                      // Book.bookId
    String bookDetail
    String authorName

    String refId
    String refName

    Double fine
    String payType
    Date returnDate
    String returnType
    Date submissionDate

    static constraints = {

    }

    static mapping = {
        table 'view_book_transaction'
        version false
    }
}
