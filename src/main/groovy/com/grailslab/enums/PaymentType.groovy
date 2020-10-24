package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum PaymentType {

    CHEQUE("By Bank"),
    CASH("By Cash"),
    BKASH("bKash")

    final String value

    PaymentType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}