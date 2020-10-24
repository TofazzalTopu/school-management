package com.grailslab.enums

/**
 * Created by khalil.ullah on 10/15/14.
 */
public enum AccountType {

    ASSET("Asset"),
    INCOME("Fee"),
    EXPENSE("Expense"),
    STATIONARY("Stationary"),
    CAFETERIA("Cafeteria")


    final String value

    AccountType(String value) {
        this.value = value
    }
    static Collection<AccountType> feeItems(){
//        return [INCOME, STATIONARY, CAFETERIA]
        return [INCOME]
    }

    static Collection<AccountType> feeMenuItems(){
        return [INCOME, STATIONARY]
    }

    static Collection<AccountType> cafeteriaItems(){
        return [STATIONARY, CAFETERIA]
    }

    String toString() { value }
    String getKey() { name() }

}