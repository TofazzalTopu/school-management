package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.BookPublisherCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class PublisherService {

    def springSecurityService
    def messageSource

    static final String[] sortColumnsAuthor = ['id', 'name', 'bengaliName', 'address', 'country']

    LinkedHashMap publisherList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 1
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsAuthor, iSortingCol)
        List dataReturns = new ArrayList()
        def c = BookPublisher.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('bengaliName', sSearch)
                    ilike('address', sSearch)
                    ilike('country', sSearch)
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
            results.each { BookPublisher publisher ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: publisher.id, 0: serial, 1: publisher.name, 2: publisher.bengaliName ?: "", 3: publisher.address ?: "", 4: publisher.country ?: "", 5: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(BookPublisherCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        BookPublisher publisher
        String message
        if (command.id) {
            publisher = BookPublisher.get(command.id)
            if (!publisher) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            publisher.properties = command.properties
            publisher.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'BookDetails Publisher Updated successfully'
        } else {
            publisher = new BookPublisher(command.properties)
            publisher.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'BookDetails Publisher Added successfully'
        }
        publisher.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        result.put("publisherId", publisher.id)
        result.put("publisherName", publisher.name)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BookPublisher publisher = BookPublisher.get(id)
        if (!publisher) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        publisher.activeStatus = ActiveStatus.INACTIVE
        publisher.updatedBy = springSecurityService.principal?.username
        publisher.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Publisher deleted successfully')
        return result
    }
}
