package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Grailslab on 10/29/2016.
 */
class AttnStudentReference {
    Date reportDate
    AttendanceType dayType  //working, holiday
    String description  // name/reason if holiday
    String pStatus
    Date inTime
    Date outTime


    AttnStudentReference(Date reportDate, AttendanceType dayType, String description) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
    }
    AttnStudentReference(Date reportDate, AttendanceType dayType, String description, String pStatus) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
        this.pStatus = pStatus
    }
    AttnStudentReference(Date reportDate, AttendanceType dayType, String description, String pStatus, Date inTime, Date outTime) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
        this.pStatus = pStatus
        this.inTime = inTime
        this.outTime = outTime
    }
}
