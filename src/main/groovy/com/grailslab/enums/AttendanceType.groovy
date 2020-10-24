package com.grailslab.enums

/**
 * Created by Aminul on 2/23/2016.
 */
enum AttendanceType {
//Working Day
    Open_Day("Open"),
    Holiday("Holiday"),
    Weekly_Holiday("Weekly Holiday"),

    //Working Day Type
    Regular("Regular"),
    Exam("Exam"),
    National_Program("National Program"),
    School_Program("School Program"),

    //Attendance Status
    Present("Present"),
    Absent("Absent"),
    Over_Time("Over Time"),
    Late_Present("Late Present"),
    NA("NA"),

    //Present Details
    Normal("Normal"),
    Tardy("Tardy"),
    Tardy_Excuse("Tardy with excuse"),

    //Absent Details
    Absent_Excuse("Absent with excuse"),
    Early_Leave("Early Leave"),
    Leave("Leave"),
    Ill("ill"),
    Unauthorized_Leave("Unauthorized Leave"),

    //Paid Type
            Payable("Payable"),
    Un_Payable("Un Payable"),
    Paid_Leave("Paid Leave"),
    Un_Paid_Leave("Un Paid Leave")

    final String value
    AttendanceType(String value) {
        this.value = value
    }

    static Collection<AttendanceType> schoolDayTypes(){
        return [Open_Day, Holiday, Weekly_Holiday, National_Program, School_Program]
    }

    static Collection<AttendanceType> workingDayTypes(){
        return [Open_Day, Exam, National_Program, School_Program]
    }
    static Collection<AttendanceType> attendanceTypes(){
        return [Present, Absent,Over_Time]
    }
    static Collection<AttendanceType> payTypes(){
        return [Payable, Un_Payable]
    }
    static Collection<AttendanceType> presentTypes(){
        return [Regular, Tardy, Tardy_Excuse]
    }
    static Collection<AttendanceType> absentTypes(){
        return [Early_Leave, Leave, Ill, Unauthorized_Leave]
    }
    static Collection<AttendanceType> stdAbsentTypes(){
        return [ Leave, Unauthorized_Leave, Ill, Early_Leave]
    }

    String toString() { value }
    String getKey() { name() }
}