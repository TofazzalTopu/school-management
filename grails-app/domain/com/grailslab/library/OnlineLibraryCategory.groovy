package com.grailslab.library

import com.grailslab.gschoolcore.ActiveStatus

class OnlineLibraryCategory {
    String categoryName
    String description
    long id
    Long sortOrder
    ActiveStatus activeStatus = ActiveStatus.ACTIVE

    static constraints = {
        sortOrder nullable: true
    }
}
