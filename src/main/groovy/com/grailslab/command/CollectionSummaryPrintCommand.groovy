package com.grailslab.command

import com.grailslab.enums.PrintOptionType

/**
 * Created by Hasnat on 6/28/2015.
 */
class CollectionSummaryPrintCommand implements grails.validation.Validateable{
    PrintOptionType printOptionType
    static constraints = {
        printOptionType nullable: true
    }
}
