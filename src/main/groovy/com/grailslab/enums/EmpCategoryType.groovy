package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum EmpCategoryType {

    Education("Education"),
    Management("Management"),
    Admin("Admin"),
    Account_Finace("Account & Finance"),
    Support("Support")


    final String value

    EmpCategoryType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}