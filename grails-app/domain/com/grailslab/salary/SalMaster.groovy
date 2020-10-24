package com.grailslab.salary

import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class SalMaster  {
    YearMonths yearMonths
    SalaryStatus salaryStatus
    String footNote
    Double pfContribution
    String pfCalField
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

        footNote nullable: true
        pfContribution nullable: true
        pfCalField nullable: true
    }
    static mapping = {
        footNote type: 'text'
    }
}
