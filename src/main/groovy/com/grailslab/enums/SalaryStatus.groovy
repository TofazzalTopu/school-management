package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum SalaryStatus {
    Draft("Draft"),
    Prepared("Prepared"),
    Disbursement("Disbursement"),

    final String value

    SalaryStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
