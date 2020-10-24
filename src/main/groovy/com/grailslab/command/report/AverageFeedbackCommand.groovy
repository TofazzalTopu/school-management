package com.grailslab.command.report
/**
 * Created by Aminul on 2/24/2016.
 */
class AverageFeedbackCommand implements grails.validation.Validateable{
    Long avgClassName
    Long avgSectionName
    Long avgWeekNo
    String avgPrintType


    static constraints = {
        avgWeekNo nullable: true
        avgPrintType nullable: true
    }
}
