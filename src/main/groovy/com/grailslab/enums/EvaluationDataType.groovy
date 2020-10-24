package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum EvaluationDataType {

    CONSIDERATION("বিবেচনাধীন"),
    GENERAL("সাধারণ"),
    GOOD("ভালো"),
    VERYGOOD("বেশ ভালো"),


    final String value

    EvaluationDataType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}