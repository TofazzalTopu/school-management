package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum FeeAppliedType {

    ALL_STD("All Student"),
    CLASS_STD("Class Student"),
    SECTION_STD("Section Student")

    final String value

    FeeAppliedType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}