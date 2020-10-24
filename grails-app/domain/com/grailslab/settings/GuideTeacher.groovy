package com.grailslab.settings

class GuideTeacher {
    Long employeeId     //Work as TC list, className add
    String studentIds    //Comma separated student id's
    String academicYear
    String activeStatus = "ACTIVE"   // Active, inactive, deleted

    static constraints = {
    }
}
