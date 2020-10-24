package com.grailslab.command

import com.grailslab.enums.HrKeyType

/**
 * Created by Hasnat on 6/27/2015.
 */
class HrCategoryCommand implements grails.validation.Validateable{
    Long id
    String name
    Integer sortOrder
//    HrKeyType hrKeyType

    static constraints = {
        id nullable: true
    }
}

class HrStaffCategoryCommand implements grails.validation.Validateable{
    Long id
    String keyName
    String name
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
    }
}
