package com.grailslab.command

import com.grailslab.enums.ExpenseHead

/**
 * Created by Hasnat on 6/27/2015.
 */
class ExpenseItemCommand implements grails.validation.Validateable{
    Long id
    String name
//    ExpenseHead expenseHead
    String description
    static constraints = {
        id nullable: true
        description nullable: true
    }
}
