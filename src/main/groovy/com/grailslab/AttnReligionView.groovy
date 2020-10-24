package com.grailslab

import com.grailslab.enums.AttendanceType

/**
 * Created by bddev on 10/25/2016.
 */
class AttnReligionView {
    Date reportDate
    AttendanceType dayType  //working, holiday

    Integer totalStudent
    Integer totalBoys
    Integer totalGirls

    Integer hinduBoys
    Integer hinduGrils

    Integer muslimsBoys
    Integer muslimsGirls

    Integer totalTc


    AttnReligionView() {

    }

    AttnReligionView(Date reportDate, AttendanceType dayType) {
        this.reportDate = reportDate
        this.dayType = dayType


        this.totalBoys=0
        this.totalGirls=0

        this.hinduBoys=0
        this.hinduGrils=0

        this.muslimsBoys=0
        this.muslimsGirls=0

        this.totalStudent=0
        this.totalTc=0
    }
}
