package com.grailslab.command

import com.grailslab.enums.AccountType
import com.grailslab.enums.FeeAppliedType

/**
 * Created by Hasnat on 6/27/2015.
 */
class ChartOfAccountsCommand implements grails.validation.Validateable{
    Long id
    String name
    Integer code
    Long parentId
    AccountType accountType
    FeeAppliedType appliedType
    Boolean allowSubHead
    Boolean scholarshipHead
//    FeeIterationType feeIterationType
//    ExpenseIterationType expenseIterationType
    static constraints = {
        id nullable: true
        code unique: true
        parentId nullable: true
        appliedType nullable: true
        allowSubHead nullable: true
        scholarshipHead nullable: true
//        expenseIterationType nullable: true
    }
}
