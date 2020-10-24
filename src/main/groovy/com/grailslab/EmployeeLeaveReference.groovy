package com.grailslab

import com.grailslab.gschoolcore.AcademicYear


/**
 * Created by Aminul on 10/29/2016.
 */
class EmployeeLeaveReference {
    AcademicYear academicYear
    String empId
    String empName
    String empDesignation
    String leaveName
    Integer leaveObtainCount
    Integer numOfDays
    Integer leaveDue

    EmployeeLeaveReference() {
    }
    EmployeeLeaveReference(String leaveName, Integer numOfDays, Integer leaveObtainCount, Integer leaveDue) {
        this.leaveName = leaveName
        this.numOfDays = numOfDays
        this.leaveObtainCount = leaveObtainCount
        this.leaveDue = leaveDue
    }
    EmployeeLeaveReference(AcademicYear academicYear, String empId, String empName, String empDesignation, String leaveName, Integer numOfDays, Integer leaveObtainCount, Integer leaveDue) {
        this.academicYear = academicYear
        this.empId = empId
        this.empName = empName
        this.empDesignation = empDesignation
        this.leaveName = leaveName
        this.numOfDays = numOfDays
        this.leaveObtainCount = leaveObtainCount
        this.leaveDue = leaveDue
    }
}
