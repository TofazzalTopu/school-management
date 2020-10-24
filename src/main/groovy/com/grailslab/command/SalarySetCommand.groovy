package com.grailslab.command

import com.grailslab.enums.EmployeeType
import com.grailslab.enums.PaymentType
import com.grailslab.enums.PrintOptionType
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory

/**
 * Created by Hasnat on 6/27/2015.
 */
class SalarySetCommand implements grails.validation.Validateable{
    Long id
    Employee employee
    EmployeeType employeeType=EmployeeType.FULL_TIME
    Double basic
    Double gross
    Double houseAllowance
    Double medicalAllowance
    Boolean inCharge
    Double mobileAllowance
    Double otherAllowance
    PaymentType paymentType
    Date lastIncrement
    Double incrementAmount
    Double pfTotal
    Boolean isFineEffective=Boolean.FALSE

    static constraints = {
        id nullable: true
        lastIncrement nullable: true
        incrementAmount nullable: true
        inCharge nullable: true
    }
}
