package com.grailslab.settings

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrPeriod

class ClassName {
    String name
    Integer sortPosition
    Boolean allowOptionalSubject = false
    Boolean groupsAvailable
    Boolean subjectGroup  = false
    Boolean subjectSba  = false
    Integer workingDays
    Integer expectedNumber
    String sectionType          // Primary, High, College
    HrPeriod hrPeriod
    //Per Subject in Percentage. Will use for Progress report card week subjects.
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

        sectionType nullable: true
        groupsAvailable nullable: true
        workingDays nullable: true
        subjectGroup nullable: true
        subjectSba nullable: true
        hrPeriod nullable: true
    }
    static mapping = {
        expectedNumber defaultValue: 60
        groupsAvailable defaultValue: Boolean.FALSE
    }
}
