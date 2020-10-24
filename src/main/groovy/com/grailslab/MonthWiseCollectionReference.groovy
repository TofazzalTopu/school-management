package com.grailslab

/**
 * Created by Aminul on 11/5/2016.
 */
class MonthWiseCollectionReference {
    String feeName
    Double january
    Double february
    Double march
    Double april
    Double may
    Double june
    Double july
    Double august
    Double september
    Double october
    Double november
    Double december

    MonthWiseCollectionReference() {
    }
    MonthWiseCollectionReference(String feeName, Double january) {
        this.feeName = feeName
        this.january = january
    }

    MonthWiseCollectionReference(String feeName, Double january, Double february, Double march, Double april, Double may, Double june, Double july, Double august, Double september, Double october, Double november, Double december) {
        this.feeName = feeName
        this.january = january
        this.february = february
        this.march = march
        this.april = april
        this.may = may
        this.june = june
        this.july = july
        this.august = august
        this.september = september
        this.october = october
        this.november = november
        this.december = december
    }
}
