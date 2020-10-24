package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum WeekDays {
    SUN('Sunday'),
    MON('Monday'),
    TUE('Tuesday'),
    WED('Wednesday'),
    THR('Thursday'),
    FRI('Friday'),
    SAT('Saturday')

    final String value

    WeekDays(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

}

public enum CalenderDays {
    Sunday('Sunday'),
    Monday('Monday'),
    Tuesday('Tuesday'),
    Wednesday('Wednesday'),
    Thursday('Thursday'),
    Friday('Friday'),
    Saturday('Saturday')

    final String value

    CalenderDays(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

}