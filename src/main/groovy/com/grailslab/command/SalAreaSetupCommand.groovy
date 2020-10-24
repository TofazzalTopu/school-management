package com.grailslab.command

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee

/**
 * Created by bddev on 12/3/2016.
 */
class SalAreaSetupCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    YearMonths yearMonths
    Employee employee
    Double amount

}
