package com.grailslab

import com.grailslab.command.DesignationCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrDesignation
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class HrDesignationService {
    def springSecurityService

    static final String[] sortColumns = ['sortOrder','name']
    LinkedHashMap designationPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        HrCategory hrCategory
        if(params.hrCategoryType){
            hrCategory = HrCategory.read(params.getLong('hrCategoryType'))
        }else {
            def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
            if(hrCategoryList){
                hrCategory = hrCategoryList.first()
            }
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = HrDesignation.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
                if(hrCategory){
                    eq("hrCategory", hrCategory)
                }
            }
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
            results.each { HrDesignation obj ->
                dataReturns.add([DT_RowId: obj.id,  0: obj.sortOrder, 1: obj.name, 2:obj.hrCategory?.name, 3: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def saveOrUpdate(DesignationCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        def refAlreadyExist
        if(command.id){
            refAlreadyExist = HrDesignation.findByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE, command.name, command.id)
        } else {
            refAlreadyExist = HrDesignation.findByActiveStatusAndName(ActiveStatus.ACTIVE,command.name)
        }
        if(refAlreadyExist){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            return result
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        HrDesignation designation
        if (command.id) {
            designation = HrDesignation.get(command.id)
            if (!designation) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            designation.properties = command.properties
            designation.updatedBy = createOrUpdateBy
            designation.save(flush: true)
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Updated successfully')
        } else {
            designation = new HrDesignation(command.properties)
            designation.createdBy = createOrUpdateBy
            designation.save(flush: true)
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Added successfully')
        }
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        HrDesignation designation = HrDesignation.get(id)
        if (!designation) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (designation.activeStatus.equals(ActiveStatus.INACTIVE)) {
            designation.activeStatus = ActiveStatus.ACTIVE
        } else {
            designation.activeStatus = ActiveStatus.INACTIVE
        }
        designation.updatedBy = springSecurityService.principal.username
        designation.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Status Change Successfully')
        return result
    }
}
