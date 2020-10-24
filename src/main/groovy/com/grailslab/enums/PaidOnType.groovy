package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum PaidOnType {

    RECEIVE("On Receive"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")

    final String value

    PaidOnType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}