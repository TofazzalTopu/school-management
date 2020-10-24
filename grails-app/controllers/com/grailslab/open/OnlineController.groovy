package com.grailslab.open

import com.grailslab.CommonUtils
import com.grailslab.command.*
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.ApplicantStatus
import com.grailslab.enums.OlympiadType
import com.grailslab.festival.FesProgram
import com.grailslab.festival.FesRegistration
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.report.CusJasperReportDef
import com.grailslab.settings.ClassName
import com.grailslab.settings.Image
import com.grailslab.stmgmt.RegForm
import com.grailslab.stmgmt.RegOnlineRegistration
import com.grailslab.stmgmt.Registration

import javax.servlet.http.HttpServletRequest

class OnlineController {
    def classNameService
    def fesRegistrationService
    def messageSource
    def uploadService
    def schoolService
    def jasperService
    def onlineRegistrationService
    def baseService
    def springSecurityService

    def index() {
        redirect(action: 'apply')
    }

    def apply() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def regFormList = RegForm.findAllByRegCloseDateGreaterThanEqualsAndAcademicYearAndActiveStatus(new Date().clearTime(), schoolService.schoolAdmissionYear(), ActiveStatus.ACTIVE)
        render(view: '/open/online/apply', layout: layoutName, model: [applyList: null, regFormList: regFormList])
    }

    def festival(Long id, FesRegistrationCommand command) {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        FesProgram fesProgram = FesProgram.read(id)
        if(!fesProgram || fesProgram.activeStatus != ActiveStatus.ACTIVE){
            redirect(action: 'apply')
            return
        }
        String programDate = CommonUtils.getDateRangeStr(fesProgram.startDate, fesProgram.endDate)
        List olympiadTopicList = new ArrayList()
        if (fesProgram.olympiadTopics) {
            def topics = fesProgram.olympiadTopics.split(",")
            OlympiadType olympiadType
            topics.each { topic ->
                olympiadType = OlympiadType.valueOf(topic)
                if(olympiadType) {
                    olympiadTopicList.add(olympiadType)
                }
            }
        }
        switch (request.method) {
            case 'GET':
                render(view: '/open/online/festival', model: [fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                break
            case 'POST':
                if (command.hasErrors()) {
                    def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                    flash.message=errorList?.join('\n')
                    render(view: '/open/online/festival', model: [command: command, fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                    return
                }
                if (command.scientificPaper) {
                    if (!command.spProjectName) {
                        flash.message = "Please Fill up Project Name and other details if you want to participate Scientific Paper/Project of ${OlympiadType.science.value} Olympiad"
                        render(view: '/open/online/festival', model: [command: command, fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                        return
                    }
                    if (command.spStudentName) {
                        if (!command.spClassName || !command.spRollNo || !command.spContactNo) {
                            flash.message = "Please Fill up All Information of your 2nd participant for Scientific Paper/Project of ${OlympiadType.science.value}"
                            render(view: '/open/online/festival', model: [command: command, fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                            return
                        }
                    }
                }
                if (command.slideShow) {
                    if (!command.ssProjectName) {
                        flash.message = "Please Fill up Project Name and other details if you want to participate Scientific Slide Show Presentation of ${OlympiadType.science.value} Olympiad"
                        render(view: '/open/online/festival', model: [command: command, fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                        return
                    }
                    if (command.ssStudentName) {
                        if (!command.ssClassName || !command.ssRollNo || !command.ssContactNo) {
                            flash.message = "Please Fill up All Information of your 2nd participant for Scientific Slide Show Presentation of ${OlympiadType.science.value}"
                            render(view: '/open/online/festival', model: [command: command, fesProgram: fesProgram, programDate: programDate, olympiadTopicList: olympiadTopicList,classNameList:classNameList])
                            return
                        }
                    }
                }
                FesRegistration fesRegistration = new FesRegistration(command.properties)
                fesRegistration.serialNo=fesRegistrationService.nextSerialNo()
                fesRegistration.fesProgram = fesProgram
                fesRegistration.academicYear = fesProgram.academicYear
                fesRegistration.createdBy = springSecurityService.principal?.username
                FesRegistration savedRegi = fesRegistration.save(flush: true)
                String message = "Can't Process your registration now. Please try later or contact helpline"
                Boolean isSuccess = false
                if (savedRegi) {
                    isSuccess = true
                    message = "You've successfully registered for ${fesProgram.name}. Your Serial Number ${savedRegi.serialNo}."
                }
                render(view: '/open/online/registrationSuccess', model: [isSuccess: isSuccess, message: message])
                break
        }
    }

    def step1(Long id){
        RegForm regForm = RegForm.read(id)
        if (!regForm || regForm.activeStatus != ActiveStatus.ACTIVE) {
            flash.message="Application form is not valid"
            redirect(action: "apply")
            return
        }
        if (regForm.regOpenDate.after(new Date()) || regForm.regCloseDate.before(new Date())){
            flash.message="Registration is not open or Already closed"
            redirect(action: "apply")
            return
        }
        ClassName className = regForm.className
        def classNameList = new ArrayList()
        classNameList.add(className)
        RegOnlineRegistration registration
        if (params.refId){
            registration = onlineRegistrationService.getRegistration(params.refId)
        }
        render(view: '/open/online/page1', model: [registration:registration, classNameList:classNameList, regForm:regForm])

    }

    def step2(OnlineStep1Command command){
        switch (request.method) {
            case 'GET':
                redirect(action: "apply")
                break
            case 'POST':
                def classNameList
                if (command.hasErrors()) {
                    classNameList = new ArrayList()
                    classNameList.add(command.className)
                    render(view: '/open/online/page1', model: [classNameList:classNameList, command: command,regForm: command.regForm])
                    return
                }
                RegForm regForm = command.regForm
                if (regForm.fromBirthDate && command.birthDate.before(regForm.fromBirthDate)){
                    command.errors.reject('online.step1.fromBirthDate.invalid')
                    classNameList = new ArrayList()
                    classNameList.add(command.className)
                    render(view: '/open/online/page1', model: [classNameList:classNameList, command: command,regForm: command.regForm])
                    return
                }
                if (regForm.toBirthDate && command.birthDate.after(regForm.toBirthDate)){
                    command.errors.reject('online.step1.toBirthDate.invalid')
                    classNameList = new ArrayList()
                    classNameList.add(command.className)
                    render(view: '/open/online/page1', model: [classNameList:classNameList, command: command,regForm: command.regForm])
                    return
                }
                RegOnlineRegistration registration = RegOnlineRegistration.findByRegFormIdAndNameAndFathersNameAndBirthDateAndActiveStatus(regForm.id, command.name, command.fathersName, command.birthDate, ActiveStatus.ACTIVE)
                if (registration) {
                    if (registration.applicantStatus != ApplicantStatus.Draft && registration.serialNo) {
                        redirect(action: 'preview', params: [refNo: registration.serialNo])
                        return
                    }
                    if (!registration.serialNo) {
                        registration.serialNo = onlineRegistrationService.nextOnlineRegistrationSerial(regForm.academicYear.value, regForm.serialPrefix)
                        registration.save(flush: true)
                    }
                    render(view: '/open/online/page2', model: [registration: registration, command: new OnlineStep2Command()])
                    return
                } else {
                    registration = new RegOnlineRegistration(command.properties)
                    registration.regFormId = regForm.id
                    registration.academicYear = regForm.academicYear
                    registration.serialNo = onlineRegistrationService.nextOnlineRegistrationSerial(regForm.academicYear.value, regForm.serialPrefix)
                    RegOnlineRegistration saveItem = registration.save(flush: true)
                    render(view: '/open/online/page2', model: [registration: saveItem, command: new OnlineStep2Command()])
                    return
                }
                break
        }
    }

    def step3(OnlineStep2Command command){
        switch (request.method) {
            case 'GET':
                redirect(action: 'apply')
                break
            case 'POST':
                RegOnlineRegistration registration = RegOnlineRegistration.get(command.regId)
                if (!registration) {
                    redirect(action: 'apply')
                    return
                }
                if (command.hasErrors()) {
                   render(view: '/open/online/page2', model:[registration: registration, command: command])
                    return
                }
                if (params.submitBtn && params.submitBtn=="saveAsDraft") {
                    registration.properties=command.properties
                    registration.save(flush: true)
                    flash.message ="Your Application saved as draft. Serial no: ${registration.serialNo}."
                    redirect(action: 'apply')
                    return
                }
                registration.properties=command.properties
                RegOnlineRegistration saveItem = registration.save(flush: true)
                Registration broOrSis
                OnlineStep3Command onlineStep3Command
                if (command.hasBroOrSisInSchool && command.broOrSis1Id) {
                    broOrSis = Registration.findByActiveStatusAndStudentID(ActiveStatus.ACTIVE, command.broOrSis1Id)
                    if (broOrSis) {
                        onlineStep3Command = new OnlineStep3Command(fathersIncome:broOrSis.fathersIncome, permanentAddress: broOrSis.permanentAddress, presentAddress: broOrSis.presentAddress, mothersName:broOrSis.mothersName)
                    } else {
                        onlineStep3Command = new OnlineStep3Command()
                    }
                } else {
                    onlineStep3Command = new OnlineStep3Command()
                }
                render(view: '/open/online/page3', model: [registration: saveItem, command: onlineStep3Command])
                return
                break
        }

    }

    def step4(OnlineStep3Command command){
        switch (request.method) {
            case 'GET':
                redirect(action: "apply")
                break
            case 'POST':
                RegOnlineRegistration registration = RegOnlineRegistration.get(command.regId)
                if (!registration) {
                    redirect(action: 'apply')
                    return
                }
                if (command.hasErrors()) {
                    render(view: '/open/online/page3', model:[registration: registration, command: command])
                    return
                }
                if (params.submitBtn && params.submitBtn=="saveAsDraft") {
                    registration.properties=command.properties
                    registration.save(flush: true)
                    flash.message ="Your Application saved as draft. Serial no: ${registration.serialNo}."
                    redirect(action: 'apply')
                    return
                }
                registration.properties=command.properties
                RegOnlineRegistration saveItem = registration.save(flush: true)
                render(view: '/open/online/page4', model: [registration: saveItem, command:  new OnlineStep4Command()])
                return

                break
        }
    }

    def step5(OnlineStep4Command command){
        switch (request.method) {
            case 'GET':
                redirect(action: "apply")
                break
            case 'POST':
                RegOnlineRegistration registration = RegOnlineRegistration.get(command.regId)
                if (!registration) {
                    redirect(action: 'apply')
                    return
                }
                /*if(registration.className.sortPosition>1){
                    if (!command.preSchoolName){
                        flash.message ="Previous school information required."
                        render(view: '/open/online/page4', model:[registration: registration, command: command])
                        return
                    }
                }*/
                if (command.hasErrors()) {
                    render(view: '/open/online/page4', model:[registration: registration, command: command])
                    return
                }
                if (params.submitBtn && params.submitBtn=="saveAsDraft") {
                    registration.properties=command.properties
                    registration.save(flush: true)
                    flash.message ="Your Application saved as draft. Serial no: ${registration.serialNo}."
                    redirect(action: 'apply')
                    return
                }
                registration.properties=command.properties
                RegOnlineRegistration saveItem = registration.save(flush: true)
                render(view: '/open/online/page5', model: [registration: saveItem, command: command])
                return
                break
        }
    }

    def preview(OnlineStep5Command command){
        println(params)
        switch (request.method) {
            case 'GET':
                if (!params.refNo) {
                    flash.message="Please provide a valid serial No."
                    redirect(action: "apply")
                    break
                }
                RegOnlineRegistration registration = onlineRegistrationService.getRegistration(params.refNo)
                if (!registration) {
                    flash.message="No Application found with the provided ref no."
                    redirect(action: "apply")
                    break
                }
                if (!registration.serialNo || registration.applicantStatus == ApplicantStatus.Draft) {
                    redirect(action: 'step1', params: [id: registration.regFormId, refId: registration.serialNo])
                    return
                }
                render(view: '/open/online/regPreview', model: [registration: registration, readOnlyMode: true])
                return
                break
            case 'POST':
                RegOnlineRegistration registration = RegOnlineRegistration.get(command.regId)
                if (command.hasErrors()) {
                    render(view: '/open/online/page5', model:[registration: registration, command: command])
                    return
                }
                if (command.regId) {
                    //code for save image
                    HttpServletRequest request = request
                    def f = request.getFile("pImage")
                    if (!f.empty) {
                        try {
                            if (f.size > 402450) {
                                command.errors.reject('regform.imageSize.invalid')
                                render(view: '/open/online/page5', model:[registration: registration, command: command])
                                return
                            }
                            Image image = uploadService.uploadImageWithThumb(request, "pImage", "onlineApply${registration.academicYear.value}")
                            registration.imagePath=image?.identifier
                        } catch (Exception e) {
                            log.error("Upload Slider Image"+ e)
                            flash.message=e.getMessage()
                            render(view: '/open/online/page5', model:[registration: registration, command: command])
                            return
                        }
                    } else {
                        if (!registration.imagePath) {
                            command.errors.reject('regform.photo.required')
                            render(view: '/open/online/page5', model:[registration: registration, command: command])
                            return
                        }
                    }
                    RegOnlineRegistration saveItem = registration.save(flush: true)
                    render(view: '/open/online/regPreview', model: [registration: saveItem])
                    return
                }
                break
        }
    }

    def submit(Long id){
        switch (request.method) {
            case 'GET':
                redirect(action: "apply")
                break
            case 'POST':
                RegOnlineRegistration registration = RegOnlineRegistration.get(id)
                if (params.submitBtn && params.submitBtn=="saveAsDraft") {
                    registration.applicantStatus=  ApplicantStatus.Draft
                    registration.save(flush: true)
                    flash.message ="Your Application saved as draft. Serial no: ${registration.serialNo}."
                    redirect(action: 'apply')
                    return
                } else if (params.submitBtn && params.submitBtn=="printPdf") {
                    redirect(action: 'formReport',params: [refId: registration.serialNo])
                    return
                } else {
                    registration.applicantStatus=  ApplicantStatus.Apply
                    registration.save(flush: true)
                    redirect(action: 'formReport',params: [refId: registration.serialNo])
                }
                break
        }
    }

    def formReport(String refId) {
        if (!refId) {
            redirect(controller: 'online', action: 'apply')
            return
        }

        RegOnlineRegistration form = onlineRegistrationService.getRegistration(refId)
        if (!form) {
            redirect(action: 'apply')
            return
        }
        RegForm regForm = RegForm.read(form.regFormId)
        if (!regForm) {
            redirect(action: 'apply')
            return
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('formId', form.id)
        paramsMap.put('extraInfo', regForm?.regFormNote)
        paramsMap.put('imageFile', uploadService.getImageInputStream(form.imagePath))


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Application"
        jasperReport.reportName = ADMISSION_FORM_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat("PDF")
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    private static final String ADMISSION_FORM_JASPER_FILE = 'applicantsForm'

}
