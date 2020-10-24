package com.grailslab.command

class AttendancePeriodCommand implements grails.validation.Validateable{
    Long id
    String attnType
    String attnTypeName    // for student shift & for teacher text field value
    String workingStatus
    String dayName
    String startTime
    String endTime
    Integer lateTolerance    // 15

    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy

    static constraints = {
        id nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
        createdBy nullable: true
        updatedBy nullable: true
    }
}
