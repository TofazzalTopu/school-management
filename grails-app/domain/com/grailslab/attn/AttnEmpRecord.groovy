package com.grailslab.attn

class AttnEmpRecord {
    AttnRecordDay recordDay
    Date recordDate
    Long employeeId
    String empNo  //Employee.employeeID
    Date inTime
    Date outTime
    Boolean isLateEntry
    Boolean isEarlyLeave

    static constraints = {
        outTime nullable: true
        isLateEntry nullable :true
        isEarlyLeave nullable :true
        recordDate nullable: true
    }
}
