package com.grailslab.enums

/**
 * Created by Hasnat on 5/28/2014.
 */
public enum PromotionStatus {

    NEW("New"),
    PROMOTED("Promoted"),       //Re Admission complete
    NOT_PROMOTED("Not Promoted")       //Re Admission complete

    final String value

    PromotionStatus(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}