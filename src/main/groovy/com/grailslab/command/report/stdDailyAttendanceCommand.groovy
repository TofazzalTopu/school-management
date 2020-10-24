package com.grailslab.command.report

import com.grailslab.enums.PrintOptionType
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student

/**
 * Created by Aminul on 2/27/2016.
 */
class StdDailyAttendanceCommand implements grails.validation.Validateable{
    Date rStartDate
    Date rEndDate
    ClassName className
    Section sectionName
    Student student
    String attndanceStatus
    String stdAttnCategory


    PrintOptionType printOptionType
    static constraints = {
        rEndDate nullable: true
        className nullable: true
        sectionName nullable: true
        attndanceStatus nullable: true
        printOptionType nullable: true
        student nullable:true
        stdAttnCategory nullable: true
    }
}
