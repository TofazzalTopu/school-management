package com.grailslab.accounting


import com.grailslab.enums.FeeItemType
import com.grailslab.enums.FeeType
import com.grailslab.enums.FeeAppliedType
import com.grailslab.enums.DueOnType
import com.grailslab.enums.FeeIterationType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName

class FeeItems  {
    ChartOfAccount chartOfAccount
    Fees fees
    String name
    String code
    Double amount =0
    Double discount =0     //in percentage
    Double netPayable =0

    FeeItemType feeItemType
    ClassName className
    FeeType feeType                     //compulsory, optional
    FeeAppliedType feeAppliedType       //all, class, section
    DueOnType dueOnType                 //SalaryAdvance, due, on delivery
    FeeIterationType iterationType      //On Receive, daily, weekly, monthly, yearly

    static hasMany = [itemsDetail: ItemsDetail]


    Boolean quickFee1=Boolean.FALSE
    Boolean quickFee2=Boolean.FALSE
    Boolean quickFee3=Boolean.FALSE  //Online fee

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        fees nullable: true
        className nullable: true
        quickFee3 nullable: true

        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)
    }
}
