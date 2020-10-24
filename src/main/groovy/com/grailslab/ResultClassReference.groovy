package com.grailslab

/**
 * Created by Aminul on 7/10/2017.
 */
class ResultClassReference {
    String reportType
    String className
    String sectionName
    Integer numberOfStudent
    Integer passCount
    Integer failCount
    Integer aPlusCount
    Integer aCount
    Integer aMinusCount
    Integer bCount
    Integer cCount
    Integer fCount

    ResultClassReference(String reportType, String className, String sectionName, Integer numberOfStudent, Integer passCount, Integer failCount, Integer aPlusCount, Integer aCount, Integer aMinusCount, Integer bCount, Integer cCount, Integer fCount) {
        this.reportType = reportType
        this.className = className
        this.sectionName = sectionName
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
