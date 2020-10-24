package com.grailslab.command.report

import com.grailslab.enums.PrintOptionType
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory

/**
 * Created by Aminul on 2/27/2016.
 */
class EmpSalaryCommand implements grails.validation.Validateable{

    Employee employee
    PrintOptionType printOptionType
    static constraints = {
        printOptionType nullable: true
        employee nullable: true
    }
}
