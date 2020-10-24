package com.grailslab

import org.grails.datastore.mapping.query.Query

/**
 * Created by Aminul on 7/10/2017.
 */
class ResultSubjectReference {
    String reportType
    String subjectName
    Integer numberOfStudent
    Integer passCount
    Integer failCount
    Integer aPlusCount
    Integer aCount
    Integer aMinusCount
    Integer bCount
    Integer cCount
    Integer fCount

    ResultSubjectReference(String reportType, String subjectName, Integer numberOfStudent, Integer passCount, Integer failCount, Integer aPlusCount, Integer aCount, Integer aMinusCount, Integer bCount, Integer cCount, Integer fCount) {
        this.reportType = reportType
        this.subjectName = subjectName
        this.numberOfStudent = numberOfStudent
        this.passCount = passCount
        this.failCount = failCount
        this.aPlusCount = aPlusCount
        this.aCount = aCount
        this.aMinusCount = aMinusCount
        this.bCount = bCount
        this.cCount = cCount
        this.fCount = fCount
    }

}
