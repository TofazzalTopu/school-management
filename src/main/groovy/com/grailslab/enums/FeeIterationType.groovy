package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum FeeIterationType {

    RECEIVE("On Receive"),
    DAILY("Daily"),
//    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")

    final String value

    FeeIterationType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}