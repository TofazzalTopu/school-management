package com.grailslab.settings

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class EvaluationSchedule {

    String scheduleName
    String classIds
    String sectionIds
    Date periodStart
    Date periodEnd
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

        classIds nullable: true
        sectionIds nullable: true
        periodStart nullable: true
        periodEnd nullable: true
    }

    String toString(){
        return scheduleName
    }
}
