package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ExamTerm {
    FIRST_TERM("1st Term"),
    SECOND_TERM("2nd Term"),
    HALF_YEARLY("Half Yearly"),
    TERM_EXAM("Term Exam"),
    TEST_EXAM("Test Exam"),
    MODEL_TEST("Model Test"),
    FINAL_TEST("Final Exam"),

    final String value

    ExamTerm(String value) {
        this.value = value
    }
    static Collection<ExamTerm> type(){
        return [FIRST_TERM, SECOND_TERM, HALF_YEARLY, TERM_EXAM, TEST_EXAM, MODEL_TEST, FINAL_TEST]
    }
    static Collection<ExamTerm> termExamList(){
        return [FIRST_TERM, SECOND_TERM, HALF_YEARLY]
    }
    String toString() { value }
    String getKey() { name() }

}