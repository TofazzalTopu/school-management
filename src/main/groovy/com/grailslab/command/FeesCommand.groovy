package com.grailslab.command

import com.grailslab.accounting.ChartOfAccount
import com.grailslab.enums.DueOnType
import com.grailslab.enums.FeeAppliedType
import com.grailslab.enums.FeeIterationType
import com.grailslab.enums.FeeType
import com.grailslab.settings.ClassName

/**
 * Created by Hasnat on 6/27/2015.
 */
class FeesCommand implements grails.validation.Validateable{
    Long id
    Double amount
    Double discount

    //enums
    FeeType feeType                     //compulsory, optional
    DueOnType dueOnType                 //SalaryAdvance, due, on delivery
    FeeIterationType iterationType                 //SalaryAdvance, due, on delivery
    FeeAppliedType feeAppliedType

    ChartOfAccount account
    ClassName className

    Boolean quickFee1 = Boolean.FALSE
    Boolean quickFee2 = Boolean.FALSE
    Boolean quickFee3 = Boolean.FALSE  // Online fee

    static constraints = {
        id nullable: true
        discount nullable: true
        className nullable: true
        quickFee1 nullable: true
        quickFee2 nullable: true
        quickFee3 nullable: true
    }
}
