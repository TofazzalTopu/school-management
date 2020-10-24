package com.grailslab.command.library
/**
 * Created by Hasnat on 6/28/2015.
 */
class BookSourceListCommand implements grails.validation.Validateable{

    Long bookSourceCategory
    Long bookSourceAuthor
    Long bookSourcePublisher
    String bookSourceReportFor
    String bookSource
    String bookSourceListPrintType
    String bookSourceLanguage

    static constraints = {
    }
}
