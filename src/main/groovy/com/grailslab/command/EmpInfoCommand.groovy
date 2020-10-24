package com.grailslab.command

/**
 * Created by Hasnat on 6/27/2015.
 */
class EmpInfoCommand implements grails.validation.Validateable{
    Long id
    String name
    String fathersName
    String mothersName
    Date birthDate
    String presentAddress
    String permanentAddress
    String emailId
    String bloodGroup
    String cardNo
    String mobile
    String fbId
    String nationalId
    String aboutMe

    static constraints = {
        id nullable: true
        cardNo nullable: true
        fbId nullable: true
        nationalId nullable: true
        aboutMe nullable: true
        bloodGroup nullable: true
    }
}
