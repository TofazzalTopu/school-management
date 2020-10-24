package com.grailslab


import com.grailslab.enums.StudentStatus
import com.grailslab.enums.TcType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import com.grailslab.stmgmt.TcAndDropOut
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class TcAndDropOutService {

    static final String[] sortColumns = ['id', 'std.studentID', 'std.name']

    LinkedHashMap tcPaginateList(GrailsParameterMap params) {
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
        def c = TcAndDropOut.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias("student", "std")
            and {
                eq("tcOrDropoutType", TcType.TC)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("std.studentStatus", StudentStatus.NEW)

            }
            if (sSearch) {
                or {
                    ilike('std.studentID', sSearch)
                    ilike('std.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;

        if (totalCount > 0) {
            results.each { TcAndDropOut dropOut ->

                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }

                dataReturns.add([DT_RowId: dropOut.id, 0: serial, 1: dropOut.student.studentID, 2: dropOut.student.name, 3: dropOut.student.section.name, 4: dropOut.releaseDate, 5: dropOut.reason, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap dropOutPaginateList(GrailsParameterMap params) {
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
        def c = TcAndDropOut.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias("student", "std")
            and {
                eq("tcOrDropoutType", TcType.DROP_OUT)
                eq("std.studentStatus", StudentStatus.NEW)
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('std.studentID', sSearch)
                    ilike('std.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;

        if (totalCount > 0) {
            results.each { TcAndDropOut dropOut ->

                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }

                dataReturns.add([DT_RowId: dropOut.id, 0: serial, 1: dropOut.student.studentID, 2: dropOut.student.name, 3: dropOut.student.section.name, 4: dropOut.releaseDate, 5: dropOut.reason, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    @Transactional
    def dropOutSave(TcAndDropOut dropOut, Student student, Registration registration){

        if (dropOut.save(flush: true) && student.save(flush: true) && registration.save(flush: true)){
            return true
        }else {
            return false
        }
    }
    @Transactional
    def deleteDropout(TcAndDropOut dropOut, Student student, Registration registration){

        if (dropOut.save(flush: true) && student.save(flush: true) && registration.save(flush: true)){
            return true
        }else {
            return false
        }
    }
}
