package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ResultStatus {

    PASSED("Pass"),
    FAILED("Fail"),
    NA("N/A")        //in case of optional subject
    final String value

    ResultStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}