package com.grailslab.settings


import com.grailslab.gschoolcore.ActiveStatus

class SubjectName {
	String name
	String description
	String code
	Boolean isAlternative = false
	Long parentId	//In case of Alternative Subject
	Boolean hasChild	//In case of Parent Subject that have child subjects
	Integer sortPosition

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

		description nullable: true
		code nullable: true
		isAlternative nullable: true
		hasChild nullable: true
		parentId nullable: true
	}
}
