package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum EmployeeType {

    FULL_TIME('Full Time'),
    PART_TIME('Part Time'),
    IN_PROVISION('In Provision'),

    final String value

    EmployeeType(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }
}