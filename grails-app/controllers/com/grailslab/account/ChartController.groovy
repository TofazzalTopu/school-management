package com.grailslab.account

import com.grailslab.CommonUtils
import com.grailslab.accounting.ChartOfAccount
import com.grailslab.accounting.Fees
import com.grailslab.command.ChartOfAccountsCommand
import com.grailslab.command.FeesCommand
import com.grailslab.enums.*
import grails.converters.JSON

class ChartController {

    def chartOfAccountService
    def classNameService
    def messageSource
    def schoolService
    def feeItemsService
    def springSecurityService

    def fees() {
        LinkedHashMap resultMap = chartOfAccountService.listAllItems()
        render(view: '/collection/chart/fees', model: [dataReturn: resultMap.results])
    }

    def saveAccount(ChartOfAccountsCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'fees')
            return
        }
        LinkedHashMap result = chartOfAccountService.saveOrUpdateAccount(command)
        String output = result as JSON
        render output
    }



    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ChartOfAccount account = ChartOfAccount.read(id)
        if (!account) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('obj', account)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = chartOfAccountService.delete(id)
        String output = result as JSON
        render output
    }

    def classFees() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        if (!classNameList) {
            render(view: '/collection/chart/classFees', model: [dataReturn: null, totalCount: 0, classNameList: null, classFeesList: null])
            return
        }
        def firstClass = classNameList.first()
        params.put('className', firstClass?.id)
        LinkedHashMap resultMap = chartOfAccountService.feesPaginationList(params, FeeAppliedType.CLASS_STD)
        def classFeesList = chartOfAccountService.feesDropDownList(AccountType.feeMenuItems(), FeeAppliedType.CLASS_STD)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            render(view: '/collection/chart/classFees', model: [dataReturn: null, totalCount: 0, classNameList: classNameList, classFeesList: classFeesList])
            return
        }
        render(view: '/collection/chart/classFees', model: [dataReturn: resultMap.results, totalCount: totalCount, classNameList: classNameList, classFeesList: classFeesList])
    }

    def commonFees() {
        LinkedHashMap resultMap = chartOfAccountService.feesPaginationList(params, FeeAppliedType.ALL_STD)
        def commonFeesList = chartOfAccountService.feesDropDownList(AccountType.feeMenuItems(), FeeAppliedType.ALL_STD)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            render(view: '/collection/chart/commonFees', model: [dataReturn: null, totalCount: 0, commonFeesList: commonFeesList])
            return
        }
        render(view: '/collection/chart/commonFees', model: [dataReturn: resultMap.results, totalCount: totalCount, commonFeesList: commonFeesList])
    }

    def manageActivationFees(Long id) {
        Fees activationFee = Fees.read(id)
        if(!activationFee || activationFee.iterationType!=FeeIterationType.MONTHLY) {
            redirect(action: 'classFees')
            return
        }
        def resultMap = feeItemsService.activationFeesList(activationFee)
        if (!resultMap) {
            render(view: '/collection/chart/manageactivationFee', model: [dataReturn: null])
            return
        }
        render(view: '/collection/chart/manageactivationFee', model: [feeId: activationFee.id, dataReturn: resultMap])
    }

    def activeFeeStatus(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = chartOfAccountService.activeFeeStatus(id)
        String output = result as JSON
        render output
    }

    def listFees() {
        LinkedHashMap gridData
        String result

        if (!params.feeAppliedType) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        FeeAppliedType feeAppliedType = FeeAppliedType.valueOf(params.feeAppliedType.toString())
        if (!feeAppliedType) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }

        LinkedHashMap resultMap = chartOfAccountService.feesPaginationList(params, feeAppliedType)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveFees(FeesCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = chartOfAccountService.saveOrUpdateFees(command)
        String output = result as JSON
        render output
    }

    def editFees(Long id){

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Fees fees = Fees.read(id)

        if (!fees) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, fees)
        outPut = result as JSON
        render outPut
    }

    def deleteFees(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = chartOfAccountService.deleteFees(id)
        String output = result as JSON
        render output

    }


    private Integer dayOrder(WeekDays weekDays) {
        Integer sortPosition =1
        switch (weekDays) {
            case weekDays.SUN:
                sortPosition = 1
                break;
            case weekDays.MON:
                sortPosition =2
                break;
            case weekDays.TUE:
                sortPosition =3
                break;
            case weekDays.WED:
                sortPosition =4
                break;
            case weekDays.THR:
                sortPosition =5
                break;
            case weekDays.FRI:
                sortPosition =6
                break;
            case weekDays.SAT:
                sortPosition =7
                break;

        }
        return sortPosition
    }
}