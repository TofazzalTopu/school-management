package com.grailslab.attn

import com.grailslab.enums.AttendanceType
import com.grailslab.gschoolcore.ActiveStatus

class AttnRecordDay {
    Date recordDate
    Date lastUploadOn
    Boolean forceLoad = false
    Boolean isClassDay = true
    Boolean recordAdded = false
    Boolean empLateUpdated = false
    Boolean stdLateUpdated = false
    ActiveStatus activeStatus = ActiveStatus.ACTIVE
    AttendanceType workingDayType //REGULAR,EXAM,NATIONAL PROGRAM,SCHOOL PROGRAM // WEEKLY HOLIDAY, NATIONAL HOLIDAY,SPECIAL HOLIDAY
    AttendanceType dayType //OPEN, HOLIDAY, Weekly Holiday
    //new Filed
    String holidayName

    static constraints = {
        lastUploadOn nullable: true
        workingDayType nullable: true
        holidayName nullable: true
        empLateUpdated nullable: true
        stdLateUpdated nullable: true
    }
}
