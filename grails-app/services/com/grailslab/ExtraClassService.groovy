package com.grailslab

import com.grailslab.command.ExtraClassCommand
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.salary.SalExtraClass
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ExtraClassService {

    def schoolService
    def classNameService
    def messageSource
    def springSecurityService

    static final String[] sortColumnsStdAtt = ['id']
        LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
            YearMonths yearMonths  = YearMonths.JANUARY
            if(params.yearMonths){
                yearMonths = YearMonths.valueOf(params.yearMonths)
            }

            List dataReturns = new ArrayList()
        def c = SalExtraClass.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'emp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
                eq("academicYear", academicYear)
                eq("yearMonths",yearMonths)

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
            results.each { SalExtraClass salExtraClass ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salExtraClass.id, 0: serial, 1: salExtraClass.employee.empID, 2: salExtraClass.employee.name,3:salExtraClass.numOfClass ,4: salExtraClass.rate, 5: salExtraClass.amount, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(ExtraClassCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        SalExtraClass extraClass
        if (params.id) {
            //edit block
            extraClass = SalExtraClass.get(params.id)
            extraClass.properties['numOfClass', 'rate'] = command.properties
            extraClass.updatedBy = createOrUpdateBy
            message = "Edit Successfully"
        } else {
            extraClass = SalExtraClass.findByAcademicYearAndYearMonthsAndEmployeeAndActiveStatus(command.academicYear, command.yearMonths, command.employee, ActiveStatus.ACTIVE)
            if (extraClass) {
                message = "Already Added, You can Edit It."
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            extraClass = new SalExtraClass(command.properties)
            extraClass.createdBy = createOrUpdateBy
            extraClass.academicYear = academicYear
            message = "Save Successfully"
        }

        extraClass.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalExtraClass extraClassRecord = SalExtraClass.get(id)
        if (!extraClassRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        extraClassRecord.activeStatus = ActiveStatus.DELETE
        extraClassRecord.updatedBy = springSecurityService.principal?.username
        extraClassRecord.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "ExtraClass deleted successfully.")
        return result
    }

}
