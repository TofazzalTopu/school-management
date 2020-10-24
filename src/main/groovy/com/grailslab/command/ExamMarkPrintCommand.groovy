package com.grailslab.command

import com.grailslab.enums.PrintOptionType
import com.grailslab.settings.ExamSchedule

/**
 * Created by Hasnat on 6/28/2015.
 */
class ExamMarkPrintCommand implements grails.validation.Validateable{
    ExamSchedule examSchedule
    PrintOptionType printOptionType
    String inputType
    static constraints = {
        inputType nullable: true
    }
}
