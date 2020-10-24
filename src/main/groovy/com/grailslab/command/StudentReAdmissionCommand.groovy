package com.grailslab.command


import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student

/**
 * Created by aminul on 3/19/2015.
 */
class StudentReAdmissionCommand implements grails.validation.Validateable{
    Student student
    Section section
    Integer rollNo
    AcademicYear academicYear
    YearMonths admissionMonth
    static constraints = {
    }
}
