package com.grailslab.festival

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName

class FesRegistration  {
    FesProgram fesProgram
    String serialNo

    String name
    String instituteName
    ClassName className
    Integer rollNo
    String contactNo
    String olympiadTopics

    //2nd student information for scientific paper/project
    Boolean scientificPaper
    String spProjectName
    String spStudentName
    ClassName spClassName
    Integer spRollNo
    String spContactNo

    //2nd student information for slide show presentation
    Boolean slideShow
    String ssProjectName
    String ssStudentName
    ClassName ssClassName
    Integer ssRollNo
    String ssContactNo

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

        scientificPaper nullable: true
        spProjectName nullable: true
        spStudentName nullable: true
        spClassName nullable: true
        spRollNo nullable: true
        spContactNo nullable: true

        slideShow nullable: true
        ssProjectName nullable: true
        ssStudentName nullable: true
        ssClassName nullable: true
        ssRollNo nullable: true
        ssContactNo nullable: true


    }
}
