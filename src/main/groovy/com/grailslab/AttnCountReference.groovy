package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Aminul on 10/29/2016.
 */
class AttnCountReference {
    String className
    String sectionName
    String studentId
    String name
    Integer rollNo
    Integer workingDayCount
    Integer presentCount
    Integer absentCount


    AttnCountReference() {
    }

    AttnCountReference(String className, String sectionName, String studentId, String name, Integer rollNo, Integer workingDayCount, Integer presentCount) {
        this.className = className
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
        this.workingDayCount = workingDayCount
        this.presentCount = presentCount
        this.absentCount = workingDayCount - presentCount
    }
}
