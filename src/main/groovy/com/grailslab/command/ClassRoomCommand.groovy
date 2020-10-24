package com.grailslab.command

/**
 * Created by aminul on 3/22/2015.
 */
class ClassRoomCommand implements grails.validation.Validateable{
    Long id
    String name
    String description

    static constraints = {
        description nullable: true
        id nullable: true
    }
}
