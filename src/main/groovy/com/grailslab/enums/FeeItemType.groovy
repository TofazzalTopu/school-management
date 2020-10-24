package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum FeeItemType {
    FEE("Fee"),
    STATIONARY("Stationary"),
    CAFETERIA("Cafeteria")

    final String value

    FeeItemType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}