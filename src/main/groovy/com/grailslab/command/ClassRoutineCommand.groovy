package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassPeriod
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName

/**
 * Created by USER on 12-Jul-17.
 */
class ClassRoutineCommand implements grails.validation.Validateable{
    Long id
    ClassName className
    Section section
    SubjectName subjectName
    String startOn      //10 AM
    String classPeriod      //10 AM
    Integer duration    // 40
    Employee employee
    String days
    AcademicYear academicYear


    static constraints = {
        id nullable: true
        subjectName nullable: true
        employee nullable: true
    }
}
