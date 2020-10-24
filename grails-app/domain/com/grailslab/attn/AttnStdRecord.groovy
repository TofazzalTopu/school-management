package com.grailslab.attn

class AttnStdRecord {
    AttnRecordDay recordDay
    Date recordDate
    Long studentId
    String stdNo     //Student.studentID
    Long sectionId
    Date inTime
    Date outTime
    String remarks
    Boolean isLateEntry
    Boolean isEarlyLeave

    static constraints = {
        outTime nullable: true
        remarks nullable :true
        isLateEntry nullable :true
        isEarlyLeave nullable :true
        recordDate nullable: true
    }
}
