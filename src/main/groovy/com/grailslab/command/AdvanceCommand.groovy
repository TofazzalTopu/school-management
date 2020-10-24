package com.grailslab.command

import com.grailslab.enums.PaymentType
import com.grailslab.hr.Employee

/**
 * Created by Hasnat on 6/27/2015.
 */
class AdvanceCommand implements grails.validation.Validateable{
    Long id
    Employee employee
    Double amount
    Double monthlyPay
    Double paidAmount
    PaymentType paymentType
    Integer numOfInstallment
    Date loanDate
    static constraints = {
        id nullable: true
        monthlyPay nullable: true
        numOfInstallment nullable: true
    }
}
class PcCommand implements grails.validation.Validateable{
    Long id
    Employee employee
    Double amount
    Double monthlyPay
    Double paidAmount
    PaymentType paymentType
    Integer numOfInstallment
    Date pcDate
    static constraints = {
        id nullable: true
        numOfInstallment nullable: true
        monthlyPay nullable: true
    }
}
