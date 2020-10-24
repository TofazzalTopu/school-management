package com.grailslab.command

import com.grailslab.enums.ExamTerm
import com.grailslab.hr.Employee
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName

/**
 * Created by aminul on 5/31/2015.
 */
class LessonCommand implements grails.validation.Validateable{
    ExamTerm examTerm
    ClassName className
    Section section
    Date lessonDate
    Employee employee
    String topics
    String homeWork
    Date examDate
    SubjectName subject
    boolean oralHomeWork
    boolean writtenHomeWork
    static constraints = {
        examDate nullable: true
        homeWork nullable: true
        section nullable: true
        lessonDate nullable: true
    }
}
