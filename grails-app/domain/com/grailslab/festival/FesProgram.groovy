package com.grailslab.festival

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class FesProgram {
    String name
    Date startDate
    Date endDate
    String olympiadTopics        //comma separated OlympiadType keys
    String helpContact
    Date regOpenDate
    Date regCloseDate

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

        olympiadTopics nullable: true
        helpContact nullable: true
        regOpenDate nullable: true
        regCloseDate nullable: true
    }
}
