package com.grailslab.command

class EvaluationScheduleCommand implements grails.validation.Validateable{
    Long id
    String scheduleName
    String classIds
    Date periodStart
    Date periodEnd

    static constraints = {
        id nullable: true
        classIds nullable: true
        periodStart nullable: true
        periodEnd nullable: true
    }
}