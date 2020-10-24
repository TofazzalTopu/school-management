package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ScholarshipType {

    FULL("full Free"),
    HALF("half Free"),



    final String value

    ScholarshipType(String value) {
        this.value = value
    }


    String toString() { value }
    String getKey() { name() }

}