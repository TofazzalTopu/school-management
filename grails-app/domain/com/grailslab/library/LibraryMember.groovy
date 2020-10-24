package com.grailslab.library

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class LibraryMember {
    String name
    String address
    String presentAddress
    String memberId
    Date memberShipDate
    String mobile
    String email
    String referenceId
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

        name nullable: true
        address nullable: true
        presentAddress nullable: true
        memberId nullable: true
        memberShipDate nullable: true
        mobile nullable: true
        email nullable: true
        referenceId nullable: true
    }
}
