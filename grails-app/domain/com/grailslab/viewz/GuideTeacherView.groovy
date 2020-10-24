package com.grailslab.viewz

class GuideTeacherView implements Serializable{
    Long id
    String activeStatus
    String academicYear
    String studentIds
    Long employeeId
    String empid
    String name

    static constraints = {
    }
    static mapping = {
        table 'v_grade_teacher'
        version false
    }
}
