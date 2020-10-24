package com.grailslab.settings

import com.grailslab.gschoolcore.ActiveStatus


class AcaYear {
    String name
    String yearType
    Boolean isAdmissionYear
    Boolean isWorkingYear
    ActiveStatus activeStatus
    Integer sortOrder

    static constraints = {
        sortOrder(nullable: true)
    }
}
