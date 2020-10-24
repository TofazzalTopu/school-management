package com.grailslab.command.library

class OnlineBookCommand implements grails.validation.Validateable{
    Long id
    String bookName
    String description
    Long categoryId

    static constraints = {
        id nullable: true
        description nullable: true
    }
}
