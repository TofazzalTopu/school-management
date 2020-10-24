package com.grailslab.accounting


import com.grailslab.enums.AccountType
import com.grailslab.enums.FeeAppliedType
import com.grailslab.enums.NodeType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class ChartOfAccount {
    String name
    Integer code
    Long parentId
    NodeType nodeType
    Boolean hasChild = false
    AccountType accountType
    FeeAppliedType appliedType
    Boolean allowEdit = true
    Boolean allowChild = true
    Boolean displayHead = false
    Boolean scholarshipHead = false

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

        parentId nullable: true
        appliedType nullable: true
        displayHead nullable: true
        scholarshipHead nullable: true
    }
}
