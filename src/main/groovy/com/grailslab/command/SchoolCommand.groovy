package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear

/**
 * Created by aminul on 3/22/2015.
 */
class SchoolCommand implements grails.validation.Validateable{
    Long id
    String name
    String address
    String email
    String contactNo
    AcademicYear academicYear
    String nonMasking

    static constraints = {
        id nullable: true
        nonMasking nullable: true
        academicYear nullable: true

    }
}
