package com.grailslab.command

/**
 * Created by aminul on 5/31/2015.
 */
class LessonWeekCommand implements grails.validation.Validateable{
    Long id
    Integer weekNumber
    Date startDate
    Date endDate
    static constraints = {
        id nullable: true
    }
}
