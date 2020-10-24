package com.grailslab

import com.amaruddog.myschool.Role
import com.amaruddog.myschool.User
import com.amaruddog.myschool.UserRole
import com.grailslab.command.EmploymentHistoryCommand
import com.grailslab.enums.AvailableRoles
import com.grailslab.enums.MainUserType
import com.grailslab.hr.Employee
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class UserService {

    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['userRef','id','username']
    LinkedHashMap userPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        MainUserType userType
        if(params.userType){
            userType=MainUserType.valueOf(params.userType)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = User.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                if (userType) {
                    eq("mainUserType", userType)
                }
            }
            if (sSearch) {
                or {
                    ilike('username', sSearch)
                    ilike('name', sSearch)
                    ilike('userRef', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            String enabled
            String accountLocked
            String passwordExpired
            String lastLogin
            int serial = iDisplayStart;
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            Employee employee
            String empStr
            results.each { User user ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }

                employee=Employee.findByEmpID(user.userRef)
                if (employee){
                    empStr=employee.empID +' - '+employee.name
                } else {
                    empStr=user.name
                }
                enabled = user.enabled?'Active':'Inactive'
                accountLocked = user.accountLocked?'Yes':'No'
                passwordExpired = user.passwordExpired?'Yes':'No'
                lastLogin = CommonUtils.getUiDateStr(user.lastLogin)
                dataReturns.add([DT_RowId: user.id, username:user.username,0:serial,1: empStr, 2: user.username, 3: enabled, 4:accountLocked, 5: passwordExpired, 6:lastLogin, 7:'' ,8:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def getWorkingUserTypes() {
        def workingUserType
        def user = springSecurityService.currentUser
        if (user && user.mainUserType==MainUserType.SuperAdmin) {
            workingUserType = MainUserType.saUserType()
        } else {
            workingUserType = MainUserType.workingUserType()
        }
        return workingUserType as List
    }

    def getRoleByAuthorities(List<String> authorities) {
        def roleList = Role.findAllByAuthorityInList(authorities)
    }

    boolean isValidatePassword(String pass) {
        return (pass.length() >4) && (pass.length() <12) && (pass =~ /\p{Alpha}/) &&
                (pass =~ /\p{Digit}/)
    }

    def saveOrUpdate(GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()

        User user = new User()
        user.name = params.name
        user.username = params.username
        user.password = params.password
        user.userRef = params.username
        user.schoolId = 10000
        def roleApplicant = Role.findByAuthority(AvailableRoles.APPLICANT.value())

        User existUser = User.findByUsername(user.username)
        if (existUser) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'User name Already exist')
            return result
        }

        if (user.hasErrors()) {
            def errorList = user?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        if (!user.save(flush: true)) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'User Save Failed')
            return result
        }

        if (!UserRole.findByUserAndRole(user, roleApplicant)) {
            UserRole.create(user, roleApplicant, true)
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'User Save Successful Please Login')
        return result
    }

}
