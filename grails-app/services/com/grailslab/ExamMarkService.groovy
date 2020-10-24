package com.grailslab

import com.grailslab.command.CtExamMarkCommand
import com.grailslab.command.HallExamMarkCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.*
import com.grailslab.stmgmt.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.web.servlet.mvc.GrailsParameterMap
import org.apache.commons.lang3.ArrayUtils
@Transactional
class ExamMarkService {
    def examMarkService
    def messageSource
    def examScheduleService
    def examService
    def springSecurityService
    def classSubjectsService
    def classNameService
    def shiftExamService
    def sessionFactory

    def gradePointService
    def studentSubjectsService
    def subjectService
    def schoolService
    def studentService
    def tabulationCtService
    def tabulationService

    static final String[] sortColumns = ['id','student']
    static final String[] ctMarkSortColumns = ['std.studentID','std.name','std.rollNo','ctObtainMark','hallObtainMark']
    static final String[] ct2MarkSortColumns = ['std.studentID','std.name','std.rollNo','ctObtainMark','ct2ObtainMark','hallObtainMark']
    static final String[] ct3MarkSortColumns = ['std.studentID','std.name','std.rollNo','ctObtainMark','ct2ObtainMark','ct3ObtainMark','hallObtainMark']
    static final String[] hallMarkSortColumns = ['std.studentID','std.name','std.rollNo','hallObtainMark']
    static final String[] hallMarkWithPracSortColumns = ['std.studentID','std.name','std.rollNo','hallWrittenMark','hallObjectiveMark','hallPracticalMark','hallSbaMark','hallObtainMark']


    def getCtMarkEntryList(GrailsParameterMap params, ExamSchedule examSchedule, SubjectName subject) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        String sortColumn = CommonUtils.getSortColumn(ctMarkSortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        int totalCount =0
        def c = ExamMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        totalCount = results.totalCount
        Student student
        if (totalCount > 0) {
            String ctMark
            String hallMark
            results.each { ExamMark examMark ->
                if(examMark.ctAttendStatus==AttendStatus.PRESENT){
                    ctMark =examMark.ctObtainMark
                } else {
                    ctMark = examMark.ctAttendStatus.value
                }
                if(examMark.hallAttendStatus==AttendStatus.PRESENT){
                    hallMark =examMark.hallObtainMark
                } else {
                    hallMark = examMark.hallAttendStatus.value
                }
                student = examMark.student
                dataReturns.add([DT_RowId: examMark.id, stdId:student.id, 0: student.studentID, 1: student.name, 2:student.rollNo, 3: ctMark, 4: hallMark, 5: subject.name, 6:''])

            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def getCt2MarkEntryList(GrailsParameterMap params, ExamSchedule examSchedule, SubjectName subject) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        String sortColumn = CommonUtils.getSortColumn(ct2MarkSortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        int totalCount =0
        def c = ExamMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("ct2Status", AttendStatus.NO_INPUT )
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        totalCount = results.totalCount
        Student student
        if (totalCount > 0) {
            String ctMark
            String ct2Mark
            results.each { ExamMark examMark ->
                if(examMark.ctAttendStatus==AttendStatus.PRESENT){
                    ctMark =examMark.ctObtainMark
                } else {
                    ctMark = examMark.ctAttendStatus.value
                }
                if(examMark.ct2Status==AttendStatus.PRESENT){
                    ct2Mark =examMark.ct2ObtainMark
                } else {
                    ct2Mark = examMark.ct2Status.value
                }

                student = examMark.student
                dataReturns.add([DT_RowId: examMark.id, stdId:student.id, 0: student.studentID, 1: student.name, 2:student.rollNo, 3: ctMark, 4: ct2Mark, 5: subject.name, 6:''])

            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }
    def getCt3MarkEntryList(GrailsParameterMap params, ExamSchedule examSchedule, SubjectName subject) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }

        String sortColumn = CommonUtils.getSortColumn(ct3MarkSortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        int totalCount =0
        def c = ExamMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("ct3Status", AttendStatus.NO_INPUT )
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        totalCount = results.totalCount
        Student student
        if (totalCount > 0) {
            String ctMark
            String ct2Mark
            String ct3Mark
            results.each { ExamMark examMark ->
                if(examMark.ctAttendStatus==AttendStatus.PRESENT){
                    ctMark =examMark.ctObtainMark
                } else {
                    ctMark = examMark.ctAttendStatus.value
                }
                if(examMark.ct2Status==AttendStatus.PRESENT){
                    ct2Mark =examMark.ct2ObtainMark
                } else {
                    ct2Mark = examMark.ct2Status.value
                }
                if(examMark.ct3Status==AttendStatus.PRESENT){
                    ct3Mark =examMark.ct3ObtainMark
                } else {
                    ct3Mark = examMark.ct3Status.value
                }

                student = examMark.student
                dataReturns.add([DT_RowId: examMark.id, stdId:student.id, 0: student.studentID, 1: student.name, 2:student.rollNo, 3: ctMark, 4: ct2Mark, 5: ct3Mark, 6: subject.name, 7:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def getAlreadyCtMarkStudents(ExamSchedule examSchedule, SubjectName subject, String ctMarkFor) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                if (ctMarkFor == "CLASS_TEST1") {
                    'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
                } else if (ctMarkFor == "CLASS_TEST2") {
                    'ne'("ct2Status", AttendStatus.NO_INPUT )
                } else if (ctMarkFor == "CLASS_TEST3") {
                    'ne'("ct3Status", AttendStatus.NO_INPUT )
                }
            }
            projections {
                property('std.id')
            }
        }
    }
    def getAlreadyHallMarkStudents(ExamSchedule examSchedule, SubjectName subject) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("hallAttendStatus", AttendStatus.NO_INPUT )
            }
            projections {
                property('std.id')
            }
        }
    }
    def getAllExamMark(ExamSchedule examSchedule, SubjectName subject) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("hallAttendStatus", AttendStatus.NO_INPUT )
            }
        }
        return results
    }

