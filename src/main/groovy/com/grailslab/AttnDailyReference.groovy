package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Aminul on 10/29/2016.
 */
class AttnDailyReference {

    Date recordDay
    AttendanceType dayType  //working, holiday
    String description  // name/reason if holiday
    String className
    String sectionName
    String studentId
    String name
    Integer rollNo
    String pStatus
    Date inTime
    Date outTime


    AttnDailyReference() {
    }

    AttnDailyReference(Date recordDay, AttendanceType dayType, String description, String className, String sectionName, String studentId, String name, Integer rollNo, String pStatus) {
        this.recordDay = recordDay
        this.dayType = dayType
        this.description = description
        this.className = className
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
        this.pStatus = pStatus
        this.inTime = null
        this.outTime = null
    }

    AttnDailyReference(Date recordDay, AttendanceType dayType, String description, String className, String sectionName, String studentId, String name, Integer rollNo, String pStatus, Date inTime, Date outTime) {
        this.recordDay = recordDay
        this.dayType = dayType
        this.description = description
        this.className = className
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
        this.pStatus = pStatus
        this.inTime = inTime
        this.outTime = outTime
    }
}
