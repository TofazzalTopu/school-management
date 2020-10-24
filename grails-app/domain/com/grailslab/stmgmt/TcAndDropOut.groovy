package com.grailslab.stmgmt


import com.grailslab.enums.TcType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class TcAndDropOut {
    Student student
    //for TC or DROP OUT student record
    TcType tcType       //Tc, Dropout, Defaulter, other
    String reason
    String schoolName
    Date releaseDate
    String releaseText
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

        tcType nullable: true
        reason nullable: true
        schoolName nullable: true
        releaseDate nullable: true
        releaseText nullable: true
    }
    static mapping = {
        releaseText type: 'text'
    }
}
