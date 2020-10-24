package com.grailslab

import com.grailslab.enums.PayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.salary.SalPc
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SalPcService {

    def messageSource
    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']
    /*static final String[] sortColumns = ['sortOrder','employee', 'date', 'amount', 'designation']*/
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        PayType payType
        if(params.payType){
            payType = PayType.valueOf(params.payType)
        }

        List dataReturns = new ArrayList()
        def c = SalPc.createCriteria()

        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (payType) {
                    eq("payType", payType)
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
            results.each { SalPc salPc ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salPc.id, 0: serial, 1: salPc.employee.empID, 2: salPc.employee.name,3: salPc.employee.hrDesignation?.name,4: salPc.payableAmount,5: salPc.outStandingAmount, 6:salPc.installmentAmount,7:salPc.payType.key,8:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        String message

        Double installAmount = params.getDouble("installmentAmount")
        Double payableAmount = params.getDouble("payableAmount")
        Double openingAmount = params.getDouble("openingAmount")

        PayType payType = PayType.DUE
        if ((payableAmount - openingAmount) <= 0) {
            payType = PayType.PAID
        }

        if (payType == PayType.DUE && installAmount <= 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please provide installment amount")
            return result
        }

        Employee employee = Employee.read(params.getLong('employeeId'))
        int priviousCount = SalPc.countByEmployeeAndActiveStatus(employee, ActiveStatus.ACTIVE)
        if (priviousCount > 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${employee.name} pc already added")
            return result
        }

        SalPc salPc = new SalPc(employee: employee,
                openingAmount: openingAmount,
                outStandingAmount: payableAmount - openingAmount,
                payableAmount: payableAmount,
                installmentAmount: installAmount,
                description: params.description,
                payType: payType,
                createdBy: createOrUpdateBy,
                academicYear: academicYear
        )
        message = "Added Successfully."
        salPc.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalPc salPcRecord = SalPc.get(id)
        if (!salPcRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        /*def advancePayment = SalAdvanceDetail.findBySalAdvance(salAdvanceRecord)
        if (advancePayment){
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,"Advance installment already paid. You can't delete this. Please contact to admin")
            outPut = result as JSON
            render outPut
            return
        }*/

        salPcRecord.activeStatus = ActiveStatus.DELETE
        salPcRecord.updatedBy = springSecurityService.principal?.username
        salPcRecord.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, " PC Amount deleted successfully.")
        return result
    }
    SalPc getPcByEmployee(Employee employee) {
        return SalPc.findByEmployeeAndPayTypeAndActiveStatus(employee, PayType.DUE, ActiveStatus.ACTIVE)
    }

}
