package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum BloodGroup {

   APOSITIVE("A +"),
   ANEGATIVE("A -"),
   BPOSITIVE("B +"),
   BNEGATIVE("B -"),
   ABPOSITIVE("AB +"),
   ABNEGATIVE("AB -"),
   OPOSITIVE("O +"),
   ONEGATIVE("O -")



    final String value

    BloodGroup(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
