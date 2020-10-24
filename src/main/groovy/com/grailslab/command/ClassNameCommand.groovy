package com.grailslab.command

import com.grailslab.hr.HrPeriod

//  ClassNameController - save
class ClassNameCommand implements grails.validation.Validateable{
    Long id
    String name
    String sectionType
    Integer sortPosition
    boolean allowOptionalSubject
    boolean groupsAvailable
    Integer expectedNumber
    Integer workingDays
    HrPeriod hrPeriod

    static constraints = {
        id nullable: true
        workingDays nullable: true
        hrPeriod nullable: true
        expectedNumber nullable: true
    }
}
