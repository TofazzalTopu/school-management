package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum AttendStatus {

    PRESENT("Present"),
    ABSENT("Absent"),
    ABSENTONLEAVE("Absent On Leave"),
    EXPELLED("Expelled"),
    NO_INPUT("No Input"),

    final String value

    AttendStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
