package com.grailslab

import com.grailslab.accounting.Invoice
import com.grailslab.enums.AccountType
import com.grailslab.enums.PaymentType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.LocalDate

@Transactional
class BkashService {
    def schoolService

    static final String[] sortColumns = ['id','std.studentID','amount','discount','payment','invoiceDate']
    LinkedHashMap manageCollectionPaginationList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        ClassName className = null
        if(params.className){
            className = ClassName.read(params.className.toLong())
        }

        LocalDate nowDate = new LocalDate();
        Date fromDate
        Date toDate

        if (params.startDate) {
            fromDate = CommonUtils.getUiPickerDate(params.startDate)
        } else {
            fromDate = nowDate.toDateTimeAtStartOfDay().toDate()
        }
        if (params.endDate) {
            toDate = CommonUtils.getUiPickerDate(params.endDate)
        } else {
            toDate = nowDate.toDateTimeAtStartOfDay().toDate()
        }

        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Invoice.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student','std')
            and {
                eq("accountType",AccountType.INCOME)
                if(params.paymentStatus){
                    if (params.paymentStatus == 'verified'){
                        eq("activeStatus",ActiveStatus.ACTIVE)
                    } else {
                        eq("activeStatus",ActiveStatus.BKASH)
                    }
                } else {
                    eq("activeStatus",ActiveStatus.ACTIVE)
                }
                eq("paymentType",PaymentType.BKASH)
                if(className){
                    eq("std.className",className)
                }
                between('invoiceDate', fromDate, toDate)
            }
            if (sSearch) {
                or {
                    ilike('invoiceNo', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            String stdName
            Student student
            results.each { Invoice invoice ->
                student = invoice?.student
                stdName = "${student?.studentID} - ${student?.name}"
                dataReturns.add([DT_RowId: invoice.id,invoiceNo:invoice.invoiceNo, 0: invoice.invoiceNo,1:stdName, 2:"${student?.className?.name} - ${student?.section?.name}", 3: invoice.amount, 4: invoice.discount, 5: invoice.payment, 6:CommonUtils.getUiDateStr(invoice.invoiceDate), 7: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

}
