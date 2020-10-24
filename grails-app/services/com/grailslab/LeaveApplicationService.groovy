package com.grailslab

import com.grailslab.command.LeaveApplyCommand
import com.grailslab.enums.LeaveApproveStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.LeaveApply
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class LeaveApplicationService {
    def leaveService
    def springSecurityService

    static final String[] sortColumnsLeaveApprove = ['id', 'em.name','leaveName','fromDate','numberOfDay','reason','approveStatus','applyDate']

    LinkedHashMap leaveApprovePaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        LeaveApproveStatus approveStatus = params.approveStatus ? LeaveApproveStatus.valueOf(params.approveStatus) : LeaveApproveStatus.Applied
        String sortColumn = CommonUtils.getSortColumn(sortColumnsLeaveApprove, iSortingCol)
        List dataReturns = new ArrayList()
        def c = LeaveApply.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('employee', 'em')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("approveStatus", approveStatus)
            }
            if (sSearch) {
                or {
                    ilike('em.empID', sSearch)
                    ilike('em.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String employeeName
        String leaveRange
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { LeaveApply apply ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                leaveRange = CommonUtils.getDateRange(apply.fromDate, apply.toDate)
                employeeName = apply.employee?.empID + '  ' + apply.employee?.name
                dataReturns.add([DT_RowId: apply.id, DT_Status: apply.approveStatus?.key, 0: serial, 1: employeeName, 2: apply.leaveName?.name,  3: leaveRange, 4: apply.numberOfDay+"("+apply.payType?.value+")", 5: apply.reason, 6: apply.approveStatus?.value, 7: CommonUtils.getUiDateStr(apply.applyDate),  8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(LeaveApplyCommand command, GrailsParameterMap parameterMap) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        Boolean isSaveSuccess = false
        String message

        LeaveApply leaveApply
        if (command.id) {
            leaveApply = LeaveApply.get(command.id)
            if (!leaveApply) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            leaveApply.updatedBy = createOrUpdateBy
            if (parameterMap.approveType) {
                leaveApply.properties = command.properties
                if (parameterMap.approveType == "Approve") {
                    leaveApply.approveStatus = LeaveApproveStatus.Approved
                    message = "Leave approved successfully"
                } else {
                    leaveApply.approveStatus = LeaveApproveStatus.Rejected
                    message = "Leave rejected successfully"
                }
                isSaveSuccess = leaveService.saveLeaveApplication(leaveApply)
            } else {
                leaveApply.properties = command.properties
                isSaveSuccess = leaveService.saveLeaveApplication(leaveApply)
                message = "Leave updated successfully"
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        } else {
            leaveApply = new LeaveApply(command.properties)
            leaveApply.createdBy = createOrUpdateBy
            leaveApply.academicYear = academicYear
            isSaveSuccess = leaveService.saveLeaveApplication(leaveApply)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = "Leave added successfully"
        }

        if (!isSaveSuccess) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Your paid leave quota for this year exceeded. Please try unpaid or contact admin")
            return result
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        LeaveApply apply = LeaveApply.get(id)
        if (!apply) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        apply.activeStatus = ActiveStatus.DELETE
        apply.updatedBy = springSecurityService.principal?.username
        apply.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        return result
    }
}
