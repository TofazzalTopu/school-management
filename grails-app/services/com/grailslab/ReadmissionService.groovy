package com.grailslab

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ReadmissionService {
    def schoolService
    def subjectService

    static final String[] sortColumns = ['id', 'name', 'studentID', 'registration']

    LinkedHashMap readmissionPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        Section section
        ClassName className

        Long secId
        Long classId
        if (params.sectionName) {
            secId = Long.parseLong(params.sectionName)
            section = Section.read(secId)
        }
        AcademicYear academicYear = schoolService.workingYear(section?.className)
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        }

        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('className', 'c')
            createAlias('section', 's')
            and {
                eq("academicYear", academicYear)
                if (section) {
                    eq("section", section)
                }
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('s.name', sSearch)
                    ilike('c.name', sSearch)
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
            results.each { Student student ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: student.id, 0: serial, 1: student.studentID, 2: student.name, 3: student.section.name, 4: student.className.name, 5: student.rollNo, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
}