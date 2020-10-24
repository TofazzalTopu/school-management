package com.grailslab

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.salary.SalDps
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class DpsAmountService {

    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']
    /*static final String[] sortColumns = ['sortOrder','employee', 'date', 'amount', 'designation']*/
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()

        List dataReturns = new ArrayList()
        def c = SalDps.createCriteria()

        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)


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
            results.each { SalDps salDps ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salDps.id, 0: serial, 1: salDps.employee.empID, 2: salDps.employee.name,3: salDps.employee.hrDesignation.name, 4:salDps.totalInsAmount,5: salDps.totalOwnAmount,6: salDps.description, 7:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        String message
        Double totalInsAmount = params.getDouble("totalInsAmount")
        Double totalOwnAmount = params.getDouble("totalOwnAmount")
        SalDps salDps
        if (params.id) {
            salDps = SalDps.get(params.getLong('id'))
            if (!salDps) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            salDps.totalInsAmount = totalInsAmount
            salDps.totalOwnAmount = totalOwnAmount
            salDps.description = params.reason
            salDps.updatedBy = createOrUpdateBy
            message = "Updated Successfully."
        } else {
            Employee employee = Employee.read(params.getLong('employeeId'))
            salDps = new SalDps(employee: employee,
                    totalInsAmount: totalInsAmount,
                    totalOwnAmount: totalOwnAmount,
                    description: params.reason,
                    createdBy: createOrUpdateBy,
                    academicYear: academicYear
            )
            message = "Added Successfully."
        }
        salDps.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result

    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalDps salDpsRecord = SalDps.get(id)
        if (!salDpsRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        salDpsRecord.activeStatus = ActiveStatus.DELETE
        salDpsRecord.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "  Dps Amount deleted successfully.")
        return result
    }

}
