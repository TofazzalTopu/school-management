package com.grailslab.command

/**
 * Created by Grailslab on 1/23/2016.
 */
class SmsPurchaseCommand implements grails.validation.Validateable{
	Date billingDate
	Integer numOfSms
	Double smsPrice
	Date expireDate

	static constraints = {
	}
}
