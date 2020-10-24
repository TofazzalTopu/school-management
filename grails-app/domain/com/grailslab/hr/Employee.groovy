package com.grailslab.hr


import com.grailslab.enums.EmployeeType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Employee {
//    new fieldsmpl
    HrCategory hrCategory
    HrDesignation hrDesignation
    String hrStaffCategory
    Integer sortOrder=1
    HrPeriod hrPeriod

    String empID
    String name
    String barcode
    String fathersName
    String mothersName
    Date birthDate
    Date joiningDate
    Date confirmationDate
    String presentAddress
    String permanentAddress
    String emailId
    String bloodGroup
    String cardNo
    String mobile
    String fbId
    String nationalId
    String aboutMe
    String imagePath
    String salaryAccounts
    String dpsAccounts
    EmployeeType employeeType
    Boolean addedAsUser = false
    Integer deviceId

    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        activeStatus(nullable: true)

        deviceId nullable: true
        empID unique: true
        barcode unique: true, nullable: true
        mothersName nullable: true
        birthDate nullable: true
        joiningDate nullable: true
        confirmationDate nullable: true
        presentAddress nullable: true
        permanentAddress nullable: true
        emailId nullable: true, email: true
        bloodGroup nullable: true
        cardNo nullable: true
        mobile nullable: true
        addedAsUser nullable: true
        fbId nullable: true
        nationalId nullable: true
        aboutMe nullable: true
        imagePath nullable: true
        salaryAccounts nullable: true
        dpsAccounts nullable: true
        employeeType nullable: true

        hrCategory nullable: true
        hrDesignation nullable: true
        hrStaffCategory nullable: true
        sortOrder nullable: true
        hrPeriod nullable: true
    }
    static mapping = {
        aboutMe type: 'text'
    }
}
