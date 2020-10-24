package com.grailslab

import com.grailslab.command.SalaryIncrementCommand
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.salary.SalIncrement
import com.grailslab.salary.SalSetup
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SalIncrementService {
    def messageSource
    def schoolService
    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']

    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        } else {
            academicYear = schoolService.workingYear(null)
        }

        YearMonths yearMonths
        if (params.yearMonths) {
            yearMonths = YearMonths.valueOf(params.yearMonths)
        }

        List dataReturns = new ArrayList()
        def c = SalIncrement.createCriteria()

        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
                if (yearMonths) {
                    eq("yearMonths", yearMonths)
                }
            }
            if (serverParams.sSearch) {
                or {
                    ilike('emp.empID', serverParams.sSearch)
                    ilike('emp.name', serverParams.sSearch)
                }

            }
            order(serverParams.sortColumn, serverParams.sSortDir)
        }


        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SalIncrement salIncrement ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salIncrement.id, 0: serial, 1: salIncrement.yearMonths.value, 2: salIncrement.employee.empID,3: salIncrement.employee.name,4: salIncrement.employee.hrDesignation?.name,5: salIncrement.netIncrement,6:salIncrement.incrementStatus, 7:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(SalaryIncrementCommand command, params) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        String message
        SalIncrement salIncrement
        int alreadyUp
        if (params.id) {
            salIncrement = SalIncrement.get(params.id)
            if (!salIncrement || salIncrement.activeStatus != ActiveStatus.ACTIVE) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            alreadyUp = SalIncrement.countByEmployeeAndAcademicYearAndYearMonthsAndIdNotEqualAndActiveStatus(command.employee, command.academicYear, command.yearMonths, salIncrement.id, ActiveStatus.ACTIVE)
            if (alreadyUp > 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "${salIncrement.employee.name} salary already increment this month, you may update.")
                return result
            }
            salIncrement.properties = command.properties
            salIncrement.updatedBy = createOrUpdateBy
            salIncrement.incrementStatus = "Draft"
            message = "Update Successfully"
        } else {
            Employee employee = command.employee
            alreadyUp = SalIncrement.countByEmployeeAndAcademicYearAndYearMonthsAndActiveStatus(employee, command.academicYear, command.yearMonths, ActiveStatus.ACTIVE)
            if (alreadyUp > 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "${employee.name} salary already increment this month, you may update.")
                return result
            }
            salIncrement = new SalIncrement(command.properties)
            salIncrement.createdBy = createOrUpdateBy
            salIncrement.incrementStatus = "Draft"
            message = "Salary increment save Successfully"
        }
        salIncrement.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        SalIncrement salIncrement = SalIncrement.get(id)
        if (!salIncrement) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (salIncrement.incrementStatus == "Confirm") {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Increment already posted to salary. You can't delete now")
            return result
        }
        salIncrement.activeStatus = ActiveStatus.DELETE
        salIncrement.updatedBy = springSecurityService.principal?.username
        salIncrement.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Salary Increment entry deleted successfully.")
        return result
    }

    def confirmIncrement(GrailsParameterMap params){

        LinkedHashMap result = new LinkedHashMap()
        if (!params.academicYear || !params.yearMonths) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select Year and Month")
            return result
        }

        String message
        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
        YearMonths yearMonths = YearMonths.valueOf(params.yearMonths)
        LinkedList allRecord = SalIncrement.findAllByActiveStatusAndAcademicYearAndYearMonthsAndIncrementStatus(ActiveStatus.ACTIVE, academicYear, yearMonths, "Draft")
        SalSetup salSetup
        for(SalIncrement singleRecord : allRecord){
            salSetup = SalSetup.findByEmployeeAndActiveStatus(singleRecord.employee, ActiveStatus.ACTIVE)
            if (salSetup) {
                salSetup.basic = singleRecord.newBasic
                salSetup.houseRent = singleRecord.houseRent
                salSetup.medical = singleRecord.medical
                salSetup.grossSalary = singleRecord.grossSalary
                salSetup.inCharge = singleRecord.inCharge
                salSetup.mobileAllowance = singleRecord.mobileAllowance
                salSetup.others = singleRecord.others
                salSetup.totalSalary = singleRecord.totalSalary
                salSetup.dpsAmountSchool = singleRecord.dpsAmountSchool
                salSetup.updatedBy = springSecurityService.principal?.username
                salSetup.save(flush: true)
            }
            singleRecord.incrementStatus = "Confirm"
            singleRecord.save(flush: true)
        }
        message = "${academicYear} - ${yearMonths} salary increment hasbeen confirm."
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,message)
        return result
    }
}
