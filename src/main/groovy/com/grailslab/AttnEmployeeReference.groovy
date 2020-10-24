package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Grailslab on 10/29/2016.
 */
class AttnEmployeeReference {
    Date reportDate
    AttendanceType dayType  //working, holiday
    String description  // name/reason if holiday
    String pStatus
    Date inTime
    Date outTime


    AttnEmployeeReference(Date reportDate, AttendanceType dayType, String description) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
    }

    AttnEmployeeReference(Date reportDate, AttendanceType dayType, String description, String pStatus) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
        this.pStatus = pStatus
    }

    AttnEmployeeReference(Date reportDate, AttendanceType dayType, String description, String pStatus, Date inTime, Date outTime) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description
        this.pStatus = pStatus
        this.inTime = inTime
        this.outTime = outTime
    }
}
