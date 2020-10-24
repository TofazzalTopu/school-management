package com.grailslab

import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.salary.SalAdvance
import com.grailslab.salary.SalAdvanceDetail
import com.grailslab.salary.SalSetup
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SalAdvanceService {

    def messageSource
    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']
    /*static final String[] sortColumns = ['sortOrder','employee', 'date', 'amount', 'designation']*/
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()

        List dataReturns = new ArrayList()
        def c = SalAdvance.createCriteria()

        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("payType", PayType.DUE)
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
            results.each { SalAdvance salAdvance ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salAdvance.id, 0: serial, 1: salAdvance.employee.empID, 2: salAdvance.employee.name, 3: salAdvance.employee.hrDesignation.name, 4:salAdvance.currAmount, 5: salAdvance.currAmount - salAdvance.outStandingAmount, 6: salAdvance.outStandingAmount, 7: salAdvance.installmentAmount, 8: salAdvance.description, 9:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    SalAdvance getDueAdvanceByEmployee(Employee employee) {
        return SalAdvance.findByEmployeeAndPayTypeAndActiveStatus(employee, PayType.DUE, ActiveStatus.ACTIVE)
    }

    def saveOrUpdate(GrailsParameterMap params){

        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        String message
        Date advanceDate
        if (params.advanceDate) {
            advanceDate = Date.parse('dd/MM/yyyy', params.advanceDate)
        } else {
            advanceDate = new Date()
        }
        Double currentAmount = params.getDouble("currentAmount")
        Double installAmount = params.getDouble("installmentAmount")
        SalAdvance salAdvance
        Employee employee

        if (params.objId) {
            salAdvance = SalAdvance.get(params.getLong('objId'))

            if (!salAdvance) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            employee = salAdvance.employee
            if (params.outStandingAmount) {
                Double outStandingAmount = salAdvance.outStandingAmount
                salAdvance.payType = PayType.PAID
                salAdvance.activeStatus = ActiveStatus.INACTIVE
                SalAdvance newAdvance = new SalAdvance(
                        employee: salAdvance.employee,
                        outStandingAmount: currentAmount + outStandingAmount,
                        currAmount: currentAmount + outStandingAmount,
                        installmentAmount: installAmount,
                        advanceDate: advanceDate,
                        description: params.description,
                        payType: PayType.DUE,
                        createdBy: createOrUpdateBy,
                        academicYear: academicYear
                )
                newAdvance.save(flush: true)
            } else {
                Double previousAmount = salAdvance.currAmount
                Double amountDiffer = currentAmount - previousAmount
                salAdvance.outStandingAmount = salAdvance.outStandingAmount + amountDiffer
                salAdvance.currAmount = currentAmount
                salAdvance.advanceDate = advanceDate
                salAdvance.description = params.description
                salAdvance.updatedBy = createOrUpdateBy
                message = "Updated Successfully."
            }
        } else {
            employee = Employee.read(params.getLong('employeeId'))
            SalAdvance previousLoan = SalAdvance.findByEmployeeAndPayTypeAndActiveStatus(employee, PayType.DUE, ActiveStatus.ACTIVE)
            if (previousLoan) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "${employee.name} already have an Advance.")
                return result
            }
            salAdvance = new SalAdvance(
                    employee: employee,
                    outStandingAmount: currentAmount,
                    currAmount: currentAmount, installmentAmount: installAmount,
                    advanceDate: advanceDate,
                    description: params.description,
                    payType: PayType.DUE,
                    createdBy: createOrUpdateBy,
                    academicYear: academicYear
            )
            message = "Added Successfully."
        }

        if (!salAdvance.save(flush: true)) {
            def errorList = salAdvance?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,errorList.join(","))
            return result
        }

        SalSetup salSetup = SalSetup.findByEmployeeAndActiveStatus(employee, ActiveStatus.ACTIVE)
        if(salSetup){
            salSetup.adStatus = true
            salSetup.adsAmount = installAmount
            salSetup.save(flush: true)
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result

    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalAdvance salAdvanceRecord = SalAdvance.get(id)
        if (!salAdvanceRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        def advancePayment = SalAdvanceDetail.findBySalAdvance(salAdvanceRecord)
        if (advancePayment) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Advance installment already paid. You can't delete this. Please contact to admin")
            return result
        }

        salAdvanceRecord.activeStatus = ActiveStatus.DELETE
        salAdvanceRecord.updatedBy = springSecurityService.principal?.username
        if (salAdvanceRecord.save(flush: true)) {
            SalSetup salSetup = SalSetup.findByEmployeeAndActiveStatus(salAdvanceRecord.employee, ActiveStatus.ACTIVE)
            if (salSetup) {
                salSetup.adStatus = false
                salSetup.adsAmount = 0
                salSetup.save(flush: true)
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, " Amount deleted successfully.")
        return result
    }

}
