package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.EmployeeCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Image
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class EmployeeController {

    def employeeService
    def messageSource
    def springSecurityService
    def uploadService
    def hrPeriodService
    def hrCategoryService

    def index() {
        String hrCategoryType = params.get('hrCategoryType')
        def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: 'sortOrder'])
        def hrStaffCategoryList = HrStaffCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: 'sortOrder'])
        def officeHourList = hrPeriodService.periodDropDownList()
        LinkedHashMap resultMap = employeeService.employeePaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/hr/employee', model: [hrCategoryType: hrCategoryType, dataReturn: null, totalCount: 0, hrCategoryList: hrCategoryList, hrStaffCategoryList: hrStaffCategoryList, officeHourList: officeHourList])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/hr/employee', model: [hrCategoryType: hrCategoryType, dataReturn: resultMap.results, totalCount: totalCount, hrCategoryList: hrCategoryList, hrStaffCategoryList: hrStaffCategoryList, officeHourList: officeHourList])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = employeeService.employeePaginateList(params)

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

    def save(EmployeeCommand command) {
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
        Employee employee
        Image image
        if (command.id) {
            employee = Employee.get(command.id)
            if (!employee) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                outPut = result as JSON
                render outPut
                return
            }
            employee.properties = command.properties
            employee.updatedBy = springSecurityService.principal?.username
            HttpServletRequest request = request
            def f = request.getFile("pImage")
            if (!f.empty) {
                if (employee.imagePath) {
                    try {
                        Boolean deleteStatus = uploadService.deleteImage(employee.imagePath)
                    } catch (Exception e) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, e.toString())
                        outPut = result as JSON
                        render outPut
                        return
                    }
                }
                try {
                    image = uploadService.uploadImageWithThumb(request, "pImage", "employee")
                    employee.imagePath = image?.identifier
                } catch (Exception e) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, e.toString())
                    outPut = result as JSON
                    render outPut
                    return
                }
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Employee Updated successfully')
        } else {
            employee = new Employee(command.properties)
            employee.createdBy = springSecurityService.principal?.username
            HttpServletRequest request = request
            def f = request.getFile("pImage")
            if (!f.empty) {
                try {
                    image = uploadService.uploadImageWithThumb(request, "pImage", "employee")
                    employee.imagePath = image?.identifier
                } catch (Exception e) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, e.toString())
                    outPut = result as JSON
                    render outPut
                    return
                }
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Employee Added successfully')
        }
        String staffCategoryKeyNames= null
        if(command.hrStaffCategory){
            List<Long> categoryIds = command.hrStaffCategory.split(",").collect{it as Long}
            staffCategoryKeyNames = hrCategoryService.getHrStaffCategoryKeys(categoryIds)

        }
        employee.hrStaffCategory= staffCategoryKeyNames
        if (employee.hasErrors() || !employee.save(flush: true)) {
            def errorList = employee?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        }
        outPut = result as JSON
        render outPut

        /*
        *
        * result = employeeService.saveOrUpdate(command, request)
        outPut = result as JSON
        render outPut*/
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Employee employee = Employee.read(id)
        String filePath
        if (!employee) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        HrCategory hrCategory = employee.hrCategory
        def designation = HrDesignation.findAllByHrCategoryAndActiveStatus(hrCategory, ActiveStatus.ACTIVE)
        def categoryIdList
        if(employee.hrStaffCategory){
            def staffCate = employee.hrStaffCategory.split(',') as List
            categoryIdList = hrCategoryService.getHrStaffCategoryIdList(staffCate)
        }

        result.put('designation', designation)
        result.put('categoryIdList', categoryIdList)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, employee)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        String outPut
        LinkedHashMap result = employeeService.inactive(id)
        outPut = result as JSON
        render outPut
    }

    def changeCategory() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        HrCategory categoryType
        if (params.hrCategory) {
            categoryType = HrCategory.read(params.getLong('hrCategory'))
        }
        if (!categoryType) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        def designation = HrDesignation.findAllByActiveStatusAndHrCategory(ActiveStatus.ACTIVE, categoryType)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('designation', designation)
        outPut = result as JSON
        render outPut
    }

    def sortOrder(){

    }
}

