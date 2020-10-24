package com.grailslab.command


import com.grailslab.enums.OpenContentType
import com.grailslab.settings.ClassName

/**
 * Created by Hasnat on 10/11/2015.
 */
class MgmtVoiceCommand implements grails.validation.Validateable{
    Long id
    OpenContentType openContentType
    String title
    String name
    String iconClass
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
        openContentType nullable: true
    }
}

class SuccessStoryCommand implements grails.validation.Validateable{
    Long id
    String name
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
    }
}

class SliderImageCommand implements grails.validation.Validateable{
    Long id
    Integer sortIndex
    String title
    String description
    String type

    static constraints = {
        id nullable: true
        id description: true
    }
}

class FestivalCommand implements grails.validation.Validateable{
    String name
    Date startDate
    Date endDate
    String olympiadTopics        //comma separated OlympiadType keys
    String helpContact
    Date regOpenDate
    Date regCloseDate
    Long id

    static constraints = {
        id nullable: true
    }

}

class FesRegistrationCommand implements grails.validation.Validateable{
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

    static constraints = {
        scientificPaper nullable: true
        slideShow nullable: true
        spProjectName nullable: true
        spStudentName nullable: true
        spClassName nullable: true
        spRollNo nullable: true
        spContactNo nullable: true

        ssProjectName nullable: true
        ssStudentName nullable: true
        ssClassName nullable: true
        ssRollNo nullable: true
        ssContactNo nullable: true
    }

}