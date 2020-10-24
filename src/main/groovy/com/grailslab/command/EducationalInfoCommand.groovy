package com.grailslab.command

import com.grailslab.hr.Board
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCertification
import com.grailslab.hr.Institution
import com.grailslab.settings.SubjectName

/**
 * Created by Hasnat on 6/27/2015.
 */
class EducationalInfoCommand implements grails.validation.Validateable{
    Long id
    Employee employee
    String name
    HrCertification certification
    Institution institution
    SubjectName majorSubject
    Board board
    Date passingYear
    String result
    String duration
    static constraints = {
        id nullable: true
        name nullable: true
        board nullable: true
        passingYear nullable: true
        employee nullable: true
    }
}
