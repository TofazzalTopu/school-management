package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ChartAccountType {

    Asset("Asset"),
    Expenditure("Expenditure"),
    Income("Income"),
    Liability("Liability")


    final String value

    ChartAccountType(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

}