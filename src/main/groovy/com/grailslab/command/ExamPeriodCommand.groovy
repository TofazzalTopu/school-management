package com.grailslab.command

import com.grailslab.enums.ExamType

//ExamPeriodController - save
class ExamPeriodCommand implements grails.validation.Validateable{
    Long id
    ExamType examType
    String startOn      //10 AM
    Integer duration    // 40

    static constraints = {
        id nullable: true
    }
}
