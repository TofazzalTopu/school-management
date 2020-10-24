package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum LetterGrade {

    GRADE_A_PLUS("A+"),
    GRADE_A("A"),
    GRADE_A_MINUS("A-"),
    GRADE_B("B"),
    GRADE_C("C"),
    GRADE_D("D"),
    GRADE_F("F")

    final String value

    LetterGrade(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}