package com.grailslab.admin

import com.grailslab.CommonUtils
import com.amaruddog.myschool.User
import com.amaruddog.myschool.UserRole
import com.grailslab.command.ManageUserCommand
import com.grailslab.command.UserAccessCommand
import com.grailslab.hr.Employee
import com.grailslab.viewz.StdEmpView
import grails.converters.JSON

class UserController {
    def userService
    def messageSource
    def schoolService
    def employeeService

    def index() {
        def workingUserTypes = userService.getWorkingUserTypes()
        render(view: '/admin/user/userList', model: [workingUserTypes: workingUserTypes])
    }
    def create(){
        UserAccessCommand command = new UserAccessCommand()
        render(view: '/admin/user/create', model: [command: command])
    }
    def saveEmployee(UserAccessCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        User workingUser
        if (command.hasErrors()) {
            if (command.userId) {
                workingUser = User.read(command.userId)
            }
            render(view: '/admin/user/create', model: [command: command, user: workingUser])
            return
        }

        Employee employee
        User userRefObj

        def userRoles = command.empRole.split(",") as List
        def nowRoles = userService.getRoleByAuthorities(userRoles)
        String defaultPassword
        if (command.userId) {
            userRefObj = User.read(command.userId)
            UserRole.executeUpdate("delete UserRole c where c.user = :aUser and c.role not in :deleteRole", [aUser:userRefObj, deleteRole:nowRoles])
            for (role in nowRoles) {
                if (!UserRole.findByUserAndRole(userRefObj, role)) {
                    UserRole.create(userRefObj, role, true)
                }
            }
            flash.message="User updated successfully."
            redirect(action:'index')
            return
        } else {
            userRefObj = User.findByUserRefOrUsername(command.objId, command.username)
            if (userRefObj) {
                if (userRefObj.userRef == command.objId) {
                    command.errors.reject("user.create.user.alreadyAdded", [command.objId] as Object[], 'userRef')
                } else {
                    command.errors.reject('user.create.username.exist', [command.username] as Object[], 'username')
                }
                render(view: '/admin/user/create', model: [command: command, user: null])
                return
            }
            employee = Employee.get(command.objId)
            if (employee) {
                defaultPassword = grailsApplication.config.getProperty('app.school.default.password')
                userRefObj = new User(username: command.username, password: defaultPassword, passwordExpired:true, schoolId: 10000, userRef: employee.empID, name: employee.name, mainUserType: command.mainUserType)
                if (!userRefObj.save(flush: true)) {
                    render(view: '/admin/user/create', model: [command: userRefObj, user: userRefObj])
                    return
                }
            }

        }
        for (role in nowRoles) {
            if (!UserRole.findByUserAndRole(userRefObj, role)) {
                UserRole.create(userRefObj, role, true)
            }
        }
        flash.message="User created successfully with default password "+defaultPassword
        render(view: '/admin/user/create', model: [command: new UserAccessCommand()])
    }

    def edit(long id){
        User user = User.read(id)
        if (!user){
            flash.message="User not found. Please try again or contact with Admin."
            redirect(action: 'index')
            return
        }

        StdEmpView stdEmpView = StdEmpView.findByStdEmpNo(user.userRef)
        if (!stdEmpView){
            flash.message="User not found. Please try again or contact with Admin."
            redirect(action: 'index')
            return
        }

        def employeeName
        if (stdEmpView.objType=='employee'){
            Employee employee = Employee.read(stdEmpView.objId)
            employeeName = employee.empID+" - "+employee.name+", "+employee.hrDesignation?.name
        } else {
            employeeName = "$stdEmpView.stdEmpNo - $stdEmpView.name, $stdEmpView.mobile"
        }

        String authorityStr = user.authorities.collect {it.authority}.join(",")
        render(view: '/admin/user/create', model: [user: user, authorityStr: authorityStr, employeeName: employeeName])
    }



    def resetPass(Long id){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        User user = User.get(id)
        if (!user) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        user.password = params.passwordReset
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,'Password Reset successfully')
        if(user.hasErrors() || !user.save(flush: true)){
            def errorList = user?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.MESSAGE,errorList?.join('\n'))
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        }
        outPut=result as JSON
        render outPut
    }

    def userManageSave(ManageUserCommand command){

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        User user = User.get(command.id)
        if (!user) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        user.properties = command.properties
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,'User Updated successfully')
        if(user.hasErrors() || !user.save(flush: true)){
            def errorList = user?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.MESSAGE,errorList?.join('\n'))
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        }
        outPut=result as JSON
        render outPut
    }

    def userLock(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        User user = User.get(id)
        if (!user) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, user)
        outPut = result as JSON
        render outPut
    }


    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =userService.userPaginateList(params)

        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

}

