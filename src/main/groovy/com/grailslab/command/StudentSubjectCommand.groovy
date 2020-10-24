package com.grailslab.command

import com.grailslab.enums.ExamType
import com.grailslab.enums.Gender
import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.Religion
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName

/**
 * Created by aminul on 3/19/2015.
 */
class StudentSubjectCommand implements grails.validation.Validateable{
    ClassName className
    Section sectionName
    Gender gender
    Religion religion
    SubjectName subjectName
    Boolean isOptional
    Boolean added
    String selectedStdId
    static constraints = {
        className nullable: true
        sectionName nullable: true
        gender nullable: true
        religion nullable: true
        subjectName nullable: true
        isOptional nullable: true
        added nullable: true
        selectedStdId nullable: true
    }
}

class TransferSubjectCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    ClassName className
    Section sectionName
    Gender gender
    Religion religion
    static constraints = {
        sectionName nullable: true
        gender nullable: true
        religion nullable: true
    }
}
class AddTransferSubjectCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    String selectedStdId

    static constraints = {
        academicYear nullable: true
        selectedStdId nullable: true
    }
}

