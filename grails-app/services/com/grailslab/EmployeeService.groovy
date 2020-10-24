package com.grailslab

import com.grailslab.command.EducationalInfoCommand
import com.grailslab.command.EmployeeCommand
import com.grailslab.command.EmploymentHistoryCommand
import com.grailslab.enums.HrKeyType
import com.grailslab.enums.SelectionTypes
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.EducationalInfo
import com.grailslab.hr.Employee
import com.grailslab.hr.EmploymentHistory
import com.grailslab.hr.HrCategory
import com.grailslab.settings.Image
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class EmployeeService {

    def messageSource
    def uploadService
    def grailsApplication
    def hrCategoryService
    def springSecurityService

    static final String[] sortColumns = ['sortOrder','empID', 'name', 'cardNo', 'mobile', 'emailId','designation.name','sortOrder']

    LinkedHashMap employeePaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        HrCategory hrCategory
        if (params.hrCategoryType) {
            hrCategory = HrCategory.read(params.getLong('hrCategoryType'))
        }
        ActiveStatus activeStatus = ActiveStatus.ACTIVE
        if (params.activeStatus) {
            activeStatus = ActiveStatus.valueOf(params.activeStatus)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Employee.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('hrDesignation', 'designation')
            and {
                eq("activeStatus", activeStatus)
                if (hrCategory) {
                    eq("hrCategory", hrCategory)
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('empID', sSearch)
                    ilike('fathersName', sSearch)
                    ilike('emailId', sSearch)
                    ilike('cardNo', sSearch)
                    ilike('mobile', sSearch)
                }
            }
            order(sortColumn, sSortDir)
            order('joiningDate', CommonUtils.SORT_ORDER_ASC)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { Employee employee ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: employee.id, 0:serial,1: employee?.empID, 2: employee?.name, 3: employee?.cardNo ?: '', 4: employee?.mobile ?: '', 5: employee.emailId ?: '', 6: employee.hrDesignation ? employee.hrDesignation.name : '',7: employee.hrStaffCategory ?: '', 8: employee.sortOrder, 9:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def teacherDropDownList() {
        List dataReturns = new ArrayList()
        HrCategory teacherType = HrCategory.findByHrKeyType(HrKeyType.TEACHER)
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                eq("hrCategory", teacherType)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("name", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name}"])
        }
        return dataReturns
    }

    def allEmployeeDropDownList() {
        List dataReturns = new ArrayList()
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name}"])
        }
        return dataReturns
    }

    def employeeList(HrCategory hrCategory) {
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                if (hrCategory){
                    eq("hrCategory", hrCategory)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def employeeList(Date joinedBefore, List<Long> empIdLIst, HrCategory hrCategory) {
        def joinedBeforeList
        if (hrCategory){
            joinedBeforeList = Employee.findAllByIdInListAndActiveStatusAndHrCategoryAndJoiningDateLessThan(empIdLIst, ActiveStatus.ACTIVE, hrCategory, joinedBefore)
        } else {
            joinedBeforeList = Employee.findAllByIdInListAndActiveStatusAndJoiningDateLessThan(empIdLIst, ActiveStatus.ACTIVE, joinedBefore)
        }
        return joinedBeforeList
    }
    def employeeListByJoinedBefore(Date joinedBefore) {
        return Employee.findAllByActiveStatusAndJoiningDateLessThan(ActiveStatus.ACTIVE, joinedBefore)
    }

    def employeeIdListByCategory(HrCategory hrCategory) {
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                if (hrCategory){
                    eq("hrCategory", hrCategory)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            projections {
                property('id')
            }
        }
        return results
    }

    def allEmployeeWithDesignationList(String sSearch) {
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Employee.createCriteria()

        def results = c.list() {
            createAlias('hrDesignation', 'designation')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('empID', sSearch)
                    ilike('name', sSearch)
                    ilike('designation.name', sSearch)
                }
            }
            order("empID", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name} - ${employee.hrDesignation.name}"])
        }
        return dataReturns
    }

    def allEmployeeDropDownListWithCardNo() {
        List dataReturns = new ArrayList()
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name} - ${employee.cardNo}"])
        }
        return dataReturns
    }

    def allEmployeeWithList(String sSearch) {
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Employee.createCriteria()

        def results = c.list() {
            createAlias('hrDesignation', 'designation')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('empID', sSearch)
                    ilike('name', sSearch)
                    ilike('designation.name', sSearch)
                }
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Employee employee ->
            dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name} - ${employee.hrDesignation.name}"])
        }
        return dataReturns
    }

    def step2EmpListForMessage(List<Long> selectionIds, SelectionTypes selectionType) {
        String sSearch = "01" + CommonUtils.PERCENTAGE_SIGN
        def c = Employee.createCriteria()
        def results = c.list() {
            createAlias('hrCategory', 'dept')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                ilike('mobile', sSearch)
                if (selectionType && selectionType == SelectionTypes.BY_TEACHER) {
                    'in'("id", selectionIds)
                } else {
                    'in'("dept.id", selectionIds)
                }
            }
        }
        List dataReturns = new ArrayList()
        String mobileNo
        results.each { Employee employee ->
            mobileNo = employee.mobile
            if (mobileNo && mobileNo.size() == 11) {
                dataReturns.add([id: employee.id, name: "${employee.empID} - ${employee.name} [${employee?.hrDesignation?.name}]", mobile: employee.mobile])
            }
        }
        return dataReturns
    }

    Employee findByEmpId(String empId) {
        Employee employee = Employee.findByEmpIDAndActiveStatus(empId, ActiveStatus.ACTIVE)
        return employee
    }

    Employee findById(Long id) {
        Employee employee = Employee.findByIdAndActiveStatus(id, ActiveStatus.ACTIVE)
        return employee
    }

    def dataJobInfoList(Employee employee) {
        List dataReturns = new ArrayList()
        def c = EmploymentHistory.createCriteria()
        def results = c.list() {
            and {
                eq("employee", employee)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("id", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { EmploymentHistory history ->
            dataReturns.add([DT_RowId: history.id, 0: history?.company, 1: history?.jobTitle, 2: CommonUtils.getUiDateStr(history?.joiningDate), 3: CommonUtils.getUiDateStr(history?.endDate), 4: history?.location, 5: ''])
        }
        return dataReturns
    }

    def academicInfoList(Employee employee) {
        List dataReturns = new ArrayList()
        def c = EducationalInfo.createCriteria()
        def results = c.list() {
            and {
                eq("employee", employee)
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("id", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { EducationalInfo info ->
            dataReturns.add([DT_RowId: info.id, 0: info?.certification?.name, 1: info?.institution?.name, 2: info?.majorSubject?.name, 3: CommonUtils.getUiDateStr(info?.passingYear), 4: info?.result, 5: info?.duration, 6: ''])
        }
        return dataReturns
    }

    def employeeCardNoList(){
        def c = Employee.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                isNotNull("cardNo")
            }
            projections {
                property('cardNo')
            }
        }
        return results
    }

    def findByEmpCardNumber(String cardNo) {
        Employee.findByCardNoAndActiveStatus(cardNo, ActiveStatus.ACTIVE)
    }

    def employeeHrPeriod(Long id) {
        Employee employee=Employee.read(id)
        if (!employee || !employee.hrPeriod) return null
        return [startTime:employee.hrPeriod.startTime, duration:employee.hrPeriod.duration]
    }

    def numberOfTotalEmployee(HrCategory hrCategory){
        if (hrCategory){
            return Employee.countByActiveStatusAndHrCategory(ActiveStatus.ACTIVE, hrCategory)
        } else {
            return Employee.countByActiveStatus(ActiveStatus.ACTIVE)
        }
    }

    def saveOrUpdate(EmployeeCommand command, request) {

        LinkedHashMap result = new LinkedHashMap()
        String saveOrUpdateBy = springSecurityService.principal.username
        Employee employee
        Image image
        if (command.id) {
            employee = Employee.get(command.id)
            if (!employee) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            employee.properties = command.properties
            employee.updatedBy = saveOrUpdateBy
            def pImage = request.getFile("pImage")
            if (!pImage.empty) {
                if (employee.imagePath) {
                    try {
                        Boolean deleteStatus = uploadService.deleteImage(employee.imagePath)
                    } catch (Exception e) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, e.toString())
                        return result
                    }
                }
                try {
                    image = uploadService.uploadImageWithThumb(request, "pImage", "employee")
                    employee.imagePath = image?.identifier
                } catch (Exception e) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, e.toString())
                    return result
                }
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Employee Updated successfully')
        } else {
            employee = new Employee(command.properties)
            employee.createdBy = saveOrUpdateBy
            def pImage = request.getFile("pImage")
            if (!pImage.empty) {
                try {
                    image = uploadService.uploadImageWithThumb(request, "pImage", "employee")
                    employee.imagePath = image?.identifier
                } catch (Exception e) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, e.toString())
                    return result
                }
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Employee Added successfully')
        }
        String staffCategoryKeyNames = null
        if (command.hrStaffCategory) {
            List<Long> categoryIds = command.hrStaffCategory.split(",").collect { it as Long }
            staffCategoryKeyNames = hrCategoryService.getHrStaffCategoryKeys(categoryIds)

        }
        employee.hrStaffCategory = staffCategoryKeyNames
        employee.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Data added successfully.')
        return result

    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Employee employee = Employee.get(id)
        if (!employee) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        String message
        if (employee.activeStatus == ActiveStatus.INACTIVE) {
            employee.activeStatus = ActiveStatus.ACTIVE
            message = 'Employee Activated successfully'
        } else {
            employee.activeStatus = ActiveStatus.INACTIVE
            message = 'Employee deleted successfully'
        }
        employee.updatedBy = springSecurityService.principal.username
        employee.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def employmentHistorySave(EmploymentHistoryCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        EmploymentHistory employmentHistory
        String message
        def loggedUser = springSecurityService.principal
        Employee employee = Employee.findByEmpID(loggedUser.userRef)
        if (!employee) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (command.id) {
            employmentHistory = EmploymentHistory.get(command.id)
            if (!employmentHistory) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            employmentHistory.properties = command.properties
            employmentHistory.updatedBy = loggedUser.username
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Employment History Updated successfully'
        } else {
            employmentHistory = new EmploymentHistory(command.properties)
            employmentHistory.createdBy = loggedUser.username
            employmentHistory.academicYear = academicYear
            employmentHistory.employee = employee
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Employment History Added successfully'
        }
        employmentHistory.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def employmentHistoryDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        EmploymentHistory history = EmploymentHistory.get(id)
        if (!history) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            history.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "EmploymentHistory Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "EmploymentHistory already in use. You Can Inactive Reference")
        }
        return result
    }

    def formEduInfoSave(EducationalInfoCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        EducationalInfo info
        String message
        String createOrUpdateBy = springSecurityService.principal?.username
        def loggedUser = springSecurityService.principal
        Employee employee = Employee.findByEmpID(loggedUser.userRef)
        if (command.id) {
            info = EducationalInfo.get(command.id)
            if (!info) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            info.properties = command.properties
            info.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Employment Education Updated successfully'
        } else {
            info = new EducationalInfo(command.properties)
            info.createdBy = createOrUpdateBy
            info.employee = employee
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Employment Education Added successfully'
        }
        info.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def educationalInfoDelete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        EducationalInfo info = EducationalInfo.get(id)
        if (!info) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            info.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "EducationalInfo Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "EducationalInfo already in use. You Can Inactive Reference")
        }
        return result
    }

    def educationalInfoInactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        EducationalInfo info = EducationalInfo.get(id)
        if (!info) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        info.activeStatus = ActiveStatus.INACTIVE
        info.updatedBy = springSecurityService.principal?.username
        info.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactivated Successfully')
        return result
    }

    def employmentHistoryInactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        EmploymentHistory history = EmploymentHistory.get(id)
        if (!history) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        history.activeStatus = ActiveStatus.INACTIVE
        history.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactivated Successfully')
        return result
    }
}