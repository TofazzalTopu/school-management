package com.grailslab.command

import com.grailslab.hr.Employee

/**
 * Created by Hasnat on 6/27/2015.
 */
class AdvanceAmountCommand implements grails.validation.Validateable{
    Long id
    Employee employee
    Date date
    double amount
    String description

    static constraints = {
        id nullable: true
        description nullable: true
    }
}
