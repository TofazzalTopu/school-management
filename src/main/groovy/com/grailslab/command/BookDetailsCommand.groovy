package com.grailslab.command

import com.grailslab.library.Author
import com.grailslab.library.BookCategory
import com.grailslab.library.BookDetails
import com.grailslab.library.BookPublisher

/**
 * Created by Hasnat on 6/28/2015.
 */
class BookDetailsCommand implements grails.validation.Validateable{
    Long id
    BookCategory category
    String name
    String title
    BookPublisher bookPublisher
    Author author
    String addAuthor
    String language

    Double price
    String source
    Date stockDate
    Integer quantity

    static constraints = {
        id nullable: true
        title nullable: true
        addAuthor nullable: true
        bookPublisher nullable: true
        category nullable: true
        price nullable: true
        source nullable: true
        stockDate nullable: true
        quantity nullable: true
    }
}
