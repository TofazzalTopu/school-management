package com.grailslab.enums

/**
 * Created by Hasnat on 5/28/2014.
 */
public enum StudentStatus {

    NEW("New"),
    ADMISSION("Admission"),
    TC("TC"),
    PASSED("Passed"),
    DELETED("Deleted")

    final String value

    StudentStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}