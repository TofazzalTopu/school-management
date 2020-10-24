package com.grailslab.command

import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegForm

/**
 * Created by Hasnat on 6/28/2015.
 */
class OnlineStep1Command implements grails.validation.Validateable{
    RegForm regForm
    ClassName className
    String name
    String fathersName
    String mobile
    Date birthDate

    static constraints = {
    }
}
