package com.grailslab.viewz

import com.grailslab.enums.SubjectType

class StdStudentSubjectView implements Serializable{
    //id field mapped as student id
    String className
    Long classNameId

    String sectionName
    Long sectionId
    String groupName

    Long stdId  //student id field
    String studentName
    Integer rollNo
    String studentid

    Long studentSubjectId

    SubjectType subjectType
    String subjectName
    Long subjectId
    Boolean isOptional

    static mapping = {
        table 'v_std_student_subject'
        version false
        cache usage:'read-only'
        id composite:['stdId', 'subjectId']

        className column:'class_name'
        classNameId column:'class_name_id'
        sectionName column:'section_name'
        sectionId column:'section_id'
        groupName column:'group_name'

        stdId column:'std_id'
        studentName column:'student_name'
        rollNo column:'roll_no'
        studentid column:'studentid'

        studentSubjectId column:'student_subject_id'

        subjectType column:'subject_type'
        subjectName column:'subject_name'
        subjectId column:'subject_id'
        isOptional column: 'is_optional'
    }

    static constraints = {
    }
}
