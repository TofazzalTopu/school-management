package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum EventType {

    WEEKLY("Weekly"),
    HOLIDAY("Holiday"),
    Annual_Program("Annual Program"),
    Exam_Days("Exam Days"),
    CT_SCHEDULE("CT Exam Schedule"),
    HALL_SCHEDULE("Hall Exam Schedule"),
    CLASS_SCHEDULE("Class Schedule"),

    final String value

    EventType(String value) {
        this.value = value
    }
    static Collection<EventType> workingHolidayType(){
        return [HOLIDAY,Annual_Program,Exam_Days]
    }
    String toString() { value }
    String getKey() { name() }

}