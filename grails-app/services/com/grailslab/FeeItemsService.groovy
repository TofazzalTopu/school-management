package com.grailslab

import com.grailslab.accounting.FeeItems
import com.grailslab.accounting.Fees
import com.grailslab.accounting.ItemsDetail
import com.grailslab.enums.FeeAppliedType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class FeeItemsService {
    def schoolService
    static final String[] sortQuickFeeItemColumns = ['code','name']
    LinkedHashMap manageQuickFeeList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        ClassName className = null
        if(params.className){
            className = ClassName.read(params.className.toLong())
        }
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortQuickFeeItemColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = FeeItems.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            if(className){
                eq("className", className)
                eq("feeAppliedType", FeeAppliedType.CLASS_STD)
            }else {
                eq("feeAppliedType", FeeAppliedType.ALL_STD)
            }

            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { FeeItems items ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: items.id,quickFee1: items.quickFee1,quickFee2: items.quickFee2,quickFee3: items.quickFee3, 0: serial, 1:items.code+' - '+items.name, 2: items.amount, 3: items?.discount, 4: items.netPayable, 5: '',6: '',7:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    def activationFeesList(Fees fees) {
        FeeItems feeItems = FeeItems.findByFees(fees)

        def c = ItemsDetail.createCriteria()
        def results = c.list(max: 15, offset: 0) {
            eq("feeItems", feeItems)
            order("activeStatus", "asc")
        }

        List dataReturns = new ArrayList()
        String feeName = "${feeItems.name}"
        results.each { ItemsDetail itemsDetail ->
            dataReturns.add([DT_RowId:itemsDetail.id, feeName: feeName, name: itemsDetail.name, amount: itemsDetail.amount, discount: itemsDetail.discount, netPayable: itemsDetail.netPayable, status: itemsDetail.activeStatus.value])
        }
        return dataReturns
    }
    FeeItems read(Long id) {
        return FeeItems.read(id)
    }
}
