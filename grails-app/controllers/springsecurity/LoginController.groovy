/* Copyright 2013-2014 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package springsecurity

import com.amaruddog.myschool.Role
import com.amaruddog.myschool.User
import com.amaruddog.myschool.UserRole
import com.grailslab.command.SignUpCommand
import com.grailslab.enums.AvailableRoles
import com.grailslab.enums.MainUserType
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes

import javax.servlet.http.HttpServletResponse

@Secured('permitAll')
class LoginController {

    /**
     * Dependency injection for the authenticationTrustResolver.
     */
    def authenticationTrustResolver

    /**
     * Dependency injection for the springSecurityService.
     */
    def springSecurityService
    def schoolService
    def userService

    /**
     * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
     */
    def index() {
        if (springSecurityService.isLoggedIn()) {
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
        } else {
            redirect action: 'auth', params: params
        }
    }

    /**
     * Show the login page.
     */
    def auth() {

        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
            return
        }

        String view = 'auth'
        String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
        render view: view, model: [postUrl            : postUrl,
                                   rememberMeParameter: config.rememberMe.parameter]
    }

    def loginSuccess() {
        if (SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN,ROLE_SCHOOL_HEAD,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")) {
            redirect(controller: 'admin', action: 'index')
            return
        } else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.ACCOUNTS.value())) {
            redirect(controller: 'collections', action: 'index')
            return
        } /*else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.LIBRARY.value())) {
            redirect(controller: 'library', action: 'index')
            return
        }*/ else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.HR.value())) {
            redirect(controller: 'hrm', action: 'index')
            return
        } else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.ORGANIZER.value())) {
            redirect(controller: 'sliderImage', action: 'index')
            return
        } else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.TEACHER.value())) {
            redirect(controller: 'teacher', action: 'index')
            return
        }  else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.STUDENT.value())) {
            redirect(controller: 'portal', action: 'index')
            return
        }
        redirect(controller: 'home')
    }

    /**
     * The redirect action for Ajax requests.
     */
    def authAjax() {
        response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
        response.sendError HttpServletResponse.SC_UNAUTHORIZED
    }

    /**
     * Show denied page.
     */
    def denied() {
        if (springSecurityService.isLoggedIn() &&
                authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
            // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
            redirect action: 'full', params: params
        }
    }

    /**
     * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
     */
    def full() {
        def config = SpringSecurityUtils.securityConfig
        render view: 'auth', params: params,
                model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
                        postUrl  : "${request.contextPath}${config.apf.filterProcessesUrl}"]
    }

    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */
    def authfail() {

        String msg = ''
        def username
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = "Sorry, your account has expired. Contact with admin." //grailsApplication.config.getProperty("springSecurity.errors.login.expired")
            } else if (exception instanceof CredentialsExpiredException) {
                msg = "Sorry, your password has expired. Contact with admin" //grailsApplication.config.getProperty("springSecurity.errors.login.passwordExpired")
            } else if (exception instanceof DisabledException) {
                msg = "Sorry, your account is disabled. Contact with admin" //grailsApplication.config.getProperty("springSecurity.errors.login.disabled")
            } else if (exception instanceof LockedException) {
                msg = "Sorry, your account is locked. Please pay your monthly subscription fee or contact with admin" //grailsApplication.config.getProperty("springSecurity.errors.login.locked")
            } else {
                msg = "Sorry, we were not able to find a user with that username and password. Contact with admin" //grailsApplication.config.getProperty("springSecurity.errors.login.fail")
            }
        }

        if (springSecurityService.isAjax(request)) {
            render([error: msg] as JSON)
        } else if (username) {
            redirect action: 'passwordExpired', params: [username: username]
        } else {
            flash.message = msg
            redirect action: 'auth', params: params
        }
    }

    /**
     * The Ajax success redirect url.
     */
    def ajaxSuccess() {
        render([success: true, username: springSecurityService.authentication.name] as JSON)
    }

    /**
     * The Ajax denied redirect url.
     */
    def ajaxDenied() {
        render([error: 'access denied'] as JSON)
    }
    def passwordExpired() {
        flash.message="Your password has expired. Please set new password"
        session['SPRING_SECURITY_LAST_USERNAME'] = params.username
    }

    def updatePassword() {
        String username = session['SPRING_SECURITY_LAST_USERNAME']
        if (!username) {
            flash.message = 'Sorry, an error has occurred'
            redirect controller: 'login', action: 'auth'
            return
        }
        String password = params.password
        String newPassword = params.password_new
        String newPassword2 = params.password_confirm
        if (!password || !newPassword || !newPassword2 ||
                newPassword != newPassword2) {
            flash.message =
                    'Please enter your current password and a valid new password'
            render view: 'passwordExpired',
                    model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }
        if (!userService.isValidatePassword(newPassword)){
            flash.message =grailsApplication.config.getProperty('user.resetpassword.not.valid')
            render view: 'passwordExpired',
                    model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }

        User user = User.findByUsername(username)
        if (springSecurityService.passwordEncoder && !springSecurityService.passwordEncoder.isPasswordValid(user.password, password, null /*salt*/)) {
            flash.message = 'Current password is incorrect'
            render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }

        if (springSecurityService.passwordEncoder && springSecurityService.passwordEncoder.isPasswordValid(user.password, newPassword, null /*salt*/)) {
            flash.message = 'Please a new password different from your current expired password'
            render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
            return
        }

        user.password = newPassword
        user.passwordExpired = false
        user.save(flush: true) // if you have password constraints check them here
        flash.message = 'Password reset successfully.'
        redirect controller: 'login', action: 'auth'
    }



    def registration(SignUpCommand command ) {
        switch (request.method) {
            case 'GET':
                render(view: 'registration')
                break
            case 'POST':
                User user = User.findByUsernameOrUserRef(command.userId, command.userId)
                if (user) {
                    flash.message = "User already exist. You can reset your password or contact Admin"
                    render(view: 'registration', model: [command: command])
                    return
                }
                Registration registration = Registration.findByBirthDateAndMobileAndActiveStatusAndStudentID(command.birthDate, command.mobileNo, ActiveStatus.ACTIVE, command.userId)
                if (!registration) {
                    flash.message = "Your user ID, Mobile No or Birth Date not match with existing record. Please re type correctly or contact Admin"
                    render(view: 'registration', model: [command: command])
                    return
                }
                def academicYears = schoolService.workingYears()
                Student student = Student.findByRegistrationAndStudentStatusAndActiveStatusAndAcademicYearInList(registration, StudentStatus.NEW, ActiveStatus.ACTIVE, academicYears)
                if (!student) {
                    flash.message = "Only Current students can access now. If you are current student, please contact Admin"
                    render(view: 'registration', model: [command: command])
                    return
                }
                Role studentRole = Role.findByAuthority(AvailableRoles.STUDENT.value())
                User newUser = new User(username: command.userId, password: command.password, schoolId: 10000, mainUserType: MainUserType.Student, userRef: command.userId).save(flush: true)
                UserRole.create(newUser, studentRole, true)
                flash.message = "your are successfully registered. Please login to continue with your user ID and given password"
                redirect(action: 'auth')
                break
        }
    }

}