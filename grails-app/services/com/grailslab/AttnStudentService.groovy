package com.grailslab

import com.grailslab.attn.AttnRecordDay
import com.grailslab.attn.AttnStdRecord
import com.grailslab.command.AttnRecordDayCommand
import com.grailslab.command.AttnStudentCommand
import com.grailslab.command.AttnStudentLateEntryCommand
import com.grailslab.enums.AttendanceType
import com.grailslab.enums.Gender
import com.grailslab.enums.Religion
import com.grailslab.enums.Shift
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrPeriod
import com.grailslab.myapp.config.ConfigKey
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student
import com.grailslab.viewz.AttnStdPresentView
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.joda.time.DateTime
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class AttnStudentService {
    def grailsApplication
    def attnStudentService
    def recordDayService
    def hrPeriodService
    def studentService
    def classSubjectsService
    def schoolService
    def sessionFactory

    static final String[] sortColumnsStdAtt = ['recordDayId','className','stdid','name','rollNo','inTime','outTime']

    LinkedHashMap stdPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        Long recordDateId =params.getLong('recordDateId')
        ClassName className
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        }
        Section sectionName
        if (params.sectionName) {
            sectionName = Section.read(params.getLong('sectionName'))
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumnsStdAtt, iSortingCol)
        List dataReturns = new ArrayList()
        def c = AttnStdPresentView.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("recordDayId",recordDateId)
                if (className) {
                    eq("className",className.name)
                }
                if (sectionName) {
                    eq("sectionName",sectionName.name)
                }
            }
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('stdid', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { AttnStdPresentView dailyRecord ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: dailyRecord.objId, 0: serial, 1: dailyRecord.className+" - "+dailyRecord.sectionName, 2: dailyRecord.stdid, 3: dailyRecord.name, 4: dailyRecord.rollNo, 5: CommonUtils.getUiTimeStr12HrLocal(dailyRecord.inTime),6: CommonUtils.getUiTimeStr12HrLocal(dailyRecord.outTime), 7: dailyRecord.isLate? "Late": "Present", 8: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def manualAttendanceList(Section section, AttnRecordDay record) {
        List dataReturns = new ArrayList()
        final session = sessionFactory.currentSession

        int attendanceCount = attendanceRecordCount(record, section.id)

        String query = "SELECT id, name, roll_no, studentid, IF((select id from attn_std_record atr where atr.record_day_id=${record.id} and atr.student_id=student.id) is null, 0, 1) attnStatus FROM student where active_status='ACTIVE' and student_status='NEW' and section_id = ${section.id} order by roll_no asc "
        if (attendanceCount == 0) {
            query = "SELECT id, name, roll_no, studentid, 1 as attnStatus FROM student where active_status='ACTIVE' and student_status='NEW' and section_id = ${section.id} order by roll_no asc "
        }

        final sqlQuery = session.createSQLQuery(query)
        final queryResults = sqlQuery.with {
            list()
        }

        queryResults.collect { resultRow ->
            dataReturns.add([id: resultRow[0], name: resultRow[1], rollNo: resultRow[2],studentid:resultRow[3], attnStatus: resultRow[4]])
        }
        return dataReturns
    }

    def studentAttendanceList(Section section , AttnRecordDay record) {
        def c = AttnStdRecord.createCriteria()
        def results = c.list() {
            and {
                eq("recordDay", record)
                eq("sectionId", section.id)
                projections {
                    property('studentId')
                }
            }
          }

        return results
    }
    def studentAttendanceList(AttnRecordDay record) {
        def c = AttnStdRecord.createCriteria()
        def results = c.list() {
            and {
                eq("recordDay", record)
                projections {
                    property('studentId')
                }
            }
        }

        return results
    }

    int attendanceRecordCount(AttnRecordDay record, Long sectionId){
        def c = AttnStdRecord.createCriteria()
        def count = c.list() {
            and {
                eq("recordDay", record)
                eq("sectionId", sectionId)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int studentPresentCount(String studentNo, Date startDate, Date endDate){
        def c = AttnStdRecord.createCriteria()
        def count = c.list() {
            createAlias('recordDay', 'rd')
            and {
                eq("stdNo", studentNo)
                eq("rd.dayType", AttendanceType.Open_Day)
                eq("rd.activeStatus", ActiveStatus.ACTIVE)
                between("rd.recordDate", startDate, endDate)
            }
            projections {
                count()
            }
        }
        return count[0]
    }
    int numberOfAttnStudent(AttnRecordDay recordDay, String className, Gender gender = null){
        if (gender) {
            return AttnStdPresentView.countByRecordDayIdAndClassNameAndGender(recordDay.id, className, gender.key)
        } else {
            return AttnStdPresentView.countByRecordDayIdAndClassName(recordDay.id, className)
        }
    }

    int numberOfAttnStudentCount(AttnRecordDay recordDay, String className, Gender gender = null){
        if (gender) {
            return AttnStdPresentView.countByRecordDayIdAndClassNameAndGender(recordDay.id, className, gender.key)
        } else {
            return AttnStdPresentView.countByRecordDayIdAndClassName(recordDay.id, className)
        }
    }
    int numberOfTotalAttnStudent(AttnRecordDay recordDay, Gender gender = null){
        if (gender) {
            return AttnStdPresentView.countByRecordDayIdAndGender(recordDay.id, gender.key)
        } else {
            return AttnStdPresentView.countByRecordDayId(recordDay.id)
        }
    }
    int numberOfTotalAttnStudent(AttnRecordDay recordDay, Gender gender, Religion religion){
        return AttnStdPresentView.countByRecordDayIdAndGenderAndReligion(recordDay.id, gender.key, religion.key)
    }

    def saveManualAttendence(AttnRecordDayCommand command, GrailsParameterMap params){

        AttnRecordDay attnRecordDay
        LinkedHashMap result = new LinkedHashMap()
        int j = 1;
        int loadedrowsize = params.getInt("loadedrow")
        Date now = new Date();
        Date attenDate = command.attendDate
        if (!attenDate) {
            attenDate = new Date().clearTime()
        }
        Section section = command.sectionName
        String classInTime
        if(section.shift == Shift.MORNING) {
            classInTime = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_STD_MORNING_IN)
        } else {
            classInTime = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_STD_DAY_IN)
        }
        Date inTime =CommonUtils.deviceLogDateStrToDate(attenDate.format("yyyyMMdd"), classInTime)

        if (attenDate <= now.clearTime()) {
            attnRecordDay = recordDayService.recordDayForDevice(attenDate)
            if (attnRecordDay) {
                int attendanceCount = attnStudentService.attendanceRecordCount(attnRecordDay, section.id)
                if (attendanceCount > 0) {
                    AttnStdRecord.executeUpdate("delete AttnStdRecord c where c.recordDay = :recordDay and c.sectionId = :sectionId", [recordDay: attnRecordDay, sectionId: section.id])
                }

                AttnStdRecord attnStdRecordInfo
                for (int i = 0; i < loadedrowsize; i++) {
                    if (params["attanStatus[" + j + "]"].equals("1")) {
                        attnStdRecordInfo = new AttnStdRecord()
                        attnStdRecordInfo.stdNo = params["stdNo[" + j + "]"]
                        attnStdRecordInfo.studentId = params.getLong(["studentId[" + j + "]"])
                        attnStdRecordInfo.sectionId = section.id
                        attnStdRecordInfo.inTime = inTime
                        attnStdRecordInfo.outTime = null;
                        attnStdRecordInfo.isLateEntry = false;
                        attnStdRecordInfo.recordDay = attnRecordDay
                        attnStdRecordInfo.recordDate = attnRecordDay.recordDate
                        attnStdRecordInfo.save(flush: true)
                    }
                    j++;
                }
                attnRecordDay.save(flush: true)
                result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
                result.put(CommonUtils.MESSAGE, "save successfully")

            } else {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Date is not valid")
            }
        } else {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Date is not valid")
        }
        return result

    }

    def updateLateEntry(AttnStudentLateEntryCommand command) {

        ClassName clasName = command.className
        def studentList = studentService.byClassName(clasName, schoolService.workingYear(clasName))

        DateTime startDateTime
        DateTime endDateTime = new DateTime(command.endDate).withHourOfDay(23)
        DateTime thisDate
        Date effectiveDate

        HrPeriod hrPeriod = clasName.hrPeriod
        AttnStdRecord attnStdRecord
        for (student in studentList) {
            startDateTime = new DateTime(command.startDate)
            while (startDateTime.isBefore(endDateTime)) {
                thisDate = startDateTime
                effectiveDate = thisDate.toDate()
                attnStdRecord = AttnStdRecord.findByRecordDateAndStdNo(effectiveDate, student.studentID)
                if (attnStdRecord) {
                    attnStdRecord.isLateEntry = hrPeriodService.isLateEntry(attnStdRecord.inTime, hrPeriod?.startTime, hrPeriod?.lateTolerance)
                    attnStdRecord.save(flush: true)
                }
                startDateTime = startDateTime.plusDays(1)
            }
        }
        LinkedHashMap result = new LinkedHashMap()
        result.put(CommonUtils.IS_ERROR, false)
        result.put(CommonUtils.MESSAGE, "Late Attendance Updated Successfully")
        return result
    }

    def saveIndividualAttendance(AttnStudentCommand command) {

        LinkedHashMap result = new LinkedHashMap()

        String message
        Student student
        Date inTimeDate
        Date outTimeDate
        Date recordDate
        AttnStdRecord attnStdRecord

        if (command.id) {
            attnStdRecord = AttnStdRecord.get(command.id)
            student = Student.read(attnStdRecord.studentId)
            recordDate = attnStdRecord.recordDate
            inTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.inTime)
            if (command.outTime) {
                outTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.outTime)
            }
            attnStdRecord.inTime = inTimeDate
            attnStdRecord.outTime = outTimeDate
            attnStdRecord.remarks = command.reason
            attnStdRecord.isLateEntry = hrPeriodService.isLateEntry(inTimeDate, student.className.hrPeriod?.startTime, student.className.hrPeriod?.lateTolerance)
            message = "Attendance for $student.name updated successfully"
        } else {
            recordDate = command.recordDate
            if (!recordDate) recordDate = new Date().clearTime()
            AttnRecordDay attnRecordDay = recordDayService.recordDayForDevice(recordDate)
            student = Student.read(command.studentId)
            attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(attnRecordDay, student.studentID)
            if (attnStdRecord) {
                result.put(CommonUtils.IS_ERROR, true)
                result.put(CommonUtils.MESSAGE, "$student.name Attendance for ${CommonUtils.getUiDateStr(recordDate)} already added. You can modify that if require.")
                return result
            }
            inTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.inTime)
            if (command.outTime) {
                outTimeDate = CommonUtils.manualAttnDateStrToDate(recordDate, command.outTime)
            }
            Boolean isLateEntry = hrPeriodService.isLateEntry(inTimeDate, student.className.hrPeriod?.startTime, student.className.hrPeriod?.lateTolerance)
            attnStdRecord = new AttnStdRecord(recordDay: attnRecordDay,
                    recordDate: attnRecordDay.recordDate,
                    studentId: student.id,
                    stdNo: student.studentID,
                    sectionId: student.section.id,
                    inTime: inTimeDate,
                    outTime: outTimeDate,
                    remarks: command.reason,
                    isLateEntry: isLateEntry)
            message = "Attendance for $student.name added successfully"
        }
        if (!attnStdRecord.save(flush: true)) {
            message = "$student.name already exist"
            result.put(CommonUtils.IS_ERROR, false)
            result.put(CommonUtils.MESSAGE, message)
            return result
        } else {
            result.put(CommonUtils.IS_ERROR, false)
            result.put(CommonUtils.MESSAGE, message)
            return result
        }
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        AttnStdRecord attnStdRecord = AttnStdRecord.get(id)
        if (!attnStdRecord) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        try {
            attnStdRecord.delete()
        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Attendance could not deleted. Already in use.")
            return result
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Attendance deleted successfully.")
        return result
    }

}
