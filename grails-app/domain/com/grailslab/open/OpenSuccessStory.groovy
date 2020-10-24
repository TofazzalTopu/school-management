package com.grailslab.open

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class OpenSuccessStory {
    def springSecurityService
    static transients = ['springSecurityService']

    String imagePath
    String name
    String description
    Integer sortOrder=1

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
        description nullable: true
    }
    static mapping = {
        sort sortOrder: "asc" // or "desc"
    }
    def beforeInsert() {
        def loggedUser =springSecurityService?.principal
        if (!createdBy) {
            createdBy = loggedUser?.username?:'system'
        }
        if(!academicYear){
//               academicYear=AcademicYear.Y2014
            academicYear=loggedUser?.academicYear
        }
    }
    def beforeUpdate(){
        def loggedUser =springSecurityService?.principal
        if (!updatedBy) {
            updatedBy = loggedUser?.username?:'system'
        }
    }
}
