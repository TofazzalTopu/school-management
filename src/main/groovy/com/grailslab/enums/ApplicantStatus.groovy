package com.grailslab.enums

/**
 * Created by Hasnat on 5/28/2014.
 */
public enum ApplicantStatus {
    Apply("Applied"),
    Draft("Draft"),
    AdmitCard("AdmitCard"),
//    GiveExam("GiveExam"),
    Selected("Selected"),
    Admitted("Admitted")

    final String value

    static Collection<ApplicantStatus> selectionStatus(){
        return [Apply, Draft, AdmitCard, Selected]
    }

    ApplicantStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}