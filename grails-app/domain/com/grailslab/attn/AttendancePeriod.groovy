package com.grailslab.attn

import com.grailslab.gschoolcore.ActiveStatus

class AttendancePeriod {
    String attnType
    String attnTypeName    // for student shift & for teacher text field value
    String workingStatus
    String dayName
    String startTime
    Date dummyStartTime      //dummy date with calculated start time
    String endTime
    Date dummyEndTime        //dummy date with calculated start time
    Integer lateTolerance    // 15

    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    ActiveStatus activeStatus = ActiveStatus.ACTIVE

    static constraints = {
        dummyStartTime nullable: true
        dummyEndTime nullable: true
        lateTolerance nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
        createdBy nullable: true
        updatedBy nullable: true
    }

    def beforeInsert() {
        dateCreated = new Date()
    }

    def beforeUpdate(){
        lastUpdated = new Date()
    }
}
