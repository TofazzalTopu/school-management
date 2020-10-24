package com.grailslab.command

import com.grailslab.library.Author
import com.grailslab.library.BookCategory
import com.grailslab.settings.ClassName
import com.grailslab.settings.SubjectName

/**
 * Created by Hasnat on 6/28/2015.
 */
class LostBookCommand implements grails.validation.Validateable{
    Long id
    BookCategory category
    String name
    String title
    Author author
    Author addAuthor
    ClassName className
    String isbn
    String language
    SubjectName subject

    static constraints = {
        id nullable: true
        addAuthor nullable: true
        className nullable: true
        isbn nullable: true
    }

}
