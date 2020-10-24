package com.grailslab.command


import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee

/**
 * Created by USER on 23-Mar-17.
 */
class SalaryIncrementCommand implements grails.validation.Validateable{
    Long id
    YearMonths yearMonths
    AcademicYear academicYear

    Employee employee
    Double previousSalary
    Double previousdpsAmount
    Double oldBasic
    Double incrementPercentage
    Double newBasic
    Double houseRent
    Double medical
    Double grossSalary
    Double inCharge
    Double mobileAllowance
    Double others
    Double totalSalary
    Double dpsAmountSchool
    Double netIncrement


    static constraints = {
        id nullable: true
        previousdpsAmount nullable: true
        inCharge nullable: true
        mobileAllowance nullable: true
        others nullable: true
        dpsAmountSchool nullable: true
        netIncrement nullable: true
        medical nullable: true
        houseRent nullable: false
    }

}
