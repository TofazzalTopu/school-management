package com.grailslab

import com.grailslab.command.LeaveNameCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.LeaveName
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class LeaveNameService {
    def springSecurityService

    static final String[] sortColumns = ['name', 'payType', 'numberOfDay']

    LinkedHashMap leaveNamePaginateList(GrailsParameterMap params) {
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
        def c = LeaveName.createCriteria()
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
        if (totalCount > 0) {
            results.each { LeaveName leaveName ->
                dataReturns.add([DT_RowId: leaveName.id, 0: leaveName.name, 1: leaveName.payType?.value, 2: leaveName.numberOfDay, 3: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(LeaveNameCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        String message = ""
        int refAlreadyExist
        if (command.id) {
            refAlreadyExist = LeaveName.countByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE, command.name, command.id)
        } else {
            refAlreadyExist = LeaveName.countByActiveStatusAndName(ActiveStatus.ACTIVE, command.name)
        }

        if (refAlreadyExist > 0) {
            message = "Leave name already exist"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        LeaveName leaveName
        if (command.id) {
            leaveName = LeaveName.get(command.id)
            if (!leaveName) {
                message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            leaveName.properties = command.properties
            leaveName.updatedBy = createOrUpdateBy
            message = "Leave saved successfully"
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, message)
        } else {
            leaveName = new LeaveName(command.properties)
            leaveName.createdBy = createOrUpdateBy
            message = "Leave updated successfully"
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, message)

        }
        leaveName.save(flush: true)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        LeaveName leaveName = LeaveName.get(id)
        if (!leaveName) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (leaveName.activeStatus.equals(ActiveStatus.ACTIVE)) {
            leaveName.activeStatus = ActiveStatus.INACTIVE
        } else {
            leaveName.activeStatus = ActiveStatus.ACTIVE
        }
        leaveName.updatedBy = springSecurityService.principal.username
        leaveName.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Deleted Successfully')
        return result
    }
}
