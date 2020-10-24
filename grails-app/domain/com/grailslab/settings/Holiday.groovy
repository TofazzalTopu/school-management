package com.grailslab.settings


import com.grailslab.enums.EventType
import com.grailslab.enums.RepeatType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Holiday {

    String name             // weekly or special(eid,puza)
    Date startDate
    Date endDate
    RepeatType repeatType = RepeatType.NEVER
    String repeatDates     // coma separated sun,mon
    EventType eventType
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

        repeatDates(nullable: true)
    }
}
