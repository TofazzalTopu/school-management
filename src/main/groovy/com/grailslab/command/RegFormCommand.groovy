package com.grailslab.command

import com.grailslab.enums.BloodGroup
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName

/**
 * Created by Hasnat on 6/28/2015.
 */
class RegFormCommand implements grails.validation.Validateable{
    Long regFormId
    String schoolClassIds
    Date regOpenDate
    Date regCloseDate
    Date fromBirthDate
    Date toBirthDate
    String instruction
    String regFormNote
    String applyType  //online, offline, both
    Double formPrice
    String applicationPrefix
    String serialPrefix
    AcademicYear academicYear
    String bandSchool

    static constraints = {
        academicYear nullable: true
        instruction nullable: true
        regFormNote nullable: true
        formPrice nullable: true
        regFormId nullable:true
        fromBirthDate nullable: true
        toBirthDate nullable: true
        bandSchool nullable: true
    }
}
