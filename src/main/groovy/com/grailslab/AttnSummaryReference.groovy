package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Aminul on 10/29/2016.
 */
class AttnSummaryReference {
    Date recordDate
    String className
    String sectionName
    Integer totalStudent
    Integer presentCount
    Integer absentCount
    AttendanceType dayType
    String description

    AttnSummaryReference() {
    }

    AttnSummaryReference(Date recordDate, Integer totalStudent, Integer presentCount, AttendanceType dayType, String description) {
        this.recordDate = recordDate
        this.totalStudent = totalStudent
        this.presentCount = presentCount
        this.dayType = dayType
        this.description = description
        this.absentCount = totalStudent - presentCount
    }

    AttnSummaryReference(Date recordDate, String className, String sectionName, Integer totalStudent, Integer presentCount, AttendanceType dayType, String description) {
        this.recordDate = recordDate
        this.className = className
        this.sectionName = sectionName
        this.totalStudent = totalStudent
        this.presentCount = presentCount
        this.dayType = dayType
        this.description = description
        this.absentCount = totalStudent - presentCount
    }

    AttnSummaryReference(Date recordDate, String className, String sectionName, Integer totalStudent, Integer presentCount) {
        this.recordDate = recordDate
        this.className = className
        this.sectionName = sectionName
        this.totalStudent = totalStudent
        this.presentCount = presentCount
        this.absentCount = totalStudent - presentCount
    }
}
