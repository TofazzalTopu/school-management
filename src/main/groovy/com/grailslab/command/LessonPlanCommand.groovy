package com.grailslab.command

import com.grailslab.enums.PrintOptionType
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName

/**
 * Created by Hasnat on 6/28/2015.
 */
class LessonPlanCommand implements grails.validation.Validateable{
    ClassName className
    Section section
    SubjectName subject
    Integer weekNo
    PrintOptionType printOptionType
    Date startDate
    Date endDate
    static constraints = {
        endDate nullable: true
        startDate nullable: true
        className nullable: true
        section nullable: true
        subject nullable: true
        weekNo nullable: true
        printOptionType nullable: true
    }
}
