package com.grailslab.command

import com.grailslab.enums.AttendStatus
import com.grailslab.settings.ExamSchedule
import com.grailslab.stmgmt.Student

/**
 * Created by Hasnat on 6/27/2015.
 */
class HallExamMarkCommand implements grails.validation.Validateable{
    Long id
    ExamSchedule examScheduleId
    Student studentId
    AttendStatus attendStatus
    Double markObtain

    Double hallWrittenMark
    Double hallPracticalMark
    Double hallObjectiveMark
    Double hallSbaMark
    Double hallInput5

    static constraints = {
        id nullable: true
        studentId nullable: true

        hallWrittenMark nullable:true
        hallPracticalMark nullable:true
        hallObjectiveMark nullable:true
        hallSbaMark nullable:true
        hallInput5 nullable:true
        markObtain blank: false, validator: { value, obj ->
            if( value> obj.examScheduleId.hallExamMark)
                return 'exam.mark.wrong.input.msg'
        }
    }
}
