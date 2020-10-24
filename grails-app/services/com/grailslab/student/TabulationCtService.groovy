package com.grailslab.student

import com.grailslab.CommonUtils
import com.grailslab.enums.ExamStatus
import com.grailslab.enums.ExamTerm
import com.grailslab.enums.GroupName
import com.grailslab.enums.LetterGrade
import com.grailslab.enums.ResultStatus
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName
import com.grailslab.stmgmt.ExamMark
import com.grailslab.stmgmt.Student
import com.grailslab.stmgmt.Tabulation
import com.grailslab.stmgmt.TabulationCt
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.apache.commons.lang3.ArrayUtils

@Transactional
class TabulationCtService {
    def studentService
    def examMarkService
    def studentSubjectsService
    def examScheduleService
    def springSecurityService

    TabulationCt getTabulation(Exam exam, Student student, Integer ctExamNo){
        return TabulationCt.findByExamAndStudentAndCtExamNo(exam, student, ctExamNo)
    }
    TabulationCt entrySubjectMark(ClassName className, Section section, Exam exam, Student student, String fieldName, Double fieldMark, Integer ctExamNo){
        TabulationCt tabulation = new TabulationCt(className:className, section:section, exam:exam, examTerm: exam.examTerm, student:student, academicYear:exam.academicYear, ctExamNo: ctExamNo)
        tabulation."${fieldName}"=fieldMark.round(2)
        tabulation.save(flush: true)
    }


    void processCtTabulation(Exam exam, Integer ctExamNo){
        def results = TabulationCt.findAllByExamAndCtExamNo(exam, ctExamNo)
        def subjectIdList
        int failedSubCounter = 0
        Double totalObtainMark
        for (tabulation in results) {
            subjectIdList = studentSubjectsService.studentSubjectIds(tabulation.student)
            if (subjectIdList) {
                if (ctExamNo == 1) {
                    totalObtainMark = examMarkService.getCtExamMark(exam, tabulation.student, subjectIdList)
                    failedSubCounter = examMarkService.getCtFailCount(exam, tabulation.student, subjectIdList)
                } else if (ctExamNo == 2) {
                    totalObtainMark = examMarkService.getCt2ExamMark(exam, tabulation.student, subjectIdList)
                    failedSubCounter = examMarkService.getCt2FailCount(exam, tabulation.student, subjectIdList)
                } else if (ctExamNo == 3) {
                    totalObtainMark = examMarkService.getCt3ExamMark(exam, tabulation.student, subjectIdList)
                    failedSubCounter = examMarkService.getCt3FailCount(exam, tabulation.student, subjectIdList)
                }
                tabulation.totalObtainMark = totalObtainMark
                if (failedSubCounter > 0) {
                    tabulation.resultStatus = ResultStatus.FAILED
                } else {
                    tabulation.resultStatus = ResultStatus.PASSED
                }
                tabulation.failedSubCounter = failedSubCounter
                tabulation.save(flush: true)
            }
        }
    }

    def createSectionResult(Exam exam, int numOfCt){
        def c = TabulationCt.createCriteria()
        def results = c.list() {
            and {
                eq("exam", exam)
                eq("ctExamNo", numOfCt)
            }
            order("failedSubCounter",CommonUtils.SORT_ORDER_ASC)
            order("totalObtainMark",CommonUtils.SORT_ORDER_DESC)
            order("subject0Mark",CommonUtils.SORT_ORDER_DESC)
            order("subject2Mark",CommonUtils.SORT_ORDER_DESC)
            order("subject3Mark",CommonUtils.SORT_ORDER_DESC)
        }
        int position =1
        results.each {tabulation ->
            tabulation.positionInSection = position
            tabulation.sectionStrPosition = CommonUtils.ordinal(position)
            position++
            tabulation.save(flush: true)
        }
        return true
    }


    def createClassResult(ClassName className, List examIds, int numOfCt){
        def c = TabulationCt.createCriteria()
        def results = c.list() {
            createAlias("exam","eXm")
            and {
                eq("className", className)
                'in'('eXm.id',examIds)
                eq("ctExamNo", numOfCt)
            }
            order("failedSubCounter",CommonUtils.SORT_ORDER_ASC)
            order("totalObtainMark",CommonUtils.SORT_ORDER_DESC)
            order("positionInSection",CommonUtils.SORT_ORDER_ASC)
        }
        int position =1
        results.each {tabulation ->
            tabulation.positionInClass = position
            tabulation.classStrPosition = CommonUtils.ordinal(position)
            position++
            tabulation.save(flush: true)
        }
        return true
    }

