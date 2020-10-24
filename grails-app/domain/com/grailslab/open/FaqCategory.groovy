package com.grailslab.open

import com.grailslab.enums.OpenCont
import com.grailslab.gschoolcore.ActiveStatus

class FaqCategory {
    String name
    OpenCont openCont
    Integer sortPosition=1
    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static hasMany = [faqQuestion: FaqQuestion]
    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        activeStatus(nullable: true)
    }
}
