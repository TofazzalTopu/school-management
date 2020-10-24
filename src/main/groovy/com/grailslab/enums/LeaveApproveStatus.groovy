package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum LeaveApproveStatus {

    Draft("Draft"),
    Applied("Applied"),
    Approved("Approved"),
    Rejected("Rejected")

    final String value

    LeaveApproveStatus(String value) {
        this.value = value
    }

    static Collection<LeaveApproveStatus> statusForApproval(){
        return [Draft, Applied, Approved, Rejected]
    }
    static Collection<LeaveApproveStatus> statusForAdminApply(){
        return [Applied, Approved, Draft]
    }
    static Collection<LeaveApproveStatus> statusForEmployee(){
        return [Draft, Applied, Approved, Rejected]
    }
    static Collection<LeaveApproveStatus> statusForEmployeeApply(){
        return [Draft, Applied]
    }


    String toString() { value }
    String getKey() { name() }

}