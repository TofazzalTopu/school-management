package com.grailslab.stmgmt

import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ResultStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.Section

class TabulationCt {
    ClassName className
    Section section
    Exam exam
    ExamTerm examTerm
    Student student
    Integer ctExamNo = 1

    Double subject0Mark = 0
    Double subject1Mark = 0
    Double subject2Mark = 0
    Double subject3Mark = 0
    Double subject4Mark = 0
    Double subject5Mark = 0
    Double subject6Mark = 0
    Double subject7Mark = 0
    Double subject8Mark = 0
    Double subject9Mark = 0
    Double subject10Mark = 0
    Double subject11Mark = 0
    Double subject12Mark = 0
    Double subject13Mark = 0
    Double subject14Mark = 0
    Double subject15Mark = 0
    Double subject16Mark = 0
    Double subject17Mark = 0
    Double subject18Mark = 0
    Double subject19Mark = 0
    Double subject20Mark = 0
    Double subject21Mark = 0
    Double subject22Mark = 0
    Double subject23Mark = 0
    Double subject24Mark = 0
    Double subject25Mark = 0
    Double subject26Mark = 0
    Double subject27Mark = 0
    Double subject28Mark = 0

    Double totalObtainMark =0

    Integer positionInSection= 0
    String sectionStrPosition
    Integer positionInClass= 0
    String classStrPosition
    Integer failedSubCounter= 0
    Double gPoint       //1
    String lGrade       //F
    ResultStatus resultStatus

    static constraints = {
        sectionStrPosition nullable: true
        classStrPosition nullable: true
        gPoint nullable: true
        lGrade nullable: true
        resultStatus nullable: true
    }
}
