package com.grailslab
/**
 * Created by Aminul on 10/29/2016.
 */
class AttnEmployeeCountReference {
    String name
    String empId
    String designation
    Integer workingDayCount
    Integer presentCount
    Integer leaveCount
    Integer absentCount
    Integer lateCount


    AttnEmployeeCountReference() {
    }

    AttnEmployeeCountReference(String name, String empId, Integer workingDayCount, Integer presentCount) {
        this.name = name
        this.empId = empId
        this.workingDayCount = workingDayCount
        this.presentCount = presentCount
        int absentDays = workingDayCount - presentCount
        this.absentCount = absentDays < 0 ? 0 : absentDays
    }
    AttnEmployeeCountReference(String name, String empId, String designation, Integer workingDayCount, Integer presentCount,
                               Integer leaveCount, Integer lateCount) {
        this.name = name
        this.empId = empId
        this.designation = designation
        this.workingDayCount = workingDayCount
        this.presentCount = presentCount
        this.leaveCount = leaveCount
        this.lateCount = lateCount
        int absentDays = workingDayCount - (presentCount+leaveCount)
        this.absentCount = absentDays < 0 ? 0 : absentDays
    }
}
