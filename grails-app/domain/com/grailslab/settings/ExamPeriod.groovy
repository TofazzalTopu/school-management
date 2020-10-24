package com.grailslab.settings


import com.grailslab.enums.ExamType
import com.grailslab.gschoolcore.ActiveStatus

class ExamPeriod {
    ExamType examType
    String name
    String startOn      //10 AM
    Integer duration    // 40
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
    }
}