    def getAllCtExamMark(ExamSchedule examSchedule, SubjectName subject, String ctMarkFor) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                if (ctMarkFor == "CLASS_TEST1") {
                    'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
                } else if (ctMarkFor == "CLASS_TEST2") {
                    'ne'("ct2Status", AttendStatus.NO_INPUT )
                } else if (ctMarkFor == "CLASS_TEST3") {
                    'ne'("ct3Status", AttendStatus.NO_INPUT )
                }
            }
        }
        return results
    }
    def getStudentResultMarks(Exam exam, Student student) {
        def subjectIdList = studentSubjectsService.studentSubjectIds(student)
        if (!subjectIdList) return null

        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                'in'("sub.id", subjectIdList )
                eq('student', student)
//                eq('eSch.isExtracurricular', false)
                'ne'("hallAttendStatus", AttendStatus.NO_INPUT )
            }
        }
        return results
    }

    def getCtExamMark(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
            }
            projections {
                sum('ctObtainMark')
            }
        }
        return results[0]
    }

    def getCtFailCount(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                eq("ctResultStatus", ResultStatus.FAILED )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
            }
            projections {
                count()
            }
        }
        return results[0]
    }

    def getCt2ExamMark(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ct2Status", AttendStatus.NO_INPUT )
            }
            projections {
                sum('ct2ObtainMark')
            }
        }
        return results[0]
    }

    def getCt2FailCount(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                eq("ct2resultStatus", ResultStatus.FAILED )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ct2Status", AttendStatus.NO_INPUT )
            }
            projections {
                count()
            }
        }
        return results[0]
    }

    def getCt3ExamMark(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ct3Status", AttendStatus.NO_INPUT )
            }
            projections {
                sum('ct3ObtainMark')
            }
        }
        return results[0]
    }

    def getCt3FailCount(Exam exam, Student student, List subjectIdList) {
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('examSchedule', 'eSch')
            createAlias('subject', 'sub')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("eSch.activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                eq("ct3resultStatus", ResultStatus.FAILED )
                'in'("sub.id", subjectIdList )
                eq('student', student)
                'ne'("ct3Status", AttendStatus.NO_INPUT )
            }
            projections {
                count()
            }
        }
        return results[0]
    }

    def getStudentExamAttendCount(Exam exam, Student student) {
        return ExamMark.countByExamAndStudentAndActiveStatusAndHallAttendStatus(exam, student, ActiveStatus.ACTIVE, AttendStatus.PRESENT)
    }
    def getExamSubjectAbsentCount(List classExamList, def studentList, SubjectName subjectName) {
        return ExamMark.countByExamInListAndStudentInListAndSubjectAndActiveStatusAndHallAttendStatusNotEqual(classExamList, studentList, subjectName, ActiveStatus.ACTIVE, AttendStatus.PRESENT)
    }
    //for alternative subject
    def getExamSubjectAbsentCount(List classExamList, def studentList, String alterSubjectIds) {
        def alterSubjectList = subjectService.allSubjectInList(alterSubjectIds)
        return ExamMark.countByExamInListAndStudentInListAndSubjectInListAndActiveStatusAndHallAttendStatusNotEqual(classExamList, studentList, alterSubjectList, ActiveStatus.ACTIVE, AttendStatus.PRESENT)
    }
    def getExamSubjectPassCount(List classExamList, def studentList, SubjectName subjectName) {
        return ExamMark.countByExamInListAndStudentInListAndSubjectAndActiveStatusAndResultStatus(classExamList, studentList, subjectName, ActiveStatus.ACTIVE, ResultStatus.PASSED)
    }
    //for alternative subject
    def getExamSubjectPassCount(List examList, def studentList, String alterSubjectIds) {
        def alterSubjectList = subjectService.allSubjectInList(alterSubjectIds)
        return ExamMark.countByExamInListAndStudentInListAndSubjectInListAndActiveStatusAndResultStatus(examList, studentList, alterSubjectList, ActiveStatus.ACTIVE, ResultStatus.PASSED)
    }
    def getExamSubjectLetterGradeCount(List classExamList, def studentList, SubjectName subjectName, String lGrade) {
        return ExamMark.countByExamInListAndStudentInListAndSubjectAndActiveStatusAndLGrade(classExamList, studentList, subjectName, ActiveStatus.ACTIVE, lGrade)
    }
    def getExamSubjectResultCount(List classExamList, def studentList, SubjectName subjectName, ResultStatus resultStatus) {
        int resultCount = 0
        if (resultStatus) {
            resultCount = ExamMark.countByExamInListAndStudentInListAndSubjectAndActiveStatusAndResultStatus(classExamList, studentList, subjectName, ActiveStatus.ACTIVE, resultStatus)
        } else {
            resultCount = ExamMark.countByExamInListAndStudentInListAndSubjectAndActiveStatus(classExamList, studentList, subjectName, ActiveStatus.ACTIVE)
        }
        return resultCount
    }
    //for alternative subject
    def getExamSubjectLetterGradeCount(List classExamList, def studentList, String alterSubjectIds, String lGrade) {
        def alterSubjectList = subjectService.allSubjectInList(alterSubjectIds)
        return ExamMark.countByExamInListAndStudentInListAndSubjectInListAndActiveStatusAndLGrade(classExamList, studentList, alterSubjectList, ActiveStatus.ACTIVE, lGrade)
    }

    Integer getCtAttendCount(Long examId, Long subjectId){
        if (!examId) return 0
        final session = sessionFactory.currentSession
        final String query = "select count(id) from exam_mark where exam_id =:examId and subject_id =:subjectId and ct_attend_status = 'PRESENT' and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('examId', examId)
                .setParameter('subjectId', subjectId).list()
        return queryResults ? queryResults[0] : null
    }
    Integer getCtPassCount(Long examId, Long subjectId){
        if (!examId) return 0
        final session = sessionFactory.currentSession
        final String query = "select count(id) from exam_mark where exam_id =:examId and subject_id =:subjectId and ct_result_status = 'PASSED' and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('examId', examId)
                .setParameter('subjectId', subjectId).list()
        return queryResults ? queryResults[0] : null
    }
    Integer getHallAttendCount(Long examId, Long subjectId){
        if (!examId) return 0
        final session = sessionFactory.currentSession
        final String query = "select count(id) from exam_mark where exam_id =:examId and subject_id =:subjectId and hall_attend_status = 'PRESENT' and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('examId', examId)
                .setParameter('subjectId', subjectId).list()
        return queryResults ? queryResults[0] : null
    }
    Integer getHallAplusCount(Long examId, Long subjectId){
        if (!examId) return 0
        final session = sessionFactory.currentSession
        final String query = "select count(id) from exam_mark where exam_id =:examId and subject_id =:subjectId and result_status = 'PASSED' and l_grade = 'A+' and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('examId', examId)
                .setParameter('subjectId', subjectId).list()
        return queryResults ? queryResults[0] : null
    }
    Integer getHallPassCount(Long examId, Long subjectId){
        if (!examId) return 0
        final session = sessionFactory.currentSession
        final String query = "select count(id) from exam_mark where exam_id =:examId and subject_id =:subjectId and result_status = 'PASSED' and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('examId', examId)
                .setParameter('subjectId', subjectId).list()
        return queryResults ? queryResults[0] : null
    }

    Double getHighestClassExamMark(List examIds, ExamTerm examTerm, SubjectName subjectName, Boolean isSbaSubject = false) {
        String runningSchool = schoolService.runningSchool()
        def highestMark = ExamMark.createCriteria().get {
            createAlias('student', 'std')
            createAlias('exam', 'xm')
            and {
                'in'('xm.id',examIds)
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("subject", subjectName )
            }
            projections {
                if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL && examTerm == ExamTerm.FINAL_TEST) {
                    max "averageMark"
                } else {
                    if (isSbaSubject) {
                        max "tabulationNosbaMark"
                    } else {
                        max "tabulationMark"
                    }
                }

            }
        } as Double
        return highestMark ?:0
    }



    Double getAverageClassExamMark(Long classNameId, List examIds, ExamTerm examTerm, SubjectName subjectName, Boolean isSbaSubject = false) {
        String runningSchool = schoolService.runningSchool()

        def totalCount = studentSubjectsService.subjectStudentCountByCls(classNameId, subjectName)
        if (!totalCount || totalCount == 0) return 0.0

        def totalMark = ExamMark.createCriteria().get {
            createAlias('student', 'std')
            createAlias('exam', 'xm')
            and {
                'in'('xm.id',examIds)
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("subject", subjectName )
            }
            projections {
                if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL && examTerm == ExamTerm.FINAL_TEST) {
                    sum "averageMark"
                } else {
                    if (isSbaSubject) {
                        sum "tabulationNosbaMark"
                    } else {
                        sum "tabulationMark"
                    }
                }
            }
        } as Double

        Double average = 0
        if (totalMark) {
            average = totalMark/totalCount
        }

        return average.round(2)
    }


    def getHallMarkEntryList(GrailsParameterMap params, Section section, ExamSchedule examSchedule,SubjectName subject) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 2
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        Boolean isHallPractical = examSchedule.isHallPractical
        String sortColumn
        if (isHallPractical) {
            sortColumn = CommonUtils.getSortColumn(hallMarkWithPracSortColumns, iSortingCol)
        } else {
            sortColumn = CommonUtils.getSortColumn(hallMarkSortColumns, iSortingCol)
        }
        List dataReturns = new ArrayList()
        int totalCount =0
        def c = ExamMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                eq("subject", subject )
                'ne'("hallAttendStatus", AttendStatus.NO_INPUT )
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        totalCount = results.totalCount
        Student student

        if (totalCount > 0) {
            String hallMark
            results.each { ExamMark examMark ->
                if(examMark.hallAttendStatus==AttendStatus.PRESENT){
                    hallMark =examMark.hallObtainMark
                } else {
                    hallMark = examMark.hallAttendStatus.value
                }
                student = examMark.student
                if (isHallPractical) {
                    dataReturns.add([DT_RowId: examMark.id, stdId:student.id, 0:student.studentID, 1: student.name, 2:student.rollNo, 3:examMark.hallWrittenMark, 4: examMark.hallObjectiveMark, 5: examMark.hallPracticalMark,6: examMark.hallSbaMark,7: examMark.hallInput5, 8: hallMark, 9 :''])
                }
                else {
                    dataReturns.add([DT_RowId: examMark.id, stdId:student.id, 0:student.studentID, 1: student.name, 2:student.rollNo, 3: hallMark, 4: subject.name, 5:''])
                }
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def markEntryStdIds(ExamSchedule examSchedule, ExamType examType){
        def c = ExamMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("examSchedule", examSchedule )
                if(examType == ExamType.CLASS_TEST){
                    'ne'("ctAttendStatus", AttendStatus.NO_INPUT )
                }
                if(examType == ExamType.HALL_TEST){
                    'ne'("hallAttendStatus", AttendStatus.NO_INPUT )
                }
            }
            projections {
                property('std.id')
            }
        }
        return results
    }


    def calculateSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double optionalSubsMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100

        if(examSchedule.subjectType != SubjectType.COMPULSORY){
            isOptSubject = studentSubjectsService.isOptional(student, subject)
            if(isOptSubject){
                optionalSubsMark = totalEffExamMark * 0.4
                if(fullMark > optionalSubsMark){
                    optionalMark = fullMark - optionalSubsMark
                }else {
                    optionalMark = 0
                }
                if(tabulationEffPercentage ==100){
                    tabulationMark = optionalMark
                }else {
                    tabulationMark =tabulationEffPercentage * optionalMark * 0.01
                }
                examMark.isOptional = true
            } else {
                if(tabulationEffPercentage ==100){
                    tabulationMark = fullMark
                }else {
                    tabulationMark =tabulationEffPercentage * fullMark * 0.01
                }
            }
        }else {
            if(tabulationEffPercentage ==100){
                tabulationMark = fullMark
            }else {
                tabulationMark =tabulationEffPercentage * fullMark * 0.01
            }
        }
        examMark.tabulationMark = tabulationMark.round(2)
        //Grade point calculation
        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint = gradePointService.getByMark(percentageMark, className)
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
                if(isOptSubject || gradePoint.gPoint>0){
                    examMark.resultStatus = ResultStatus.PASSED
                } else {
                    examMark.resultStatus = ResultStatus.FAILED
                }
        }
        return true
    }
    def calculateIdealSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double optionalSubsMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100

        if (examSchedule.isExtracurricular) {
                examMark.isExtraCurricular = true
            }

        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)
        //ignored optional subject for ideal school result
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }
        //Grade point calculation
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
            if(gradePoint.gPoint>0){
                examMark.resultStatus = ResultStatus.PASSED
            } else {
                examMark.resultStatus = ResultStatus.FAILED
            }
        }

        examMark.tabulationMark = tabulationMark.round(2)

        return true
    }
    def calculateIdealFinalSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double optionalSubsMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100

        if (examSchedule.isExtracurricular) {
            examMark.isExtraCurricular = true
        }

        //ignored optional subject for ideal school result
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        } else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }

        Double hFullMark = 0
        Double hTabulationMark = 0
        Double avgFullMark
        Double avgMark
        if (!examSchedule.isOtherActivity){
            ExamMark halfYearlyMark = ExamMark.findByStudentAndSubjectAndExamNotEqualAndActiveStatus(student, subject, examMark.exam, ActiveStatus.ACTIVE)
            if (halfYearlyMark) {
                hFullMark = halfYearlyMark.fullMark
                hTabulationMark = halfYearlyMark.tabulationMark
                avgFullMark = (fullMark + hFullMark)/2
                avgMark = (tabulationMark + hTabulationMark)/2
            } else {
                avgFullMark = fullMark
                avgMark = tabulationMark
            }
        } else {
            avgFullMark = fullMark
            avgMark = tabulationMark
        }
        examMark.averageMark = avgMark
        examMark.halfYearlyMark = hTabulationMark


        def percentageMark = avgFullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)

        //Grade point calculation
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
            if(gradePoint.gPoint>0){
                examMark.resultStatus = ResultStatus.PASSED
            } else {
                examMark.resultStatus = ResultStatus.FAILED
            }
        }

        examMark.tabulationMark = tabulationMark.round(2)

        return [hTabulationMark, avgMark]
    }
    def calculateIdealFinalGroupSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject, Exam exam, SubjectName groupSubject, Double groupEffExmMark) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        Double tabulationMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }

        examMark.tabulationMark = tabulationMark.round(2)
        //Group Grade point calculation
        Double subFullMark = tabulationMark
        totalEffExamMark = totalEffExamMark + groupEffExmMark
        Double gTabulationMark = 0
        ExamMark groupSubMark = ExamMark.findByExamAndStudentAndSubjectAndActiveStatus(exam, student, groupSubject, ActiveStatus.ACTIVE)
        if (groupSubMark) {
            fullMark = fullMark + groupSubMark.fullMark
            gTabulationMark = groupSubMark.tabulationMark
        }



        Double hFullMark = 0
        Double hTabulationMark = 0
        ExamMark halfYearlyGroupMark = ExamMark.findByStudentAndSubjectAndExamNotEqualAndActiveStatus(student, groupSubject, examMark.exam, ActiveStatus.ACTIVE)
        if (halfYearlyGroupMark) {
            hFullMark = halfYearlyGroupMark.fullMark
            hTabulationMark = halfYearlyGroupMark.tabulationMark
        }
        Double subjectAvgMark = 0
        Double subjectHalfMark = 0
        ExamMark halfYearlyMark = ExamMark.findByStudentAndSubjectAndExamNotEqualAndActiveStatus(student, subject, examMark.exam, ActiveStatus.ACTIVE)
        if (halfYearlyMark) {
            subjectHalfMark = halfYearlyMark.tabulationMark
            hFullMark += halfYearlyMark.fullMark
            hTabulationMark += halfYearlyMark.tabulationMark
            subjectAvgMark = (subFullMark + subjectHalfMark)/2
        } else {
            subjectAvgMark = subFullMark
        }

        examMark.averageMark = subjectAvgMark
        examMark.halfYearlyMark = subjectHalfMark

        Double avgFullMark = (fullMark + hFullMark)/2
        Double avgMark = (gTabulationMark + tabulationMark + hTabulationMark)/2

        def percentageMark = avgFullMark * 100 /totalEffExamMark

        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
            if(gradePoint.gPoint>0){
                examMark.resultStatus = ResultStatus.PASSED
            } else {
                examMark.resultStatus = ResultStatus.FAILED
            }
        }
        //logic for
        return [hTabulationMark, avgMark]
    }
    def calculateIdealGroupSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject, Exam exam, SubjectName groupSubject, Double groupEffExmMark) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        Double tabulationMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }

        examMark.tabulationMark = tabulationMark.round(2)
        //Group Grade point calculation

        totalEffExamMark = totalEffExamMark + groupEffExmMark
        ExamMark groupSubMark = ExamMark.findByExamAndStudentAndSubjectAndActiveStatus(exam, student, groupSubject, ActiveStatus.ACTIVE)
        fullMark = fullMark + groupSubMark?.fullMark
        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
            if(gradePoint.gPoint>0){
                examMark.resultStatus = ResultStatus.PASSED
            } else {
                examMark.resultStatus = ResultStatus.FAILED
            }
        }
        //logic for
        return true
    }

    def calculateNhsSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0

        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0

        Double hallSbaMark = 0
        Double hallSbaObtainMark = 0
        if (className.subjectSba && examSchedule.hallSbaMark > 0) {
            hallSbaMark = examSchedule.hallSbaMark
            hallSbaObtainMark = examMark.hallSbaMark?:0
            hallExamMark = hallExamMark - hallSbaMark
            hallObtainMark = hallObtainMark - hallSbaObtainMark
        }

        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double tabulationNosbaMark = 0
        Double optionalSubsMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100

        if (examSchedule.isExtracurricular) {
            examMark.isExtraCurricular = true
        }

        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint
        if ((examSchedule.passMark && examSchedule.passMark > 0) || examSchedule.isPassSeparately) {
            if (examSchedule.passMark && examSchedule.passMark > 0) {
                if (percentageMark < examSchedule.passMark) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else {
                    gradePoint = gradePointService.getByMark(percentageMark, className)
                }
            }
            if (examSchedule.isPassSeparately){
                if (examSchedule.writtenPassMark && examSchedule.writtenPassMark > 0 && examSchedule.writtenPassMark > examMark.hallWrittenMark) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else if (examSchedule.practicalPassMark && examSchedule.practicalPassMark > 0 && examSchedule.practicalPassMark > examMark.hallPracticalMark) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else if (examSchedule.objectivePassMark && examSchedule.objectivePassMark > 0 && examSchedule.objectivePassMark > examMark.hallObjectiveMark) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else if (examSchedule.sbaPassMark && examSchedule.sbaPassMark > 0 && examSchedule.sbaPassMark > examMark.hallSbaMark) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else if (examSchedule.input5PassMark && examSchedule.input5PassMark > 0 && examSchedule.input5PassMark > examMark.hallInput5) {
                    gradePoint = GradePoint.findByLGrade(LetterGrade.GRADE_F)
                } else {
                    gradePoint = gradePointService.getByMark(percentageMark, className)
                }
            }
        } else {
            gradePoint = gradePointService.getByMark(percentageMark, className)
        }


            if(examSchedule.subjectType != SubjectType.COMPULSORY){
                if (examSchedule.isExtracurricular){
                    if(tabulationEffPercentage ==100){
                        tabulationMark = fullMark + hallSbaObtainMark
                        tabulationNosbaMark = fullMark
                    }else {
                        tabulationMark =tabulationEffPercentage * (fullMark + hallSbaObtainMark) * 0.01
                        tabulationNosbaMark =tabulationEffPercentage * fullMark  * 0.01
                    }
                    if(gradePoint){
                        examMark.gPoint = gradePoint.gPoint
                        examMark.lGrade = gradePoint.lGrade.value
                        examMark.resultStatus = ResultStatus.PASSED
                    }
                } else {
                    isOptSubject = studentSubjectsService.isOptional(student, subject)
                    if(isOptSubject){
                        optionalSubsMark = totalEffExamMark * 0.4
                        if(fullMark> optionalSubsMark){
                            optionalMark = fullMark  - optionalSubsMark
                        }else {
                            optionalMark = 0
                        }
                        if(tabulationEffPercentage ==100){
                            tabulationMark = optionalMark
                            tabulationNosbaMark = optionalMark + hallSbaObtainMark
                        }else {
                            tabulationMark =tabulationEffPercentage * (optionalMark + hallSbaObtainMark) * 0.01
                            tabulationNosbaMark =tabulationEffPercentage * optionalMark * 0.01
                        }
                        examMark.isOptional = true
                        //Grade point calculation

                        if(gradePoint){
                            examMark.gPoint = gradePoint.gPoint
                            examMark.lGrade = gradePoint.lGrade.value
                            examMark.resultStatus = ResultStatus.PASSED
                        }
                    } else {
                        if(tabulationEffPercentage ==100){
                            tabulationMark = fullMark + hallSbaObtainMark
                            tabulationNosbaMark = fullMark
                        }else {
                            tabulationMark =tabulationEffPercentage * (fullMark + hallSbaObtainMark) * 0.01
                            tabulationNosbaMark =tabulationEffPercentage * fullMark  * 0.01
                        }
                        //Grade point calculation
                        if(gradePoint){
                            examMark.gPoint = gradePoint.gPoint
                            examMark.lGrade = gradePoint.lGrade.value
                            if(gradePoint.gPoint>0){
                                examMark.resultStatus = ResultStatus.PASSED
                            } else {
                                examMark.resultStatus = ResultStatus.FAILED
                            }
                        }
                    }
                }

            } else {
                if(tabulationEffPercentage ==100){
                    tabulationMark = fullMark + hallSbaObtainMark
                    tabulationNosbaMark = fullMark
                }else {
                    tabulationMark =tabulationEffPercentage * (fullMark + hallSbaObtainMark) * 0.01
                    tabulationNosbaMark =tabulationEffPercentage * fullMark  * 0.01
                }
                //Grade point calculation
                if(gradePoint){
                    examMark.gPoint = gradePoint.gPoint
                    examMark.lGrade = gradePoint.lGrade.value
                    if(gradePoint.gPoint>0){
                        examMark.resultStatus = ResultStatus.PASSED
                    } else {
                        examMark.resultStatus = ResultStatus.FAILED
                    }
                }
            }

        examMark.tabulationMark = tabulationMark.round(2)
        examMark.tabulationNosbaMark = tabulationNosbaMark.round(2)

        return true
    }

    def calculateNhsGroupSubjectMark(ExamSchedule examSchedule, Student student, ExamMark examMark, ClassName className, SubjectName subject, Exam exam, SubjectName groupSubject, Double groupEffExmMark) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark?:0
            Double ctEffMark = examSchedule.ctMarkEffPercentage?:100
            Double ctObtainMark = examMark.ctObtainMark?:0
            if(ctEffMark ==100){
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            }else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0

        Double hallSbaMark = 0
        Double hallSbaObtainMark = 0
        if (className.subjectSba && examSchedule.hallSbaMark > 0) {
            hallSbaMark = examSchedule.hallSbaMark
            hallSbaObtainMark = examMark.hallSbaMark?:0
            hallExamMark = hallExamMark - hallSbaMark
            hallObtainMark = hallObtainMark - hallSbaObtainMark
        }

        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        Double tabulationMark = 0
        Double tabulationNosbaMark = 0
        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark + hallSbaObtainMark
            tabulationNosbaMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * (fullMark + hallSbaObtainMark) * 0.01
            tabulationNosbaMark =tabulationEffPercentage * fullMark  * 0.01
        }

        examMark.tabulationMark = tabulationMark.round(2)
        examMark.tabulationNosbaMark = tabulationNosbaMark.round(2)
        //Group Grade point calculation

        totalEffExamMark = totalEffExamMark + groupEffExmMark
        ExamMark groupSubMark = ExamMark.findByExamAndStudentAndSubjectAndActiveStatus(exam, student, groupSubject, ActiveStatus.ACTIVE)
        if (groupSubMark) {
            fullMark = fullMark + groupSubMark.fullMark
        }

        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)
        if(gradePoint){
            examMark.gPoint = gradePoint.gPoint
            examMark.lGrade = gradePoint.lGrade.value
            if(gradePoint.gPoint>0){
                examMark.resultStatus = ResultStatus.PASSED
            } else {
                examMark.resultStatus = ResultStatus.FAILED
            }
        }
        //logic for
        return true
    }

    def calculateAdarshaSubjectMark(int numberOfCt, ExamMark examMark, ClassName className, ExamSchedule examSchedule) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark ?: 0
            if (numberOfCt >= 2) {
                ctExamMark += examSchedule.ctExamMark2 ?: 0
            }
            if (numberOfCt == 3) {
                ctExamMark += examSchedule.ctExamMark3 ?: 0
            }
            Double ctEffMark = examSchedule.ctMarkEffPercentage ?: 100
            Double ctObtainMark = examMark.ctObtainMark ?: 0
            if (numberOfCt >= 2) {
                ctObtainMark += examMark.ct2ObtainMark
            }
            if (numberOfCt == 3) {
                ctObtainMark += examMark.ct3ObtainMark
            }
            if (ctEffMark == 100) {
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            } else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double optionalSubsMark = 0

        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }

        examMark.tabulationMark = tabulationMark.round(2)
        //Grade point calculation
        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)

        if (examSchedule.isExtracurricular) {
            examMark.isExtraCurricular = true
            if(gradePoint){
                examMark.gPoint = gradePoint.gPoint
                examMark.lGrade = gradePoint.lGrade.value
                examMark.resultStatus = ResultStatus.PASSED
            }
        } else {
            if(gradePoint){
                examMark.gPoint = gradePoint.gPoint
                examMark.lGrade = gradePoint.lGrade.value
                if(gradePoint.gPoint>0){
                    examMark.resultStatus = ResultStatus.PASSED
                } else {
                    examMark.resultStatus = ResultStatus.FAILED
                }
            }
        }


        return true
    }
    def calculateLightHouseSubjectMark(int numberOfCt, ExamMark examMark, ClassName className, ExamSchedule examSchedule) {
        Double ctMark =0
        Double ctExamEffMark = 0
        if (examSchedule.isCtExam) {
            Double ctExamMark = examSchedule.ctExamMark ?: 0
            if (numberOfCt >= 2) {
                ctExamMark += examSchedule.ctExamMark2 ?: 0
            }
            if (numberOfCt == 3) {
                ctExamMark += examSchedule.ctExamMark3 ?: 0
            }
            Double ctEffMark = examSchedule.ctMarkEffPercentage ?: 100
            Double ctObtainMark = examMark.ctObtainMark ?: 0
            if (numberOfCt >= 2) {
                ctObtainMark += examMark.ct2ObtainMark
            }
            if (numberOfCt == 3) {
                ctObtainMark += examMark.ct3ObtainMark
            }
            if (ctEffMark == 100) {
                ctMark = ctObtainMark
                ctExamEffMark = ctExamMark
            } else {
                ctMark = ctEffMark * ctObtainMark * 0.01
                ctExamEffMark = ctEffMark * ctExamMark * 0.01
            }
        }
        examMark.ctMark = ctMark.round(2)

        // now hall mark
        Double hallExamMark = examSchedule.hallExamMark?:0
        Double hallEffMark = examSchedule.hallMarkEffPercentage?:100
        Double hallObtainMark = examMark.hallObtainMark?:0
        Double hallMark =0
        Double hallExamEffMark = 0
        if(hallEffMark ==100){
            hallMark = hallObtainMark
            hallExamEffMark = hallExamMark
        }else {
            hallMark = hallEffMark * hallObtainMark * 0.01
            hallExamEffMark = hallEffMark * hallExamMark * 0.01
        }
        examMark.hallMark = hallMark.round(2)

        //Full Mark
        Double fullMark = ctMark+hallMark
        Double optionalMark = 0
        examMark.fullMark = fullMark.round(2)


        Double totalEffExamMark = ctExamEffMark + hallExamEffMark

        //optional and tabulation mark calculation
        boolean isOptSubject = false
        Double tabulationMark = 0
        Double optionalSubsMark = 0

        Double tabulationEffPercentage = examSchedule.tabulationEffPercentage?:100
        if(tabulationEffPercentage ==100){
            tabulationMark = fullMark
        }else {
            tabulationMark =tabulationEffPercentage * fullMark * 0.01
        }

        examMark.tabulationMark = tabulationMark.round(2)
        //Grade point calculation
        def percentageMark = fullMark * 100 /totalEffExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)

        if (examSchedule.isExtracurricular) {
            examMark.isExtraCurricular = true
            if(gradePoint){
                examMark.gPoint = gradePoint.gPoint
                examMark.lGrade = gradePoint.lGrade.value
                examMark.resultStatus = ResultStatus.PASSED
            }
        } else {
            if(gradePoint){
                examMark.gPoint = gradePoint.gPoint
                examMark.lGrade = gradePoint.lGrade.value
                if(gradePoint.gPoint>0){
                    examMark.resultStatus = ResultStatus.PASSED
                } else {
                    examMark.resultStatus = ResultStatus.FAILED
                }
            }
        }


        return true
    }

    ResultStatus getCtResultStatus(ClassName className, Double ctExamMark, Double ctObtainMark) {
        Double percentageMark = (ctObtainMark * 100) / ctExamMark
        GradePoint gradePoint =gradePointService.getByMark(percentageMark, className)
        if(gradePoint){
            if(gradePoint.gPoint>0){
                return ResultStatus.PASSED
            } else {
                return ResultStatus.FAILED
            }
        }
        return ResultStatus.FAILED
    }




    Boolean markHallComplete(ShiftExam shiftExam, Exam exam, ClassName className, Section section, String subjectIds,  ExamSchedule examSchedule) {
        SubjectName subject = examSchedule.subject
        if (examSchedule.subjectType != SubjectType.COMPULSORY || section.groupName) {
            int numOfSubjectStudent = studentSubjectsService.subjectStudentCount(section, subject)
            if (numOfSubjectStudent == 0) {
                examSchedule.highestMark = 0
                examSchedule.averageMark = 0
                examSchedule.isHallMarkInput = true
                examSchedule.save(flush: true)
                return true
            }
        }

        List<ExamMark> allExamMarks = getAllExamMark(examSchedule, subject)
        if (allExamMarks.size() == 0) {
            return false
        }

        def examSubjectIds = subjectIds.split(',')
        int idxOfSub = -1

        if (examSchedule.subjectType == SubjectType.COMPULSORY) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.OPTIONAL ) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.INUSE) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, examSchedule.parentSubId.toString())
        }

        if(idxOfSub == -1){
            return false
        }

        boolean subjectGroup = className.subjectGroup
        boolean subjectSba = className.subjectSba
        SubjectName groupSubject
        Double groupEffExmMark = 0
        int numberOfCt = shiftExam.ctExam ?: 0
        if (subjectGroup && (idxOfSub == 1 || idxOfSub == 3)){
            Long groupSubId
            if (idxOfSub == 1){
                groupSubId = Long.valueOf(examSubjectIds[0])
            } else if (idxOfSub == 3) {
                groupSubId = Long.valueOf(examSubjectIds[2])
            }
            groupSubject = SubjectName.read(groupSubId)

            ExamSchedule groupClsSchedule = ExamSchedule.findByActiveStatusAndExamAndSubject(ActiveStatus.ACTIVE, exam, groupSubject)
            Double gCtExamEffMark = 0
            if (groupClsSchedule.isCtExam) {
                Double ctExamMark = groupClsSchedule.ctExamMark
                if (numberOfCt >= 2) {
                    numberOfCt += groupClsSchedule.ctExamMark2
                }
                if (numberOfCt == 3) {
                    numberOfCt += groupClsSchedule.ctExamMark3
                }
                Double ctEffMark = groupClsSchedule.ctMarkEffPercentage?:100
                if(ctEffMark ==100){
                    gCtExamEffMark = ctExamMark
                }else {
                    gCtExamEffMark = ctEffMark * ctExamMark * 0.01
                }
            }
            Double gHallExamEffMark = 0
            Double hallExamMark = groupClsSchedule.hallExamMark?:0
            Double hallSbaMark = groupClsSchedule.hallSbaMark?:0
            Double hallEffMark = groupClsSchedule.hallMarkEffPercentage?:100

            hallExamMark = hallExamMark - hallSbaMark
            if(hallEffMark ==100){
                gHallExamEffMark = hallExamMark
            }else {
                gHallExamEffMark = hallEffMark * hallExamMark * 0.01
            }
            groupEffExmMark = gCtExamEffMark + gHallExamEffMark
        }


        String runningSchool = schoolService.runningSchool()
        Student student
        String fieldName = "subject${idxOfSub}Mark"
        String hallFieldName = "subject${idxOfSub}hall"
        String hallObtainName = "subject${idxOfSub}hallObtain"
        String ctFieldName = "subject${idxOfSub}ct"
        String ctaFieldName = "subject${idxOfSub}cta"

        if (runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL) {
            Tabulation tabulation
            TabulationDetails details

            String fieldWritten = "subject${idxOfSub}Written"
            String fieldPractical = "subject${idxOfSub}Practical"
            String fieldObjective = "subject${idxOfSub}Objective"
            String fieldSba = "subject${idxOfSub}Sba"
            String fieldInput5 = "subject${idxOfSub}Input5"
            String fieldAvg = "subject${idxOfSub}avg"
            String fieldgp = "subject${idxOfSub}gp"
            String fieldComment = "subject${idxOfSub}Comment"

            for (examMark in allExamMarks) {
                student = examMark.student
                tabulation = tabulationService.getTabulation(exam, student)
                if(exam.examTerm == ExamTerm.FIRST_TERM || exam.examTerm == ExamTerm.SECOND_TERM || exam.examTerm == ExamTerm.HALF_YEARLY || exam.examTerm == ExamTerm.TEST_EXAM || exam.examTerm == ExamTerm.TERM_EXAM || exam.examTerm == ExamTerm.MODEL_TEST){ // all term exams
                    calculateLightHouseSubjectMark(numberOfCt, examMark, className, examSchedule)
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    }else {
                        tabulation = tabulationService.entryTermSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryTermDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }
                } else if (exam.examTerm == ExamTerm.FINAL_TEST){
                    //firnl term tabulation on else block
                    calculateLightHouseSubjectMark(numberOfCt, examMark, className, examSchedule)
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    }else {
                        tabulation = tabulationService.entryFinalSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details."${fieldComment}"= tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryFinalDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }
                }

                examMark.idxOfSub = idxOfSub
                examMark.save(flush: true)
            }

        } else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL) {
            Tabulation tabulation
            TabulationDetails details

            String fieldWritten = "subject${idxOfSub}Written"
            String fieldPractical = "subject${idxOfSub}Practical"
            String fieldObjective = "subject${idxOfSub}Objective"
            String fieldSba = "subject${idxOfSub}Sba"
            String fieldInput5 = "subject${idxOfSub}Input5"
            String fieldAvg = "subject${idxOfSub}avg"
            String fieldgp = "subject${idxOfSub}gp"
            String fieldComment = "subject${idxOfSub}Comment"

            for (examMark in allExamMarks) {
                student = examMark.student
                tabulation = tabulationService.getTabulation(exam, student)
                if(exam.examTerm == ExamTerm.FIRST_TERM || exam.examTerm == ExamTerm.SECOND_TERM || exam.examTerm == ExamTerm.HALF_YEARLY || exam.examTerm == ExamTerm.TEST_EXAM || exam.examTerm == ExamTerm.TERM_EXAM || exam.examTerm == ExamTerm.MODEL_TEST){ // all term exams
                    calculateAdarshaSubjectMark(numberOfCt, examMark, className, examSchedule)
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    }else {
                        tabulation = tabulationService.entryTermSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryTermDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }
                } else if (exam.examTerm == ExamTerm.FINAL_TEST){
                    //firnl term tabulation on else block
                    calculateAdarshaSubjectMark(numberOfCt, examMark, className, examSchedule)
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    }else {
                        tabulation = tabulationService.entryFinalSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details."${fieldComment}"= tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryFinalDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details."${fieldComment}"=tabulationService.subjectComment(examMark, examSchedule, subject)
                        details.save(flush: true)
                    }
                }

                examMark.idxOfSub = idxOfSub
                examMark.save(flush: true)
            }

        } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            Tabulation tabulation
            TabulationDetails details
            TabulationAvgMark tabulationAvgMark

            String fieldAvg = "subject${idxOfSub}avg"
            String fieldTerm = "subject${idxOfSub}term"
            Double termMark, avgMark

            String fieldWritten = "subject${idxOfSub}Written"
            String fieldPractical = "subject${idxOfSub}Practical"
            String fieldObjective = "subject${idxOfSub}Objective"
            String fieldSba = "subject${idxOfSub}Sba"
            String fieldInput5 = "subject${idxOfSub}Input5"
            String fieldgp = "subject${idxOfSub}gp"

            for (examMark in allExamMarks) {
                student = examMark.student
                if(exam.examTerm == ExamTerm.FIRST_TERM || exam.examTerm == ExamTerm.SECOND_TERM || exam.examTerm == ExamTerm.HALF_YEARLY || exam.examTerm == ExamTerm.TEST_EXAM || exam.examTerm == ExamTerm.TERM_EXAM || exam.examTerm == ExamTerm.MODEL_TEST){ // all term exams
                    if (subjectGroup && (idxOfSub == 1 || idxOfSub == 3)){
                        calculateIdealGroupSubjectMark(examSchedule, student, examMark, className, subject, exam, groupSubject, groupEffExmMark)
                    } else {
                        calculateIdealSubjectMark(examSchedule, student, examMark, className, subject)
                    }
                    tabulation = tabulationService.getTabulation(exam, student)
                    if(!tabulation){
                        tabulation = tabulationService.termTabulation(className, section, exam, exam.examTerm, student)
                    }
                    tabulation."${ctFieldName}"=examMark.ctObtainMark
                    tabulation."${fieldName}"=examMark.tabulationMark
                    tabulation.save(flush: true)
                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if (details) {
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details.save(flush: true)
                    } else {
                        details = tabulationService.entryTermDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details.save(flush: true)
                    }
                } else if(exam.examTerm == ExamTerm.FINAL_TEST){
                    if (subjectGroup && (idxOfSub == 1 || idxOfSub == 3)){
                        (termMark, avgMark) = calculateIdealFinalGroupSubjectMark(examSchedule, student, examMark, className, subject, exam, groupSubject, groupEffExmMark)
                    } else {
                        (termMark, avgMark) =  calculateIdealFinalSubjectMark(examSchedule, student, examMark, className, subject)
                    }
                    tabulation = tabulationService.getTabulation(exam, student)
                    if(!tabulation){
                        tabulation = tabulationService.finalTabulation(className, section, exam, student)
                    }

                    tabulation."${ctFieldName}"=examMark.ctObtainMark
                    tabulation."${fieldName}"=examMark.tabulationMark
                    tabulation.save(flush: true)

                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryFinalDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details.save(flush: true)
                    }
                    tabulationAvgMark = tabulationService.getTabulationAvgMark(tabulation?.id)
                    if (!tabulationAvgMark) {
                        tabulationAvgMark = new TabulationAvgMark(tabulationId: tabulation?.id)
                    }
                    tabulationAvgMark."${fieldAvg}"=avgMark?.round(2)
                    tabulationAvgMark."${fieldTerm}"= termMark
                    tabulationAvgMark."${hallFieldName}"= examMark.hallMark
                    tabulationAvgMark."${hallObtainName}"= examMark.hallObtainMark
                    tabulationAvgMark."${ctaFieldName}"= examMark.ctMark
                    tabulationAvgMark.save(flush: true)
                }
                //need to handle firnl term tabulation on else block
                examMark.idxOfSub = idxOfSub
                examMark.save(flush: true)
            }
        } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
            Tabulation tabulation
            TabulationDetails details
            TabulationAvgMark tabulationAvgMark

            String fieldAvg = "subject${idxOfSub}avg"
            String fieldTerm = "subject${idxOfSub}term"
            Double termMark, avgMark

            String fieldWritten = "subject${idxOfSub}Written"
            String fieldPractical = "subject${idxOfSub}Practical"
            String fieldObjective = "subject${idxOfSub}Objective"
            String fieldSba = "subject${idxOfSub}Sba"
            String fieldInput5 = "subject${idxOfSub}Input5"
            String fieldgp = "subject${idxOfSub}gp"

            for (examMark in allExamMarks) {
                student = examMark.student
                if(exam.examTerm == ExamTerm.FIRST_TERM || exam.examTerm == ExamTerm.SECOND_TERM || exam.examTerm == ExamTerm.HALF_YEARLY || exam.examTerm == ExamTerm.TEST_EXAM || exam.examTerm == ExamTerm.TERM_EXAM || exam.examTerm == ExamTerm.MODEL_TEST){ // all term exams
                    if (subjectGroup && (idxOfSub == 1 || idxOfSub == 3)){
                        calculateNhsGroupSubjectMark(examSchedule, student, examMark, className, subject, exam, groupSubject, groupEffExmMark)
                    } else {
                        calculateNhsSubjectMark(examSchedule, student, examMark, className, subject)
                    }
                    tabulation = tabulationService.getTabulation(exam, student)
                    if(!tabulation){
                        tabulation = tabulationService.termTabulation(className, section, exam, exam.examTerm, student)
                    }
                    tabulation."${ctFieldName}"=examMark.ctObtainMark
                    tabulation."${fieldName}"=subjectSba ? examMark.tabulationNosbaMark : examMark.tabulationMark
                    tabulation.save(flush: true)

                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryTermDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details.save(flush: true)
                    }
                } else if(exam.examTerm == ExamTerm.FINAL_TEST){
                    if (subjectGroup && (idxOfSub == 1 || idxOfSub == 3)){
                        calculateNhsGroupSubjectMark(examSchedule, student, examMark, className, subject, exam, groupSubject, groupEffExmMark)
                    } else {
                        calculateNhsSubjectMark(examSchedule, student, examMark, className, subject)
                    }
                    tabulation = tabulationService.getTabulation(exam, student)
                    if(!tabulation){
                        tabulation = tabulationService.finalTabulation(className, section, exam, student)
                    }
                    tabulation."${ctFieldName}"=examMark.ctObtainMark
                    tabulation."${fieldName}"=subjectSba ? examMark.tabulationNosbaMark : examMark.tabulationMark
                    tabulation.save(flush: true)

                    details = tabulationService.getTabulationDetails(tabulation?.id)
                    if(details){
                        if (examSchedule.isHallPractical){
                            details."${fieldWritten}"=examMark.hallWrittenMark
                            details."${fieldPractical}"=examMark.hallPracticalMark
                            details."${fieldObjective}"=examMark.hallObjectiveMark
                            details."${fieldSba}"=examMark.hallSbaMark
                            details."${fieldInput5}"=examMark.hallInput5
                        } else {
                            details."${fieldWritten}"=examMark.hallObtainMark
                        }
                        details."${fieldgp}"=examMark.gPoint
                        details.save(flush: true)
                    }else {
                        details = tabulationService.entryFinalDetailMark(tabulation, fieldWritten, fieldPractical, fieldObjective, fieldSba, fieldInput5, fieldgp, examMark, examSchedule.isHallPractical)
                        details.save(flush: true)
                    }
                }
                //need to handle firnl term tabulation on else block
                examMark.idxOfSub = idxOfSub
                examMark.save(flush: true)
            }
        } else if (runningSchool == CommonUtils.BAILY_SCHOOL){
            Tabulation tabulation
            for (examMark in allExamMarks) {
                student = examMark.student
                calculateSubjectMark(examSchedule, student, examMark, className, subject)
                tabulation = tabulationService.getTabulation(exam, student)
                if(exam.examTerm == ExamTerm.FIRST_TERM || exam.examTerm == ExamTerm.SECOND_TERM || exam.examTerm == ExamTerm.HALF_YEARLY || exam.examTerm == ExamTerm.TEST_EXAM || exam.examTerm == ExamTerm.TERM_EXAM || exam.examTerm == ExamTerm.MODEL_TEST){ // all term exams
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    }else {
                        tabulationService.entryTermSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                } else if (exam.examTerm == ExamTerm.FINAL_TEST) {
                    if(tabulation){
                        tabulation."${fieldName}"=examMark.tabulationMark.round(2)
                        tabulation.save(flush: true)
                    } else {
                        tabulation = tabulationService.entryFinalSubjectMark(className, section, exam, student, fieldName, examMark.tabulationMark)
                    }
                }
                examMark.idxOfSub = idxOfSub
                examMark.save(flush: true)
            }
        }
        //highest and averageMark calcualted on Merit Position calculatation
        examSchedule.highestMark = 0
        examSchedule.averageMark = 0
        examSchedule.isHallMarkInput = true
        examSchedule.save(flush: true)
        return true
    }

    void deleteUnusedExamMark(Exam exam, ExamSchedule examSchedule, List<Student> subjectStudentIdList) {
        ExamMark.executeUpdate("update ExamMark em set em.activeStatus =:deleteStatus where em.exam =:exam " +
                "and em.examSchedule =:examSchedule and em.activeStatus =:activeStatus and em.student not in (:subjectStudentIdList) ",
                [deleteStatus: ActiveStatus.DELETE, exam: exam, examSchedule: examSchedule,
                 activeStatus: ActiveStatus.ACTIVE, subjectStudentIdList: subjectStudentIdList])
    }

