package com.grailslab

import com.grailslab.enums.AttendStatus
import com.grailslab.enums.ExamStatus
import com.grailslab.enums.ExamTerm
import com.grailslab.enums.ExamType
import com.grailslab.enums.GroupName
import com.grailslab.enums.LetterGrade
import com.grailslab.enums.ResultStatus
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam
import com.grailslab.settings.SubjectName
import com.grailslab.stmgmt.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class TabulationService {
    def springSecurityService
    def schoolService
    def gradePointService
    def examMarkService
    def examService
    def studentService
    def examScheduleService
    def studentSubjectsService
    def tabulationCtService
    def grailsApplication


    Double highestMark(List examIds, String fieldName) {
        Double highestMark = Tabulation.createCriteria().get {
            createAlias("exam","eXm")
            and {
                'in'('eXm.id',examIds)
            }
            projections {
                max fieldName
            }
        } as Double
    }

    Tabulation getTabulation(Exam exam, Student student){
       return Tabulation.findByExamAndStudent(exam, student)
    }
    TabulationDetails getTabulationDetails(Long tabulationId){
        return TabulationDetails.findByTabulationId(tabulationId)
    }
    TabulationAvgMark getTabulationAvgMark(Long tabulationId){
        return TabulationAvgMark.findByTabulationId(tabulationId)
    }

    TabulationDetails entryTermDetailMark(Tabulation tabulation, String fieldWritten, String fieldPractical, String fieldObjective, String fieldSba, String fieldInput5, String fieldgp, ExamMark examMark, Boolean isHallPractical){
        TabulationDetails details = new TabulationDetails(tabulationId: tabulation.id, examId: tabulation.examId)
        if (isHallPractical) {
            details."${fieldWritten}"=examMark.hallWrittenMark
            details."${fieldPractical}"=examMark.hallPracticalMark
            details."${fieldObjective}"=examMark.hallObjectiveMark
            details."${fieldSba}"=examMark.hallSbaMark
            details."${fieldInput5}"=examMark.hallInput5
        } else {
            details."${fieldWritten}"=examMark.hallObtainMark
        }
        details."${fieldgp}"=examMark.gPoint
        return details
    }
    TabulationDetails entryFinalDetailMark(Tabulation tabulation, String fieldWritten, String fieldPractical, String fieldObjective, String fieldSba, String fieldInput5, String fieldgp, ExamMark examMark, Boolean isHallPractical){
        TabulationDetails details = new TabulationDetails(tabulationId: tabulation.id, examId: tabulation.examId)
        if (isHallPractical) {
            details."${fieldWritten}"=examMark.hallWrittenMark
            details."${fieldPractical}"=examMark.hallPracticalMark
            details."${fieldObjective}"=examMark.hallObjectiveMark
            details."${fieldSba}"=examMark.hallSbaMark
            details."${fieldInput5}"=examMark.hallInput5
        } else {
            details."${fieldWritten}"=examMark.hallObtainMark
        }
        details."${fieldgp}"=examMark.gPoint
        return details
    }


    @Transactional
    Tabulation entryTermSubjectMark(ClassName className, Section section, Exam exam, Student student, String fieldName, Double fieldMark){
        Tabulation tabulation = new Tabulation(className:className, section:section, exam:exam, examTerm:exam.examTerm, student:student, academicYear:exam.academicYear)
        tabulation."${fieldName}"=fieldMark.round(2)
        tabulation.save(flush: true)
    }
    @Transactional
    Tabulation entryFinalSubjectMark(ClassName className, Section section, Exam exam, Student student, String fieldName, Double fieldMark){
        Tabulation tabulation = new Tabulation(className:className, section:section, exam:exam, examTerm:ExamTerm.FINAL_TEST, student:student, academicYear:exam.academicYear)
        tabulation."${fieldName}"=fieldMark.round(2)
        tabulation.save(flush: true)
    }
    Tabulation finalTabulation(ClassName className, Section section, Exam exam, Student student){
        return new Tabulation(className:className, section:section, exam:exam, examTerm:ExamTerm.FINAL_TEST, student:student, academicYear:exam.academicYear)
    }
    Tabulation termTabulation(ClassName className, Section section, Exam exam, ExamTerm examTerm, Student student){
        return new Tabulation(className:className, section:section, exam:exam, examTerm:examTerm, student:student, academicYear:exam.academicYear)
    }


    int getNumberOfPassStudentCount(List classExamList) {
        return Tabulation.countByExamInListAndResultStatus(classExamList, ResultStatus.PASSED)
    }
    int classPassStudentCount(ShiftExam shiftExam, ClassName className, GroupName groupName) {
        List<Exam> classExamList = examService.classExamList(shiftExam, className, groupName)
        if (!classExamList) return 0
        return Tabulation.countByExamInListAndResultStatus(classExamList, ResultStatus.PASSED)
    }

    @Transactional
    def createSectionTermTabulation(Section section, Exam exam){
        String runningSchool = schoolService.runningSchool()
        def results = examTabulationList(section, exam)

        ClassName className = section.className
        boolean subjectGroup = className.subjectGroup

        Double totalMark
        Double grandTotalMark
        List<ExamMark> studentMarkList
        Student student

        Double gPoint
        Integer numberOfSubject
        Integer failCount
        Double gPointAverage
        LetterGrade letterGrade
        Integer attendSubjectCount

        Integer attended = 0
        AttnStudentSummery attnStudentSummery
        Integer workingDay = className.workingDays?:0
        for (tabulation in results) {
            totalMark = 0
            grandTotalMark = 0

            student = tabulation.student

            gPoint = 0
            numberOfSubject =0
            failCount = 0
            gPointAverage = 0
            studentMarkList = examMarkService.getStudentResultMarks(exam, student)
            for (examMark in studentMarkList) {
                totalMark += examMark.tabulationMark ?: 0

                if (examMark.isExtraCurricular) {
                    if(examMark.gPoint >0){
                        gPoint+= examMark.gPoint
                        numberOfSubject+=1
                    }
                } else if (examMark.isOptional) {
                    if(examMark.resultStatus==ResultStatus.PASSED && examMark.gPoint >2){
                        gPoint+= examMark.gPoint-2
                    }
                } else {
                    if (subjectGroup) {
                        if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                            //do nothing
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            }else {
                                failCount += 1
                            }
                        }
                    } else {
                        if(examMark.resultStatus==ResultStatus.PASSED){
                            gPoint+= examMark.gPoint
                            numberOfSubject+=1
                        }else {
                            failCount += 1
                        }
                    }

                }
            }
            attendSubjectCount = examMarkService.getStudentExamAttendCount(exam, student)
            if (attendSubjectCount > 0){
                attended++
            }

            if(failCount > 0){
                tabulation.resultStatus = ResultStatus.FAILED
                tabulation.gPoint = 0
                tabulation.lGrade = LetterGrade.GRADE_F.value
                tabulation.failedSubCounter = failCount
            }else {
                gPointAverage = gPoint / numberOfSubject
                if(gPointAverage > 5){
                    gPointAverage = 5
                }
                letterGrade = gradePointService.getByPoint(gPointAverage)
                tabulation.lGrade = letterGrade.value
                tabulation.gPoint = gPointAverage.round(2)
                tabulation.resultStatus = ResultStatus.PASSED
                tabulation.failedSubCounter = failCount
            }
            if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL){
                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudentAndAcademicYear(student, exam.academicYear)
                    if(attnStudentSummery){
                        if(exam.examTerm == ExamTerm.SECOND_TERM) {
                            tabulation.attendanceDay = attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay)
                        } else {
                            tabulation.attendanceDay = attnStudentSummery.term1attenDay
                        }
                    }
                }
            } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                attnStudentSummery = AttnStudentSummery.findByStudentAndAcademicYear(student, exam.academicYear)
                if(attnStudentSummery){
                    if(exam.examTerm == ExamTerm.SECOND_TERM) {
                        tabulation.attendanceDay = attnStudentSummery.term2attenDay
                    } else {
                        tabulation.attendanceDay = attnStudentSummery.term1attenDay
                    }
                }
            }
            grandTotalMark = totalMark
            tabulation.totalObtainMark = totalMark.round(2)
            tabulation.grandTotalMark = grandTotalMark.round(2)
            tabulation.save(flush: true)
        }
        //variables used in exam object for statics
        exam.attended = attended
        return true
    }

    @Transactional
    def createSectionFinalTabulation(Section section, Exam exam){
        String runningSchool = schoolService.runningSchool()
        List<Tabulation> previousTerms
        Double ctTotalMark    // for Adarsha school only
        def results = examTabulationList(section, exam)

        ClassName className = section.className
        boolean subjectGroup = className.subjectGroup
        Integer workingDay = className.workingDays?:0
        Integer attendDay
        Double attendPercentage
        Double term1Mark
        Double term2Mark
        Double attendanceMark
        AttnStudentSummery attnStudentSummery
        TabulationAvgMark tabulationAvgMark

        Double totalMark
        Double grandTotalMark
        List<ExamMark> studentMarkList
        Student student

        Double gPoint
        Integer numberOfSubject
        Integer failCount
        Double gPointAverage
        LetterGrade letterGrade
        Integer attendSubjectCount

        Integer attended = 0
        int numOfPreExam = examService.numberOfTermExam(section)
        for (tabulation in results) {
            attendPercentage = 0
            attendanceMark = 0
            totalMark = 0
            ctTotalMark = 0
            grandTotalMark = 0

            student = tabulation.student

            gPoint = 0
            numberOfSubject =0
            failCount = 0
            gPointAverage = 0

            studentMarkList = examMarkService.getStudentResultMarks(exam, student)

            // calculate previous term mark and attendance mark
            if (runningSchool == CommonUtils.BAILY_SCHOOL){
                for (examMark in studentMarkList) {
                    totalMark += examMark.tabulationMark ?:0

                    if (examMark.isExtraCurricular) {
                        if(examMark.gPoint >0){
                            gPoint+= examMark.gPoint
                            numberOfSubject+=1
                        }
                    } else if (examMark.isOptional) {
                        if(examMark.resultStatus==ResultStatus.PASSED && examMark.gPoint >2){
                            gPoint+= examMark.gPoint-2
                        }
                    } else {
                        if (subjectGroup) {
                            if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                                //do nothing
                            } else {
                                if(examMark.resultStatus==ResultStatus.PASSED){
                                    gPoint+= examMark.gPoint
                                    numberOfSubject+=1
                                }else {
                                    failCount += 1
                                }
                            }
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            } else {
                                failCount += 1
                            }
                        }
                    }
                }

                term1Mark = 0
                term2Mark = 0
                attendanceMark = 0
                previousTerms = previousTermTabulations(className, student)
                if (previousTerms) {
                    if (numOfPreExam > 1) {
                        term1Mark = previousTerms[0].totalObtainMark * 0.05
                        if (previousTerms.size() >1) {
                            term2Mark = previousTerms[1].totalObtainMark * 0.05
                        }
                    } else {
                        term1Mark = previousTerms[0].totalObtainMark * 0.1
                    }
                }
                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudent(student)
                    if(attnStudentSummery){
                        attendDay =  attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay) + attnDay(attnStudentSummery.attendDay)
                        if(attendDay && attendDay>0){
                            attendPercentage = Math.round(attendDay * 100 /workingDay)
                            if(attendPercentage && attendPercentage >=80){
                                attendanceMark = 0.05 * totalMark
                            }
                        }

                    }
                }

                tabulation.term1Mark = term1Mark.round(2)
                tabulation.term2Mark = term2Mark.round(2)
                tabulation.attendanceDay = attendDay
                tabulation.attendancePercent = attendPercentage
                tabulation.attendanceMark = attendanceMark.round(2)
                grandTotalMark = totalMark + term1Mark + term2Mark + attendanceMark.round(2)
                tabulation.totalObtainMark = totalMark.round(2)
                tabulation.grandTotalMark = grandTotalMark.round(2)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL){
                for (examMark in studentMarkList) {
                    totalMark += examMark.tabulationMark ?:0
                    ctTotalMark += examMark.ctMark
                    if (examMark.isExtraCurricular) {
                        if(examMark.gPoint >0){
                            gPoint+= examMark.gPoint
                        }
                    } else {
                        if (subjectGroup) {
                            if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                                //do nothing
                            } else {
                                if(examMark.resultStatus==ResultStatus.PASSED){
                                    gPoint+= examMark.gPoint
                                    numberOfSubject+=1
                                }else {
                                    failCount += 1
                                }
                            }
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            } else {
                                failCount += 1
                            }
                        }
                    }
                }

                term1Mark = 0
                term2Mark = 0
                attendanceMark = 0
                previousTerms = previousTermTabulations(className, student)
                if (previousTerms) {
                    term1Mark = previousTerms[0].totalObtainMark
                    if (previousTerms.size() >1) {
                        term2Mark = previousTerms[1].totalObtainMark
                    }
                }

                if(ctTotalMark && ctTotalMark > 0){
                    ctTotalMark = 0.05 * ctTotalMark
                } else {
                    ctTotalMark = 0
                }

                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudent(student)
                    if(attnStudentSummery){
                        attendDay =  attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay) + attnDay(attnStudentSummery.attendDay)
                        if(attendDay && attendDay>0){
                            attendPercentage = Math.round(attendDay * 100 /workingDay)
                            if(attendPercentage && attendPercentage >=85){
                                attendanceMark = 0.05 * totalMark
                            }
                        }

                    }
                }

                tabulation.term1Mark = term1Mark.round(2)
                tabulation.term2Mark = term2Mark.round(2)
                tabulation.attendanceDay = attendDay
                tabulation.attendancePercent = attendPercentage
                tabulation.attendanceMark = attendanceMark.round(2)
                tabulation.totalCtMark = ctTotalMark
                grandTotalMark = totalMark + term1Mark + term2Mark + attendanceMark.round(2) + ctTotalMark
                tabulation.totalObtainMark = totalMark.round(2)
                tabulation.grandTotalMark = grandTotalMark.round(2)
            } else if (runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL){
                for (examMark in studentMarkList) {
                    totalMark += examMark.tabulationMark ?:0
                    if (examMark.isExtraCurricular) {
                        if(examMark.gPoint >0){
                            gPoint+= examMark.gPoint
                        }
                    } else {
                        if (subjectGroup) {
                            if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                                //do nothing
                            } else {
                                if(examMark.resultStatus==ResultStatus.PASSED){
                                    gPoint+= examMark.gPoint
                                    numberOfSubject+=1
                                }else {
                                    failCount += 1
                                }
                            }
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            } else {
                                failCount += 1
                            }
                        }
                    }
                }

                attendanceMark = 0
                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudent(student)
                    if(attnStudentSummery){
                        attendDay =  attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay) + attnDay(attnStudentSummery.attendDay)
                        /*if(attendDay && attendDay>0){
                            attendPercentage = Math.round(attendDay * 100 /workingDay)
                            if(attendPercentage && attendPercentage >=85){
                                attendanceMark = 0.05 * totalMark
                            }
                        }*/
                    }
                }

                tabulation.term1Mark = 0.0
                tabulation.term2Mark = 0.0
                tabulation.attendanceDay = attendDay
                tabulation.attendancePercent = attendPercentage
                tabulation.attendanceMark = attendanceMark.round(2)
                tabulation.totalCtMark = 0.0
                grandTotalMark = totalMark + attendanceMark.round(2)
                tabulation.totalObtainMark = totalMark.round(2)
                tabulation.grandTotalMark = grandTotalMark.round(2)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
                for (examMark in studentMarkList) {
                    totalMark += examMark.tabulationMark ?:0

                    if (examMark.isExtraCurricular) {
                        if(examMark.gPoint >0){
                            gPoint+= examMark.gPoint
                            numberOfSubject+=1
                        }
                    } else if (examMark.isOptional) {
                        if(examMark.resultStatus==ResultStatus.PASSED && examMark.gPoint >2){
                            gPoint+= examMark.gPoint-2
                        }
                    } else {
                        if (subjectGroup) {
                            if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                                //do nothing
                            } else {
                                if(examMark.resultStatus==ResultStatus.PASSED){
                                    gPoint+= examMark.gPoint
                                    numberOfSubject+=1
                                }else {
                                    failCount += 1
                                }
                            }
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            } else {
                                failCount += 1
                            }
                        }
                    }
                }

                term1Mark = 0
                term2Mark = 0

                previousTerms = previousTermTabulations(className, student)
                if (previousTerms) {
                    term1Mark = previousTerms[0].totalObtainMark
                    if (previousTerms.size() >1) {
                        term2Mark = previousTerms[1].totalObtainMark
                    }
                }
                attendanceMark = 0
                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudent(student)
                    if(attnStudentSummery){
                        attendDay =  attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay) + attnDay(attnStudentSummery.attendDay)
                        if(attendDay && attendDay>0){
                            attendPercentage = Math.round(attendDay * 100 /workingDay)
                            if(attendPercentage && attendPercentage >=80){
                                attendanceMark = 0.05 * totalMark
                            }
                        }
                    }
                }
                tabulationAvgMark = TabulationAvgMark.findByTabulationId(tabulation.id)
                if (tabulationAvgMark) {
                    if(subjectGroup) {
                        grandTotalMark = avgSubMark(tabulationAvgMark.subject1avg) + avgSubMark(tabulationAvgMark.subject3avg) + avgSubMark(tabulationAvgMark.subject4avg) + avgSubMark(tabulationAvgMark.subject5avg) + avgSubMark(tabulationAvgMark.subject6avg) + avgSubMark(tabulationAvgMark.subject7avg) + avgSubMark(tabulationAvgMark.subject8avg) + avgSubMark(tabulationAvgMark.subject9avg) + avgSubMark(tabulationAvgMark.subject10avg) + avgSubMark(tabulationAvgMark.subject11avg) + avgSubMark(tabulationAvgMark.subject12avg) + avgSubMark(tabulationAvgMark.subject13avg) + avgSubMark(tabulationAvgMark.subject14avg) + avgSubMark(tabulationAvgMark.subject15avg) + avgSubMark(tabulationAvgMark.subject16avg) + avgSubMark(tabulationAvgMark.subject17avg) + avgSubMark(tabulationAvgMark.subject18avg) + avgSubMark(tabulationAvgMark.subject19avg) + avgSubMark(tabulationAvgMark.subject20avg) + avgSubMark(tabulationAvgMark.subject21avg) + avgSubMark(tabulationAvgMark.subject22avg)+ avgSubMark(tabulationAvgMark.subject23avg) + avgSubMark(tabulationAvgMark.subject24avg) + avgSubMark(tabulationAvgMark.subject25avg) + avgSubMark(tabulationAvgMark.subject26avg) + avgSubMark(tabulationAvgMark.subject27avg) + avgSubMark(tabulationAvgMark.subject28avg)
                    } else {
                        grandTotalMark = avgSubMark(tabulationAvgMark.subject0avg) + avgSubMark(tabulationAvgMark.subject1avg) + avgSubMark(tabulationAvgMark.subject2avg) + avgSubMark(tabulationAvgMark.subject3avg) + avgSubMark(tabulationAvgMark.subject4avg) + avgSubMark(tabulationAvgMark.subject5avg) + avgSubMark(tabulationAvgMark.subject6avg) + avgSubMark(tabulationAvgMark.subject7avg) + avgSubMark(tabulationAvgMark.subject8avg) + avgSubMark(tabulationAvgMark.subject9avg) + avgSubMark(tabulationAvgMark.subject10avg) + avgSubMark(tabulationAvgMark.subject11avg) + avgSubMark(tabulationAvgMark.subject12avg) + avgSubMark(tabulationAvgMark.subject13avg) + avgSubMark(tabulationAvgMark.subject14avg) + avgSubMark(tabulationAvgMark.subject15avg) + avgSubMark(tabulationAvgMark.subject16avg) + avgSubMark(tabulationAvgMark.subject17avg) + avgSubMark(tabulationAvgMark.subject18avg) + avgSubMark(tabulationAvgMark.subject19avg) + avgSubMark(tabulationAvgMark.subject20avg)+ avgSubMark(tabulationAvgMark.subject21avg)+ avgSubMark(tabulationAvgMark.subject22avg)+ avgSubMark(tabulationAvgMark.subject23avg)+ avgSubMark(tabulationAvgMark.subject24avg)+ avgSubMark(tabulationAvgMark.subject25avg)+ avgSubMark(tabulationAvgMark.subject26avg)+ avgSubMark(tabulationAvgMark.subject27avg)+ avgSubMark(tabulationAvgMark.subject28avg)
                    }

                } else {
                    grandTotalMark = totalMark
                }
                grandTotalMark = grandTotalMark + attendanceMark

                tabulation.term1Mark = term1Mark.round(2)
                tabulation.term2Mark = term2Mark.round(2)
                tabulation.attendanceDay = attendDay
                tabulation.attendancePercent = attendPercentage
                tabulation.attendanceMark = attendanceMark.round(2)

                tabulation.totalObtainMark = totalMark.round(2)
                tabulation.grandTotalMark = grandTotalMark.round(2)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                for (examMark in studentMarkList) {
                    totalMark += examMark.tabulationMark ?:0

                    if (examMark.isExtraCurricular) {
                        if(examMark.gPoint >0){
                            gPoint+= examMark.gPoint
//                            numberOfSubject+=1
                        }
                    } else if (examMark.isOptional) {
                        if(examMark.resultStatus==ResultStatus.PASSED && examMark.gPoint >2){
                            gPoint+= examMark.gPoint-2
                        }
                    } else {
                        if (subjectGroup) {
                            if (examMark.idxOfSub == 0 || examMark.idxOfSub == 2){
                                //do nothing
                            } else {
                                if(examMark.resultStatus==ResultStatus.PASSED){
                                    gPoint+= examMark.gPoint
                                    numberOfSubject+=1
                                }else {
                                    failCount += 1
                                }
                            }
                        } else {
                            if(examMark.resultStatus==ResultStatus.PASSED){
                                gPoint+= examMark.gPoint
                                numberOfSubject+=1
                            } else {
                                failCount += 1
                            }
                        }
                    }
                }

                term1Mark = 0
                term2Mark = 0
                attendanceMark = 0
                previousTerms = previousTermTabulations(className, student)
                if (previousTerms) {
                    term1Mark = previousTerms[0].totalObtainMark
                    if (previousTerms.size() >1) {
                        term2Mark = previousTerms[1].totalObtainMark
                    }
                }

                if (workingDay > 0){
                    attnStudentSummery = AttnStudentSummery.findByStudent(student)
                    if(attnStudentSummery){
                        attendDay =  attnDay(attnStudentSummery.term1attenDay) + attnDay(attnStudentSummery.term2attenDay) + attnDay(attnStudentSummery.attendDay)
                        /*if(attendDay && attendDay>0){
                            attendPercentage = Math.round(attendDay * 100 /workingDay)
                            if(attendPercentage && attendPercentage >=80){
                                attendanceMark = 0.05 * totalMark
                            }
                        }*/
                    }
                }

                tabulation.term1Mark = term1Mark.round(2)
                tabulation.term2Mark = term2Mark.round(2)
                tabulation.attendanceDay = attendDay
                tabulation.attendancePercent = 0
                tabulation.attendanceMark = 0   //attendanceMark
                grandTotalMark = (totalMark + term1Mark)/2
                tabulation.totalObtainMark = totalMark.round(2)
                tabulation.grandTotalMark = grandTotalMark.round(2)
            }
            attendSubjectCount = examMarkService.getStudentExamAttendCount(exam, student)
            if (attendSubjectCount > 0){
                attended++
            }

            if(failCount > 0){
                tabulation.resultStatus = ResultStatus.FAILED
                tabulation.gPoint = 0
                tabulation.lGrade = LetterGrade.GRADE_F.value
                tabulation.failedSubCounter = failCount
            }else {
                gPointAverage = gPoint / numberOfSubject
                if(gPointAverage > 5){
                    gPointAverage = 5
                }
                letterGrade = gradePointService.getByPoint(gPointAverage)
                tabulation.lGrade = letterGrade.value
                tabulation.gPoint = gPointAverage.round(2)
                tabulation.resultStatus = ResultStatus.PASSED
                tabulation.failedSubCounter = failCount
            }
            tabulation.save(flush: true)
        }
        //variables used in exam object for statics
        exam.attended = attended
        return true
    }
    List<Tabulation> previousTermTabulations(ClassName className, Student student) {
        def c = Tabulation.createCriteria()
        def results = c.list() {
            and {
                eq("className", className)
                eq("student", student)
                'in'("examTerm", ExamTerm.termExamList())
            }
            order("id",CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    @Transactional
    def createSectionResult(Exam exam){
        String runningSchool = schoolService.runningSchool()
        def c = Tabulation.createCriteria()
        def results = c.list() {
            and {
                eq("exam", exam)
            }
            order("failedSubCounter",CommonUtils.SORT_ORDER_ASC)
            order("grandTotalMark",CommonUtils.SORT_ORDER_DESC)
            order("subject0Mark",CommonUtils.SORT_ORDER_DESC)
            order("subject2Mark",CommonUtils.SORT_ORDER_DESC)
        }
        int position =1
        Double previousTotal = 0
        int failOn1Subject = 0
        int failOn2Subject = 0
        int failOnMoreSubject = 0
        int scoreAPlus = 0
        int scoreA = 0
        int scoreAMinus = 0
        int scoreB = 0
        int scoreC = 0
        int scoreF = 0
        if (runningSchool == CommonUtils.BAILY_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL){
            results.each {Tabulation tabulation ->
                if (tabulation.lGrade == LetterGrade.GRADE_A_PLUS.value){
                    scoreAPlus += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_A.value){
                    scoreA += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_A_MINUS.value){
                    scoreAMinus += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_B.value){
                    scoreB += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_C.value){
                    scoreC += 1
                } else {
                    scoreF += 1
                    if (tabulation.failedSubCounter > 2) {
                        failOnMoreSubject += 1
                    } else if (tabulation.failedSubCounter == 2) {
                        failOn2Subject += 1
                    } else {
                        failOn1Subject += 1
                    }
                }
                if(tabulation.grandTotalMark==previousTotal){
                    --position
                    tabulation.positionInSection = position
                    tabulation.sectionStrPosition = CommonUtils.ordinal(position)
                    position++
                }else {
                    tabulation.positionInSection = position
                    tabulation.sectionStrPosition = CommonUtils.ordinal(position)
                    previousTotal = tabulation.grandTotalMark
                    position++
                }
                tabulation.save(flush: true)
            }
        } else {
            results.each {Tabulation tabulation ->
                if (tabulation.lGrade == LetterGrade.GRADE_A_PLUS.value){
                    scoreAPlus += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_A.value){
                    scoreA += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_A_MINUS.value){
                    scoreAMinus += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_B.value){
                    scoreB += 1
                } else if (tabulation.lGrade == LetterGrade.GRADE_C.value){
                    scoreC += 1
                } else {
                    scoreF += 1
                    if (tabulation.failedSubCounter > 2) {
                        failOnMoreSubject += 1
                    } else if (tabulation.failedSubCounter == 2) {
                        failOn2Subject += 1
                    } else {
                        failOn1Subject += 1
                    }
                }
                tabulation.positionInSection = position
                tabulation.sectionStrPosition = CommonUtils.ordinal(position)
                position++
                tabulation.save(flush: true)
            }
        }
        exam.failOn1Subject= failOnMoreSubject
        exam.failOn2Subject = failOn2Subject
        exam.failOnMoreSubject = failOnMoreSubject
        exam.scoreAPlus = scoreAPlus
        exam.scoreA = scoreA
        exam.scoreAMinus = scoreAMinus
        exam.scoreB = scoreB
        exam.scoreC = scoreC
        exam.scoreF = scoreF
        return true
    }
    @Transactional
    def createClassResult(ClassName className, List examIds, AcademicYear academicYear){
        String runningSchool = schoolService.runningSchool()
        def c = Tabulation.createCriteria()
        def results = c.list() {
            createAlias("exam","eXm")
            and {
                eq("className", className)
                'in'('eXm.id',examIds)
            }
            order("failedSubCounter",CommonUtils.SORT_ORDER_ASC)
            order("grandTotalMark",CommonUtils.SORT_ORDER_DESC)
            order("positionInSection",CommonUtils.SORT_ORDER_ASC)
        }
        int position =1
        Double previousTotal = 0
        if (runningSchool == CommonUtils.BAILY_SCHOOL){
            results.each {Tabulation tabulation ->
                if(tabulation.grandTotalMark==previousTotal){
                    --position
                    tabulation.positionInClass = position
                    tabulation.classStrPosition = CommonUtils.ordinal(position)
                    position++
                }else {
                    tabulation.positionInClass = position
                    tabulation.classStrPosition = CommonUtils.ordinal(position)
                    previousTotal = tabulation.grandTotalMark
                    position++
                }
                tabulation.save(flush: true)
            }
        }else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL){
            results.each {Tabulation tabulation ->
                if(tabulation.grandTotalMark==previousTotal){
                    --position
                    tabulation.positionInClass = position
                    tabulation.classStrPosition = CommonUtils.ordinal(position)
                    position++
                }else {
                    tabulation.positionInClass = position
                    tabulation.classStrPosition = CommonUtils.ordinal(position)
                    previousTotal = tabulation.grandTotalMark
                    position++
                }
                tabulation.save(flush: true)
            }
        } else {
            results.each {Tabulation tabulation ->
                tabulation.positionInClass = position
                tabulation.classStrPosition = CommonUtils.ordinal(position)
                previousTotal = tabulation.grandTotalMark
                position++
                tabulation.save(flush: true)
            }
        }
        return true
    }

    List listStudentByFinalExamForPromotion(AcademicYear academicYear, ClassName className, ExamTerm examTerm, GroupName groupName, int offset, int limit){
        def c = Tabulation.createCriteria()
        List results = c.list(max: limit, offset: offset) {
            createAlias("section","sec")
            and {
                eq("className", className)
                eq("resultStatus", ResultStatus.PASSED)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                eq("academicYear", academicYear)
                eq("examTerm", examTerm)
            }
            order("positionInClass",CommonUtils.SORT_ORDER_ASC)
            projections {
                property('student')
            }
        }
        return results
    }
    int countStudentByFinalExamForPromotion(AcademicYear academicYear, ClassName className, ExamTerm examTerm, GroupName groupName){
        def c = Tabulation.createCriteria()
        List results = c.list() {
            createAlias("section","sec")
            and {
                eq("className", className)
                eq("resultStatus", ResultStatus.PASSED)
                if (groupName) {
                    eq("sec.groupName", groupName)
                }
                eq("academicYear", academicYear)
                eq("examTerm", examTerm)
            }
            projections {
                count()
            }
        }
        return results[0]
    }



    def listStudentBasedClsPosition(ClassName className, List examIds, GroupName groupName, int offset, int limit){
        def c = Tabulation.createCriteria()
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



    def tabulationListForSms(List<Long> examIds){
        def c = Tabulation.createCriteria()
        def results = c.list() {
            createAlias('exam', 'xm')
            createAlias('section', 'sec')
            createAlias('student', 'std')
            and {
                'in'("xm.id", examIds)
            }
            order("sec.id",CommonUtils.SORT_ORDER_DESC)
            order("std.rollNo",CommonUtils.SORT_ORDER_DESC)
        }
        List dataReturns = new ArrayList()
        Registration registration
        Student student1
        String mobileNo
        boolean validNumber
        String sectionStr
        String studentStr
        String msgStr
        int numOfMsg
        int msgLength
        String msgStatus
        String senderBrand = grailsApplication.config.getProperty('app.message.brand.name')
        results.each { Tabulation tabulation ->
            validNumber = false
            student1 = tabulation.student
            registration = student1?.registration
            mobileNo = registration.mobile
            if(mobileNo){
                if(mobileNo.startsWith('01') && mobileNo.length()==11){
                    validNumber=true
                }else if(mobileNo.startsWith('88') && mobileNo.length()==13){
                    validNumber=true
                }else {
                    validNumber=false
                }
            }
            if(validNumber){
                sectionStr="${tabulation.className?.name} - ${tabulation.section?.name}"
                studentStr="${student1.studentID} - ${student1.name} - ${student1.rollNo}"
                msgStr=buildMsgStr(tabulation.exam.name, student1.name,tabulation.lGrade, tabulation.gPoint, tabulation.grandTotalMark, tabulation.sectionStrPosition, tabulation.classStrPosition, tabulation.resultStatus.value, senderBrand)
                msgLength=msgStr.length()
                if(msgLength>0 && msgLength<160){
                    numOfMsg=1
                }else if(msgLength>=160 && msgLength<320){
                    numOfMsg=2
                }else {
                    numOfMsg=3
                }
                msgStatus=tabulation.sentMessage?"Yes":"No"
                dataReturns.add([id: tabulation.id,sectionStr:sectionStr,studentStr:studentStr,mobileNo:mobileNo,
                                 msgStr:msgStr,numOfMsg:numOfMsg,msgStatus:msgStatus])
            }

        }
        return dataReturns
    }
    def prepareResultSms(List<Long> tabulationIds){
        def c = Tabulation.createCriteria()
        def results = c.list() {
            and {
                'in'("id", tabulationIds)
            }
        }
        List dataReturns = new ArrayList()
        Registration registration
        Student student1
        String mobileNo
        String msgStr
        String senderBrand = grailsApplication.config.getProperty('app.message.brand.name')
        results.each { Tabulation tabulation ->
            student1 = tabulation.student
            registration = student1?.registration
            mobileNo = registration.mobile
                msgStr=buildMsgStr(tabulation.exam?.name,student1.name,tabulation.lGrade, tabulation.gPoint, tabulation.grandTotalMark, tabulation.sectionStrPosition, tabulation.classStrPosition, tabulation.resultStatus.value, senderBrand)
                dataReturns.add([tabulationObj: tabulation,mobileNo:mobileNo,msgStr:msgStr])
        }
        return dataReturns
    }
    String buildMsgStr(String examName, String strName,String lGrade, Double gPoint, Double totalMark, String secPo, String clsPo, String resultStatus, String senderBrand){
       return "Exam: ${examName}, Name: ${strName}, Result:${lGrade} (${gPoint}), Total Mark: ${totalMark}, Sec Pos:${secPo}, Class Pos:${clsPo}, Status:${resultStatus}. ${senderBrand}"
    }

    String subjectComment(ExamMark examMark, ExamSchedule examSchedule, SubjectName subjectName) {
        if (examMark.hallAttendStatus != AttendStatus.PRESENT) {
            return '0 (Abs)'
        }
        def mark = examMark.tabulationMark.round(1)
        if (examSchedule.subjectType == SubjectType.INUSE && examMark.resultStatus == ResultStatus.FAILED) {
            return  "${mark} (${subjectName.name.charAt(0)})(F)"
        } else if (examSchedule.subjectType == SubjectType.INUSE) {
            return  "${mark} (${subjectName.name.charAt(0)})"
        } else if (examMark.resultStatus == ResultStatus.FAILED) {
            return  "${mark} (F)"
        }
        return null
    }
    Integer attnDay (Integer days) {
        if (!days) return 0
        return days
    }
    Double avgSubMark(Double subjectMark) {
        if (!subjectMark) return 0
        return subjectMark
    }
    List<Tabulation> examTabulationList(Section section, Exam exam) {
        return Tabulation.findAllByExam(exam)

    }

    //result analytics
    int hallStudentCount(List publishingExamList, List studentList, ResultStatus resultStatus, String lGrade) {
        if (!publishingExamList || ! studentList) return 0

        int studentCount = 0
        if (!resultStatus && !lGrade){
            studentCount = Tabulation.countByExamInListAndStudentInList(publishingExamList, studentList)
        } else if (lGrade) {
            studentCount = Tabulation.countByExamInListAndStudentInListAndResultStatusAndLGrade(publishingExamList, studentList, resultStatus, lGrade)
        } else {
            studentCount = Tabulation.countByExamInListAndStudentInListAndResultStatus(publishingExamList, studentList, resultStatus)
        }
        return studentCount
    }

    void resetMasterHallResult(Exam exam) {
        TabulationDetails.executeUpdate("delete TabulationDetails where examId = :examId",[examId: exam.id])
        Tabulation.executeUpdate("delete Tabulation where exam = :exam",[exam: exam])
        ExamSchedule.executeUpdate("update ExamSchedule es set es.isHallMarkInput = false where es.exam = :exam " +
                "and es.isHallMarkInput = true",[exam: exam])
        exam.updatedBy = springSecurityService.principal?.username
        exam.examStatus = ExamStatus.NEW
        exam.save(flush: true)
    }

    def createTabulation(Long id) {
        LinkedHashMap result = new LinkedHashMap()

        Exam exam = Exam.get(id)
        if (!exam) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
            return result
        }

        if (exam.examStatus == ExamStatus.PUBLISHED || exam.examStatus == ExamStatus.RESULT) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, 'Result already prepared or published')
            return result
        }

        if (exam.examStatus == ExamStatus.TABULATION) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, 'Tabulation already Created.')
            return result
        }
        def examSchedules = examScheduleService.examSchedules(exam, Boolean.TRUE)
        if (!examSchedules) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, 'No Exam Schedule Added')
            return result
        }

        int pendingExamCount = Exam.countByActiveStatusAndExamStatus(ActiveStatus.ACTIVE, ExamStatus.PENDING)
        if (pendingExamCount > 0) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, 'One or more of your other section exam result is still in processing. Please wait for some time to complete that and try again.')
            return result
        }

        Section section = exam.section

        if (exam.examTerm == ExamTerm.FINAL_TEST) {
            //final term exam
            createSectionFinalTabulation(section, exam)
        } else {
            createSectionTermTabulation(section, exam)
        }
        exam.updatedBy = springSecurityService.principal?.username
        exam.studentCount = studentService.numberOfStudent(section)
        exam.examStatus = ExamStatus.TABULATION
        exam.save(flush: true)
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE, 'Tabulation Created Successfully.')
        return result
    }
}
