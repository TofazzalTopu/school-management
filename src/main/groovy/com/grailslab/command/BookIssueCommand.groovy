package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.library.Book
import com.grailslab.stmgmt.Student
import sun.util.calendar.AbstractCalendar

/**
 * Created by Hasnat on 6/28/2015.
 */
class BookIssueCommand implements grails.validation.Validateable{
    Long id
    AcademicYear stdEmpAcademicYr
    String stdEmpId
    Book book
    String bookBarcode
    Date issueDate
    Date returnDate

    static constraints = {
        id nullable: true
        issueDate nullable: true
        returnDate nullable: true
        book nullable: true
        bookBarcode nullable: true
    }

}
