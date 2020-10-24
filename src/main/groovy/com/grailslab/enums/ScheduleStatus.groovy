package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ScheduleStatus {

    NO_SCHEDULE("Not Added"),
    ADDED("Added"),   //both CT and Hall schedule Added
    PUBLISHED("Published")   //both CT and Hall schedule Added

    final String value

    ScheduleStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}