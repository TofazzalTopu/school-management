package com.grailslab.stmgmt

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName

class RegForm {
    ClassName className
    Date regOpenDate
    Date regCloseDate
    Date fromBirthDate
    Date toBirthDate
    String instruction
    String regFormNote
    String applyType  //online, offline, both
    Double formPrice
    String serialPrefix
    Integer serialStartFrom
    String applicationPrefix
    Integer applicationStartFrom

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted
    String bandSchool

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        instruction nullable: true
        regFormNote nullable: true
        formPrice nullable: true
        serialPrefix nullable: true
        serialStartFrom nullable: true
        applicationPrefix nullable: true
        applicationStartFrom nullable: true
        fromBirthDate nullable: true
        toBirthDate nullable: true
        bandSchool nullable: true

    }
    static mapping = {
        instruction type: 'text'
    }
}
