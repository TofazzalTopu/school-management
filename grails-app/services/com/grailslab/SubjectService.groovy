package com.grailslab

import com.grailslab.command.SubjectCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.SubjectName
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class SubjectService {
    def springSecurityService

    static final String[] sortColumns = ['sortPosition','name']

    LinkedHashMap subjectPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = SubjectName.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("isAlternative", false)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('description', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SubjectName subject ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: subject.id, hasChild: subject.hasChild, 0:serial, 1: subject.name, 2: subject.code,3:subject.sortPosition, 4: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap childPaginateList(GrailsParameterMap params, SubjectName subjectName) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = SubjectName.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("isAlternative", true)
                eq("parentId", subjectName.id)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)

        }
        int totalCount = results.totalCount
        int serial = iDisplayStart
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { SubjectName subject ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: subject.id,0:serial, 1: subject.name, 2: subject.code,3:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def allSubjectList(){
        def results = SubjectName.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: 200, sort: "name", order: "asc"])
    }

    def allMainSubjectDropDownList(){
        def c = SubjectName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("isAlternative", Boolean.FALSE)
            }
            order('name', CommonUtils.SORT_ORDER_ASC)
        }

        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }
    def allAlterSubjectDropList(SubjectName subjectName){
        def c = SubjectName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
               eq("isAlternative", Boolean.TRUE)
                eq("parentId", subjectName.id)
            }
            order('name', CommonUtils.SORT_ORDER_ASC)
        }

        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }

    def allMainSubjectNotInListDropDown(List ids){
        def c = SubjectName.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("isAlternative", Boolean.FALSE)
                if(ids){
                    not {'in'("id",ids)}
                }
            }
            order('name', CommonUtils.SORT_ORDER_ASC)
        }

        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }

    def allSubjectInListDropDown(String ids){
        if (!ids) {
            return null
        }
        def subjectIds = ids.split(',').collect { it as Long }
        if (!subjectIds) {
            return null
        }
        def query = SubjectName.where {
            (id in subjectIds)
        }
        def results = query.list()

        List dataReturns = new ArrayList()
        for (obj in results) {
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }
    def allSubjectInList(String ids){
        if (!ids) {
            return null
        }
        def subjectIds = ids.split(',').collect { it as Long }
        if (!subjectIds) {
            return null
        }
        def query = SubjectName.where {
            (id in subjectIds)
        }
        def results = query.list()
    }

    String getName(String ids) {
        if (!ids) {
            return null
        }
        def subjectIds = ids.split(',').collect { it as Long }
        if (!subjectIds) {
            return null
        }
        def query = SubjectName.where {
            (id in subjectIds)
        }
        def results = query.list()
        String subjectStr = results.collect { it.name }.join(', ')
        return subjectStr
    }

    def getCommaSeparatedIdAsList(String commaSeparatedIds) {
        if (!commaSeparatedIds) return ""
        return commaSeparatedIds.split(",") as List
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        SubjectName subject = SubjectName.get(id)
        if (!subject) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (subject.activeStatus.equals(ActiveStatus.INACTIVE)) {
            subject.activeStatus = ActiveStatus.ACTIVE
        } else {
            subject.activeStatus = ActiveStatus.INACTIVE
        }
        subject.updatedBy = springSecurityService.principal.username
        subject.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Subject deleted successfully.")
        result result
    }

    def saveOrUpdate(SubjectCommand subjectCommand) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError', true)
        if (subjectCommand.hasErrors()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        String createOrUpdateBy = springSecurityService.principal.username
        SubjectName subject
        if (subjectCommand.id) { //update Currency
            subject = SubjectName.get(subjectCommand.id)
            if (!subject) {
                result.put('message', 'Subject not found')
                return result
            }
            SubjectName ifExistSubjectName = SubjectName.findByNameAndActiveStatusAndIdNotEqual(subjectCommand.name, ActiveStatus.ACTIVE, subjectCommand.id)
            if (ifExistSubjectName) {
                result.put('message', 'Same Subject already Exist')
                return result
            }
            subject.properties = subjectCommand.properties
            subject.updatedBy = createOrUpdateBy
            if (!subjectCommand.hasChild) {
                subject.hasChild = false
            }
            if (subjectCommand.parentId) {
                subject.isAlternative = true
            } else {
                subject.isAlternative = false
            }
            if (!subject.validate()) {
                result.put('message', 'Please fill the form correctly')
                return result
            }
            subject.save(flush: true)
            result.put('isError', false)
            result.put('message', 'Subject Updated successfully')
            return result
        }

        subject = new SubjectName(subjectCommand.properties)
        subject.createdBy = createOrUpdateBy
        if (!subjectCommand.hasChild) {
            subject.hasChild = false
        }
        if (subjectCommand.parentId) {
            subject.isAlternative = true
        } else {
            subject.isAlternative = false
        }

        if (!subjectCommand.validate()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        SubjectName ifExistSubjectName = SubjectName.findByNameAndActiveStatus(subjectCommand.name, ActiveStatus.ACTIVE)
        if (ifExistSubjectName) {
            result.put('message', 'Same Subject already Exist')
            return result
        }
        SubjectName savedCurr = subject.save(flush: true)
        if (!savedCurr) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        result.put('isError', false)
        result.put('message', 'Subject Create successfully')
        return result
    }
}
