package com.grailslab.accounting

import com.grailslab.SequenceConstructor
import com.grailslab.enums.AccountType
import com.grailslab.enums.PaymentType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Student

class Invoice {
    Student student
    String invoiceNo
    Double amount = 0         //Total Paid amonut
    Double discount = 0     // Total Discount
    Double payment = 0      // Net Payment
    Date invoiceDate
    String description
    String comment
    AccountType accountType     // Income or expense
    Boolean verified = true
    Boolean paid = true
    String refInvoiceNo     //in case of edit invoice
    PaymentType paymentType = PaymentType.CASH
    String transactionId

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

        invoiceNo unique: true
        student nullable: true
        description nullable: true, maxSize: 2000
        comment nullable: true
        refInvoiceNo nullable: true
        paymentType nullable: true
        transactionId nullable: true
    }

    static {
        //sequential auto generated sequence with custom logic
        SequenceConstructor.constructSequence(
                "Invoice.invoiceNoSequence", 1,
                """
                invCode.toUpperCase() + String.format("%06d", sequenceNumber)
                """
        )
    }
}
