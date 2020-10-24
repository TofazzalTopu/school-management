package com.grailslab.command

import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ExamType
import com.grailslab.enums.Shift
import com.grailslab.settings.Exam

/**
 * Created by Hasnat on 6/28/2015.
 */
class ExamScheduleBySectionCommand implements grails.validation.Validateable{
    Shift shift
    Exam exam
    ExamTerm examTerm
    ExamType examType
    static constraints = {
        shift nullable: true
        exam nullable: true
        examTerm nullable: true
    }
}
