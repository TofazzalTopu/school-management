package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum ExamStatus {
    NEW("New"),
    PENDING("Processing"),
    PROCESSED("Processed"),
    TABULATION("Tabulation"),
    RESULT("Result"),
    PUBLISHED("Published")

    final String value
    ExamStatus(String value) {
        this.value = value
    }

    static Collection<ExamStatus> resultWorkingList(){
        return [NEW, PENDING, PROCESSED, TABULATION, RESULT]
    }

    static Collection<ExamStatus> resultPublishingList(){
        return [TABULATION, RESULT, PUBLISHED]
    }

    String toString() { value }
    String getKey() { name() }

}