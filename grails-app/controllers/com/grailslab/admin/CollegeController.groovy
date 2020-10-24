package com.grailslab.admin

import com.grailslab.enums.AcaYearType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.Student

class CollegeController {

    def studentService
    def schoolService
    def sectionService
    def classNameService
    def messageSource
    def registrationService
    def studentSubjectsService
    def classSubjectsService
    def index() {
        AcademicYear academicYear = schoolService.collegeWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
//        params.academicYear = academicYear.key
//        LinkedHashMap resultMap = studentService.studentPaginateList(params)
        def classList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        render(view: '/admin/studentMgmt/student', model: [academicYearList:academicYearList, workingYear:academicYear, classList:classList])

    }

    def admission() {
        AcademicYear academicYear = schoolService.collegeWorkingYear()
        params.academicYear = academicYear.key
        LinkedHashMap resultMap = studentService.studentPaginateList(params)
        def classList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        def newStudents=registrationService.newRegistrationDropDownList()
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            render(view: '/admin/studentMgmt/admission', model: [newStudents:newStudents,classList:classList, workingYear:academicYear, dataReturn: null, totalCount: 0])
            return
        }
        render(view: '/admin/studentMgmt/admission', model: [newStudents:newStudents,classList:classList, workingYear:academicYear, dataReturn: resultMap.results, totalCount: totalCount])
    }

    def roleReorder(Long id){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        String createOrUpdateBy = springSecurityService?.principal.username
        switch (request.method) {
            case 'GET':
                ClassName className
                if(id){
                    className = ClassName.read(id)
                }
                AcademicYear academicYear
                if (params.academicYear){
                    academicYear = AcademicYear.valueOf(params.academicYear)
                }else{
                    academicYear = schoolService.collegeWorkingYear()
                }

                def resultMap
                if (className) {
                    resultMap = studentService.byClassName(className, academicYear)
                }
                render(view: '/admin/studentMgmt/roleReorder', model: [acaYearType: AcaYearType.COLLEGE, resultMap: resultMap, className:className, academicYear:academicYear, classNameList:classNameList, academicYearList:academicYearList])
                break
            case 'POST':
                Student student
                Integer newRollNo
                params.student.each{ studentId ->
                    try {
                        student = Student.get(Long.parseLong(studentId.key))
                        newRollNo = Integer.parseInt(studentId.value)
                        if (newRollNo && student.rollNo != newRollNo) {
                            student.rollNo = newRollNo
                            student.createdBy = createOrUpdateBy
                            student.save(flush: true)
                        }
                    } catch (e) {
                        log.error(e.message)
                    }
                }
                flash.message = "Student Roll(s) Updated Successfully"
                render(view: '/admin/studentMgmt/roleReorder', model: [acaYearType: AcaYearType.COLLEGE, academicYearList:academicYearList, classNameList: classNameList])
                break
        }
    }

    def reAdmission(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        render(view: '/admin/studentMgmt/reAdmission', model: [classNameList:classNameList, academicYearList: academicYearList])
    }
    def promotion(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.COLLEGE)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.COLLEGE)
        render(view: '/admin/studentMgmt/promotion', model: [classNameList:classNameList, academicYearList: academicYearList])
    }






}
