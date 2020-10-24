package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum DueOnType {

    ADVANCE("On Advance"),
    DUE("On Due"),
    DELIVERY("On Delivery")

    final String value

    DueOnType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}