package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum CalculationType {

    CONTINUE("Continue"),
    BALANCED("Balanced")


    final String value

    CalculationType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}