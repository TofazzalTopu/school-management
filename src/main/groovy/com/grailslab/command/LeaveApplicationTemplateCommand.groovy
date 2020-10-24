package com.grailslab.command

import com.grailslab.enums.LeaveApplyType

class LeaveApplicationTemplateCommand implements grails.validation.Validateable{
    Long id
    LeaveApplyType applyType
    String leaveTemplate


    static constraints = {
        id nullable: true
        leaveTemplate nullable: true

    }
}
