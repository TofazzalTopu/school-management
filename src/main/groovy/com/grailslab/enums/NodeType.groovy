package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum NodeType {

    ROOT("Root"),
    FIRST("First"),
    SECOND("Second"),
    THIRD("Third")

    final String value

    NodeType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}