package com.grailslab

import com.grailslab.command.EasMarkCommand
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.myapp.config.ConfigKey
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.EasMark
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.web.servlet.mvc.GrailsParameterMap
import com.grailslab.gschoolcore.ActiveStatus

@Transactional
class EasService {
    def springSecurityService
    def messagingService
    def messageSource
    def schoolService
    def grailsApplication

    static final String[] termMarkSortColumns = ['std.rollNo','std.name','std.studentID']
    static final String[] sortColumns = ['studentID', 'name', 'rollNo', 'easTotalMark', 'section']

    LinkedHashMap easPaginateList(GrailsParameterMap params) {
        def workingYears = schoolService.workingYears()
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        /* String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER*/
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        Section section
        ClassName className
        Long secId
        Long classId
        if(params.sectionName){
            secId = Long.parseLong(params.sectionName)
            section = Section.read(secId)
        }
        if(params.className){
            classId = Long.parseLong(params.className)
            className = ClassName.read(classId)
        }

        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        Double easMark = 0
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                gt('easTotalMark', easMark)
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                if(section){
                    eq("section", section)
                }
                if(className){
                    eq("className", className)
                }
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
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
            results.each { Student easMarkView ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: easMarkView.id, 0: serial, 1: easMarkView.studentID, 2: easMarkView.name, 3: easMarkView.rollNo, 4:easMarkView.registration.mobile, 5: easMarkView.easTotalMark, 6: easMarkView.section.name, 7: easMarkView.studentStatus.value, 8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def getEasMarkList(GrailsParameterMap params, Section section) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(termMarkSortColumns, iSortingCol)

        //get Subject Studentids List

        List dataReturns = new ArrayList()
        int totalCount =0
        def c = EasMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("std.section", section)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        totalCount = results.totalCount
        Student student
        String addedByName
        if (totalCount > 0) {
            results.each { EasMark termMark ->
                student = termMark.student
                if (termMark.addedBy) {
                    addedByName = "$termMark.addedBy.empID - $termMark.addedBy.name"
                } else {
                    addedByName = ""
                }
                dataReturns.add([DT_RowId: termMark.id, 0:student.studentID,1: student.name, 2:student.rollNo, 3:termMark.mark, 4: termMark.description, 5: addedByName, 6: CommonUtils.getUiDateStr(termMark.entryDate), 7:''])

            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def saveOrUpdateEas(EasMarkCommand command){

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String message
        Student student = command.student
        Double easTotalMark = student.easTotalMark ?: 0

        easTotalMark = easTotalMark + command.mark
        EasMark easMark = new EasMark(command.properties)
        easMark.createdBy = createOrUpdateBy
        easMark.academicYear = student.academicYear
        message = "EAS Mark added Successfully"

        String smsText
        Double presentEasMark = 100 - easTotalMark
        if (easTotalMark > 20) {
            student.studentStatus = StudentStatus.TC
            smsText = "Dear guardian, $student.name, Student ID: $student.studentID. His/ Her EAS marks reached at $presentEasMark (below 80). He/She has been given auto TC from school as per earlier notice."
            message = "EAS Mark added Successfully and alert SMS sent to guardian"
        } else {
            smsText = "Dear guardian, $command.mark marks from EAS has been punished for $student.name, student ID: $student.studentID. His/her present EAS marks is $presentEasMark. If it goes down below 80, an auto TC will be ready for him/her."
            message = "EAS Mark added Successfully and alert SMS sent to guardian"
        }
        boolean isError = false
        if (grailsApplication.config.getProperty(ConfigKey.EAS_NOTIFICATION_SMS, Boolean)) {
            LinkedHashMap resultMap = messagingService.sendMessages(smsText, student.registration.mobile, 1)
            isError = resultMap.isError
            if (isError) message = resultMap.message
        }
        if (isError) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            outPut = result as JSON
            render outPut
            return
        }
        student.easTotalMark = easTotalMark
        student.updatedBy = createOrUpdateBy
        if (easMark.save(flush: true) && student.save(flush: true)) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }
        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        result.put(CommonUtils.MESSAGE, "Can't save EAS mark now. Please contact with admin")
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        EasMark easMark = EasMark.get(id)
        if (!easMark) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (SpringSecurityUtils.ifAnyGranted("ROLE_SCHOOL_HEAD,ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")){
            String updatedBy = springSecurityService.principal.username
            easMark.activeStatus = ActiveStatus.DELETE
            easMark.updatedBy = updatedBy
            Student student = easMark.student
            student.easTotalMark = student.easTotalMark - easMark.mark

            if (easMark.save() && student.save()) {
                result.put(CommonUtils.IS_ERROR, false)
                result.put(CommonUtils.MESSAGE, "EAS entry removed successfully.")
                return result
            }
        }
        result.put(CommonUtils.IS_ERROR, true)
        result.put(CommonUtils.MESSAGE, "You don't have access to remove EAS entry. Please contact with admin")
        return result
    }
}
