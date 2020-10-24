package com.grailslab.accounting

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Student

class StudentDiscount {
    Student student
    Double discountPercent
    Double amount
    FeeItems feeItems

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted
    static constraints = {
        updatedBy(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
    }
}
