package com.grailslab.command

/**
 * Created by Hasnat on 6/27/2015.
 */
class ManageUserCommand implements grails.validation.Validateable{
    Long id
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static constraints = {

    }
}
