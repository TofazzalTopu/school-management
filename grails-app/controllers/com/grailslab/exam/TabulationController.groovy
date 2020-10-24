package com.grailslab.exam

import com.grailslab.CommonUtils
import com.grailslab.enums.ExamStatus
import com.grailslab.settings.*
import grails.converters.JSON

class TabulationController {
    def tabulationService
    def examService
    def studentService
    def schoolService
    def examScheduleService
    def tabulationCtService
    def examMarkService
    def springSecurityService

    def index() {}

    def createTabulation(Long id) {
        LinkedHashMap result = tabulationService.createTabulation(id)
        String output = result as JSON
        render output
    }

    def createCtTabulation(Long id){
        String ctExamNo = params.ctExamNo
        LinkedHashMap result = tabulationCtService.createCtTabulation(id, ctExamNo)
        String output = result as JSON
        render output
    }

    def markAllCtComplete(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Exam exam=Exam.get(id)
        if (!exam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        String ctExamNo = params.ctExamNo
        ShiftExam shiftExam = exam.shiftExam
        def examSchedules
        Section section = exam.section
        ClassName className = exam.className
        boolean entryCompleted = true
        if (ctExamNo == "CLASS_TEST3" && shiftExam.ctExam >= 3) {
            if(exam.ct3ExamStatus==ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'CT 3 Result already prepared or published')
                outPut=result as JSON
                render outPut
                return
            }

            if(exam.ct3ExamStatus == ExamStatus.TABULATION){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'CT 3 Tabulation already Created. Please reset if necessary.')
                outPut=result as JSON
                render outPut
                return
            }
            examSchedules = examScheduleService.ctSchedules(exam)
            if(!examSchedules){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'No Exam Schedule Added')
                outPut=result as JSON
                render outPut
                return
            }

