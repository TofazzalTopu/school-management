package com.grailslab.settings


import com.grailslab.enums.ExamStatus
import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ScheduleStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Exam  {
    ShiftExam shiftExam
    ExamTerm examTerm       //duplicate for usability
    Section section
    ClassName className
    String name
    String subjectIds
    String ctSubjectIds
    Integer numberOfSubject =0
    Double totalExamMark = 0
    ScheduleStatus hallSchedule = ScheduleStatus.NO_SCHEDULE
    ExamStatus examStatus = ExamStatus.NEW
    ExamStatus ctExamStatus = ExamStatus.NEW
    ExamStatus ct2ExamStatus = ExamStatus.NEW
    ExamStatus ct3ExamStatus = ExamStatus.NEW
    Integer studentCount = 0
    Integer attended = 0
    Integer failOn1Subject = 0
    Integer failOn2Subject = 0
    Integer failOnMoreSubject = 0
    Integer scoreAPlus = 0
    Integer scoreA = 0
    Integer scoreAMinus = 0
    Integer scoreB = 0
    Integer scoreC = 0
    Integer scoreF = 0

    Integer isLocked = 0

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
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        subjectIds(nullable: true)
        ctSubjectIds(nullable: true)
        scoreF nullable: true
    }

    String toString() {
        return this.name
    }

}
