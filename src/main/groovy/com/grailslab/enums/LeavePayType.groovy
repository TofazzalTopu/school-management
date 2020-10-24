package com.grailslab.enums

public enum LeavePayType {

    PAID_LEAVE("Paid"),
    CASUAL_LEAVE("Casual"),
    UN_PAID_LEAVE("Un paid"),
    UN_AUTHORIZE_LEAVE("Un authorize"),
    LATE_PRESENT("Late days"),
    HALF_DAY_LEAVE("Half day leave"),
    UN_AUTHORIZE_HALF_DAY_LEAVE("Un authorize half day Leave"),
    final String value

    LeavePayType(String value) {
        this.value = value
    }

    static Collection<LeavePayType> paidUnPaidLeave(){
        return [PAID_LEAVE, UN_PAID_LEAVE]
    }
    static Collection<LeavePayType> leavePayTypeLatePresentHalfDay(){
        return [LATE_PRESENT, HALF_DAY_LEAVE]
    }

    String toString() { value }
    String getKey() { name() }

}