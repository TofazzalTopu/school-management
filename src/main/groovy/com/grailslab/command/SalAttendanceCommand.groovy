package com.grailslab.command

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee

/**
 * Created by Hasnat on 6/27/2015.
 */
class SalAttendanceCommand implements grails.validation.Validateable{
    YearMonths yearMonths
    AcademicYear academicYear
    Integer workingDays
    Integer holidays
    Employee employee
    Integer presentDays
    Integer lateDays
    Integer absentDays
    Integer leaveDays

    static constraints = {
        yearMonths nullable: true
        workingDays nullable: true
        holidays nullable: true
        presentDays nullable: true
        lateDays nullable: true
        absentDays nullable: true
        leaveDays nullable: true

    }
}
