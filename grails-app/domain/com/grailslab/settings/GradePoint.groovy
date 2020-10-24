package com.grailslab.settings


import com.grailslab.enums.LetterGrade
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class GradePoint {
    Double upToMark    //49
    Double fromMark    //0
    Double gPoint       //1
    LetterGrade lGrade       //F
    String laterGrade       //dubplicate
    String credentials  // Unsuccessful
    ClassName className

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        activeStatus(nullable: true)

        className nullable: true
        laterGrade nullable: true
    }
}
