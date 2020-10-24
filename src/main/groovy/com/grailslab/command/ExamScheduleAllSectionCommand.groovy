package com.grailslab.command

import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ExamType
import com.grailslab.enums.Shift

/**
 * Created by Hasnat on 6/28/2015.
 */
class ExamScheduleAllSectionCommand implements grails.validation.Validateable{
    Shift shift
    ExamTerm examTerm
    ExamType examType
    String sections
    static constraints = {
    }
}
