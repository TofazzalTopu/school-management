package com.grailslab

import com.grailslab.command.CommonCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Board
import com.grailslab.hr.HrCertification
import com.grailslab.hr.Institution
import grails.converters.JSON
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class HrSettingsService {
    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['name']

    LinkedHashMap boardPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Board.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { Board board ->
                dataReturns.add([DT_RowId: board.id, 0: board.name, 1: board.description, 2: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap institutionPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Institution.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { Institution institution ->
                dataReturns.add([DT_RowId: institution.id, 0: institution.name, 1: institution.description, 2: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap certificationPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = HrCertification.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { HrCertification certification ->
                dataReturns.add([DT_RowId: certification.id, 0: certification.name, 1: certification.description, 2: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def saveOrUpdate(CommonCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        def refAlreadyExist
        if (command.id) {
            refAlreadyExist = Board.findByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE, command.name, command.id)
        } else {
            refAlreadyExist = Board.findByActiveStatusAndName(ActiveStatus.ACTIVE, command.name)
        }
        if (refAlreadyExist) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            return result
        }
        Board board
        String createOrUpdateBy = springSecurityService.principal.username
        if (command.id) {
            board = Board.get(command.id)
            if (!board) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            board.properties = command.properties
            board.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Updated successfully')
        } else {
            board = new Board(command.properties)
            board.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Added successfully')
        }
        board.save(flush: true)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Board board = Board.get(id)
        if (!board) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        board.activeStatus = ActiveStatus.INACTIVE
        board.updatedBy = springSecurityService.principal.username
        board.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        return result
    }

    def certificationSave(CommonCommand command) {
        String createOrUpdateBy = springSecurityService.principal?.username
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
           return result
        }
        def refAlreadyExist
        if(command.id){
            refAlreadyExist = HrCertification.findByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE,command.name,command.id)
        } else {
            refAlreadyExist = HrCertification.findByActiveStatusAndName(ActiveStatus.ACTIVE,command.name)
        }
        if(refAlreadyExist){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            return result
        }
        HrCertification certification
        if (command.id) {
            certification = HrCertification.get(command.id)
            if (!certification) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            certification.properties = command.properties
            certification.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Updated successfully')
        } else {
            certification = new HrCertification(command.properties)
            certification.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put(CommonUtils.MESSAGE,'Data Added successfully')
        }
        certification.save(flush: true)
        return result
    }

    def certificationInactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        HrCertification certification = HrCertification.get(id)
        if (!certification) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        certification.activeStatus = ActiveStatus.INACTIVE
        certification.updatedBy = springSecurityService.principal?.username
        certification.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        return result
    }

    def institutionSave(CommonCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
           return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        def refAlreadyExist
        if (command.id) {
            refAlreadyExist = Institution.findByActiveStatusAndNameAndIdNotEqual(ActiveStatus.ACTIVE, command.name, command.id)
        } else {
            refAlreadyExist = Institution.findByActiveStatusAndName(ActiveStatus.ACTIVE, command.name)
        }
        if (refAlreadyExist) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Name already exist.")
            return result
        }
        Institution institution
        if (command.id) {
            institution = Institution.get(command.id)
            if (!institution) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            institution.properties = command.properties
            institution.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Updated successfully')
        } else {
            institution = new Institution(command.properties)
            institution.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Data Added successfully')
        }
        institution.save(flush: true)
        return result
    }

    def institutionInactive(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        Institution institution = Institution.get(id)
        if (!institution) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        institution.activeStatus = ActiveStatus.INACTIVE
        institution.updatedBy = springSecurityService.principal?.username
        institution.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        return result
    }

    def institutionDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Institution institution = Institution.get(id)
        if (!institution) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            institution.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Data Deleted Successfully.")

        } catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Data already in use. You Can Inactive Reference")
        }
        return result
    }
}
