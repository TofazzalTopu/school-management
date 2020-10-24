package com.grailslab.enums

public enum OpenCont {

    OUR_SCHOOL_LEFT_CONTENT("Our School Left Content"),
    OUR_SCHOOL_RIGHT_CONTENT("Our School Right Content"),
    MANAGING_COMMITTEE_CONTENT("Managing Committee"),
    VOICES_CONTENT("Voices"),
    FAQ_CONTENT("F.A.Q"),
    TIMELINE_CONTENT("Time Line"),
    ATAGLANCE_CONTENT("At a Glance"),
    HOME_OUR_SCHOOL("Our School"),
    HOME_AIMS_OBJECTIVE("Aims And Objective"),
    HOME_VOICE("Our Voice"),
    HOME_FAQ("F.A.Q"),
    HOME_LINKS("Links"),
    NOTICE("Notice Board"),
    ABOUT_US_OUR_VALUES("Our Values"),
    ABOUT_US_OUR_FUTURE("Our Future"),
    ABOUT_US_OUR_FACILITY("Our Facilities"),
    EDUCATION_SYSTEM("Education Systems"),
    MISSION_AND_VISSION("Mission And Vission"),
    LIBRARY_FACILITY("Library Facility"),

    final String value

    OpenCont(String value) {
        this.value = value
    }
    String toString() { value }
    String getKey() { name() }
}
