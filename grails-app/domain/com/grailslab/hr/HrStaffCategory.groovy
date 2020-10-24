package com.grailslab.hr


import com.grailslab.gschoolcore.ActiveStatus

class HrStaffCategory {

    String keyName
    String name
    String description
    Integer sortOrder

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

        keyName unique: true
    }
    static mapping = {
        description type: 'text'
    }
}
