package com.grailslab

import com.grailslab.accounting.FeePayments
import com.grailslab.attn.AttnRecordDay
import com.grailslab.attn.AttnStdRecord
import com.grailslab.command.AttnRecordDayCommand
import com.grailslab.command.StudentCommand
import com.grailslab.command.StudentReAdmissionCommand
import com.grailslab.command.TcAndDropOutCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName
import com.grailslab.stmgmt.ExamMark
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import com.grailslab.stmgmt.Tabulation
import com.grailslab.stmgmt.TabulationCt
import com.grailslab.stmgmt.TcAndDropOut
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class StudentService {

    def springSecurityService
    def messageSource
    def schoolService
    def studentSubjectsService
    def grailsApplication
    def examService
    def tabulationService
    def sectionService
    def tabulationCtService

    static final String[] sortColumns = ['id', 'studentID', 'name', 'rollNo', 'reg.fathersName', 'reg.birthDate', 'reg.mobile', 'section']

    Student read(Long id) {
        return Student.read(id)
    }
    LinkedHashMap studentPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
       /* String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER*/
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        Section section
        ClassName className
        Shift shift
        Long secId
        Long classId
        Long shiftId
        if(params.sectionName){
            secId = Long.parseLong(params.sectionName)
            section = Section.read(secId)
        }
        if(params.className){
            classId = Long.parseLong(params.className)
            className = ClassName.read(classId)
        }
        if(params.shift){
            shift=Shift.valueOf(params.shift)
        }
        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        AcademicYear workingYear
        if(params.workingYear){
            workingYear = AcademicYear.valueOf(params.workingYear)
        }

        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('section', 's')
            createAlias('registration', 'reg')
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYear)
                }
                eq("studentStatus", StudentStatus.NEW)
                if(section){
                    eq("section", section)
                }
                if(className){
                    eq("className", className)
                }
                if(shift){
                    eq("s.shift", shift)
                }
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('reg.fathersName', sSearch)
                    ilike('reg.mobile', sSearch)
                    ilike('s.name', sSearch)
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
            results.each { Student student ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: student.id, registrationId: student.registration?.id, 0: serial, 1: student.studentID,2: student.name, 3: student.rollNo, 4: student.registration?.fathersName, 5:CommonUtils.getUiDateStr(student?.registration?.birthDate), 6: student.registration?.mobile, 7: "${student.className.name} - ${student.section.name}", 8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    static final String[] scholarshipColumns = ['lastUpdated', 'studentID', 'name', 'rollNo', 'section']
    def studentScholarshipList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(scholarshipColumns, iSortingCol)

        def workingYears = schoolService.workingYears()
        Section section
        ClassName className
        if(params.sectionName){
            section = Section.read(params.getLong('sectionName'))
        }
        if(params.className){
            className = ClassName.read(params.getLong('className'))
        }

        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }

        def c = Student.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                if(section){
                    eq("section", section)
                }
                if(className){
                    eq("className", className)
                }
                eq("studentStatus", StudentStatus.NEW)
                eq("scholarshipObtain", Boolean.TRUE)
            }
            if (sSearch) {
                or {
                    ilike('std.studentID', sSearch)
                    ilike('std.name', sSearch)
                    ilike('fItem.name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }

        int totalCount = results.totalCount
        List dataReturns = new ArrayList()
        int serial = 0;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { Student student ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: student.id, 0: serial, 1: student.studentID, 2:student.name, 3: student.rollNo, 4: student.className.name, 5:student.section.name, 6:student.scholarshipType?.value, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    @Transactional
    Student savedStudent(Registration registration, Section section, Integer rollNo, AcademicYear academicYear) {
        Student student = new Student(registration: registration, section: section, rollNo: rollNo, academicYear: academicYear)
        student.className = section.className
        student.name = registration.name
        student.studentID = registration.studentID
        student.createdBy = registration.createdBy
        student.save(flush: true)
    }


    @Transactional
    Student savedSectionTransfer(Student newStudent, Section newSection, Section oldSection) {
        String createOrUpdateBy = springSecurityService.principal.username
        Student savedSt = newStudent.save(flush: true)
        if(savedSt){
            //Handle Update Exam Mark
            Exam nextSectionExam
            def examMarkList
            SubjectName subject
            ExamSchedule examSchedule
            Tabulation tabulation
            TabulationCt tabulationCt
            def examList = examService.sectionExamList(oldSection)
            for (exam in examList) {
                nextSectionExam = Exam.findBySectionAndExamTermAndActiveStatus(newSection, exam.examTerm, ActiveStatus.ACTIVE)
                if (nextSectionExam) {
                    examMarkList = ExamMark.findAllByExamAndStudentAndActiveStatus(exam, newStudent, ActiveStatus.ACTIVE)
                    for (examMark in examMarkList) {
                        subject = examMark.subject
                        examSchedule = ExamSchedule.findByExamAndSubjectAndActiveStatus(nextSectionExam, subject, ActiveStatus.ACTIVE)
                        if (examSchedule) {
                            examMark.exam = nextSectionExam
                            examMark.examSchedule = examSchedule
                            examMark.updatedBy = createOrUpdateBy
                            examMark.save(flush: true)
                        }
                    }
                    tabulation = tabulationService.getTabulation(exam, newStudent)
                    if (tabulation) {
                        tabulation.exam = nextSectionExam
                        tabulation.section = newSection
                        tabulation.save(flush: true)
                    }
                    tabulationCt = tabulationCtService.getTabulation(exam, newStudent, 1)
                    if (tabulationCt) {
                        tabulationCt.exam = nextSectionExam
                        tabulationCt.section = newSection
                        tabulationCt.save(flush: true)
                    }
                    tabulationCt = tabulationCtService.getTabulation(exam, newStudent, 2)
                    if (tabulationCt) {
                        tabulationCt.exam = nextSectionExam
                        tabulationCt.section = newSection
                        tabulationCt.save(flush: true)
                    }
                    tabulationCt = tabulationCtService.getTabulation(exam, newStudent, 3)
                    if (tabulationCt) {
                        tabulationCt.exam = nextSectionExam
                        tabulationCt.section = newSection
                        tabulationCt.save(flush: true)
                    }
                }
            }
        }
        return savedSt
    }

    @Transactional
    boolean saveBatchPromotionByCls(AcademicYear academicYear, Student oldStudent, Section newSection, int rollNo, String createOrUpdateBy) {
        if (oldStudent.promotionStatus != PromotionStatus.PROMOTED) {
            new Student(
                    name: oldStudent.name,
                    studentID: oldStudent.studentID,
                    registration: oldStudent.registration,
                    section: newSection,
                    className: newSection.className,
                    rollNo: rollNo,
                    academicYear: academicYear,
                    admissionMonth: YearMonths.JANUARY,
                    createdBy: createOrUpdateBy
            ).save(flush: true)
            oldStudent.promotionStatus = PromotionStatus.PROMOTED
            oldStudent.save(flush: true)
        }
        return true
    }


    def allStudentInList(List idList){
        if (!idList) {
            return null
        }
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                'in'("id",idList)
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }
    def allStudent(Section section){
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }
    List<Student> studentListForBulkPromotion(Section section){
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("promotionStatus", PromotionStatus.NEW)
                eq("section", section)
            }
        }
        return results
    }
    def allStudentForTabulation(Section section){
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
            }
        }
        return results
    }
    def previousClasses(String stdID, Student currStd){
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias("className","cName")
            and {
                eq("studentID", stdID)
                'ne'("studentStatus", StudentStatus.DELETED)
                'ne'("id", currStd.id)
            }
            order("cName.sortPosition", CommonUtils.SORT_ORDER_DESC)
        }

        return results
    }
    def numberOfStudent(Section section, AcademicYear academicYear =null){
        if(!academicYear){
            academicYear = schoolService.workingYear(section?.className)
        }
        return Student.countByAcademicYearAndStudentStatusAndSection(academicYear, StudentStatus.NEW, section)
    }
    def numberOfTotalStudent(AcademicYear academicYear =null){
        if(!academicYear){
            academicYear = schoolService.schoolWorkingYear()
        }
        return Student.countByAcademicYearAndStudentStatus(academicYear, StudentStatus.NEW)
    }
    def numberOfTcStudent(AcademicYear academicYear =null){
        if(!academicYear){
            academicYear = schoolService.schoolWorkingYear()
        }
        return Student.countByAcademicYearAndStudentStatus(academicYear, StudentStatus.TC)
    }

    int numberOfClassStudent(ClassName className, GroupName groupName = null, AcademicYear academicYear =null){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        if (groupName) {
            def groupSectionList = sectionService.groupSections(className, groupName, academicYear)
            return Student.countByAcademicYearAndStudentStatusAndActiveStatusAndClassNameAndSectionInList(academicYear, StudentStatus.NEW, ActiveStatus.ACTIVE, className, groupSectionList)
        } else {
            return Student.countByAcademicYearAndStudentStatusAndActiveStatusAndClassName(academicYear, StudentStatus.NEW, ActiveStatus.ACTIVE, className)
        }
    }

    List listStudentByRollForPromotion(ClassName className, AcademicYear academicYear, GroupName groupName, int offset, int limit){
        def groupSectionList
        if (groupName){
            groupSectionList = sectionService.groupSections(className, groupName, academicYear)
        }

        def c = Student.createCriteria()
        List results = c.list(max: limit, offset: offset) {
            and {
                eq("className", className)
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                if (groupName) {
                    'in'("section", groupSectionList)
                }
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    //Number of class student for promotion. i.e: promotion status = NEW
    int numberOfClassStudentForPromotion(ClassName className, AcademicYear academicYear, GroupName groupName){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        if (groupName) {
            def groupSectionList = sectionService.groupSections(className, groupName, academicYear)
            return Student.countByAcademicYearAndStudentStatusAndActiveStatusAndPromotionStatusAndClassNameAndSectionInList(academicYear, StudentStatus.NEW, ActiveStatus.ACTIVE, PromotionStatus.NEW, className, groupSectionList)
        } else {
            return Student.countByAcademicYearAndStudentStatusAndActiveStatusAndPromotionStatusAndClassName(academicYear, StudentStatus.NEW, ActiveStatus.ACTIVE, PromotionStatus.NEW, className)
        }
    }
    def listStudent(AcademicYear academicYear, ClassName className, Section section, Gender gender, Religion religion){
        def c = Student.createCriteria()
        def results = c.list(max: 300, offset: 0) {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                eq("className", className)
                if (section) {
                    eq("section", section)
                }
                if (gender) {
                    eq("reg.gender", gender)
                }
                if (religion) {
                    eq("reg.religion", religion)
                }
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }


    def studentIdsByGender(AcademicYear academicYear, List sectionList, Religion religion, Gender paramGender, Gender analysisGender){
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                if (sectionList) {
                    'in'("section", sectionList)
                }
                if (religion) {
                    eq("reg.religion", religion)
                }
                if (paramGender) {
                    eq("reg.gender", paramGender)
                } else {
                    eq("reg.gender", analysisGender)
                }
            }

        }
        return results
    }

    def studentIdsByReligion(AcademicYear academicYear, List sectionList, Gender gender, Religion paramReligion, Religion analysisReligion){
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                if (sectionList) {
                    'in'("section", sectionList)
                }
                if (gender) {
                    eq("reg.gender", gender)
                }
                if (paramReligion) {
                    eq("reg.religion", paramReligion)
                } else {
                    eq("reg.religion", analysisReligion)
                }
            }
        }
        return results
    }

    def listStudentForAnalysis(AcademicYear academicYear, List sectionList, Gender gender, Religion religion){
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                if (sectionList) {
                    'in'("section", sectionList)
                }
                if (gender) {
                    eq("reg.gender", gender)
                }
                if (religion) {
                    eq("reg.religion", religion)
                }

            }
        }
        return results
    }

    def studentListBySection(AcademicYear academicYear, Section section, Gender gender, Religion religion){
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
                if (gender) {
                    eq("reg.gender", gender)
                }
                if (religion) {
                    eq("reg.religion", religion)
                }
            }

        }
        return results
    }

    int numberOfGirlsStudent(ClassName className, AcademicYear academicYear =null){
        if(!academicYear){
            academicYear = schoolService.workingYear(className)
        }
        def c = Student.createCriteria()
        def count = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                eq("studentStatus", StudentStatus.NEW)
                eq("reg.gender", Gender.FEMALE)
                eq("reg.activeStatus", ActiveStatus.ACTIVE)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int numberOfStudentByGender(AcademicYear academicYear, ClassName className, Section section, Gender gender){
        def c = Student.createCriteria()
        def count = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                if (className) {
                    eq("className", className)
                }
                if (section) {
                    eq("section", section)
                }
                eq("studentStatus", StudentStatus.NEW)
                eq("reg.gender", gender)
                eq("reg.activeStatus", ActiveStatus.ACTIVE)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int numberOfStudentByReligion(AcademicYear academicYear, ClassName className, Section section, Religion religion){
        def c = Student.createCriteria()
        def count = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                if (className) {
                    eq("className", className)
                }
                if (section) {
                    eq("section", section)
                }
                eq("studentStatus", StudentStatus.NEW)
                eq("reg.religion", religion)
                eq("reg.activeStatus", ActiveStatus.ACTIVE)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int numberOfStudentByAdmission(AcademicYear academicYear, ClassName className, Section section, String admissionType){
        def c = Student.createCriteria()
        def count = c.list() {
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                if (className) {
                    eq("className", className)
                }
                if (admissionType == "admission") {
                    eq("reg.academicYear", academicYear)
                } else {
                    //promotion
                    ne("reg.academicYear", academicYear)
                }
                if (section) {
                    eq("section", section)
                }
                eq("studentStatus", StudentStatus.NEW)
                eq("reg.activeStatus", ActiveStatus.ACTIVE)
            }
            projections {
                count()
            }
        }
        return count[0]
    }

    def sectionStudentDDList(Section section){
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }


    def allStudentTypeAheadWithRollSectionDD(String sSearch, AcademicYear academicYear = null){
        List dataReturns = new ArrayList()
        def workingYears = schoolService.workingYears()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            createAlias('section', 's')
            createAlias('registration', 'reg')
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                eq("studentStatus", StudentStatus.NEW)
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('reg.mobile', sSearch)
                }
            }
            order("cls.sortPosition",CommonUtils.SORT_ORDER_ASC)
            order("s.name",CommonUtils.SORT_ORDER_ASC)
            order("rollNo",CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [${student.rollNo}] - ${student.section.name} - ${student.className.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }

    //So all student by year
    def allStudentTypeAheadListByStdId(String sSearch, AcademicYear academicYear = null){
        List dataReturns = new ArrayList()
        def workingYears = schoolService.workingYears()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            createAlias('section', 's')
            createAlias('registration', 'reg')
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                eq("studentStatus", StudentStatus.NEW)
            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                    ilike('reg.mobile', sSearch)
                }
            }
            order("cls.sortPosition",CommonUtils.SORT_ORDER_ASC)
            order("s.name",CommonUtils.SORT_ORDER_ASC)
            order("rollNo",CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.studentID, name: "${student.studentID} - ${student.name} - [${student.rollNo}] - ${student.section.name} - ${student.className.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }

    def sectionStudentIdsList(Section section){
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
            }
            projections {
                property('id')
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def allStudentNotInList(Section section, List idList){
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)

                if(idList){
                    not {'in'("id",idList)}
                }
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }

    //re admission logic
    def classStudentsForReAdmission(ClassName className, AcademicYear academicYear){
        List dataReturns = new ArrayList()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                eq("studentStatus", StudentStatus.NEW)
                eq("promotionStatus", PromotionStatus.NEW)
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }

    def classStudentsList(ClassName className, AcademicYear academicYear = null){
        List dataReturns = new ArrayList()
        if (!academicYear) {
            academicYear = schoolService.workingYear(className)
        }

        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                eq("studentStatus", StudentStatus.NEW)
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [Section: ${student.section?.name}, Roll: ${student.rollNo}]"])
        }
        return dataReturns
    }

    def classSectionStudentList(ClassName className, Section section, AcademicYear academicYear = null){
        List dataReturns = new ArrayList()
        if (!academicYear) {
            if (section) {
                academicYear = section.academicYear
            } else {
                academicYear = schoolService.workingYear(className)
            }
        }

        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('section', 's')
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                if (section){
                    eq("section", section)
                }
                eq("studentStatus", StudentStatus.NEW)
            }
            order("s.name",CommonUtils.SORT_ORDER_ASC)
            order("rollNo",CommonUtils.SORT_ORDER_ASC)
        }

        return results
    }
    def classStudentList(ClassName className, AcademicYear academicYear = null){
        List dataReturns = new ArrayList()
        if (!academicYear) {
            academicYear = schoolService.workingYear(className)
        }
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                eq("studentStatus", StudentStatus.NEW)
            }
        }

        return results
    }

    def byClassName(ClassName className, AcademicYear academicYear, GroupName groupName = null){
        def c = Student.createCriteria()
        def results = c.list() {
            if (groupName){
                createAlias('section', 'se')
            }
            and {
                eq("academicYear", academicYear)
                eq("className", className)
                if (groupName){
                    eq("se.groupName", groupName)
                }
                eq("studentStatus", StudentStatus.NEW)
            }
            order("section", CommonUtils.SORT_ORDER_ASC)
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
//    @todo-aminul remove this
    def allStudentDDListTypeAheadForMessaging(GrailsParameterMap parameterMap){
        String sSearch = parameterMap.q
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def workingYears = schoolService.workingYears()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                'in'("academicYear", workingYears)
                eq("studentStatus", StudentStatus.NEW)
            }
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - ${student.section.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }
    def allStudentDDListForMessaging(){
        List dataReturns = new ArrayList()
        def workingYears = schoolService.workingYears()
        def c = Student.createCriteria()
        def results = c.list() {
            and {
                'in'("academicYear", workingYears)
                eq("studentStatus", StudentStatus.NEW)
            }
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - ${student.section.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }
    def step2StudentListForMessage(List<Long> selectionIds, SelectionTypes selectionType=null){
        String sSearch = "01" + CommonUtils.PERCENTAGE_SIGN
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('section', 'se')
            createAlias('registration', 'reg')
            and {
                eq("studentStatus", StudentStatus.NEW)
                ilike('reg.mobile', sSearch)
                if (selectionType == SelectionTypes.BY_STUDENT) {
                    'in'("id", selectionIds)
                }else {
                    'in'("se.id", selectionIds)
                }
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        Registration registration
        String mobileNo
        results.each { Student student ->
            registration = student.registration
            mobileNo = registration.mobile
            if(mobileNo && mobileNo.size()==11){
                dataReturns.add([id: student.id, name: student.studentID + ' - ' + student.name, mobile: registration.mobile])
            }
        }
        return dataReturns
    }


    //manage TC and DROP OUT cases
    static final String[] tcOrDropDownSortColumns = ['id', 'std.studentID', 'std.name']
    LinkedHashMap tcOrDropOutPaginateList(GrailsParameterMap params) {
        def workingYears = schoolService.workingYears()
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(tcOrDropDownSortColumns, iSortingCol)
        List dataReturns = new ArrayList()

        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        TcType tcType = null
        if(params.tcType){
            tcType = TcType.valueOf(params.tcType)
        }
        def c = TcAndDropOut.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias("student", "std")
            and {
                if(academicYear){
                    eq("std.academicYear", academicYear)
                } else {
                    'in'("std.academicYear", workingYears)
                }
                eq("std.studentStatus", StudentStatus.TC)
                if(tcType){
                    eq("tcType", tcType)
                }
                eq("activeStatus", ActiveStatus.ACTIVE)
            }
            if (sSearch) {
                or {
                    ilike('std.studentID', sSearch)
                    ilike('std.name', sSearch)
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
            Student student
            results.each { TcAndDropOut dropOut ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                student = dropOut.student
                dataReturns.add([DT_RowId: dropOut.id, 0: serial, 1: student.studentID, 2: student.name, 3: student.section.name, 4:dropOut.tcType?.value, 5: CommonUtils.getUiDateStr(dropOut.releaseDate), 6: dropOut.reason, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    //Fee Item not paid student List
    static final String[] feeItemNotPaidSortColumns = ['section','studentID', 'name', 'rollNo']
    LinkedHashMap feeItemNotPaidPaginateList(GrailsParameterMap params, List<Long> paidStdIds) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(feeItemNotPaidSortColumns, iSortingCol)
        List dataReturns = new ArrayList()

        def workingYears = schoolService.workingYears()
        AcademicYear academicYear
        if(params.academicYear){
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        ClassName className = null
        if(params.className){
            className = ClassName.read(params.className.toLong())
        }
        def c = Student.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                if(academicYear){
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                eq("studentStatus", StudentStatus.NEW)
                if(className){
                    eq("className", className)
                }
                if(paidStdIds){
                    not {'in'("id",paidStdIds)}
                }

            }
            if (sSearch) {
                or {
                    ilike('studentID', sSearch)
                    ilike('name', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        if (totalCount > 0) {
            results.each { Student student ->
                dataReturns.add([DT_RowId: student.id, 0: student.section.name, 1: student.studentID, 2: student.name, 3: student.rollNo, 4:'', 5: '', 6: '', 7: '', 8:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }
    Integer nextRoll(Section section, AcademicYear academicYear = null){
        if(!academicYear){
            academicYear = section.academicYear
        }
        int rollNumber =0
        def listStudent

        String schoolName = schoolService.runningSchool()
        if(schoolName=='nideal'){
            listStudent = Student.findAllByStudentStatusAndAcademicYearAndClassName(StudentStatus.NEW,academicYear,section.className,[sort: "rollNo",order:'desc',max: 1, offset: 0])
        } else {
            listStudent = Student.findAllByStudentStatusAndAcademicYearAndSection(StudentStatus.NEW,academicYear,section,[sort: "rollNo",order:'desc',max: 1, offset: 0])
        }
        if(listStudent){
            rollNumber = listStudent.last().rollNo
        }
        return rollNumber+1
    }

    def allAbsenceStudent(String sSearch,Section section, def idList){
        List dataReturns = new ArrayList()
        AcademicYear academicYear = schoolService.workingYear(section?.className)
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            createAlias('section', 's')
            createAlias('registration', 'reg')
            and {
                eq("academicYear", academicYear)
                eq("studentStatus", StudentStatus.NEW)
                eq("section", section)
                if(idList){
                    not {'in'("id",idList)}
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('reg.mobile', sSearch)
                    ilike('cls.name', sSearch)

                }
            }

            order("cls.sortPosition",CommonUtils.SORT_ORDER_ASC)
            order("s.name",CommonUtils.SORT_ORDER_ASC)
            order("rollNo",CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [${student.rollNo}] - ${student.section.name} - ${student.className.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }

    def allAbsenceStudent(String sSearch, def idList){
        List dataReturns = new ArrayList()
       if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = Student.createCriteria()
        def results = c.list() {
            createAlias('className', 'cls')
            createAlias('section', 's')
            createAlias('registration', 'reg')
            and {
                eq("studentStatus", StudentStatus.NEW)
                if(idList){
                    not {'in'("id",idList)}
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('reg.mobile', sSearch)
                    ilike('cls.name', sSearch)
                    ilike('studentID', sSearch)

                }
            }

            order("cls.sortPosition",CommonUtils.SORT_ORDER_ASC)
            order("s.name",CommonUtils.SORT_ORDER_ASC)
            order("rollNo",CommonUtils.SORT_ORDER_ASC)
        }
        results.each { Student student ->
            dataReturns.add([id: student.id, name: "${student.studentID} - ${student.name} - [${student.rollNo}] - ${student.section.name} - ${student.className.name} - ${student.registration.mobile}"])
        }
        return dataReturns
    }
    Student findByStudentId(String studentId, AcademicYear academicYear){
        def workingYears
        if (!academicYear) {
            workingYears = schoolService.workingYears()
        }
        if (academicYear) {
            return Student.findByAcademicYearAndStudentStatusAndStudentID(academicYear, StudentStatus.NEW, studentId)
        }
        return Student.findByStudentStatusAndAcademicYearInListAndStudentID(StudentStatus.NEW, workingYears, studentId)
    }

    def saveOrUpdateAdmission(StudentCommand studentCommand) {

        LinkedHashMap result = new LinkedHashMap()
        if (studentCommand.hasErrors()) {
            def errorList = studentCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        int stdList = 0
        if (!studentCommand.id) {
            stdList = Student.countByAcademicYearAndSectionAndRollNoAndStudentStatus(studentCommand.academicYear, studentCommand.section, studentCommand.rollNo, StudentStatus.NEW)
        }

        if (stdList > 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Roll Number Already Exist, Pleas Try Another')
            return result
        }

        Student student
        Section section = studentCommand.section
        Registration registration
        ClassName className = section.className
        String createOrUpdateBy = springSecurityService.principal.username
        if (studentCommand.id) {
            student = Student.get(studentCommand.id)
            if (!student) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            registration = student.registration
            student.academicYear = studentCommand.academicYear
            student.section = studentCommand.section
            student.rollNo = studentCommand.rollNo
            student.className = className
            student.admissionMonth = studentCommand.admissionMonth
            student.updatedBy = createOrUpdateBy
            student.save(flush: true)
            registration.studentStatus = StudentStatus.ADMISSION
            registration.updatedBy = createOrUpdateBy
            registration.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Student Updated successfully')
            return result
        }

        registration = studentCommand.registration
        registration.updatedBy = createOrUpdateBy
        savedStudent(registration, studentCommand.section, studentCommand.rollNo, studentCommand.academicYear)
        registration.studentStatus = StudentStatus.ADMISSION
        registration.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Student Create successfully')
        result.put('rollNumber', studentCommand.rollNo + 1)
        return result
    }

    def saveOrUpdateStudent(StudentCommand studentCommand) {
        LinkedHashMap result = new LinkedHashMap()
        if (studentCommand.hasErrors()) {
            def errorList = studentCommand?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Section section = studentCommand.section
        Student student
        String createOrUpdateBy = springSecurityService.principal.username
        if (studentCommand.id) {
            student = Student.get(studentCommand.id)
            if (!student) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            Section oldSection = student.section
            student.section = section
            student.rollNo = studentCommand.rollNo
            student.admissionMonth = studentCommand.admissionMonth
            student.updatedBy = createOrUpdateBy
            savedSectionTransfer(student, section, oldSection)

            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Student Updated successfully')
            return result
        }

        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
        result.put(CommonUtils.MESSAGE, 'Student not load correctly. Please contact admin')
        return result
    }

    def saveOrUpdateReAdmission(StudentReAdmissionCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username

        Student oldStudent = command.student
        if (oldStudent.promotionStatus == PromotionStatus.PROMOTED) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${oldStudent.name} Already Re Admit")
            return result
        }

        def stdList = Student.countByStudentStatusAndAcademicYearAndSectionAndRollNo(StudentStatus.NEW, command.academicYear, command.section, command.rollNo)
        if (stdList > 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Roll No Already Exist. Pleas Try Another')
            return result
        }

        Section section = command.section
        Registration registration = command.student.registration
        ClassName className = section.className

        Student newStudent = new Student(
                name: registration.name,
                studentID: registration.studentID,
                registration: registration,
                section: section,
                className: className,
                rollNo: command.rollNo,
                academicYear: command.academicYear,
                createdBy: createOrUpdateBy,
                admissionMonth: command.admissionMonth
        )
        if (!newStudent.validate()) {
            def errorList = newStudent?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Student savedStudent = newStudent.save(flush: true)
        if (!savedStudent) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Error student re admission")
            return result
        }

        oldStudent.promotionStatus = PromotionStatus.PROMOTED
        oldStudent.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "${savedStudent.name} promoted successfully.")
        result.put('rollNumber', savedStudent.rollNo + 1)
        return result
    }

    def deleteStudent(Long id){

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        Student student = Student.get(id)
        if (!student || student.studentStatus != StudentStatus.NEW) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        int studentYear = Integer.valueOf(student.academicYear.value)
        Calendar now = Calendar.getInstance();
        if (studentYear < now.get(Calendar.YEAR)) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Can't delete old student")
            return result
        }

        if (student.promotionStatus == PromotionStatus.PROMOTED) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Student already promoted.")
            return result
        }
        // if student paid any fee for current academic year
        def studentPayments = FeePayments.findByStudentAndAcademicYearAndActiveStatus(student, student.academicYear, ActiveStatus.ACTIVE)
        if(studentPayments){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Student already Paid fee. Please contact admin to delete this student.")
            return result
        }

        List<Student> previousClasses = previousClasses(student.studentID, student)
        Registration registration = student.registration
        Student lastStd
        student.studentStatus = StudentStatus.DELETED
        student.updatedBy = createOrUpdateBy
        student.save(flush: true)

        if (previousClasses) {
            lastStd = previousClasses.first()
            if (lastStd) {
                lastStd.promotionStatus = PromotionStatus.NEW
                lastStd.save(flush: true)
            }
        } else {
            registration.studentStatus = StudentStatus.NEW
            registration.updatedBy = createOrUpdateBy
            registration.save(flush: true)
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE,"Student deleted successfully.")
        return result
    }

    def deleteStudentTc(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        TcAndDropOut dropOut = TcAndDropOut.get(id)
        if (!dropOut) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        Student student = dropOut.student
        student.studentStatus = StudentStatus.NEW
        dropOut.activeStatus = ActiveStatus.DELETE
        student.updatedBy = createOrUpdateBy
        dropOut.updatedBy = createOrUpdateBy
        student.save(flush: true)
        dropOut.save(flush: true)

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "TC deleted Successfully")
        return result
    }

    def saveTcOrDropOutStudent(TcAndDropOutCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService?.principal.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        TcAndDropOut dropOut
        String message
        dropOut = new TcAndDropOut(command.properties)
        message = "TC issued successfully."
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        if (dropOut.hasErrors()) {
            def errorList = dropOut?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            message = errorList?.join('\n')
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        Date releaseDate = new Date()
        if (command.releaseDate) {
            releaseDate = command.releaseDate
        }
        Student student = command.student
        student.studentStatus = StudentStatus.TC
        student.updatedBy = createOrUpdateBy
        student.save(flush: true)
        if (!command.releaseText) {
            dropOut.releaseText = certificateText(dropOut, student)
        }
        dropOut.releaseDate = releaseDate
        dropOut.createdBy = createOrUpdateBy
        dropOut.academicYear = academicYear
        dropOut.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    private String certificateText(TcAndDropOut dropOut, Student student){
        Registration registration = student.registration
        String schoolName = grailsApplication.config.getProperty('app.school.name')
        String textStr ='<p>Certification is hereby issued to <strong><em><u>'+student.name;
        textStr+='</u></em></strong>, Son of <span style="text-decoration: underline;"><em><strong>'+registration.fathersName
        textStr+='</strong></em></span> and <span style="text-decoration: underline;"><em><strong>'+registration.mothersName
        textStr+='</strong></em></span>, <span style="text-decoration: underline;"><em><strong>'+registration.presentAddress
        textStr+='</strong></em></span> that he was a student of class <span style="text-decoration: underline;"><em><strong>'+student.className.name
        textStr+='</strong></em></span>, ID# <span style="text-decoration: underline;"><em><strong>'+student.studentID
        textStr+='</strong></em></span> in '+schoolName +' till '+CommonUtils.getUiDateStr(dropOut?.releaseDate)
        textStr+='. He was born in <span style="text-decoration: underline;"><em><strong>'+CommonUtils.getUiDateStr(registration?.birthDate)+'</strong></em></span>.</p>'
        textStr+='<p>&nbsp;</p><p>What is more, he is a student of good etiquette &amp; behavior.</p><p>&nbsp;</p><p>May all success attend his.</p>'
        return textStr;
    }

    def updateStudentTcOrDropOut(TcAndDropOutCommand command, TcAndDropOut dropOut) {
        String createOrUpdateBy = springSecurityService.principal.username

        dropOut.properties['tcType', 'reason', 'schoolName', 'releaseDate', 'releaseText'] = command.properties
        Date releaseDate = new Date()
        if (command.releaseDate) {
            releaseDate = command.releaseDate
        }
        Student student = dropOut.student
        if (!command.releaseText) {
            dropOut.releaseText = certificateText(dropOut, student)
        }
        dropOut.releaseDate = releaseDate
        dropOut.updatedBy = createOrUpdateBy
        dropOut.academicYear = springSecurityService.principal.academicYear
        dropOut.save(flush: true)
        /*String tcSubmitButton = params.tcSubmitButton
        if( true *//*tcSubmitButton && tcSubmitButton=="updatePrintBtn"*//*){
            redirect(controller: 'studentReport', action: 'printTransferCertificate', params: [tcId:dropOut?.id])
            return
        }*/
    }

    def saveStudentIndividualAttendance(GrailsParameterMap params){

        AttnStdRecord attnStdRecord
        AttnRecordDay attnRecordDay
        LinkedHashMap result = new LinkedHashMap()
        try {
            Date attenDate = params.getDate("releaseDate", "dd/MM/yyyy")
            Student student = Student.findById(params.getInt("student"))
            if (student) {
                attnStdRecord = AttnStdRecord.findByStdNo(student.id)
            } else {
                attnStdRecord = AttnStdRecord.findByStdNo(params.id)
            }

            if (!attnStdRecord) {
                Section section = student.section
                Date InTimevalue = params.getDate("inTime", "hh:mm a")
                String classInTime = InTimevalue.format("HHmmss")
                Date inTime = CommonUtils.deviceLogDateStrToDate(attenDate.format("yyyyMMdd"), classInTime)

                attnRecordDay = AttnRecordDay.findByRecordDate(attenDate)
                AttnStdRecord attnStdRecordInfo = new AttnStdRecord()
                attnStdRecordInfo.stdNo = student.rollNo
                attnStdRecordInfo.studentId = student.id
                attnStdRecordInfo.sectionId = section.id
                attnStdRecordInfo.inTime = inTime
                attnStdRecordInfo.outTime = null;
                attnStdRecordInfo.remarks = params.reason
                attnStdRecordInfo.recordDay = attnRecordDay
                if (!attnStdRecordInfo.save(flush: true)) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "save fail")
                } else {
                    result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
                    result.put(CommonUtils.MESSAGE, "save successfully")
                }
            } else {
                Date InTimevalue = params.getDate("inTime", "hh:mm a")
                String classInTime = InTimevalue.format("HHmmss")
                Date inTime = CommonUtils.deviceLogDateStrToDate(attenDate.format("yyyyMMdd"), classInTime)
                //attnRecordDay = AttnRecordDay.findByRecordDate(attenDate)
                attnStdRecord.inTime = inTime
                attnStdRecord.remarks = params.reason

                // attnStdRecordInfo.recordDay = attnRecordDay
                if (!attnStdRecord.save(flush: true)) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "edit fail")
                } else {
                    result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
                    result.put(CommonUtils.MESSAGE, "edit successfully")
                }
            }

        } catch (Exception e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "save fail")
        }
        return result
    }
}
