package com.grailslab.enums

/**
 * Created by monir on 11/29/2016.
 */
enum LinkType {

    HOME("Home"),
    GALLERY("Gallery"),

    final String value

    LinkType(String value) {
        this.value = value
    }
    static Collection<LinkType> linkTypeContents(){
        return [HOME, GALLERY]
    }
    String toString() { value }
    String getKey() { name() }
}