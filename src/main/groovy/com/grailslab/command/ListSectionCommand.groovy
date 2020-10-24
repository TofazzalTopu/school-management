package com.grailslab.command

import com.grailslab.enums.GroupName
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.EvaluationSchedule
import com.grailslab.settings.ShiftExam

/**
 * Created by aminul on 3/19/2015.
 */
class ListSectionCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    Shift shift
    ClassName className
    GroupName groupName
    static constraints = {
        academicYear nullable: true
        className nullable: true
        groupName nullable: true
        shift nullable: true
    }
}

class ListExamClassCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    ShiftExam examName
    EvaluationSchedule scheduleName
    ClassName className
    GroupName groupName
    String examType
    static constraints = {
        academicYear nullable: true
        examName nullable: true
        scheduleName nullable: true
        className nullable: true
        examType nullable: true
        groupName nullable: true
    }
}
