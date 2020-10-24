package com.grailslab.enums

public enum SliderType {
    HOME("HOME"),
    ABOUT("ABOUT"),
    VIRTUAL_LIBRARY("VIRTUAL_LIBRARY"),
    OFFLINE_LIBRARY("OFFLINE_LIBRARY"),

    final String value

    SliderType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

    static Collection<SliderType> typeList(){
        return [HOME, ABOUT, VIRTUAL_LIBRARY, OFFLINE_LIBRARY]
    }
}
