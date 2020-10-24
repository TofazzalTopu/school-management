package com.grailslab.command

class SalConfigurationCommand implements grails.validation.Validateable{
    Double  extraClassRate
    Integer lateFineForDays
    Double  pfContribution
    String  pfCalField


    static constraints = {
    }
}
