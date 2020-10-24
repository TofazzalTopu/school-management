package com.grailslab

/**
 * Created by Aminul on 10/29/2016.
 */
class FeePaymentReference {
    String sectionName
    String studentId
    String name
    Integer rollNo
    Double amount
    Double discount
    Double netPayment
    String invoiceNo
    String invoiceDate

    FeePaymentReference() {
    }

    FeePaymentReference(String sectionName, String studentId, String name, Integer rollNo) {
        this.sectionName = sectionName
        this.studentId = studentId
        this.name = name
        this.rollNo = rollNo
    }
}
