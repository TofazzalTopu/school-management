package com.grailslab.command.report

import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.ShiftExam
import com.grailslab.settings.SubjectName

/**
 * Created by Aminul on 7/10/2017.
 */
class ResultAnalysisCommand implements grails.validation.Validateable{
    Shift shift
    AcademicYear academicYear
    ShiftExam examName
    ExamType examType
    ClassName className
    GroupName groupName
    Exam examId
    SubjectName subjectName
    String analysisReportType
    PrintOptionType printOptionType
    static constraints = {
        className nullable: true
        groupName nullable: true
        examId nullable: true
        subjectName nullable: true
        shift nullable: true
    }
}
