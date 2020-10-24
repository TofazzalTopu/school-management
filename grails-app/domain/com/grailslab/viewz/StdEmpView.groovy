package com.grailslab.viewz

class StdEmpView implements Serializable{
    String name
    String mobile
    Long objId
    String objType
    String stdEmpNo

    static constraints = {
    }
    static mapping = {
        table 'v_std_emp'
        version false
        id composite:['objId', 'objType']
        cache usage:'read-only'
        name column:'name'
        mobile column:'mobile'
        stdEmpNo column:'std_emp_no'
        objId column:'obj_id'
        objType column:'obj_type'
    }
}
