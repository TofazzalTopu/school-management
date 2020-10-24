package com.grailslab

import com.grailslab.command.ClassRoomCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassRoom
import grails.web.servlet.mvc.GrailsParameterMap
import grails.gorm.transactions.Transactional

@Transactional
class ClassRoomService {
    def springSecurityService

    static final String[] sortColumns = ['id','name','description']

    LinkedHashMap classRoomPaginateList(GrailsParameterMap params){
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
        def c = ClassRoom.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)

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
            results.each { ClassRoom classRoom ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: classRoom.id, 0: serial, 1: classRoom.name, 2: classRoom.description, 3: ''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def classRoomDropDownList() {
        List dataReturns = new ArrayList()
        def c = ClassRoom.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            order("name", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { ClassRoom classRoom ->
            dataReturns.add([id: classRoom.id, name: classRoom.name])
        }
        return dataReturns
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        ClassRoom classRoom = ClassRoom.get(id)
        if (!classRoom) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        if (classRoom.activeStatus.equals(ActiveStatus.INACTIVE)) {
            classRoom.activeStatus = ActiveStatus.ACTIVE
        } else {
            classRoom.activeStatus = ActiveStatus.INACTIVE
        }
        classRoom.updatedBy = springSecurityService.principal.username
        classRoom.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Class Room deleted successfully.")
        return result
    }

    def saveOrUpdate(ClassRoomCommand classRoomCommand) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username

        ClassRoom classRoom
        if (classRoomCommand.id) { //update Currency
            classRoom = ClassRoom.get(classRoomCommand.id)
            if (!classRoom) {
                result.put('message', 'Class room not found')
                return result
            }
            ClassRoom ifExistClassRoom = ClassRoom.findByNameAndActiveStatusAndIdNotEqual(classRoomCommand.name, ActiveStatus.ACTIVE, classRoomCommand.id)
            if (ifExistClassRoom) {
                result.put('message', 'Same Class Room already Exist')
                return result
            }
            classRoom.properties = classRoomCommand.properties
            classRoom.updatedBy = createOrUpdate
            if (!classRoom.validate()) {
                result.put('message', 'Please fill the form correctly')
                return result
            }

            classRoom.save(flush: true)
            result.put('isError', false)
            result.put('message', 'Class room Updated successfully')
            return result
        }

        classRoom = new ClassRoom(classRoomCommand.properties)
        classRoom.createdBy = createOrUpdate
        if (!classRoomCommand.validate()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        ClassRoom ifExistClassRoom = ClassRoom.findByNameAndActiveStatus(classRoom.name, ActiveStatus.ACTIVE)
        if (ifExistClassRoom) {
            result.put('message', 'Same Class Room already Exist')
            return result
        }
        ClassRoom savedCurr = classRoom.save(flush: true)
        if (!savedCurr) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        result.put('isError', false)
        result.put('message', 'Class room Create successfully')
        return result
    }

}
