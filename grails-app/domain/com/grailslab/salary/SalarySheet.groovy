package com.grailslab.salary

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalarySheet  {
    SalMaster salMaster
    Employee employee
    YearMonths yearMonths
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
    Boolean adStatus
    Double dpsAmount
    Double dpsAmountSchool
    Double adsAmount
    Double fine
    Double pc
    Double netPayable
    Integer lateDays
    Double lateFine
    Integer ulDays
    Double  ulFine
    Integer extraClass
    Double  extraClassAmount
    Double increment
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

        grossSalary nullable: true
        totalSalary nullable: true
        basic nullable: true
        houseRent nullable: true
        medical nullable: true
        inCharge nullable: true
        mobileAllowance nullable: true
        others nullable: true
        pfStatus nullable: true
        dpsAmount nullable: true
        fine nullable: true
        pc nullable: true
        netPayable nullable: true
        area nullable: true
        adStatus nullable: true
        adsAmount nullable: true
        lateDays  nullable: true
        lateFine  nullable: true
        ulDays    nullable: true
        ulFine    nullable: true
        extraClass nullable: true
        extraClassAmount nullable: true
        dpsAmountSchool nullable:true
        increment nullable:true
    }

}
