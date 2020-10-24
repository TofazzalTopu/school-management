package com.grailslab.command

import com.grailslab.hr.HrCategory

/**
 * Created by Hasnat on 6/27/2015.
 */
class DesignationCommand implements grails.validation.Validateable{
    Long id
    String name
    HrCategory hrCategory
    Integer sortOrder

    static constraints = {
        id nullable: true
    }
}
