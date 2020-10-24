package com.grailslab

import com.grailslab.command.ProfessionCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.Profession
import grails.converters.JSON
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class ProfessionService {

    def springSecurityService

    static final String[] sortColumns = ['id','name','description']
    LinkedHashMap professionPaginateList(GrailsParameterMap params){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = Profession.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('description', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { Profession profession ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: profession.id, 0: serial, 1: profession.name,2: profession.description, 3: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def saveOrUpdate(ProfessionCommand professionCommand) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError', true)
        if (professionCommand.hasErrors()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }

        String createOrUpdateBy = springSecurityService.principal.username
        Profession profession
        if (professionCommand.id) {
            profession = Profession.get(professionCommand.id)
            if (!profession) {
                result.put('message', 'Profession not found')
                return result
            }
            Profession ifExistClassName = Profession.findByNameAndActiveStatusAndIdNotEqual(professionCommand.name, ActiveStatus.ACTIVE, professionCommand.id)
            if (ifExistClassName) {
                result.put('message', 'Same Profession Name already Exist')
                return result
            }
            profession.properties = professionCommand.properties
            profession.updatedBy = createOrUpdateBy
            if (!profession.validate()) {
                result.put('message', 'Please fill the form correctly')
                return result
            }
            profession.save(flush: true)
            result.put('isError', false)
            result.put('message', 'Profession Updated successfully')
            return result
        }
        profession = new Profession(professionCommand.properties)
        profession.createdBy = createOrUpdateBy
        if (!profession.validate()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        Profession ifExistClassName = Profession.findByNameAndActiveStatus(profession.name, ActiveStatus.ACTIVE)
        if (ifExistClassName) {
            result.put('message', 'Same Profession Name already Exist')
            return result
        }
        Profession savedCurr = profession.save(flush: true)
        if (!savedCurr) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        result.put('isError', false)
        result.put('message', 'Profession Create successfully')
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        Profession profession = Profession.get(id)
        if (!profession) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (profession.activeStatus.equals(ActiveStatus.INACTIVE)) {
            profession.activeStatus = ActiveStatus.ACTIVE
        } else {
            profession.activeStatus = ActiveStatus.INACTIVE
        }
        profession.updatedBy = createOrUpdateBy
        profession.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, " Profession deleted successfully.")
        return result
    }
}
