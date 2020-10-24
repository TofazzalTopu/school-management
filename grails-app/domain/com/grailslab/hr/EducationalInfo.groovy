package com.grailslab.hr

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.SubjectName

class EducationalInfo {
    String name
    Employee employee
    HrCertification certification
    Institution institution
    SubjectName majorSubject
    Board board
    Date passingYear
    String result
    String duration

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

        name nullable: true
        board nullable: true
        passingYear nullable: true
    }
}
