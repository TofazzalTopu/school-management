package com.grailslab.command

import com.grailslab.enums.BloodGroup
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegForm

/**
 * Created by Hasnat on 6/28/2015.
 */
class ApplicantEditCommand implements grails.validation.Validateable{

    RegForm regForm
    ClassName className
    String name
    String fathersName
    String mobile
    Date birthDate
    AcademicYear academicYear



    String nameBangla
    Long regId
    Religion religion
    Gender gender
    BloodGroup bloodGroup
    String birthCertificateNo
    String nationality
    String email
    Boolean hasBroOrSisInSchool
    String broOrSis1Id
    String broOrSis2Id



    String fathersProfession
    String fathersMobile
    Double fathersIncome
    Boolean fathersIsalive
    String presentAddress
    String permanentAddress


    String mothersName
    String mothersProfession
    String mothersMobile
    Double mothersIncome
    String mothersIsalive

    String legalGuardianName
    String legalGuardianProfession
    String legalGuardianMobile
    String relationWithLegalGuardian



    String preSchoolName
    String preSchoolAddress
    String preSchoolClass
    Date preSchoolTcDate






    static constraints = {

        className nullable: true

        academicYear nullable: true
        regForm nullable: true
        name nullable: true
        fathersName nullable: true
        mobile nullable: true
        birthDate nullable: true
        nameBangla nullable: true
        religion nullable: true

        gender nullable: true
        birthCertificateNo nullable: true
        fathersProfession nullable: true
        mothersProfession nullable: true


        fathersMobile nullable: true
        fathersIncome nullable: true
        email nullable: true
        bloodGroup nullable: true
        hasBroOrSisInSchool nullable: true
        broOrSis1Id nullable: true
        broOrSis2Id nullable: true
        presentAddress nullable: true
        permanentAddress nullable: true


        fathersIsalive nullable:true
        mothersMobile nullable: true
        mothersIncome nullable: true
        mothersIsalive nullable: true
        legalGuardianName nullable: true
        legalGuardianProfession nullable: true
        legalGuardianMobile nullable: true
        relationWithLegalGuardian nullable: true


        preSchoolName nullable: true
        preSchoolAddress nullable: true
        preSchoolClass nullable: true
        preSchoolTcDate nullable: true

        regId nullable: true



    }
}
