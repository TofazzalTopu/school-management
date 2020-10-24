package com.grailslab

import com.grailslab.command.LessonCommand
import com.grailslab.command.LessonUpdateCommand
import com.grailslab.command.LessonWeekCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Lesson
import com.grailslab.settings.LessonDetails
import com.grailslab.settings.LessonWeek
import com.grailslab.settings.Section
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime

import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields

@Transactional
class LessonService {
    def schoolService
    def springSecurityService
    def sessionFactory
    def calenderService
    def sectionService
    def messageSource

    def todayWorkingLessonList(ClassName className, Section section, String username){
        Date startDates = new Date().minus(1);
        final session = sessionFactory.currentSession
        def selectStr = new StringBuilder("select ls.id lesson_id, ld.id lesson_detail_id, ls.lesson_date, ls.week_number, cls.name class_name, sec.id section_id, sec.name section_name," +
                " emp.name teacher_name, ld.exam_date, ld.home_work, sub.name subject_name, ld.topics, ld.oral_home_work, ld.written_home_work " +
                " from lesson ls inner join lesson_details ld on ls.id=ld.lesson_id" +
                " inner join class_name cls on ls.class_name_id = cls.id " +
                " inner join section sec on ls.section_id = sec.id " +
                " inner join employee emp on ld.employee_id = emp.id " +
                " inner join subject_name sub on ld.subject_id = sub.id " +
                " where ls.active_status='ACTIVE' AND ld.active_status='ACTIVE' and ls.lesson_date>=:filterDate ")
        if(SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")){
            selectStr.append(" and ls.class_name_id = :classNameId")
            if (section) {
                selectStr.append(" and sec.id = :sectionId")
            }
        } else {
            selectStr.append(" and (ld.created_by=:filterUser or ld.updated_by=:filterUser)")
        }
        selectStr.append(" order by ls.lesson_date asc ")

        final sqlQuery = session.createSQLQuery(selectStr.toString())

        final queryResults = sqlQuery.with {
            setString('filterDate', CommonUtils.getMysqlQueryDateStr(startDates))
            if(SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE")){
                setLong('classNameId', className.id)
                if (section) {
                    setLong('sectionId', section.id)
                }
            } else {
                setString('filterUser', username)
            }
            list()
        }

        List lessonPlanList = new ArrayList()
        for(resultRow in queryResults) {
            lessonPlanList.add([lessonId: resultRow[0], lessonDetailId: resultRow[1], lessonDate: resultRow[2], week_number: resultRow[3], className: resultRow[4], section_id: resultRow[5], sectionName: resultRow[6], teacher_name: resultRow[7], examDate: resultRow[8], homeWork: resultRow[9], subjectName: resultRow[10], topics: resultRow[11], oral_home_work: resultRow[12], written_home_work: resultRow[13]])
        }
        return lessonPlanList
    }
    def manageLessonList(Section section, Date fromDate, Date toDate, Long subjectId){
        final session = sessionFactory.currentSession
        def selectStr = new StringBuilder("select ls.id lesson_id, ld.id lesson_detail_id, ls.lesson_date, ls.week_number, cls.name class_name, sec.id section_id, sec.name section_name," +
                " emp.name teacher_name, ld.exam_date, ld.home_work, sub.name subject_name, ld.topics, ld.oral_home_work, ld.written_home_work " +
                " from lesson ls inner join lesson_details ld on ls.id=ld.lesson_id" +
                " inner join class_name cls on ls.class_name_id = cls.id " +
                " inner join section sec on ls.section_id = sec.id " +
                " inner join employee emp on ld.employee_id = emp.id " +
                " inner join subject_name sub on ld.subject_id = sub.id " +
                " where ls.active_status='ACTIVE' AND ld.active_status='ACTIVE' and ls.lesson_date>=:filterFromDate and ls.lesson_date<:filterToDate ")
        if (section) {
            selectStr.append(" and sec.id = :sectionId")
        }
        if(subjectId){
            selectStr.append(" and sub.id = :subjectId")
        }
        selectStr.append(" order by ls.lesson_date asc ")

        final sqlQuery = session.createSQLQuery(selectStr.toString())

        final queryResults = sqlQuery.with {
            setString('filterFromDate', CommonUtils.getMysqlQueryDateStr(fromDate.clearTime()))
            setString('filterToDate', CommonUtils.getMysqlQueryDateStr(toDate.plus(1).clearTime()))
            if (section) {
                setLong('sectionId', section.id)
            }
            if (subjectId) {
                setLong('subjectId', subjectId)
            }
            list()
        }

        List lessonPlanList = new ArrayList()
        for(resultRow in queryResults) {
            lessonPlanList.add([lessonId: resultRow[0], lessonDetailId: resultRow[1], lessonDate: resultRow[2], week_number: resultRow[3], className: resultRow[4], section_id: resultRow[5], sectionName: resultRow[6], teacher_name: resultRow[7], examDate: resultRow[8], homeWork: resultRow[9], subjectName: resultRow[10], topics: resultRow[11], oral_home_work: resultRow[12], written_home_work: resultRow[13]])
        }
        return lessonPlanList
    }

