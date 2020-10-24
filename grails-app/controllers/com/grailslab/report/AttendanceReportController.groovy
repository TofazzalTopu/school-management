package com.grailslab.report

import com.grailslab.*
import com.grailslab.attn.AttnEmpRecord
import com.grailslab.attn.AttnRecordDay
import com.grailslab.attn.AttnStdRecord
import com.grailslab.command.report.EmpDailyAttendanceCommand
import com.grailslab.command.report.StdDailyAttendanceCommand
import com.grailslab.command.report.StdReligionAttendanceCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.AttendanceType
import com.grailslab.enums.Gender
import com.grailslab.enums.LeavePayType
import com.grailslab.enums.Religion
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student

class AttendanceReportController {
    def classNameService
    def messageSource
    def schoolService
    def sectionService
    def jasperService
    def sqlStatementService
    def studentService
    def attnStudentService
    def recordDayService
    def employeeService
    def attnEmployeeService
    def leaveService
    def baseService


    def studentAttnIndividualReport(StdDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, STD_ATTN_INDIVIDUAL_JASPER_FILE)
        List<AttnStudentReference> studentAttendance = new ArrayList<AttnStudentReference>()

        Student student = command.student
        ClassName className = student.className
        Section section = student.section

        AttnStudentReference attnStudentReference
        AttnStdRecord attnStdRecord
        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        if (command.attndanceStatus =="all") {
            for (recordday in attnRecordDay) {
                if (recordday.dayType == AttendanceType.Open_Day) {
                    attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                    if (attnStdRecord) {
                        attnStudentReference = new AttnStudentReference(recordday.recordDate, recordday.dayType, null, 'Present', attnStdRecord.inTime, attnStdRecord.outTime)
                    } else {
                        attnStudentReference = new AttnStudentReference(recordday.recordDate, recordday.dayType, null, 'Absent')
                    }
                    studentAttendance.add(attnStudentReference)
                } else {
                    attnStudentReference = new AttnStudentReference(recordday.recordDate, recordday.dayType, recordday.holidayName)
                    studentAttendance.add(attnStudentReference)
                }
            }
        } else if (command.attndanceStatus =="present") {
            def presentRecords = AttnStdRecord.findAllByRecordDayInListAndStdNo(attnRecordDay, student.studentID)
            for (stdRecord in presentRecords){
                attnStudentReference = new AttnStudentReference(stdRecord.inTime, AttendanceType.Open_Day, null, 'Present', stdRecord.inTime, stdRecord.outTime)
                studentAttendance.add(attnStudentReference)
            }
        } else {
            for (recordday in attnRecordDay) {
                if (recordday.dayType == AttendanceType.Open_Day) {
                    attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                    if (!attnStdRecord) {
                        attnStudentReference = new AttnStudentReference(recordday.recordDate, recordday.dayType, null, 'Absent')
                        studentAttendance.add(attnStudentReference)
                    }

                }
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))
        paramsMap.put('className', className.name)
        paramsMap.put('sectionName', section.name)
        paramsMap.put('studentName', student.name)
        paramsMap.put('rollNo', student.rollNo)
        paramsMap.put('studentId', student.studentID)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "${student.studentID}_attendance_report"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = studentAttendance
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def studentAttnReport(StdDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, STD_ATTN_DAILY_JASPER_FILE)
         //String reportFileName = STD_ATTN_BY_DATE_RANGE_JASPER_FILE
        List<AttnDailyReference> dailyReferences = new ArrayList<AttnDailyReference>()

        AttnDailyReference dailyReference
        AttnStdRecord attnStdRecord
        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        def stdNameList = studentService.classSectionStudentList(command.className, command.sectionName)

