package com.grailslab.command

import com.grailslab.enums.GroupName
import com.grailslab.enums.SubjectType
import com.grailslab.settings.ClassName
import com.grailslab.settings.SubjectName

/**
 * Created by aminul on 3/22/2015.
 */
class SubjectMappingCommand implements grails.validation.Validateable{
    Long id
    ClassName className
    GroupName groupName
    SubjectName subject
    SubjectType subjectType

    Integer weightOnResult
    Integer passMark
    Integer sortOrder

    boolean isExtracurricular
    boolean isOtherActivity

    boolean isCtExam
    Integer ctMark
    Integer ctMark2
    Integer ctMark3
    Integer ctEffMark

    Integer hallMark
    Integer hallEffMark

    Boolean isHallPractical
    Integer hallWrittenMark
    Integer hallPracticalMark
    Integer hallObjectiveMark
    Integer hallSbaMark
    Integer hallInput5

    Boolean isPassSeparately
    Integer writtenPassMark
    Integer practicalPassMark
    Integer objectivePassMark
    Integer sbaPassMark
    Integer input5PassMark

    String alternativeSubIds

    static constraints = {
        id nullable: true
        groupName nullable: true
        passMark nullable: true

        ctMark nullable: true
        ctMark2 nullable: true
        ctMark3 nullable: true
        ctEffMark nullable: true

        hallMark nullable: true
        hallEffMark nullable: true

        isHallPractical nullable: true
        hallWrittenMark nullable: true
        hallPracticalMark nullable: true
        hallObjectiveMark nullable: true
        hallSbaMark nullable: true
        hallInput5 nullable: true

        isPassSeparately nullable: true
        writtenPassMark nullable: true
        practicalPassMark nullable: true
        objectivePassMark nullable: true
        sbaPassMark nullable: true
        input5PassMark nullable: true

        alternativeSubIds nullable: true
    }
}
