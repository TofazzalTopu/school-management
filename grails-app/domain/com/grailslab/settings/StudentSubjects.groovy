package com.grailslab.settings

import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Student

class StudentSubjects {
    Student student
    SubjectName subject
    SubjectType subjectType
    Boolean isOptional = false
    Long parentSubId     //in case of alternative, parent class subject id

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
        parentSubId nullable: true
    }
}
