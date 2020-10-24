package com.grailslab.viewz

class AttnStdPresentView implements Serializable{
    Long objId
    Long recordDayId
    Long studentId

    Date recordDate
    Date inTime
    Boolean isLate
    Boolean isEarlyLeave
    Date outTime
    String rollNo
    String stdid
    String name
    String gender
    String religion
    String sectionName
    String dayType
    String holidayName
    String workingDayType
    String className

    static constraints = {
    }
    static mapping = {
        table 'v_attn_std_present'
        version false
        id composite:['recordDayId', 'objId']
        cache usage:'read-only'
        objId column:'obj_id'
        recordDate column:'record_date'
        workingDayType column:'working_day_type'
        inTime column:'in_time'
        isLate column:'is_late'
        isEarlyLeave column:'is_early_leave'
        outTime column:'out_time'
        recordDayId column:'record_day_id'
        studentId column:'student_id'
        rollNo column:'roll_no'
        stdid column:'stdid'
        name column:'name'
        gender column:'gender'
        religion column:'religion'
        sectionName column:'section_name'
        className column:'class_name'
        dayType column:'day_type'
        holidayName column:'holiday_name'
    }
}
