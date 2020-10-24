package com.grailslab.command.library

class OnlineBookCategoryCommand implements grails.validation.Validateable{
    Long id
    String categoryName
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
        description nullable: true
    }
}
