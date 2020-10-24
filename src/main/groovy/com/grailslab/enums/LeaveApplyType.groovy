package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum LeaveApplyType {
    REQUEST_FOR_LEAVE("Leave in advance"),
    APPROVED_ABSENCE("Leave of absence"),
    REQUEST_FOR_HALF_DAY("Request For Half Day")


    final String value

    LeaveApplyType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}

public enum LeaveDayCount {

    Flat("Flat"),
    Weekly_Holiday("Weekly_Holiday"),
    All_Holiday("All_Holiday")


    final String value

    LeaveDayCount(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}