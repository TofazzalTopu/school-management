package com.grailslab.command

/**
 * Created by Hasnat on 6/27/2015.
 */
class QuickSmsCommand implements grails.validation.Validateable{
    String phoneNumbers
    String textMessage
    static constraints = {
        phoneNumbers(nullable: false, size: 11..4000)
        textMessage(nullable: false, size: 3..479)
    }
}