            for (def examSchedule: examSchedules) {
                entryCompleted = examMarkService.markCt3Complete(exam, section, examSchedule, className)
                if (!entryCompleted) {
                    break
                }
            }
            if(!entryCompleted){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,"One or more subject mark can't processed for CT 3 Exam. Please ensure all student mark entry or contact to admin")
                outPut=result as JSON
                render outPut
                return
            }
            exam.ct3ExamStatus = ExamStatus.PROCESSED
        } else if (ctExamNo == "CLASS_TEST2" && shiftExam.ctExam >= 2) {
            if(exam.ct2ExamStatus == ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'Result already prepared or published for CT 2')
                outPut=result as JSON
                render outPut
                return
            }

            if(exam.ct2ExamStatus == ExamStatus.TABULATION){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'Tabulation already Created for CT 2.')
                outPut=result as JSON
                render outPut
                return
            }
            examSchedules = examScheduleService.ctSchedules(exam)
            if(!examSchedules){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'No Exam Schedule Added')
                outPut=result as JSON
                render outPut
                return
            }

            for (def examSchedule: examSchedules) {
                entryCompleted = examMarkService.markCt2Complete(exam, section, examSchedule, className)
                if (!entryCompleted) {
                    break
                }
            }
            if(!entryCompleted){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,"One or more subject mark can't processed. Please ensure all student mark entry or contact to admin")
                outPut=result as JSON
                render outPut
                return
            }
            exam.ct2ExamStatus = ExamStatus.PROCESSED
        } else {
            if(exam.ctExamStatus == ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'Result already prepared or published')
                outPut=result as JSON
                render outPut
                return
            }

            if(exam.ctExamStatus == ExamStatus.TABULATION){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'Tabulation already Created.')
                outPut=result as JSON
                render outPut
                return
            }
            examSchedules = examScheduleService.ctSchedules(exam)
            if(!examSchedules){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'No Exam Schedule Added')
                outPut=result as JSON
                render outPut
                return
            }

            for (def examSchedule: examSchedules) {
                entryCompleted = examMarkService.markCt1Complete(exam, section, examSchedule, className)
                if (!entryCompleted) {
                    break
                }
            }
            if(!entryCompleted){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,"One or more subject mark can't processed. Please ensure all student mark entry or contact to admin")
                outPut=result as JSON
                render outPut
                return
            }
            exam.ctExamStatus = ExamStatus.PROCESSED
        }
        exam.save(flush: true)
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE,'Ct exam mark processed successfully.')
        outPut=result as JSON
        render outPut
    }

    def markAllHallComplete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Exam exam=Exam.get(id)
        if (!exam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        if(exam.examStatus !=ExamStatus.NEW){
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Result Already created or Published')
            outPut = result as JSON
            render outPut
            return
        }

        List<ExamSchedule> examSchedules = examScheduleService.examSchedules(exam, Boolean.FALSE)
        if(!examSchedules){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,'No Exam Schedule Added')
            outPut=result as JSON
            render outPut
            return
        }
        ShiftExam shiftExam = exam.shiftExam
        ClassName className = exam.className
        Section section = exam.section
        String subjectIds = exam.subjectIds

        boolean entryCompleted = true
        for (ExamSchedule examSchedule: examSchedules) {
            entryCompleted = examMarkService.markHallComplete(shiftExam, exam, className, section, subjectIds, examSchedule)
            if (!entryCompleted) {
                break
            }
        }
        if(!entryCompleted){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,"One or more subject mark can't processed. Please ensure all student mark entry completed. If still problem persist, contact to admin")
            outPut=result as JSON
            render outPut
            return
        }
        exam.examStatus = ExamStatus.PROCESSED
        exam.updatedBy = springSecurityService.principal?.username
        exam.save(flush: true)
        result.put(CommonUtils.IS_ERROR,false)
        result.put(CommonUtils.MESSAGE,'Exam mark processed successfully.')
        outPut=result as JSON
        render outPut
    }

    def masterResetCtResult(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Exam exam=Exam.get(id)
        if (!exam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        ShiftExam shiftExam = exam.shiftExam
        String ctExamNo = params.ctExamNo

        if (ctExamNo == "CLASS_TEST3" && shiftExam.ctExam >= 3){
            if(exam.ct3ExamStatus == ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'CT 3 Result already published')
                outPut=result as JSON
                render outPut
                return
            }
            tabulationCtService.resetMasterCtResult(exam, 3)
        } else if (ctExamNo == "CLASS_TEST2" && shiftExam.ctExam >= 2){
            if(exam.ct2ExamStatus == ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'CT 2 Result already published')
                outPut=result as JSON
                render outPut
                return
            }
            tabulationCtService.resetMasterCtResult(exam, 2)
        } else {
            if(exam.ctExamStatus == ExamStatus.PUBLISHED){
                result.put(CommonUtils.IS_ERROR,true)
                result.put(CommonUtils.MESSAGE,'CT 2 Result already published')
                outPut=result as JSON
                render outPut
                return
            }
            tabulationCtService.resetMasterCtResult(exam, 1)
        }

        result.put(CommonUtils.IS_ERROR,false)
        result.put(CommonUtils.MESSAGE,'Master Reset success message.')
        outPut=result as JSON
        render outPut
    }

    def masterResetHallResult(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Exam exam=Exam.get(id)
        if (!exam){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_ERROR_MESSAGE)
            outPut=result as JSON
            render outPut
            return
        }
        if(exam.examStatus == ExamStatus.PUBLISHED || exam.examStatus==ExamStatus.PENDING){
            result.put(CommonUtils.IS_ERROR,true)
            result.put(CommonUtils.MESSAGE,'Result already published or pending processing')
            outPut=result as JSON
            render outPut
            return
        }
        tabulationService.resetMasterHallResult(exam)
        result.put(CommonUtils.IS_ERROR,false)
        result.put(CommonUtils.MESSAGE,'Master Reset success message.')
        outPut=result as JSON
        render outPut
    }

}