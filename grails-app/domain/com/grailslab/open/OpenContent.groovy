package com.grailslab.open

import com.grailslab.enums.OpenContentType
import com.grailslab.gschoolcore.ActiveStatus

class OpenContent {
    OpenContentType openContentType
    String title
    String iconClass
    String description
    Integer sortOrder=1
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

        description nullable: true
        iconClass nullable: true
    }

    static mapping = {
        description type: 'text'
    }
}
