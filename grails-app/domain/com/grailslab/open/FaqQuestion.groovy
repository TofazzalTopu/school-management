package com.grailslab.open


import com.grailslab.gschoolcore.ActiveStatus

class FaqQuestion {
//    FAQCategory category
    String name
    String answers
    Integer sortPosition=1
    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static belongsTo = [faqCategory :FaqCategory]

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        activeStatus(nullable: true)
    }
    static mapping = {
        answers type: 'text'
    }
}
