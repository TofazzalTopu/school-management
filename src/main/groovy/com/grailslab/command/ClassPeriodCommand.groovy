package com.grailslab.command

import com.grailslab.enums.Shift

 //ClassPeriodController - save
class ClassPeriodCommand implements grails.validation.Validateable{
    Long id
    Integer sortPosition
    String name
    String startOn      //10 AM
    Integer duration    // 40
    Shift shift
    Boolean isPlayTime

    static constraints = {
        id nullable: true
        shift nullable: true
        sortPosition nullable: true
        isPlayTime nullable: true
    }
}
