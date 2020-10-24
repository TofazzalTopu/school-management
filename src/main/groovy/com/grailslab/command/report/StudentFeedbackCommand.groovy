package com.grailslab.command.report
/**
 * Created by Aminul on 2/24/2016.
 */
class StudentFeedbackCommand implements grails.validation.Validateable{
    Long stdClassName
    Long stdSectionName
    Long stdWeekNo
    Long stdRating
    String stdSortedBy
    String stdPrintType


    static constraints = {
        stdWeekNo nullable: true
        stdPrintType nullable: true
    }
}
