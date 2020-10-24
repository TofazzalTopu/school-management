package com.grailslab.enums

/**
 * Created by aminul on 10/1/2015.
 */
enum HrKeyType {
    HM("Head Master"),
    AHM("Assistant Head Master"),
    TEACHER("Teacher"),
    OSTAFF("Office Staff"),
    SSTAFF("Support Staff")

    final String value

    HrKeyType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}