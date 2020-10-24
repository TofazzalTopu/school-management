package com.grailslab.command

import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

import com.grailslab.stmgmt.RegOnlineRegistration
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class ByClassCommand implements grails.validation.Validateable{
    String feeId
    Student student
}

class FormFeeCommand implements grails.validation.Validateable{
    String feeId
    ClassName cName
}

class OnlineAdmissionCommand implements grails.validation.Validateable{
    String feeId
    RegOnlineRegistration selectedApplicant
    Section section
}
