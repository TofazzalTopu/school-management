package com.grailslab.library

import com.grailslab.CommonUtils
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON
import org.grails.web.json.JSONObject

class LibrarySettingController {

    def springSecurityService
    def librarySettingService
    def schoolService

    def index() {

        /*def libraryConfigList = librarySettingService.confList()
        render(view: '/library/configuration', model: [libraryConfigList: libraryConfigList])
        */
        render(view: '/library/configuration')
    }

    def list() {
        println(params)
        Integer pageNumber = params.getInt('pageNumber')
        def abc = params.result
        Integer totalPerPage = params.getInt('totalPerPage')
        Integer offset, max
        if(pageNumber && totalPerPage) {
            offset = (pageNumber-1) * totalPerPage
            max = offset + totalPerPage
        }
        def libraryConfigList = LibraryConfig.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: totalPerPage, offset: offset])
        def responseData =[items: libraryConfigList, total: LibraryConfig.findAllByActiveStatus(ActiveStatus.ACTIVE).size()]
        render responseData as JSON
    }

    def edit() { }

    def save() {
        JSONObject saveConfig = JSON.parse(request.JSON.toString())
        def responseData
        def isExist
        LibraryConfig libraryConfig
        LibraryConfig savedConfiguration
        String createOrUpdateBy = springSecurityService.principal?.username

        if (saveConfig.id == 0) {
            /*isExist = LibraryConfig.countByMemberTypeAndActiveStatus(saveConfig.memberType, ActiveStatus.ACTIVE)
            if (isExist > 0){
                responseData = [hasError: true, 'errorMsg': 'This user already exist !']
                render responseData as JSON
                return
            }*/
            libraryConfig = new LibraryConfig(memberType: saveConfig.memberType, allowedDays: saveConfig.allowedDays, numberOfBook: saveConfig.numberOfBook,
                    fineAmount: schoolService.getJsonValue(saveConfig, 'fineAmount'),
                    memberFee: schoolService.getJsonValue(saveConfig, 'memberFee'))
            libraryConfig.createdBy = createOrUpdateBy
            savedConfiguration = libraryConfig.save(flush: true)
            responseData = ['hasError': false, 'message': 'Save successfully', id: savedConfiguration.id]
            render responseData as JSON
        } else {
            /* Long id = Long.valueOf(saveConfig.id)
             isExist = LibraryConfig.countByMemberTypeAndActiveStatusAndIdNotEqual(saveConfig.memberType, ActiveStatus.ACTIVE, id)
             if (isExist > 0) {
                 responseData = ['hasError': true, 'errorMsg': 'This user already exist !']
                 render responseData as JSON
                 return
             }*/
            def fineAmount = schoolService.getJsonValue(saveConfig, 'fineAmount')
            def memberFee = schoolService.getJsonValue(saveConfig, 'memberFee')
            libraryConfig = LibraryConfig.get(saveConfig.getLong("id"))
            libraryConfig.memberType = saveConfig.memberType
            libraryConfig.allowedDays = saveConfig.optInt("allowedDays")
            libraryConfig.numberOfBook = saveConfig.optInt("numberOfBook")
            libraryConfig.fineAmount = fineAmount ? Double.parseDouble(fineAmount) : null
            libraryConfig.memberFee = memberFee ? Double.parseDouble(memberFee) : null
            libraryConfig.updatedBy = createOrUpdateBy
            savedConfiguration = libraryConfig.save(flush: true)
            responseData = ['hasError': false, 'updateMsg': 'Update successfully', id: savedConfiguration.id]
        }
        render responseData as JSON
    }

    def delete() {
        JSONObject deleteConfig = JSON.parse(request.JSON.toString())
        Long id = Long.valueOf(deleteConfig.id)
        LibraryConfig libraryConfig = LibraryConfig.get(id)
        libraryConfig.activeStatus = ActiveStatus.DELETE
        libraryConfig.updatedBy = springSecurityService.principal?.username
        libraryConfig.save(flush: true)
        def responseData = ['hasError': false, 'message': '', successMsg: "deleted successfully"]
        render responseData as JSON
    }
}
