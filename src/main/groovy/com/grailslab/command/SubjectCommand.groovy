package com.grailslab.command

/**
 * Created by aminul on 3/22/2015.
 */
class SubjectCommand implements grails.validation.Validateable{
    Long id
    String name
    Integer sortPosition
    String code
    Boolean isAlternative

    Long parentId	//In case of Alternative Subject
    Boolean hasChild

    static constraints = {
        id nullable: true
        code nullable: true
        isAlternative nullable: true

        hasChild nullable: true
        parentId nullable: true
    }
}
