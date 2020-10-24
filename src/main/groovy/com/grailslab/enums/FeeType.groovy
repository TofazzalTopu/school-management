package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum FeeType {

    COMPULSORY("Compulsory"),
    OPTIONAL("Optional"),
    ACTIVATION("Activation"),

    final String value

    FeeType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}