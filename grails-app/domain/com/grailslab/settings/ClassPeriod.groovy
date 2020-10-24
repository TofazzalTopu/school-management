package com.grailslab.settings

import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class ClassPeriod {
    String name
    Integer sortPosition      //1, 2 3 ... must be unique
    String startOn      //10 AM
    Integer duration    // 40
    Shift shift
    Boolean isPlayTime = false

    //calculated fields
    Date startTime
    String period       // 10 AM - 10.40 AM
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
    }
}
