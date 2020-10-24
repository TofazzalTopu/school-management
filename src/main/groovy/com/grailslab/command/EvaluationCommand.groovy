package com.grailslab.command

import com.grailslab.stmgmt.Student

class EvaluationCommand implements grails.validation.Validateable{
    Student student
    Long classNameId
    Long sectionId
    Long scheduleId         //EvaluationScheduleId
    String socFriendly
    String socSharing
    String socTolerance
    String socLanguageUsages
    String phyIncrease
    String phyWellness
    String phyEatInterest
    String menAttention
    String menUnrest
    String menAccuracy
    String eduMotion
    String eduInterest
    String eduHomeWork
    String eduClassWork
    String totBangla
    String totEnglish
    String totMathematics
    String totEnvEcology
    String totCleanness
    String comment

    static constraints = {
        comment nullable: true
        student nullable: true
    }
}
