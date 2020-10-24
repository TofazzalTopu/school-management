package com.grailslab.accounting

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Student

class InvoiceDetails {

    Invoice invoice
    Student student
    FeeItems feeItems
    String description      // in case of item details. ie. January - March
    Integer quantity = 0
    Double amount = 0
    Double discount = 0     //discount percent
    Double netPayment = 0
    Double totalPayment = 0
    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        student nullable: true
        description nullable: true
    }
}
