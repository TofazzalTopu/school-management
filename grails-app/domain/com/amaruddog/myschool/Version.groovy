package com.amaruddog.myschool

class Version {

    long id
    long versionNumber
    Integer pluginId

    static mapping = {
        version false
        table 'core_version'
    }

    static constraints = {
        versionNumber unique: 'pluginId'
    }
}
