package com.grailslab


import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam
import grails.gorm.transactions.Transactional

@Transactional
class ExamService {

    def springSecurityService
    def examService
    def classNameService
    def messageSource
    def sectionService
    def shiftExamService
    def uploadService
    def schoolService
    def examScheduleService
    def sessionFactory

    def examListForPreparingResult(ShiftExam shiftExam, ClassName className, GroupName groupName){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    Boolean isExamSchedulePublished(ShiftExam shiftExam, Long classNameId){
        def c = Exam.createCriteria()
        def count = c.list() {
            createAlias('className','cls')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("cls.id", classNameId)
                eq("shiftExam", shiftExam)
                eq("hallSchedule", ScheduleStatus.PUBLISHED)
            }
            projections {
                count()
            }
        }
        if (count[0] > 0) {
            return true
        }
        return false
    }


    def sectionExamList(Section section){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("section", section)
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def classExamIdList(ShiftExam shiftExam, ClassName className, GroupName groupName){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
            }
            projections {
                property('id')
            }
        }
        return results
    }

    List<Exam> classExamList(ShiftExam shiftExam, ClassName className, GroupName groupName){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    List<Exam> shiftExamList(ShiftExam shiftExam){
        def c = Exam.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("shiftExam", shiftExam)
                eq("examStatus", ExamStatus.NEW)
            }
        }
        return results
    }

    List<Exam> examsNotInSectionIdList(ShiftExam shiftExam, List sectionIds){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("shiftExam", shiftExam)
                not{'in'("sec.id", sectionIds)}
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
    def ctExamsForManageTabulation(ShiftExam shiftExam, ClassName className, String ctExamNo){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (ctExamNo == "CLASS_TEST3") {
                    'in'("ct3ExamStatus", ExamStatus.resultWorkingList())
                } else if (ctExamNo == "CLASS_TEST2") {
                    'in'("ct2ExamStatus", ExamStatus.resultWorkingList())
                } else {
                    'in'("examStatus", ExamStatus.resultWorkingList())
                }
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        List examList = new ArrayList()
        String examStatusStr
        for (Exam exam : results) {
            if (ctExamNo == "CLASS_TEST3") {
                examStatusStr = exam.ct3ExamStatus? exam.ct3ExamStatus.value : "New"
            } else if (ctExamNo == "CLASS_TEST2") {
                examStatusStr = exam.ct2ExamStatus? exam.ct2ExamStatus.value : "New"
            } else {
                examStatusStr = exam.ctExamStatus? exam.ctExamStatus.value : "New"
            }
            examList.add([id: exam.id, className: exam.className.name, sectionName: exam.section.name, examStatusStr: examStatusStr])
        }
        return examList
    }

    def examsForManageTabulation(ShiftExam shiftExam, ClassName className, ExamType examType){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                'in'("examStatus", ExamStatus.resultWorkingList())
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }

        List examList = new ArrayList()
        for (Exam exam : results) {
            examList.add([id: exam.id, className: exam.className.name, sectionName: exam.section.name,
                          examStatus: exam.examStatus?.value])
        }
        return examList
    }

    def examsForSmsResult(ShiftExam shiftExam, ClassName className){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("shiftExam", shiftExam)
                if (className){
                    eq("className", className)
                }
                eq("examStatus", ExamStatus.RESULT)
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def classExamsDropDownAsSectionName(ShiftExam shiftExam, ClassName className, GroupName groupName, String examType = 'new'){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                if (examType == 'new') {
                    eq("examStatus", ExamStatus.NEW)
                } else if(examType == 'working') {
                    'in'("examStatus", ExamStatus.resultWorkingList())
                } else if (examType == 'publishing'){
                    'in'("examStatus", ExamStatus.resultPublishingList())
                } else if (examType == 'published') {
                    eq("examStatus", ExamStatus.PUBLISHED)
                }
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        for (exam in results) {
            dataReturns.add([id: exam.id, name: exam.section.name])
        }
        return dataReturns
    }
    def classExamsDropDownAsSectionNameForMarkEntry(ShiftExam shiftExam, ClassName className, GroupName groupName, String examType = 'new'){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('section','sec')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("className", className)
                eq("shiftExam", shiftExam)
                eq("isLocked", 0)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                if (examType == 'new') {
                    eq("examStatus", ExamStatus.NEW)
                } else if(examType == 'working') {
                    'in'("examStatus", ExamStatus.resultWorkingList())
                } else if (examType == 'publishing'){
                    'in'("examStatus", ExamStatus.resultPublishingList())
                } else if (examType == 'published') {
                    eq("examStatus", ExamStatus.PUBLISHED)
                }
            }
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        for (exam in results) {
            dataReturns.add([id: exam.id, name: exam.section.name])
        }
        return dataReturns
    }


    Exam getExam(ShiftExam shiftExam, Section section){
        return Exam.findByShiftExamAndSectionAndActiveStatus(shiftExam,section,ActiveStatus.ACTIVE)
    }

   List<Exam> classExamListByTerm(ClassName className, AcademicYear academicYear, ExamTerm examTerm) {
       def c = Exam.createCriteria()
       def results = c.list() {
           createAlias('shiftExam','se')
           and {
               eq("examTerm", examTerm)
               eq("se.activeStatus", ActiveStatus.ACTIVE)
               eq("activeStatus", ActiveStatus.ACTIVE)
               eq("academicYear", academicYear)
               eq("className", className)
           }
       }
       return results
   }

    int numberOfTermExam(Section section) {
        Exam.countByExamTermInListAndSectionAndActiveStatusAndHallScheduleNotEqual(ExamTerm.termExamList(), section, ActiveStatus.ACTIVE, ScheduleStatus.NO_SCHEDULE)
    }

    //exam ids for result analytics
    def publishingExamList(Shift shift, ShiftExam shiftExam, ExamType examType, ClassName className, GroupName groupName){
        def c = Exam.createCriteria()
        def results = c.list() {
            createAlias('className','cls')
            createAlias('section','sec')
            and {
                eq("shiftExam", shiftExam)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (className) {
                    eq("className", className)
                }
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                if (shift) {
                    eq("sec.shift", shift)
                }
                if(examType && examType == ExamType.CLASS_TEST) {
                    'in'("ctExamStatus", ExamStatus.resultWorkingList())
                } else {
                    'in'("examStatus", ExamStatus.resultWorkingList())
                }
            }
            order("cls.sortPosition", CommonUtils.SORT_ORDER_ASC)
            order('sec.sortOrder', CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }
    def inactive(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        Exam exam = Exam.get(id)
        if (!exam) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (exam.examStatus != ExamStatus.NEW) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Can't delete. Result Already Processing or Published.")
            return result
        }
        if (exam.examStatus.equals(ExamStatus.NEW)) {
            exam.activeStatus = ActiveStatus.INACTIVE
            exam.updatedBy = springSecurityService.principal?.username
            exam.save(flush: true)

            result.put(CommonUtils.IS_ERROR, false)
            result.put(CommonUtils.MESSAGE, "Exam Deleted successfully.")
            return result
        }
        result.put(CommonUtils.IS_ERROR, true)
        result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
        return result
    }
    Long getCtExamId(Long shiftExamId, Long sectionId){
        final session = sessionFactory.currentSession
        final String query = "select id from exam where shift_exam_id =:shiftExamId and section_id =:sectionId and ct_exam_status in ('RESULT') and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('shiftExamId', shiftExamId)
                .setParameter('sectionId', sectionId).list()
        return queryResults ? queryResults[0] : null
    }
    Long getHallExamId(Long shiftExamId, Long sectionId){
        final session = sessionFactory.currentSession
        final String query = "select id from exam where shift_exam_id =:shiftExamId and section_id =:sectionId and exam_status in ('RESULT') and active_status = 'ACTIVE';"

        final queryResults = session.createSQLQuery(query)
                .setParameter('shiftExamId', shiftExamId)
                .setParameter('sectionId', sectionId).list()
        return queryResults ? queryResults[0] : null
    }
}
