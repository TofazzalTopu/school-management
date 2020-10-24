package com.grailslab

import com.grailslab.accounting.InvoiceDay
import com.grailslab.command.InvoiceDayCommand
import com.grailslab.enums.InvoiceDayType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
@Transactional
class InvoiceDayService {

    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['id','invoiceDate','invoiceDayType','createdBy','lastUpdated','updatedBy']
    LinkedHashMap paginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)

        List dataReturns = new ArrayList()
        def c = InvoiceDay.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('createdBy', sSearch)
                    ilike('updatedBy', sSearch)
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
            results.each { InvoiceDay obj ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: obj.id, 0: serial, 1: CommonUtils.getUiDateStr(obj.invoiceDate), 2:obj.invoiceDayType?.value, 3: obj.createdBy, 4:CommonUtils.getUiDateStr(obj.lastUpdated), 5: obj.updatedBy, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(InvoiceDayCommand command) {
        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal.academicYear

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        InvoiceDay invoiceDay
        String message
        if (command.id) {
            invoiceDay = InvoiceDay.get(command.id)
            if (!invoiceDay) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            invoiceDay.properties = command.properties
            invoiceDay.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Invoice Date Updated successfully'
        } else {
            invoiceDay = new InvoiceDay(command.properties)
            invoiceDay.createdBy = createOrUpdateBy
            invoiceDay.academicYear = academicYear
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Invoice Date Added successfully'
        }
        invoiceDay.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def reOpen(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        InvoiceDay invoiceDay = InvoiceDay.get(id)
        if (!invoiceDay) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        String message = "Can't process your action. Please contact to admin"
        if (invoiceDay.invoiceDayType.equals(InvoiceDayType.OPEN)) {
            //logic to close an Open Day
            invoiceDay.invoiceDayType = InvoiceDayType.CLOSED
            message = "Invoice Day CLOSED Successfully"
        } else if (invoiceDay.invoiceDayType.equals(InvoiceDayType.REOPEN)) {
            //logic to close an Reopen Day
            invoiceDay.invoiceDayType = InvoiceDayType.CLOSED
            message = "Invoice Day CLOSED Successfully"
        } else if (invoiceDay.invoiceDayType.equals(InvoiceDayType.CLOSED)) {
            //logic to reopen an closed Day
            invoiceDay.invoiceDayType = InvoiceDayType.REOPEN
            message = "Invoice Day REOPEN Successfully"
        }
        invoiceDay.updatedBy = springSecurityService.principal?.username
        invoiceDay.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        InvoiceDay invoiceDay = InvoiceDay.get(id)
        if (!invoiceDay) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (invoiceDay.activeStatus.equals(ActiveStatus.INACTIVE)) {
            invoiceDay.activeStatus = ActiveStatus.ACTIVE
        } else {
            invoiceDay.activeStatus = ActiveStatus.INACTIVE
        }
        invoiceDay.updatedBy = springSecurityService.principal?.username
        invoiceDay.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Invoice Date Deleted Successfully')
        return result
    }
}
