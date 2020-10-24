package com.grailslab.command

import com.grailslab.accounting.ChartOfAccount

/**
 * Created by Hasnat on 6/27/2015.
 */
class AssetsCommand implements grails.validation.Validateable{

    Long id
    ChartOfAccount account
    String name
    Long invoiceId
    Long referenceId
    Date buyDate
    Double cost
    String comment
    Double depreciation
    Date lastDepreciation
    Double currentValue

    static constraints = {
        id nullable: true
        invoiceId nullable: true
        referenceId nullable: true
        buyDate nullable: true
        cost nullable: true
        comment nullable: true
        depreciation nullable: true
        lastDepreciation nullable: true
        currentValue nullable: true
    }
}
