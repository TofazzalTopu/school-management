package com.grailslab.settings

import com.grailslab.enums.GroupName
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class ExamSchedule {
    SubjectName subject
    ClassRoom examRoom
    Exam exam
    Double highestMark =0
    Double averageMark =0

    Boolean isCtExam

    Boolean isHallExam
    Date hallExamDate
    ExamPeriod hallPeriod
    Boolean isHallMarkInput=Boolean.FALSE

    Long parentSubId
    Boolean isExtracurricular=Boolean.FALSE
    Boolean isOtherActivity
    Boolean isCompulsory
    Double fullMark
    SubjectType subjectType
    GroupName groupName

    Integer tabulationEffPercentage //weightOnResult
    Integer passMark
    Integer sortOrder


    Double ctExamMark               //ctMark
    Double ctExamMark2               //ctMark2
    Double ctExamMark3               //ctMark3
    Integer ctMarkEffPercentage     //ctEffMark

    Double hallExamMark             //hallMark
    Integer hallMarkEffPercentage   //hallEffMark

    Boolean isHallPractical=Boolean.FALSE
    Integer hallWrittenMark
    Integer hallPracticalMark
    Integer hallObjectiveMark
    Integer hallSbaMark
    Integer hallInput5

    //new adjustments
    Boolean isPassSeparately
    Integer writtenPassMark
    Integer practicalPassMark
    Integer objectivePassMark
    Integer sbaPassMark
    Integer input5PassMark

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

        groupName(nullable: true)
        subjectType(nullable: true)
        isCtExam(nullable: true)
        isHallExam(nullable: true)
        isOtherActivity(nullable: true)
        ctExamMark(nullable: true)
        ctExamMark2(nullable: true)
        ctExamMark3(nullable: true)
        hallExamDate(nullable: true)
        hallPeriod(nullable: true)
        hallExamMark(nullable: true)
        fullMark(nullable: true)
        examRoom(nullable: true)
        ctMarkEffPercentage(nullable: true)
        hallMarkEffPercentage(nullable: true)
        tabulationEffPercentage(nullable: true)
        isCompulsory(nullable: true)
        isHallPractical(nullable: true)
        averageMark(nullable: true)

        //newly added
        sortOrder nullable: true
        passMark nullable: true
        parentSubId nullable: true
        isHallPractical nullable: true
        hallWrittenMark nullable: true
        hallPracticalMark nullable: true
        hallObjectiveMark nullable: true
        hallSbaMark nullable: true
        hallInput5 nullable: true

        isPassSeparately nullable: true
        writtenPassMark nullable: true
        practicalPassMark nullable: true
        objectivePassMark nullable: true
        sbaPassMark nullable: true
        input5PassMark nullable: true
    }

}
