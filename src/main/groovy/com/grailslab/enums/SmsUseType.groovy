package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum SmsUseType {

    BUY("Buy"),
    SEND("Send"),
    EXPIRE("Expire")


    final String value

    SmsUseType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}