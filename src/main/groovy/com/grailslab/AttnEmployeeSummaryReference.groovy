package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Aminul on 10/29/2016.
 */
class AttnEmployeeSummaryReference {
    Date recordDate
    Integer totalEmployee
    Integer presentCount
    Integer leaveCount
    Integer lateCount
    Integer absentCount
    String dayType
    String description

    AttnEmployeeSummaryReference() {
    }

    AttnEmployeeSummaryReference(Date recordDate, Integer totalEmployee, Integer presentCount, Integer leaveCount, Integer lateCount, String dayType, String description) {
        this.recordDate = recordDate
        this.totalEmployee = totalEmployee
        this.presentCount = presentCount
        this.leaveCount = leaveCount
        this.lateCount = lateCount
        this.absentCount = totalEmployee - (presentCount + leaveCount)
        this.dayType = dayType
        this.description = description
    }
}
