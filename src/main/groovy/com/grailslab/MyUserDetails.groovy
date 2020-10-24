package com.grailslab

import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.AcaYear
import grails.plugin.springsecurity.userdetails.GrailsUser

/**
 * Created by aminul on 1/25/2015.
 */
class MyUserDetails extends GrailsUser {
    final Long schoolId
    final String userRef
    final String name
    final AcademicYear academicYear
    final AcademicYear admissionYear
    final AcademicYear collegeWorkingYear
    final AcademicYear collegeAdmissionYear
    MyUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection authorities, long id, Long schoolId, String userRef, String name) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id)
        this.schoolId = schoolId
        this.userRef = userRef
        this.name = name
        this.academicYear=getAcaWorkingYear(AcaYearType.SCHOOL.value)
        this.admissionYear=getAcaAdmissionYear(AcaYearType.SCHOOL.value)
        this.collegeWorkingYear=getAcaWorkingYear(AcaYearType.COLLEGE.value)
        this.collegeAdmissionYear=getAcaAdmissionYear(AcaYearType.COLLEGE.value)
    }
    AcademicYear getAcaWorkingYear(String acaYearType){
        AcademicYear workingYear
        AcaYear acaYear = AcaYear.findByActiveStatusAndIsWorkingYearAndYearType(ActiveStatus.ACTIVE, Boolean.TRUE, acaYearType)
        if (acaYear) {
            workingYear = AcademicYear.valueOf("Y$acaYear.name")
        }
        return workingYear
    }
    AcademicYear getAcaAdmissionYear(String acaYearType){
        AcademicYear admissionYear
        AcaYear acaYear = AcaYear.findByActiveStatusAndIsAdmissionYearAndYearType(ActiveStatus.ACTIVE, Boolean.TRUE, acaYearType)
        if (acaYear) {
            admissionYear = AcademicYear.valueOf("Y$acaYear.name")
        }
        return admissionYear
    }
}
