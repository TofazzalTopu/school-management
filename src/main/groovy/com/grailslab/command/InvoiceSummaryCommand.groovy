package com.grailslab.command

import com.grailslab.enums.DateRangeType
import com.grailslab.enums.PrintOptionType
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

/**
 * Created by Hasnat on 6/28/2015.
 */
class InvoiceSummaryCommand implements grails.validation.Validateable{
    DateRangeType pDateRange
    Date startDate
    Date endDate
    ClassName pClassName
    Section section
    PrintOptionType printOptionType
    String reportType
    String reportSortType
    String discount

    static constraints = {
        pClassName nullable: true
        section nullable: true
        reportType nullable: true
        reportSortType nullable: true
        discount nullable: true
        pDateRange nullable: true
        startDate nullable: true
        endDate nullable: true
    }
}
