package com.grailslab.command

/**
 * Created by Aminul
 */
class SignUpCommand implements grails.validation.Validateable{
    String userId
    String mobileNo
    Date birthDate
    String password

    static constraints = {

    }
}
