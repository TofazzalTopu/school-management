package com.grailslab.library

import com.grailslab.gschoolcore.ActiveStatus

class OnlineLibrary {
    String bookName
    String description
    String link
    Long categoryId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE

    static constraints = {
        description nullable: true
        categoryId nullable: true
    }
}
