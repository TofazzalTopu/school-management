package com.grailslab.viewz

class LibraryMemberView implements Serializable {
    String name
    String mobile
    Long objId
    Long allowedDays
    String objType
    String stdEmpNo

    static constraints = {
    }
    static mapping = {
        table 'v_library_member'
        version false
        id composite:['objId', 'objType']
        cache usage:'read-only'
        name column:'name'
        mobile column:'mobile'
        stdEmpNo column:'std_emp_no'
        objId column:'obj_id'
        allowedDays column:'allowed_days'
        objType column:'obj_type'
    }
}
