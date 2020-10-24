package com.grailslab.stmgmt

import com.grailslab.SequenceConstructor
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Registration {
    //reference of current class, section and student if admitted
    StudentStatus studentStatus = StudentStatus.NEW         // only using NEW - new Registration, Admission - if adimt

    String studentID
    String cardNo
    Integer deviceId
    String name
    String nameBangla
    String mobile
    Integer admissionYear
    Gender gender
    String bloodGroup
    Religion religion
    String nationality
    Date birthDate
    String birthCertificateNo
    String presentAddress
    String permanentAddress
    String email
    String imagePath

//fathers information
    String fathersName
    String fathersMobile
    String fathersProfession
    Double fathersIncome
    Boolean fatherIsAlive

    //mothers information
    String mothersName
    String mothersMobile
    String mothersProfession
    Double mothersIncome
    Boolean motherIsAlive

//guardian information
    String guardianName
    String guardianMobile
    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        nameBangla nullable: true
        nationality nullable: true
        fathersMobile nullable: true
        fatherIsAlive nullable: true
        mothersMobile nullable: true
        mothersIncome nullable: true
        motherIsAlive nullable: true
        guardianName nullable: true
        guardianMobile nullable: true
        admissionYear nullable: true
        deviceId unique: true, nullable: true
        studentID unique: true
        religion nullable: true
        presentAddress nullable: true
        permanentAddress nullable: true
        email nullable: true
        mobile nullable: true
        fathersProfession nullable: true
        mothersProfession nullable: true
        fathersIncome nullable: true
        cardNo nullable: true
        permanentAddress nullable: true
        birthDate nullable: true
        imagePath nullable: true
        mothersName nullable: true
        bloodGroup nullable: true
        gender nullable: true
        birthCertificateNo nullable: true
    }

    static {
        //sequential auto generated sequence with custom logic
        SequenceConstructor.constructSequence(
                "Registration.studentId6DigitSequence", 1,
                """
                regCode.toUpperCase() + String.format("%06d", sequenceNumber)
                """
        )

        SequenceConstructor.constructSequence(
                "Registration.studentId4DigitSequence", 1,
                """
                regCode.toUpperCase() + String.format("%04d", sequenceNumber)
                """
        )

    }

}
