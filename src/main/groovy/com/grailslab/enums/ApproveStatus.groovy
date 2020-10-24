package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ApproveStatus {

    DRAFT("Draft"),
    APPROVED("Approved"),
    PENDING("Pending"),
    SUBMITTED("Submitted")


    final String value

    ApproveStatus(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

}