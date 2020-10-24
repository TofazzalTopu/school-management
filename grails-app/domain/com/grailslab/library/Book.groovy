package com.grailslab.library


import com.grailslab.enums.BookStockStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class Book  {
    String bookId
    String barcode
    BookDetails bookDetails
    BookStockStatus stockStatus
    String source
    Double price
    String comments
    Date stockDate
    Date lostDate
    //default fields
    Date dateCreated // autoupdated by GORM
    Date lastUpdated // autoupdated by GORM
    String createdBy
    String updatedBy
    Long schoolId
    AcademicYear academicYear
    ActiveStatus activeStatus = ActiveStatus.ACTIVE   // Active, inactive, deleted

    static constraints = {
        createdBy(nullable: true)
        updatedBy(nullable: true)
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        schoolId(nullable: true)
        academicYear(nullable: true)
        activeStatus(nullable: true)

        bookId unique: true
        barcode unique: true, nullable: true
        source nullable: true
        price nullable: true
        comments nullable: true
        lostDate nullable: true
    }

}
