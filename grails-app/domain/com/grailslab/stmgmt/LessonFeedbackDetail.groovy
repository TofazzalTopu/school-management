package com.grailslab.stmgmt

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.SubjectName

class LessonFeedbackDetail {

    LessonFeedback feedback
    Student student
    SubjectName subjectName
    Double rating
    String comment
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

        comment nullable: true
    }
}
