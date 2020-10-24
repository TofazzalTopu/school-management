package com.grailslab.accounting

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Student

class FeePayments {

    Student student
    FeeItems feeItems
    ItemsDetail itemsDetail
    String itemDetailName
    Integer itemDetailSortPosition
    Integer quantity = 0
    Double amount = 0
    Double discount = 0     //discount percent
    Double netPayment = 0
    Double totalPayment = 0
    Invoice invoice
    Boolean includedInSummary = false
    Date summaryDate

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

        itemsDetail nullable: true
        itemDetailName nullable: true
        itemDetailSortPosition nullable: true
        includedInSummary nullable: true
        summaryDate nullable: true
    }
    static mapping = {
        includedInSummary defaultValue: Boolean.FALSE
    }
}
