package com.grailslab

import com.grailslab.command.ClassRoutineCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.settings.*
import com.grailslab.stmgmt.Student
import com.grailslab.viewz.StdEmpView
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class ClassRoutineService {
    def springSecurityService
    def grailsApplication
    def schoolService
    def calenderService
    def sessionFactory

    static final String[] sortColumnsTeacher = ['days','classPeriod','period','subjectName','section','className','employee']
    static final String[] sortColumnsClass = ['classPeriod','period','subjectName','days','section','className','employee']

    LinkedHashMap classRoutineList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        Section section
        if (params.section) {
            section = Section.read(params.getLong('section'))
        }
        ClassName className
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        }
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        String sortColumn = CommonUtils.getSortColumn(sortColumnsClass, iSortingCol)
        List dataReturns = new ArrayList()
        def c = ClassRoutine.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            eq("academicYear", academicYear)
            if (className) {
                eq('className', className)
            }
            if (section) {
                eq('section', section)
            }
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { ClassRoutine obj ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: obj.id, 0: obj.className.name, 1: obj.section.name, 2: obj.classPeriod, 3: obj.period, 4: obj.subjectName? obj.subjectName.name : '', 5: obj.employee? obj.employee.empID+", "+obj.employee.name:'', 6: obj.days, 7:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    LinkedHashMap teacherRoutineList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        Employee employee
        if (params.employeeId) {
            employee = Employee.read(params.getLong('employeeId'))
        } else {
            employee = schoolService.loggedInEmployee()
        }
        Section section
        if (params.section) {
            section = Section.read(params.getLong('section'))
        }
        ClassName className
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        }
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        String sortColumn = CommonUtils.getSortColumn(sortColumnsTeacher, iSortingCol)
        List dataReturns = new ArrayList()
        def c = ClassRoutine.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            eq("activeStatus", ActiveStatus.ACTIVE)
            eq("academicYear", academicYear)
            if (employee) {
                eq('employee', employee)
            }
            if (className) {
                eq('className', className)
            }
            if (section) {
                eq('section', section)
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { ClassRoutine obj ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: obj.id, 0: obj.days, 1: obj.classPeriod, 2: obj.period, 3: obj.subjectName.name, 4: obj.section.name, 5: obj.className.name, 6: obj.employee.empID+", "+obj.employee.name,  7:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    boolean isValidDays(List previousDays, List newDays) {
        return previousDays.disjoint(newDays)
    }
    List getRoutineAsEvents(GrailsParameterMap parameterMap) {
        Date sStartFrom = CommonUtils.calendarEventDateWeekViewFormat(parameterMap.start, true)
        Date sStartEnd = CommonUtils.calendarEventDateWeekViewFormat(parameterMap.end, false)
        AcademicYear academicYear = schoolService.schoolWorkingYear()
        List dataReturns = calenderService.getPublicEvents(sStartFrom, sStartEnd)
        if (!dataReturns) {
            dataReturns = new ArrayList()
        }
        int numberOfDays = sStartEnd.minus(sStartFrom)
        if (numberOfDays <=10) {
            List dayRoutines = new ArrayList()
            //        get class Routine
            String routineType = parameterMap.routineType
            List routineDays
            if (routineType == ROUTINE_TYPE_CLASS) {
                Section section = Section.read(parameterMap.getLong('sectionId'))
                if (section) {
                    routineDays = ClassRoutine.findAllBySectionAndActiveStatusAndAcademicYear(section, ActiveStatus.ACTIVE, academicYear)
                }
            } else if (routineType == ROUTINE_TYPE_TEACHER) {
                Employee employee
                if (parameterMap.employee) {
                    employee = Employee.read(parameterMap.getLong('employee'))
                } else {
                    employee = schoolService.loggedInEmployee()
                }
                routineDays = ClassRoutine.findAllByEmployeeAndActiveStatusAndAcademicYear(employee, ActiveStatus.ACTIVE, academicYear)
            } else {
                // dashboard routine
                String userRef = schoolService.getUserRef()
                StdEmpView stdEmpView = StdEmpView.findByStdEmpNo(userRef)
                if (stdEmpView) {
                    if (stdEmpView.objType == "student") {
                        Section section = Student.read(stdEmpView.objId)?.section
                        if (section) {
                            routineDays = ClassRoutine.findAllBySectionAndActiveStatusAndAcademicYear(section, ActiveStatus.ACTIVE, academicYear)
                        }
                    } else {
                        Employee employee = Employee.read(stdEmpView.objId)
                        routineDays = ClassRoutine.findAllByEmployeeAndActiveStatusAndAcademicYear(employee, ActiveStatus.ACTIVE, academicYear)
                    }
                }
            }
            DateTime startDt = new DateTime(sStartFrom)
            DateTime endDt = new DateTime(sStartEnd)
            int dayOfWeek
            if (routineDays) {
                def weeklyList = schoolService.weeklyHolidays()
                DateTime temp2Date = new DateTime(startDt.getMillis());
                boolean isOpenDay = false
                while(temp2Date.compareTo(endDt) <=0 ){
                    dayOfWeek = temp2Date.getDayOfWeek()
                    //check if this is a class day
                    isOpenDay = calenderService.checkIfOpenDay(temp2Date)
                    if (isOpenDay) {
                        if(dayOfWeek ==  DateTimeConstants.FRIDAY ){
                            routineDays.each {routine ->
                                if (routine.days.contains("FRI")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        } else if(dayOfWeek ==  DateTimeConstants.SATURDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("SAT")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        } else if(dayOfWeek ==  DateTimeConstants.SUNDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("SUN")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        } else if(dayOfWeek ==  DateTimeConstants.MONDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("MON")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        }else if(dayOfWeek ==  DateTimeConstants.TUESDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("TUE")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        }else if(dayOfWeek ==  DateTimeConstants.WEDNESDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("WED")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        }else if(dayOfWeek ==  DateTimeConstants.THURSDAY){
                            routineDays.each {routine ->
                                if (routine.days.contains("THU")) {
                                    dayRoutines.add([clsDay: temp2Date, routine: routine])
                                }
                            }
                        }
                    }
                    temp2Date = temp2Date.plusDays(1);
                }
            }

            ClassRoutine classRoutine
            Date clsDate
            String titleStr
            String toolTipStr
            dayRoutines.each{k ->
                clsDate = k.getAt('clsDay').toDate()
                classRoutine = k.getAt('routine')
                if (routineType == ROUTINE_TYPE_CLASS) {
                    if (classRoutine.employee) {
                        toolTipStr = classRoutine.employee.name
                        titleStr = classRoutine.classPeriod + "<br/>"+ classRoutine.subjectName.name + "<br/>"+ toolTipStr
                    } else {
                        toolTipStr = classRoutine.period
                        titleStr = classRoutine.classPeriod + "<br/>"+toolTipStr
                    }
                } else if (routineType == ROUTINE_TYPE_TEACHER){
                    if (classRoutine.subjectName) {
                        toolTipStr = classRoutine.subjectName.name
                        titleStr = classRoutine.classPeriod + "<br/>"+classRoutine.className.name +" - "+classRoutine.section.name+"<br/>"+classRoutine.subjectName.name
                    } else {
                        toolTipStr = classRoutine.period
                        titleStr = classRoutine.classPeriod + "<br/>"+toolTipStr
                    }
                } else {
                    if (classRoutine.employee) {
                        toolTipStr = classRoutine.employee.name
                        titleStr = classRoutine.classPeriod + "<br/>"+classRoutine.subjectName.name + "<br/>"+ toolTipStr
                    } else {
                        toolTipStr = classRoutine.period
                        titleStr = classRoutine.classPeriod + "<br/>"+toolTipStr
                    }
                }
                dataReturns.add([ title:titleStr, start: CommonUtils.classStartTime(clsDate, classRoutine.startOn),end:CommonUtils.classEndTime(clsDate, classRoutine.startOn, classRoutine.duration),startEditable:false,durationEditable:false,className:'syllabus-event class-routine',allDay:false,tooltip:titleStr])
            }
        }

        return dataReturns
    }
    LinkedHashMap deleteRoutine(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username
        ClassRoutine classRoutine = ClassRoutine.get(id)
        if (!classRoutine) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        classRoutine.activeStatus = ActiveStatus.DELETE
        classRoutine.updatedBy = createOrUpdate
        classRoutine.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Class Routine Deleted Successfully')
        return result
    }
    LinkedHashMap saveRoutine(ClassRoutineCommand command) {
        ClassRoutine classRoutine
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username
        AcademicYear academicYear = springSecurityService.principal.academicYear
        if (command.hasErrors()) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,'Please fill the form correctly')
            return result
        }
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(CommonUtils.UI_TIME_INPUT);
        LocalTime startTime =timeFormatter.parseLocalTime(command.startOn)
        LocalTime endTime = startTime.plusMinutes(command.duration)
        if (command.id) {
            classRoutine = ClassRoutine.get(command.id)
            if (!classRoutine) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Routine not found.")
            }
            classRoutine.properties = command.properties
            classRoutine.period = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
            classRoutine.updatedBy = createOrUpdate
            classRoutine.academicYear = academicYear
            classRoutine.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put("isEdit", Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Class Routine updated Successfully')
        } else {
            classRoutine = new ClassRoutine(command.properties)
            classRoutine.period = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
            classRoutine.createdBy = createOrUpdate
            classRoutine.academicYear = academicYear
            classRoutine.save(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put("isEdit", Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, 'Class Routine added Successfully')
        }

        return result
    }

    def teacherRoutineForAnalysis(String academicYear, Long employeeId){
        final session = sessionFactory.currentSession
        final String query = "select distinct cr.class_name_id, cr.section_id, concat(cn.name,' - ', sec.name) cls_sec, cr.subject_name_id, sn.name, cn.sort_position cposition, sn.sort_position sposition from class_routine cr inner join class_name cn on cr.class_name_id = cn.id inner join section sec on cr.section_id = sec.id inner join subject_name sn on cr.subject_name_id = sn.id where cr.academic_year =:academicYear and cr.employee_id =:employeeId and cr.active_status = 'ACTIVE' order by cn.sort_position desc, sn.sort_position asc;"

        final queryResults = session.createSQLQuery(query)
                .setParameter('academicYear', academicYear)
                .setParameter('employeeId', employeeId).list()

        List teacherRoutineList = new ArrayList()
        for(resultRow in queryResults) {
            teacherRoutineList.add([classNameId: resultRow[0], sectionId: resultRow[1], clsSec: resultRow[2], subjectId: resultRow[3], subjectName: resultRow[4]])
        }
        return teacherRoutineList
    }

    public static final String ROUTINE_TYPE_CLASS = "classRoutine"
    public static final String ROUTINE_TYPE_TEACHER = "teacherRoutine"

}
