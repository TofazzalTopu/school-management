package com.grailslab

import com.grailslab.enums.ExamType
import com.grailslab.enums.GroupName
import com.grailslab.enums.ScheduleStatus
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassSubjects
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.Section
import com.grailslab.settings.ShiftExam
import com.grailslab.settings.SubjectName
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ExamScheduleService {
    def sessionFactory
    def examScheduleService
    def classSubjectsService
    ClassNameService classNameService
    def examMarkService
    def examService

    ExamSchedule getExamSchedule(Exam exam, SubjectName subjectName){
        return ExamSchedule.findByExamAndSubjectAndActiveStatus(exam, subjectName, ActiveStatus.ACTIVE)
    }

    LinkedHashMap examScheduleList(Exam exam){
        List dataReturns = new ArrayList()
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam)
            }
            order("sortOrder", CommonUtils.SORT_ORDER_ASC)

        }
        String ctMark
        String ctEntryStatus
        boolean isCtExam
        String hallMark
        String hallEntryStatus
            results.each { ExamSchedule examSchedule ->
                if(examSchedule.isCtExam){
                    ctEntryStatus = 'Open'
                    isCtExam = true
                } else {
                    ctEntryStatus =CommonUtils.NOT_APPLICABLE
                    isCtExam = false
                }
                hallEntryStatus =examSchedule.isHallMarkInput?'Closed':'Open'
                dataReturns.add([id: examSchedule.id, subjectName: examSchedule.subject.name,
                                 ctEntryStatus:ctEntryStatus, isCtExam:isCtExam,
                                 hallEntryStatus:hallEntryStatus])
            }
        return [results:dataReturns]
    }


    def classScheduleForAdd(ClassName className, boolean isOtherActivity){
        final session = sessionFactory.currentSession
        String query
        if (isOtherActivity){
            query = new StringBuilder("select distinct cs.subject_id, sn.name subject_name, cs.sort_order sort_order, " +
                    "cs.is_ct_exam, cs.ct_mark ct_exam_mark, cs.ct_mark2 ct_exam_mark2, cs.ct_mark3 ct_exam_mark3,  cs.ct_eff_mark ct_mark_eff_percentage, " +
                    "cs.is_hall_exam, cs.hall_mark hall_exam_mark, cs.weight_on_result tabulation_eff_percentage, cs.pass_mark pass_mark, cs.hall_eff_mark hall_mark_eff_percentage, " +
                    "cs.is_hall_practical is_hall_practical, cs.is_pass_separately is_pass_separately, " +
                    "cs.hall_written_mark hall_written_mark, cs.written_pass_mark written_pass_mark, cs.hall_objective_mark hall_objective_mark, cs.objective_pass_mark objective_pass_mark, " +
                    "cs.hall_practical_mark hall_practical_mark, cs.practical_pass_mark practical_pass_mark, cs.hall_sba_mark hall_sba_mark, cs.sba_pass_mark sba_pass_mark, cs.hall_input5, cs.input5pass_mark, cs.is_extracurricular is_extracurricular " +
                    "from class_subjects cs " +
                    "inner join subject_name sn on cs.subject_id = sn.id " +
                    "where cs.active_status = 'ACTIVE' AND sn.active_status = 'ACTIVE' and cs.hall_mark > 0 AND  cs.class_name_id =:classNameId " +
                    "and cs.subject_type != 'ALTERNATIVE' order by cs.sort_order, sn.name;")
        } else {
            query = new StringBuilder("select distinct cs.subject_id, sn.name subject_name, cs.sort_order sort_order, " +
                    "cs.is_ct_exam, cs.ct_mark ct_exam_mark, cs.ct_mark2 ct_exam_mark2, cs.ct_mark3 ct_exam_mark3,  cs.ct_eff_mark ct_mark_eff_percentage, " +
                    "cs.is_hall_exam, cs.hall_mark hall_exam_mark, cs.weight_on_result tabulation_eff_percentage, cs.pass_mark pass_mark, cs.hall_eff_mark hall_mark_eff_percentage, " +
                    "cs.is_hall_practical is_hall_practical, cs.is_pass_separately is_pass_separately, " +
                    "cs.hall_written_mark hall_written_mark, cs.written_pass_mark written_pass_mark, cs.hall_objective_mark hall_objective_mark, cs.objective_pass_mark objective_pass_mark, " +
                    "cs.hall_practical_mark hall_practical_mark, cs.practical_pass_mark practical_pass_mark, cs.hall_sba_mark hall_sba_mark, cs.sba_pass_mark sba_pass_mark, cs.hall_input5, cs.input5pass_mark, cs.is_extracurricular is_extracurricular " +
                    "from class_subjects cs " +
                    "inner join subject_name sn on cs.subject_id = sn.id " +
                    "where cs.is_other_activity = false AND cs.active_status = 'ACTIVE' AND sn.active_status = 'ACTIVE' and cs.hall_mark > 0 AND cs.class_name_id =:classNameId " +
                    "and cs.subject_type != 'ALTERNATIVE' order by cs.sort_order, sn.name;")
        }

        final queryResults = session.createSQLQuery(query)
                .setParameter('classNameId', className.id).list()

        List examScheduleList = new ArrayList()
        for(resultRow in queryResults) {
            examScheduleList.add([subjectId: resultRow[0], subjectName: resultRow[1], sortOrder: resultRow[2],
                                  isCtExam: resultRow[3], ctExamMark: resultRow[4], ctExamMark2: resultRow[5], ctExamMark3: resultRow[6], ctMarkEffPercentage: resultRow[7],
                                  isHallExam: resultRow[8], hallExamMark: resultRow[9], tabulationEffPercentage: resultRow[10], passMark: resultRow[11], hallMarkEffPercentage: resultRow[12],
                                  isHallPractical: resultRow[13], isPassSeparately: resultRow[14],
                                  hallWrittenMark: resultRow[15], writtenPassMark: resultRow[16], hallObjectiveMark: resultRow[17], objectivePassMark: resultRow[18],
                                  hallPracticalMark: resultRow[19], practicalPassMark: resultRow[20], hallSbaMark: resultRow[21], sbaPassMark: resultRow[22], hallInput5: resultRow[23], input5PassMark: resultRow[24], isExtracurricular: resultRow[25]])
        }
        examScheduleList
    }
    def classScheduleForEdit(List examIdList){
        if (!examIdList) return null
        final session = sessionFactory.currentSession

        final String query = new StringBuilder("select distinct es.subject_id, sn.name subject_name, es.sort_order sort_order, " +
                "es.is_ct_exam, es.ct_exam_mark, es.ct_exam_mark2, es.ct_exam_mark3, es.ct_mark_eff_percentage, " +
                "es.is_hall_exam, es.hall_exam_mark, es.tabulation_eff_percentage, es.pass_mark pass_mark, es.hall_mark_eff_percentage, " +
                "es.is_hall_practical, es.is_pass_separately, " +
                "es.hall_written_mark, es.written_pass_mark, es.hall_objective_mark, es.objective_pass_mark, " +
                "es.hall_practical_mark, es.practical_pass_mark, es.hall_sba_mark, es.sba_pass_mark, es.hall_input5, es.input5pass_mark, es.is_extracurricular, es.full_mark " +
                "from exam_schedule es " +
                "inner join subject_name sn on es.subject_id = sn.id " +
                "where es.exam_id in (:examIdStr) and es.active_status = 'ACTIVE' order by es.sort_order, sn.name ")

        final queryResults = session.createSQLQuery(query)
                .setParameterList('examIdStr', examIdList).list()

        List examScheduleList = new ArrayList()
        for(resultRow in queryResults) {
            examScheduleList.add([subjectId: resultRow[0], subjectName: resultRow[1], sortOrder: resultRow[2],
                                  isCtExam: resultRow[3], ctExamMark: resultRow[4], ctExamMark2: resultRow[5], ctExamMark3: resultRow[6], ctMarkEffPercentage: resultRow[7],
                                  isHallExam: resultRow[8], hallExamMark: resultRow[9], tabulationEffPercentage: resultRow[10], passMark: resultRow[11], hallMarkEffPercentage: resultRow[12],
                                  isHallPractical: resultRow[13], isPassSeparately: resultRow[14],
                                  hallWrittenMark: resultRow[15], writtenPassMark: resultRow[16], hallObjectiveMark: resultRow[17], objectivePassMark: resultRow[18],
                                  hallPracticalMark: resultRow[19], practicalPassMark: resultRow[20], hallSbaMark: resultRow[21], sbaPassMark: resultRow[22], hallInput5: resultRow[23], input5PassMark: resultRow[24], isExtracurricular: resultRow[25], fullMark: resultRow[26]])
        }
        examScheduleList
    }

    void updateAllSchedule(ClassName className, List<Exam> classExamList, Map groupScheduleMap, List<String> classSubIds) {
        SubjectType subjectType
        ClassSubjects classSubject
        Section section
        Long parentSubId
        ExamSchedule examSchedule
        List<Long> ctSubjectIds
        List<Long> allSubjectIds
        def ctSubjectsSet
        def allSubjectsSet
        boolean isCompulsory = false
        SubjectName subjectName
        Map scheduleInputMap

        List<SubjectName> examSubjectIList = new ArrayList<SubjectName>()
        List<ExamSchedule> deletedSchedules
        for (Exam exam: classExamList) {
            ctSubjectIds = new ArrayList<Long>()
            allSubjectIds = new ArrayList<Long>()
            section = exam.section
            classSubIds.each { String clsSubjectId ->
                scheduleInputMap = groupScheduleMap.get(clsSubjectId)
                subjectName = scheduleInputMap.get("subjectName")
                if (section.groupName) {
                    classSubject = classSubjectsService.getBySubject(className, section.groupName, subjectName)
                } else {
                    classSubject = classSubjectsService.getBySubject(className, subjectName)
                }
                if (classSubject) {
                    examSubjectIList.add(subjectName)
                    if (classSubject.subjectType == SubjectType.INUSE && classSubject.parentSubId) {
                        classSubject = ClassSubjects.read(classSubject.parentSubId)
                        parentSubId = classSubject.subject.id
                        subjectType = SubjectType.INUSE
                    } else {
                        parentSubId = null
                        subjectType = classSubject.subjectType
                    }
                    allSubjectIds.add(classSubject.subject.id)
                    isCompulsory = true
                    if (classSubject.subjectType != SubjectType.COMPULSORY) {
                        isCompulsory = false
                    }
                    if (scheduleInputMap.get("isCtExam")){
                        ctSubjectIds.add(classSubject.subject.id)
                    }
                    examSchedule = examScheduleService.getExamSchedule(exam, subjectName)
                    if (examSchedule) {
                        examSchedule.isCtExam= scheduleInputMap.get("isCtExam")
                        examSchedule.isHallExam = Boolean.TRUE
                        examSchedule.isHallMarkInput= Boolean.FALSE
                        examSchedule.parentSubId= parentSubId
                        examSchedule.isExtracurricular= scheduleInputMap.get("isExtracurricular")
                        examSchedule.isCompulsory= isCompulsory
                        examSchedule.fullMark= scheduleInputMap.get("fullMark")
                        examSchedule.subjectType= subjectType
                        examSchedule.groupName= section.groupName
                        examSchedule.tabulationEffPercentage= scheduleInputMap.get("weightOnResult")
                        examSchedule.passMark= scheduleInputMap.get("passMark")
                        examSchedule.sortOrder= scheduleInputMap.get("sortOrder")
                        examSchedule.ctExamMark= scheduleInputMap.get("ct1Mark")
                        examSchedule.ctExamMark2= scheduleInputMap.get("ct2Mark")
                        examSchedule.ctExamMark3= scheduleInputMap.get("ct3Mark")
                        examSchedule.ctMarkEffPercentage= scheduleInputMap.get("ctEffMark")
                        examSchedule.hallExamMark= scheduleInputMap.get("hallMark")
                        examSchedule.hallMarkEffPercentage= scheduleInputMap.get("hallEffMark")
                        examSchedule.isHallPractical= scheduleInputMap.get("isHallPractical")
                        examSchedule.hallWrittenMark= scheduleInputMap.get("hallWrittenMark")
                        examSchedule.hallPracticalMark= scheduleInputMap.get("hallPracticalMark")
                        examSchedule.hallObjectiveMark= scheduleInputMap.get("hallObjectiveMark")
                        examSchedule.hallSbaMark= scheduleInputMap.get("hallSbaMark")
                        examSchedule.hallInput5= scheduleInputMap.get("hallInput5")
                        examSchedule.isPassSeparately= scheduleInputMap.get("isPassSeparately")
                        examSchedule.writtenPassMark= scheduleInputMap.get("writtenPassMark")
                        examSchedule.practicalPassMark= scheduleInputMap.get("practicalPassMark")
                        examSchedule.objectivePassMark= scheduleInputMap.get("objectivePassMark")
                        examSchedule.sbaPassMark= scheduleInputMap.get("sbaPassMark")
                        examSchedule.input5PassMark= scheduleInputMap.get("input5PassMark")
                        examSchedule.lastUpdated= new Date()
                        examSchedule.updatedBy= "admin"
                        examSchedule.save(flush: true)
                    } else {
                        new ExamSchedule(subject: subjectName, examRoom: null, exam: exam, highestMark: 0.0, averageMark: 0.0,
                                isCtExam: scheduleInputMap.get("isCtExam"), isHallExam: Boolean.TRUE,
                                hallExamDate: new Date(), hallPeriod: null, isHallMarkInput: Boolean.FALSE, parentSubId: parentSubId,
                                isExtracurricular: scheduleInputMap.get("isExtracurricular"), isOtherActivity: Boolean.FALSE,
                                isCompulsory: isCompulsory, fullMark: scheduleInputMap.get("fullMark"), subjectType: subjectType, groupName: section.groupName,
                                tabulationEffPercentage: scheduleInputMap.get("weightOnResult"), passMark: scheduleInputMap.get("passMark"), sortOrder: scheduleInputMap.get("sortOrder"),
                                ctExamMark: scheduleInputMap.get("ct1Mark"), ctExamMark2: scheduleInputMap.get("ct2Mark"), ctExamMark3: scheduleInputMap.get("ct3Mark"), ctMarkEffPercentage: scheduleInputMap.get("ctEffMark"),
                                hallExamMark: scheduleInputMap.get("hallMark"), hallMarkEffPercentage: scheduleInputMap.get("hallEffMark"),
                                isHallPractical: scheduleInputMap.get("isHallPractical"), hallWrittenMark: scheduleInputMap.get("hallWrittenMark"), hallPracticalMark: scheduleInputMap.get("hallPracticalMark"),
                                hallObjectiveMark: scheduleInputMap.get("hallObjectiveMark"), hallSbaMark: scheduleInputMap.get("hallSbaMark"), hallInput5: scheduleInputMap.get("hallInput5"),
                                isPassSeparately: scheduleInputMap.get("isPassSeparately"), writtenPassMark: scheduleInputMap.get("writtenPassMark"), practicalPassMark: scheduleInputMap.get("practicalPassMark"),
                                objectivePassMark: scheduleInputMap.get("objectivePassMark"), sbaPassMark: scheduleInputMap.get("sbaPassMark"), input5PassMark: scheduleInputMap.get("input5PassMark"),
                                dateCreated: new Date(), lastUpdated: null, createdBy: 'admin', updatedBy: null, schoolId: null,
                                academicYear: exam.academicYear, activeStatus: ActiveStatus.ACTIVE).save(flash: true)
                    }

                } else {
                    examSchedule = examScheduleService.getExamSchedule(exam, subjectName)
                    if (examSchedule) {
                        examSchedule.activeStatus = ActiveStatus.DELETE
                        examSchedule.save(flush: true)
                    }
                }
            }
            deletedSchedules = examScheduleService.schedulesNotInList(exam, examSubjectIList)
            for (ExamSchedule schedule: deletedSchedules) {
                schedule.activeStatus = ActiveStatus.DELETE
                schedule.save(flush: true)
            }

            ctSubjectsSet = ctSubjectIds as Set
            allSubjectsSet = allSubjectIds as Set
            exam.hallSchedule = ScheduleStatus.PUBLISHED
            exam.ctSubjectIds = ctSubjectsSet.join(',')
            exam.subjectIds = allSubjectsSet.join(',')
            exam.numberOfSubject = allSubjectsSet.size()
            exam.save(flash: true)
        }
    }
    void saveAllSchedule(ClassName className, List<Exam> classExamList, Map groupScheduleMap, List<String> classSubIds) {
        SubjectType subjectType
        ClassSubjects classSubject
        Section section
        Long parentSubId
        List<Long> ctSubjectIds
        List<Long> allSubjectIds
        def ctSubjectsSet
        def allSubjectsSet
        boolean isCompulsory = false
        SubjectName subjectName
        Map scheduleInputMap

        for (Exam exam: classExamList) {
            ctSubjectIds = new ArrayList<Long>()
            allSubjectIds = new ArrayList<Long>()
            section = exam.section
            classSubIds.each { String clsSubjectId ->
                scheduleInputMap = groupScheduleMap.get(clsSubjectId)
                subjectName = scheduleInputMap.get("subjectName")
                if (section.groupName) {
                    classSubject = classSubjectsService.getBySubject(className, section.groupName, subjectName)
                } else {
                    classSubject = classSubjectsService.getBySubject(className, subjectName)
                }
                if (classSubject) {
                    if (classSubject.subjectType == SubjectType.INUSE && classSubject.parentSubId) {
                        classSubject = ClassSubjects.read(classSubject.parentSubId)
                        parentSubId = classSubject.subject.id
                        subjectType = SubjectType.INUSE
                    } else {
                        parentSubId = null
                        subjectType = classSubject.subjectType
                    }
                    allSubjectIds.add(classSubject.subject.id)
                    isCompulsory = true
                    if (classSubject.subjectType != SubjectType.COMPULSORY) {
                        isCompulsory = false
                    }
                    if (scheduleInputMap.get("isCtExam")){
                        ctSubjectIds.add(classSubject.subject.id)
                    }
                    new ExamSchedule(subject: subjectName, examRoom: null, exam: exam, highestMark: 0.0, averageMark: 0.0,
                            isCtExam: scheduleInputMap.get("isCtExam"), isHallExam: Boolean.TRUE,
                            hallExamDate: new Date(), hallPeriod: null, isHallMarkInput: Boolean.FALSE, parentSubId: parentSubId,
                            isExtracurricular: scheduleInputMap.get("isExtracurricular"), isOtherActivity: Boolean.FALSE,
                            isCompulsory: isCompulsory, fullMark: scheduleInputMap.get("fullMark"), subjectType: subjectType, groupName: section.groupName,
                            tabulationEffPercentage: scheduleInputMap.get("weightOnResult"), passMark: scheduleInputMap.get("passMark"), sortOrder: scheduleInputMap.get("sortOrder"),
                            ctExamMark: scheduleInputMap.get("ct1Mark"), ctExamMark2: scheduleInputMap.get("ct2Mark"), ctExamMark3: scheduleInputMap.get("ct3Mark"), ctMarkEffPercentage: scheduleInputMap.get("ctEffMark"),
                            hallExamMark: scheduleInputMap.get("hallMark"), hallMarkEffPercentage: scheduleInputMap.get("hallEffMark"),
                            isHallPractical: scheduleInputMap.get("isHallPractical"), hallWrittenMark: scheduleInputMap.get("hallWrittenMark"), hallPracticalMark: scheduleInputMap.get("hallPracticalMark"),
                            hallObjectiveMark: scheduleInputMap.get("hallObjectiveMark"), hallSbaMark: scheduleInputMap.get("hallSbaMark"), hallInput5: scheduleInputMap.get("hallInput5"),
                            isPassSeparately: scheduleInputMap.get("isPassSeparately"), writtenPassMark: scheduleInputMap.get("writtenPassMark"), practicalPassMark: scheduleInputMap.get("practicalPassMark"),
                            objectivePassMark: scheduleInputMap.get("objectivePassMark"), sbaPassMark: scheduleInputMap.get("sbaPassMark"), input5PassMark: scheduleInputMap.get("input5PassMark"),
                            dateCreated: new Date(), lastUpdated: null, createdBy: 'admin', updatedBy: null, schoolId: null,
                            academicYear: exam.academicYear, activeStatus: ActiveStatus.ACTIVE).save(flash: true)
                }
            }
            ctSubjectsSet = ctSubjectIds as Set
            allSubjectsSet = allSubjectIds as Set
            exam.hallSchedule = ScheduleStatus.PUBLISHED
            exam.ctSubjectIds = ctSubjectsSet.join(',')
            exam.subjectIds = allSubjectsSet.join(',')
            exam.numberOfSubject = allSubjectsSet.size()
            exam.save(flash: true)
        }
    }

    void addScheduleMarkToMap(Map groupScheduleMap, Long subjectId, String clsSubId, Integer weightOnResult, Integer passMark, Integer sortOrder,
                                      boolean isExtracurricular, Integer ctExam, boolean isCtExam, Integer ct1Mark, Integer ct2Mark, Integer ct3Mark, Integer ctEffMark,
                                      Integer hallMark, Integer hallEffMark, boolean isHallPractical, Integer hallWrittenMark, Integer hallPracticalMark,
                                      Integer hallObjectiveMark, Integer hallSbaMark, Integer hallInput5, boolean isPassSeparately, Integer writtenPassMark,
                                      Integer practicalPassMark, Integer objectivePassMark, Integer sbaPassMark, Integer input5PassMark) {
        if (ctExam < 1 || !isCtExam) {
            isCtExam = false
            ct1Mark = 0
            ct2Mark = 0
            ct3Mark = 0
            ctEffMark = 0
        }

        if (isHallPractical) {
            hallMark = hallWrittenMark + hallPracticalMark + hallObjectiveMark + hallSbaMark + hallInput5
            if (!isPassSeparately) {
                writtenPassMark = 0
                practicalPassMark = 0
                objectivePassMark = 0
                sbaPassMark = 0
                input5PassMark = 0
            }
        } else {
            hallWrittenMark = 0
            hallPracticalMark = 0
            hallObjectiveMark = 0
            hallSbaMark = 0
            hallInput5 = 0
            isPassSeparately = false
            writtenPassMark = 0
            practicalPassMark = 0
            objectivePassMark = 0
            sbaPassMark = 0
            input5PassMark = 0
        }
        Integer fullMark = 0
        if (hallSbaMark > 0) {
            Integer hallwithoutSba = hallMark - hallSbaMark
            fullMark = (hallwithoutSba * hallEffMark * 0.01) + ((ct1Mark + ct2Mark + ct3Mark) * ctEffMark * 0.01)
        } else {
            fullMark = (hallMark * hallEffMark * 0.01) + ((ct1Mark + ct2Mark + ct3Mark) * ctEffMark * 0.01)
        }
        SubjectName subjectName= SubjectName.read(subjectId)
        groupScheduleMap.put(clsSubId, [subjectName         : subjectName,
                                        weightOnResult      : weightOnResult,
                                        passMark            : passMark,
                                        sortOrder           : sortOrder,
                                        isExtracurricular   : isExtracurricular,
                                        isCtExam            : isCtExam,
                                        ct1Mark              : ct1Mark,
                                        ct2Mark             : ct2Mark,
                                        ct3Mark             : ct3Mark,
                                        ctEffMark           : ctEffMark,
                                        hallEffMark         : hallEffMark,
                                        isHallPractical     : isHallPractical,
                                        hallWrittenMark     : hallWrittenMark,
                                        hallPracticalMark   : hallPracticalMark,
                                        hallObjectiveMark   : hallObjectiveMark,
                                        hallSbaMark         : hallSbaMark,
                                        hallInput5          : hallInput5,
                                        isPassSeparately    : isPassSeparately,
                                        writtenPassMark     : writtenPassMark,
                                        practicalPassMark   : practicalPassMark,
                                        objectivePassMark   : objectivePassMark,
                                        sbaPassMark         : sbaPassMark,
                                        input5PassMark      : input5PassMark,
                                        hallMark            : hallMark,
                                        fullMark            : fullMark
        ])
    }

    List<ExamSchedule> schedulesNotInList(Exam exam, List<SubjectName> subjectList){
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam )
                not {'in'("subject", subjectList)}
            }
        }
        return results
    }

    def ctSchedules(Exam exam){
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam)
                eq("isCtExam", Boolean.TRUE)
            }
        }
        return results
    }

    def examSchedules(Exam exam, Boolean isHallMarkInput){
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("isHallMarkInput", isHallMarkInput )
                eq("exam", exam)
            }
            order ('sortOrder', 'asc')
        }
        return results
    }

    def examScheduleDDListForAnalysis(ShiftExam shiftExam, ExamType examType, ClassName className){
        def c = ExamSchedule.createCriteria()
//        def results = c.list() {
        def results = c.listDistinct() {
            createAlias('exam', 'xm')
            and {
                eq("xm.shiftExam", shiftExam)
                if (className) {
                    eq("xm.className", className)
                }
                if (examType) {
                    if (examType == ExamType.CLASS_TEST) {
                        eq("isCtExam", Boolean.TRUE)
                    } else {
                        eq("isHallExam", Boolean.TRUE)
                    }
                }
                eq("activeStatus", ActiveStatus.ACTIVE )
            }
            projections {
                distinct "subject"
            }
        }
        List dataReturns = new ArrayList()
        results.each { subjectName  ->
            dataReturns.add([id: subjectName.id, name: subjectName.name])
        }
        return dataReturns
    }
    def ctSchedulesDropDownForAnalysis(Exam exam){
        List dataReturns = new ArrayList()
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam)
                eq("isCtExam", Boolean.TRUE)
            }
        }
        results.each { ExamSchedule schedule ->
            dataReturns.add([id: schedule.subject.id, name: schedule.subject.name])
        }
        return dataReturns
    }
    def hallSchedulesDropDownForAnalysis(Exam exam){
        List dataReturns = new ArrayList()
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            eq("activeStatus", ActiveStatus.ACTIVE )
            eq("exam", exam)
            or{
                and {
                    eq("isHallExam", Boolean.TRUE)
                }
                and {
                    eq("isExtracurricular", true)
                }
            }
        }
        results.each { ExamSchedule schedule ->
            dataReturns.add([id: schedule.subject.id, name: schedule.subject.name])
        }
        return dataReturns
    }

    def ctSchedulesDropDown(Exam exam){
        List dataReturns = new ArrayList()
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam)
                eq("isCtExam", Boolean.TRUE)
            }
        }
        results.each { ExamSchedule schedule ->
            dataReturns.add([id: schedule.id, subjectId: schedule.subject.id, name: schedule.subject.name])
        }
        return dataReturns
    }
    def hallSchedulesDropDown(Exam exam){
        List dataReturns = new ArrayList()
        def c = ExamSchedule.createCriteria()
        def results = c.list() {
                eq("activeStatus", ActiveStatus.ACTIVE )
                eq("exam", exam)
                eq("isHallExam", Boolean.TRUE)
        }
        results.each { ExamSchedule schedule ->
            dataReturns.add([id: schedule.id, subjectId: schedule.subject.id, name: schedule.subject.name])
        }
        return dataReturns
    }
}
