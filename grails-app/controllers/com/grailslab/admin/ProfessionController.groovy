package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.ProfessionCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Profession
import grails.converters.JSON

class ProfessionController {

    def springSecurityService
    def professionService

    def index() {
        LinkedHashMap resultMap = professionService.professionPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/profession', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/profession', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def save(ProfessionCommand professionCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = professionService.saveOrUpdate(professionCommand)
        String outPut = result as JSON
        render outPut
    }

    /*def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut
        Profession profession = Profession.get(id)
        if(profession) {
            try {
                profession.activeStatus=ActiveStatus.INACTIVE
                profession.save(flush:true)
                result.put('isError',false)
                result.put('message',"Profession deleted successfully.")
                outPut = result as JSON
                render outPut
                return

            }

            catch(DataIntegrityViolationException e) {
                result.put('isError',true)
                result.put('message',"Profession could not deleted. Already in use.")
                outPut = result as JSON
                render outPut
                return
            }

        }
        result.put('isError',true)
        result.put('message',"Profession not found")
        outPut = result as JSON
        render outPut
        return
    }*/

    def inactive(Long id) {
        LinkedHashMap result = professionService.inactive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =professionService.professionPaginateList(params)

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

    def edit(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut
        Profession profession = Profession.read(id)
        if (!profession) {
            result.put('message','Profession name not found')
            outPut = result as JSON
            render outPut
            return
        }
        result.put('isError',false)
        result.put('obj',profession)
        outPut = result as JSON
        render outPut
    }
}
