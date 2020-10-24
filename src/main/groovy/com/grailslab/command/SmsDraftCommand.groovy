package com.grailslab.command

import com.grailslab.enums.SelectionTypes

/**
 * Created by Hasnat on 6/27/2015.
 */
class SmsDraftCommand implements grails.validation.Validateable{
    Long id
    String name
    String message
    static constraints = {
        id nullable: true
        message(nullable: false, size: 1..479)
    }
}
