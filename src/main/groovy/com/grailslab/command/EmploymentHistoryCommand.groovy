package com.grailslab.command

import com.grailslab.hr.Employee

/**
 * Created by Hasnat on 6/27/2015.
 */
class EmploymentHistoryCommand implements grails.validation.Validateable{

    Long id
    Employee employee
    String company
    String jobTitle
    Date joiningDate
    Date endDate
    String location
    static constraints = {
        id nullable: true
        joiningDate nullable: true
        endDate nullable: true
        location nullable: true
        employee nullable: true
    }
}
