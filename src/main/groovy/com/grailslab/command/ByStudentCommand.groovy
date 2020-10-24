package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class ByStudentCommand implements grails.validation.Validateable{
    String quickFee
    Integer feePaidCriteria
    String student
    AcademicYear academicYear
    static constraints = {
        quickFee nullable: true
        feePaidCriteria nullable: true
        academicYear nullable: true
    }
}
