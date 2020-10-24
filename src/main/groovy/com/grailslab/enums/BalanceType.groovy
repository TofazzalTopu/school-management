package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum BalanceType {

    DEBIT("Debit"),
    CREDIT("Credit")

    final String value

    BalanceType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}