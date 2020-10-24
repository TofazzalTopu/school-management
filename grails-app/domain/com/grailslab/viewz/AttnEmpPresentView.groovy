package com.grailslab.viewz

class AttnEmpPresentView implements Serializable{
    Long objId
    Long recordDayId
    Long employeeId

    Date recordDate
    Date inTime
    Date outTime
    Boolean isLate
    Boolean isEarlyLeave
    String cardNo
    String empid
    String mobile
    String name
    String employeeType
    String designation
    Long hrCategoryId
    String holidayName
    String dayType
    String workingDayType

    static constraints = {
    }
    static mapping = {
        table 'v_attn_emp_present'
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
        employeeId column:'employee_id'
        cardNo column:'card_no'
        empid column:'empid'
        mobile column:'mobile'
        name column:'name'
        employeeType column:'employee_type'
        designation column:'designation'
        hrCategoryId column:'hr_category_id'
        holidayName column:'holiday_name'
        dayType column:'day_type'
    }
}
