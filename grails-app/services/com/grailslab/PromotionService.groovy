package com.grailslab

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Registration
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
@Transactional
class PromotionService {
    def subjectService

    static final String[] sortColumns = ['id','studentID','section','admission']

    LinkedHashMap promotionPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        Section preSection =params.preSection ?Section.findById(params.getLong('preSection')): null
        AcademicYear preYear=params.preYear ? AcademicYear.valueOf(params.preYear) : null

        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Registration.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'st')
            createAlias('section', 's')
            and {
                eq("st.admissionYear", preYear)
                eq("section", preSection )
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('studentsFirstName', sSearch)
                    ilike('studentsLastName', sSearch)
                    ilike('s.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String subjectsName
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { Registration registration ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                subjectsName= subjectService.getName(registration.student.optSubjectId)
                dataReturns.add([DT_RowId: registration.id, DT_Std_Id: registration.studentID, DT_Std_Name: registration.fullName,DT_SectionID: registration.section.id, 0: serial, 1: registration.studentID, 2: registration.fullName, 3: registration.section.name,4: registration.section.className.name, 5: registration.student.classRoll, 6: subjectsName,7: registration.student?.status?.value, 8: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
}
