package com.grailslab.library

import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class BookTransaction {

    Book book
    Double fine
    Date issueDate
    Date returnDate
    Long referenceId                // studentId, employeeId, memberId
    String referenceType            // Student, Teacher, Member
    Date submissionDate
    PayType payType = PayType.DUE
    PayType returnType = PayType.DUE

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

        fine nullable: true
        payType nullable: true
        submissionDate nullable: true
    }
}
