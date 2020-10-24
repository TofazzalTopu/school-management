package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum PriceType {

    BUY("Buy"),
    SELL("Sell")

    final String value

    PriceType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}