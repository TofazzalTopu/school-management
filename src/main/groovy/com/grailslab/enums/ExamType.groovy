package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ExamType {

    CLASS_TEST("Class Test"),
    HALL_TEST("Hall Test"),

    final String value

    ExamType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}