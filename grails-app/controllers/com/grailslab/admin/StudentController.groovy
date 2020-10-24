package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.*
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import com.grailslab.stmgmt.TcAndDropOut
import grails.converters.JSON

class StudentController {

    def studentService
    def schoolService
    def sectionService
    def classNameService
    def messageSource
    def registrationService
    def studentSubjectsService
    def classSubjectsService
    def examScheduleService
    def shiftExamService
    def examService
    def tabulationService
    def tabulationCtService
    def springSecurityService


    def index() {
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentMgmt/student', model: [academicYearList:academicYearList, workingYear:academicYear, classList:classList])
    }

    def admission() {
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)

        def newStudents=registrationService.newRegistrationDropDownList()

        render(view: '/admin/studentMgmt/admission', model: [newStudents:newStudents,classList:classList, workingYear:academicYear])
    }

    def admission2() {
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        LinkedHashMap resultMap = studentService.studentPaginateList(params)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            render(view: '/admin/studentMgmt/admission2', model: [classList:classList, workingYear:academicYear, dataReturn: null, totalCount: 0])
            return
        }
        render(view: '/admin/studentMgmt/admission2', model: [classList:classList, workingYear:academicYear, dataReturn: resultMap.results, totalCount: totalCount])
    }

    def report(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentMgmt/stdMgmtReport', model: [classNameList: classNameList, academicYearList: academicYearList])
    }

    def analysis(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        render(view: '/admin/studentMgmt/studentAnalysis', model: [classNameList: classNameList, academicYearList: academicYearList])
    }

    def registrationTypeAheadList(){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        String sSearch = params.q
        def studentList = registrationService.newRegistrationDropDownList2(sSearch)
        result.put('items', studentList)
        outPut = result as JSON
        render outPut
    }

    def saveAdmission(StudentCommand studentCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentService.saveOrUpdateAdmission(studentCommand)
        String outPut = result as JSON
        render outPut
    }

    def saveStudent(StudentCommand studentCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = studentService.saveOrUpdateStudent(studentCommand)
        String outPut = result as JSON
        render outPut
    }

    def saveReAdmission(StudentReAdmissionCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentService.saveOrUpdateReAdmission(command)
        String outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentService.deleteStudent(id)
        String output = result as JSON
        render output
    }

    def list() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap =studentService.studentPaginateList(params)

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
        String outPut
        Student student = Student.read(id)
        if (!student || student.studentStatus != StudentStatus.NEW) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        int studentYear = Integer.valueOf(student.academicYear.value)
        Calendar now = Calendar.getInstance();
        if (studentYear < now.get(Calendar.YEAR)) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Can't delete old student")
            outPut = result as JSON
            render outPut
            return
        }

        if(student.promotionStatus ==PromotionStatus.PROMOTED){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Student already promoted.")
            outPut = result as JSON
            render outPut
            return
        }
        //If any fee payment, then allow rollNo edit only
        String feePaidMsg = "Only RollNo and Section Transfer can update. For class change, please contact admin"
        String studentID=student.studentID
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('obj',student)
        result.put('studentID',studentID)
        result.put('feePaidMsg',feePaidMsg)
        result.put('studentName',student.name)
        result.put('className',student.className.name)
        result.put('sectionName',student.section.name)
        outPut = result as JSON
        render outPut
    }

    def roleReorder(Long id){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
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
                    academicYear = schoolService.schoolWorkingYear()
                }

                def resultMap
                if (className) {
                    resultMap = studentService.byClassName(className, academicYear)
                }
                render(view: '/admin/studentMgmt/roleReorder', model: [acaYearType: AcaYearType.SCHOOL, resultMap: resultMap, className:className, academicYear:academicYear, classNameList:classNameList, academicYearList:academicYearList])
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
                            student.updatedBy = createOrUpdateBy
                            student.save(flush: true)
                        }
                    }catch (e){
                        log.error(e.message)
                    }
                }
                flash.message = "Student Roll(s) Updated Successfully"
                render(view: '/admin/studentMgmt/roleReorder', model: [acaYearType: AcaYearType.SCHOOL, academicYearList:academicYearList, classNameList: classNameList])
                break
        }
    }

    def examStudentList(Long id){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        Exam exam = Exam.read(id)
        String message
        if(!exam || exam.activeStatus != ActiveStatus.ACTIVE){
            message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            outPut = result as JSON
            render outPut
            return
        }
        if(exam.examStatus == ExamStatus.NEW){
            message ="Result not ready"
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            outPut = result as JSON
            render outPut
            return
        }
        def studentList = studentService.sectionStudentDDList(exam.section)
        if(!studentList){
            message ="No Student for this Section and Academic Year."
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('studentList', studentList)
        outPut = result as JSON
        render outPut
        return
    }

    def reAdmission(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentMgmt/reAdmission', model: [classNameList:classNameList, academicYearList: academicYearList])
    }
    def bulkPromotion(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        render(view: '/admin/studentMgmt/bulkPromotion', model: [classNameList:classNameList, academicYearList: academicYearList])
    }
    def saveBulkPromotion() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        Section oldSection = Section.read(params.getLong('oldSectionId'))
        AcademicYear newYear
        if (params.academicYear){
            newYear = AcademicYear.valueOf(params.academicYear)
        }
        Section newSection = Section.read(params.getLong('section'))
        String createOrUpdateBy = springSecurityService.principal?.username
        List<Student> oldStudentList = studentService.studentListForBulkPromotion(oldSection)
        if (!oldStudentList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No student available for Promotion. Please check again or contact admin")
            outPut = result as JSON
            render outPut
            return
        }
        for (student in oldStudentList) {
            if (student.section.id != newSection.id) {
                studentService.saveBatchPromotionByCls(newYear, student, newSection, student.rollNo, createOrUpdateBy)
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Bulk Promotion successfully")
        outPut = result as JSON
        render outPut
    }
    def sectionTransfer(){
        def examNameList = shiftExamService.examNameDropDown()
        ShiftExam shiftExam
        if (examNameList) {
            def lastExam = examNameList.last()
            shiftExam = ShiftExam.read(lastExam.id)
        }
        def classNameList = classNameService.classNameInIdListDD(shiftExam?.classIds);
        switch (request.method) {
            case 'GET':
                render(view: '/admin/studentMgmt/sectionTransfer', model: [classNameList: classNameList, examNameList: examNameList, shiftExam:shiftExam])
                break
            case 'POST':
                List sectionList=new ArrayList()
                if (params.sectionIds){
                    if (params.sectionIds instanceof String){
                        sectionList.add(params.sectionIds)
                    }else {
                        sectionList = params.sectionIds
                    }
                }
                ClassName className = ClassName.read(params.getLong('classNameId'))
                ShiftExam shiftExamName = ShiftExam.read(params.getLong('shiftExamId'))

                GroupName groupName
                if (params.groupNameVal){
                    groupName = GroupName.valueOf(params.groupNameVal)
                }
                String examTypeVal = params.examTypeVal

                def clsExamIds = examService.classExamIdList(shiftExamName, className, groupName)
                Section newSection
                Section oldSection
                int startNum
                int endNum
                int limitNum
                def transferStdList
                sectionList.each {idx ->
                    newSection = Section.read(Long.valueOf(idx))
                    startNum = Integer.valueOf(params."start$idx")
                    endNum = Integer.valueOf(params."end$idx")
                    if (startNum !=0 && startNum < endNum) {
                        startNum = startNum - 1
                        limitNum = endNum - startNum
                        if (examTypeVal == "byCt") {
                            transferStdList = tabulationCtService.listStudentBasedClsPosition(className, clsExamIds, groupName, startNum, limitNum)
                        } else {
                            transferStdList = tabulationService.listStudentBasedClsPosition(className, clsExamIds, groupName, startNum, limitNum)
                        }

                        for (student in transferStdList) {
                            if (student.section.id != newSection.id){
                                oldSection = student.section
                                student.section=newSection
                                studentService.savedSectionTransfer(student, newSection, oldSection)
                            }
                        }
                    }
                }
                flash.message = "Student Section Transfer Done Successfully"
                render(view: '/admin/studentMgmt/sectionTransfer', model: [classNameList: classNameList, examNameList: examNameList, shiftExam:shiftExamName])
                break
        }
    }

    def batchPromotion(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL);
        String createOrUpdateBy = springSecurityService.principal?.username
        switch (request.method) {
            case 'GET':
                render(view: '/admin/studentMgmt/batchPromotion', model: [academicYearList :academicYearList, classNameList: classNameList])
                break
            case 'POST':
                List sectionList=new ArrayList()
                if (params.sectionIds){
                    if (params.sectionIds instanceof String){
                        sectionList.add(params.sectionIds)
                    }else {
                        sectionList = params.sectionIds
                    }
                }
                ClassName className = ClassName.read(params.getLong('oldClassId'))

                GroupName groupName
                if (params.groupNameVal){
                    groupName = GroupName.valueOf(params.groupNameVal)
                }
                AcademicYear oldYear
                if (params.oldYear){
                    oldYear = AcademicYear.valueOf(params.oldYear)
                }

                AcademicYear newYear
                if (params.newYear){
                    newYear = AcademicYear.valueOf(params.newYear)
                }
                String transferTypeVal = params.transferTypeVal

                Section newSection
                Section oldSection
                int startNum
                int endNum
                int limitNum
                def transferStdList
                int rollNo
                sectionList.each {idx ->
                    newSection = Section.read(Long.valueOf(idx))
                    startNum = Integer.valueOf(params."start$idx")
                    endNum = Integer.valueOf(params."end$idx")
                    if (startNum !=0 && startNum < endNum) {
                        rollNo = startNum
                        startNum = startNum - 1
                        limitNum = endNum - startNum
                        if (transferTypeVal == "byMeritPosition") {
                            transferStdList = tabulationService.listStudentByFinalExamForPromotion(oldYear, className, ExamTerm.FINAL_TEST, groupName, startNum, limitNum)
                        } else {
                            transferStdList = studentService.listStudentByRollForPromotion(className, oldYear, groupName, startNum, limitNum)
                        }
                        for (student in transferStdList) {
                            if (student.section.id != newSection.id) {
                                studentService.saveBatchPromotionByCls(newYear, student, newSection, rollNo, createOrUpdateBy)
                                rollNo++
                            }
                        }
                    }
                }
                flash.message = "Student Batch Promotion Done Successfully"
                render(view: '/admin/studentMgmt/batchPromotion', model: [academicYearList:academicYearList,classNameList: classNameList])
                break
        }
    }
    def oddEvenPromotion(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL);
        String createOrUpdateBy = springSecurityService.principal?.username
        switch (request.method) {
            case 'GET':
                render(view: '/admin/studentMgmt/oddEvenPromotion', model: [academicYearList :academicYearList, classNameList: classNameList])
                break
            case 'POST':
                List sectionList=new ArrayList()
                if (params.sectionIds){
                    if (params.sectionIds instanceof String){
                        sectionList.add(params.sectionIds)
                    }else {
                        sectionList = params.sectionIds
                    }
                }
                ClassName className = ClassName.read(params.getLong('oldClassId'))

                GroupName groupName
                if (params.groupNameVal){
                    groupName = GroupName.valueOf(params.groupNameVal)
                }
                AcademicYear oldYear
                if (params.oldYear){
                    oldYear = AcademicYear.valueOf(params.oldYear)
                }

                AcademicYear newYear
                if (params.newYear){
                    newYear = AcademicYear.valueOf(params.newYear)
                }
                String transferTypeVal = params.transferTypeVal
                List transferStdList
                if (transferTypeVal == "byMeritPosition") {
                    transferStdList = tabulationService.listStudentByFinalExamForPromotion(oldYear, className, ExamTerm.FINAL_TEST, groupName, 0, 500)
                } else {
                    transferStdList = studentService.listStudentByRollForPromotion(className, oldYear, groupName, 0, 500)
                }

                if (!transferStdList) {
                    flash.message = "Student Batch Promotion Done Successfully"
                    return
                }
                int numberOfStudent = transferStdList.size()
                Section newSection
                int startNum

                List promoteSectionList = new ArrayList()
                sectionList.each {idx ->
                    newSection = Section.read(Long.valueOf(idx))
                    startNum = Integer.valueOf(params."start$idx")
                    promoteSectionList.add([sectionName: newSection, startNum: startNum])
                }
                int numOfSection = promoteSectionList.size()
                int start = 0
                int getIndex = 0
                int oddEvenIndex = 0
                int rollNo = 1
                while (start < numberOfStudent) {
                    for (def item : promoteSectionList) {
                        getIndex = oddEvenIndex + item.startNum - 1
                        if (getIndex < numberOfStudent){
                            rollNo = oddEvenIndex + item.startNum
                            studentService.saveBatchPromotionByCls(newYear, transferStdList.get(getIndex), item.sectionName, rollNo, createOrUpdateBy)
                        }
                        start++
                    }
                    oddEvenIndex = oddEvenIndex + numOfSection
                }
                flash.message = "Student Batch Promotion Done Successfully"
                render(view: '/admin/studentMgmt/batchPromotion', model: [academicYearList:academicYearList,classNameList: classNameList])
                break
        }
    }

    def promotion(){
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/admin/studentMgmt/promotion', model: [classNameList:classNameList, academicYearList: academicYearList])
    }

    def setSection(Long className){

        LinkedHashMap result = new LinkedHashMap()
        result.put('isError',true)
        String outPut

        ClassName classNameObj=ClassName.get(className)
        List<Section> sectionList = sectionService.classSections(classNameObj)
        String fields=''
        if (sectionList){
            fields=fields+' <option value="">Select section..</option>'
            sectionList.each { Section section ->
                fields=fields+" <option value=${section.id}>${section.name}</option>"
            }
        }else{
            fields=fields+' <option value=""> No Section</option>'
        }
        result.put('isError',false)
        result.put('fields',fields)
        outPut = result as JSON
        render outPut
    }

    def loadReadmission(LoadReAdmissionCommand command){
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
            outPut = result as JSON
            render outPut
            return
        }
        def studentList = studentService.classStudentsForReAdmission(command.className,command.academicYear)
        if(!studentList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No student found for Re Admission.")
            outPut = result as JSON
            render outPut
            return
        }
        String academicYear = command.academicYear.value
        int year
        AcademicYear nextYear
        if (academicYear.length() == 4) {
            year = Integer.parseInt(academicYear)
            year = year +1
            nextYear = AcademicYear.getYearByString(year.toString())
        } else {
            year = Integer.parseInt(academicYear.substring(0, 4))
            int lowYear = year+1
            int highYear = year+2
            nextYear = AcademicYear.getYearByString(lowYear+"-"+highYear)
        }

        if(!nextYear){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        def nextClasses = classNameService.nextClasses(command.className)
        if(!nextClasses){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        ClassName nextClass = nextClasses.first()
        if(!nextClass){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        def sectionList = sectionService.classSections(nextClass, nextYear)
        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section available for admission")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('academicYear',nextYear.key)
        result.put('academicYearStr',nextYear.value)
        result.put('className',nextClass.name)
        result.put('studentList',studentList)
        result.put('sectionList',sectionList)
        outPut = result as JSON
        render outPut
    }
    def loadBulkPromotion(LoadReAdmissionCommand command){
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
            outPut = result as JSON
            render outPut
            return
        }

        String academicYear = command.academicYear.value
        int year
        AcademicYear nextYear
        if (academicYear.length() == 4) {
            year = Integer.parseInt(academicYear)
            year = year +1
            nextYear = AcademicYear.getYearByString(year.toString())
        } else {
            year = Integer.parseInt(academicYear.substring(0, 4))
            int lowYear = year+1
            int highYear = year+2
            nextYear = AcademicYear.getYearByString(lowYear+"-"+highYear)
        }

        if(!nextYear){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        def nextClasses = classNameService.nextClasses(command.className)
        if(!nextClasses){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        ClassName nextClass = nextClasses.first()
        if(!nextClass){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        def sectionList = sectionService.classSections(nextClass, nextYear)
        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section available for admission")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('academicYear',nextYear.key)
        result.put('oldSectionId',params.getLong('sectionOld'))
        result.put('academicYearStr',nextYear.value)
        result.put('className',nextClass.name)
        result.put('sectionList',sectionList)
        outPut = result as JSON
        render outPut
    }

    def loadPromotion(LoadReAdmissionCommand command){
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
            outPut = result as JSON
            render outPut
            return
        }
        def studentList = studentService.classStudentsForReAdmission(command.className,command.academicYear)
        if(!studentList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No student found for Re Admission.")
            outPut = result as JSON
            render outPut
            return
        }
        String academicYear = command.academicYear.value
        int year
        AcademicYear nextYear
        if (academicYear.length() == 4) {
            year = Integer.parseInt(academicYear)
            year = year +1
            nextYear = AcademicYear.getYearByString(year.toString())
        } else {
            year = Integer.parseInt(academicYear.substring(0, 4))
            int lowYear = year+1
            int highYear = year+2
            nextYear = AcademicYear.getYearByString(lowYear+"-"+highYear)
        }
        if(!nextYear){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet for promotion")
            outPut = result as JSON
            render outPut
            return
        }

        def sectionList = sectionService.sectionDropDownList(nextYear)
        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section available for promotion")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('academicYear',nextYear.key)
        result.put('academicYearStr',nextYear.value)
        result.put('studentList',studentList)
        result.put('sectionList',sectionList)
        outPut = result as JSON
        render outPut
    }

    def loadSectionTransfer(Long id){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        ShiftExam shiftExam = ShiftExam.read(id)
        if (!shiftExam) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select exam name to transfer section")
            outPut = result as JSON
            render outPut
            return
        }
        ClassName className
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        }
        GroupName groupName
        if (params.groupName) {
            groupName = GroupName.valueOf(params.groupName)
        }
        int numOfStudent = studentService.numberOfClassStudent(className, groupName)


        def sectionList = sectionService.groupSections(className, groupName, null)
        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section available for section transfer")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)

        result.put('numOfStudent',numOfStudent)
        result.put('nameOfCls',className.name)
        result.put('classNameId',className.id)
        result.put('shiftExamId',shiftExam.id)
        result.put('groupNameVal',groupName?.key)
        result.put('sectionList',sectionList)
        outPut = result as JSON
        render outPut
    }

    def loadBatchPromotion(Long id){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut



        ClassName oldClass
        if (params.className) {
            oldClass = ClassName.read(params.getLong('className'))
        }
        def nextClasses = classNameService.nextClasses(oldClass)
        if(!nextClasses){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        ClassName newClass = nextClasses.first()
        if(!newClass){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Class available for re admission")
            outPut = result as JSON
            render outPut
            return
        }
        GroupName groupName
        if (params.groupName) {
            groupName = GroupName.valueOf(params.groupName)
        }
        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        if(!academicYear){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select academic year")
            outPut = result as JSON
            render outPut
            return
        }


        String transferType = params.transferType
        int numOfStudent = 0
        if (transferType == "byMeritPosition") {
            List<Exam> finalExamList = examService.classExamListByTerm(oldClass, academicYear, ExamTerm.FINAL_TEST)
            if (!finalExamList || finalExamList.size() == 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "No Final Exam found")
                outPut = result as JSON
                render outPut
                return
            }
            List<String> notCompletedExams = new ArrayList<String>()
            for (Exam aFinalExam: finalExamList) {
                if (aFinalExam.examStatus == ExamStatus.NEW || aFinalExam.examStatus == ExamStatus.TABULATION ) {
                    notCompletedExams.add(aFinalExam.section.name)
                }
            }

            if (notCompletedExams.size() > 0) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Please complete Final exam result for section(s):" + notCompletedExams.join(","))
                outPut = result as JSON
                render outPut
                return
            }
            numOfStudent = tabulationService.countStudentByFinalExamForPromotion(academicYear, oldClass, ExamTerm.FINAL_TEST, groupName)
        } else {
            numOfStudent = studentService.numberOfClassStudentForPromotion(oldClass, academicYear, groupName)
        }
        if(numOfStudent == 0){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No student found for Promotion.")
            outPut = result as JSON
            render outPut
            return
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
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet for promotion")
            outPut = result as JSON
            render outPut
            return
        }

        def sectionList = sectionService.classSectionsDDList(newClass, nextYear)
        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section available for promotion")
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('numOfStudent',numOfStudent)
        result.put('nameOfCls',newClass.name)
        result.put('oldClassId',oldClass.id)
        result.put('newClassId',newClass.id)
        result.put('oldYear',academicYear.key)
        result.put('newYear',nextYear.key)
        result.put('academicYearStr',nextYear.value)
        result.put('groupNameVal',groupName?.key)
        result.put('transferTypeVal', transferType)
        result.put('sectionList',sectionList)
        outPut = result as JSON
        render outPut
    }




    //manage TC and Dropout student
    def tc() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        AcademicYear academicYear = schoolService.workingYear(null)
        LinkedHashMap resultMap = studentService.tcOrDropOutPaginateList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/admin/studentMgmt/tcOrDropout', model: [academicYearList: academicYearList, dataReturn: null, totalCount: 0, workingYear:academicYear])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/admin/studentMgmt/tcOrDropout', model: [academicYearList: academicYearList, dataReturn: resultMap.results, totalCount: totalCount, workingYear:academicYear])
    }

    def listTcOrDropOut() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = studentService.tcOrDropOutPaginateList(params)
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

    def saveTcOrDropOut(TcAndDropOutCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentService.saveTcOrDropOutStudent(command)
        String outPut = result as JSON
        render outPut
    }

    def updateTcOrDropOut(TcAndDropOutCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'tc')
            return
        }

        LinkedHashMap result = new LinkedHashMap()
        String outPut

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/admin/studentMgmt/transferCertificate')
            return
        }

        if (!command.id) {
            flash.message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/admin/studentMgmt/transferCertificate')
            return
        }

        TcAndDropOut dropOut = TcAndDropOut.get(command.id)
        if (!dropOut) {
            flash.message = CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/admin/studentMgmt/transferCertificate')
            return
        }

        studentService.updateStudentTcOrDropOut(command, dropOut)
        redirect(action: 'tc')
    }

    def transferCertificate(Long id){
        TcAndDropOut dropOut = TcAndDropOut.read(id)
        if(!dropOut || dropOut.activeStatus!=ActiveStatus.ACTIVE){
            flash.message = "No Transfer Certificate available."
            render(view: '/admin/studentMgmt/transferCertificate')
            return
        }
        render (view: '/admin/studentMgmt/transferCertificate',model: [tcObj:dropOut])
    }

    def deleteTc(Long id) {
        LinkedHashMap result = studentService.deleteStudentTc(id)
        String output = result as JSON
        render output
    }

    def saveIndividualAttendance(AttnRecordDayCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = studentService.saveStudentIndividualAttendance(params)
        String outPut = result as JSON
        render outPut
    }

   /* def roleReorderSave(){
        Employee employee

        params.sortOrder.each{ employeeData ->

            try {
                employee=Employee.get(Long.parseLong(employeeData.key))
                employee.sortOrder=Integer.parseInt(employeeData.value)
                employee.save(flush: true)
            }catch (e){
                e.print(employee.empID)
            }
        }
        redirect(action: 'hrStaffCategory')
    }*/

    def showAcademicHistory(Long id) {
        Registration registration = Registration.read(id)
        List<Student> lstStudent = Student.findAllByRegistration(registration, [sort: "id", order: "desc"])
        render(view: '/admin/studentMgmt/studentHistory', model: [lstStudent : lstStudent, registration: registration, selectedTab: "academic-history-tab"])
    }

    def showPaymentHistory(Long id){
        Registration registration = Registration.read(id)
        render(view: '/admin/studentMgmt/studentHistory', model: [registration: registration, selectedTab : "payment-history-tab"])
    }
}
