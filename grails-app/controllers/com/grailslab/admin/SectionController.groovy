package com.grailslab.admin

import com.grailslab.command.SectionCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Section
import grails.converters.JSON

class SectionController {

    def classNameService
    def sectionService
    def schoolService
    def employeeService
    def classRoomService

    def index() {
        def teacherList = employeeService.teacherDropDownList()
        def classNameList =classNameService.allClassNames()
        def classRoomList = classRoomService.classRoomDropDownList()
        AcademicYear workingYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/section', model: [academicYearList:academicYearList,workingYear:workingYear, classRoomList:classRoomList,classNameList:classNameList,teacherList:teacherList,classList:classList])
    }

    def save(SectionCommand sectionCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = sectionService.saveOrUpdate(sectionCommand)
        String outPut = result as JSON
        render outPut
    }

  /*  def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut
        Section section = Section.get(id)
        if(section) {
            try {
                section.activeStatus= ActiveStatus.INACTIVE
                section.save(flush: true)
                result.put('isError',false)
                result.put('message',"Section deleted successfully.")
                outPut = result as JSON
                render outPut
                return

            }
            catch(DataIntegrityViolationException e) {
                result.put('isError',true)
                result.put('message',"Section could not deleted. Already in use.")
                outPut = result as JSON
                render outPut
                return
            }

        }
        result.put('isError',true)
        result.put('message',"Section not found")
        outPut = result as JSON
        render outPut
        return
    }*/

    def inactive(Long id) {
        LinkedHashMap result = sectionService.inactive(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =sectionService.sectionPaginateList(params)

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
        Section section = Section.read(id)
        if (!section) {
            result.put('message','Section name not found')
            outPut = result as JSON
            render outPut
            return
        }
        result.put('isError',false)
        result.put('obj',section)
        outPut = result as JSON
        render outPut
    }

     def moveSection() {
         AcademicYear academicYear
         if (params.academicYear) {
             academicYear = AcademicYear.valueOf(params.academicYear)
         } else {
             academicYear = schoolService.workingYear(null)
         }
         int year
         AcademicYear nextYear
         if (academicYear?.value.length() == 4) {
             year = Integer.parseInt(academicYear.value)
             year = year +1
             nextYear = AcademicYear.getYearByString(year.toString())
         } else {
             year = Integer.parseInt(academicYear?.value.substring(0, 4))
             int lowYear = year+1
             int highYear = year+2
             nextYear = AcademicYear.getYearByString(lowYear+"-"+highYear)
         }

         if(!nextYear){
             flash.message ="Next year yet not initiated."
             redirect(action: 'index')
             return
         }
         def nextYearSectionList = sectionService.sectionDropDownList(nextYear)
         List sectionIds
         if (nextYearSectionList && nextYearSectionList.size() > 0){
             sectionIds = nextYearSectionList.collect {it.id}
         }

        def sectionList = sectionService.sectionDropDownNotInList(academicYear, sectionIds)
        render(view: '/admin/sectionTransfer', model: [sectionList: sectionList, nextYearSectionList: nextYearSectionList, academicYear:academicYear])
    }

    def saveTransfer() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = sectionService.saveTransfer(params)
        String output = result as JSON
        render output
    }
}
