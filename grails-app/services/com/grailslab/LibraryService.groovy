package com.grailslab

import com.grailslab.command.BookDetailsCommand
import com.grailslab.enums.BookStockStatus
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.library.*
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class LibraryService {

    def messageSource
    def libraryService
    def schoolService
    def sectionService
    def bookStockService
    def springSecurityService

    static final String[] sortColumnsBook = ['id','name','at.name','ct.name','bp.name']
    LinkedHashMap bookDetailPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        BookCategory bookCategory
        if (params.categoryName) {
            bookCategory = BookCategory.read(params.getLong("categoryName"))
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsBook,iSortingCol)
        List dataReturns = new ArrayList()
        def c = BookDetails.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('category', 'ct')
            createAlias('author', 'at')
            createAlias('bookPublisher', 'bp')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (bookCategory) {
                    eq("category",bookCategory)
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('at.name', sSearch)
                    ilike('ct.name', sSearch)
                    ilike('bp.name', sSearch)
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
            results.each { BookDetails bookDetails ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: bookDetails.id, 0: serial, 1: bookDetails.name, 2:bookDetails.author.name,3: bookDetails.bookPublisher?.name?:"-", 4:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def allBookCategoryDropDownList(){
        def c = BookCategory.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
        }
        List dataReturns = new ArrayList()
        results.each { BookCategory category ->
            dataReturns.add([id: category.id, name: category.name])
        }
        return dataReturns
    }

    def bookCategoryList(){
        def c = BookCategory.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
        }
        return results
    }

    def bookPublisherList(){
        def c = BookPublisher.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
        }
        return results
    }

    def bookAuthorList(){
        def c = Author.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
        }
        return results
    }

    def allBookLanguage(){
        List dataReturns = new ArrayList()
        def results = BookDetails.withCriteria() {
            projections {
                distinct("language")
            }
        }
        for (obj in results) {
            dataReturns.add([name: obj])
        }
        return dataReturns
    }

    def allBookSource(){
        List dataReturns = new ArrayList()
        def results = Book.withCriteria() {
            projections {
                distinct("source")
            }
        }
        for (obj in results) {
            dataReturns.add([name: obj])
        }
        return dataReturns
    }

    def authorTypeAheadList(GrailsParameterMap parameterMap){
        String sSearch = parameterMap.q
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Author.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('country', sSearch)
                }
            }
            order("name",CommonUtils.SORT_ORDER_ASC)
        }
        String name
        for (obj in results) {
            name = obj.name
            if (obj.country) {
                name = name+", "+ obj.country
            }
            dataReturns.add([id: obj.id, name: name])
        }
        return dataReturns
    }

    def publisherTypeAheadList(GrailsParameterMap parameterMap){
        String sSearch = parameterMap.q
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = BookPublisher.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('country', sSearch)
                }
            }
            order("name",CommonUtils.SORT_ORDER_ASC)
        }
        String name
        for (obj in results) {
            name = obj.name
            if (obj.country) {
                name = name+", "+ obj.country
            }
            dataReturns.add([id: obj.id, name: name])
        }
        return dataReturns
    }

    int bookCount(BookCategory bookCategory, BookPublisher publisher, Author author, String language, BookStockStatus stockStatus) {
        def totalBook = Book.createCriteria().count() {
            createAlias('bookDetails', 'bd')
            and {
                eq("bd.activeStatus", ActiveStatus.ACTIVE)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (stockStatus){
                    eq("stockStatus", stockStatus)
                }
                if (bookCategory){
                    eq("bd.category", bookCategory)
                }
                if (author){
                    eq("bd.author", author)
                }
                if (publisher){
                    eq("bd.bookPublisher", publisher)
                }
                if (language){
                    eq("bd.language", language)
                }
            }
        }
        return totalBook
    }

    def bookDetailSave(BookDetailsCommand command) {
        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        BookDetails bookDetails
        String message
        if (command.id) {
            bookDetails = BookDetails.get(command.id)
            if (!bookDetails) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            bookDetails.properties = command.properties
            bookDetails.updatedBy = createOrUpdateBy
            bookDetails.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'BookDetails Updated successfully'
        } else {
            def bookDetailList = BookDetails.findAllByAuthorAndName(command.author, command.name)
            if (bookDetailList) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Book already exist.')
                return result
            }

            bookDetails = new BookDetails(command.properties)
            bookDetails.createdBy = createOrUpdateBy

            if (bookDetails.hasErrors() || !bookDetails.save(flush: true)) {
                def errorList = bookDetails?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                message = errorList?.join('\n')
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }

            Integer quantity = command.quantity
            if (quantity > 0) {
                if (!command.price) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, 'Quantity is required.')
                    return result
                }

                if (!command.stockDate) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, 'Stock date is required.')
                    return result
                }

                if (!command.source) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, 'Source is required.')
                    return result
                }

                def bookIdList = bookStockService.createBookIdList(quantity, bookDetails)
                Book book = new Book()
                book.properties = command.properties
                book.createdBy = createOrUpdateBy
                book.stockStatus = BookStockStatus.ADDED
                Boolean bookSave = Boolean.FALSE
                if (bookIdList) {
                    book.bookDetails = bookDetails
                    bookSave = bookStockService.saveBooks(book, bookIdList)
                }

                if (!bookSave) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, 'Book cannot save please try again.')
                } else {
                    result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
                    result.put(CommonUtils.MESSAGE, 'Book added successfully.')
                }
            }
        }
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def bookDetailInactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BookDetails bookDetails = BookDetails.get(id)
        if (!bookDetails) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        bookDetails.activeStatus = ActiveStatus.INACTIVE
        bookDetails.updatedBy = springSecurityService.principal?.username
        bookDetails.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Delete Successfully')
        return result
    }

    def categoryDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        BookCategory categoryDel = BookCategory.get(id)
        if (!categoryDel) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            categoryDel.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "BookDetails Category Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Category already in use. You Can Inactive Or Delete Reference.")
        }
        return result
    }

    def authorDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Author author = Author.get(id)
        if (!author) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            author.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Author Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Author in use. You Can Inactive Or Delete Reference.")
        }
        return result
    }
}
