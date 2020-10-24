package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum ManageBookStatus {

    ADD_BOOK('Add BookDetails'),
    LOST_BOOK('Lost BookDetails'),

    final String value

    ManageBookStatus(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

    public static ManageBookStatus getYearByString(String year){
        for(ManageBookStatus e : ManageBookStatus.values()){
            if(year == e.value) return e;
        }
        return null;
    }

}