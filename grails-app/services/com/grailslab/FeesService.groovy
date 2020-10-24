package com.grailslab

import com.grailslab.accounting.ChartOfAccount
import com.grailslab.accounting.Fees

import com.grailslab.enums.FeeAppliedType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class FeesService {

    static final String[] sortColumns = ['id', 'name', 'code']

    LinkedHashMap classFeesList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Fees.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("feeAppliedType", FeeAppliedType.CLASS_STD)

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
            results.each { Fees fees ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: fees.id, 0: serial, 1: fees.name, 2: fees.code, 3: fees.glCode.name, 4: fees?.className?.name, 5: fees.amount.toString(), 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }


    LinkedHashMap commonFees(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Fees.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
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
            results.each { Fees fees ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: fees.id, 0: serial, 1: fees.name, 2: fees.code, 3: fees.glCode.name, 4: 'All Class', 5: fees.amount.toString(), 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def feesListClass(ClassName className) {
        def c = Fees.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("feeAppliedType", FeeAppliedType.CLASS_STD)
                eq("className", className)
            }
            order('feeType', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { Fees fees ->
            dataReturns.add([id: fees.id, name: fees.name, amount: fees.amount, feeType: fees.feeType])
        }
        return dataReturns
    }

    def feesListCommon() {
        def c = Fees.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("feeAppliedType", FeeAppliedType.ALL_STD)
            }
            order('feeType', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { Fees fees ->
            dataReturns.add([id: fees.id, name: fees.name, amount: fees.amount, feeType: fees.feeType])
        }
        return dataReturns
    }

   def feesDDListByStudent(Student student) {
       List dataReturns = new ArrayList()
       def c = Fees.createCriteria()
       def results = c.list() {
           and {
               eq("activeStatus", ActiveStatus.ACTIVE)
//               eq("feeAppliedType", FeeAppliedType.ALL_STD)
           }
           order('feeType', CommonUtils.SORT_ORDER_ASC)
       }
       ChartOfAccount accounts
       results.each { Fees fees ->
           accounts=fees.account
           dataReturns.add([id: fees.id, name: "${accounts.code} - ${accounts.name}"])
       }
       return dataReturns
   }

}
