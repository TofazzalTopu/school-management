package com.grailslab.command.report

import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

/**
 * Created by Aminul on 2/27/2016.
 */
class EmpDailyAttendanceCommand implements grails.validation.Validateable{
    Date rStartDate
    Date rEndDate
    HrCategory hrCategory
    Employee employee
    String attndanceStatus
    PrintOptionType printOptionType
    static constraints = {
        rEndDate nullable: true
        hrCategory nullable: true
        printOptionType nullable: true
        attndanceStatus nullable: true
        employee nullable: true
    }
}
