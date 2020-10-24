package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.command.library.LibraryMemberCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class LibraryMemberService {

    def messageSource
    def springSecurityService
    def sequenceGeneratorService

    static transactional = false
    static final String[] sortColumns = ['id', 'name']

    LinkedHashMap categoryPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = LibraryMember.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        String memberShipDate
        int serial = iDisplayStart;
        if (totalCount > 0) {
            results.each { LibraryMember member ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                memberShipDate = member.memberShipDate ? CommonUtils.getUiDateStr(member.memberShipDate) : ''
                dataReturns.add([DT_RowId: member.id, 0: serial, 1: member.name, 2: member.memberId, 3: member.referenceId, 4: member.mobile, 5: member.presentAddress, 6: memberShipDate, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(LibraryMemberCommand command, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        LibraryMember libraryMember
        String message
        if (params.objId) {
            libraryMember = LibraryMember.get(params.objId)
            if (!libraryMember) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            libraryMember.properties['name', 'address', 'memberShipDate', 'mobile', 'email', 'referenceId'] = command.properties
            libraryMember.updatedBy = createOrUpdateBy
            libraryMember.academicYear = academicYear
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Library Member Updated successfully'
        } else {
            libraryMember = new LibraryMember(command.properties)
            libraryMember.memberId = nextMembershipNo()
            libraryMember.createdBy = createOrUpdateBy
            libraryMember.academicYear = academicYear
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Library Member Added successfully'
        }
        libraryMember.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        LibraryMember libraryMember = LibraryMember.get(id)
        if (!libraryMember) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            libraryMember.activeStatus = ActiveStatus.INACTIVE
            libraryMember.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Library Member Deleted Successfully.")
        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Library in use")
        }
        return result
    }

    String nextMembershipNo() {
        String serialNo = sequenceGeneratorService.nextNumber('libraryMembership')
    }

}
