package com.grailslab.command

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee

/**
 * Created by Hasnat on 6/27/2015.
 */
class SalarySetupCommand implements grails.validation.Validateable{
    Employee employee
    Double grossSalary
    Double totalSalary
    Double basic
    Double houseRent
    Double medical
    Double inCharge
    Double mobileAllowance
    Double others
    Double area
    Boolean pfStatus
    Boolean fineOnAbsent
    Double dpsAmount
    Double fine
    Double netPayable

    static constraints = {
        grossSalary nullable: true
        totalSalary nullable: true
        basic nullable: true
        houseRent nullable: true
        medical nullable: true
        inCharge nullable: true
        mobileAllowance nullable: true
        others nullable: true
        pfStatus nullable: true
        fineOnAbsent nullable: true
        dpsAmount nullable: true
        fine nullable: true
        netPayable nullable: true
        area nullable: true
    }
}
