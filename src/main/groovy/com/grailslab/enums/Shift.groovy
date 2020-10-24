package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum Shift {

    MORNING("Morning Shift"),
    DAY("Day Shift")

    final String value

    Shift(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }
}