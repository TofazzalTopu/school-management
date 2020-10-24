package com.grailslab

import com.grailslab.enums.StudentStatus
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class RegistrationService {

    def springSecurityService
    SchoolService schoolService
    def grailsApplication
    def sequenceGeneratorService

    static final String[] sortColumns = ['id','studentID','name','fathersName','mobile','cardNo']
    LinkedHashMap registrationPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX

        String sSearch = params.sSearch ? params.sSearch : null
        String rStatus = "NEW"
        if(params.rStatus){
            rStatus = params.rStatus
        }
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Registration.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                if(rStatus=="ADMISSION"){
                    eq("studentStatus", StudentStatus.ADMISSION)
                }else if(rStatus=="TC"){
                    eq("studentStatus", StudentStatus.TC)
                }else if(rStatus=="DELETED"){
                    eq("studentStatus", StudentStatus.DELETED)
                } else {
                    eq("studentStatus", StudentStatus.NEW)
                }
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('fathersName', sSearch)
                    ilike('mobile', sSearch)
                    ilike('cardNo', sSearch)

                }
            }
            order(sortColumn, sSortDir)
//            order('studentID', CommonUtils.SORT_ORDER_DESC)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            String classSection
            String religion
            results.each { Registration registration ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }

                classSection = "N/A"
                religion = registration.religion?.value
                if ( registration.gender) {
                    religion += " - "+registration.gender
                }
                dataReturns.add([DT_RowId: registration.id,studentStatus: registration.studentStatus?.key, 0: serial,1: registration.studentID, 2: registration.name, 3: registration.fathersName, 4: registration.mobile, 5:religion, 6: classSection, 7:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def newRegistrationDropDownList(){
        List dataReturns = new ArrayList()
        def c = Registration.createCriteria()
        def results = c.list(max: 100, offset: 0) {
            and {
                eq("studentStatus", StudentStatus.NEW)
            }
            order('studentID', CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Registration registration ->
            dataReturns.add([id: registration.id, name: "${registration.studentID} - ${registration.name} - ${registration.fathersName}"])
        }
        return dataReturns
    }

    def newRegistrationDropDownList2(String sSearch){
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        List dataReturns = new ArrayList()
        def c = Registration.createCriteria()
        def results = c.list(max: 20, offset: 0) {
            and {
                eq("studentStatus", StudentStatus.NEW)
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('fathersName', sSearch)
                    ilike('mobile', sSearch)
                }
            }
            order('studentID', CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Registration registration ->
            dataReturns.add([id: registration.id, name: "${registration.studentID} - ${registration.name} - ${registration.fathersName} - ${registration.mobile}"])
        }registration
        return dataReturns
    }
    String nextStudentNo(){
        Integer numOfDigit = grailsApplication.config.getProperty("registration.stdid.numofdigit", Integer)
        String prefix = grailsApplication.config.getProperty("registration.stdid.prefix")
        if (numOfDigit == 6) {
            return sequenceGeneratorService.generateTenant("Registration.studentId6DigitSequence", 1, [regCode: prefix], true)
        } else {
            return sequenceGeneratorService.generateTenant("Registration.studentId4DigitSequence", 1, [regCode: prefix], true)
        }
    }

    def delete(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        Registration registration = Registration.get(id)
        if (!registration) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        Student student = Student.findByRegistrationAndStudentStatusAndAcademicYearInList(registration, StudentStatus.NEW, schoolService.workingYears())
        if (student) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Student already admitted. Please delete the admission first.")
            return result
        }
        registration.updatedBy = createOrUpdateBy
        registration.studentStatus = StudentStatus.DELETED
        registration.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Registration Inactive successfully.")
        return result
    }

    def doActive(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        Registration registration = Registration.get(id)
        if (!registration) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        registration.studentStatus = StudentStatus.NEW
        registration.updatedBy = createOrUpdateBy
        registration.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Registration Activate successfully.")
        return result
    }

}
