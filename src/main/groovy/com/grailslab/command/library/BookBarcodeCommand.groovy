package com.grailslab.command.library

/**
 * Created by Hasnat on 6/28/2015.
 */
class BookBarcodeCommand implements grails.validation.Validateable{
    Long bookCategoryId
    Long bookAuthorId
    Long bookPublisherId
    Long bookDetailId
    Long bookNameId
    String barcodePrintType

    static constraints = {
        bookCategoryId nullable: true
        bookAuthorId nullable: true
        bookPublisherId nullable: true
        bookDetailId nullable: true
        bookNameId nullable: true
        barcodePrintType nullable: true
    }
}
