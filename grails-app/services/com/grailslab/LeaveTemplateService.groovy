package com.grailslab

import com.grailslab.command.LeaveApplicationTemplateCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.LeaveTemplate
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class LeaveTemplateService {
    def springSecurityService

    static final String[] sortColumns = ['applyType']

    LinkedHashMap leaveApplicationPaginateList(GrailsParameterMap params) {
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
        def c = LeaveTemplate.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('leaveTemplate', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }

        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { obj ->
                dataReturns.add([DT_RowId: obj.id, 0: obj.applyType?.value, 1: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(LeaveApplicationTemplateCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username

        String message = ""

        int refAlreadyExist
        if (command.id) {
            refAlreadyExist = LeaveTemplate.countByApplyTypeAndActiveStatusAndIdNotEqual(command.applyType, ActiveStatus.ACTIVE, command.id)
        } else {
            refAlreadyExist = LeaveTemplate.countByApplyTypeAndActiveStatus(command.applyType, ActiveStatus.ACTIVE)
        }

        if (refAlreadyExist > 0) {
            message = "Leave template name already exist"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        LeaveTemplate leaveApplicationTemplate
        if (command.id) {
            leaveApplicationTemplate = LeaveTemplate.get(command.id)
            if (!leaveApplicationTemplate) {
                message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
            leaveApplicationTemplate.properties = command.properties
            leaveApplicationTemplate.updatedBy = createOrUpdateBy
            message = "Template saved successfully"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
        } else {
            leaveApplicationTemplate = new LeaveTemplate(command.properties)
            leaveApplicationTemplate.createdBy = createOrUpdateBy
            message = "Template updated successfully"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
        }
        leaveApplicationTemplate.save(flush: true)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        LeaveTemplate leaveApplicationTemplate = LeaveTemplate.get(id)
        if (!leaveApplicationTemplate) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (leaveApplicationTemplate.activeStatus.equals(ActiveStatus.ACTIVE)) {
            leaveApplicationTemplate.activeStatus = ActiveStatus.INACTIVE
        } else {
            leaveApplicationTemplate.activeStatus = ActiveStatus.ACTIVE
        }
        leaveApplicationTemplate.updatedBy = springSecurityService.principal.username
        leaveApplicationTemplate.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Template deleted Successfully')
        return result
    }
}
