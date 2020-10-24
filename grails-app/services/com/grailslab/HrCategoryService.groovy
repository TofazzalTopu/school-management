package com.grailslab

import com.grailslab.command.HrCategoryCommand
import com.grailslab.enums.HrKeyType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrStaffCategory
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class HrCategoryService {
    def springSecurityService

    static final String[] sortColumns = ['sortOrder', 'name']

    LinkedHashMap hrCategoryPaginateList(GrailsParameterMap params) {
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
        def c = HrCategory.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
//            and {
//                eq("activeStatus",ActiveStatus.ACTIVE)
//            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            results.each { HrCategory hrCategory ->
                dataReturns.add([DT_RowId: hrCategory.id, 0: hrCategory.sortOrder, 1: hrCategory.name, 2: hrCategory?.hrKeyType?.key, 3: hrCategory?.activeStatus?.value, 4: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    static final String[] sortColumnsStaff = ['sortOrder', 'name', 'keyName']

    LinkedHashMap hrStaffCategoryPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsStaff, iSortingCol)
        List dataReturns = new ArrayList()
        def c = HrStaffCategory.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
//            and {
//                eq("activeStatus",ActiveStatus.ACTIVE)
//            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('keyName', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {

            results.each { HrStaffCategory hrStaffCategory ->
                dataReturns.add([DT_RowId: hrStaffCategory.id, 0: hrStaffCategory?.sortOrder, 1: hrStaffCategory.name, 2: hrStaffCategory?.keyName, 3: hrStaffCategory.description, 4: hrStaffCategory?.activeStatus?.value, 5: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    String getHrStaffCategoryKeys(List<Long> staffCategoryIds) {
        def c = HrStaffCategory.createCriteria()
        def results = c.list() {
            if (staffCategoryIds) {
                'in'("id", staffCategoryIds)
            }
            projections {
                property('keyName')
            }
        }.join(',')
    }

    def getHrStaffCategoryIdList(List<String> staffCategoryKeyNames) {
        def c = HrStaffCategory.createCriteria()
        def results = c.list() {
            if (staffCategoryKeyNames) {
                'in'("keyName", staffCategoryKeyNames)
            }
            projections {
                property('id')
            }
        } as List
    }
    HrCategory getHrCatoryByType(HrKeyType hrKeyType){
        return HrCategory.findByHrKeyTypeAndActiveStatus(hrKeyType, ActiveStatus.ACTIVE)
    }

    def hrStaffSortList(HrStaffCategory category) {
        String sSearch = CommonUtils.PERCENTAGE_SIGN + category.keyName + CommonUtils.PERCENTAGE_SIGN
        List dataReturns = new ArrayList()
        def c = Employee.createCriteria()
        def results = c.list() {
            ilike('hrStaffCategory', sSearch)
            eq("activeStatus", ActiveStatus.ACTIVE)
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: employee.name, empId: employee.empID, imagePath:employee.imagePath, designation:employee.hrDesignation?.name, empOrder: employee.sortOrder])
        }
        return dataReturns
    }

    def hrCategoryDropDownList(){
        def c = HrCategory.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order('sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.id, name: obj.name])
        }
        return dataReturns
    }

    def saveOrUpdate(HrCategoryCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        String saveOrUpdatedBy = springSecurityService.principal.username
        HrCategory hrCategory
        if (command.id) {
            hrCategory = HrCategory.get(command.id)
            if (!hrCategory) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            hrCategory.properties = command.properties
            hrCategory.updatedBy = saveOrUpdatedBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Updated successfully')
        } else {
            hrCategory = new HrCategory(command.properties)
            hrCategory.createdBy = saveOrUpdatedBy
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Data create successfully')
        }
        hrCategory.save(flush: true)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        HrCategory hrCategory = HrCategory.get(id)
        if (!hrCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        if (hrCategory.activeStatus.equals(ActiveStatus.INACTIVE)) {
            hrCategory.activeStatus = ActiveStatus.ACTIVE
        } else {
            hrCategory.activeStatus = ActiveStatus.INACTIVE
        }
        hrCategory.updatedBy = springSecurityService.principal.username
        hrCategory.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }

    def saveOrUpdateStaff(HrStaffCategory command) {

        LinkedHashMap result = new LinkedHashMap()

        def refAlreadyExist
        if (command.id) {
            refAlreadyExist = HrStaffCategory.findByKeyNameAndIdNotEqual(command.keyName, command.id)
        } else {
            refAlreadyExist = HrStaffCategory.findByKeyName(command.keyName)
        }
        if (refAlreadyExist) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            return result
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        HrStaffCategory hrStaffCategory
        if (command.id) {
            hrStaffCategory = HrStaffCategory.get(command.id)
            if (!hrStaffCategory) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            hrStaffCategory.properties = command.properties
            hrStaffCategory.updatedBy = createOrUpdateBy
            hrStaffCategory.keyName = command.keyName.replaceAll(" ", "").toLowerCase()
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Updated successfully')
        } else {
            hrStaffCategory = new HrStaffCategory(command.properties)
            hrStaffCategory.createdBy = createOrUpdateBy
            hrStaffCategory.keyName = command.keyName.replaceAll(" ", "").toLowerCase()
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Added successfully')
        }
        hrStaffCategory.save(flush: true)
        return result
    }

    def inactiveStaff(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        HrStaffCategory hrStaffCategory = HrStaffCategory.get(id)
        if (!hrStaffCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (hrStaffCategory.activeStatus.equals(ActiveStatus.ACTIVE)) {
            hrStaffCategory.activeStatus = ActiveStatus.INACTIVE
        } else {
            hrStaffCategory.activeStatus = ActiveStatus.ACTIVE
        }
        hrStaffCategory.updatedBy = springSecurityService.principal.username
        hrStaffCategory.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }
}

