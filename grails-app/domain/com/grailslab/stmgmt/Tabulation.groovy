package com.grailslab.stmgmt


import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ResultStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam

class Tabulation {

    ClassName className
    Section section
    Exam exam
    ExamTerm examTerm
    Student student

    Double subject0Mark =0
    Double subject1Mark =0
    Double subject2Mark =0
    Double subject3Mark =0
    Double subject4Mark =0
    Double subject5Mark =0
    Double subject6Mark =0
    Double subject7Mark =0
    Double subject8Mark =0
    Double subject9Mark =0
    Double subject10Mark =0
    Double subject11Mark =0
    Double subject12Mark =0
    Double subject13Mark =0
    Double subject14Mark =0
    Double subject15Mark =0
    Double subject16Mark =0
    Double subject17Mark =0
    Double subject18Mark =0
    Double subject19Mark =0
    Double subject20Mark =0
    Double subject21Mark =0
    Double subject22Mark =0
    Double subject23Mark =0
    Double subject24Mark =0
    Double subject25Mark =0
    Double subject26Mark =0
    Double subject27Mark =0
    Double subject28Mark =0

    //ct exam marks
    Double subject0ct =0
    Double subject1ct =0
    Double subject2ct =0
    Double subject3ct =0
    Double subject4ct =0
    Double subject5ct =0
    Double subject6ct =0
    Double subject7ct =0
    Double subject8ct =0
    Double subject9ct =0
    Double subject10ct =0
    Double subject11ct =0
    Double subject12ct =0
    Double subject13ct =0
    Double subject14ct =0
    Double subject15ct =0
    Double subject16ct =0
    Double subject17ct =0
    Double subject18ct =0
    Double subject19ct =0
    Double subject20ct =0
    Double subject21ct =0
    Double subject22ct =0
    Double subject23ct =0
    Double subject24ct =0
    Double subject25ct =0
    Double subject26ct =0
    Double subject27ct =0
    Double subject28ct =0

    Double totalCtMark =0

    Double totalObtainMark =0       //total obtain mark

    Double term1Mark  =0
    Integer attendanceDay = 0       //Total Attendance Day for every exam term
    Double attendancePercent  =0       //used for final result based on attendance percentage
    Double attendanceMark  =0       //used for final result based on attendance percentage
    Double term2Mark =0
    Double grandTotalMark =0        //total obtain mark + attendance mark
    String specialCareSubjects

    Integer positionInSection=0
    String sectionStrPosition
    Integer positionInClass=0
    String classStrPosition
    Integer failedSubCounter=0
    Double gPoint       //1
    String lGrade       //F
    ResultStatus resultStatus
    Boolean sentMessage
    Date messageDate
    AcademicYear academicYear
    String nextSection
    Integer nextRollNo


    static constraints = {
        nextSection nullable: true
        nextRollNo nullable: true
        attendanceDay nullable: true
        positionInSection nullable: true
        sectionStrPosition nullable: true
        positionInClass nullable: true
        classStrPosition nullable: true
        resultStatus nullable: true
        gPoint nullable: true
        lGrade nullable: true
        attendanceMark nullable: true
        specialCareSubjects nullable: true
        sentMessage nullable: true
        messageDate nullable: true
        subject15Mark nullable: true
        subject16Mark nullable: true
        subject17Mark nullable: true
        subject18Mark nullable: true
        subject19Mark nullable: true
        subject20Mark nullable: true
        subject21Mark nullable: true
        subject22Mark nullable: true
        subject23Mark nullable: true
        subject24Mark nullable: true
        subject25Mark nullable: true
        subject26Mark nullable: true
        subject27Mark nullable: true
        subject28Mark nullable: true
        subject0ct nullable: true
        subject1ct nullable: true
        subject2ct nullable: true
        subject3ct nullable: true
        subject4ct nullable: true
        subject5ct nullable: true
        subject6ct nullable: true
        subject7ct nullable: true
        subject8ct nullable: true
        subject9ct nullable: true
        subject10ct nullable: true
        subject11ct nullable: true
        subject12ct nullable: true
        subject13ct nullable: true
        subject14ct nullable: true
        subject15ct nullable: true
        subject16ct nullable: true
        subject17ct nullable: true
        subject17ct nullable: true
        subject18ct nullable: true
        subject19ct nullable: true
        subject20ct nullable: true
        subject21ct nullable: true
        subject22ct nullable: true
        subject23ct nullable: true
        subject24ct nullable: true
        subject25ct nullable: true
        subject26ct nullable: true
        subject27ct nullable: true
        subject28ct nullable: true

        totalCtMark nullable: true
    }
}
