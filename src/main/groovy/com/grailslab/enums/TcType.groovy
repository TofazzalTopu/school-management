package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum TcType {

    TC("TC"),
    Dropout("Drop Out"),
    Defaulter("Defaulter"),
    Other("Other")

    final String value

    TcType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
