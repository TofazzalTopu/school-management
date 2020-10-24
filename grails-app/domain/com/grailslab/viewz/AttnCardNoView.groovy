package com.grailslab.viewz

class AttnCardNoView implements Serializable{
    String cardNo
    Long objId
    Long sectionId
    String objType
    String stdEmpNo
    Long hrPeriodId

    static constraints = {
        sectionId nullable: true
        hrPeriodId nullable: true
    }
    static mapping = {
        table 'v_attn_card_no'
        version false
        id composite:['cardNo', 'objId']
        cache usage:'read-only'
        cardNo column:'card_no'
        stdEmpNo column:'std_emp_no'
        objId column:'obj_id'
        sectionId column:'section_id'
        objType column:'obj_type'
        hrPeriodId column: 'hr_period_id'
    }
}
