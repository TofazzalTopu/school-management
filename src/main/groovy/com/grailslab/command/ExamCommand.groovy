package com.grailslab.command

import com.grailslab.enums.ExamTerm
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear

/**
 * Created by Hasnat on 6/27/2015.
 */
class ExamCommand implements grails.validation.Validateable{
    Long id
    String examName
    Integer ctExam
    ExamTerm examTerm       //old
    Integer weightOnResult
    String classIds
    AcademicYear academicYear
//    Shift shift         //need to remove or nulll

    Date resultPublishOn            //possible result publish date when create exam

    Date periodStart
    Date periodEnd

    static constraints = {
        id nullable: true
        ctExam nullable: true
        weightOnResult nullable: true
        resultPublishOn nullable: true
        periodStart nullable: true
        periodEnd nullable: true
        academicYear nullable: true
    }
}
