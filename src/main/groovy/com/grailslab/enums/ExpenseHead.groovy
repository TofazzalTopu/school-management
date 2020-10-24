package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum ExpenseHead {

    SALARY('Salary'),
    BONUS('Bonus'),
    VOUCHER('Voucher'),

    final String value

    ExpenseHead(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

    public static ExpenseHead getYearByString(String year){
        for(ExpenseHead e : ExpenseHead.values()){
            if(year == e.value) return e;
        }
        return null;
    }

}