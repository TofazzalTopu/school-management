package com.grailslab.command

import com.grailslab.attn.AttnRecordDay
import com.grailslab.enums.DateRangeType

import com.grailslab.stmgmt.Student

/**
 * Created by Aminul on 2/24/2016.
 */
class AttnStudentCommand implements grails.validation.Validateable{
    Long id
    Date recordDate
    Long studentId
    String inTime
    String outTime
    String reason
    static constraints = {
        id nullable: true
        inTime nullable: true
        outTime nullable: true
        reason  nullable: true
    }
}
