package com.grailslab.command

/**
 * Created by aminul on 3/22/2015.
 */
class ProfessionCommand implements grails.validation.Validateable{
    Long id
    String name
    String description

    static constraints = {
        id nullable: true
        description nullable: true
    }
}
