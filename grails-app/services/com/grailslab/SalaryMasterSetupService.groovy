package com.grailslab

import com.grailslab.enums.PayType
import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.salary.*
import grails.web.servlet.mvc.GrailsParameterMap

class SalaryMasterSetupService {
    def schoolService
    SalAdvanceService salAdvanceService
    SalPcService salPcService

    static final String[] sortColumnsStdAtt = ['id', 'yearMonths']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        List dataReturns = new ArrayList()
        def c = SalMaster.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("academicYear", academicYear)
            }
            order(serverParams.sortColumn, serverParams.sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SalMaster salMaster ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salMaster.id, 0: serial, 1: salMaster.academicYear.value, 2:salMaster.yearMonths.value,3: salMaster.salaryStatus.value, 4:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }


  void calAdvanceAmount(SalMaster salMaster, YearMonths yearMonths, AcademicYear academicYear){
        SalAdvance salAdvance
        Double totalAmount
        Double restAmount
        Double paidAmount
      List<SalarySheet> salarySheetList = salarySheetList(salMaster)
      salarySheetList.each {SalarySheet salsheet ->
            totalAmount = 0
            restAmount = 0
            paidAmount = 0
            salAdvance = salAdvanceService.getDueAdvanceByEmployee(salsheet.employee)
            if(salAdvance && salAdvance.outStandingAmount > 0 && salsheet.adsAmount){
                totalAmount = salAdvance.outStandingAmount
                if (totalAmount <= salsheet.adsAmount) {
                    paidAmount = totalAmount
                } else {
                    paidAmount = salsheet.adsAmount
                }
                restAmount = totalAmount - paidAmount
                if(restAmount == 0){
                    salAdvance.paidDate=new Date()
                    salAdvance.payType= PayType.PAID
                }
                salAdvance.outStandingAmount=restAmount
                if (salAdvance.installmentAmount > restAmount) {
                    salAdvance.installmentAmount = restAmount
                }
                salAdvance.save(flush: true)
                new SalAdvanceDetail(
                        salAdvance: salAdvance,
                        academicYear: academicYear,
                        yearMonths: yearMonths,
                        paidAmount: paidAmount,
                        paymentDate: new Date(),
                ).save(flush: true)
            }
        }
    }

  void calPcAmount(SalMaster salMaster, YearMonths yearMonths, AcademicYear academicYear) {
        SalPc salPc
        Double totalAmount
        Double restAmount
        Double paidAmount
        List<SalarySheet> salarySheetList = salarySheetList(salMaster)
        salarySheetList.each { SalarySheet salsheet ->
            totalAmount = 0
            restAmount = 0
            paidAmount = 0
            salPc = salPcService.getPcByEmployee(salsheet.employee)
            if (salPc && salPc.outStandingAmount > 0 && salsheet.pc > 0) {
                totalAmount = salPc.outStandingAmount
                if (totalAmount <= salsheet.pc) {
                    paidAmount = totalAmount
                } else {
                    paidAmount = salsheet.pc
                    restAmount = totalAmount - paidAmount
                }
                if (restAmount == 0) {
                    salPc.paidDate = new Date()
                    salPc.payType = PayType.PAID
                }
                salPc.outStandingAmount = restAmount
                if (salPc.installmentAmount > restAmount) {
                    salPc.installmentAmount = restAmount
                }
                salPc.save()
                new SalPcDetail(salPc: salPc,
                        paidAmount: paidAmount,
                        paymentDate: new Date(), yearMonths: yearMonths,
                        academicYear: academicYear,
                ).save()
            }
        }
  }

    void calDPSAmount(SalMaster salMaster, YearMonths yearMonths, AcademicYear academicYear) {
        Double totalInsAmount
        Double totalOwnAmount
        Double payOwnAmount
        Double dspSchoolAmount
        SalDps salDps
        List<SalarySheet> salarySheetList = salarySheetList(salMaster)
        salarySheetList.each { SalarySheet salaryPrepared ->
            totalInsAmount = 0
            totalOwnAmount = 0
            payOwnAmount = 0
            dspSchoolAmount = 0
            if (salaryPrepared.pfStatus && salaryPrepared.dpsAmount) {
                dspSchoolAmount = salaryPrepared.dpsAmountSchool
                if (salaryPrepared.dpsAmount > dspSchoolAmount) {
                    payOwnAmount = salaryPrepared.dpsAmount - dspSchoolAmount
                }

                salDps = SalDps.findByEmployeeAndActiveStatus(salaryPrepared.employee, ActiveStatus.ACTIVE)
                if (salDps) {
                    totalInsAmount = salDps.totalInsAmount ?: 0
                    totalOwnAmount = salDps.totalOwnAmount ?: 0
                    salDps.totalInsAmount = totalInsAmount + dspSchoolAmount
                    salDps.totalOwnAmount = totalOwnAmount + payOwnAmount
                    salDps.updatedBy = salMaster.createdBy
                    salDps.save(flush: true)
                    new SalDpsDetail(salDps: salDps, transferDate: new Date(), insAmount: dspSchoolAmount, ownAmount: payOwnAmount).save(flush: true)
                } else {
                    salDps = new SalDps(
                            employee: salaryPrepared.employee,
                            AcademicYear: academicYear,
                            totalInsAmount: dspSchoolAmount,
                            totalOwnAmount: payOwnAmount,
                            createdBy: salMaster.createdBy,
                            academicYear :academicYear
                    ).save(flush: true)
                    new SalDpsDetail(salDps: salDps, transferDate: new Date(), insAmount: dspSchoolAmount, ownAmount: payOwnAmount).save(flush: true)
                }
            }
        }
    }

    List<SalarySheet> salarySheetList(SalMaster salMaster) {
        return SalarySheet.findAllBySalMasterAndActiveStatus(salMaster, ActiveStatus.ACTIVE)
    }
    SalMaster getWorkingSalMaster() {
        return SalMaster.findByActiveStatusAndSalaryStatus(ActiveStatus.ACTIVE, SalaryStatus.Prepared)
    }

}
