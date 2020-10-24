package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by Aminul on 10/29/2016.
 */
class EmpDailyReference {



    Date recordDay
    AttendanceType dayType  //working, holiday
    String description  // name/reason if holiday
    String empId
    String name
    String pStatus
    Date inTime
    Date outTime


    EmpDailyReference() {
    }

    EmpDailyReference(Date recordDay, AttendanceType dayType, String description, String empId, String name, String pStatus) {
        this.recordDay = recordDay
        this.dayType = dayType
        this.description = description
        this.empId = empId
        this.name = name
        this.pStatus = pStatus
        this.inTime = null
        this.outTime = null
    }

    EmpDailyReference(Date recordDay, AttendanceType dayType, String description, String empId,String name, String pStatus, Date inTime, Date outTime) {
        this.recordDay = recordDay
        this.dayType = dayType
        this.description = description
        this.empId = empId
        this.name = name
        this.pStatus = pStatus
        this.inTime = inTime
        this.outTime = outTime
    }


}
