package com.grailslab.command

import com.grailslab.enums.GroupName
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassRoom

/**
 * Created by aminul on 3/19/2015.
 */
class SectionCommand implements grails.validation.Validateable{
    Long id
    String name
    Shift shift
    GroupName groupName
    ClassRoom classRoom
    ClassName className
    AcademicYear academicYear
    Employee employee
    Integer sortOrder
    static constraints = {
        id nullable: true
        name nullable: false
        classRoom nullable: true
        groupName nullable: true
        employee nullable: true
        academicYear nullable: true
    }
}
