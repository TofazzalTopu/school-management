package com.grailslab.settings


import com.grailslab.enums.EventType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class CalenderEvent {
    Long id

    Holiday holiday
    ExamSchedule examSchedule
    Employee teacher                // for class Schedule

    ClassName className
    Section section         //
    SubjectName subject

    EventType eventType
    String name
    String description
    Date startDate          // date with time
    Date endDate             // date with time
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

        holiday(nullable:true)
        examSchedule(nullable:true)
        teacher(nullable:true)
        description(nullable:true)
        className(nullable:true)
        section(nullable:true)
        subject(nullable:true)
    }
}
