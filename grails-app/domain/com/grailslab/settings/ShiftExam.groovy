package com.grailslab.settings


import com.grailslab.enums.ExamStatus
import com.grailslab.enums.ExamTerm
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus

class ShiftExam {
    String examName
    Integer ctExam
    ExamTerm examTerm       //old
    Integer weightOnResult
    String classIds
    Integer isLocked = 0

    String sectionIds
    ExamStatus examStatus = ExamStatus.NEW
    Date resultPublishOn            //possible result publish date when create exam
    Date resultPreparedDate
    Date publishedDate
    Date periodStart
    Date periodEnd
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

        ctExam nullable: true
        examName nullable: true
        weightOnResult nullable: true
        classIds nullable: true
        resultPreparedDate nullable: true
        publishedDate nullable: true
        sectionIds nullable: true
        periodStart nullable: true
        periodEnd nullable: true
    }

    String toString(){
        return examName
    }
}
