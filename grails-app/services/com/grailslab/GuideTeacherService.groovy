package com.grailslab

import com.grailslab.settings.GuideTeacher
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import com.grailslab.viewz.GuideTeacherView

@Transactional
class GuideTeacherService {

    static final String[] dropDownSortColumns = ['id', 'empid', 'name', 'studentIds']
    LinkedHashMap guideTeacherPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(dropDownSortColumns, iSortingCol)
        List dataReturns = new ArrayList()

        def c = GuideTeacherView.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", "ACTIVE")
            }
            if (sSearch) {
                or {
                    ilike('empid', sSearch)
                    ilike('name', sSearch)
                    ilike('studentIds', sSearch)
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
            results.each { def dropOut ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: dropOut.id, employeeId: dropOut.employeeId, academicYear: dropOut.academicYear, 0: serial, 1: dropOut.empid, 2: dropOut.name, 3: dropOut.studentIds, 4: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    GuideTeacher update(Long objId, Long employeeId, String academicYear, String studentIds) {
        GuideTeacher gtch = GuideTeacher.get(objId)
        gtch.employeeId = employeeId
        gtch.academicYear = academicYear
        gtch.studentIds= studentIds
        gtch.save(flash: true)
    }
}
