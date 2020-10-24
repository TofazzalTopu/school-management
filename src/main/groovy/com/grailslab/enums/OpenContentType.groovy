package com.grailslab.enums

public enum OpenContentType {
    Feature_Content("Feature Content"),
    About_School("About School"),
    School_Facilities("School Facilities"),

    voice("Voice"),                             //voice and management committee
    Mgmt_Committee("Management Committee"),

    VIDEO("Video"),                             //picture and video gallery
    PICTURE("Picture")

    final String value

    OpenContentType(String value) {
        this.value = value
    }

    static Collection<OpenContentType> featureContents(){
        return [Feature_Content, About_School, School_Facilities]
    }
    static Collection<OpenContentType> mgmtVoiceContents(){
        return [voice, Mgmt_Committee]
    }
    String toString() { value }
    String getKey() { name() }
}
