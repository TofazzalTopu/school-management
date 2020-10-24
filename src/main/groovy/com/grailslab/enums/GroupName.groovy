package com.grailslab.enums

/**
 * Created by aminul on 9/17/2014.
 */
public enum GroupName {
    SCIENCE("Science"),
    ARTS("Arts"),
    COMMERCE("Commerce")

    final String value

    GroupName(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}