//    new implementation
    Boolean markCt1Complete(Exam exam, Section section, ExamSchedule examSchedule, ClassName className){
        SubjectName subject = examSchedule.subject
        if (examSchedule.subjectType != SubjectType.COMPULSORY) {
            int numOfSubjectStudent = studentSubjectsService.subjectStudentCount(section, subject)
            if (numOfSubjectStudent == 0) {
                return true
            }
        }
        Double ctExamMark = examSchedule.ctExamMark
        if(!ctExamMark || ctExamMark < 0){
            return false
        }
        def allExamMarks = getAllCtExamMark(examSchedule,subject, "CLASS_TEST1")
        if (!allExamMarks) {
            return false
        }

        def examSubjectIds = exam.ctSubjectIds.split(',')
        int idxOfSub = -1

        if (examSchedule.subjectType == SubjectType.COMPULSORY) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.OPTIONAL ) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.INUSE) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, examSchedule.parentSubId.toString())
        }

        if(idxOfSub == -1){
            return false
        }

        String fieldName = "subject${idxOfSub}Mark"
        Student student
        TabulationCt tabulationCt
        Double ctObtainMark
        for (examMark in allExamMarks) {
            student = examMark.student
            ctObtainMark = examMark.ctObtainMark
            examMark.ctResultStatus = getCtResultStatus(className, ctExamMark, ctObtainMark)
            tabulationCt = tabulationCtService.getTabulation(exam, student, 1)
            if(tabulationCt){
                tabulationCt."${fieldName}"= ctObtainMark
                tabulationCt.save(flush: true)
            }else {
                tabulationCtService.entrySubjectMark(className, section, exam, student, fieldName, ctObtainMark, 1)
            }
            examMark.save(flush: true)
        }
        return true
    }
    Boolean markCt2Complete(Exam exam, Section section, ExamSchedule examSchedule, ClassName className){
        SubjectName subject = examSchedule.subject
        if (examSchedule.subjectType != SubjectType.COMPULSORY) {
            int numOfSubjectStudent = studentSubjectsService.subjectStudentCount(section, subject)
            if (numOfSubjectStudent == 0) {
                return true
            }
        }
        Double ctExamMark = examSchedule.ctExamMark2
        if(!ctExamMark || ctExamMark < 0){
            return false
        }
        def allExamMarks = getAllCtExamMark(examSchedule,subject, "CLASS_TEST2")
        if (!allExamMarks) {
            return false
        }

        def examSubjectIds = exam.ctSubjectIds.split(',')
        int idxOfSub = -1

        if (examSchedule.subjectType == SubjectType.COMPULSORY) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.OPTIONAL ) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.INUSE) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, examSchedule.parentSubId.toString())
        }

        if(idxOfSub == -1){
            return false
        }

        String fieldName = "subject${idxOfSub}Mark"
        Student student
        TabulationCt tabulationCt
        Double ctObtainMark
        for (examMark in allExamMarks) {
            student = examMark.student
            ctObtainMark = examMark.ct2ObtainMark
            examMark.ctResultStatus = getCtResultStatus(className, ctExamMark, ctObtainMark)
            tabulationCt = tabulationCtService.getTabulation(exam, student, 2)
            if(tabulationCt){
                tabulationCt."${fieldName}"= ctObtainMark
                tabulationCt.save(flush: true)
            }else {
                tabulationCtService.entrySubjectMark(className, section, exam, student, fieldName, ctObtainMark, 2)
            }
            examMark.save(flush: true)
        }
        return true
    }
    Boolean markCt3Complete(Exam exam, Section section, ExamSchedule examSchedule, ClassName className){
        SubjectName subject = examSchedule.subject
        if (examSchedule.subjectType != SubjectType.COMPULSORY) {
            int numOfSubjectStudent = studentSubjectsService.subjectStudentCount(section, subject)
            if (numOfSubjectStudent == 0) {
                return true
            }
        }
        Double ctExamMark = examSchedule.ctExamMark3
        if(!ctExamMark || ctExamMark < 0){
            return false
        }
        def allExamMarks = getAllCtExamMark(examSchedule,subject, "CLASS_TEST3")
        if (!allExamMarks) {
            return false
        }

        def examSubjectIds = exam.ctSubjectIds.split(',')
        int idxOfSub = -1

        if (examSchedule.subjectType == SubjectType.COMPULSORY) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.OPTIONAL ) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, subject.id.toString())
        } else if (examSchedule.subjectType == SubjectType.INUSE) {
            idxOfSub = ArrayUtils.indexOf(examSubjectIds, examSchedule.parentSubId.toString())
        }

        if(idxOfSub == -1){
            return false
        }

        String fieldName = "subject${idxOfSub}Mark"
        Student student
        TabulationCt tabulationCt
        Double ctObtainMark
        for (examMark in allExamMarks) {
            student = examMark.student
            ctObtainMark = examMark.ct3ObtainMark
            examMark.ctResultStatus = getCtResultStatus(className, ctExamMark, ctObtainMark)
            tabulationCt = tabulationCtService.getTabulation(exam, student, 3)
            if(tabulationCt){
                tabulationCt."${fieldName}"= ctObtainMark
                tabulationCt.save(flush: true)
            }else {
                tabulationCtService.entrySubjectMark(className, section, exam, student, fieldName, ctObtainMark, 3)
            }
            examMark.save(flush: true)
        }
        return true
    }

    def specialMarkEntry(Long id, GrailsParameterMap params) {

        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if (!examSchedule) {
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        Exam exam = examSchedule.exam
        SubjectName subject = examSchedule.subject

        ExamType examType = ExamType.valueOf(params.examType)
        Double markObtain = params.getDouble('markObtain')

        if (examType == ExamType.CLASS_TEST) {
            if (examSchedule.ctExamMark < markObtain) {
                result.put(CommonUtils.MESSAGE, "Invalid mark entry. Please enter upto ${examSchedule.ctExamMark}")
                return result
            }
        } else {
            if (examSchedule.isHallMarkInput) {
                result.put(CommonUtils.MESSAGE, "Mark entry already completed.")
                return result
            }
            if (examSchedule.hallExamMark < markObtain) {
                result.put(CommonUtils.MESSAGE, "Invalid mark entry. Please enter upto ${examSchedule.hallExamMark}")
                return result
            }
        }

        def studentList

        if (examSchedule.subjectType == SubjectType.COMPULSORY) {
            studentList = studentService.allStudent(exam.section)
        } else {
            def subjectStudents = studentSubjectsService.subjectStudentsExcludingList(exam.section, subject, null)
            studentList = studentService.allStudentInList(subjectStudents)
        }
        if (!studentList || studentList.size() == 0) {
            result.put(CommonUtils.MESSAGE, "No Student found for mark entry")
            return result
        }

        AttendStatus attendStatus = AttendStatus.PRESENT
        ExamMark examMark
        Student student
        Double stdMarkObtain = 0

        if (examType == ExamType.CLASS_TEST) {
            for (stds in studentList) {
                student = Student.read(stds.id)
                if (subject.name == "EAS" && student.easTotalMark > 0) {
                    stdMarkObtain = markObtain - student.easTotalMark
                } else {
                    stdMarkObtain = markObtain
                }
                examMark = ExamMark.findByExamScheduleAndStudentAndActiveStatus(examSchedule, student, ActiveStatus.ACTIVE)
                if (examMark) {
                    examMark.ctObtainMark = stdMarkObtain
                    examMark.ctAttendStatus = attendStatus
                    examMark.updatedBy = createOrUpdateBy
                } else {
                    examMark = new ExamMark(
                            ctAttendStatus: attendStatus,
                            ctObtainMark: stdMarkObtain,
                            student: student, exam: exam,
                            examSchedule: examSchedule,
                            createdBy: createOrUpdateBy,
                            academicYear: exam.academicYear,
                            subject: subject)
                }
                examMark.save(flush: true)
            }
        } else {
            for (stds in studentList) {
                student = Student.read(stds.id)
                if (subject.name == "EAS" && student.easTotalMark > 0) {
                    stdMarkObtain = markObtain - student.easTotalMark
                } else {
                    stdMarkObtain = markObtain
                }
                examMark = ExamMark.findByExamScheduleAndStudentAndActiveStatus(examSchedule, student, ActiveStatus.ACTIVE)
                if (examMark) {
                    examMark.hallObtainMark = stdMarkObtain
                    examMark.hallAttendStatus = attendStatus
                    examMark.updatedBy = createOrUpdateBy
                } else {
                    examMark = new ExamMark(
                            hallAttendStatus: attendStatus,
                            hallObtainMark: stdMarkObtain, student: student,
                            exam: exam,
                            examSchedule: examSchedule,
                            subject: subject,
                            createdBy: createOrUpdateBy,
                            academicYear: exam.academicYear
                    )
                }
                examMark.save(flush: true)
            }
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Exam Mark inserted successfully for all students")
        return result
    }

    def deleteHallMark(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if (!examSchedule) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Exam Schedule Not found')
            return result
        }
        Exam exam = examSchedule.exam
        if (exam.examStatus == ExamStatus.PUBLISHED) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Result Already Published')
            return result
        }

        SubjectName subject = examSchedule.subject
        def markEntryStdIds = examMarkService.markEntryStdIds(examSchedule, ExamType.HALL_TEST)
        if (!markEntryStdIds) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'No mark input')
            return result
        }
        def principal = springSecurityService.principal
        if (!SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You are not authenticated to delete all mark entry. Only ADMIN or SHIFT_INCHARGE can delete this. Please contact with admin.")
            return result
        }

        def allExamMarks = examMarkService.getAllExamMark(examSchedule, subject)
        for (examMark in allExamMarks) {
            examMark.hallAttendStatus = AttendStatus.NO_INPUT
            examMark.save(flush: true)
        }

        examSchedule.isHallMarkInput = false
        examSchedule.updatedBy = createOrUpdateBy
        examSchedule.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Deleted all exam mark input Successfully')
        return result
    }

    def deleteCtMark(Long id, GrailsParameterMap parameterMap){
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        ExamSchedule examSchedule = ExamSchedule.get(id)
        if(!examSchedule){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Exam Schedule Not found')
            return result
        }

        Exam exam = examSchedule.exam
        if(exam.examStatus==ExamStatus.PUBLISHED){
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Result Already Published')
            return result
        }
        if (exam.isLocked == 1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Mark Entry locked. Please contact admin for allow mark entry")
            return result
        }

        SubjectName subject = examSchedule.subject

        def principal = springSecurityService.principal
        if (!SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You are not authenticated to delete mark entry. Only ADMIN can delete this. Please contact with admin.")
            return result
        }
        String ctMarkFor = parameterMap.ctMarkFor
        def allExamMarks = examMarkService.getAllCtExamMark(examSchedule, subject, ctMarkFor)

        if (ctMarkFor == "CLASS_TEST1") {
            for (ExamMark examMark in allExamMarks) {
                examMark.ctAttendStatus = AttendStatus.NO_INPUT
                examMark.ctObtainMark = 0
                examMark.updatedBy = createOrUpdateBy
                examMark.save(flush: true)
            }
        } else if (ctMarkFor == "CLASS_TEST2") {
            for (ExamMark examMark in allExamMarks) {
                examMark.ct2Status = AttendStatus.NO_INPUT
                examMark.ct2ObtainMark = 0
                examMark.updatedBy = createOrUpdateBy
                examMark.save(flush: true)
            }
        } else if (ctMarkFor == "CLASS_TEST3") {
            for (ExamMark examMark in allExamMarks) {
                examMark.ct3Status = AttendStatus.NO_INPUT
                examMark.ct3ObtainMark = 0
                examMark.updatedBy = createOrUpdateBy
                examMark.save(flush: true)
            }
        }
        examSchedule.isHallMarkInput = false
        examSchedule.updatedBy = createOrUpdateBy
        examSchedule.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Deleted all CT exam mark input Successfully')
        return result
    }

    def saveCtMark(CtExamMarkCommand markCommand){
        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username

        ExamSchedule examSchedule = markCommand.examScheduleId
        ExamMark examMark
        Double ctObtainMark = 0
        AttendStatus attendStatus

        Exam exam = examSchedule.exam
        if (exam.isLocked == 1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Mark Entry locked. Please contact admin for allow mark entry")
            return result
        }
        SubjectName subject = examSchedule.subject

        String message
        if(markCommand.id){
            examMark = ExamMark.get(markCommand.id)
            if (!examMark) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
                return result
            }
            attendStatus = markCommand.attendStatus
            if(attendStatus == AttendStatus.PRESENT){
                ctObtainMark =markCommand.markObtain
            }
            if (markCommand.ctMarkFor == "CLASS_TEST1") {
                examMark.ctAttendStatus = attendStatus
                examMark.ctObtainMark = ctObtainMark
            } else if (markCommand.ctMarkFor == "CLASS_TEST2") {
                examMark.ct2Status = attendStatus
                examMark.ct2ObtainMark = ctObtainMark
            } else if (markCommand.ctMarkFor == "CLASS_TEST3") {
                examMark.ct3Status = attendStatus
                examMark.ct3ObtainMark = ctObtainMark
            }
            examMark.updatedBy = createOrUpdateBy
            message = "Mark Successfully Updated."
        }else {
            Student student = markCommand.studentId
            if (!student) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "No student found to add mark")
                return result
            }

            attendStatus = markCommand.attendStatus
            if (attendStatus == AttendStatus.PRESENT) {
                ctObtainMark = markCommand.markObtain
            }
            examMark = ExamMark.findByExamScheduleAndStudentAndActiveStatus(examSchedule, student, ActiveStatus.ACTIVE)
            if (examMark) {
                if (markCommand.ctMarkFor == "CLASS_TEST1") {
                    examMark.ctAttendStatus = attendStatus
                    examMark.ctObtainMark = ctObtainMark
                } else if (markCommand.ctMarkFor == "CLASS_TEST2") {
                    examMark.ct2Status = attendStatus
                    examMark.ct2ObtainMark = ctObtainMark
                } else if (markCommand.ctMarkFor == "CLASS_TEST3") {
                    examMark.ct3Status = attendStatus
                    examMark.ct3ObtainMark = ctObtainMark
                }
                examMark.updatedBy = createOrUpdateBy
            } else {
                if (markCommand.ctMarkFor == "CLASS_TEST1") {
                    examMark = new ExamMark(ctAttendStatus: attendStatus,
                            ctObtainMark: ctObtainMark,
                            student: student, exam: exam,
                            examSchedule: examSchedule,
                            subject: subject,
                            createdBy: createOrUpdateBy,
                            academicYear: exam.academicYear
                    )
                } else if (markCommand.ctMarkFor == "CLASS_TEST2") {
                    examMark = new ExamMark(ct2Status: attendStatus,
                            ct2ObtainMark: ctObtainMark,
                            student: student, exam: exam,
                            examSchedule: examSchedule,
                            subject: subject,
                            createdBy: createOrUpdateBy,
                            academicYear: exam.academicYear
                    )
                } else if (markCommand.ctMarkFor == "CLASS_TEST3") {
                    examMark = new ExamMark(ct3Status: attendStatus,
                            ct3ObtainMark: ctObtainMark,
                            student: student, exam: exam,
                            examSchedule: examSchedule,
                            subject: subject,
                            createdBy: createOrUpdateBy,
                            academicYear: exam.academicYear
                    )
                }
            }
            message = "Mark Successfully Added."
        }
        examMark.save()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id, String examType, String ctMarkFor) {
        LinkedHashMap result = new LinkedHashMap()
        ExamMark examMark = ExamMark.get(id)
        if (!examMark) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        Exam exam = examMark.exam
        if (exam.isLocked == 1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Mark Entry locked. Please contact admin for allow mark entry")
            return result
        }

        def principal = springSecurityService.principal
        if (!(SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN, ROLE_ADMIN,ROLE_SHIFT_INCHARGE") || principal.username == examMark.createdBy || principal.username == examMark.updatedBy)) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You are not authenticated to edit this mark entry. Either ADMIN or ${examMark.createdBy} or ${examMark.updatedBy} can edit this. Please contact with admin.")
            return result
        }

        if (ctMarkFor == "CLASS_TEST2") {
            examMark.ct2Status = AttendStatus.NO_INPUT
            examMark.ct2ObtainMark = 0
        } else if (ctMarkFor == "CLASS_TEST3") {
            examMark.ct3ObtainMark = 0
            examMark.ct3Status = AttendStatus.NO_INPUT
        } else if (ctMarkFor == "CLASS_TEST1"){
            examMark.ctObtainMark = 0
            examMark.ctAttendStatus = AttendStatus.NO_INPUT
        } else {
            examMark.hallAttendStatus = AttendStatus.NO_INPUT
            examMark.hallObtainMark = 0
        }

        examMark.updatedBy = principal.username
        examMark.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Mark Entry deleted successfully.")
        return result
    }

    def saveHallMark(HallExamMarkCommand markCommand){
        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username

        ExamSchedule examSchedule = markCommand.examScheduleId
        ExamMark examMark
        Double hallObtainMark = 0
        Double hallWrittenMark = 0
        Double hallPracticalMark = 0
        Double hallObjectiveMark = 0
        Double hallSbaMark = 0
        Double hallInput5 = 0
        AttendStatus attendStatus

        Exam exam = examSchedule.exam
        if (exam.isLocked == 1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Mark Entry locked. Please contact admin for allow mark entry")
            return result
        }
        SubjectName subject = examSchedule.subject

        String message
        if(markCommand.id){
            examMark = ExamMark.get(markCommand.id)
            if(!examMark){
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
                return result
            }

            attendStatus = markCommand.attendStatus
            if (attendStatus == AttendStatus.PRESENT) {
                hallObtainMark = markCommand.markObtain
                if (examSchedule.isHallPractical) {
                    hallWrittenMark = markCommand.hallWrittenMark ?:0
                    hallPracticalMark = markCommand.hallPracticalMark ?:0
                    hallObjectiveMark = markCommand.hallObjectiveMark ?:0
                    hallSbaMark = markCommand.hallSbaMark ?:0
                    hallInput5 = markCommand.hallInput5 ?:0
                }
            }

            examMark.hallAttendStatus = attendStatus
            examMark.hallObtainMark = hallObtainMark
            examMark.hallWrittenMark = hallWrittenMark
            examMark.hallPracticalMark = hallPracticalMark
            examMark.hallObjectiveMark = hallObjectiveMark
            examMark.hallSbaMark = hallSbaMark
            examMark.hallInput5 = hallInput5
            examMark.updatedBy = createOrUpdateBy
            message = "Mark Successfully Updated."
        } else {
            Student student = markCommand.studentId
            if (!student) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "No student found to add mark")
                return result
            }

            attendStatus = markCommand.attendStatus
            if (attendStatus == AttendStatus.PRESENT) {
                hallObtainMark = markCommand.markObtain
                if (examSchedule.isHallPractical) {
                    hallWrittenMark = markCommand.hallWrittenMark ?:0
                    hallPracticalMark = markCommand.hallPracticalMark ?:0
                    hallObjectiveMark = markCommand.hallObjectiveMark ?:0
                    hallSbaMark = markCommand.hallSbaMark ?:0
                    hallInput5 = markCommand.hallInput5 ?:0
                }
            }

            examMark = ExamMark.findByExamScheduleAndStudentAndActiveStatus(examSchedule, student, ActiveStatus.ACTIVE)
            if (examMark) {
                examMark.hallObtainMark = hallObtainMark
                examMark.hallAttendStatus = attendStatus
                examMark.hallWrittenMark = hallWrittenMark
                examMark.hallPracticalMark = hallPracticalMark
                examMark.hallObjectiveMark = hallObjectiveMark
                examMark.hallSbaMark = hallSbaMark
                examMark.hallInput5 = hallInput5
                examMark.updatedBy = createOrUpdateBy
            } else {
                examMark = new ExamMark(hallInput5: hallInput5,
                        hallSbaMark: hallSbaMark, hallObjectiveMark: hallObjectiveMark,
                        hallPracticalMark: hallPracticalMark, hallWrittenMark: hallWrittenMark,
                        hallAttendStatus: attendStatus, hallObtainMark: hallObtainMark,
                        student: student,
                        exam: exam,
                        examSchedule: examSchedule,
                        subject: subject,
                        createdBy: createOrUpdateBy,
                        academicYear: exam.academicYear
                )
            }
            message = "Mark Successfully Added."
        }

        examMark.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}