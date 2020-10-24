package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum CaroselImageType {

    HomePage("Home Page"),
    AboutUs("About Us"),


    final String value

    CaroselImageType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}