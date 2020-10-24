package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum RepeatType {

    NEVER("Never"),
    WEEKLY("Weekly"),

    final String value

    RepeatType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}