package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum SelectionTypes {

    BY_SHIFT("By Shift"),
    BY_CLASS("By Class"),
    BY_SECTION("By Section"),
    BY_STUDENT("By Student"),
    BY_TEACHER("Teacher & Others")

    final String value

    SelectionTypes(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }
}