        for (recordday in attnRecordDay) {
            for (student in stdNameList) {
                if (command.attndanceStatus =="all") {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, null, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Present', attnStdRecord.inTime, attnStdRecord.outTime)
                        } else {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, null, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Absent')
                        }
                        dailyReferences.add(dailyReference)
                    } else {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, recordday.holidayName, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Present', attnStdRecord.inTime, attnStdRecord.outTime)
                        } else {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, recordday.holidayName, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Absent')
                        }
                        dailyReferences.add(dailyReference)
                    }
                } else if (command.attndanceStatus =="present") {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, null, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Present', attnStdRecord.inTime, attnStdRecord.outTime)
                            dailyReferences.add(dailyReference)
                        }
                    } else {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, recordday.holidayName, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Present', attnStdRecord.inTime, attnStdRecord.outTime)
                            dailyReferences.add(dailyReference)
                        }
                    }
                } else {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (!attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, null, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Absent')
                            dailyReferences.add(dailyReference)
                        }
                    } else {
                        attnStdRecord = AttnStdRecord.findByRecordDayAndStdNo(recordday, student.studentID)
                        if (!attnStdRecord) {
                            dailyReference = new AttnDailyReference(recordday.recordDate, recordday.dayType, recordday.holidayName, student.className.name, student.section.name, student.studentID, student.name, student.rollNo, 'Absent')
                            dailyReferences.add(dailyReference)
                        }
                    }
                }
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = dailyReferences
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def studentAttnSummaryReport(StdDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        List<AttnSummaryReference> studentSummary = new ArrayList<AttnSummaryReference>()
        AttnSummaryReference summaryReference
        Integer pCount
        Integer totalCount
        String reportFileName
        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        if (params.reportType == 'classWise') {
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_STD_SUMMERY_CLASS_WISE_JASPER_FILE)
            def classNameList = classNameService.allClassNames()
            for (recordDay in attnRecordDay) {
                if (recordDay.dayType == AttendanceType.Open_Day) {
                    for (className in classNameList) {
                        totalCount = studentService.numberOfClassStudent(className)
                        pCount = attnStudentService.numberOfAttnStudent(recordDay, className.name)
                        summaryReference = new AttnSummaryReference(recordDay.recordDate, className.name, null, totalCount, pCount, recordDay.dayType, null)
                        studentSummary.add(summaryReference)
                    }
                } else {
                    for (className in classNameList) {
                        totalCount = studentService.numberOfClassStudent(className)
                        pCount = attnStudentService.numberOfAttnStudent(recordDay, className.name)
                        summaryReference = new AttnSummaryReference(recordDay.recordDate, className.name, null, totalCount, pCount, recordDay.dayType, recordDay.holidayName)
                        studentSummary.add(summaryReference)
                    }
                }
            }
        } else if (params.reportType == 'sectionWise'){
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_STD_SUMMERY_SECTION_WISE_JASPER_FILE)
            def sectionNameList = sectionService.allSections(params)
            for (recordDay in attnRecordDay) {
                if (recordDay.dayType == AttendanceType.Open_Day) {
                    for (sectionName in sectionNameList) {
                        totalCount = studentService.numberOfStudent(sectionName)
                        pCount = attnStudentService.attendanceRecordCount(recordDay, sectionName.id)
                        summaryReference = new AttnSummaryReference(recordDay.recordDate, sectionName.className.name, sectionName.name, totalCount, pCount, recordDay.dayType, null)
                        studentSummary.add(summaryReference)
                    }
                }
            }

        } else if (params.reportType == 'DayWise'){
            reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_STD_SUMMERY_DAY_WISE_JASPER_FILE)
            for (recordDay in attnRecordDay) {
                if (recordDay.dayType == AttendanceType.Open_Day) {
                        totalCount = studentService.numberOfTotalStudent()
                        pCount = attnStudentService.numberOfTotalAttnStudent(recordDay)
                        summaryReference = new AttnSummaryReference(recordDay.recordDate, totalCount, pCount, recordDay.dayType, null)
                        studentSummary.add(summaryReference)
                } else {
                    totalCount = studentService.numberOfTotalStudent()
                    pCount = attnStudentService.numberOfTotalAttnStudent(recordDay)
                    summaryReference = new AttnSummaryReference(recordDay.recordDate, totalCount, pCount, recordDay.dayType, recordDay.holidayName)
                    studentSummary.add(summaryReference)
                }
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "student_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = studentSummary
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def student() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
         render(view: '/attn/stdAttnReport', model: [classNameList: classNameList])
    }


    def employee() {
        def hrCategoryList = HrCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
        render(view: '/attn/employee/empAttnReport',model: [ hrCategoryList: hrCategoryList])
    }

    def empDailyAttendance(EmpDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        AttnRecordDay recordDay = AttnRecordDay.findByRecordDate(command.rStartDate)

        String sqlParam = "where vp.record_day_id = ${recordDay ? recordDay.id : 0}"
        if (command.hrCategory) {
            sqlParam += ' and vp.hr_category_id ="' + command.hrCategory + '"'
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('recordDate', recordDay?.recordDate)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_attendance_list"
        jasperReport.reportName = EMP_DAILY_ATTENDANCE_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def stdDailyAttendance(StdDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        AttnRecordDay recordDay = AttnRecordDay.findByRecordDate(command.rStartDate)

        String sqlParam = "where spv.record_day_id = ${recordDay ? recordDay.id : 0}"
        if (command.className) {
            sqlParam += ' and spv.className ="' + command.className.name + '"'
        }
        if (command.sectionName) {
            sqlParam += ' and spv.sectionName ="' + command.sectionName.name + '"'
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('sqlParam', sqlParam)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "student_attendance_list"
        jasperReport.reportName = STD_DAILY_ATTENDANCE_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }
    def studentCountReport(StdDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, STD_ATTN_COUNT_JASPER_FILE)
        List<AttnCountReference> studentCountAttendance = new ArrayList<AttnCountReference>()
        AttnCountReference attnCountReference

        Integer pCount
        Integer workingDayCount = recordDayService.workingDay(command.rStartDate, command.rEndDate)
        def stdNameList
        if (command.sectionName) {
            stdNameList = studentService.classSectionStudentList(command.className, command.sectionName)
        } else {
            int attnYear = command.rEndDate[Calendar.YEAR]
            AcademicYear academicYear = AcademicYear.getYearByString(attnYear.toString())
            stdNameList = studentService.classSectionStudentList(command.className, command.sectionName, academicYear)
        }

        for (student in stdNameList){
            pCount = attnStudentService.studentPresentCount(student.studentID, command.rStartDate, command.rEndDate)
            attnCountReference = new AttnCountReference(student.className.name,  student.section.name, student.studentID, student.name, student.rollNo, workingDayCount, pCount)
            studentCountAttendance.add(attnCountReference)
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "student_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = studentCountAttendance
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def studentAttnBoardReport(StdDailyAttendanceCommand command){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_STD_BOARD_REPOT_JASPER_FILE)
        PrimaryAttnReference attnReference = new PrimaryAttnReference()
        String primaryClassNames = grailsApplication.config.getProperty('app.primary.class.names')
        def pClassNames = primaryClassNames.split(",")
        ClassName className
        int classStudent = 0
        int totalStudent = 0
        int classGirl = 0
        int totalGirl = 0
        for(int i=0; i<pClassNames.length; i++){
            className = ClassName.findByNameAndActiveStatus(pClassNames[i], ActiveStatus.ACTIVE)
            if (className) {
                attnReference."class${i+1}id"= className.id
                attnReference."class${i+1}name"= className.name
                classStudent = studentService.numberOfClassStudent(className)
                attnReference."class${i+1}total"= classStudent
                totalStudent += classStudent
                classGirl = studentService.numberOfGirlsStudent(className)
                attnReference."class${i+1}girl" = classGirl
                totalGirl += classGirl
            }
        }
        attnReference.totalStudent = totalStudent
        attnReference.totalGirl = totalGirl

        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        List<AttnBoardSummary> attnSummary = new ArrayList<AttnBoardSummary>()
        AttnBoardSummary dailySummary

        for (recordday in attnRecordDay) {
            if (recordday.dayType == AttendanceType.Open_Day) {
                dailySummary = new AttnBoardSummary(recordday.recordDate, recordday.dayType, null)
                 dailySummary.attn1 = attnStudentService.numberOfAttnStudent(recordday, attnReference.class1name)
                 dailySummary.attn1girl = attnStudentService.numberOfAttnStudent(recordday, attnReference.class1name, Gender.FEMALE)

                dailySummary.attn2 = attnStudentService.numberOfAttnStudent(recordday, attnReference.class1name)
                dailySummary.attn2girl = attnStudentService.numberOfAttnStudent(recordday, attnReference.class2name, Gender.FEMALE)

                dailySummary.attn3 = attnStudentService.numberOfAttnStudent(recordday, attnReference.class3name)
                dailySummary.attn3girl = attnStudentService.numberOfAttnStudent(recordday, attnReference.class3name, Gender.FEMALE)

                dailySummary.attn4 = attnStudentService.numberOfAttnStudent(recordday, attnReference.class1name)
                dailySummary.attn4girl = attnStudentService.numberOfAttnStudent(recordday, attnReference.class1name, Gender.FEMALE)

                dailySummary.attn5 = attnStudentService.numberOfAttnStudent(recordday, attnReference.class5name)
                dailySummary.attn5girl = attnStudentService.numberOfAttnStudent(recordday, attnReference.class5name, Gender.FEMALE)

            } else {
                dailySummary = new AttnBoardSummary(recordday.recordDate, recordday.dayType, recordday.holidayName)
            }
            attnSummary.add(dailySummary)
        }

        //school information
        attnReference.schoolAddress=grailsApplication.config.getProperty("app.report.address.line1")
        attnReference.creditLine=grailsApplication.config.getProperty("company.credit.footer")
        attnReference.schoolName= grailsApplication.config.getProperty('app.school.name')
        attnReference.schoolPost=grailsApplication.config.getProperty('app.school.post')
        attnReference.schoolUpozilla=grailsApplication.config.getProperty('app.school.upozilla')
        attnReference.schoolDistrict=grailsApplication.config.getProperty('app.school.district')
        attnReference.schoolNo=grailsApplication.config.getProperty('app.school.no')
        attnReference.startDate=command.rStartDate
        attnReference.endDate=command.rEndDate


        Map reportParams = new LinkedHashMap()
        reportParams.put('SUBREPORT_DIR', schoolService.subreportDir(CommonUtils.MODULE_ATTENDANCE))
        reportParams.put('attnReference', attnReference)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "student_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = reportParams
        jasperReport.reportData = attnSummary
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    def studentAttnReligion(StdReligionAttendanceCommand command){
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_STD_BOARD_REPORT2_JASPER_FILE)

        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        List<AttnReligionView> attnSummary = new ArrayList<AttnReligionView>()
        AttnReligionView dailySummary


        for (recordday in attnRecordDay) {

            if (recordday.dayType == AttendanceType.Open_Day) {

                dailySummary = new AttnReligionView(recordday.recordDate, recordday.dayType)
                dailySummary.totalStudent = attnStudentService.numberOfTotalAttnStudent(recordday)
                dailySummary.totalBoys = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.MALE )
                dailySummary.totalGirls = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.FEMALE )

                dailySummary.hinduBoys = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.MALE, Religion.HINDU )
                dailySummary.hinduGrils = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.FEMALE, Religion.HINDU )

                dailySummary.muslimsBoys = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.MALE, Religion.ISLAM )
                dailySummary.muslimsGirls = attnStudentService.numberOfTotalAttnStudent(recordday,Gender.FEMALE, Religion.ISLAM )
                dailySummary.totalTc = studentService.numberOfTcStudent()


            } else {
                /*dailySummary = new AttnReligionView(recordday.recordDate, recordday.dayType, recordday.holidayName)*/
            }
            attnSummary.add(dailySummary)
        }

        Map reportParams = new LinkedHashMap()
        reportParams.put('SUBREPORT_DIR', schoolService.subreportDir(CommonUtils.MODULE_ATTENDANCE))
        reportParams.put('dailySummary', dailySummary)
        reportParams.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        reportParams.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        reportParams.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        reportParams.put('REPORT_LOGO', schoolService.reportLogoPath())
        reportParams.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "student_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = reportParams
        jasperReport.reportData = attnSummary
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }


    def employeeAttnIndividualReport(EmpDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        Date rStartDate = Date.parse('dd/MM/yyyy', params.rStartDate)
        Date rEndDate = Date.parse('dd/MM/yyyy', params.rEndDate)

        if (params.year && params.month) {
            AcademicYear year = AcademicYear.valueOf(params.year)
            int yearVal = Integer.parseInt(year.value)
            YearMonths yearMonths = YearMonths.valueOf(params.month)
            def dateMap = CommonUtils.getFirstAndLastDate(yearVal, yearMonths.serial)
            rStartDate = dateMap.firstDay.clearTime()
            rEndDate = dateMap.lastDate.clearTime()
        }

        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, EMP_ATTN_INDIVIDUAL_JASPER_FILE)
        List<AttnEmployeeReference> employeeAttendance = new ArrayList<AttnEmployeeReference>()
        Employee employee = command.employee
        AttnEmployeeReference attnEmployeeReference
        AttnEmpRecord attnEmpRecord
        Integer openDayCount = 0
        Integer presentCount = 0
        Integer absentCount = 0
        Integer holidayCount = 0
        Integer leaveCount = 0
        Integer lateCount = 0

        String attnStatus
        def attnRecordDay = recordDayService.recordDayList(rStartDate, rEndDate)
        if (command.attndanceStatus =="all") {
            for (recordday in attnRecordDay) {
                if (recordday.dayType == AttendanceType.Open_Day) {
                    attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                    if (attnEmpRecord) {
                        if (attnEmpRecord.isLateEntry) {
                            attnStatus = "Late"
                            lateCount++
                        } else {
                            attnStatus = "Present"
                        }
                        attnEmployeeReference = new AttnEmployeeReference(recordday.recordDate, recordday.dayType, null, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                        presentCount++
                    } else {
                        if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                            attnStatus = "Leave"
                            leaveCount++
                        } else {
                            attnStatus = "Absent"
                            absentCount++
                        }
                        attnEmployeeReference = new AttnEmployeeReference(recordday.recordDate, recordday.dayType, null, attnStatus)
                    }
                    employeeAttendance.add(attnEmployeeReference)
                    openDayCount++
                } else {
                    attnEmployeeReference = new AttnEmployeeReference(recordday.recordDate, recordday.dayType, recordday.holidayName)
                    employeeAttendance.add(attnEmployeeReference)
                    holidayCount++
                }
            }
        } else if (command.attndanceStatus =="present") {
            for (recordday in attnRecordDay) {
                if (recordday.dayType == AttendanceType.Open_Day) {
                    attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                    if (attnEmpRecord) {
                        if (attnEmpRecord.isLateEntry) {
                            attnStatus = "Late"
                            lateCount++
                        } else {
                            attnStatus = "Present"
                        }
                        attnEmployeeReference = new AttnEmployeeReference(recordday.recordDate, recordday.dayType, null, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                        employeeAttendance.add(attnEmployeeReference)
                        presentCount++
                    } else {
                        if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                            leaveCount++
                        } else {
                            absentCount++
                        }
                    }
                    openDayCount++
                } else {
                    holidayCount++
                }
            }
        } else {
            for (recordday in attnRecordDay) {
                if (recordday.dayType == AttendanceType.Open_Day) {
                    attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                    if (!attnEmpRecord) {
                        if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                            attnStatus = "Leave"
                            leaveCount++
                        } else {
                            attnStatus = "Absent"
                            absentCount++
                        }

                        attnEmployeeReference = new AttnEmployeeReference(recordday.recordDate, recordday.dayType, null, attnStatus)
                        employeeAttendance.add(attnEmployeeReference)
                    } else {
                        presentCount++
                    }
                    openDayCount++
                } else {
                    holidayCount++
                }
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(rStartDate, rEndDate))
        paramsMap.put('employeeName', employee.name)
        paramsMap.put('employeeId', employee.empID)
        paramsMap.put('openDayCount', openDayCount)
        paramsMap.put('presentCount', presentCount)
        paramsMap.put('absentCount', absentCount)
        paramsMap.put('leaveCount', leaveCount)
        paramsMap.put('lateCount', lateCount)
        paramsMap.put('holidayCount', holidayCount)
        paramsMap.put('hrDesignation', employee.hrDesignation.name)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = employeeAttendance
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content

    }

    def employeeAttnSummeryReport(EmpDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, ATTN_EMP_SUMMERY_DAY_WISE_JASPER_FILE)
        List<AttnEmployeeSummaryReference> employeeSummary = new ArrayList<AttnEmployeeSummaryReference>()


        HrCategory hrCategory =command.hrCategory
        AttnEmployeeSummaryReference attnEmployeeSummaryReference
        AttnEmpRecord attnEmpRecord

        Integer pCount
        Integer leaveCount
        Integer lateCount

        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)

        def  empNameList = employeeService.employeeList(command.hrCategory)

        Integer totalCount = employeeService.numberOfTotalEmployee(command.hrCategory)
        for (recordDay in attnRecordDay) {
            pCount = attnEmployeeService.numberOfTotalAttnEmployee(recordDay, command.hrCategory)
            lateCount = attnEmployeeService.numberOfTotalLateEmployee(recordDay, command.hrCategory)
            leaveCount = leaveService.leaveCountByDate(recordDay.recordDate, command.hrCategory)
            if (recordDay.dayType == AttendanceType.Open_Day) {
                attnEmployeeSummaryReference = new AttnEmployeeSummaryReference(recordDay.recordDate, totalCount, pCount, leaveCount, lateCount, recordDay.dayType.value, null)
            } else {
                attnEmployeeSummaryReference = new AttnEmployeeSummaryReference(recordDay.recordDate, totalCount, pCount, leaveCount, lateCount, recordDay.dayType.value, recordDay.holidayName)
            }
            employeeSummary.add(attnEmployeeSummaryReference)
        }


        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))
        paramsMap.put('department', hrCategory? hrCategory.name : "All")

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = employeeSummary
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content

    }

    def employeeCountReport(EmpDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }

        HrCategory hrCategory =command.hrCategory

        AttnEmployeeCountReference attnEmployeeCountReference
        AttnEmpRecord attnEmpRecord

        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, EMP_ATTN_COUNT_JASPER_FILE)
        List<AttnEmployeeCountReference> employeeCountAttendance = new ArrayList<AttnEmployeeCountReference>()

        Integer pCount
        Integer leaveCount
        Integer lateCount
        Integer workingDayCount = recordDayService.workingDay(command.rStartDate, command.rEndDate)
        def  empNameList = employeeService.employeeList(command.hrCategory)

        for (employee in empNameList){
            pCount = attnEmployeeService.employeePresentCount(employee.empID,command.rStartDate, command.rEndDate)
            lateCount = attnEmployeeService.employeeLateCount(employee.empID, command.rStartDate, command.rEndDate)
            leaveCount = leaveService.leaveCount(employee.id, command.rStartDate, command.rEndDate, LeavePayType.PAID_LEAVE.key)
            attnEmployeeCountReference = new AttnEmployeeCountReference(employee.name, employee.empID, employee.hrDesignation?.name, workingDayCount, pCount, leaveCount, lateCount)
            employeeCountAttendance.add(attnEmployeeCountReference)
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('reportDate', CommonUtils.getDateRangeStr(command.rStartDate, command.rEndDate))
        paramsMap.put('department', hrCategory? hrCategory.name : "All")

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = employeeCountAttendance
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content

    }

    def employeeAttnReport(EmpDailyAttendanceCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('\n')
            render(view: '/baseReport/reportNotFound')
            return
        }
        String reportFileName = schoolService.reportFileName(CommonUtils.MODULE_ATTENDANCE, EMP_ATTN_DAILY_JASPER_FILE)

        List<EmpDailyReference> empdailyReferences = new ArrayList<EmpDailyReference>()

        HrCategory hrCategory =command.hrCategory
        EmpDailyReference empdailyReference
        AttnEmpRecord attnEmpRecord
        def attnRecordDay = recordDayService.recordDayList(command.rStartDate, command.rEndDate)
        def  empNameList = employeeService.employeeList(command.hrCategory)

        String attnStatus
        for (recordday in attnRecordDay) {
            for (employee in empNameList) {
                if (command.attndanceStatus =="all") {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (attnEmpRecord) {
                            if (attnEmpRecord.isLateEntry) {
                                attnStatus = "Late"
                            } else {
                                attnStatus = "Present"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                        } else {
                            if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                                attnStatus = "Leave"
                            } else {
                                attnStatus = "Absent"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus)
                        }
                        empdailyReferences.add(empdailyReference)
                    } else {
                        attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (attnEmpRecord) {
                            if (attnEmpRecord.isLateEntry) {
                                attnStatus = "Late"
                            } else {
                                attnStatus = "Present"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                        } else {
                            if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                                attnStatus = "Leave"
                            } else {
                                attnStatus = "Absent"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus)
                        }
                        empdailyReferences.add(empdailyReference)
                    }
                } else if (command.attndanceStatus =="present") {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnEmpRecord = AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (attnEmpRecord) {
                            if (attnEmpRecord.isLateEntry) {
                                attnStatus = "Late"
                            } else {
                                attnStatus = "Present"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                            empdailyReferences.add(empdailyReference)
                        }
                    } else {
                        attnEmpRecord =AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (attnEmpRecord) {
                            if (attnEmpRecord.isLateEntry) {
                                attnStatus = "Late"
                            } else {
                                attnStatus = "Present"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus, attnEmpRecord.inTime, attnEmpRecord.outTime)
                            empdailyReferences.add(empdailyReference)
                        }
                    }
                } else {
                    if (recordday.dayType == AttendanceType.Open_Day) {
                        attnEmpRecord =AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (!attnEmpRecord) {
                            if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                                attnStatus = "Leave"
                            } else {
                                attnStatus = "Absent"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus)
                            empdailyReferences.add(empdailyReference)
                        }
                    } else {
                        attnEmpRecord =AttnEmpRecord.findByRecordDayAndEmpNo(recordday, employee.empID)
                        if (!attnEmpRecord) {
                            if (leaveService.isOnLeaveByDate(employee.id, recordday.recordDate)){
                                attnStatus = "Leave"
                            } else {
                                attnStatus = "Absent"
                            }
                            empdailyReference = new EmpDailyReference(recordday.recordDate, recordday.dayType, null, employee.empID, employee.name+", "+employee.hrDesignation?.name, attnStatus)
                            empdailyReferences.add(empdailyReference)
                        }
                    }
                }
            }
        }

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('department', hrCategory? hrCategory.name : "All")

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "employee_attendance_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = empdailyReferences
        ByteArrayOutputStream baos = jasperService.generateReport(jasperReport)
        jasperReport.content = baos?.toByteArray()

        boolean forceDownload = false
        String outputFileName = (jasperReport.outputFilename ?: jasperReport.reportName) + "." + jasperReport.reportFormat.extension
        String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
        response.setContentType(jasperReport.reportFormat.mimeType)
        response.setHeader("Content-disposition", contentDisposition)
        response.setHeader("Content-Length", "${jasperReport.content.length}")
        response.outputStream << jasperReport.content
    }

    //student List Jasper report
    private static final String EMP_DAILY_ATTENDANCE_JASPER_FILE = 'attnEmpDailyAttendance'
    private static final String STD_DAILY_ATTENDANCE_JASPER_FILE = 'attnStdDailyAttendance'
    private static final String STD_ATTN_BY_Daily_PA_COUNT = 'attnStdDailyPresentAbsentCount'
    private static final String ATTN_STD_SUMMERY_CLASS_WISE_JASPER_FILE = 'attnStdSummeryClassWise'
    private static final String ATTN_STD_SUMMERY_SECTION_WISE_JASPER_FILE = 'stdSummerySectionWise'
    private static final String ATTN_STD_SUMMERY_DAY_WISE_JASPER_FILE = 'stdSummeryDayWise'
    private static final String ATTN_STD_BOARD_REPOT_JASPER_FILE = 'attnBoardReportPrimary'
    private static final String ATTN_STD_BOARD_REPORT2_JASPER_FILE = 'attnBoardReportPrimary2'
    private static final String STD_ATTN_INDIVIDUAL_JASPER_FILE = 'studentAttnRecord' //   Individual student attendance report
    private static final String EMP_ATTN_INDIVIDUAL_JASPER_FILE = 'employeeAttnRecord' //   Individual student attendance report
    private static final String STD_ATTN_DAILY_JASPER_FILE = 'studentDailyRecord' //   Daily student attendance report
    private static final String EMP_ATTN_DAILY_JASPER_FILE = 'employeeDailyRecord' //   Daily student attendance report
    private static final String STD_ATTN_COUNT_JASPER_FILE = 'studentAttnCountRecord' //   COUNT student attendance report
    private static final String EMP_ATTN_COUNT_JASPER_FILE = 'employeeAttnCountRecord' //   COUNT employee attendance report
    private static final String ATTN_EMP_SUMMERY_DAY_WISE_JASPER_FILE = 'employeeSummeryDayWise' // Employee Day wise summery report


}
