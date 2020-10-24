package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.SubjectCommand
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.SubjectName
import grails.converters.JSON

class SubjectController {
    def springSecurityService

    def subjectService

    def index() {
        LinkedHashMap resultMap = subjectService.subjectPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/subject', model: [dataReturn: null, totalCount: 0])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/subject', model: [dataReturn: resultMap.results, totalCount: totalCount])
    }



    def save(SubjectCommand subjectCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = subjectService.saveOrUpdate(subjectCommand)
        String outPut = result as JSON
        render outPut
    }

   /* def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut
        SubjectName subject = SubjectName.get(id)
        if(subject) {
            try {
                subject.activeStatus=ActiveStatus.INACTIVE
                subject.save(flush:true)
                result.put('isError',false)
                result.put('message',"Subject deleted successfully.")
                outPut = result as JSON
                render outPut
                return

            }

            catch(DataIntegrityViolationException e) {
                result.put('isError',true)
                result.put('message',"Subject could not deleted. Already in use.")
                outPut = result as JSON
                render outPut
                return
            }

        }
        result.put('isError',true)
        result.put('message',"Subject not found")
        outPut = result as JSON
        render outPut
        return
    }*/

    def inactive(Long id) {
        LinkedHashMap result = subjectService.inactive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =subjectService.subjectPaginateList(params)

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

    def childlist(Long id) {
        LinkedHashMap gridData
        String result

        SubjectName subjectName = SubjectName.read(id)
        if(!subjectName){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        LinkedHashMap resultMap = subjectService.childPaginateList(params, subjectName)

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
        SubjectName subject = SubjectName.read(id)
        if (!subject) {
            result.put('message','Subject not found')
            outPut = result as JSON
            render outPut
            return
        }
        result.put('isError',false)
        result.put('obj',subject)
        outPut = result as JSON
        render outPut
    }

    def addSubject(Long id) {
        SubjectName subjectName = SubjectName.read(id)
        if (!subjectName) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap resultMap = subjectService.childPaginateList(params, subjectName)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/addSubject', model: [dataReturn: null, totalCount: 0, subjectName: subjectName])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/addSubject', model: [subjectName: subjectName, dataReturn: resultMap.results, totalCount: totalCount])
    }
}
