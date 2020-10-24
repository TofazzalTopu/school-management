package com.grailslab.messaging

import com.grailslab.enums.SmsUseType
import com.grailslab.gschoolcore.ActiveStatus

class SmsBilling {
	Integer smsLength
	String message
	Integer numOfSender

	SmsUseType smsUseType = SmsUseType.SEND
	Date billingDate
	Integer numOfSms
	Integer smsBalance
	Double smsPrice
	Date expireDate
	String insertedSmsIds
	//default fields
	Date dateCreated // autoupdated by GORM
	Date lastUpdated // autoupdated by GORM
	String createdBy
	String updatedBy
	Long schoolId
	ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

	static constraints = {
		createdBy(nullable: true)
		updatedBy(nullable: true)
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
		schoolId(nullable: true)
		activeStatus(nullable: true)

		message(nullable: true, size: 1..479)
		smsLength nullable: true
		numOfSender nullable: true
		billingDate nullable: true
		numOfSms nullable: true
		smsBalance nullable: true
		smsPrice nullable: true
		expireDate nullable: true
		insertedSmsIds nullable: true
	}

}
