package com.grailslab.salary

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalAttendance {
    YearMonths yearMonths
    Integer workingDays
    Integer holidays
    Employee employee
    Integer presentDays
    Integer lateDays
    Integer absentDays
    Integer leaveDays

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

        yearMonths nullable: true
        workingDays nullable: true
        holidays nullable: true
        presentDays nullable: true
        lateDays nullable: true
        absentDays nullable: true
        leaveDays nullable: true
    }

}
