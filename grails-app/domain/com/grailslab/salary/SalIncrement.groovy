package com.grailslab.salary

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class SalIncrement {
    def springSecurityService
    static transients = ['springSecurityService']

    Employee employee
    Double previousSalary = 0
    Double previousdpsAmount = 0
    Double oldBasic = 0
    Double incrementPercentage = 0
    Double newBasic = 0
    Double houseRent = 0
    Double medical = 0
    Double grossSalary = 0
    Double inCharge = 0
    Double mobileAllowance = 0
    Double others = 0
    Double totalSalary = 0
    Double dpsAmountSchool = 0
    Double netIncrement = 0
    YearMonths yearMonths
    String incrementStatus

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

        previousdpsAmount nullable: true
        houseRent nullable: true
        medical nullable: true
        inCharge nullable: true
        mobileAllowance nullable: true
        others nullable: true
        dpsAmountSchool nullable: true
        netIncrement nullable: true
        incrementStatus nullable: true

    }
    def beforeInsert() {
        def loggedUser =springSecurityService?.principal
        if (!createdBy) {
            createdBy = loggedUser?.username?:'system'
        }
        if(!academicYear){
//               academicYear=AcademicYear.Y2014
            academicYear=loggedUser?.academicYear
        }
    }
    def beforeUpdate(){
        def loggedUser =springSecurityService?.principal
        if (!updatedBy) {
            updatedBy = loggedUser?.username?:'system'
        }
    }

}
