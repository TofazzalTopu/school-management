package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum Gender {

    MALE("Male"),
    FEMALE("Female"),

    final String value
    Gender(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
