package com.grailslab.enums

/**
 * Created by Hasnat on 9/11/2014.
 */
public enum ImageUpCategory {

    HOME_CAROUSEL_SLIDER('carousel slider home'),
    HOME_SUCCESS_STUDENT('Success Stories'),
    MANAGING_COMMITTEE('Managing Committee'),

    final String value

    ImageUpCategory(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

    public static ImageUpCategory getYearByString(String year){
        for(ImageUpCategory e : ImageUpCategory.values()){
            if(year == e.value) return e;
        }
        return null;
    }

}