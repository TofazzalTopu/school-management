package com.grailslab.command

/**
 * Created by Hasnat on 6/28/2015.
 */
class BookPublisherCommand implements grails.validation.Validateable{
    Long id
    String name
    String bengaliName
    String address
    String country
    static constraints = {
        id nullable: true
        bengaliName nullable: true
        address nullable: true
        country nullable: true
    }
}
