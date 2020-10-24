package com.grailslab.applicants

import com.grailslab.CommonUtils
import com.grailslab.command.ApplicantEditCommand
import com.grailslab.enums.ApplicantStatus
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Image
import com.grailslab.stmgmt.RegForm
import com.grailslab.stmgmt.RegOnlineRegistration
import grails.converters.JSON

import javax.servlet.http.HttpServletRequest

class OnlineRegistrationController {
    def onlineRegistrationService
    def messageSource
    def uploadService
    def admissionFormService
    def schoolService

    def index() {
        def classNameList = admissionFormService.classesDropDown()
        def applicantStatusList = ApplicantStatus.values()
        if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            applicantStatusList = ApplicantStatus.selectionStatus()
        }
        render(view: 'index', model: [classNameList: classNameList, applicantStatusList: applicantStatusList])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = onlineRegistrationService.applicantsPaginateList(params)

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

    def admitlist() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = onlineRegistrationService.applicantsAdmitPaginateList(params)

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

    def inactive(Long id) {
        LinkedHashMap result = onlineRegistrationService.inactive(id)
        String output = result as JSON
        render output
    }

    def selectApplicant(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        RegOnlineRegistration registration = RegOnlineRegistration.get(id)
        if (!registration) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
        }
        if (onlineRegistrationService.hasSelectRight()) {
            registration.applicantStatus = ApplicantStatus.Selected
            registration.save(flush: true)
            result.put(CommonUtils.MESSAGE, "Applicant selected successfully for admission.")
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        } else {
            result.put(CommonUtils.MESSAGE, "You don't have permission to select.")
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        }

        outPut = result as JSON
        render outPut
    }

    def removeApplicant(Long id) {
        LinkedHashMap result = onlineRegistrationService.removeApplicant(id)
        String output = result as JSON
        render output
    }


    def edit(Long id) {
        RegOnlineRegistration registration = RegOnlineRegistration.read(id)
        def classNameList = admissionFormService.classesDropDown()
        if (!registration) {
            redirect(action: 'index')
            return
        }
        render (view: 'edit', model: [registration: registration,classNameList:classNameList])
    }

    def create = {
        def classNameList = admissionFormService.classesDropDown()
        render(view: 'edit', model: [classNameList:classNameList])
    }

    def save(ApplicantEditCommand command){

        if (!request.method.equals('POST')) {
            flash.message="Request Method not allow here."
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message=errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        if (!schoolService.hasFeatureAccess()) {
            redirect(action: 'index')
            return
        }

//        def classNameList
        /*if (regForm.fromBirthDate && command.birthDate.before(regForm.fromBirthDate)){
            command.errors.reject('online.step1.fromBirthDate.invalid')
            classNameList = new ArrayList()
            classNameList.add(command.className)
            render(view: 'edit', model: [classNameList:classNameList, command: command])
            return
        }
        if (regForm.toBirthDate && command.birthDate.after(regForm.toBirthDate)){
            command.errors.reject('online.step1.toBirthDate.invalid')
            classNameList = new ArrayList()
            classNameList.add(command.className)
            render(view: 'edit', model: [classNameList:classNameList, command: command])
            return
        }*/

        RegOnlineRegistration  regOnlineRegistration

        if (command.regId) {
            regOnlineRegistration = RegOnlineRegistration.get(command.regId)
            if (!regOnlineRegistration) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            regOnlineRegistration.properties['name','fathersName','mobile','birthDate','bloodGroup','birthCertificateNo','nameBangla','gender','presentAddress','permanentAddress','email','fathersIncome','religion','nationality','fathersProfession','fathersIsalive','fathersMobile','mothersName','mothersIncome','mothersProfession','mothersIsalive','mothersMobile','legalGuardianName','legalGuardianProfession','relationWithLegalGuardian','legalGuardianMobile','preSchoolName','preSchoolClass','preSchoolAddress','preSchoolTcDate','hasBroOrSisInSchool','broOrSis1Id','broOrSis2Id'] = command.properties
            regOnlineRegistration.applicantStatus=  ApplicantStatus.Apply
            flash.message = ' Updated Successfully.'
        } else {
            RegForm regForm = RegForm.findByAcademicYearAndActiveStatusAndClassName(schoolService.schoolAdmissionYear(), ActiveStatus.ACTIVE, command.className)
            if (!regForm) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            regOnlineRegistration = new RegOnlineRegistration(command.properties)
            regOnlineRegistration.regFormId = regForm.id
            regOnlineRegistration.academicYear = regForm.academicYear
            regOnlineRegistration.serialNo = onlineRegistrationService.nextOnlineRegistrationSerial(regForm.academicYear.value, regForm.serialPrefix)
            regOnlineRegistration.applicantStatus=  ApplicantStatus.Apply
            flash.message = ' Added Successfully.'
        }

        //code for save image
        HttpServletRequest request = request
        def f = request.getFile("pImage")
        if (!f.empty) {
            if (regOnlineRegistration.imagePath) {
                try {
                    Boolean deleteStatus = uploadService.deleteImage(regOnlineRegistration.imagePath)
                } catch (Exception e) {
                    log.error("Delete Image"+ e)
                }
            }
            try {
                Image image = uploadService.uploadImageWithThumb(request, "pImage", "regOnlineRegistration")
                regOnlineRegistration.imagePath=image?.identifier
            } catch (Exception e) {
                log.error("Upload Student Image"+ e)
                flash.message=e.getMessage()
                redirect(action: 'index')
                return
            }
        }

        if (regOnlineRegistration.hasErrors() || !regOnlineRegistration.save(flush: true)) {
            def errorList = regOnlineRegistration?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('\n')
            redirect(action: 'index')
            return
        }
        redirect(action: 'index')


    }

    def applicantList() {
        String toDayStr = CommonUtils.getUiDateStrForPicker(new Date())
        def classNameList = admissionFormService.classesDropDown()
        render(view: '/open/online/applicantReportlist', model: [toDayStr:toDayStr,classNameList: classNameList])
    }

    def listAdmitCollection() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = null//collectionsService.manageCollectionPaginationList(params)
        int totalCount = 0//resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }




}
