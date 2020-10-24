package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class StudentPayCommand implements grails.validation.Validateable{
    String student
    String payStatus
    AcademicYear academicYear
    static constraints = {
        academicYear nullable: true
    }
}
