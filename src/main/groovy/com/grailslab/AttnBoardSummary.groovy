package com.grailslab

import com.grailslab.enums.AttendanceType
import org.grails.datastore.mapping.query.Query

/**
 * Created by bddev on 10/25/2016.
 */
class AttnBoardSummary {
    Date reportDate
    AttendanceType dayType  //working, holiday
    String description  // name/reason if holiday

    Integer attn1
    Integer attn1girl

    Integer attn2
    Integer attn2girl

    Integer attn3
    Integer attn3girl

    Integer attn4
    Integer attn4girl

    Integer attn5
    Integer attn5girl

    Integer total
    Integer totalgirl



    AttnBoardSummary() {

    }

    AttnBoardSummary(Date reportDate, AttendanceType dayType, String description) {
        this.reportDate = reportDate
        this.dayType = dayType
        this.description = description

        this.attn1=0
        this.attn1girl=0

        this.attn2=0
        this.attn2girl=0

        this.attn3=0
        this.attn3girl=0

        this.attn4=0
        this.attn4girl=0

        this.attn5=0
        this.attn5girl=0

        this.total=0
        this.totalgirl=0
    }
}
