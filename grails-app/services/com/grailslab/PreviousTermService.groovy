package com.grailslab

import com.grailslab.enums.ExamTerm
import com.grailslab.enums.StudentStatus
import com.grailslab.exam.PreviousTermCommand
import com.grailslab.exam.PreviousTermCtCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.Exam
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName
import com.grailslab.stmgmt.AttnStudentSummery
import com.grailslab.stmgmt.PreviousTermMark
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class PreviousTermService {

    def messageSource
    def studentSubjectsService
    def springSecurityService

    static final String[] termMarkSortColumns = ['std.rollNo','std.name','std.studentID']

    def attendanceEntryStdIds(Section section, ExamTerm examTerm, AcademicYear academicYear = null){
        def c = AttnStudentSummery.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                eq("std.studentStatus", StudentStatus.NEW)
                eq("std.section", section)
                if (examTerm == ExamTerm.FINAL_TEST) {
                    isNotNull('attendDay')
                } else if (examTerm == ExamTerm.SECOND_TERM) {
                    isNotNull('term2attenDay')
                } else {
                    isNotNull('term1attenDay')
                }

            }
            projections {
                property('std.id')
            }
        }
        return results
    }
    def getPreviousTermList(GrailsParameterMap params, Section section, ExamTerm examTerm) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(termMarkSortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        int totalCount =0
        def c = AttnStudentSummery.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                eq("std.studentStatus", StudentStatus.NEW)
                eq("std.section", section)
                if (examTerm == ExamTerm.FINAL_TEST) {
                    isNotNull('attendDay')
                } else if (examTerm == ExamTerm.SECOND_TERM) {
                    isNotNull('term2attenDay')
                } else {
                    isNotNull('term1attenDay')
                }
            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }

            order(sortColumn, sSortDir)
        }
        Integer workingDay = section?.className?.workingDays
        Long attendPercentage
        totalCount = results.totalCount
        Student student
        Integer term1Atten
        Integer term2Atten
        Integer finalAtten
        Integer totalAtten
        if (totalCount > 0) {
            results.each { AttnStudentSummery termMark ->
                student = termMark.student
                term1Atten = termMark.term1attenDay ? termMark.term1attenDay : 0
                term2Atten = termMark.term2attenDay ? termMark.term2attenDay : 0
                finalAtten = termMark.attendDay ? termMark.attendDay : 0
                totalAtten = term1Atten + term2Atten + finalAtten

                dataReturns.add([DT_RowId: termMark.id, 0:student.studentID,1: student.name, 2:student.rollNo, 3: term1Atten,  4: term2Atten, 5: finalAtten, 6: totalAtten, 7:''])
            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def ctEntryStdIds(Section section, SubjectName subjectName, def subjectStdIds, AcademicYear academicYear = null){
        def c = PreviousTermMark.createCriteria()
        def results = c.list() {
            createAlias('student', 'std')
            and {
                'in'("std.id", subjectStdIds)
                eq("subjectName", subjectName)
                isNotNull('ctMark')
            }
            projections {
                property('std.id')
            }
        }
        return results
    }

    def getPreviousTermCtList(GrailsParameterMap params, Section section, SubjectName subjectName, List subjectStdIds) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(termMarkSortColumns, iSortingCol)

        //get Subject Studentids List

        List dataReturns = new ArrayList()
        int totalCount =0
        def c = PreviousTermMark.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            and {
                'in'("std.id", subjectStdIds)
                eq("subjectName", subjectName)
                isNotNull('ctMark')

            }
            if (sSearch) {
                or {
                    ilike('std.name', sSearch)
                    ilike('std.studentID', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        Integer workingDay = section?.className?.workingDays
        Long attendPercentage
        totalCount = results.totalCount
        Student student
        if (totalCount > 0) {
            results.each { PreviousTermMark termMark ->
                student = termMark.student
                if(termMark.ctMark && workingDay){
                    attendPercentage = Math.round(termMark.ctMark * 100 /workingDay)
                }else {
                    attendPercentage = 0
                }
                dataReturns.add([DT_RowId: termMark.id, 0:student.studentID,1: student.name, 2:student.rollNo, 3:termMark.ctMark, 4:''])

            }
        }
        return [totalCount:totalCount,results:dataReturns]
    }

    //get total CT mark for final exam result
    def totalCtMark(Student student, AcademicYear academicYear = null){
        def subjectIds = studentSubjectsService.studentSubjectIds(student)
        def c = PreviousTermMark.createCriteria()
        def results = c.list() {
            createAlias('subjectName', 'sn')
            and {
                'in'("sn.id", subjectIds)
                eq("student", student)
                isNotNull('ctMark')
            }
            projections {
                sum('ctMark')
            }
        }
        return results ? results[0] : 0
    }

    def saveOrUpdate(PreviousTermCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        AttnStudentSummery attnStudentSummery
        String message

        Exam exam = Exam.read(command.examId)

        if (command.id) {
            attnStudentSummery = AttnStudentSummery.get(command.id)
            if (!attnStudentSummery) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            if (exam.examTerm == ExamTerm.FINAL_TEST) {
                attnStudentSummery.attendDay = command.attendDay
            } else if (exam.examTerm == ExamTerm.SECOND_TERM) {
                attnStudentSummery.term2attenDay = command.attendDay
            } else {
                attnStudentSummery.term1attenDay = command.attendDay
            }
            attnStudentSummery.updatedBy = createOrUpdateBy
            message = "Total Attendance Updated Successfully"

        } else {
            attnStudentSummery = AttnStudentSummery.findByStudent(command.student)
            if (!attnStudentSummery) {
                attnStudentSummery = new AttnStudentSummery(student: command.student)
            }

            if (exam.examTerm == ExamTerm.FINAL_TEST) {
                attnStudentSummery.attendDay = command.attendDay
            } else if (exam.examTerm == ExamTerm.SECOND_TERM) {
                attnStudentSummery.term2attenDay = command.attendDay
            } else {
                attnStudentSummery.term1attenDay = command.attendDay
            }
            attnStudentSummery.createdBy = createOrUpdateBy
            attnStudentSummery.academicYear = academicYear
            message = "Total Attendance Added Successfully"

        }
        attnStudentSummery.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def saveOrUpdateCt(PreviousTermCtCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear
        PreviousTermMark previousTermMark
        String message
        if (command.id) {
            previousTermMark = PreviousTermMark.get(command.id)
            if (!previousTermMark) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            previousTermMark.ctMark = command.ctMark
            previousTermMark.updatedBy = createOrUpdateBy
            message = "Ct Mark Updated Successfully"

        } else {
            previousTermMark = PreviousTermMark.findByStudentAndSubjectName(command.student, command.subjectName)
            if (!previousTermMark) {
                previousTermMark = new PreviousTermMark(student: command.student, subjectName: command.subjectName)
            }
            previousTermMark.ctMark = command.ctMark
            previousTermMark.createdBy = createOrUpdateBy
            previousTermMark.academicYear = academicYear
            message = "Total Ct Mark Added Successfully"
        }
        previousTermMark.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

}
