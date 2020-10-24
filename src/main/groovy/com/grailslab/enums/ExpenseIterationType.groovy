package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ExpenseIterationType {

    ONPAY("On Pay"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    EXAMTERM("Exam Term"),
    FESTIVAL("Festival"),
    YEARLY("Yearly")

    final String value

    ExpenseIterationType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}