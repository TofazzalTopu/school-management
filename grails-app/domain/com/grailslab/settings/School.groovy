package com.grailslab.settings

import com.grailslab.gschoolcore.AcademicYear


class School {
    long id
    String name
    String address
    String email
    String contactNo
    AcademicYear academicYear
    String nonMasking
    String tabulationType

    static constraints = {
        academicYear nullable: true
        nonMasking nullable: true
    }
    static mapping = {
//        id generator: 'assigned'
//        cache true
    }
}
