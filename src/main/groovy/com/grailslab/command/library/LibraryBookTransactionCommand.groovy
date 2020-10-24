package com.grailslab.command.library
/**
 * Created by Hasnat on 6/28/2015.
 */
class LibraryBookTransactionCommand implements grails.validation.Validateable{

    Long transactionCategory
    Long transactionAuthor
    Long transactionPublisher
    String bookTransactionFor
    String transactionPrintType
    String transactionLanguage

    static constraints = {
    }
}
