package com.grailslab.stmgmt

import com.grailslab.SequenceConstructor
import com.grailslab.enums.ApplicantStatus
import com.grailslab.enums.BloodGroup
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Profession

class RegOnlineRegistration {
    Long regFormId
    //step1 fields
    ClassName className
    String name
    String fathersName
    String mobile
    Date birthDate

    //step2
    String nameBangla
    Religion religion
    Gender gender
    BloodGroup bloodGroup
    String birthCertificateNo
    String nationality
    String email
    Boolean hasBroOrSisInSchool
    String broOrSis1Id
    String broOrSis2Id

    //step3
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

    //Step4
    String preSchoolName
    String preSchoolAddress
    String preSchoolClass
    Date preSchoolTcDate

    //step5
    String imagePath

    //final submit
    ApplicantStatus applicantStatus = ApplicantStatus.Draft
    String serialNo
    AcademicYear academicYear

    //Form Fee
    Double feeAmount = 0
    Double payment = 0
    String applyNo      //invoice no
    Date invoiceDate
    String description


    ActiveStatus activeStatus = ActiveStatus.ACTIVE

    Date dateApplication
    Date dateAdmitCard
    //auto date inserted by grails gorm
    Date dateCreated
    Date lastUpdated

    static constraints = {
        regFormId nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
        dateApplication nullable: true
        dateAdmitCard nullable: true
        nameBangla nullable: true
        fathersName nullable: true
        birthDate nullable: true
        religion nullable: true
        gender nullable: true
        bloodGroup nullable: true
        birthCertificateNo nullable: true
        nationality nullable: true
        presentAddress nullable: true
        permanentAddress nullable: true
        email nullable: true
        hasBroOrSisInSchool nullable: true
        broOrSis1Id nullable: true
        broOrSis2Id nullable: true
        fathersProfession nullable: true
        fathersMobile nullable: true
        fathersIncome nullable: true
        fathersIsalive nullable: true
        mothersName nullable: true
        mothersProfession nullable: true
        mothersMobile nullable: true
        mothersIncome nullable: true
        mothersIsalive nullable: true
        hasBroOrSisInSchool nullable: true
        legalGuardianName nullable: true
        legalGuardianProfession nullable: true
        legalGuardianMobile nullable: true
        relationWithLegalGuardian nullable: true

        preSchoolName nullable: true
        preSchoolAddress nullable: true
        preSchoolClass nullable: true
        preSchoolTcDate nullable: true

        applicantStatus nullable: true
        serialNo nullable: true
        applyNo nullable: true
        feeAmount nullable: true
        payment nullable: true
        invoiceDate nullable: true
        academicYear nullable: true
        description nullable: true

        imagePath nullable:true
    }

    static {
        //sequential auto generated sequence with custom logic
        SequenceConstructor.constructSequence(
                "RegOnlineRegistration.serialNoSequence", 1,
                """
                slCode.toUpperCase() + String.format("%04d", sequenceNumber)
                """
        )

        SequenceConstructor.constructSequence(
                "RegOnlineRegistration.applyNoSequence", 1,
                """
                regCode.toUpperCase() + String.format("%04d", sequenceNumber)
                """
        )

    }
}
