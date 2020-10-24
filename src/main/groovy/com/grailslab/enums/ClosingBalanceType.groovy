package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ClosingBalanceType {

    RUNNING_BALANCE("Running Balance"),
    CLOSING_BALANCE("Closing Balance")

    final String value

    ClosingBalanceType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}