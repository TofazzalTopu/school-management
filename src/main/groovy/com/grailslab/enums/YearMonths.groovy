package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum YearMonths {
    JANUARY('January', 1),
    FEBRUARY('February', 2),
    MARCH('March', 3),
    APRIL('April', 4),
    MAY('May', 5),
    JUNE('June', 6),
    JULY('July', 7),
    AUGUST('August', 8),
    SEPTEMBER('September', 9),
    OCTOBER('October', 10),
    NOVEMBER('November', 11),
    DECEMBER('December', 12)

    private final String value;
    private final int serial;

    YearMonths(String value, int serial) {
        this.value = value
        this.serial = serial
    }
    YearMonths(String value) {
        this.value = value
    }
    static Collection<YearMonths> collegeMonths(){
        return [JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE]
    }

    String toString() { value }

    String getKey() { name() }
    public String getValue() {
        return value;
    }

    public int getSerial() {
        return serial;
    }

}

public enum CalenderMonths {
    January('January'),
    February('February'),
    March('March'),
    April('April'),
    May('May'),
    June('June'),
    July('July'),
    August('August'),
    September('September'),
    October('October'),
    November('November'),
    December('December')

    final String value

    CalenderMonths(String value) {
        this.value = value
    }

    String toString() { value }

    String getKey() { name() }

}