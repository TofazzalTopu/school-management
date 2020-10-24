package com.grailslab.command


import com.grailslab.enums.TcType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.stmgmt.Student

/**
 * Created by aminul on 3/19/2015.
 */
class TcAndDropOutCommand implements grails.validation.Validateable{
    Long id
    Student student
    TcType tcType       //Tc, Dropout, Defaulter, other
    String reason
    String schoolName
    Date releaseDate
    String releaseText
    AcademicYear academicYear

    static constraints = {
        id nullable: true
        student nullable: true
        academicYear nullable: true
        schoolName nullable: true
        releaseDate nullable: true
        releaseText nullable: true
    }
}
