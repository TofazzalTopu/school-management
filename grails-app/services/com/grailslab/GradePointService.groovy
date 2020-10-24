package com.grailslab

import com.grailslab.enums.LetterGrade
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.GradePoint
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class GradePointService {
    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['id','gPoint','fromMark' ,'laterGrade','credentials']
    LinkedHashMap gradePointPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        ClassName className
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        } else {
            className = ClassName.first()
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = GradePoint.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("className", className)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('laterGrade', sSearch)
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
            results.each {GradePoint gradePoint ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: gradePoint.id, 0: serial, 1: gradePoint.gPoint, 2: gradePoint.fromMark + ' - ' + gradePoint.upToMark, 3: gradePoint.laterGrade, 4: gradePoint.credentials, 5:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    GradePoint getByMark(Double percentageMark, ClassName className) {
        return GradePoint.findByClassNameAndFromMarkLessThanEqualsAndUpToMarkGreaterThanEquals(className, percentageMark, percentageMark)
    }
    GradePoint getByLetterGrade(ClassName className, LetterGrade letterGrade) {
        return GradePoint.findByClassNameAndLaterGrade(className, letterGrade)
    }
    LetterGrade getByPoint(Double gPoint) {
        LetterGrade letterGrade
        if(gPoint >= 5){
            letterGrade = LetterGrade.GRADE_A_PLUS
        }else if(gPoint>=4 && gPoint<5){
            letterGrade = LetterGrade.GRADE_A
        }else if(gPoint>=3 && gPoint<4){
            letterGrade = LetterGrade.GRADE_A_MINUS
        }else if(gPoint>=2 && gPoint<3){
            letterGrade = LetterGrade.GRADE_B
        }else if(gPoint>=1 && gPoint<2){
            letterGrade = LetterGrade.GRADE_C
        }else {
            letterGrade = LetterGrade.GRADE_F
        }
        return letterGrade
    }

    def updateGradePoint(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        GradePoint gradePoint = GradePoint.read(Long.parseLong(params.id))
        if (!gradePoint) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }


        Double fromMark, upToMark

        /*String markRangeToSplit = params.fromMark
        String[] markRange;
        String delimiter = "-";
        markRange = markRangeToSplit.split(delimiter);
        Double tempMark
        for (int i = 0; i < markRange.length; i++) {
            tempMark = Double.parseDouble(markRange[i])
            if (i==0) {
                fromMark = tempMark
            } else {
                upToMark = tempMark
            }
        }*/

        gradePoint.fromMark = params.getDouble("fromMark")
        gradePoint.upToMark = params.getDouble("upToMark")
        if (gradePoint.hasErrors()) {
            def errorList = gradePoint?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        gradePoint.gPoint = Double.parseDouble(params.gPoint)
        //gradePoint.fromMark = fromMark
        //gradePoint.upToMark = upToMark
        gradePoint.laterGrade = params.laterGrade
        gradePoint.credentials = params.credentials
        gradePoint.updatedBy = springSecurityService.principal?.username
        gradePoint.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Successfully updated")
        return result

    }
}
