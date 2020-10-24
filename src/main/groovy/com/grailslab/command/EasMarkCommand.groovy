package com.grailslab.command

import com.grailslab.hr.Employee
import com.grailslab.stmgmt.Student

class EasMarkCommand implements grails.validation.Validateable{
    Long id
    Student student
    Double mark
    String description
    Date entryDate
    Employee addedBy

    static constraints = {
        id nullable: true
    }
}
