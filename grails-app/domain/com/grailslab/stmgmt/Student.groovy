package com.grailslab.stmgmt


import com.grailslab.enums.PromotionStatus
import com.grailslab.enums.ScholarshipType
import com.grailslab.enums.StudentStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section

class Student {

    String name     //@todo-remove
    String studentID //@todo-remove

    Registration registration
    Section section
    ClassName className
    Integer rollNo
    Boolean scholarshipObtain = false
    Double scholarshipPercent = 0
    ScholarshipType scholarshipType

    Integer positionInSection
    Integer positionInClass
    Boolean autoPromotion
    StudentStatus studentStatus = StudentStatus.NEW
    //NEW - to a class, TC - in case of TcAndDropout Events, DELETED - delete from class and return to previous class
    PromotionStatus promotionStatus = PromotionStatus.NEW
    YearMonths admissionMonth = YearMonths.JANUARY
    Double easTotalMark = 0

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

        autoPromotion nullable: true
        positionInSection nullable: true
        positionInClass nullable: true
        scholarshipObtain nullable: true
        scholarshipPercent nullable: true
        admissionMonth nullable: true
        scholarshipType nullable: true
        easTotalMark nullable: true
    }
    static mapping = {
        promotionStatus defaultValue: PromotionStatus.NEW
        autoPromotion defaultValue: Boolean.FALSE
        sort rollNo: "asc" // or "desc"
    }
}
