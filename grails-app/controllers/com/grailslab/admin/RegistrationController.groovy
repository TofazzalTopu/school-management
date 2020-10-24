package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.RegistrationCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Image
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class RegistrationController {
    def springSecurityService
    def registrationService
    def schoolService
    def classNameService
    def studentService
    def messageSource
    def uploadService

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentMgmt/registration', model: [classNameList:classNameList, academicYearList: academicYearList])
    }

    def save(RegistrationCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join(','))
            outPut = result as JSON
            render outPut
            return
        }
        Registration registration
        Student student
        String createOrUpdateBy = springSecurityService.principal.username
        AcademicYear academicYear = springSecurityService.principal.academicYear
        if (params.id) {
            registration = Registration.get(command.id)
            if (!registration) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                outPut = result as JSON
                render outPut
                return
            }
            registration.properties['name','fathersName','mothersName','bloodGroup','religion','presentAddress','permanentAddress','deviceId','email','mobile','fathersProfession','mothersProfession','fathersIncome','gender','cardNo','birthDate'] = command.properties
            registration.updatedBy = createOrUpdateBy
            if(command.studentID){
                Registration existingReg = Registration.findByIdNotEqualAndStudentID(command.id, command.studentID)
                if(existingReg){
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "Student Id already exist")
                    outPut = result as JSON
                    render outPut
                    return
                }
                registration.studentID = command.studentID
            }

            //code for save image
            HttpServletRequest request = request
            def f = request.getFile("pImage")
            if (!f.empty) {
                if (registration.imagePath) {
                    try {
                        uploadService.deleteImage(registration.imagePath)
                    } catch (Exception e) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, e.toString())
                        outPut = result as JSON
                        render outPut
                        return
                    }
                }
                try {
                    Image image = uploadService.uploadImageWithThumb(request, "pImage", "registration", 450, 350)
                    registration.imagePath=image?.identifier
                } catch (Exception e) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, e.toString())
                    outPut = result as JSON
                    render outPut
                    return
                }
            }
            //code for save image

            if (registration.hasErrors() || !registration.save(failOnError: true)) {
                def errorList = registration?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, errorList?.join(','))
                outPut = result as JSON
                render outPut
                return
            }
            student = Student.findByRegistrationAndStudentStatusAndAcademicYearInList(registration, StudentStatus.NEW,schoolService.workingYears())
            if (student) {
                student.name = registration.name
                student.studentID = registration.studentID
                student.updatedBy = createOrUpdateBy
                student.save(flush: true)
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Registration Updated successfully')
            outPut = result as JSON
            render outPut
            return
        }

        registration = new Registration(command.properties)
        if(command.studentID){
            Registration existingReg = Registration.findByStudentID(command.studentID)
            if(existingReg){
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Student Id already exist")
                outPut = result as JSON
                render outPut
                return
            }
            registration.studentID = command.studentID
        }else {
            registration.studentID = registrationService.nextStudentNo()
        }
        if (registration.hasErrors()) {
            def errorList = registration?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join(','))
            outPut = result as JSON
            render outPut
            return
        }
        registration.createdBy = createOrUpdateBy
        registration.academicYear = academicYear
        registration.save(flush: true)
        if (command.academicYear && command.sectionName && command.rollNo) {
            studentService.savedStudent(registration, command.sectionName, command.rollNo, command.academicYear)
            registration.studentStatus=StudentStatus.ADMISSION
            registration.save(flush: true)
        }


        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Registration Created successfully')
        outPut = result as JSON
        render outPut
    }

    /*def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Registration registration = Registration.get(id)
        if (!registration) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        String message
        if(registration.studentStatus!=StudentStatus.NEW){
            if(registration.studentStatus==StudentStatus.ADMISSION){
                message = "Student already admitted. Please delete the admission first."
            }else {
                message = "Can't delete ${registration.studentStatus.value} registration."
            }
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,message)
            outPut = result as JSON
            render outPut
            return
        }
        try {
            registration.delete()
        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Registration could not deleted. Already in use.")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Registration deleted successfully.")
        outPut = result as JSON
        render outPut
    }*/

    def delete(Long id) {
        LinkedHashMap result = registrationService.delete(id)
        String output = result as JSON
        render output
    }

    def doActive(Long id) {
        LinkedHashMap result = registrationService.doActive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = registrationService.registrationPaginateList(params)

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

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Registration registration = Registration.read(id)
        if (!registration) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('obj', registration)
        outPut = result as JSON
        render outPut
    }

    def addDetails(Long id){
        /*if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            redirect(action: 'index')
            return
        }*/
        Registration registration = Registration.read(id)
        render(view: '/admin/studentMgmt/registrationDetails', model: [registration: registration])
    }

    def showHistory(Long id){
        Registration registration = Registration.read(id)
        render(view: '/admin/studentMgmt/studentHistory', model: [registration: registration, selectedTab : "student-basic-info-tab"])
    }

    def saveDetailsReg(RegistrationCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            render(view: '/admin/studentMgmt/registrationDetails', model: [commandObj: command, registration:new Registration()])
            return
        }
        if (!schoolService.hasFeatureAccess()) {
            redirect(action: 'index')
            return
        }
        Registration registration
        Student student

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        if (params.id) {
            registration = Registration.get(command.id)
            if (!registration) {
                flash.message= CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }

            registration.properties['name','guardianName','guardianMobile','motherIsAlive','mothersIncome','mothersMobile','fatherIsAlive','fathersMobile','birthCertificateNo','nationality','admissionYear','nameBangla','fathersName','mothersName','bloodGroup','religion','presentAddress','permanentAddress','deviceId','email','mobile','fathersProfession','mothersProfession','fathersIncome','gender','cardNo','birthDate'] = command.properties
            registration.updatedBy = createOrUpdateBy
            //code for save image
            HttpServletRequest request = request
            def f = request.getFile("pImage")
            if (!f.empty) {
                if (registration.imagePath) {
                    try {
                        uploadService.deleteImage(registration.imagePath)
                    } catch (Exception e) {
                        flash.message=  e.toString()
                        redirect(action: 'index')
                        return
                    }
                }
                try {
                    Image image = uploadService.uploadImageWithThumb(request, "pImage", "registration", 450, 350)
                    registration.imagePath=image?.identifier
                } catch (Exception e) {
                    flash.message=  e.toString()
                    redirect(action: 'index')
                    return
                }
            }
            //code for save image

            if (registration.hasErrors() || !registration.save(failOnError: true)) {
                def errorList = registration?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                flash.message= errorList?.join(',')
                redirect(action: 'index')
                return
            }

            student = Student.findByRegistrationAndStudentStatusAndAcademicYearInList(registration, StudentStatus.NEW, schoolService.workingYears())
            if (student) {
                student.name = registration.name
                student.studentID = registration.studentID
                student.updatedBy = createOrUpdateBy
                student.save(flush: true)
            }

            flash.message= 'Registration Updated successfully'
            redirect(action: 'index')
            return
        }
        redirect(action: 'index')
    }
}
