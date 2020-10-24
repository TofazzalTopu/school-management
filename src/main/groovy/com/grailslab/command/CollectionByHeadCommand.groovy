package com.grailslab.command


import com.grailslab.enums.PrintOptionType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

/**
 * Created by Hasnat on 6/28/2015.
 */
class CollectionByHeadCommand implements grails.validation.Validateable{
    ClassName className
    Date startDate
    Date endDate
    String discount
//    Section section
    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
//        section nullable: true
    }
}
