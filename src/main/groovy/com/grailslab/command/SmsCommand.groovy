package com.grailslab.command

import com.grailslab.enums.SelectionTypes

/**
 * Created by Hasnat on 6/27/2015.
 */
class SmsCommand implements grails.validation.Validateable{
    String message
    String selectIds
    static constraints = {
        message(nullable: false, size: 1..479)
    }
}
