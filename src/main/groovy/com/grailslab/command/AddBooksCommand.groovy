package com.grailslab.command

import com.grailslab.enums.BookStockStatus
import com.grailslab.library.Book
import com.grailslab.library.BookDetails

/**
 * Created by Hasnat on 6/28/2015.
 */
class AddBooksCommand implements grails.validation.Validateable{
    Long id
    String barcode
    BookDetails bookDetails
    String source
    Integer quantity
    Double price
    String comments
    Date stockDate
    Date lostDate
    Book book
    Long transactionId
    BookStockStatus stockStatus

    static constraints = {
        id nullable: true
        barcode nullable: true
        comments nullable: true
        transactionId nullable: true
        lostDate nullable: true
        book nullable: true
        stockStatus nullable: true
    }
}
