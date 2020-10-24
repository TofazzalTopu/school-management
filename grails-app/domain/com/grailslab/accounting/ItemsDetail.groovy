package com.grailslab.accounting

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus


class ItemsDetail {
    String name
    Double amount =0
    Double discount =0     //in percentage
    Double netPayable =0
    Integer sortPosition
    Integer continuityOrder         //included to identify next available month in fee item list for monthly activated item

    Boolean quickFee1=Boolean.FALSE
    Boolean quickFee2=Boolean.FALSE
    Boolean quickFee3=Boolean.FALSE      // online fee

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static belongsTo = [feeItems:FeeItems]

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        sortPosition nullable: true
        continuityOrder nullable: true
        quickFee3 nullable: true
    }
    static mapping = {
        sort continuityOrder: "asc"
    }
}
