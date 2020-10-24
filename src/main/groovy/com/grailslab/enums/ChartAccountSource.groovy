package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum ChartAccountSource {

    Customer("Customer"),
    Employee("Employee"),
    None("None"),
    Subaccount("Subaccount"),
    Supplier("Supplier"),
    Student("Student")


    final String value

    ChartAccountSource(String value){
        this.value = value
    }

    String toString() {value}
    String getKey() {name()}
}
