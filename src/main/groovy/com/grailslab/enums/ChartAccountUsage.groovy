package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ChartAccountUsage {

    AP("AP"),
    AR("AR"),
    Bank("Bank"),
    Ledger("Ledger"),
    Cash("Cash")


    final String value

    ChartAccountUsage(String value){
        this.value = value
    }

    String toString() {value}
    String getKey() {name()}

}