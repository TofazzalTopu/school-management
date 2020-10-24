package com.grailslab.hr

import com.grailslab.CommonUtils
import com.grailslab.command.DesignationCommand
import com.grailslab.gschoolcore.ActiveStatus
import grails.converters.JSON

class HrDesignationController {
    def springSecurityService
    def messageSource
    def hrDesignationService

    def index() {
        def hrCategory = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
        LinkedHashMap resultMap = hrDesignationService.designationPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/hr/designation', model: [dataReturn: null, totalCount: 0, hrCategory:hrCategory])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/hr/designation', model: [dataReturn: resultMap.results, totalCount: totalCount, hrCategory:hrCategory])
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = hrDesignationService.designationPaginateList(params)

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

    def save(DesignationCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut=result as JSON
            render outPut
            return
        }
        /*LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut=result as JSON
            render outPut
            return
        }

        def refAlreadyExist
        if(command.id){
            refAlreadyExist = HrDesignation.findByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE, command.name, command.id)
        } else {
            refAlreadyExist = HrDesignation.findByActiveStatusAndName(ActiveStatus.ACTIVE,command.name)
        }
        if(refAlreadyExist){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            outPut=result as JSON
            render outPut
            return
        }

        HrDesignation designation
        if (command.id) {
            designation = HrDesignation.get(command.id)
            if (!designation) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                outPut=result as JSON
                render outPut
                return
            }
            designation.properties = command.properties
            designation.updatedBy = springSecurityService.principal.username
            designation.save(flash: true)
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Updated successfully')
        } else {
            designation = new HrDesignation(command.properties)
            designation.createdBy = springSecurityService.principal.username
            designation.save()
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Added successfully')
        }*/
        result = hrDesignationService.saveOrUpdate(command)
        outPut=result as JSON
        render outPut
    }

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        HrDesignation designation = HrDesignation.read(id)
        if (!designation) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, designation)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = hrDesignationService.inactive(id)
        String outPut = result as JSON
        render outPut
    }

}
