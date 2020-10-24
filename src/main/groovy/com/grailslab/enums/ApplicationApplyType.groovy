package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ApplicationApplyType {
    BOTH("Both"),
    ONLINE("Online"),
    OFFLINE("Offline")


    final String value

    ApplicationApplyType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}