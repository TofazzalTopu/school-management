package com.grailslab.command.library
/**
 * Created by Hasnat on 6/28/2015.
 */
class LibraryMemberCommand implements grails.validation.Validateable{

    String referenceId
    String name
    String memberId
    String address
    String presentAddress
    Date memberShipDate
    String mobile
    String email

    static constraints = {
        memberId nullable: true
        memberShipDate nullable: true
        email nullable: true
    }
}
