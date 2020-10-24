package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum TransactionType {

    BANK_TRANSACTION("Bank Transaction"),
    INITIAL_DEPOSIT("Initial Deposit"),
    VOUCHER("Voucher"),
    SALARY("Salary"),
    COLLECTION("Collection")

    final String value

    TransactionType(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }

}