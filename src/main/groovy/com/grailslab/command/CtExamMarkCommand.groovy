package com.grailslab.command

import com.grailslab.enums.AttendStatus
import com.grailslab.settings.ExamSchedule
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class CtExamMarkCommand implements grails.validation.Validateable{

    Long id
    ExamSchedule examScheduleId
    Student studentId
    AttendStatus attendStatus
    Double markObtain
    String ctMarkFor

    static constraints = {
        id nullable: true
        studentId nullable: true
        markObtain blank: false, validator: { value, obj ->
            if(obj.ctMarkFor == "CLASS_TEST1" &&  value> obj.examScheduleId.ctExamMark) {
                return 'exam.mark.wrong.input.msg'
            } else if(obj.ctMarkFor == "CLASS_TEST2" &&  value> obj.examScheduleId.ctExamMark2) {
                return 'exam.mark.wrong.input.msg'
            } else if(obj.ctMarkFor == "CLASS_TEST3" &&  value> obj.examScheduleId.ctExamMark3) {
                return 'exam.mark.wrong.input.msg'
            }
        }
    }
}
