package com.grailslab.command

import com.grailslab.accounting.FeeItems
import com.grailslab.enums.ScholarshipType
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class StudentScholarshipCommand implements grails.validation.Validateable{
    Student student
    ScholarshipType scholarshipType
    static constraints = {

        student nullable: true
        scholarshipType nullable: true
    }
}
