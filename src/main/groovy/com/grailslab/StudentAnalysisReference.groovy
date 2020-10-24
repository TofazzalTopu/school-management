package com.grailslab

/**
 * Created by Aminul on 7/10/2017.
 */
class StudentAnalysisReference {
    //Type All
    Integer maleStudentCount
    Integer femaleStudentCount
    Integer muslimCount
    Integer hinduCount
    Integer christanCount
    Integer buddhistCount
    Integer admissionCount
    Integer promotionCount

    String className
    String sectionName
    Integer sortOrder

    StudentAnalysisReference() {
    }

    StudentAnalysisReference(String className, String sectionName, Integer sortOrder, Integer maleStudentCount, Integer femaleStudentCount,
    Integer muslimCount, Integer hinduCount, Integer christanCount, Integer buddhistCount, Integer admissionCount, Integer promotionCount) {
        this.className = className
        this.sectionName = sectionName
        this.sortOrder = sortOrder
        this.maleStudentCount = maleStudentCount
        this.femaleStudentCount = femaleStudentCount
        this.muslimCount = muslimCount
        this.hinduCount = hinduCount
        this.christanCount = christanCount
        this.buddhistCount = buddhistCount
        this.admissionCount = admissionCount
        this.promotionCount = promotionCount
    }
}
