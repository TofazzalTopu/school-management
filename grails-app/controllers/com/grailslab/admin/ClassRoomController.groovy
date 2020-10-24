package com.grailslab.admin


import com.grailslab.command.ClassRoomCommand
import com.grailslab.settings.ClassRoom
import grails.converters.JSON

class ClassRoomController {
    def classRoomService

    def index() {
        LinkedHashMap resultMap = classRoomService.classRoomPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/classRoom', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/classRoom', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }

    def save(ClassRoomCommand classRoomCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError', true)
        String outPut
        if (classRoomCommand.hasErrors()) {
            result.put('message', 'Please fill the form correctly')
            outPut = result as JSON
            render outPut
            return
        }

        result = classRoomService.saveOrUpdate(classRoomCommand)
        outPut = result as JSON
        render outPut
    }

   /* def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut
        ClassRoom classRoom = ClassRoom.get(id)
        if(classRoom) {
            try {
                classRoom.activeStatus= ActiveStatus.INACTIVE
                classRoom.save(flush:true)
                result.put('isError',false)
                result.put('message',"Class room deleted successfully.")
                outPut = result as JSON
                render outPut
                return

            }

            catch(DataIntegrityViolationException e) {
                result.put('isError',true)
                result.put('message',"Class room could not deleted. Already in use.")
                outPut = result as JSON
                render outPut
                return
            }

        }
        result.put('isError',true)
        result.put('message',"Class room not found")
        outPut = result as JSON
        render outPut
        return
    }
*/
    def inactive(Long id) {
        LinkedHashMap result = classRoomService.inactive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =classRoomService.classRoomPaginateList(params)

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
        ClassRoom classRoom = ClassRoom.read(id)
        if (!classRoom) {
            result.put('message','Class room not found')
            outPut = result as JSON
            render outPut
            return
        }
        result.put('isError',false)
        result.put('obj',classRoom)
        outPut = result as JSON
        render outPut
    }
}
