package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum HolidayType {

    WEEKLY("Weekly"),
    GOVT("Govt Holiday"),

    final String value

    HolidayType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}