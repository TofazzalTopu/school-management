package com.grailslab

import com.grailslab.command.AddTransferSubjectCommand
import com.grailslab.command.StudentSubjectCommand
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.enums.StudentStatus
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassSubjects
import com.grailslab.settings.Section
import com.grailslab.settings.StudentSubjects
import com.grailslab.settings.SubjectName
import com.grailslab.stmgmt.Student
import com.grailslab.viewz.StdStudentSubjectView
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class StudentSubjectsService {
    def springSecurityService
    def schoolService
    def messageSource
    def classSubjectsService
    def sessionFactory
    def studentService

    static final String SELECT_HQL_WITH_REGISTRATION = "FROM student std inner join registration regis on std.registration_id = regis.id inner join section sec on sec.id = std.section_id inner join class_name cls on sec.class_name_id = cls.id where sec.academic_year= :acaYearKey and sec.active_status= 'ACTIVE' and std.student_status= 'NEW' and cls.id = :classNameId"
    static final String SELECT_HQL = "FROM student std inner join section sec on sec.id = std.section_id inner join class_name cls on sec.class_name_id = cls.id where sec.academic_year= 'acaYear' and sec.active_status= 'ACTIVE' and std.student_status= 'NEW' and sec.id= :sectionId"
    def studentSubjectsList(Section section){
        final session = sessionFactory.currentSession

        def selectStr = new StringBuilder('std.id, cls.id class_name_id, cls.name class_name, sec.id section_id, sec.name section_name, sec.group_name, std.name student_name, std.roll_no, std.studentid,')
        selectStr.append(" (select CASE WHEN cls.allow_optional_subject = false THEN '' ELSE GROUP_CONCAT(subject_name order by sort_position ASC SEPARATOR '; ') END FROM v_std_student_subject WHERE std_id = std.id and subject_type in ('OPTIONAL', 'INUSE') and is_optional = true and (group_name is null or group_name = sec.group_name)) opt_subjects,")
        selectStr.append(" (select GROUP_CONCAT(subject_name order by sort_position ASC SEPARATOR '; ') FROM v_std_student_subject WHERE std_id = std.id and is_optional = false and subject_type in ('OPTIONAL', 'INUSE') and (group_name is null or group_name = sec.group_name)) comp_subjects")

        final String query = "select $selectStr ${SELECT_HQL.replace("acaYear", schoolService.workingYear(section.className).key)}"
        final sqlQuery = session.createSQLQuery(query)

        final queryResults = sqlQuery.with {
            setLong('sectionId', section.id)
            list()
        }
        final results = queryResults.collect { resultRow ->
            [id: resultRow[0], classNameId: resultRow[1], className: resultRow[2], sectionId: resultRow[3], sectionName: resultRow[4], groupName: resultRow[5], studentName: resultRow[6], rollNo: resultRow[7], studentid: resultRow[8], optSubjects: resultRow[9], compSubjects: resultRow[10]]
        }
        results
    }

    def studentSubjectsListCommonMapping(AcademicYear academicYear, ClassName className, Section section, Gender gender, Religion religion){
        final session = sessionFactory.currentSession

        def selectStr = new StringBuilder('std.id, cls.id class_name_id, cls.name class_name, sec.id section_id, sec.name section_name, sec.group_name, std.name student_name, std.roll_no, std.studentid,')
        selectStr.append(" (select CASE WHEN cls.allow_optional_subject = false THEN '' ELSE GROUP_CONCAT(subject_name order by sort_position ASC SEPARATOR '; ') END FROM v_std_student_subject WHERE std_id = std.id and subject_type in ('OPTIONAL', 'INUSE') and is_optional = true and (group_name is null or group_name = sec.group_name)) opt_subjects,")
        selectStr.append(" (select GROUP_CONCAT(subject_name order by sort_position ASC SEPARATOR '; ') FROM v_std_student_subject WHERE std_id = std.id and is_optional = false and subject_type in ('OPTIONAL', 'INUSE') and (group_name is null or group_name = sec.group_name)) comp_subjects")

        String query = "select $selectStr $SELECT_HQL_WITH_REGISTRATION"
        if (section) {
            query += " and sec.id= :sectionId"
        }
        if (gender) {
            query += " and regis.gender= :gender"
        }
        if (religion) {
            query += " and regis.religion= :religion"
        }
        final sqlQuery = session.createSQLQuery(query)

        final queryResults = sqlQuery.with {
            setString('acaYearKey', academicYear.key)
            setLong('classNameId', className.id)
            if (section) {
                setLong('sectionId', section.id)
            }
            if (section) {
                setLong('sectionId', section.id)
            }
            if (gender) {
                setString('gender', gender.key)
            }
            if (religion) {
                setString('religion', religion.key)
            }
            list()
        }
        String selectedSubjects
        int serial = 0
        final results = queryResults.collect { resultRow ->
            serial++
            selectedSubjects = ""
            if (resultRow[10]){
                selectedSubjects = resultRow[10]+" [Com]"
            }
            if (resultRow[9]){
                selectedSubjects += ", "+resultRow[9]+" [Opt]"
            }
            [serial:serial, id: resultRow[0], classNameId: resultRow[1], className: resultRow[2], sectionId: resultRow[3], sectionName: resultRow[4], groupName: resultRow[5], studentName: resultRow[6], rollNo: resultRow[7], studentid: resultRow[8], selectedSubjects:selectedSubjects]
        }
        results
    }

    def studentSubjectsListForEdit(Section section, Long studentId = null){

        final session = sessionFactory.currentSession

        def selectStr = new StringBuilder('std.id, cls.id class_name_id, cls.name class_name, sec.id section_id, sec.name section_name, sec.group_name, std.name student_name, std.roll_no, std.studentid,')
        selectStr.append(" (select CASE WHEN cls.allow_optional_subject = false THEN '' ELSE GROUP_CONCAT(subject_id order by sort_position ASC SEPARATOR ',') END FROM v_std_student_subject WHERE std_id = std.id and subject_type in ('OPTIONAL', 'INUSE') and is_optional = true and (group_name is null or group_name = sec.group_name)) opt_subject_ids,")
        selectStr.append(" (select GROUP_CONCAT(subject_id order by sort_position ASC SEPARATOR ',') FROM v_std_student_subject WHERE std_id = std.id and is_optional = false and subject_type in ('OPTIONAL', 'INUSE') and (group_name is null or group_name = sec.group_name)) comp_subject_ids")

        String query = "select $selectStr ${SELECT_HQL.replace("acaYear", schoolService.workingYear(section.className).key)}"
        if (studentId) {
            query += " and std.id =:studentId"
        }
        final sqlQuery = session.createSQLQuery(query)

        final queryResults = sqlQuery.with {
            setLong('sectionId', section.id)
            if (studentId) {
                setLong('studentId', studentId)
            }
            list()
        }
        List<Long> optSubjectIds
        List<Long> compSubjectIds
        String vauleStr
        final results = queryResults.collect { resultRow ->
            vauleStr = resultRow[9] ?: null
            optSubjectIds = new ArrayList<Long>()
            for (String s : vauleStr?.split(",") ) {
                optSubjectIds.add(Long.valueOf(s));
            }
            vauleStr = resultRow[10] ?: null
            compSubjectIds = new ArrayList<Long>()
            for (String s :vauleStr?.split(",") ) {
                compSubjectIds.add(Long.valueOf(s));
            }
            [id: resultRow[0], classNameId: resultRow[1], className: resultRow[2], sectionId: resultRow[3], sectionName: resultRow[4], groupName: resultRow[5], studentName: resultRow[6], rollNo: resultRow[7], studentid: resultRow[8], optSubjectIds: optSubjectIds, compSubjectIds: compSubjectIds]
        }
        results
    }

    def subjectStudentsExcludingList(Section section, SubjectName subject, List stdIds){
        def c = StudentSubjects.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("subject", subject )
                eq("std.section", section)
                eq("std.studentStatus", StudentStatus.NEW)
                if(stdIds){
                    not {'in'("std.id",stdIds)}
                }

            }
            projections {
                property('std.id')
            }
        }
    }

    def alternativeSubject(Student student, Boolean includeAll = false){
        def c = StudentSubjects.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("student", student )
                if (!includeAll) {
                    'in'("subjectType",SubjectType.chooseOptCom())
                }
            }
        }
    }

    def markEntryStudentsExcludingList(Section section, SubjectName subject, List stdIds){
        AcademicYear academicYear = schoolService.workingYear(section?.className)
        def c = StudentSubjects.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("academicYear", academicYear)
                eq("subject", subject )
                eq("std.section", section)
                eq("std.studentStatus", StudentStatus.NEW)
                if(stdIds){
                    not {'in'("std.id",stdIds)}
                }
            }
        }
        List dataReturns = new ArrayList()
        Student student
        for (stSub in results) {
            student = stSub.student
            dataReturns.add([id: student?.id, name: "${student?.studentID} - ${student?.name} - [Roll: ${student?.rollNo}]"])
        }
        return dataReturns
    }
    def studentIdBySubject(Section section, SubjectName subject){
        def c = StudentSubjects.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("subject", subject )
                eq("std.section", section)
                eq("std.studentStatus", StudentStatus.NEW)
            }
            projections {
                property('student')
            }
        }
        return results
    }


    boolean isOptional(Student student, SubjectName subject){
        StudentSubjects studentSubjects = StudentSubjects.findByStudentAndSubjectAndIsOptionalAndActiveStatus(student,subject,true, ActiveStatus.ACTIVE)
    }

    def studentSubjectIds(Student student) {
        def results = StdStudentSubjectView.findAllByStdId(student.id)
        return results?.collect{it.subjectId}
    }
    def studentSubjectDropDownList(Student student) {
        def results = StdStudentSubjectView.findAllByStdId(student.id)
        List dataReturns = new ArrayList()
        results.each { obj ->
            dataReturns.add([id: obj.subjectId, name: obj.subjectName])
        }
        return dataReturns
    }

    int subjectStudentCount(Section section, SubjectName subjectName) {
        return StdStudentSubjectView.countBySectionIdAndSubjectId(section.id, subjectName.id)
    }
    int subjectStudentCountByCls(Long classId, SubjectName subjectName) {
        return StdStudentSubjectView.countByClassNameIdAndSubjectId(classId, subjectName.id)
    }

    //Subject Student Id List
    def subjectStudentIdsList(Section section, SubjectName subjectName) {
        def results = StdStudentSubjectView.findAllBySectionIdAndSubjectId(section.id, subjectName.id)
        return results?.collect{it.stdId}
    }
    def subjectStudentDetailList(Section section, SubjectName subject) {
        List dataReturns = new ArrayList()
        def results = StdStudentSubjectView.findAllBySectionIdAndSubjectId(section.id, subject.id)
        results.each { obj ->
            dataReturns.add([id: obj.stdId, stuId: obj.studentid, stuName: obj.studentName, stuRoll: obj.rollNo, rating: '', comment: ''])
        }
        return dataReturns
    }
    def subjectStudentsNotInList(Section section, SubjectName subjectName, List idList){
        List dataReturns = new ArrayList()
        def c = StdStudentSubjectView.createCriteria()
        def results = c.list() {
            and {
                eq("sectionId", section.id)
                eq("subjectId", subjectName.id)
                if(idList){
                    not {'in'("stdId",idList)}
                }
            }
            order("rollNo", CommonUtils.SORT_ORDER_ASC)
        }
        results.each { obj ->
            dataReturns.add([id: obj.stdId, name: "${obj.studentid} - ${obj.studentName} - [Roll: ${obj.rollNo}]"])
        }
        return dataReturns
    }

    def saveOrUpdate(AddTransferSubjectCommand command){
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService?.principal.username
        String academicYear = command.academicYear.value
        int year
        AcademicYear nextYear

        if (academicYear.length() == 4) {
            year = Integer.parseInt(academicYear)
            year = year + 1
            nextYear = AcademicYear.getYearByString(year.toString())
        } else {
            year = Integer.parseInt(academicYear.substring(0, 4))
            int lowYear = year + 1
            int highYear = year + 2
            nextYear = AcademicYear.getYearByString(lowYear + "-" + highYear)
        }

        if(!nextYear){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${year} not initiated yet. Please contact admin")
            return result
        }

        def selectedIds = command.selectedStdId.split(',')
        Student student
        Student newStudent
        def oldSubjects
        long count = 0

        selectedIds.each { idx ->
            student = Student.read(idx.toLong())
            newStudent = studentService.findByStudentId(student.studentID, nextYear)
            if (newStudent) {
                oldSubjects = alternativeSubject(student, true)
                for (studentSub in oldSubjects) {
                    count++
                    if (StudentSubjects.countByActiveStatusAndStudentAndSubject(ActiveStatus.ACTIVE, newStudent, studentSub.subject) == 0) {
                        new StudentSubjects(student: newStudent, subject: studentSub.subject, subjectType: studentSub.subjectType, isOptional: studentSub.isOptional, parentSubId: studentSub.parentSubId, academicYear: nextYear, updatedBy: createOrUpdateBy).save(flush: true)
                    }
                }
            }
        }

        String message = "Subjects transfered successfully for " + count + " selected Students."
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def saveOrUpdateAlternative(GrailsParameterMap params) {
        LinkedHashMap result = new LinkedHashMap()
        Student student = Student.read(params.getLong('id'))
        SubjectName subjectName = SubjectName.read(params.getLong('subjectId'))
        if (!student || !subjectName) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        Boolean added = params.getBoolean('added')
        Boolean isOptional = params.getBoolean('isOptional')
        StudentSubjects studentSubjects
        if (added) {
            studentSubjects = StudentSubjects.findByStudentAndSubjectAndActiveStatus(student, subjectName, ActiveStatus.ACTIVE)
            if (studentSubjects) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Subject Already added")
                return result
            }

            ClassSubjects classSubjects = ClassSubjects.findByActiveStatusAndClassNameAndSubject(ActiveStatus.ACTIVE, student.className, subjectName)
            if (!classSubjects) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Subject is not mapping with class")
                return result
            }
            if (classSubjects.subjectType == SubjectType.INUSE) {
                ClassSubjects parentSubject = ClassSubjects.read(classSubjects.parentSubId)
                if (!parentSubject) {
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, "Subject is not allowed for this class")
                    return result
                }
                if (parentSubject.subjectType == SubjectType.ALTERNATIVE) {
                    studentSubjects = StudentSubjects.findByActiveStatusAndStudentAndParentSubId(ActiveStatus.ACTIVE, student, classSubjects.parentSubId)
                    if (studentSubjects) {
                        result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                        result.put(CommonUtils.MESSAGE, "Another Subject of same group already added. Please remove that first")
                        return result
                    }
                }
                new StudentSubjects(academicYear: student.academicYear,
                        student: student, subject: subjectName,
                        subjectType: SubjectType.INUSE, isOptional: isOptional,
                        createdBy: springSecurityService.principal.username,
                        parentSubId: classSubjects.parentSubId).save(flush: true)
            } else {
                new StudentSubjects(academicYear: student.academicYear,
                        student: student, subject: subjectName,
                        subjectType: SubjectType.OPTIONAL,
                        createdBy: springSecurityService.principal.username,
                        isOptional: isOptional).save(flush: true)
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            return result
        }

        studentSubjects = StudentSubjects.findByStudentAndSubjectAndActiveStatus(student, subjectName, ActiveStatus.ACTIVE)
        if (!studentSubjects) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Subject not added")
            return result
        }
        studentSubjects.activeStatus = ActiveStatus.DELETE
        studentSubjects.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        return result
    }

    def saveStudentSubjectCommonMapping(StudentSubjectCommand command){

        LinkedHashMap result = new LinkedHashMap()
        if (!command.subjectName) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        def selectedIds = command.selectedStdId.split(',')
        Student student
        StudentSubjects studentSubjects
        ClassSubjects classSubjects
        String createOrUpdateBy = springSecurityService.principal.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        selectedIds.each { idx ->
            student = Student.read(idx.toLong())
            if (command.added) {
                studentSubjects = StudentSubjects.findByStudentAndSubjectAndActiveStatus(student, command.subjectName, ActiveStatus.ACTIVE)
                if (!studentSubjects) {
                    classSubjects = ClassSubjects.findByActiveStatusAndClassNameAndSubject(ActiveStatus.ACTIVE, student.className, command.subjectName)
                    if (classSubjects) {
                        if (classSubjects.subjectType == SubjectType.INUSE) {
                            ClassSubjects parentSubject = ClassSubjects.read(classSubjects.parentSubId)
                            if (parentSubject.subjectType == SubjectType.ALTERNATIVE) {
                                studentSubjects = StudentSubjects.findByActiveStatusAndStudentAndParentSubId(ActiveStatus.ACTIVE, student, classSubjects.parentSubId)
                                if (!studentSubjects) {
                                    studentSubjects = new StudentSubjects(student: student, subject: command.subjectName,
                                            subjectType: SubjectType.INUSE, isOptional: command.isOptional,academicYear: academicYear,
                                            parentSubId: classSubjects.parentSubId, createdBy: createOrUpdateBy).save(flush: true)
                                }
                            } else {
                                studentSubjects = new StudentSubjects(
                                        student: student,
                                        subject: command.subjectName,
                                        subjectType: SubjectType.INUSE,
                                        isOptional: command.isOptional,
                                        parentSubId: classSubjects.parentSubId,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                ).save(flush: true)
                            }

                        } else {
                            studentSubjects = new StudentSubjects(
                                    student: student,
                                    subject: command.subjectName,
                                    subjectType: SubjectType.OPTIONAL,
                                    isOptional: command.isOptional,
                                    createdBy: createOrUpdateBy,
                                    academicYear: academicYear
                            ).save(flush: true)
                        }
                    }
                }
            } else {
                studentSubjects = StudentSubjects.findByStudentAndSubjectAndActiveStatus(student, command.subjectName, ActiveStatus.ACTIVE)
                if (studentSubjects) {
                    studentSubjects.activeStatus = ActiveStatus.DELETE
                    studentSubjects.updatedBy = createOrUpdateBy
                    studentSubjects.save(flush: true)
                }
            }
        }

        String message = "Subject Removed successfully for selected Students."
        if (command.added) {
            message = "Subject Added successfully for selected Students."
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}
