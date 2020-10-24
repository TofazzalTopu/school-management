package com.grailslab.command.report
/**
 * Created by Aminul on 2/24/2016.
 */
class FeedbackByStudentCommand implements grails.validation.Validateable{
    Long studentName
    Long stuSubjectName
    Integer stuWeekNo
    String stuPrintType


    static constraints = {
        stuWeekNo nullable: true
        stuPrintType nullable: true
    }
}
