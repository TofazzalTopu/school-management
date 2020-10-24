package com.grailslab

import com.grailslab.command.SalConfigurationCommand
import com.grailslab.command.SalarySetupCommand
import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.salary.*
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SalarySetUpService {

    def springSecurityService
    def salarySetUpService
    def messageSource
    def salaryMasterSetupService
    def salAdvanceService
    def salPcService

    static final String[] sortColumnsStdAtt = ['id']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        Map serverParams = CommonUtils.getPaginationParams(params, sortColumnsStdAtt)
        int iDisplayStart= serverParams.iDisplayStart.toInteger();
        int  iDisplayLength= serverParams.iDisplayLength.toInteger()
        ActiveStatus activeStatus = ActiveStatus.ACTIVE
        if (params.setupStatus && params.setupStatus == "DELETE") {
            activeStatus = ActiveStatus.DELETE
        }

        List dataReturns = new ArrayList()
        def c = SalSetup.createCriteria()
        def results = c.list(max:iDisplayLength, offset: iDisplayStart) {
            createAlias("employee", "emp")
            and {
                eq("activeStatus", activeStatus)
            }
            if (serverParams.sSearch) {
                or {
                    ilike('emp.empID', serverParams.sSearch)
                    ilike('emp.name',serverParams.sSearch)
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
            results.each { SalSetup salSetup ->
                if (serverParams.sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: salSetup.id, 0: salSetup.employee.empID, 1:salSetup.employee.name,2: salSetup.grossSalary, 3:salSetup.basic ,4: salSetup.houseRent, 5: salSetup.medical,6: salSetup.inCharge,7: salSetup.mobileAllowance,8: salSetup.others,9: salSetup.pfStatus?"Yes":"No", 10 : salSetup.fineOnAbsent ? "Yes" : "No", 11:salSetup.dpsAmount,12: salSetup.fine,13:salSetup.netPayable,14:'' ])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def salarySetupList(){
      def c = SalSetup.createCriteria()
      return c.list() {and {eq("activeStatus", ActiveStatus.ACTIVE)}}
    }

    def salSetupEmpList(HrCategory hrCategory = null) {
        def c = SalSetup.createCriteria()
        def results = c.list() {
            createAlias("employee", "emp")
            and {
                if (hrCategory){
                    eq("emp.hrCategory", hrCategory)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("emp.sortOrder", CommonUtils.SORT_ORDER_ASC)
        }

        return results?.collect {it.employee}
    }

    SalSetup salSetupByEmployee(Long employeeId) {
        Employee employee = Employee.read(employeeId)
        if (!employee) return null
        return SalSetup.findByEmployeeAndActiveStatus(employee, ActiveStatus.ACTIVE)
    }

    def saveFootNote(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        SalMaster master = SalMaster.read(params.id)
        if (!params.id) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return
        }
        master.footNote = params.footNote
        master.updatedBy = springSecurityService.principal?.username
        master.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Foot Note Saved successfully")
        return result
    }

    def masterSetupSave(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        String message
        SalMaster salMaster
        AcademicYear academicYear
        YearMonths yearMonths
        if (params.id) {
            salMaster = SalMaster.get(params.getLong('id'))
            academicYear = salMaster.academicYear
            yearMonths = salMaster.yearMonths
            int deleteFlag = SalarySheet.where { salMaster == salMaster }.deleteAll()
            insertSalarySheet(salMaster, yearMonths, academicYear);
            message = "Salary Regenerated Successfully"
            salMaster.updatedBy = createOrUpdateBy
            salMaster.save(flush: true)
        } else {
            academicYear = AcademicYear.valueOf(params.academicYear)
            yearMonths = YearMonths.valueOf(params.yearMonths)
            SalaryStatus salaryStatus = SalaryStatus.Prepared
            salMaster = SalMaster.findByAcademicYearAndYearMonthsAndActiveStatus(academicYear, yearMonths, ActiveStatus.ACTIVE)
            if (salMaster) {
                message = "Already Added"
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            SalConfiguration salConfig = SalConfiguration.first()
            salMaster = new SalMaster()
            salMaster.yearMonths = yearMonths
            salMaster.salaryStatus = salaryStatus
            salMaster.academicYear = academicYear
            salMaster.pfContribution = salConfig?.pfContribution
            salMaster.pfCalField = salConfig?.pfCalField
            salMaster.createdBy = createOrUpdateBy
            salMaster.academicYear = academicYear
            if (salMaster.save(flush: true)) {
                insertSalarySheet(salMaster, yearMonths, academicYear);
            }
            message = "Salary Generate Successfully"
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def disburseSalary(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()

        if (!params.academicYear || !params.yearMonths) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select Year and Month")
            return result
        }

        String message
        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
        YearMonths yearMonths = YearMonths.valueOf(params.yearMonths)

        SalMaster salMaster = SalMaster.findByAcademicYearAndYearMonthsAndSalaryStatusAndActiveStatus(academicYear, yearMonths, SalaryStatus.Prepared, ActiveStatus.ACTIVE)
        if (!salMaster) {
            message = "No Prepared salary found for disburse. Please Prepare salary first"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }
        salaryMasterSetupService.calAdvanceAmount(salMaster, salMaster.yearMonths, salMaster.academicYear)
        salaryMasterSetupService.calPcAmount(salMaster, salMaster.yearMonths, salMaster.academicYear)
        salaryMasterSetupService.calDPSAmount(salMaster, salMaster.yearMonths, salMaster.academicYear)
        salMaster.salaryStatus = SalaryStatus.Disbursement
        salMaster.updatedBy = springSecurityService.principal?.username
        message = "Salary disbursement Successfully"
        salMaster.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def saveConfig(SalConfigurationCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String message
        SalConfiguration salConfiguration
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        if (params.id) {
            //edit bolck
            salConfiguration = SalConfiguration.get(params.id)
            salConfiguration.properties = command.properties
            message = "Edit Successfully"
        } else {
            salConfiguration = new SalConfiguration(command.properties)
            message = "Save Successfully"
        }

        if (salConfiguration.save(flush: true)) {
            List allSetup = salarySetUpService.salarySetupList()
            for (salSetup in allSetup) {
                if (salSetup.pfStatus) {
                    salSetup.dpsAmountSchool = caldpsSchoolAmount(salConfiguration, salSetup.basic, salSetup.grossSalary, salSetup.totalSalary)
                } else {
                    salSetup.dpsAmountSchool = 0
                }
            }
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, salConfiguration)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalSetup salSetup = SalSetup.get(id)
        if (!salSetup) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (salSetup.activeStatus == ActiveStatus.DELETE) {
            salSetup.activeStatus = ActiveStatus.ACTIVE
        } else {
            salSetup.activeStatus = ActiveStatus.DELETE
        }
        salSetup.updatedBy = springSecurityService.principal?.username
        salSetup.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Salary Setup Active/Inactive successfully.")
        return result
    }

    def saveOrUpdate(SalarySetupCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        String message
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        SalConfiguration salConfiguration = SalConfiguration.first()
        if (!salConfiguration) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Salary Configuration is not saved. Please add that first")
            return result
        }

        SalSetup salSetup
        if (params.id) {
            //edit block
            salSetup = SalSetup.get(params.id)
            salSetup.properties = command.properties
            salSetup.updatedBy = createOrUpdateBy
            message = "Update Successfully"
        } else {
            salSetup = SalSetup.findByEmployeeAndActiveStatus(command.employee, ActiveStatus.ACTIVE)
            if (salSetup) {
                message = "Already Added, You can Edit It."
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            salSetup = new SalSetup(command.properties)
            salSetup.createdBy = createOrUpdateBy
            message = "Save Successfully"
        }

        if (salSetup.pfStatus) {
            salSetup.dpsAmountSchool = caldpsSchoolAmount(salConfiguration, salSetup.basic, salSetup.grossSalary, salSetup.totalSalary)
        } else {
            salSetup.dpsAmountSchool = 0
        }
        salSetup.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def masterDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SalMaster salMaster = SalMaster.get(id)
        if (!salMaster) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (salMaster.salaryStatus == SalaryStatus.Disbursement) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Salary Already disbursed.")
            return result
        }
        salMaster.activeStatus=ActiveStatus.DELETE
        salMaster.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Master Setup deleted successfully.")
        return result
    }

    private void insertSalarySheet(SalMaster salMaster, YearMonths yearMonths, AcademicYear academicYear) {
        int incrementCount = SalIncrement.countByAcademicYearAndYearMonthsAndIncrementStatus(academicYear, yearMonths, "Confirm")
        SalConfiguration salConfig = SalConfiguration.first()
        List allSetup = salarySetUpService.salarySetupList()
        SalarySheet salarySheet
        for (salSetup in allSetup) {
            salarySheet = new SalarySheet(salMaster: salMaster, yearMonths: yearMonths, academicYear: academicYear)
            if (incrementCount > 0) {
                salarySheet.increment = calIncrementAmount(salSetup.employee, yearMonths, academicYear)
            }
            salarySheet.employee = salSetup.employee
            salarySheet.grossSalary = salSetup.grossSalary
            salarySheet.totalSalary = salSetup.totalSalary
            salarySheet.basic = salSetup.basic
            salarySheet.houseRent = salSetup.houseRent
            salarySheet.medical = salSetup.medical
            salarySheet.inCharge = salSetup.inCharge
            salarySheet.mobileAllowance = salSetup.mobileAllowance
            salarySheet.others = salSetup.others
            salarySheet.area = calAreaAmount(salSetup.employee, yearMonths, academicYear)
            salarySheet.pfStatus = salSetup.pfStatus
            calLoanAdvanceAmount(salSetup.employee, salarySheet)
            salarySheet.dpsAmount = salSetup.dpsAmount
            if (salSetup.pfStatus) {
                salarySheet.dpsAmountSchool = salSetup.dpsAmountSchool
            } else {
                salarySheet.dpsAmountSchool = 0
            }
            calLateAndULFine(salConfig, salSetup.employee, yearMonths, academicYear, salarySheet, salSetup.fineOnAbsent)
            calExtraClassAndAmount(salSetup.employee, yearMonths, academicYear, salarySheet)
            salarySheet.fine = salSetup.fine
            calPcAmount(salSetup.employee, salarySheet)
            salarySheet.netPayable = salSetup.netPayable
            salarySheet.createdBy = salMaster.createdBy
            salarySheet.academicYear = salMaster.academicYear
            salarySheet.save()
        }

    }

    private def calLateAndULFine(SalConfiguration salConfig, Employee employee, YearMonths yearMonths, AcademicYear academicYear, SalarySheet salarySheet, Boolean fineOnAbsent) {
        Double perdayAmount = 0
        if (fineOnAbsent == false) {
            salarySheet.lateDays = 0
            salarySheet.lateFine = 0
            salarySheet.ulDays = 0
            salarySheet.ulFine = 0
            return
        }

        SalAttendance salAttendance = SalAttendance.findByEmployeeAndYearMonthsAndAcademicYearAndActiveStatus(employee, yearMonths, academicYear, ActiveStatus.ACTIVE)
        if (!salAttendance) {
            salarySheet.lateDays = 0
            salarySheet.lateFine = 0
            salarySheet.ulDays = 0
            salarySheet.ulFine = 0
            return
        }

        int leaveDays = salAttendance.leaveDays > 0 ? salAttendance.leaveDays : 0
        int lateFineDays = salAttendance.lateDays > 0 ? (salAttendance.lateDays / salConfig.lateFineForDays) : 0
        int ulFinedays = salAttendance.absentDays > 0 ? salAttendance.absentDays : 0

        if (salConfig.pfCalField.equals("basic") && salarySheet.basic > 0) {
            perdayAmount = (salarySheet.basic) / 30
        } else if (salConfig.pfCalField.equals("totalSalary") && salarySheet.totalSalary > 0) {
            perdayAmount = (salarySheet.totalSalary) / 30
        } else if (salarySheet.grossSalary > 0) {
            perdayAmount = (salarySheet.grossSalary) / 30
        }
        perdayAmount = perdayAmount.round()
        salarySheet.lateDays = salAttendance.lateDays
        salarySheet.lateFine = (perdayAmount * lateFineDays)
        salarySheet.ulDays = ulFinedays
        salarySheet.ulFine = (perdayAmount * ulFinedays)
    }

    private Double calIncrementAmount(Employee employee, YearMonths yearMonths, AcademicYear academicYear) {
        Double increment = 0
        SalIncrement salIncrement = SalIncrement.findByEmployeeAndActiveStatusAndAcademicYearAndYearMonthsAndIncrementStatus(employee, ActiveStatus.ACTIVE, academicYear, yearMonths, "Confirm")
        if (salIncrement && salIncrement.netIncrement > 0) {
            increment = salIncrement.netIncrement
        }
        return increment
    }

    private void calPcAmount(Employee employee, SalarySheet salarySheet) {
       SalPc salPc = salPcService.getPcByEmployee(employee)
        if (salPc) {
            salarySheet.pc = salPc.installmentAmount
        } else {
            salarySheet.pc = 0.0
        }
    }

    private void calLoanAdvanceAmount(Employee employee, SalarySheet salarySheet) {
       SalAdvance advance = salAdvanceService.getDueAdvanceByEmployee(employee)
        if (advance) {
            salarySheet.adStatus = true
            salarySheet.adsAmount = advance.installmentAmount
        } else {
            salarySheet.adStatus = false
            salarySheet.adsAmount = 0
        }
    }
    private void calExtraClassAndAmount(Employee employee, YearMonths yearMonths, AcademicYear academicYear, SalarySheet salarySheet) {
        SalExtraClass salExtraClass = SalExtraClass.findByEmployeeAndYearMonthsAndAcademicYearAndActiveStatus(employee, yearMonths, academicYear, ActiveStatus.ACTIVE)
        if (salExtraClass) {
            salarySheet.extraClass = salExtraClass.numOfClass
            salarySheet.extraClassAmount = salExtraClass.amount
        } else {
            salarySheet.extraClass = 0
            salarySheet.extraClassAmount = 0
        }
    }

    private Double calAreaAmount(Employee employee, YearMonths yearMonths, AcademicYear academicYear) {
        SalArea salArea = SalArea.findByEmployeeAndYearMonthsAndAcademicYearAndActiveStatus(employee, yearMonths, academicYear, ActiveStatus.ACTIVE)
        if (salArea) {
            return salArea.amount
        }
        return 0.0
    }


    private Double caldpsSchoolAmount(SalConfiguration salConfig, Double basic, Double gross, Double totalSalary) {
        Double schoolAmount = 0
        if (salConfig.pfCalField.equals("basic") && basic > 0) {
            schoolAmount = (basic * salConfig.pfContribution) / 100
        } else if (salConfig.pfCalField.equals("totalSalary") && totalSalary > 0) {
            schoolAmount = (totalSalary * salConfig.pfContribution) / 100
        } else if (salConfig.pfCalField.equals("grossSalary") && gross > 0) {
            schoolAmount = (gross * salConfig.pfContribution) / 100
        }
        return schoolAmount.round()
    }

}
