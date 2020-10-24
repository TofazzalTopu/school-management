package com.grailslab.open


import com.grailslab.gschoolcore.ActiveStatus

class OpenSliderImage  {
    String imagePath
    Integer sortIndex=1
    String title
    String description
    String type

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
    }
    static mapping = {
        sort sortIndex: "asc" // or "desc"
    }
}
