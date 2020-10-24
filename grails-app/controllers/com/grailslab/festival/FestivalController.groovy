package com.grailslab.festival

import com.grailslab.CommonUtils
import com.grailslab.command.FestivalCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.report.CusJasperReportDef
import grails.converters.JSON

class FestivalController {
    def festivalService
    def messageSource
    def fesRegistrationService
    def classNameService
    def schoolService
    def jasperService
    def baseService
    def springSecurityService

    def index() {
        LinkedHashMap resultMap = festivalService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0)

        {
            render(view: '/festival/festivalList',model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/festival/festivalList',model: [dataReturn: resultMap.results, totalCount: totalCount])
    }
    def create() {
        render(view: '/festival/festivalCreateOrEdit')

    }
    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = festivalService.paginateList(params)

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
        FesProgram fesProgram = FesProgram.read(id)
        if (!fesProgram) {
            flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
            redirect(action: 'index')
            return
        }
        List selectedTopics = fesProgram.olympiadTopics?.split(",") as List
        render(view: '/festival/festivalCreateOrEdit', model: [fesProgram: fesProgram, selectedTopics: selectedTopics])
    }

    def save(FestivalCommand command) {
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

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        FesProgram festivalList
        if (command.id) {
            festivalList = FesProgram.get(command.id)
            if (!festivalList) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            festivalList.properties = command.properties
            festivalList.updatedBy = createOrUpdateBy
            flash.message = 'Program Updated Successfully.'
        } else {
            festivalList = new FesProgram(command.properties)
            festivalList.createdBy = createOrUpdateBy
            festivalList.academicYear = academicYear
            flash.message = 'Program Added Successfully.'
        }
        festivalList.save(flush: true)
        redirect(action: 'index')
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = festivalService.delete(id)
        String output = result as JSON
        render output
    }

    def registration(Long id) {
        def fesProgramList = festivalService.festivalDDList()
        if (!id && !fesProgramList) {
            render(view: '/festival/fesRegistrationList',model: [dataReturn: null, totalCount: 0, fesProgramList: fesProgramList])
            return
        }
        if (fesProgramList) {
            id = fesProgramList.first().id
        }
        FesProgram fesProgram = FesProgram.read(id)
        if (!fesProgram) {
            render(view: '/festival/fesRegistrationList',model: [dataReturn: null, totalCount: 0, fesProgramList: fesProgramList])
            return
        }

        LinkedHashMap resultMap = fesRegistrationService.paginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/festival/fesRegistrationList',model: [fesProgram: fesProgram, dataReturn: null, totalCount: 0, fesProgramList: fesProgramList])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/festival/fesRegistrationList',model: [fesProgram: fesProgram, dataReturn: resultMap.results, totalCount: totalCount, fesProgramList: fesProgramList])
    }

    def fesRegList() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = fesRegistrationService.paginateList(params)

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

    def fesRegDelete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'festival')
            return
        }

        LinkedHashMap result = festivalService.fesRegDelete(id)
        String output = result as JSON
        render output
    }

    def report(){
        def fesProgramList = festivalService.festivalDDList()
        render(view: '/festival/fesReport',model: [fesProgramList: fesProgramList])
    }

    def registrationTopics(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        FesProgram fesProgram = FesProgram.read(id)
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        def registrationTopicList = fesRegistrationService.registrationTopicList(fesProgram)

        if(!registrationTopicList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No topic added")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('registrationTopicList', registrationTopicList)
        outPut = result as JSON
        render outPut
        return
    }

    def registrationList(){
        if (!params.fesProgram){
            flash.message="Please select Program name first"
            redirect(action: 'index')
        }
        FesProgram fesProgram = FesProgram.read(params.getLong('fesProgram'))

        String sqlParams = ""
        if (params.topicName) {
            sqlParams = " and olympiad_topics like '%"+params.topicName+"%'"
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('fesProgram', fesProgram.id)
        paramsMap.put('programName', fesProgram.name)
        paramsMap.put('sqlParams', sqlParams)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Registration_List"
        jasperReport.reportName = REGISTRATION_LIST_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

        private static final String REGISTRATION_LIST_JASPER_FILE = 'festivalRegistrationList'
}
