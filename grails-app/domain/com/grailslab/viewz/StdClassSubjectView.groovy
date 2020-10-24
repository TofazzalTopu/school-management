package com.grailslab.viewz

import com.grailslab.enums.SubjectType

class StdClassSubjectView {
    String className
    Long classNameId
    String groupName
    SubjectType subjectType
    Integer weightOnResult
    Integer passMark
    Boolean isExtracurricular
    Boolean isOtherActivity
    String alternativeSubIds
    String alternativeSubNames
    Boolean isCtExam
    Integer ctMark
    Integer ctMark2
    Integer ctMark3
    Integer ctEffMark
    Boolean isHallExam
    Integer hallMark
    Integer hallEffMark
    Boolean isHallPractical
    Integer hallWrittenMark
    Integer hallPracticalMark
    Integer hallObjectiveMark
    Integer hallSbaMark
    Integer hallInput5
    Integer sortPosition
    String subjectName
    Long subjectId
    String code

    Boolean isPassSeparately
    Integer writtenPassMark
    Integer practicalPassMark
    Integer objectivePassMark
    Integer sbaPassMark
    Integer input5PassMark

    static mapping = {
        table 'v_std_class_subject'
        version false
        cache usage:'read-only'
    }

    static constraints = {
    }
}
