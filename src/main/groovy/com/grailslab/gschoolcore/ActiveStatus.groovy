package com.grailslab.gschoolcore

/**
 * Created by aminul on 8/25/2015.
 */
public enum ActiveStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETE("Delete"),
    BKASH("bKash"),     //draft - waiting for confirmation state
    EDIT("Edit"),

    final String value

    ActiveStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}