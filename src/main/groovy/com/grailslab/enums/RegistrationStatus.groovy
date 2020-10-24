package com.grailslab.enums

/**
 * Created by Hasnat on 5/28/2014.
 */
public enum RegistrationStatus {

    NEW("New"),
    ADMISSION("Admission"),     //admitted to any section
    READMISSION("Re Admission"), //Promoted to next class
    NOT_PROMOTED("Not Promoted"), //Promoted to next class
    DROPOUT("Drop Out"),
    TC("TC"),
    DELETED("Deleted"),

    final String value

    RegistrationStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}