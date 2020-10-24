package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum BookTransactionType {

    BY_BOOK("By Book"),
    BY_EMPLOYEE("By Employee"),
    BY_STUDENT("By Student"),


    final String value

    BookTransactionType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}