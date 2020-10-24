package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum AcaYearType {
    SCHOOL("School"),
    COLLEGE("College"),
    ALL("All"),


    final String value

    AcaYearType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}