package com.grailslab.library


import com.grailslab.gschoolcore.ActiveStatus

class BookDetails {
    BookCategory category
    String name
    String title
    Author author
    BookPublisher bookPublisher
    String addAuthor
    String language
    Integer lostDamage = 0
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

        title nullable: true
        addAuthor nullable: true
        author nullable: true
        lostDamage nullable: true
        bookPublisher nullable: true

    }
}
