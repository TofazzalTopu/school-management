package com.grailslab.viewz

class LessonFeedbackView implements Serializable{
    String className
    Long classNameId
    String sectionName
    Long sectionNameId
    String weekNumber
    Long weekNumberId
    String subjectName
    Long subjectNameId
    String academicYear

    static constraints = {
    }

    static mapping = {
        table 'v_lesson_feedback'
        version false
        id composite:['classNameId', 'sectionNameId', 'weekNumberId', 'subjectNameId', 'academicYear']
    }
}
