package com.grailslab.stmgmt

import com.grailslab.enums.AttendStatus
import com.grailslab.enums.ResultStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.SubjectName

class ExamMark {
    Exam exam
    Student student
    SubjectName subject
    ExamSchedule examSchedule
    Double ctMark = 0
    Double ctObtainMark = 0
    Double ct2ObtainMark = 0
    Double ct3ObtainMark = 0


    Double hallMark = 0
    Double hallObtainMark = 0
    Double fullMark = 0
    Double hallWrittenMark = 0      //hallInput1
    Double hallObjectiveMark = 0    //hallInput2
    Double hallPracticalMark = 0    //hallInput3
    Double hallSbaMark = 0          //hallInput4
    Double hallInput5 = 0          //hallInput4
    Double tabulationMark = 0    // mark that count on result
    Double tabulationNosbaMark = 0    // mark that count on result
    Boolean isOptional = false
    Boolean isExtraCurricular = false
    Integer idxOfSub       // Subject index in tabulation. Need for calculate Group subject results
    Double gPoint       //1
    String lGrade       //F
    ResultStatus resultStatus
    ResultStatus ctResultStatus
    ResultStatus ct2resultStatus
    ResultStatus ct3resultStatus
    AttendStatus ctAttendStatus= AttendStatus.NO_INPUT
    AttendStatus ct2Status= AttendStatus.NO_INPUT
    AttendStatus ct3Status= AttendStatus.NO_INPUT
    AttendStatus hallAttendStatus= AttendStatus.NO_INPUT

    //average mark for final tarm
    Double halfYearlyMark = 0
    Double averageMark = 0

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)

        gPoint nullable: true
        lGrade nullable: true
        resultStatus nullable: true
        idxOfSub nullable: true
        ctResultStatus nullable: true
        ct2resultStatus nullable: true
        ct3resultStatus nullable: true
    }
}
