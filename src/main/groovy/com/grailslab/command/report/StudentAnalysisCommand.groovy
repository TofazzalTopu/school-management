package com.grailslab.command.report

import com.grailslab.enums.ExamType
import com.grailslab.enums.GroupName
import com.grailslab.enums.PrintOptionType
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam
import com.grailslab.settings.SubjectName

/**
 * Created by Aminul on 7/10/2017.
 */
class StudentAnalysisCommand implements grails.validation.Validateable{
    Shift shift
    AcademicYear academicYear
    ClassName className
    Section section
    GroupName groupName
    String analysisReportType
    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
        groupName nullable: true
        shift nullable: true
        section nullable: true
    }
}
