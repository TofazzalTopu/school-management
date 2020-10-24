package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.AuthorCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class AuthorService {

    def messageSource
    def springSecurityService

    static final String[] sortColumnsAuthor = ['id','name','bengaliName','country']
    LinkedHashMap paginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 1
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsAuthor,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Author.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('bengaliName', sSearch)
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
            results.each { Author author ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: author.id, 0: serial, 1: author.name, 2: author.bengaliName?:"", 3:author.country?:"", 4:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def saveOrUpdate(AuthorCommand command) {

        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        Author author
        String message
        if (command.id) {
            author = Author.get(command.id)
            if (!author) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            author.properties = command.properties
            author.updatedBy = createOrUpdateBy
            author.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Author Updated successfully'
        } else {
            author = new Author(command.properties)
            author.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Author Added successfully'
        }
        author.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Author author = Author.get(id)
        if (!author) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        author.activeStatus = ActiveStatus.INACTIVE
        author.updatedBy = springSecurityService.principal?.username
        author.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Author deleted successfully')
        return result
    }
}
