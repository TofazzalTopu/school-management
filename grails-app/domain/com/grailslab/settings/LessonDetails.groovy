package com.grailslab.settings

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class LessonDetails {
    Employee employee
    String topics
    String homeWork
    Date examDate
    SubjectName subject
    Lesson lesson

    Boolean oralHomeWork = false
    Boolean writtenHomeWork = false

//    static belongsTo = [lesson:Lesson]
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

        examDate nullable: true
        homeWork nullable: true, maxSize: 2000
    }
    static mapping = {
        topics type: 'text'
    }

}
