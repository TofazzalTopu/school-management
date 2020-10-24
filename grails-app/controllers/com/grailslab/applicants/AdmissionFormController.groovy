package com.grailslab.applicants

import com.grailslab.CommonUtils
import com.grailslab.SchoolService
import com.grailslab.command.RegFormCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegForm
import grails.converters.JSON
import org.joda.time.DateTime

class AdmissionFormController {

    def springSecurityService
    def classNameService
    def admissionFormService
    SchoolService schoolService

    def index() {
        LinkedHashMap resultMap = admissionFormService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admissionForm/admissionFormList', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admissionForm/admissionFormList', model: [dataReturn: resultMap.results, totalCount: totalCount])

    }

    def create(long id) {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classList = classNameService.classNameDropDownList(AcaYearType.ALL)
         render(view: '/admissionForm/admissionCreateOrEdit' , model: [ classList:classList, academicYearList: academicYearList])

    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = admissionFormService.paginateList(params)

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

    def save(RegFormCommand command){
        String createOrUpdateBy = springSecurityService.principal?.username

        switch (request.method) {
            case 'GET':
                redirect(action: 'create')
                break
            case 'POST':
                def classList
                if (command.hasErrors()) {
                    classList = classNameService.classNameDropDownList(AcaYearType.ALL)
                    render(view: '/admissionForm/admissionCreateOrEdit', model: [classList: classList, command: command])
                    return
                }
                DateTime regCloseDateTime = new DateTime(command.regCloseDate).plusHours(23).plusMinutes(59)
                Date toBirthDate
                if (command.toBirthDate) {
                    DateTime birthCloseDateTime = new DateTime(command.toBirthDate).plusHours(23).plusMinutes(59)
                    toBirthDate = birthCloseDateTime.toDate()
                }
                ClassName pName
                RegForm regForm
                if (command.regFormId) {
                    RegForm regFormedit = RegForm.get(command.regFormId)
                    regFormedit.properties["regOpenDate", "fromBirthDate", "instruction", "regFormNote", "applyType", "formPrice", "bandSchool","serialPrefix","applicationPrefix"] = command.properties
                    regFormedit.regCloseDate = regCloseDateTime.toDate()
                    regFormedit.toBirthDate = toBirthDate
                    regFormedit.updatedBy = createOrUpdateBy
                    regFormedit.save(flush: true)
                    redirect(action: 'index')
                    return

                } else {
                    //Form submit no
                    def classIds = command.schoolClassIds.split(",")
                    classIds.each { idx ->
                        pName = ClassName.read(Long.valueOf(idx))
                        regForm = RegForm.findByClassNameAndAcademicYearAndActiveStatus(pName, command.academicYear, ActiveStatus.ACTIVE)
                        if (regForm) {
                            regForm.properties["regOpenDate", "fromBirthDate", "instruction", "regFormNote", "applyType", "formPrice", "bandSchool","serialPrefix","applicationPrefix"] = command.properties
                            regForm.regCloseDate = regCloseDateTime.toDate()
                            regForm.toBirthDate = toBirthDate
                            regForm.updatedBy = createOrUpdateBy
                        } else {
                            regForm = new RegForm(command.properties)
                            regForm.regCloseDate = regCloseDateTime.toDate()
                            regForm.toBirthDate = toBirthDate
                            regForm.className = pName
                            regForm.createdBy = createOrUpdateBy
                        }
                        regForm.save(flush: true)
                    }
                    redirect(action: 'index')
                    break
                }
        }
    }

    def edit(Long id) {
        RegForm regAdmissionForm = RegForm.read(id)
        if (!regAdmissionForm) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        render(view: '/admissionForm/admissionCreateOrEdit', model: [regAdmissionForm:regAdmissionForm])
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = admissionFormService.delete(id)
        String output = result as JSON
        render output
    }
}
