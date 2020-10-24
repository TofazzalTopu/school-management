package com.grailslab.command

/**
 * Created by Hasnat on 6/27/2015.
 */
class NoticeBoardCommand implements grails.validation.Validateable{
    Long id
    String title
    String details
    String scrollText
    String scrollColor
    String category
    Date publish
    Date expire
    Boolean keepBoard
    Boolean keepScroll
    static constraints = {
        id nullable: true
        scrollText nullable: true
        details nullable: true
        keepBoard nullable: true
        keepScroll nullable: true
        category nullable: true
    }
}
