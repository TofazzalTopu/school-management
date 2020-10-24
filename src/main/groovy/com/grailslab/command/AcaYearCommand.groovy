package com.grailslab.command

/**
 * Created by aminul on 3/22/2015.
 */
class AcaYearCommand implements grails.validation.Validateable{
    Long id
    String name
    String yearType

    static constraints = {
        id nullable: true

    }
}
