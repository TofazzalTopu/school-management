package com.grailslab.salary

import com.grailslab.CommonUtils
import com.grailslab.command.SalaryBonusCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.SalaryStatus
import com.grailslab.gschoolcore.AcademicYear
import grails.converters.JSON

class SalaryBonusController {

    def salaryBonusService
    def schoolService

    def index() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        render(view: '/salary/bonusSetting', model: [academicYearList:academicYearList, workingYear:academicYear])
    }

    def save(SalaryBonusCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = salaryBonusService.saveOrUpdate(command)
        String outPut = result as JSON
        render outPut
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = salaryBonusService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def disbursement(Long id) {
        LinkedHashMap result = salaryBonusService.disbursement(id)
        String outPut = result as JSON
        render outPut
    }

    def regenerate(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (id) {
            BonusMaster bonusMaster = BonusMaster.get(id)
            if (bonusMaster && bonusMaster.salaryStatus == SalaryStatus.Draft) {
                int deleteFlag = BonusSheet.where { bonusMaster == bonusMaster }.deleteAll()
                salaryBonusService.insertBonusSheet(bonusMaster)
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Bonus regenerate successfully.")
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        LinkedHashMap result = salaryBonusService.delete(id)
        String outPut = result as JSON
        render outPut
    }
}
