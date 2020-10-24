package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum InvoiceDayType {

    OPEN("Asset"),
    CLOSED("Income"),
    REOPEN("Expense"),

    final String value

    InvoiceDayType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}