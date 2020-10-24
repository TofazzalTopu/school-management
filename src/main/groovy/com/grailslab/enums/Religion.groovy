package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum Religion {

    ISLAM("Islam"),
    HINDU("Hindu"),
    CHRISTIAN("Christian"),
    BUDDHIST("Buddhist"),

    final String value




    Religion(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }
}