package com.grailslab.command

import com.grailslab.enums.MainUserType

/**
 * Created by Hasnat on 6/27/2015.
 */

class UserAccessCommand implements grails.validation.Validateable{
    Long userId
    String objId
    String username
    String mainUserType
    String empRole

    static constraints = {
        userId nullable: true
        objId nullable: true
        username blank: false
    }
}
