package com.grailslab.salary


import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalSetup {
    Employee employee
    Double grossSalary
    Double totalSalary
    Double basic
    Double houseRent
    Double medical
    Double inCharge
    Double mobileAllowance
    Double others
    //Double area
    Boolean pfStatus
    Boolean fineOnAbsent
    Double dpsAmount
    Double dpsAmountSchool
    Double fine
    Double netPayable

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
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
        fineOnAbsent nullable: true
        dpsAmount nullable: true
        dpsAmountSchool nullable: true
        fine nullable: true
        netPayable nullable: true
    }
}