    def listStudentBasedClsPosition(ClassName className, List examIds, GroupName groupName, int offset, int limit){
        def c = TabulationCt.createCriteria()
        def results = c.list(max: limit, offset: offset) {
            createAlias("exam","eXm")
            createAlias("section","sec")
            and {
                eq("className", className)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                'in'('eXm.id',examIds)
            }
            order("positionInClass",CommonUtils.SORT_ORDER_ASC)
            projections {
                property('student')
            }
        }
        return results
    }

    void resetMasterCtResult(Exam exam, Integer ctExamNo) {
        TabulationCt.executeUpdate("delete TabulationCt where exam = :exam and ctExamNo = :ctExamNo",[exam: exam, ctExamNo: ctExamNo])
        if (ctExamNo == 3) {
            exam.ct3ExamStatus = ExamStatus.NEW
        } else if (ctExamNo == 2) {
            exam.ct2ExamStatus = ExamStatus.NEW
        } else {
            exam.ctExamStatus = ExamStatus.NEW
        }
        exam.updatedBy = springSecurityService.principal?.username
        exam.save(flush: true)
    }

    def createCtTabulation(Long id, String ctExamNo) {
        LinkedHashMap result = new LinkedHashMap()
        Exam exam = Exam.get(id)
        if (!exam) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
            return result
        }
        if (ctExamNo == "CLASS_TEST3") {
            if (exam.ct3ExamStatus == ExamStatus.PUBLISHED || exam.ct3ExamStatus == ExamStatus.RESULT) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Result already prepared or published')
                return result
            }
            if (exam.ct3ExamStatus == ExamStatus.TABULATION) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Tabulation already Created.')
                return result
            }
            processCtTabulation(exam, 3)
            exam.ct3ExamStatus = ExamStatus.TABULATION
        } else if (ctExamNo == "CLASS_TEST2") {
            if (exam.ct2ExamStatus == ExamStatus.PUBLISHED || exam.ct2ExamStatus == ExamStatus.RESULT) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Result already prepared or published')
                return result
            }

            if (exam.ct2ExamStatus == ExamStatus.TABULATION) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Tabulation already Created.')
                return result
            }
            processCtTabulation(exam, 2)
            exam.ct2ExamStatus = ExamStatus.TABULATION
        } else {
            if (exam.ctExamStatus == ExamStatus.PUBLISHED || exam.ctExamStatus == ExamStatus.RESULT) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Result already prepared or published')
                return result
            }
            if (exam.ctExamStatus == ExamStatus.TABULATION) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, 'Tabulation already Created.')
                return result
            }
            processCtTabulation(exam, 1)
            exam.ctExamStatus = ExamStatus.TABULATION
        }
        exam.updatedBy = springSecurityService.principal?.username
        exam.save(flush: true)
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE, 'Ct tabulation created successfully.')
        return result
    }
    String getCtTabulationQueryStr(Long examId, int ctExamNo, String orderByStr) {
        String querySql = """
        select std.name, std.studentid, std.roll_no as rollNo,
            tabu.subject0mark, tabu.subject1mark, tabu.subject2mark, tabu.subject3mark, tabu.subject4mark, tabu.subject5mark, tabu.subject6mark, tabu.subject7mark, 
            tabu.subject8mark, tabu.subject9mark, tabu.subject10mark, tabu.subject11mark, tabu.subject12mark, tabu.subject13mark, tabu.subject14mark,
            tabu.total_obtain_mark as finalToal, tabu.section_str_position, tabu.position_in_class, tabu.failed_sub_counter, tabu.result_status,tabu.g_point,tabu.l_grade
              from tabulation_ct tabu
            inner join student std on std.id=tabu.student_id and std.student_status="NEW"
             where tabu.exam_id= ${examId} AND tabu.ct_exam_no = ${ctExamNo}  order by ${orderByStr};
        """
        return querySql
    }
}
