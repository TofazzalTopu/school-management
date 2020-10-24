package com.grailslab

import com.grailslab.command.SalaryBonusCommand
import com.grailslab.enums.SalaryStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.salary.BonusMaster
import com.grailslab.salary.BonusSheet
import com.grailslab.salary.SalSetup
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SalaryBonusService {

    def springSecurityService
    def messageSource
    def employeeService
    def salarySetUpService
    def schoolService

    static final String[] sortColumnsStdAtt = ['id']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        List dataReturns = new ArrayList()
        def c = BonusMaster.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
            }
            if (serverParams.sSearch) {
                or {
                    ilike('festivalName', serverParams.sSearch)
                    ilike('basedOn', serverParams.sSearch)
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
            results.each { BonusMaster bonusMaster ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: bonusMaster.id, 0: serial, 1: bonusMaster.festivalName, 2:bonusMaster.joinBefore,3: bonusMaster.basedOn, 4:bonusMaster.basicPercentage, 5: bonusMaster.grossPercentage, 6:bonusMaster.salaryStatus.value, 7:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(SalaryBonusCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        BonusMaster bonusMaster = new BonusMaster(command.properties)
        bonusMaster.salaryStatus = SalaryStatus.Draft
        bonusMaster.createdBy = createOrUpdateBy
        message = "Bonus save Successfully"
        if (bonusMaster.save(flush: true)) {
            insertBonusSheet(bonusMaster);
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def disbursement(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        if (id) {
            BonusMaster bonusMaster = BonusMaster.get(id)
            if (bonusMaster && bonusMaster.salaryStatus == SalaryStatus.Draft) {
                bonusMaster.salaryStatus = SalaryStatus.Disbursement.key
                bonusMaster.updatedBy = springSecurityService.principal?.username
                bonusMaster.save(flush: true)
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Bonus disbursement successfully.")
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BonusMaster bonusMaster = BonusMaster.get(id)
        if (!bonusMaster) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        bonusMaster.activeStatus = ActiveStatus.DELETE
        bonusMaster.updatedBy = springSecurityService.principal?.username
        bonusMaster.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Bonus deleted successfully.")
        return result
    }

    void insertBonusSheet(BonusMaster bonusMaster) {
        Date joinBefore = bonusMaster.joinBefore
        String basedOn = bonusMaster.basedOn
        Double basicPercentage = bonusMaster.basicPercentage ?:0
        Double grossPercentage = bonusMaster.grossPercentage ?:0
        def joinedBeforeList = employeeService.employeeListByJoinedBefore(joinBefore)
        SalSetup salSetup
        Double baseAmount = 0
        Double bonusAmount = 0
        for (employee in joinedBeforeList) {
            salSetup = SalSetup.findByEmployeeAndActiveStatus(employee, ActiveStatus.ACTIVE)
            if (salSetup) {
                if (basedOn == "grossSalary" && salSetup.grossSalary) {
                    baseAmount = salSetup.grossSalary
                    bonusAmount = baseAmount * grossPercentage * 0.01
                } else if (basedOn == "totalSalary" && salSetup.totalSalary) {
                    baseAmount = salSetup.totalSalary
                    bonusAmount = baseAmount * grossPercentage * 0.01
                } else if (basedOn == "basic" && salSetup.basic) {
                    baseAmount = salSetup.basic
                    bonusAmount = baseAmount * basicPercentage * 0.01
                } else if (basedOn == "basicOrGross") {
                    if (salSetup.basic) {
                        baseAmount = salSetup.basic
                        bonusAmount = baseAmount * basicPercentage * 0.01
                    } else if(salSetup.grossSalary) {
                        baseAmount = salSetup.grossSalary
                        bonusAmount = baseAmount * grossPercentage * 0.01
                    } else {
                        baseAmount = 0
                        bonusAmount = 0
                    }
                } else {
                    baseAmount = 0
                    bonusAmount = 0
                }
                new BonusSheet(bonusMaster: bonusMaster, employee: employee, baseAmount: baseAmount, amount: bonusAmount, createdBy: bonusMaster.createdBy, academicYear: bonusMaster.academicYear).save()
            }
        }
    }
}
