package com.grailslab.enums

public enum BookStockStatus {
    ADDED("Stock"),
    OUT("Out"),
    LOST("Lost"),
    DAMAGE("Damage")

    final String value

    BookStockStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}