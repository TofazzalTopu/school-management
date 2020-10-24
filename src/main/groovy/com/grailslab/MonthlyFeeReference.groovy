package com.grailslab

/**
 * Created by Aminul on 10/30/2016.
 */
class MonthlyFeeReference {
    String sectionName
    String studentId
    String name
    Integer rollNo
    Double payAmount
    String paidMonths
    Double paidAmount
    Integer dueMonth
    Double dueAmount

    MonthlyFeeReference() {
    }

    MonthlyFeeReference(String sectionName, String studentId, String name, Integer rollNo) {
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
    }

    MonthlyFeeReference(String sectionName, String studentId, String name, Integer rollNo, Double payAmount, String paidMonths, Double paidAmount, Integer dueMonth, Double dueAmount) {
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
        this.payAmount = payAmount
        this.paidMonths = paidMonths
        this.paidAmount = paidAmount
        this.dueMonth = dueMonth
        this.dueAmount = dueAmount
    }
}
