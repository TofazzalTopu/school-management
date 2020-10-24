package com.grailslab.command

/**
 * Created by Hasnat on 6/27/2015.
 */
//@todo-aminul remove this

class CafeteriaCommand implements grails.validation.Validateable{
    Long id
    Double amount
    Double discount
    String name
    Integer code
    String description
    static constraints = {
        id nullable: true
        description nullable: true
    }
}
