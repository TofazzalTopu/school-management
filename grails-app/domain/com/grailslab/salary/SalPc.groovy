package com.grailslab.salary

import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalPc {
    Employee employee
    Date paidDate
    Double openingAmount = 0
    Double outStandingAmount = 0
    Double payableAmount = 0
    Double installmentAmount = 0
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
