package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum PrintOptionType {

    PDF("Pdf Report"),
    XLSX("MS Excell"),
    DOCX("MS Word")

    final String value

    PrintOptionType(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }
}