    def saveLesson(GrailsParameterMap parameterMap, LessonCommand command){
        LinkedHashMap result = new LinkedHashMap()
        List sectionIds
        if (parameterMap.sectionObjIds instanceof String){
            sectionIds = new ArrayList()
            sectionIds.add(sectionIds)
        }else {
            sectionIds = parameterMap.sectionObjIds
        }
        def principal = springSecurityService.principal

        Long selectedSectionId
        Date selectedSecLessonDate
        int lessonCount = 0
        boolean isError = false
        String errorMessage
        Lesson lesson

        if(parameterMap.allSection){
            LessonWeek lessonWeek = getWeek(command.lessonDate)
            if(!lessonWeek){
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Lesson Date is not valid (Sat - Thu) or week not initiated. Please contact admin.")
                return result
            }
            DateTime lessonDate = new DateTime(command.lessonDate)
            if (!calenderService.checkIfOpenDay(lessonDate)) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "You're trying to add lesson plan for weekly or public holiday. Please check again your calender or contact contact admin.")
                return result
            }
            List sectionList = sectionService.classSections(command.className)
            sectionList.each { sectionObj ->
                lesson = getLesson(sectionObj, command.lessonDate)
                if(lesson){
                    lessonCount = LessonDetails.countByLessonAndSubjectAndActiveStatus(lesson, command.subject, ActiveStatus.ACTIVE)
                    if(lessonCount > 0){
                        isError = true
                        errorMessage = "One or more section lesson already added. Please check or contact admin"
                    }
                }
            }
            if (isError) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, errorMessage)
                return result
            }

            sectionList.each { sectionObj ->
                lesson = getLesson(sectionObj, command.lessonDate)
                if(lesson){
                    new LessonDetails(lesson: lesson, employee:command.employee,topics:command.topics,homeWork:command.homeWork,examDate:command.examDate,subject:command.subject,oralHomeWork: command.oralHomeWork,writtenHomeWork: command.writtenHomeWork, createdBy: principal.username, academicYear: principal.academicYear).save(flush: true)
                } else {
                    lesson = new Lesson(examTerm:command.examTerm, className:command.className, section:sectionObj,lessonDate:command.lessonDate, createdBy: principal.username, academicYear: principal.academicYear)
                    lesson.weekNumber = lessonWeek.weekNumber
                    lesson.weekStartDate = lessonWeek.startDate
                    lesson.weekEndDate = lessonWeek.endDate
                    lesson.save(flush: true)
                    new LessonDetails(lesson: lesson, employee:command.employee,topics:command.topics,homeWork:command.homeWork,examDate:command.examDate,subject:command.subject,oralHomeWork: command.oralHomeWork,writtenHomeWork: command.writtenHomeWork, createdBy: principal.username, academicYear: principal.academicYear).save(flush: true)
                }
            }
            result.put(CommonUtils.MESSAGE, "Lesson Added successfully.")
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }else{
            String sectionStr
            String lessonDateStr

            Section section
            LessonWeek lessonWeek
            DateTime lessonDate

            sectionIds.each { sectionId ->
                sectionStr = parameterMap."section${sectionId}"
                lessonDateStr = parameterMap."lessonDate${sectionId}"
                if (sectionStr == "on") {
                    selectedSectionId = Long.valueOf(sectionId)
                    selectedSecLessonDate = CommonUtils.getStringToDate(lessonDateStr)
                    if (selectedSectionId && selectedSecLessonDate) {
                        lessonWeek = getWeek(selectedSecLessonDate)
                        if(lessonWeek){
                            lessonDate = new DateTime(selectedSecLessonDate)
                            if (calenderService.checkIfOpenDay(lessonDate)) {
                                if(selectedSectionId){
                                    section = sectionService.find(selectedSectionId)
                                    lesson = getLesson(section, selectedSecLessonDate)
                                    if(lesson){
                                        lessonCount = LessonDetails.countByLessonAndSubjectAndActiveStatus(lesson, command.subject, ActiveStatus.ACTIVE)
                                        if(lessonCount > 0){
                                            isError = true
                                            errorMessage = "One or more section lesson already added. Please check or contact admin"
                                        }
                                    }
                                }
                            } else {
                                isError = true
                                errorMessage = "You're trying to add lesson plan for weekly or public holiday. Please check again your calender or contact contact admin."
                            }
                        } else {
                            isError = true
                            errorMessage = "Lesson Date is not valid (Sat - Thu) or week not initiated. Please contact admin."
                        }
                    } else {
                        isError = true
                        errorMessage = "Lesson date is missing for 1 or more section"
                    }
                }
            }
            if (isError) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, errorMessage)
                return result
            }

            sectionIds.each { sectionId ->
                sectionStr = parameterMap."section${sectionId}"
                lessonDateStr = parameterMap."lessonDate${sectionId}"
                if (sectionStr == "on") {
                    selectedSectionId = Long.valueOf(sectionId)
                    selectedSecLessonDate = CommonUtils.getStringToDate(lessonDateStr)

                    if (selectedSectionId && selectedSecLessonDate) {
                        lessonWeek = getWeek(selectedSecLessonDate)
                        if(lessonWeek){
                            lessonDate = new DateTime(selectedSecLessonDate)
                            if(selectedSectionId){
                                section = sectionService.find(selectedSectionId)
                                lesson = getLesson(section, selectedSecLessonDate)
                                if(lesson){
                                    new LessonDetails(lesson: lesson, employee:command.employee,topics:command.topics,homeWork:command.homeWork,examDate:command.examDate,subject:command.subject,oralHomeWork: command.oralHomeWork,writtenHomeWork: command.writtenHomeWork, createdBy: principal.username, academicYear: principal.academicYear).save(flush: true)
                                } else {
                                    lesson = new Lesson(examTerm:command.examTerm, className:command.className, section:section,lessonDate:selectedSecLessonDate, createdBy: principal.username, academicYear: principal.academicYear)
                                    lesson.weekNumber = lessonWeek.weekNumber
                                    lesson.weekStartDate = lessonWeek.startDate
                                    lesson.weekEndDate = lessonWeek.endDate
                                    lesson.save(flush: true)
                                    new LessonDetails(lesson: lesson, employee:command.employee,topics:command.topics,homeWork:command.homeWork,examDate:command.examDate,subject:command.subject,oralHomeWork: command.oralHomeWork,writtenHomeWork: command.writtenHomeWork, createdBy: principal.username, academicYear: principal.academicYear).save(flush: true)
                                }
                            }
                        }
                    }
                }
            }
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }
        result.put(CommonUtils.MESSAGE, "Lesson Added successfully.")
        return result
    }

    def updateLesson(LessonUpdateCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        DateTime lessonDate = new DateTime(command.lessonDate)
        if (!calenderService.checkIfOpenDay(lessonDate)) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You're trying to add lesson plan for weekly or public holiday. Please check again your calender or contact contact admin.")
            return result
        }

        Lesson lesson1 = Lesson.get(command.lessonId)
        if (!lesson1) {
            result.put(CommonUtils.IS_ERROR, true)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
            return result
        }

        LessonWeek lessonWeek = getWeek(command.lessonDate)
        if (!lessonWeek) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Lesson Date is not valid (Sun - Thu) or week not initiated. Please contact admin.")
            return result
        }

        int alreadyAdded = LessonDetails.countByLessonAndSubjectAndIdNotEqualAndActiveStatus(lesson1, command.subject, command.lessonDetailId, ActiveStatus.ACTIVE)
        if (alreadyAdded > 0) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${command.subject.name} Lesson already added. Please edit lesson if require.")
            return result
        }
        def principal = springSecurityService.principal
        LessonDetails oldDetail
        if (lesson1.lessonDate.clearTime() == command.lessonDate.clearTime()) {
            oldDetail = LessonDetails.get(command.lessonDetailId)
            if (oldDetail) {
                oldDetail.updatedBy = principal.username
                oldDetail.activeStatus = ActiveStatus.DELETE
                oldDetail.save(flush: true)
            }
            new LessonDetails(lesson: lesson1,
                    employee: command.employee, topics: command.topics,
                    homeWork: command.homeWork, examDate: command.examDate,
                    subject: command.subject, oralHomeWork: command.oralHomeWork,
                    writtenHomeWork: command.writtenHomeWork, createdBy: principal.username,
                    academicYear: principal.academicYear
            ).save(flush: true)

            String message = "Lesson Updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }

        Section section = lesson1.section
        ClassName className = lesson1.className
        int preLCount = LessonDetails.countByLessonAndActiveStatus(lesson1, ActiveStatus.ACTIVE)
        if (preLCount == 1) {
            lesson1.updatedBy = principal.username
            lesson1.activeStatus = ActiveStatus.DELETE
            lesson1.save(flush: true)
        } else {
            oldDetail = LessonDetails.get(command.lessonDetailId)
            if (oldDetail) {
                oldDetail.updatedBy = principal.username
                oldDetail.activeStatus = ActiveStatus.DELETE
                oldDetail.save(flush: true)
            }
        }

        LessonDetails lessonDetails
        Lesson lesson = getLesson(section, command.lessonDate)//command.className, command.lessonDate
        if (lesson) {
            oldDetail = LessonDetails.findByLessonAndSubjectAndActiveStatus(lesson, command.subject, ActiveStatus.ACTIVE)
            if (oldDetail) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Lesson already added. Please edit lesson if require.")
                return result
            }
            new LessonDetails(lesson: lesson,
                    employee: command.employee, topics: command.topics,
                    homeWork: command.homeWork, examDate: command.examDate,
                    subject: command.subject, oralHomeWork: command.oralHomeWork,
                    writtenHomeWork: command.writtenHomeWork, createdBy: principal.username,
                    academicYear: principal.academicYear).save(flush: true)
        } else {
            lesson = new Lesson(examTerm: command.examTerm,
                    className: className, section: section,
                    lessonDate: command.lessonDate, createdBy: principal.username, academicYear: principal.academicYear)
            lesson.weekNumber = lessonWeek.weekNumber
            lesson.weekStartDate = lessonWeek.startDate
            lesson.weekEndDate = lessonWeek.endDate
            lesson.save(flush: true)
            new LessonDetails(lesson: lesson, employee: command.employee, topics: command.topics, homeWork: command.homeWork, examDate: command.examDate, subject: command.subject, oralHomeWork: command.oralHomeWork, writtenHomeWork: command.writtenHomeWork, createdBy: principal.username, academicYear: principal.academicYear).save(flush: true)
        }

        String message = "Lesson updated successfully."
        result.put(CommonUtils.MESSAGE, message)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        return result
    }

    LinkedHashMap deleteLessonPlan(Long id, Long lessonDetailId) {
        LinkedHashMap result = new LinkedHashMap()
        Lesson lesson1 = Lesson.get(id)
        if (!lesson1) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_ERROR_MESSAGE)
            return result
        }
        int countDetail = LessonDetails.countByLessonAndActiveStatus(lesson1, ActiveStatus.ACTIVE)
        if (countDetail == 1) {
            lesson1.updatedBy = springSecurityService.principal?.username
            lesson1.activeStatus = ActiveStatus.DELETE
            lesson1.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Lesson deleted successfully.")
            return result
        }
        LessonDetails lessonDetails = LessonDetails.get(lessonDetailId)
        if (lessonDetails) {
            lessonDetails.updatedBy = springSecurityService.principal?.username
            lessonDetails.activeStatus = ActiveStatus.DELETE
            lessonDetails.save(flush: true)
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Lesson deleted successfully.")
        return result
    }

    def lessonPlanForFeedback(Section section,  Integer weekNumber){
        def c = Lesson.createCriteria()
        def results = c.list() {
            and {
                eq("section", section)
                eq("activeStatus",ActiveStatus.ACTIVE)
                eq("weekNumber", weekNumber)
            }
            order("lessonDate", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    Lesson getLesson(Section section, Date lessonDate){
        return Lesson.findBySectionAndLessonDateAndActiveStatus(section,lessonDate, ActiveStatus.ACTIVE)
    }

    def lessonWeeksList(ClassName className, Section section){
        AcademicYear workingYear = schoolService.schoolWorkingYear()
        def c = Lesson.createCriteria()
        List<Integer> lessonWeeks = c.list() {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
                eq("academicYear", workingYear)
                eq("className", className)
                if (section) {
                    eq("section", section)
                }
            }
            projections {
                distinct("weekNumber")
            }
        }as List<Integer>

        return lessonWeeksDropDownList(lessonWeeks)
    }
    def lessonWeeksDropDownList(List<Integer> weekNumbers){
        AcademicYear workingYear = schoolService.schoolWorkingYear()
        def c = LessonWeek.createCriteria()
        def lessonWeeks = c.list() {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
                eq("academicYear", workingYear)
                if (weekNumbers) {
                    'in'("weekNumber", weekNumbers)
                }
            }
            order("weekNumber", CommonUtils.SORT_ORDER_DESC)
        }as List<Integer>
        List dataReturns = new ArrayList()
        String dateRange
        lessonWeeks.each { obj ->
            dateRange = CommonUtils.getDateRangeStr(obj.startDate, obj.endDate)
            dataReturns.add([id: obj.weekNumber, name: "Week No ${obj.weekNumber} ($dateRange)"])
        }
        return dataReturns
    }
    def lessonWeekListForFeedback(Section section){
        def c = Lesson.createCriteria()
        def lessonWeeks = c.list() {
            and {
                eq("activeStatus",ActiveStatus.ACTIVE)
                eq("section", section)
            }
            projections {
                distinct("weekNumber")
            }
            order("weekNumber", CommonUtils.SORT_ORDER_DESC)
        }as List<Integer>
        List dataReturns = new ArrayList()
        lessonWeeks.each { obj ->
            dataReturns.add([id: obj, name: "Week No ${obj}"])
        }
        return dataReturns
    }

    LessonWeek getWeek(Date workingDate){
        LessonWeek aWeek = LessonWeek.findByActiveStatusAndStartDateLessThanEqualsAndEndDateGreaterThanEquals(ActiveStatus.ACTIVE, workingDate, workingDate)
        if (!aWeek && schoolService.runningSchool() != CommonUtils.BAILY_SCHOOL) {
            LocalDate date = workingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            WeekFields weekFields = WeekFields.of(Locale.US);
            int weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
            int dayOfWeek = date.get(weekFields.dayOfWeek())

            LocalDate startOfWeek
            LocalDate endOfWeek
            if (dayOfWeek == 7) {
                weekOfYear = weekOfYear + 1
                startOfWeek =date.with(weekFields.dayOfWeek(), 1).plusDays(6);
                endOfWeek = date.with(weekFields.dayOfWeek(), 7).plusDays(6);
            } else {
                startOfWeek =date.with(weekFields.dayOfWeek(), 1).minusDays(1);
                endOfWeek = date.with(weekFields.dayOfWeek(), 7).minusDays(1);
            }
            Date sqlStartDate = java.sql.Date.valueOf(startOfWeek);
            Date sqlEndDate = java.sql.Date.valueOf(endOfWeek);
            aWeek = saveLessonWeek(weekOfYear, sqlStartDate, sqlEndDate)
        }
        return aWeek
    }

    LessonWeek getWeekByWeekNo(Integer weekNo){
        return getWeekByWeekNo(weekNo, schoolService.schoolWorkingYear())
    }
    LessonWeek getWeekByWeekNo(Integer weekNo, AcademicYear academicYear){
        LessonWeek.findByWeekNumberAndActiveStatusAndAcademicYear(weekNo, ActiveStatus.ACTIVE, academicYear)
    }

    static final String[] sortLessonWeekColumns = ['id', 'weekNumber','startDate', 'endDate']

    LinkedHashMap lessonWeekPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 1//CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortLessonWeekColumns, iSortingCol)
        List dataReturns = new ArrayList()

        def workingYears = schoolService.workingYears()
        def c = LessonWeek.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                'in'("academicYear", workingYears)
                eq("activeStatus",ActiveStatus.ACTIVE)
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { LessonWeek lWeek ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: lWeek.id, 0: serial, 1: lWeek.weekNumber,2: CommonUtils.getUiDateStr(lWeek.startDate), 3: CommonUtils.getUiDateStr(lWeek.endDate), 4: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LessonWeek saveLessonWeek(Integer weekNumber, Date startDate, Date endDate) {
        String createOrUpdateBy = springSecurityService.principal.username
        String acaYear = startDate.format("yyyy")
        AcademicYear aYear = AcademicYear.getYearByString(acaYear)
        return new LessonWeek(weekNumber: weekNumber, startDate: startDate, endDate: endDate, academicYear: aYear, createdBy: createOrUpdateBy).save(flush: true)
    }

    def saveOrUpdate(LessonWeekCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String message
        String createOrUpdateBy = springSecurityService.principal.username
        LessonWeek lessonWeek
        if (command.id) {
            lessonWeek = LessonWeek.get(command.id)
            if (!lessonWeek) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            lessonWeek.properties = command.properties
            String acaYear = command.startDate.format("yyyy")
            AcademicYear aYear = AcademicYear.getYearByString(acaYear)
            lessonWeek.academicYear = aYear
            lessonWeek.updatedBy = createOrUpdateBy
            lessonWeek.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Lesson week Updated successfully'
        } else {
            saveLessonWeek(command.weekNumber, command.startDate, command.endDate)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Lesson Week Added successfully'
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        LessonWeek lessonWeek = LessonWeek.get(id)
        if (!lessonWeek) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        lessonWeek.activeStatus = ActiveStatus.INACTIVE
        lessonWeek.updatedBy = createOrUpdateBy
        lessonWeek.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Lesson Week Deleted Successfully')
        return result
    }
}