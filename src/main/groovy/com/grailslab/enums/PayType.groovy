package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum PayType {

    PAID("Complete"),
    DUE("Deducting"),
    WITHDRAW("Return")

    final String value

    PayType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}