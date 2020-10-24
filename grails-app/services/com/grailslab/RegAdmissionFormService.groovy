package com.grailslab

import com.grailslab.enums.ApplicantStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegOnlineRegistration
import grails.gorm.transactions.Transactional

@Transactional
class RegAdmissionFormService {
    def springSecurityService
    def messageSource
    def schoolService


    static final String[] sortColumns = ['name']


    def regOnlineRegistrationList(ClassName className, ApplicantStatus applicantStatus, Date fromDate, Date toDate, AcademicYear academicYear){
        String sortColumn = "id"
        if (applicantStatus == ApplicantStatus.AdmitCard || applicantStatus == ApplicantStatus.Admitted || applicantStatus == ApplicantStatus.Selected) {
            sortColumn = "invoiceDate"
        }
        def c = RegOnlineRegistration.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if(applicantStatus) {
                    eq("applicantStatus", applicantStatus)
                }
                if (className) {
                    eq("className", className)
                }
                if (fromDate && toDate) {
                    between("dateApplication", fromDate, toDate)
                }
            }
            order("cls.sortPosition",CommonUtils.SORT_ORDER_ASC)
            order(sortColumn, CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
    def regOnlineCollectionList(ClassName className, ApplicantStatus applicantStatus, Date fromDate, Date toDate, AcademicYear academicYear){
        def c = RegOnlineRegistration.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if(applicantStatus) {

                    eq("applicantStatus", applicantStatus)
                }
                if (className) {
                    eq("className", className)
                }
                between("invoiceDate", fromDate, toDate)
            }
            order("invoiceDate",CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
}