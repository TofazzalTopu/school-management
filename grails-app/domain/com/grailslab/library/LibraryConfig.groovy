package com.grailslab.library


import com.grailslab.gschoolcore.ActiveStatus

class LibraryConfig {
    String memberType
    Integer numberOfBook
    Integer allowedDays
    Double fineAmount
    Double memberFee
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

         fineAmount nullable: true
         memberFee nullable: true
    }
}
