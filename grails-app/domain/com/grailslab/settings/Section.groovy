package com.grailslab.settings


import com.grailslab.enums.GroupName
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee

class Section {
    String name
    Shift shift
    ClassRoom classRoom
    ClassName className
    GroupName groupName
    Employee employee
    Integer sortOrder
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

        classRoom nullable: true
        employee nullable: true
        groupName nullable: true
        sortOrder nullable: true
    }
}
