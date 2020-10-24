package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum DateRangeType {

    TODAY("Today"),
//    THIS_WEEK("This Week"),
//    THIS_MONTH("This Month"),
//    THIS_YEAR("This Year"),
    All("All")


    final String value

    DateRangeType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}