package com.grailslab.command

import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class ByClassStepCommand implements grails.validation.Validateable{
    Student studentId
    String feeItemIds
    Date invoiceDate
    static constraints = {
        invoiceDate nullable: true
    }
}

class OnlineAdmissionStepCommand implements grails.validation.Validateable{
    String feeItemIds
    Section section
    String admitCardNumber
    Integer rollNo
    Date invoiceDate
    static constraints = {
        invoiceDate nullable: true
    }
}

class FormFeeInvoiceCommand implements grails.validation.Validateable{
    ClassName className
    String feeItemIds
    String name
    String mobile
    String fathersName
    Date birthDate
    static constraints = {
        birthDate nullable: true
        fathersName nullable: true
    }
}
