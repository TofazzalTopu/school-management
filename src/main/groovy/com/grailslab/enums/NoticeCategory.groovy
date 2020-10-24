package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum NoticeCategory {

    PRIMARY("Primary"),
    HIGH_SCHOOL("High School"),
    COLLEGE("College"),


    final String value

    NoticeCategory(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

    static Collection<NoticeCategory> categoryList(){
        return [PRIMARY, HIGH_SCHOOL, COLLEGE]
    }

}