package com.grailslab.command

import com.grailslab.enums.ApplicantStatus
import com.grailslab.enums.PrintOptionType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

/**
 * Created by Aminul on 2/24/2016.
 */
class OnlineRegistrationRecordCommand implements grails.validation.Validateable{
    Long id
    Date fromDate
    Date toDate
    ClassName className
    /*AcademicYear academicYear*/
    ApplicantStatus applicantStatus
    String reportType
    PrintOptionType printOptionType

    static constraints = {
        id nullable: true
        fromDate nullable: true
        toDate nullable: true
        applicantStatus nullable: true
       /* academicYear nullable: true*/
        printOptionType nullable: true
        className nullable: true
        reportType nullable: true


    }
}
