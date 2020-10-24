package com.grailslab.hr

import com.grailslab.gschoolcore.ActiveStatus

class HrPeriod {
    String name
    String periodRange
    String startOn      //10 AM
    Integer duration    // 40
    Integer lateTolerance    // 40
    Date startTime      //dummy date with calculated start time

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

        periodRange nullable: true
    }
    static mapping = {
        cache true
    }

}
