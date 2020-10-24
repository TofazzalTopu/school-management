package com.grailslab.settings


import com.grailslab.enums.ExamTerm
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Lesson  {
    Integer weekNumber
    Date weekStartDate
    Date weekEndDate
    ExamTerm examTerm
    Section section
    ClassName className
    Date lessonDate
//    static hasMany = [lessonDetails:LessonDetails]
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
        section(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        examTerm nullable: true
        className nullable: true
    }
}
