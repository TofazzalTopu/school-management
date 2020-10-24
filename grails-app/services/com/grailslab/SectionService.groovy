package com.grailslab

import com.grailslab.command.SectionCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.GroupName
import com.grailslab.enums.Shift
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class SectionService {

    def springSecurityService
    def messageSource
    def schoolService

    Section read(Long id) {
        return Section.read(id)
    }
    static final String[] sortColumns = ['n.sortPosition','name','sortOrder','className','shift']
    LinkedHashMap sectionPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        AcademicYear academicYear
        ClassName classNameObj
        Long classId
        if(params.className){
            classId = Long.parseLong(params.className)
            classNameObj = ClassName.read(classId)
        }
        if(params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        } else {
            academicYear = schoolService.schoolWorkingYear()
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Section.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('className', 'n')
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)

                if(classNameObj){
                    eq("className", classNameObj)
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('n.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            String className
            results.each { Section section ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                if (section.groupName){
                    className=section.className.name+" ( "+ section.groupName+" )"
                }else {
                    className=section.className.name
                }
                dataReturns.add([DT_RowId: section.id, 0: serial, 1: className, 2: section.name, 3: section.sortOrder, 4: section.employee?.name, 5:section.shift?.value, 6: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def allSections(GrailsParameterMap params){
        AcademicYear academicYear= null
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        return allSections(null, academicYear)
    }

    def allSections(AcaYearType acaYearType, AcademicYear academicYear){
        def workingYears
        if (acaYearType && !academicYear) {
            workingYears = schoolService.workingYears(acaYearType)
        }
        def c = Section.createCriteria()
        def results = c.list() {
            createAlias('className', 'n')
            and {
                if (academicYear) {
                    eq("academicYear", academicYear)
                }else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('n.sortPosition', CommonUtils.SORT_ORDER_ASC)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def classSections(ClassName className){
        AcademicYear academicYear = schoolService.workingYear(className)
        return classSections(className, academicYear)
    }
    def classSections(ClassName className, AcademicYear academicYear){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        def c = Section.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def groupSections(ClassName className, GroupName groupName, AcademicYear academicYear){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        def c = Section.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                if (groupName) {
                    eq("groupName", groupName)
                }
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        return results
    }

    def sectionDropDownList(AcademicYear academicYear = null){
        def workingYears = schoolService.workingYears()
        def c = Section.createCriteria()
        def results = c.list() {
            createAlias('className', 'n')
            and {
                if (academicYear) {
                    eq("academicYear", academicYear)
                }else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('n.sortPosition', CommonUtils.SORT_ORDER_ASC)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: "${obj.className.name} - ${obj.name}"])
        }
        return dataReturns
    }

    def classNameWiseSectionDropDownList(ClassName className, AcademicYear academicYear){
        def workingYears = schoolService.workingYears()
        def c = Section.createCriteria()
        def results = c.list() {
            eq("className", className)
            and {
                if (academicYear) {
                    eq("academicYear", academicYear)
                }else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: "${obj.name}"])
        }
        return dataReturns
    }

    def sectionDropDownNotInList(AcademicYear academicYear, List sectionIds){
        def workingYears = schoolService.workingYears()
        def c = Section.createCriteria()
        def results = c.list() {
            createAlias('className', 'n')
            and {
                if (academicYear) {
                    eq("academicYear", academicYear)
                }else {
                    'in'("academicYear", workingYears)
                }
                if(sectionIds){
                    not{'in'("id", sectionIds)}
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('n.sortPosition', CommonUtils.SORT_ORDER_ASC)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: "${obj.className.name} - ${obj.name}"])
        }
        return dataReturns
    }



    def classSectionsDDList(ClassName className){
        if (!className) return null
        AcademicYear academicYear = schoolService.workingYear(className)
        return classSectionsDDList(className, null, academicYear)
    }
    def classSectionsDDList(ClassName className, AcademicYear academicYear){
        return classSectionsDDList(className, null, academicYear)
    }

    def classSectionsDDList(ClassName className, GroupName groupName, AcademicYear academicYear){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        def c = Section.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                if (groupName) {
                    eq("groupName", groupName)
                }
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }

    def classSectionsDDList(Shift shift, ClassName className, GroupName groupName, AcademicYear academicYear){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        def c = Section.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (className) {
                    eq("className", className)
                }
                if (shift) {
                    eq("shift", shift)
                }
                if (groupName) {
                    eq("groupName", groupName)
                }
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }

    List<Long> sectionIdsByShift(List<Shift> shiftList, AcademicYear academicYear){
        def workingYears = schoolService.workingYears()
        def c = Section.createCriteria()
        List<Long> sectionIds = c.list() {
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
                'in'("shift", shiftList)
            }
            projections {
                property "id"
            }
        } as List
        return sectionIds
    }

    List<Long> sectionIdsByClassId(List<Long> classIds, AcademicYear academicYear){
        def workingYears = schoolService.workingYears()
        def c = Section.createCriteria()
        List<Long> sectionIds = c.list() {
            createAlias('className', 'n')
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
                'in'("n.id", classIds)
            }
            projections {
                property "id"
            }
        } as List
        return sectionIds
    }

    def sectionsByClassIds(String classIds, AcademicYear academicYear = null){
        if (!classIds) {
            return null
        }
        def ids = classIds.split(',').collect { it as Long }
        if (!ids) {
            return null
        }
        def workingYears = schoolService.workingYears()

        def c = Section.createCriteria()
        def results = c.list() {
            createAlias('className', 'n')
            and {
                if (academicYear) {
                    eq("academicYear", academicYear)
                }else {
                    'in'("academicYear", workingYears)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
                'in'("n.id", ids)

            }
            order('n.sortPosition', CommonUtils.SORT_ORDER_ASC)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        return results
    }

    Section find(Long id){
       return Section.findByIdAndActiveStatus(id, ActiveStatus.ACTIVE)
    }

    def saveOrUpdate(SectionCommand sectionCommand) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        if (sectionCommand.hasErrors()) {
            def errorList = sectionCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Section section
        String message
        int ifExistSection
        AcademicYear academicYear = sectionCommand.academicYear
        if (sectionCommand.id) { //update Currency
            section = Section.get(sectionCommand.id)
            if (!section) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            ifExistSection = Section.countByNameAndActiveStatusAndAcademicYearAndClassNameAndIdNotEqual(sectionCommand.name, ActiveStatus.ACTIVE, academicYear, sectionCommand.className, sectionCommand.id)
            if (ifExistSection > 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Section already Exist')
                return result
            }

            if (section.academicYear != academicYear) {
                int sectionStudent = Student.countBySectionAndStudentStatus(section, StudentStatus.NEW)
                if (sectionStudent > 0) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "Can't update Academic year of this section. Please delete all student of this section first")
                    return result
                }
            }

            section.properties = sectionCommand.properties
            section.academicYear = academicYear
            section.updatedBy = createOrUpdateBy
            if (!sectionCommand.className.groupsAvailable) {
                section.groupName = null
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Section Updated successfully'
        } else {
            ifExistSection = Section.countByNameAndActiveStatusAndAcademicYearAndClassName(sectionCommand.name, ActiveStatus.ACTIVE, academicYear, sectionCommand.className)

            if (ifExistSection > 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Section already Exist')
                return result
            }

            section = new Section(sectionCommand.properties)
            section.academicYear = academicYear
            section.createdBy = createOrUpdateBy
            if (!sectionCommand.className.groupsAvailable) {
                section.groupName = null
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Section Added successfully'
        }

        section.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        Section section = Section.get(id)
        if (!section) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (section.activeStatus.equals(ActiveStatus.INACTIVE)) {
            section.activeStatus = ActiveStatus.ACTIVE
        } else {
            section.activeStatus = ActiveStatus.INACTIVE
        }
        section.updatedBy = createOrUpdateBy
        section.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Section deleted successfully.")
        return result
    }

    def saveTransfer(GrailsParameterMap params) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        String message

        if (!params.academicYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)

        int year
        AcademicYear nextYear
        if (academicYear?.value.length() == 4) {
            year = Integer.parseInt(academicYear.value)
            year = year + 1
            nextYear = AcademicYear.getYearByString(year.toString())
        } else {
            year = Integer.parseInt(academicYear?.value.substring(0, 4))
            int lowYear = year + 1
            int highYear = year + 2
            nextYear = AcademicYear.getYearByString(lowYear + "-" + highYear)
        }

        if (!nextYear) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet for section transfer. Please contact admin")
            return result
        }

        if (!params.sectionIds) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select section to move next year")
            return result
        }
        def sectionIdList = params.sectionIds.split(",")

        if (!sectionIdList || sectionIdList.size() == 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select section to move next year")
            return result
        }

        Section oldSection
        Section newSection
        int ifExistSection
        int oldSectionCount = 0
        sectionIdList.each { it ->
            oldSection = Section.read(Long.valueOf(it))
            ifExistSection = Section.countByNameAndActiveStatusAndAcademicYearAndClassName(oldSection.name, ActiveStatus.ACTIVE, nextYear, oldSection.className)
            if (ifExistSection == 0) {
                newSection = new Section(oldSection.properties)
                newSection.createdBy = createOrUpdateBy
                newSection.academicYear = nextYear
                newSection.save(flush: true)
            } else {
                oldSectionCount++
            }
        }

        if (oldSectionCount > 0) {
            message = "$oldSectionCount sections already exist. Other sections moved successfully"
        } else {
            message = "All sections moved successfully"
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

}