package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum OlympiadType {
    lCompitition("ভাষা প্রতিযোগিতা"),
    mathematics("গণিত"),
    science("বিজ্ঞান"),
    gKnowledge("সাধারণ জ্ঞান")

    final String value

    OlympiadType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}