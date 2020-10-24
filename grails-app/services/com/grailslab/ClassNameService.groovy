package com.grailslab

import com.grailslab.command.ClassNameCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ClassNameService {

    def springSecurityService
    static transactional = false

    ClassName read(Long id) {
        return ClassName.read(id)
    }

    static final String[] sortColumns = ['sortPosition','name']
    LinkedHashMap classNamePaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = ClassName.createCriteria()
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

        String allowOptional
        if (totalCount > 0) {
            results.each { ClassName className ->
                allowOptional = ""
                allowOptional+=className.groupsAvailable?"Group, ":""
                allowOptional+=className.allowOptionalSubject?"Optional":""

                dataReturns.add([DT_RowId: className.id, 0: className.sortPosition, 1: className.name,2: allowOptional,3: className.sectionType?:'',4: className.expectedNumber+' % ', 5:className.hrPeriod? className.hrPeriod.periodRange: "-" ,6:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def allClassNames(){
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def classNamesList(Long classNameId){
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (classNameId) {
                    eq("id", classNameId)
                }
            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
    def classNamesList(AcaYearType yearType){
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (yearType == AcaYearType.SCHOOL) {
                    'in'('sectionType', ['Primary', 'High'] as List)
                } else if (yearType == AcaYearType.COLLEGE){
                    eq("sectionType", 'College')
                }
            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def classNameDropDownList(AcaYearType yearType){
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (yearType == AcaYearType.SCHOOL) {
                    'in'('sectionType', ['Primary', 'High'] as List)
                } else if (yearType == AcaYearType.COLLEGE){
                    eq("sectionType", 'College')
                }

            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { ClassName className ->
            dataReturns.add([id: className.id, name: className.name])
        }
        return dataReturns
    }

    def classNameInIdListDD(String classIds){
        if (!classIds) {
            return null
        }
        def ids = classIds.split(',').collect { it as Long }
        if (!ids) {
            return null
        }
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                'in'("id", ids)
            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { ClassName className ->
            dataReturns.add([id: className.id, name: className.name])
        }
        return dataReturns
    }

    def classNameInIdListDDBatch(String classIds, AcaYearType yearType = AcaYearType.SCHOOL){
        if (!classIds) {
            return null
        }
        def ids = classIds.split(',').collect { it as Long }
        if (!ids) {
            return null
        }
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                'in'("id", ids)
                if (yearType == AcaYearType.SCHOOL) {
                    'in'('sectionType', ['Primary', 'High'] as List)
                } else if (yearType == AcaYearType.COLLEGE){
                    eq("sectionType", 'College')
                }

            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { ClassName className ->
            dataReturns.add([id: className.id, name: className.name])
        }
        return dataReturns
    }
    def classNameNotInIdListDD(String classIds, AcaYearType yearType = AcaYearType.SCHOOL){
        if (!classIds) {
            return null
        }
        def ids = classIds.split(',').collect { it as Long }
        if (!ids) {
            return null
        }
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                not {'in'("id", ids)}
                if (yearType == AcaYearType.SCHOOL) {
                    'in'('sectionType', ['Primary', 'High'] as List)
                } else if (yearType == AcaYearType.COLLEGE){
                    eq("sectionType", 'College')
                }

            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { ClassName className ->
            dataReturns.add([id: className.id, name: className.name])
        }
        return dataReturns
    }

    def nextClasses(ClassName oldClass){
        def c = ClassName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                gt("sortPosition",oldClass.sortPosition)
            }
            order('sortPosition', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def saveOrUpdate(ClassNameCommand classNameCommand) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username
        ClassName className
        String message
        ClassName ifExistClassName
        if (classNameCommand.id) {
            className = ClassName.get(classNameCommand.id)
            if (!className) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            ifExistClassName = ClassName.findByNameAndActiveStatusAndIdNotEqual(classNameCommand.name, ActiveStatus.ACTIVE, classNameCommand.id)
            if (ifExistClassName) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Class Name already Exist')
                return result
            }
            className.properties = classNameCommand.properties
            className.updatedBy = createOrUpdate
            className.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Class Updated successfully'
        } else {
            ifExistClassName = ClassName.findByNameAndActiveStatus(classNameCommand.name, ActiveStatus.ACTIVE)
            if (ifExistClassName) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Class Name already Exist')
                return result
            }
            className = new ClassName(classNameCommand.properties)
            className.createdBy = createOrUpdate
            className.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Class Added successfully'
        }
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        ClassName className = ClassName.get(id)
        if (!className) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (className.activeStatus.equals(ActiveStatus.INACTIVE)) {
            className.activeStatus = ActiveStatus.ACTIVE
        } else {
            className.activeStatus = ActiveStatus.INACTIVE
        }
        className.updatedBy = springSecurityService.principal.username
        className.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Class Deleted successfully.")
        return result
    }

}
