package com.grailslab.command

import com.grailslab.enums.EmployeeType
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrDesignation
import com.grailslab.hr.HrPeriod

/**
 * Created by Hasnat on 6/27/2015.
 */
class EmployeeCommand implements grails.validation.Validateable{

    Long id
    String empID
    String name
    String fathersName
    String mothersName
    Date birthDate
    Date joiningDate
    String presentAddress
    String emailId
    String bloodGroup
    String cardNo
    Integer deviceId
    String mobile
    String salaryAccounts
    String dpsAccounts
    EmployeeType employeeType
    Date confirmationDate
    String aboutMe
    String fbId

    HrCategory hrCategory
    HrDesignation hrDesignation
    String hrStaffCategory
    Integer sortOrder
    HrPeriod hrPeriod

    static constraints = {

        id nullable: true
        empID unique: true
        mothersName nullable: true
        birthDate nullable: true
        joiningDate nullable: true
        presentAddress nullable: true
        emailId nullable: true, email: true
        bloodGroup nullable: true
        cardNo nullable: true
        deviceId nullable: true
        mobile nullable: true
        salaryAccounts nullable: true
        dpsAccounts nullable: true
        employeeType nullable: true
        confirmationDate nullable: true
        aboutMe nullable: true
        fbId nullable: true

        hrCategory nullable: true
        hrDesignation nullable: true
        hrStaffCategory nullable: true
        hrPeriod nullable: true
    }
}
