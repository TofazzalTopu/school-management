package com.grailslab.command

import com.grailslab.enums.OpenContentType

/**
 * Created by Hasnat on 10/11/2015.
 */
class OpenContentCommand implements grails.validation.Validateable{
    Long id
    OpenContentType openContentType
    String title
    String iconClass
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
        openContentType nullable: true
    }
}

class OpenTimeLineCommand implements grails.validation.Validateable{
    Long id
    String title
    String name
    String iconClass
    String description
    Integer sortOrder

    static constraints = {
        id nullable: true
    }
}

class OpenQuickLinkCommand implements grails.validation.Validateable{
    Long id
    String displayName
    String urlType
    String linkUrl
    String iconClass
    Integer sortIndex

    static constraints = {
        id nullable: true
    }
}

class InvoiceDayCommand implements grails.validation.Validateable{
    Long id
    Date invoiceDate

    static constraints = {
        id nullable: true
    }
}

