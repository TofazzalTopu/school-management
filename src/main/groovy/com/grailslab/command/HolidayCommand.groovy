package com.grailslab.command

import com.grailslab.enums.EventType

/**
 * Created by Hasnat on 6/27/2015.
 */
class HolidayCommand implements grails.validation.Validateable{
    Long id
    String name
    Date startDate
    Date endDate
    EventType eventType
    static constraints = {
        id nullable: true
    }
}
