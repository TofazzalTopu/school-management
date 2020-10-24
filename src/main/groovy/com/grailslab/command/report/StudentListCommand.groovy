package com.grailslab.command.report

import com.grailslab.enums.Gender
import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.Religion
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

/**
 * Created by aminul on 3/19/2015.
 */
class StudentListCommand implements grails.validation.Validateable{
        ClassName className
        Section section
        AcademicYear academicYear
        StudentStatus studentStatus
        PrintOptionType printOptionType
        static constraints = {
            className nullable: true
            section nullable: true
            academicYear nullable: true
            studentStatus nullable: true
            printOptionType nullable: true
        }
    }

class StudentDynamicListCommand implements grails.validation.Validateable{
    ClassName className
    Section section
    AcademicYear academicYear
    Gender gender
    Religion religion
    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
        section nullable: true
        academicYear nullable: true
        gender nullable: true
        religion nullable: true
        printOptionType nullable: true
    }
}

class StudentContactListCommand implements grails.validation.Validateable{
    ClassName className
    Section section
    AcademicYear academicYear
    String reportType
    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
        section nullable: true
        academicYear nullable: true
        reportType nullable: true
        printOptionType nullable: true
    }
}
class StudentBirthDayListCommand implements grails.validation.Validateable{
    ClassName className
    Section section
    AcademicYear academicYear
    String startDate
    String endDate

    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
        section nullable: true
        academicYear nullable: true
        printOptionType nullable: true
        startDate nullable: true
        endDate nullable: true
    }
}