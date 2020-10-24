package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.HrCategoryCommand
import grails.converters.JSON

class HrCategoryController {

    def messageSource
    def hrCategoryService
    def springSecurityService

    def index() {
        LinkedHashMap resultMap = hrCategoryService.hrCategoryPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/hr/hrCategory', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/hr/hrCategory', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = hrCategoryService.hrCategoryPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def save(HrCategoryCommand command) {

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }

        result = hrCategoryService.saveOrUpdate(command)
        outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        HrCategory hrCategory = HrCategory.read(id)
        if (!hrCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, hrCategory)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        String outPut
        LinkedHashMap result = hrCategoryService.inactive(id)
        outPut = result as JSON
        render outPut
    }

    def hrStaffCategory() {
        LinkedHashMap resultMap = hrCategoryService.hrStaffCategoryPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/hr/hrStaffCategory', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/hr/hrStaffCategory', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def listStaff() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = hrCategoryService.hrStaffCategoryPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveStaff(HrStaffCategory command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'hrStaffCategory')
            return
        }

        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }

        result = hrCategoryService.saveOrUpdateStaff(command)
        outPut = result as JSON
        render outPut
    }

    def editStaff(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'hrStaffCategory')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        HrStaffCategory hrStaffCategory = HrStaffCategory.read(id)
        if (!hrStaffCategory) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, hrStaffCategory)
        outPut = result as JSON
        render outPut
    }

    def inactiveStaff(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'hrStaffCategory')
            return
        }
        String outPut
        LinkedHashMap result = hrCategoryService.inactiveStaff(id)
        outPut = result as JSON
        render outPut
    }

    def staffSorting(Long id) {
        HrStaffCategory category = HrStaffCategory.read(id)
        if (!category) {
            redirect(action: 'hrStaffCategory')
            return
        }

        def resultMap = hrCategoryService.hrStaffSortList(category)
        render(view: '/hr/hrStaffSorting', model: [employeeList: resultMap])
    }

    def staffSortingSave() {
        Employee employee
        params.sortOrder.each { employeeData ->
            try {
                employee = Employee.get(Long.parseLong(employeeData.key))
                employee.sortOrder = Integer.parseInt(employeeData.value)
                employee.save(flush: true)
            } catch (e) {
                e.print(employee.empID)
            }
        }
        redirect(action: 'hrStaffCategory')
    }
}
