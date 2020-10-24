package com.grailslab.salary

import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalAdvance {
    Employee employee
    Date advanceDate
    Date paidDate
    Double outStandingAmount = 0
    Double currAmount
    Double installmentAmount
    String description
    PayType payType
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

        description nullable: true
        paidDate nullable: true
    }
}
