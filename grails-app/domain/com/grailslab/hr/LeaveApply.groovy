package com.grailslab.hr

import com.grailslab.enums.LeaveApplyType
import com.grailslab.enums.LeaveApproveStatus
import com.grailslab.enums.LeavePayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class LeaveApply {

    Employee employee
    LeaveName leaveName
    LeaveApplyType applyType
    LeavePayType payType
    Date fromDate
    Date toDate
    Integer numberOfDay
    Date applyDate      // code input.. not from UI
    String reason       //Reason for Leave.
    String remarks      // Optional remarks/Comments like with to without pay leave
    String argentContactNo     //required. Provide a second phone No
    String placeOnLeave
    LeaveApproveStatus approveStatus = LeaveApproveStatus.Draft    //One of Draft,Applied,Approved or Rejected. Returned Application also treat as Draft
    String supportedBy  //employee.empid
    String application

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

        argentContactNo nullable: true
        reason nullable: true
        remarks nullable: true
        supportedBy nullable: true
        placeOnLeave nullable: true
        application nullable: true
        approveStatus nullable: true
        applyDate nullable: true
        payType nullable: true
    }
    static mapping = {
        application type: 'text'
    }
}
