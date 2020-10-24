package com.grailslab.settings

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class NoticeBoard {

    String title
    String details
    String scrollText
    String scrollColor
    String iconClass
    String fileLink
    String category
    Date publish
    Date expire
    Boolean keepBoard
    Boolean keepScroll
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

        scrollText nullable: true, maxSize: 2000
        title nullable: true, maxSize: 2000
        details nullable: true, maxSize: 2000
        iconClass nullable: true
        scrollColor nullable: true
        fileLink nullable: true
        category nullable: true
    }
    static mapping = {
        keepBoard defaultValue: Boolean.FALSE
        keepScroll defaultValue: Boolean.FALSE
    }
}
