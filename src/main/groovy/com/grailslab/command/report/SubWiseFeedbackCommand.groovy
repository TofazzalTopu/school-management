package com.grailslab.command.report

import com.grailslab.settings.ClassName
import com.grailslab.settings.LessonWeek
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName

/**
 * Created by Aminul on 2/24/2016.
 */
class SubWiseFeedbackCommand implements grails.validation.Validateable{
    Long className
    Long sectionName
    Long subjectName
    Long weekNo
    String sortedBy
    String feedbackPrintType


    static constraints = {
        weekNo nullable: true
        feedbackPrintType nullable: true
    }
}
