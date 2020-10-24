package com.grailslab.report

import com.grailslab.*
import com.grailslab.command.ExamScheduleBySectionCommand
import com.grailslab.command.report.ResultAnalysisCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.*

class ExamReportController{
    def messageSource
    def schoolService
    def jasperService
    def studentService
    def classSubjectsService
    def studentSubjectsService
    def examMarkService
    def tabulationService
    def examService
    def classNameService
    def subjectService
    def baseService
    def employeeService
    def classRoutineService
    def sectionService
    def tabulationCtService

    def tabulation(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)
        Exam exam
        if (params.examId){
            exam = Exam.read(params.getLong('examId'))
        }
        if(!shiftExam || !exam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }
        if(exam.examStatus == ExamStatus.NEW){
            flash.message ="Result not ready"
            render(view: '/baseReport/reportNotFound')
            return
        }

//        def subIds = classSubjectsService.hallExamSubjectHeaderIds(exam.className)
        int numberOfSubject = exam.numberOfSubject //exam.numberOfSubject      //subIds.size()
        if(numberOfSubject<3 || numberOfSubject>30){
            flash.message ="Tabulation only Supported from 3 to 30 subjects in Class."
            render(view: '/baseReport/reportNotFound')
            return
        }
        String runningSchool = schoolService.runningSchool()

        def subIds = exam.subjectIds.split(',')
        def subjects
        SubjectName subject
        ClassName className = exam.className
        ExamSchedule examSchedule
        def idValue
        Double clsOrSecHighest = 0
        if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            def examIds
            if (false/*exam.examStatus == ExamStatus.RESULT || exam.examStatus == ExamStatus.PUBLISHED*/){
                List<Exam> examList = examService.examListForPreparingResult(shiftExam, className, null)
                examIds = examList.collect {it.id}
            } else {
                examIds = new ArrayList<Long>()
                examIds.add(exam.id)
            }
            subjects = new ParamDetailReference()
            for(int i=0; i<numberOfSubject; i++){
                idValue = subIds[i]
                subject = SubjectName.read(idValue.toLong())
                if(subject){
                    subjects."param${i}Name"= subject.name
                    if (subject.hasChild) {
                        examSchedule = ExamSchedule.findByExamAndParentSubIdAndActiveStatus(exam, subject.id, ActiveStatus.ACTIVE)
                    } else {
                        examSchedule = ExamSchedule.findByExamAndSubjectAndActiveStatus(exam, subject, ActiveStatus.ACTIVE)
                    }
                    if (examSchedule.isHallPractical) {
                        subjects."subject${i}ct" = examSchedule.ctExamMark?:0 + examSchedule.ctExamMark2?:0 + examSchedule.ctExamMark3?:0
                        subjects."subject${i}ctEff" = examSchedule.ctMarkEffPercentage
                        subjects."subject${i}hallWri" = examSchedule.hallWrittenMark
                        subjects."subject${i}hallObj" = examSchedule.hallObjectiveMark
                        subjects."subject${i}hallPrac" = examSchedule.hallPracticalMark
                        subjects."subject${i}hallEff" = examSchedule.hallMarkEffPercentage
                    } else {
                        subjects."subject${i}ct" = examSchedule.ctExamMark?:0 + examSchedule.ctExamMark2?:0 + examSchedule.ctExamMark3?:0
                        subjects."subject${i}ctEff" = examSchedule.ctMarkEffPercentage
                        subjects."subject${i}hallWri" = examSchedule.hallExamMark
                        subjects."subject${i}hallEff" = examSchedule.hallMarkEffPercentage
                    }
                    subjects."subject${i}highest" = tabulationService.highestMark(examIds, "subject${i}Mark")
                }
            }
            clsOrSecHighest = tabulationService.highestMark(examIds, "totalObtainMark")
        } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
            def examIds
            if (false/*exam.examStatus == ExamStatus.RESULT || exam.examStatus == ExamStatus.PUBLISHED*/){
                List<Exam> examList = examService.examListForPreparingResult(shiftExam, className, null)
                examIds = examList.collect {it.id}
            } else {
                examIds = new ArrayList<Long>()
                examIds.add(exam.id)
            }
            subjects = new ParamDetailReference()
            for(int i=0; i<numberOfSubject; i++){
                idValue = subIds[i]
                subject = SubjectName.read(idValue.toLong())
                if(subject){
                    subjects."param${i}Name"= subject.name
                    if (subject.hasChild) {
                        examSchedule = ExamSchedule.findByExamAndParentSubIdAndActiveStatus(exam, subject.id, ActiveStatus.ACTIVE)
                    } else {
                        examSchedule = ExamSchedule.findByExamAndSubjectAndActiveStatus(exam, subject, ActiveStatus.ACTIVE)
                    }
                    if (examSchedule.isHallPractical) {
                        subjects."subject${i}ct" = examSchedule.ctExamMark?:0 + examSchedule.ctExamMark2?:0 + examSchedule.ctExamMark3?:0
                        subjects."subject${i}ctEff" = examSchedule.ctMarkEffPercentage
                        subjects."subject${i}hallWri" = examSchedule.hallWrittenMark
                        subjects."subject${i}hallObj" = examSchedule.hallObjectiveMark
                        subjects."subject${i}hallPrac" = examSchedule.hallPracticalMark
                        subjects."subject${i}hallSba" = examSchedule.hallSbaMark
                        subjects."subject${i}hallEff" = examSchedule.hallMarkEffPercentage
                    } else {
                        subjects."subject${i}ct" = examSchedule.ctExamMark?:0 + examSchedule.ctExamMark2?:0 + examSchedule.ctExamMark3?:0
                        subjects."subject${i}ctEff" = examSchedule.ctMarkEffPercentage
                        subjects."subject${i}hallWri" = examSchedule.hallExamMark
                        subjects."subject${i}hallEff" = examSchedule.hallMarkEffPercentage
                    }
                    subjects."subject${i}highest" = tabulationService.highestMark(examIds, "subject${i}Mark")
                }
            }
            clsOrSecHighest = tabulationService.highestMark(examIds, "totalObtainMark")
        } else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL || runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL) {
            subjects = new ParamReference()
            for(int i=0; i<numberOfSubject; i++){
                idValue = subIds[i]
                subject = SubjectName.read(idValue.toLong())
                if(subject){
                    subjects."param${i}Name"= subject.name
                }
            }
        } else {
            subjects = new ParamReference()
            for(int i=0; i<numberOfSubject; i++){
                idValue = subIds[i]
                subject = SubjectName.read(idValue.toLong())
                if(subject){
                    if (subject.hasChild) {
                        examSchedule = ExamSchedule.findByExamAndParentSubIdAndActiveStatus(exam, subject.id, ActiveStatus.ACTIVE)
                    } else {
                        examSchedule = ExamSchedule.findByExamAndSubjectAndActiveStatus(exam, subject, ActiveStatus.ACTIVE)
                    }
                    if(examSchedule.tabulationEffPercentage<100){
                        subjects."param${i}Name"= "${examSchedule.tabulationEffPercentage}% of ${subject.name}"
                    }else {
                        subjects."param${i}Name"= subject.name
                    }
                }
            }
        }
        int numOfPreExam = 1
        String reportFileName
        String reportQuery
        String sortBy = params.sortBy
        String orderByStr = "order by std.roll_no asc"
        if(exam.examStatus==ExamStatus.RESULT || exam.examStatus==ExamStatus.PUBLISHED){
            if (sortBy == 'secMeritPos') {
                orderByStr = "order by tabu.position_in_section asc"
            } else if(sortBy == 'clsMeritPos') {
                orderByStr = "order by tabu.position_in_class asc"
            }
        }

        if(exam.examTerm==ExamTerm.FINAL_TEST){
            if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                if (className.subjectGroup) {
                    //need to show group in tabulation
                    reportFileName =schoolService.reportFileName("finalTabulationGroup${numberOfSubject}")
                } else {
                    reportFileName =schoolService.reportFileName("finalTabulationNoGroup${numberOfSubject}")
                }
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.FINAL_TEST.key)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL) {
                reportFileName =schoolService.reportFileName("finalTabulation${numberOfSubject}")
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.FINAL_TEST.key)
            } else if (runningSchool == CommonUtils.BAILY_SCHOOL) {
                numOfPreExam = examService.numberOfTermExam(exam.section)
                if(numOfPreExam==1){
                    reportFileName=schoolService.reportFileName("finalTabulation${numberOfSubject}Term1")
                }else {
                    reportFileName=schoolService.reportFileName("finalTabulation${numberOfSubject}")
                }
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.FINAL_TEST.key)
            } else if (runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL) {
                reportFileName=schoolService.reportFileName("termTabulation${numberOfSubject}")
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.FINAL_TEST.key)
            }
        } else {
            if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL || runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                if (className.subjectGroup) {
                    reportFileName =schoolService.reportFileName("termTabulationGroup${numberOfSubject}")
                } else {
                    reportFileName =schoolService.reportFileName("termTabulationNoGroup${numberOfSubject}")
                }
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.TERM_EXAM.key)
            } else {
                reportFileName =schoolService.reportFileName("termTabulation${numberOfSubject}")
                reportQuery = getTabulationReportCardQuery(runningSchool, exam.id, orderByStr, ExamTerm.TERM_EXAM.key)
            }
        }
        String reportLogo = schoolService.reportLogoPath()


        int studentCount = exam.studentCount?:0
        int attended = exam.attended?:0
        int scoreF = exam.scoreF?:0

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)

        paramsMap.put('reportQuery', reportQuery)
        paramsMap.put('className', className.name)
        paramsMap.put('sectionName', exam.section.name)
        paramsMap.put('groupName', exam.section.groupName?.key)
        paramsMap.put('academicYear', exam.academicYear.value)
        paramsMap.put('examName', shiftExam.examName)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('resultPublishOn', CommonUtils.getUiDateStr(shiftExam.resultPublishOn))
        //exam parametes
        paramsMap.put('numberOfStudent', studentCount)
        paramsMap.put('absentCount', studentCount - attended)
        paramsMap.put('scoreaplus', exam.scoreAPlus)
        paramsMap.put('scorea', exam.scoreA)
        paramsMap.put('scoreaminus', exam.scoreAMinus)
        paramsMap.put('scoreb', exam.scoreB)
        paramsMap.put('scorec', exam.scoreC)
        paramsMap.put('scoref', scoreF)
        paramsMap.put('passCount', studentCount - scoreF)
        paramsMap.put('fail_on1subject', exam.failOn1Subject)
        paramsMap.put('fail_on2subject', exam.failOn2Subject)
        paramsMap.put('fail_on_more_subject', exam.failOnMoreSubject)
        paramsMap.put('clsOrSecHighest', clsOrSecHighest)
        paramsMap.put('periodStart', shiftExam.periodStart)
        paramsMap.put('periodEnd', shiftExam.periodEnd)
        paramsMap.put('workingDays', className.workingDays)
        paramsMap.put('signatureText', "Principal's Signature")
        if(numOfPreExam==1){
            paramsMap.put('term1Name', "Half Yearly")
        }else if(numOfPreExam==2){
            paramsMap.put('term1Name', "1st Term")
            paramsMap.put('term2Name', "2nd Term")
        }
        // put subject name parameter up to 14 subjects
        String paramName
        String paramSub
        if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            String paramCtMark
            Integer ctExamMark
            String paramCtEffMark
            Integer ctEffMark
            String paramWriMark
            Integer wriMark
            String paramObjMark
            Integer objMark
            String paramPracMark
            Integer pracMark
            String paramHallEffMark
            Integer hallEffMark

            String paramHighestMark
            Double highestMark

            for(int i=0; i<numberOfSubject; i++){
                paramName = "param${i}Name"
                paramSub = subjects."param${i}Name"
                paramsMap.put(paramName,paramSub)

                paramCtMark = "param${i}CtMark"
                ctExamMark = subjects."subject${i}ct"
                paramsMap.put(paramCtMark,ctExamMark)

                paramCtEffMark = "param${i}CtEffMark"
                ctEffMark = subjects."subject${i}ctEff"
                paramsMap.put(paramCtEffMark,ctEffMark)

                paramWriMark = "param${i}WriMark"
                wriMark = subjects."subject${i}hallWri"
                paramsMap.put(paramWriMark,wriMark)

                paramObjMark = "param${i}ObjMark"
                objMark = subjects."subject${i}hallObj"
                paramsMap.put(paramObjMark,objMark)

                paramPracMark = "param${i}PracMark"
                pracMark = subjects."subject${i}hallPrac"
                paramsMap.put(paramPracMark,pracMark)

                paramHallEffMark = "param${i}HallEffMark"
                hallEffMark = subjects."subject${i}hallEff"
                paramsMap.put(paramHallEffMark,hallEffMark)

                paramHighestMark = "param${i}highest"
                highestMark = subjects."subject${i}highest"
                paramsMap.put(paramHighestMark, highestMark)
            }
        } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
            String paramCtMark
            Integer ctExamMark
            String paramCtEffMark
            Integer ctEffMark
            String paramWriMark
            Integer wriMark
            String paramObjMark
            Integer objMark
            String paramPracMark
            Integer pracMark
            String paramSbaMark
            Integer sbaMark
            String paramHallEffMark
            Integer hallEffMark

            String paramHighestMark
            Double highestMark

            for(int i=0; i<numberOfSubject; i++){
                paramName = "param${i}Name"
                paramSub = subjects."param${i}Name"
                paramsMap.put(paramName,paramSub)

                paramCtMark = "param${i}CtMark"
                ctExamMark = subjects."subject${i}ct"
                paramsMap.put(paramCtMark,ctExamMark)

                paramCtEffMark = "param${i}CtEffMark"
                ctEffMark = subjects."subject${i}ctEff"
                paramsMap.put(paramCtEffMark,ctEffMark)

                paramWriMark = "param${i}WriMark"
                wriMark = subjects."subject${i}hallWri"
                paramsMap.put(paramWriMark,wriMark)

                paramObjMark = "param${i}ObjMark"
                objMark = subjects."subject${i}hallObj"
                paramsMap.put(paramObjMark,objMark)

                paramPracMark = "param${i}PracMark"
                pracMark = subjects."subject${i}hallPrac"
                paramsMap.put(paramPracMark,pracMark)

                paramSbaMark = "param${i}SbaMark"
                sbaMark = subjects."subject${i}hallSba"
                paramsMap.put(paramSbaMark,sbaMark)

                paramHallEffMark = "param${i}HallEffMark"
                hallEffMark = subjects."subject${i}hallEff"
                paramsMap.put(paramHallEffMark,hallEffMark)

                paramHighestMark = "param${i}highest"
                highestMark = subjects."subject${i}highest"
                paramsMap.put(paramHighestMark, highestMark)
            }
        } else {
            for(int i=0; i<numberOfSubject; i++){
                paramName = "param${i}Name"
                paramSub = subjects."param${i}Name"
                paramsMap.put(paramName,paramSub)
            }
        }

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Tabulation_Sheet"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def ctTabulation(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)
        Exam exam
        if (params.examId){
            exam = Exam.read(params.getLong('examId'))
        }
        if(!shiftExam || !exam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }

        def subIds = exam.ctSubjectIds.split(',')
        int numberOfSubject = subIds.size()
        if(numberOfSubject<3 || numberOfSubject>25){
            flash.message ="Tabulation only Supported from 3 to 25 subjects in Class."
            render(view: '/baseReport/reportNotFound')
            return
        }

        String examName = shiftExam.examName
        String ctExamNo = params.ctExamNo
        String sortBy = params.sortBy
        String orderByStr = "std.roll_no asc"
        String queryParam
        if (ctExamNo == "CLASS_TEST3") {
            if (shiftExam.ctExam < 3 || exam.ct3ExamStatus == ExamStatus.NEW || exam.ct3ExamStatus == ExamStatus.PENDING || exam.ct3ExamStatus == ExamStatus.PROCESSED) {
                flash.message ="Result not ready"
                render(view: '/baseReport/reportNotFound')
                return
            } else {
                if(exam.ct3ExamStatus==ExamStatus.RESULT || exam.ct3ExamStatus==ExamStatus.PUBLISHED){
                    if (sortBy == 'secMeritPos') {
                        orderByStr = "tabu.position_in_section asc"
                    } else if(sortBy == 'clsMeritPos') {
                        orderByStr = "tabu.position_in_class asc"
                    }
                }
            }
            queryParam = tabulationCtService.getCtTabulationQueryStr(exam.id, 3, orderByStr)
            examName += " Tutorial 3"

        } else if (ctExamNo == "CLASS_TEST2") {
            if (shiftExam.ctExam < 2 || exam.ct2ExamStatus == ExamStatus.NEW || exam.ct2ExamStatus == ExamStatus.PENDING || exam.ct2ExamStatus == ExamStatus.PROCESSED) {
                flash.message ="Result not ready"
                render(view: '/baseReport/reportNotFound')
                return
            } else {
                if(exam.ct2ExamStatus==ExamStatus.RESULT || exam.ct2ExamStatus==ExamStatus.PUBLISHED){
                    if (sortBy == 'secMeritPos') {
                        orderByStr = "tabu.position_in_section asc"
                    } else if(sortBy == 'clsMeritPos') {
                        orderByStr = "tabu.position_in_class asc"
                    }
                }
            }
            queryParam = tabulationCtService.getCtTabulationQueryStr(exam.id, 2, orderByStr)
            examName += " Tutorial 2"
        } else if (ctExamNo == "CLASS_TEST1") {
            if (shiftExam.ctExam < 1 || exam.ctExamStatus == ExamStatus.NEW || exam.ctExamStatus == ExamStatus.PENDING || exam.ctExamStatus == ExamStatus.PROCESSED) {
                flash.message ="Result not ready"
                render(view: '/baseReport/reportNotFound')
                return
            } else {
                if(exam.ctExamStatus==ExamStatus.RESULT || exam.ctExamStatus==ExamStatus.PUBLISHED){
                    if (sortBy == 'secMeritPos') {
                        orderByStr = "tabu.position_in_section asc"
                    } else if(sortBy == 'clsMeritPos') {
                        orderByStr = "tabu.position_in_class asc"
                    }
                }
            }
            queryParam = tabulationCtService.getCtTabulationQueryStr(exam.id, 1, orderByStr)
            examName += " Tutorial 1"
        }

        SubjectName subject
        ExamSchedule examSchedule
        ClassName className = exam.className
        def idValue

        def subjects = new ParamReference()
        for(int i=0; i<numberOfSubject; i++){
            idValue = subIds[i]
            subject = SubjectName.read(idValue.toLong())
            if(subject){
                subjects."param${i}Name"= subject.name
            }
        }
        Section section = exam.section
        String reportFileName =schoolService.reportFileName(CommonUtils.MODULE_CT, "termCtTabulation${numberOfSubject}")
        String reportLogo = schoolService.reportLogoPath()

        int studentCount = studentService.numberOfStudent(section)
        int attended = studentCount

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('examId', exam?.id)

        paramsMap.put('className', className.name)
        paramsMap.put('sectionName', section.name)
        paramsMap.put('groupName', section.groupName?.key)
        paramsMap.put('academicYear', exam.academicYear.value)
        paramsMap.put('examName', examName)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))

        paramsMap.put('numberOfStudent', studentCount)
        paramsMap.put('absentCount', studentCount - attended)
        paramsMap.put('queryParam', queryParam)

        // put subject name parameter up to 14 subjects
        String paramName
        String paramSub
        for(int i=0; i<numberOfSubject; i++){
            paramName = "param${i}Name"
            paramSub = subjects."param${i}Name"
            paramsMap.put(paramName,paramSub)
        }


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Tabulation_Sheet"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def topsheet(){
        ShiftExam shiftExam
        if (params.examName){
            shiftExam = ShiftExam.read(params.getLong('examName'))
        }
        ClassName className
        if (params.classname) {
            className = ClassName.read(params.getLong('classname'))
        }
        GroupName groupName
        if (params.groupName){
            groupName = GroupName.valueOf(params.groupName)
        }
        if(!shiftExam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }

        Exam exam
        if (params.examId){
            exam = Exam.read(params.examId)
        }

        def studentList
        if (exam) {
            studentList = studentService.allStudentForTabulation(exam.section)
        } else {
            studentList = studentService.byClassName(className, shiftExam.academicYear, groupName)
        }
        List classExamList
        if (exam) {
            classExamList = [exam]
        } else {
            classExamList = examService.classExamList(shiftExam, className, groupName)
            if (classExamList){
                exam = classExamList.first()
            }
        }
        int numberOfSubject = exam.numberOfSubject      //subIds.size()
        if(numberOfSubject < 4 || numberOfSubject > 30){
            flash.message ="Top Sheet only Supported from 4 to 20 subjects in Class."
            render(view: '/baseReport/reportNotFound')
            return
        }

        def subIds = exam.subjectIds.split(',')
        TopSheetReference topSheetReference = new TopSheetReference()
        SubjectName subject
        ExamSchedule examSchedule
        def idValue
        int failCount = 0
        int passCount = 0
        int numberOfExaminee = studentList.size()
        for(int i=0; i<numberOfSubject; i++){
            idValue = subIds[i]
            subject = SubjectName.read(idValue.toLong())
            if(subject){
                if (subject.hasChild) {
//                    String alterNativeSubIds = SubjectName.findAllByActiveStatusAndParentId(ActiveStatus.ACTIVE, subject.id).collect {it.id}.join(",")
                    examSchedule = ExamSchedule.findByExamAndParentSubIdAndActiveStatus(exam, subject.id, ActiveStatus.ACTIVE)
                } else {
                    examSchedule = ExamSchedule.findByExamAndSubjectAndActiveStatus(exam, subject, ActiveStatus.ACTIVE)
                }
                if (examSchedule.subjectType == SubjectType.ALTERNATIVE) {
                    passCount = examMarkService.getExamSubjectPassCount(classExamList, studentList, alterNativeSubIds)
                    topSheetReference."param${i}absent" = examMarkService.getExamSubjectAbsentCount(classExamList, studentList, alterNativeSubIds)
                    topSheetReference."param${i}pass" = passCount
                    topSheetReference."param${i}fail" = numberOfExaminee - passCount
                    topSheetReference."param${i}aPlus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, alterNativeSubIds, LetterGrade.GRADE_A_PLUS.value)
                    topSheetReference."param${i}a" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, alterNativeSubIds, LetterGrade.GRADE_A.value)
                    topSheetReference."param${i}aMinus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, alterNativeSubIds, LetterGrade.GRADE_A_MINUS.value)
                    topSheetReference."param${i}b" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, alterNativeSubIds, LetterGrade.GRADE_B.value)
                    topSheetReference."param${i}c" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, alterNativeSubIds, LetterGrade.GRADE_C.value)
                } else if (examSchedule.subjectType == SubjectType.OPTIONAL) {
                    topSheetReference."param${i}absent" = examMarkService.getExamSubjectAbsentCount(classExamList, studentList, subject)
                    topSheetReference."param${i}pass" = examMarkService.getExamSubjectPassCount(classExamList, studentList, subject)
                    topSheetReference."param${i}fail" = 0
                    topSheetReference."param${i}aPlus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A_PLUS.value)
                    topSheetReference."param${i}a" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A.value)
                    topSheetReference."param${i}aMinus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A_MINUS.value)
                    topSheetReference."param${i}b" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_B.value)
                    topSheetReference."param${i}c" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_C.value)
                } else {
                    passCount = examMarkService.getExamSubjectPassCount(classExamList, studentList, subject)
                    topSheetReference."param${i}absent" = examMarkService.getExamSubjectAbsentCount(classExamList, studentList, subject)
                    topSheetReference."param${i}pass" = passCount
                    topSheetReference."param${i}fail" = numberOfExaminee - passCount
                    topSheetReference."param${i}aPlus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A_PLUS.value)
                    topSheetReference."param${i}a" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A.value)
                    topSheetReference."param${i}aMinus" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_A_MINUS.value)
                    topSheetReference."param${i}b" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_B.value)
                    topSheetReference."param${i}c" = examMarkService.getExamSubjectLetterGradeCount(classExamList, studentList, subject, LetterGrade.GRADE_C.value)
                }
                if(examSchedule && examSchedule.tabulationEffPercentage<100){
                    topSheetReference."param${i}Name"= "${examSchedule.tabulationEffPercentage}% of ${subject.name}"
                }else {
                    topSheetReference."param${i}Name"= subject.name
                }

            }
        }
        String reportFileName ="topsheet${numberOfSubject}"
        String reportLogo = schoolService.reportLogoPath()
        int numberOfSuccess = tabulationService.getNumberOfPassStudentCount(classExamList)

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())

        paramsMap.put('examId', exam?.id)
        paramsMap.put('className', exam.className.name)
        paramsMap.put('classNameId', exam.className.id)
        paramsMap.put('sectionName', groupName? groupName.value : exam.section.name)
        paramsMap.put('academicYear', exam.academicYear.value)
        paramsMap.put('examName', exam.shiftExam?.examName)
        paramsMap.put('resultPublishOn', CommonUtils.getUiDateStr(exam.shiftExam.resultPublishOn))
        //exam parametes
        paramsMap.put('numberOfStudent', numberOfExaminee)
        paramsMap.put('numberOfExaminee', numberOfExaminee)
        paramsMap.put('numberOfSuccess', numberOfSuccess)

        // put subject name parameter up to 14 subjects
        String paramName
        String paramSub
        String paramAbsent
        int paramAbsentScore
        String paramPass
        int paramPassScore

        String paramFail
        int paramFailScore

        String paramaPlus
        int paramaPlusScore
        String paramAGrade
        int paramAScore
        String paramaMinus
        int paramaMinusScore

        String paramBGrade
        int paramBScore
        String paramCGrade
        int paramCScore
        for(int i=0; i<numberOfSubject; i++){
            paramName = "param${i}Name"
            paramSub = topSheetReference."param${i}Name"
            paramsMap.put(paramName,paramSub)

            paramAbsent = "param${i}absent"
            paramAbsentScore = topSheetReference."param${i}absent"
            paramsMap.put(paramAbsent, paramAbsentScore)

            paramPass = "param${i}pass"
            paramPassScore = topSheetReference."param${i}pass"
            paramsMap.put(paramPass, paramPassScore)

            paramFail = "param${i}fail"
            paramFailScore = topSheetReference."param${i}fail"
            paramsMap.put(paramFail, paramFailScore)

            paramaPlus = "param${i}aPlus"
            paramaPlusScore = topSheetReference."param${i}aPlus"
            paramsMap.put(paramaPlus, paramaPlusScore)

            paramAGrade = "param${i}a"
            paramAScore = topSheetReference."param${i}a"
            paramsMap.put(paramAGrade, paramAScore)

            paramaMinus = "param${i}aMinus"
            paramaMinusScore = topSheetReference."param${i}aMinus"
            paramsMap.put(paramaMinus, paramaMinusScore)

            paramBGrade = "param${i}b"
            paramBScore = topSheetReference."param${i}b"
            paramsMap.put(paramBGrade, paramBScore)

            paramCGrade = "param${i}c"
            paramCScore = topSheetReference."param${i}c"
            paramsMap.put(paramCGrade, paramCScore)
        }

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Top_Sheet_${exam.section.name.replaceAll(" ","_")}"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def meritList(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)

        Exam exam
        if (params.examId){
            exam = Exam.read(params.getLong('examId'))
        }
        if(!shiftExam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName className = ClassName.read(params.getLong('classname'))

        String sqlParam = "exam_id in ( select exam.id from exam inner join section on exam.section_id = section.id where exam.active_status ='ACTIVE' and exam.shift_exam_id = ${id}"

        String reportFileName
        GroupName groupName
        if (params.examId){
            reportFileName ="meritListReport"
            sqlParam += " and exam.section_id = ${exam.section.id} )"

            if (params.sortBy =="rollNo") {
                sqlParam += " order by sec.id, st.roll_no "
            } else if (params.sortBy =="secMeritPos") {
                sqlParam += " order by sec.id, ta.position_in_section "
            } else {
                sqlParam += " order by ta.position_in_class "
            }
        } else {
            reportFileName ="meritListReportByClass"
            if (params.groupName) {
                groupName = GroupName.valueOf(params.groupName)
                sqlParam += " and section.group_name = '"+params.groupName+"'"
            }
            sqlParam += " and exam.class_name_id = ${params.classname} )"

            if (params.sortBy =="rollNo") {
                sqlParam += " order by sec.id, st.roll_no "
            } else if (params.sortBy =="secMeritPos") {
                sqlParam += " order by sec.id, ta.position_in_section "
            } else {
                sqlParam += " order by ta.position_in_class "
            }
        }


       /* AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
*/
        int clsStdCount = studentService.numberOfClassStudent(className, groupName, shiftExam.academicYear)
        int clsPassCount = tabulationService.classPassStudentCount(shiftExam, className, groupName)
        int clsFailCount = clsStdCount - clsPassCount


        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('examName', shiftExam.examName)
        paramsMap.put('groupName', groupName ? groupName.value : "General")
        paramsMap.put('clsStdCount', clsStdCount)
        paramsMap.put('clsFailCount', clsFailCount)
        paramsMap.put('clsPassCount', clsPassCount)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "MERIT_LIST"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def meritListCt(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)

        Exam exam
        if (params.examId){
            exam = Exam.read(params.getLong('examId'))
        }
     /*   if(!shiftExam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }*/
        ClassName className = ClassName.read(params.getLong('classname'))

        String sqlParam = "exam_id in ( select exam.id from exam inner join section on exam.section_id = section.id where exam.active_status ='ACTIVE' and exam.shift_exam_id = ${id}"

        String reportFileName
        GroupName groupName
        if (params.examId){
            reportFileName =schoolService.reportFileName(CommonUtils.MODULE_CT,"ctMeritListReport")
            sqlParam += " and exam.section_id = ${exam.section.id} )"

            if (params.sortBy =="rollNo") {
                sqlParam += " order by sec.id, st.roll_no "
            } else if (params.sortBy =="secMeritPos") {
                sqlParam += " order by sec.id, ta.position_in_section "
            } else {
                sqlParam += " order by ta.position_in_class "
            }
        } else {
            reportFileName =schoolService.reportFileName(CommonUtils.MODULE_CT,"ctMeritListReportByClass")
            if (params.groupName) {
                groupName = GroupName.valueOf(params.groupName)
                sqlParam += " and section.group_name = '"+params.groupName+"'"
            }
            sqlParam += " and exam.class_name_id = ${params.classname} )"

            if (params.sortBy =="rollNo") {
                sqlParam += " order by sec.id, st.roll_no "
            } else if (params.sortBy =="secMeritPos") {
                sqlParam += " order by sec.id, ta.position_in_section "
            } else {
                sqlParam += " order by ta.position_in_class "
            }
        }


        /* AcademicYear academicYear = AcademicYear.valueOf(params.academicYear)
 */
        int clsStdCount = studentService.numberOfClassStudent(className, groupName, shiftExam.academicYear)
        int clsPassCount = 0 //tabulationService.classPassStudentCount(shiftExam, className, groupName)
        int clsFailCount = 0 //clsStdCount - clsPassCount


        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('examName', shiftExam.examName)
        paramsMap.put('groupName', groupName ? groupName.value : "General")
        paramsMap.put('clsStdCount', clsStdCount)
        paramsMap.put('clsFailCount', clsFailCount)
        paramsMap.put('clsPassCount', clsPassCount)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "MERIT_LIST"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def promotionList(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)

        if(!shiftExam || shiftExam.examTerm != ExamTerm.FINAL_TEST){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName className = ClassName.read(params.getLong('classname'))

        def nextClasses = classNameService.nextClasses(className)
        if(!nextClasses){
            flash.message = "Nex class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }
        ClassName nextClass = nextClasses.first()
        if(!nextClass){
            flash.message = "Nex class not found"
            render(view: '/baseReport/reportNotFound')
            return
        }
        String sqlParam = " and exam_id in ( select id from exam where active_status ='ACTIVE' and shift_exam_id = ${id} and class_name_id=$className.id)"

        String reportFileName ="meritPromotionList"

        String reportLogo = schoolService.reportLogoPath()

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('examName', shiftExam.examName)
        paramsMap.put('oldClsName', className.name)
        paramsMap.put('nextClassName', nextClass?.name)


        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "promotion_list"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def sectionReportCard(Long id){
        ShiftExam shiftExam = ShiftExam.read(id)
        Exam exam
        if (params.examId){
            exam = Exam.read(params.getLong('examId'))
        }
        if(!shiftExam || !exam){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }
        if(exam.examStatus == ExamStatus.NEW){
            flash.message ="Result not ready"
            render(view: '/baseReport/reportNotFound')
            return
        }
        Section section = exam.section
        ClassName className = exam.className
        int numberOfSubject = exam.numberOfSubject  //subIds.size()
        String nextClassName

        String reportFileName
        String reportQuery
        String runningSchool = schoolService.runningSchool()
        String termPercentage

        String stdIDs
        if(params.student){
            stdIDs = params.student
        } else {
            def studentList = studentService.sectionStudentIdsList(section)
            if(!studentList){
                flash.message ="No student found for the exam."
                render(view: '/baseReport/reportNotFound')
                return
            }
            stdIDs=studentList.join(",")
        }

        if(exam.examTerm==ExamTerm.FINAL_TEST){
            if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL) {
                def nextClasses = classNameService.nextClasses(className)
                if(nextClasses){
                    nextClassName = nextClasses.first().name
                }

                reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_10LESS_JASPER_FILE)
                if(numberOfSubject >= 10){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_10PLUS_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                if (className.subjectGroup){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_WITH_GROUP_JASPER_FILE)
                } else {
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_NO_GROUP_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else if (runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL) {
                if (className.subjectGroup){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_NO_GROUP_JASPER_FILE)
                } else {
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_NO_GROUP_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
                if (className.subjectGroup){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_WITH_GROUP_JASPER_FILE)
                } else {
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_NO_GROUP_JASPER_FILE)
                    if (className.sectionType == 'High'){
                        reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_HIGH_NO_GROUP_JASPER_FILE)
                    }
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else {
                int numOfPreExam = examService.numberOfTermExam(section)
                termPercentage = "10"
                if (numOfPreExam > 1) {
                    termPercentage = "5"
                }
                reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_10LESS_JASPER_FILE)
                if(numberOfSubject>10 && numberOfSubject<14){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_10PLUS_JASPER_FILE)
                }else if(numberOfSubject>=14){
                    reportFileName=schoolService.reportFileName(FINAL_REPORT_CARD_14ANDPLUS_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            }
        } else {
            if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL) {
                reportFileName = schoolService.reportFileName(TERM_REPORT_CARD_10LESS_JASPER_FILE)
                if(numberOfSubject >= 10){
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_10PLUS_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
                if (className.subjectGroup){
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_WITH_GROUP_JASPER_FILE)
                } else {
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_NO_GROUP_JASPER_FILE)
                    if(numberOfSubject>=14){
                        reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_10PLUS_JASPER_FILE)
                    }
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
                if (className.subjectGroup){
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_WITH_GROUP_JASPER_FILE)
                } else {
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_NO_GROUP_JASPER_FILE)
                    if (className.sectionType == 'High'){
                        reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_HIGH_NO_GROUP_JASPER_FILE)
                    }
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            } else {
                reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_10LESS_JASPER_FILE)
                if(numberOfSubject>10 && numberOfSubject<14){
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_10PLUS_JASPER_FILE)
                }else if(numberOfSubject>=14){
                    reportFileName=schoolService.reportFileName(TERM_REPORT_CARD_14ANDPLUS_JASPER_FILE)
                }
                reportQuery = getProgressReportCardQuery(runningSchool, exam.id, stdIDs)
            }
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('examId', exam.id)
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.schoolSubreportDir())
        paramsMap.put('className', className.name)
        paramsMap.put('classNameId', className.id)
        paramsMap.put('sectionName', section.name)
        paramsMap.put('groupName', section.groupName?.key)
        paramsMap.put('secTeacher', section?.employee?.name)
        paramsMap.put('academicYear', exam.academicYear.value)
        paramsMap.put('examName', shiftExam?.examName)
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('resultPublishOn', CommonUtils.getUiDateStr(shiftExam.resultPublishOn))
        paramsMap.put('workingDays', className.workingDays)
        paramsMap.put('reportQuery', reportQuery)
        paramsMap.put('numberOfSubject', numberOfSubject%2 == 0 ? numberOfSubject/2 : (numberOfSubject + 1)/2)
        paramsMap.put('durationStr', CommonUtils.getDateRangeStr(shiftExam.periodStart, shiftExam.periodEnd))
        if (runningSchool == CommonUtils.BAILY_SCHOOL) {
            paramsMap.put('termPercentage', termPercentage)
        }
        if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL){
            paramsMap.put('nextClassName', nextClassName)
        }

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Progress_Report_${shiftExam.examName}_${section.name}"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def examScheduleBySection(ExamScheduleBySectionCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message = errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }
        AcademicYear workingYear = schoolService.workingYear(command.exam?.className)
        String examIds
        ShiftExam shiftExam
        AcademicYear academicYear
        ExamTerm examTerm
        if(command.shift && command.examTerm){
            shiftExam= ShiftExam.findByShiftAndExamTermAndAcademicYear(command.shift,command.examTerm, workingYear)
            if(!shiftExam){
                flash.message = "No Schedule available for ${command.shift.value} ${command.examTerm.value} exam."
                render(view: '/baseReport/reportNotFound')
                return
            }

            def exams = Exam.findAllByShiftExam(shiftExam)
            if(!exams){
                flash.message = "No Schedule available for ${shiftExam} ${command.examType.value}."
                render(view: '/baseReport/reportNotFound')
                return
            }
            def examLdList = exams.collect {it.id}
            examIds = examLdList.join(',')
            academicYear=shiftExam.academicYear
            examTerm = command.examTerm
        } else if(command.exam){
            Exam exam = command.exam
            examIds = exam.id.toString()
            shiftExam = exam.shiftExam
            academicYear = exam.academicYear
            examTerm = exam.examTerm
        } else {
            flash.message = "No Schedule available."
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportFileName = EXAM_SCHEDULE_BY_SECTION_HALL_JASPER_FILE
        if(command.examType==ExamType.CLASS_TEST){
            reportFileName = EXAM_SCHEDULE_BY_SECTION_CT_JASPER_FILE
        }
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('academicYear', academicYear.value)
        paramsMap.put('examTerm', examTerm.value)
        paramsMap.put('resultPublishOn', shiftExam.resultPublishOn)
        paramsMap.put('examId', examIds)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "section_schedule"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat("PDF")
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

    def classSchedule(Long id){
        Section section = Section.read(id)
        if(!section){
            flash.message =CommonUtils.COMMON_NOT_FOUND_MESSAGE
            render(view: '/baseReport/reportNotFound')
            return
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sectionId', section.id)
        paramsMap.put('sectionName', section.name)
        paramsMap.put('secTeacher', section?.employee?.name)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Class_Schedule"
        jasperReport.reportName = CLASS_SCHEDULE_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat("PDF")
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
    def subjectStudent(){
        Long className = params.getLong('className')
        Long section = params.getLong('section')
        Long subjectName = params.getLong('subjectName')
        String printOptionType = params.printOptionType

        String sqlQuery = " select class_name clsName, section_name secName, student_name stdName, studentid, roll_no, subject_name\n" +
                "from v_std_student_subject\n" +
                "where class_name_id =${className} and subject_id = ${subjectName}"
        if (section) {
            sqlQuery += " and section_id=${section}"
        }
        sqlQuery += " order by section_name asc, roll_no asc"
        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))
        paramsMap.put('creditLine', grailsApplication.config.getProperty("company.credit.footer"))
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('sqlQueryParam', sqlQuery)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "StudentList"
        jasperReport.reportName = SUBJECT_STUDENT_LIST_JASPER_FILE
        jasperReport.reportFormat = baseService.printFormat(printOptionType)
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

    def markEntryStatus(Long id){
        ShiftExam shiftExam= ShiftExam.read(id)
        Exam exam = Exam.read(params.getLong('exam'))
        String className = exam.className.name
        String sectionName = exam.section.name

        String reportFileName
        String sqlParams
        if (!shiftExam.ctExam) {
//            and student_id in (select std_id from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id)
            sqlParams = " select ex.name exam, sn.name subject, exs.is_ct_exam, (select count(*) from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id) numOfStd, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.hall_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') hallEntryCount from exam_schedule exs inner join exam ex on ex.id= exs.exam_id  and ex.active_status='ACTIVE' inner join subject_name sn on sn.id = exs.subject_id where ex.shift_exam_id = ${shiftExam.id} and exs.exam_id=${exam.id}  and exs.active_status='ACTIVE'; "
            reportFileName = EXAM_MARK_ENTRY_STATUS_JASPER_FILE
        } else if (shiftExam.ctExam == 1) {
//            and student_id in (select std_id from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id)
            sqlParams = " select ex.name exam, sn.name subject, exs.is_ct_exam, (select count(*) from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id) numOfStd, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ctEntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.hall_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') hallEntryCount from exam_schedule exs inner join exam ex on ex.id= exs.exam_id  and ex.active_status='ACTIVE' inner join subject_name sn on sn.id = exs.subject_id where ex.shift_exam_id = ${shiftExam.id} and exs.exam_id=${exam.id}  and exs.active_status='ACTIVE'; "
            reportFileName = EXAM_MARK_ENTRY_STATUS_1CT_JASPER_FILE
        } else if (shiftExam.ctExam == 2) {
            sqlParams = " select ex.name exam, sn.name subject, exs.is_ct_exam, (select count(*) from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id) numOfStd, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ctEntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct2status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ct2EntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.hall_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') hallEntryCount from exam_schedule exs inner join exam ex on ex.id= exs.exam_id  and ex.active_status='ACTIVE' inner join subject_name sn on sn.id = exs.subject_id where ex.shift_exam_id = ${shiftExam.id} and exs.exam_id=${exam.id}  and exs.active_status='ACTIVE'; "
            reportFileName = EXAM_MARK_ENTRY_STATUS_2CT_JASPER_FILE
        } else if (shiftExam.ctExam == 3) {
            sqlParams = " select ex.name exam, sn.name subject, exs.is_ct_exam, (select count(*) from v_std_student_subject where section_id = ex.section_id and subject_id=sn.id) numOfStd, (select count(em.id) from exam_mark  em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ctEntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct2status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ct2EntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.ct3status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') ct3EntryCount, (select count(em.id) from exam_mark em inner join student st on em.student_id = st.id where em.exam_id=ex.id and em.exam_schedule_id=exs.id and em.hall_attend_status !='NO_INPUT' and em.active_status='ACTIVE' and st.student_status ='NEW') hallEntryCount from exam_schedule exs inner join exam ex on ex.id= exs.exam_id  and ex.active_status='ACTIVE' inner join subject_name sn on sn.id = exs.subject_id where ex.shift_exam_id = ${shiftExam.id} and exs.exam_id=${exam.id}  and exs.active_status='ACTIVE'; "
            reportFileName = EXAM_MARK_ENTRY_STATUS_3CT_JASPER_FILE
        }


        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('className', className)
        paramsMap.put('sectionName', sectionName)
        paramsMap.put('sqlParams', sqlParams)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "markEntryStatus"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(params.printOptionType)
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

    def teacherAnalysis(){
        String academicYear = params.academicYear
        Long shiftExamId = params.getLong('examName')
        ShiftExam shiftExam = ShiftExam.read(shiftExamId)
        String examType = params.examType
        String printOptionType = params.printOptionType
        AcademicYear academicYearObj = AcademicYear.valueOf(academicYear)
        List referenceList = new ArrayList()
        def teacherList = employeeService.teacherDropDownList()
        List routineList

        String subjectName
        String sectionName
        String subjectStudent
        String subjectaPlus
        String subjecta
        String subjectFail



        int maxList = 0
        def routine
        Long examId
        TeacherAnalysisReference aReference
        Long sectionId
        Long subjectId
        Integer subjectStudentCount
        Integer aPlusStudentCount
        Integer passStudentCount
        Integer failStudentCount
        for (def teacherObj: teacherList) {
            routineList = classRoutineService.teacherRoutineForAnalysis(academicYear, teacherObj.id)
            if (routineList && routineList.size() > 0) {
                aReference = new TeacherAnalysisReference(teacherObj.name)
                maxList = routineList.size() > 8 ? 8 : routineList.size()
                for (int i = 0; i < maxList; i++) {
                    routine = routineList.get(i)
                    sectionId = routine.sectionId
                    subjectId = routine.subjectId
                    if (examType == "HALL_TEST") {
                        examId = examService.getHallExamId(shiftExamId, sectionId)
                        subjectStudentCount = examMarkService.getHallAttendCount(examId, subjectId)
                        if (subjectStudentCount > 0) {
                            aPlusStudentCount  = examMarkService.getHallAplusCount(examId, subjectId)
                            passStudentCount  = examMarkService.getHallPassCount(examId, subjectId)
                            failStudentCount  = subjectStudentCount - passStudentCount
                        } else {
                            aPlusStudentCount  = 0
                            passStudentCount  = 0
                            failStudentCount  = 0
                        }
                    } else {
                        examId = examService.getCtExamId(shiftExamId, sectionId)
                        subjectStudentCount = examMarkService.getCtAttendCount(examId, subjectId)
                        if (subjectStudentCount > 0) {
                            aPlusStudentCount  = 0
                            passStudentCount  = examMarkService.getCtPassCount(examId, subjectId)
                            failStudentCount  = subjectStudentCount - passStudentCount
                        } else {
                            aPlusStudentCount  = 0
                            passStudentCount  = 0
                            failStudentCount  = 0
                        }
                    }
                    subjectName = "subject${i}name"
                    sectionName = "section${i}name"
                    subjectStudent = "subject${i}student"
                    subjectaPlus = "subject${i}aPlus"
                    subjecta = "subject${i}a"
                    subjectFail = "subject${i}fail"
                    aReference."$subjectName" = routine.subjectName
                    aReference."$sectionName" = routine.clsSec

                    aReference."$subjectStudent" = subjectStudentCount
                    aReference."$subjectaPlus" = aPlusStudentCount
                    aReference."$subjecta" = passStudentCount
                    aReference."$subjectFail" = failStudentCount
                }
                referenceList.add(aReference)
            }
        }

        String reportLogo = schoolService.reportLogoPath()
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('academicYear', academicYearObj.value)
        paramsMap.put('examName', "half yearly")
        paramsMap.put('examType', examType)
        paramsMap.put('className', "One")
        paramsMap.put('groupName', "A")

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "Teacher_Analysis"
        jasperReport.reportName = TEACHER_RESULT_ANALYSIS_SUMMERY
        jasperReport.reportFormat = baseService.printFormat(printOptionType)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.reportData = referenceList
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
    def resultAnalysis(ResultAnalysisCommand command){
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            flash.message =errorList?.join('. ')
            render(view: '/baseReport/reportNotFound')
            return
        }

        def publishingExamList
        if (command.examId) {
            publishingExamList = new ArrayList()
            publishingExamList.add(command.examId)
        } else {
            publishingExamList = examService.publishingExamList(command.shift, command.examName, command.examType, command.className, command.groupName)
        }

        def sectionList

        String reportFileName

        List referenceList = new ArrayList()

        if (command.analysisReportType == "summaryReport") {
            reportFileName = EXAMINATION_RESULT_ANALYSIS_SUMMERY
            Integer maleStudentCount = 0
            Integer malePassCount = 0
            Integer maleaPlusCount = 0
            Integer maleaCount = 0
            Integer maleaMinusCount = 0
            Integer malebCount = 0
            Integer malecCount = 0

            sectionList = publishingExamList.collect {it.section}
            def maleStudentIds = studentService.studentIdsByGender(command.academicYear, sectionList, null, null, Gender.MALE)
            if (command.examType == ExamType.HALL_TEST) {
                maleStudentCount = tabulationService.hallStudentCount(publishingExamList, maleStudentIds, null, null)
                if (maleStudentCount > 0) {
                    malePassCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, null)
                    maleaPlusCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                    maleaCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                    maleaMinusCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                    malebCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                    malecCount = tabulationService.hallStudentCount( publishingExamList, maleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                }
            }


            Integer maleFailCount = maleStudentCount - malePassCount
            Integer malefCount = maleFailCount

            Integer femaleStudentCount = 0
            Integer femalePassCount = 0
            Integer femaleaPlusCount = 0
            Integer femaleaCount = 0
            Integer femaleaMinusCount = 0
            Integer femalebCount = 0
            Integer femalecCount = 0

            def femaleStudentIds = studentService.studentIdsByGender(command.academicYear, sectionList, null, null, Gender.FEMALE)
            if (command.examType == ExamType.HALL_TEST) {
                femaleStudentCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, null, null)
                if (maleStudentCount > 0) {
                    femalePassCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, null)
                    femaleaPlusCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                    femaleaCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                    femaleaMinusCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                    femalebCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                    femalecCount = tabulationService.hallStudentCount( publishingExamList, femaleStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                }
            }
            Integer femaleFailCount = femaleStudentCount - femalePassCount
            Integer femalefCount = femaleFailCount

            Integer studentCount = maleStudentCount + femaleStudentCount
            Integer passCount = malePassCount + femalePassCount
            Integer failCount = studentCount - passCount
            Integer aPlusCount = maleaPlusCount + femaleaPlusCount
            Integer aCount = maleaCount + femaleaCount
            Integer aMinusCount = maleaMinusCount + femaleaMinusCount
            Integer bCount = malebCount + femalebCount
            Integer cCount = malecCount + femalecCount
            Integer fCount = failCount

            //by Religion
            Integer muslimStudentCount = 0
            Integer muslimPassCount = 0
            Integer muslimaPlusCount = 0
            Integer muslimaCount = 0
            Integer muslimaMinusCount = 0
            Integer muslimbCount = 0
            Integer muslimcCount = 30

            def muslimStudentIds = studentService.studentIdsByReligion(command.academicYear, sectionList, null, null, Religion.ISLAM)
            if (command.examType == ExamType.HALL_TEST) {
                muslimStudentCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, null, null)
                if (muslimStudentCount > 0) {
                    muslimPassCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, null)
                    muslimaPlusCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                    muslimaCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                    muslimaMinusCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                    muslimbCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                    muslimcCount = tabulationService.hallStudentCount( publishingExamList, muslimStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                }
            }

            Integer muslimFailCount = muslimStudentCount - muslimPassCount
            Integer muslimfCount = muslimFailCount

            Integer hinduStudentCount = 0
            Integer hinduPassCount = 0
            Integer hinduaPlusCount = 0
            Integer hinduaCount = 0
            Integer hinduaMinusCount = 0
            Integer hindubCount = 0
            Integer hinducCount = 0

            def hinduStudentIds = studentService.studentIdsByReligion(command.academicYear, sectionList, null, null, Religion.HINDU)
            if (command.examType == ExamType.HALL_TEST) {
                hinduStudentCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, null, null)
                if (hinduStudentCount > 0) {
                    hinduPassCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, null)
                    hinduaPlusCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                    hinduaCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                    hinduaMinusCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                    hindubCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                    hinducCount = tabulationService.hallStudentCount( publishingExamList, hinduStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                }
            }

            Integer hinduFailCount = hinduStudentCount - hinduPassCount
            Integer hindufCount = hinduFailCount

            Integer otherStudentCount = studentCount - (muslimStudentCount + hinduStudentCount)
            Integer otherPassCount = passCount - (muslimPassCount + hinduPassCount)
            Integer otherFailCount = otherStudentCount - otherPassCount
            Integer otheraPlusCount = aPlusCount - (muslimaPlusCount + hinduaPlusCount)
            Integer otheraCount = aCount - (muslimaCount + hinduaCount)
            Integer otheraMinusCount = aMinusCount - (muslimaMinusCount + hinduaMinusCount)
            Integer otherbCount = bCount - (muslimbCount + hindubCount)
            Integer othercCount = cCount - (muslimcCount + hinducCount)
            Integer otherfCount = otherFailCount

            ResultAnalysisReference analysisReference = new ResultAnalysisReference(studentCount, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount,
                    maleStudentCount, malePassCount, maleFailCount, maleaPlusCount, maleaCount, maleaMinusCount, malebCount, malecCount, malefCount,
                    femaleStudentCount, femalePassCount, femaleFailCount, femaleaPlusCount, femaleaCount, femaleaMinusCount, femalebCount, femalecCount, femalefCount,
                    muslimStudentCount, muslimPassCount, muslimFailCount, muslimaPlusCount, muslimaCount, muslimaMinusCount, muslimbCount, muslimcCount, muslimfCount,
                    hinduStudentCount, hinduPassCount, hinduFailCount, hinduaPlusCount, hinduaCount, hinduaMinusCount, hindubCount, hinducCount, hindufCount,
                    otherStudentCount, otherPassCount, otherFailCount, otheraPlusCount, otheraCount, otheraMinusCount, otherbCount, othercCount, otherfCount)
            referenceList.add(analysisReference)

        } else if (command.analysisReportType == "byClass") {
            reportFileName = EXAMINATION_RESULT_ANALYSIS_BYCLASS

            String reportType
            String className
            String sectionName

            Integer passCount
            Integer failCount
            Integer aPlusCount
            Integer aCount
            Integer aMinusCount
            Integer bCount
            Integer cCount
            Integer numberOfStudent
            Integer fCount
            def analysisStudentIds
            Gender gender
            Religion religion
            for (examItem in publishingExamList) {
                reportType = "All"
                className = examItem.className.name
                sectionName = examItem.section.name
                gender = null
                religion = null
                analysisStudentIds = studentService.studentListBySection(command.academicYear, examItem.section, gender, religion)
                numberOfStudent = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, null, null)
                passCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, null)
                failCount = numberOfStudent - passCount
                aPlusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                aCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                aMinusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                bCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                cCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                fCount = failCount
                referenceList.add(new ResultClassReference(reportType, className, sectionName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))

                reportType = "Male"
                gender = Gender.MALE
                analysisStudentIds = studentService.studentListBySection(command.academicYear, examItem.section, gender, religion)
                numberOfStudent = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, null, null)
                passCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, null)
                failCount = numberOfStudent - passCount
                aPlusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                aCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                aMinusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                bCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                cCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                fCount = failCount
                referenceList.add(new ResultClassReference(reportType, className, sectionName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))

                reportType = "Female"
                gender = Gender.FEMALE
                analysisStudentIds = studentService.studentListBySection(command.academicYear, examItem.section, gender, religion)
                numberOfStudent = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, null, null)
                passCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, null)
                failCount = numberOfStudent - passCount
                aPlusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                aCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                aMinusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                bCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                cCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                fCount = failCount
                referenceList.add(new ResultClassReference(reportType, className, sectionName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))

                reportType = "Muslim"
                gender = null
                religion = Religion.ISLAM
                analysisStudentIds = studentService.studentListBySection(command.academicYear, examItem.section, gender, religion)
                numberOfStudent = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, null, null)
                passCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, null)
                failCount = numberOfStudent - passCount
                aPlusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                aCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                aMinusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                bCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                cCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                fCount = failCount
                referenceList.add(new ResultClassReference(reportType, className, sectionName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))

                reportType = "Hindu"
                gender = null
                religion = Religion.HINDU
                analysisStudentIds = studentService.studentListBySection(command.academicYear, examItem.section, gender, religion)
                numberOfStudent = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, null, null)
                passCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, null)
                failCount = numberOfStudent - passCount
                aPlusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_PLUS.value)
                aCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A.value)
                aMinusCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_A_MINUS.value)
                bCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_B.value)
                cCount = tabulationService.hallStudentCount( publishingExamList, analysisStudentIds, ResultStatus.PASSED, LetterGrade.GRADE_C.value)
                fCount = failCount
                referenceList.add(new ResultClassReference(reportType, className, sectionName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
            }
        } else {
            reportFileName = EXAMINATION_RESULT_ANALYSIS_BYGRADE

            String examSubjectIds
            if (command.examType == ExamType.HALL_TEST) {
                if (command.subjectName) {
                    examSubjectIds = command.subjectName.id.toString()
                }else {
                    examSubjectIds = publishingExamList.collect {it.subjectIds}.join(",")
                }
                def subjectList = subjectService.allSubjectInList(examSubjectIds)

                String reportType
                String subjectName
                Integer numberOfStudent
                Integer passCount
                Integer failCount
                Integer aPlusCount
                Integer aCount
                Integer aMinusCount
                Integer bCount
                Integer cCount
                Integer fCount

                sectionList = publishingExamList.collect {it.section}
                def allStudentIds = studentService.listStudentForAnalysis(command.academicYear, sectionList, null, null)
                def maleStudentIds = studentService.listStudentForAnalysis(command.academicYear, sectionList, Gender.MALE, null)
                def femaleStudentIds = studentService.listStudentForAnalysis(command.academicYear, sectionList, Gender.FEMALE, null)
                def muslimStudentIds = studentService.listStudentForAnalysis(command.academicYear, sectionList, null, Religion.ISLAM)
                def hinduStudentIds = studentService.listStudentForAnalysis(command.academicYear, sectionList, null, Religion.HINDU)
                for (subjectNam in subjectList) {
                     reportType = "All"
                     subjectName = subjectNam.name
                     numberOfStudent = examMarkService.getExamSubjectResultCount(publishingExamList, allStudentIds, subjectNam, null)
                    if (numberOfStudent > 0){
                        passCount = examMarkService.getExamSubjectResultCount(publishingExamList, allStudentIds, subjectNam, ResultStatus.PASSED)
                        failCount = numberOfStudent - passCount
                        aPlusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, allStudentIds, subjectNam, LetterGrade.GRADE_A_PLUS.value)
                        aCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, allStudentIds, subjectNam, LetterGrade.GRADE_A.value)
                        aMinusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, allStudentIds, subjectNam, LetterGrade.GRADE_A_MINUS.value)
                        bCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, allStudentIds, subjectNam, LetterGrade.GRADE_B.value)
                        cCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, allStudentIds, subjectNam, LetterGrade.GRADE_C.value)
                        fCount = failCount
                        referenceList.add(new ResultSubjectReference(reportType, subjectName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
                    }


                    reportType = "Male"
                    subjectName = subjectNam.name
                    numberOfStudent = examMarkService.getExamSubjectResultCount(publishingExamList, maleStudentIds, subjectNam, null)
                    if (numberOfStudent > 0){
                        passCount = examMarkService.getExamSubjectResultCount(publishingExamList, maleStudentIds, subjectNam, ResultStatus.PASSED)
                        failCount = numberOfStudent - passCount
                        aPlusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, maleStudentIds, subjectNam, LetterGrade.GRADE_A_PLUS.value)
                        aCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, maleStudentIds, subjectNam, LetterGrade.GRADE_A.value)
                        aMinusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, maleStudentIds, subjectNam, LetterGrade.GRADE_A_MINUS.value)
                        bCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, maleStudentIds, subjectNam, LetterGrade.GRADE_B.value)
                        cCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, maleStudentIds, subjectNam, LetterGrade.GRADE_C.value)
                        fCount = failCount
                        referenceList.add(new ResultSubjectReference(reportType, subjectName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
                    }

                    reportType = "Female"
                    subjectName = subjectNam.name
                    numberOfStudent = examMarkService.getExamSubjectResultCount(publishingExamList, femaleStudentIds, subjectNam, null)
                    if (numberOfStudent > 0) {
                        passCount = examMarkService.getExamSubjectResultCount(publishingExamList, femaleStudentIds, subjectNam, ResultStatus.PASSED)
                        failCount = numberOfStudent - passCount
                        aPlusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, femaleStudentIds, subjectNam, LetterGrade.GRADE_A_PLUS.value)
                        aCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, femaleStudentIds, subjectNam, LetterGrade.GRADE_A.value)
                        aMinusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, femaleStudentIds, subjectNam, LetterGrade.GRADE_A_MINUS.value)
                        bCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, femaleStudentIds, subjectNam, LetterGrade.GRADE_B.value)
                        cCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, femaleStudentIds, subjectNam, LetterGrade.GRADE_C.value)
                        fCount = failCount
                        referenceList.add(new ResultSubjectReference(reportType, subjectName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
                    }

                    reportType = "Muslim"
                    subjectName = subjectNam.name
                    numberOfStudent = examMarkService.getExamSubjectResultCount(publishingExamList, muslimStudentIds, subjectNam, null)
                    if (numberOfStudent > 0){
                        passCount = examMarkService.getExamSubjectResultCount(publishingExamList, muslimStudentIds, subjectNam, ResultStatus.PASSED)
                        failCount = numberOfStudent - passCount
                        aPlusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, muslimStudentIds, subjectNam, LetterGrade.GRADE_A_PLUS.value)
                        aCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, muslimStudentIds, subjectNam, LetterGrade.GRADE_A.value)
                        aMinusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, muslimStudentIds, subjectNam, LetterGrade.GRADE_A_MINUS.value)
                        bCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, muslimStudentIds, subjectNam, LetterGrade.GRADE_B.value)
                        cCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, muslimStudentIds, subjectNam, LetterGrade.GRADE_C.value)
                        fCount = failCount
                        referenceList.add(new ResultSubjectReference(reportType, subjectName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
                    }


                    reportType = "Hindu"
                    subjectName = subjectNam.name
                    numberOfStudent = examMarkService.getExamSubjectResultCount(publishingExamList, hinduStudentIds, subjectNam, null)
                    if (numberOfStudent > 0) {
                        passCount = examMarkService.getExamSubjectResultCount(publishingExamList, hinduStudentIds, subjectNam, ResultStatus.PASSED)
                        failCount = numberOfStudent - passCount
                        aPlusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, hinduStudentIds, subjectNam, LetterGrade.GRADE_A_PLUS.value)
                        aCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, hinduStudentIds, subjectNam, LetterGrade.GRADE_A.value)
                        aMinusCount = examMarkService.getExamSubjectLetterGradeCount(publishingExamList, hinduStudentIds, subjectNam, LetterGrade.GRADE_A_MINUS.value)
                        bCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, hinduStudentIds, subjectNam, LetterGrade.GRADE_B.value)
                        cCount= examMarkService.getExamSubjectLetterGradeCount(publishingExamList, hinduStudentIds, subjectNam, LetterGrade.GRADE_C.value)
                        fCount = failCount
                        referenceList.add(new ResultSubjectReference(reportType, subjectName, numberOfStudent, passCount, failCount, aPlusCount, aCount, aMinusCount, bCount, cCount, fCount))
                    }
                }
            }
        }



        String reportLogo = schoolService.reportLogoPath()
        Section section = command.examId?.section
        String shiftName = ""
        if (command.shift) {
            shiftName = " ($command.shift.value)"
        }
        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', reportLogo)
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('academicYear', command.academicYear?.value)
        paramsMap.put('examName', command.examName?.examName)
        paramsMap.put('examType', command.examType?.value)
        paramsMap.put('className', command.className?.name)
        paramsMap.put('groupName', command.groupName?.value)
        paramsMap.put('sectionName', section?.name)
        paramsMap.put('subjectName', command.subjectName?.name)
        paramsMap.put('religionName', null)
        paramsMap.put('genderName', null)
        paramsMap.put('shiftName', shiftName)

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "EXAMINATION_RESULT_ANALYSIS"
        jasperReport.reportName = reportFileName
        jasperReport.reportFormat = baseService.printFormat(command.printOptionType?.key)
        jasperReport.reportDir = schoolService.subreportDir()
        jasperReport.parameters = paramsMap
        jasperReport.reportData = referenceList
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

    //for report card
    private static final String FINAL_REPORT_CARD_14ANDPLUS_JASPER_FILE = 'finalReportCard14Andplus'
    private static final String FINAL_REPORT_CARD_10PLUS_JASPER_FILE = 'finalReportCard10plus'
    private static final String FINAL_REPORT_CARD_10LESS_JASPER_FILE = 'finalReportCard10less'

    private static final String TERM_REPORT_CARD_14ANDPLUS_JASPER_FILE = 'termReportCard14Andplus'
    private static final String TERM_REPORT_CARD_10PLUS_JASPER_FILE = 'termReportCard10plus'
    private static final String TERM_REPORT_CARD_10LESS_JASPER_FILE = 'termReportCard10less'
    private static final String TERM_REPORT_CARD_NO_GROUP_JASPER_FILE = 'termReportCardNoGroup'
    private static final String TERM_REPORT_CARD_HIGH_NO_GROUP_JASPER_FILE = 'termReportCardNoGroup_high'
    private static final String FINAL_REPORT_CARD_NO_GROUP_JASPER_FILE = 'finalReportCardNoGroup'
    private static final String FINAL_REPORT_CARD_HIGH_NO_GROUP_JASPER_FILE = 'finalReportCardNoGroup_high'
    private static final String TERM_REPORT_CARD_WITH_GROUP_JASPER_FILE = 'termReportCardWithGroup'
    private static final String FINAL_REPORT_CARD_WITH_GROUP_JASPER_FILE = 'finalReportCardWithGroup'

    //tabulation
    private static final String CT_TERM_TABULATION_9_JASPER_FILE = 'termCtTabulation9'

    private static final String EXAM_SCHEDULE_BY_SECTION_CT_JASPER_FILE = 'sectionCTExamSchedule'
    private static final String EXAM_SCHEDULE_BY_SECTION_HALL_JASPER_FILE = 'sectionHallExamSchedule'

    //for Class Schedule
    private static final String CLASS_SCHEDULE_JASPER_FILE = 'classSchedule'
    //common report variables

    private static final String SUBJECT_STUDENT_LIST_JASPER_FILE = 'subjectStudentMarkEntryList'
    private static final String EXAM_MARK_ENTRY_STATUS_JASPER_FILE = 'examMarkEntryStatus'
    private static final String EXAM_MARK_ENTRY_STATUS_1CT_JASPER_FILE = 'examMarkEntryStatus1ct'
    private static final String EXAM_MARK_ENTRY_STATUS_2CT_JASPER_FILE = 'examMarkEntryStatus2ct'
    private static final String EXAM_MARK_ENTRY_STATUS_3CT_JASPER_FILE = 'examMarkEntryStatus3ct'


    // for result analysis
    private static final String TEACHER_RESULT_ANALYSIS_SUMMERY = 'teacherAnalysisReport'
    private static final String EXAMINATION_RESULT_ANALYSIS_SUMMERY = 'examAnalysisReport'
    private static final String EXAMINATION_RESULT_ANALYSIS_BYCLASS = 'examAnalysisReportByClass'
    private static final String EXAMINATION_RESULT_ANALYSIS_BYGRADE = 'examAnalysisReportByGrade'

    String getProgressReportCardQuery(String runningSchool, Long examId, String stdIDs) {
        String querySql = """
            select std.id as id, std.studentid, std.name, std.roll_no, tabu.attendance_day attend_day,
            tabu.result_status, tabu.grand_total_mark, tabu.g_point, tabu.l_grade, tabu.section_str_position, tabu.class_str_position, 
            sn.name subject, emark.ct_attend_status, exsch.ct_exam_mark as ct_exam_mark, exsch.ct_mark_eff_percentage ct_mark_eff_percentage, 
            emark.ct_obtain_mark, emark.ct2obtain_mark, emark.ct3obtain_mark, emark.ct_mark cmark,
            emark.hall_attend_status, exsch.hall_exam_mark as hall_exam_mark, exsch.hall_mark_eff_percentage hall_mark_eff_percentage, emark.hall_mark as hmark,
            emark.is_optional, exsch.hall_written_mark, exsch.hall_objective_mark, exsch.hall_practical_mark, exsch.hall_sba_mark, emark.hall_input5,
            emark.tabulation_mark as oTotalMark, exsch.tabulation_eff_percentage tabulation_eff_percentage,
            emark.g_point as gPoint, emark.l_grade as lGrade, exsch.highest_mark, tabu.total_obtain_mark as tom,
            tabu.term1mark + tabu.term2mark as termMark, tabu.attendance_percent,tabu.attendance_mark,
            tabu.total_ct_mark, tabu.term1mark,  tabu.term2mark,  tabu.next_section, tabu.next_roll_no,
            tabu.position_in_section, tabu.position_in_class, emark.idx_of_sub, emark.hall_written_mark as writtenMark,
            emark.hall_objective_mark as objectiveMark,  emark.hall_sba_mark as sbaMark,  emark.hall_practical_mark as practicalMark, emark.hall_input5 as hallInput5,
            IFNULL(emark.hall_written_mark, 0) + IFNULL(emark.hall_objective_mark, 0)+IFNULL(emark.hall_practical_mark, 0) totalNoSba,
            emark.hall_obtain_mark, tabu.attendance_day as presentCount,
            (IFNULL(exsch.hall_exam_mark, 0) * IFNULL(exsch.hall_mark_eff_percentage, 0) * 0.01) as hallEMark, 
            (IFNULL(exsch.ct_exam_mark, 0) * IFNULL(exsch.ct_mark_eff_percentage, 0) * 0.01) as ctEMark, exsch.full_mark full_exam_mark, 
            emark.half_yearly_mark, emark.average_mark, (IFNULL(emark.average_mark, 0) - IFNULL(exsch.average_mark, 0))  as standard_deviation
            from exam_mark emark
            inner join student std on emark.student_id=std.id
            inner join exam_schedule exsch on emark.exam_schedule_id=exsch.id
            inner join tabulation tabu on emark.student_id=tabu.student_id AND emark.exam_id=tabu.exam_id
            inner join subject_name sn on exsch.subject_id = sn.id
            where emark.exam_id=${examId} and exsch.active_status='ACTIVE' and emark.active_status='ACTIVE' and emark.student_id in (${stdIDs})  order by std.roll_no, std.id, exsch.sort_order;
        """
        return querySql
    }

    String getTabulationReportCardQuery(String runningSchool, Long examId, String orderByStr, String examTerm) {
        // for bailyschool - termTabulation
        String queryStrSelect
        if (runningSchool == CommonUtils.BAILY_SCHOOL) {
            queryStrSelect = "select std.name,std.studentid,std.roll_no as rollNo," +
                    "tabu.subject0mark,tabu.subject1mark,tabu.subject2mark,tabu.subject3mark,tabu.subject4mark," +
                    "tabu.subject5mark,tabu.subject6mark,tabu.subject7mark,tabu.subject8mark,tabu.subject9mark," +
                    "tabu.subject10mark,tabu.subject11mark,tabu.subject12mark,tabu.subject13mark,tabu.subject14mark," +
                    "tabu.subject15mark,tabu.subject16mark,tabu.subject17mark, tabu.subject18mark, tabu.subject19mark, tabu.subject20mark, tabu.subject21mark," +
                    "tabu.attendance_mark as attendanceMark,tabu.total_obtain_mark as finalToal,tabu.term1mark," +
                    "tabu.term2mark,tabu.grand_total_mark grandTotal,tabu.section_str_position,tabu.class_str_position," +
                    "tabu.failed_sub_counter,tabu.result_status,tabu.g_point,tabu.l_grade" +
                    " from tabulation tabu" +
                    " inner join student std on std.id=tabu.student_id and std.student_status='NEW'"
        } else if (runningSchool == CommonUtils.NARAYANGANJ_ADARSHA_SCHOOL || runningSchool == CommonUtils.LIGHT_HOUSE_SCHOOL) {
            queryStrSelect = "select std.name,std.studentid,std.roll_no as rollNo," +
                    "tabu.subject0mark,td.subject0comment,tabu.subject1mark,td.subject1comment," +
                    "tabu.subject2mark,td.subject2comment,tabu.subject3mark,td.subject3comment," +
                    "tabu.subject4mark,td.subject4comment,tabu.subject5mark,td.subject5comment," +
                    "tabu.subject6mark,td.subject6comment,tabu.subject7mark,td.subject7comment," +
                    "tabu.subject8mark,td.subject8comment,tabu.subject9mark,td.subject9comment," +
                    "tabu.subject10mark,td.subject10comment,tabu.subject11mark,td.subject11comment," +
                    "tabu.subject12mark,td.subject12comment,tabu.subject13mark,td.subject13comment," +
                    "tabu.attendance_mark, tabu.total_ct_mark," +
                    "tabu.total_obtain_mark as finalToal,tabu.term1mark, tabu.term2mark , tabu.grand_total_mark grandTotal," +
                    "tabu.section_str_position, tabu.class_str_position, tabu.failed_sub_counter, tabu.result_status,tabu.g_point,tabu.l_grade" +
                    " from tabulation tabu" +
                    " inner join tabulation_details td on tabu.id=td.tabulation_id" +
                    " inner join student std on std.id=tabu.student_id and std.student_status='NEW'"
        } else if (runningSchool == CommonUtils.NARAYANGANJ_HIGH_SCHOOL  || runningSchool == CommonUtils.NARAYANGANJ_BIDDA_NIKETON_SCHOOL) {
            queryStrSelect = "select std.name,std.studentid,std.roll_no as rollNo," +
                    "tabu.subject0mark, td.subject0written, td.subject0objective, td.subject0practical, td.subject0Sba, IFNULL(td.subject0written, 0) + IFNULL(td.subject0objective, 0)+IFNULL(td.subject0practical, 0) subject0ht, td.subject0gp, tabu.subject0ct," +
                    "tabu.subject1mark, td.subject1written, td.subject1objective, td.subject1practical, td.subject1Sba, IFNULL(td.subject1written, 0) + IFNULL(td.subject1objective, 0)+IFNULL(td.subject1practical, 0) subject1ht, td.subject1gp, tabu.subject1ct," +
                    "tabu.subject2mark, td.subject2written, td.subject2objective, td.subject2practical, td.subject2Sba, IFNULL(td.subject2written, 0) + IFNULL(td.subject2objective, 0)+IFNULL(td.subject2practical, 0) subject2ht, td.subject2gp, tabu.subject2ct," +
                    "tabu.subject3mark, td.subject3written, td.subject3objective, td.subject3practical, td.subject3Sba, IFNULL(td.subject3written, 0) + IFNULL(td.subject3objective, 0)+IFNULL(td.subject3practical, 0) subject3ht, td.subject3gp, tabu.subject3ct," +
                    "tabu.subject4mark, td.subject4written, td.subject4objective, td.subject4practical, td.subject4Sba, IFNULL(td.subject4written, 0) + IFNULL(td.subject4objective, 0)+IFNULL(td.subject4practical, 0) subject4ht, td.subject4gp, tabu.subject4ct," +
                    "tabu.subject5mark, td.subject5written, td.subject5objective, td.subject5practical, td.subject5Sba, IFNULL(td.subject5written, 0) + IFNULL(td.subject5objective, 0)+IFNULL(td.subject5practical, 0) subject5ht, td.subject5gp, tabu.subject5ct," +
                    "tabu.subject6mark, td.subject6written, td.subject6objective, td.subject6practical, td.subject6Sba, IFNULL(td.subject6written, 0) + IFNULL(td.subject6objective, 0)+IFNULL(td.subject6practical, 0) subject6ht, td.subject6gp, tabu.subject6ct," +
                    "tabu.subject7mark, td.subject7written, td.subject7objective, td.subject7practical, td.subject7Sba, IFNULL(td.subject7written, 0) + IFNULL(td.subject7objective, 0)+IFNULL(td.subject7practical, 0) subject7ht, td.subject7gp, tabu.subject7ct," +
                    "tabu.subject8mark, td.subject8written, td.subject8objective, td.subject8practical, td.subject8Sba, IFNULL(td.subject8written, 0) + IFNULL(td.subject8objective, 0)+IFNULL(td.subject8practical, 0) subject8ht, td.subject8gp, tabu.subject8ct," +
                    "tabu.subject9mark, td.subject9written, td.subject9objective, td.subject9practical, td.subject9Sba, IFNULL(td.subject9written, 0) + IFNULL(td.subject9objective, 0)+IFNULL(td.subject9practical, 0) subject9ht, td.subject9gp, tabu.subject9ct," +
                    "tabu.subject10mark, td.subject10written, td.subject10objective, td.subject10practical, td.subject10Sba, IFNULL(td.subject10written, 0) + IFNULL(td.subject10objective, 0)+IFNULL(td.subject10practical, 0) subject10ht, td.subject10gp, tabu.subject10ct," +
                    "tabu.subject11mark, td.subject11written, td.subject11objective, td.subject11practical, td.subject11Sba, IFNULL(td.subject11written, 0) + IFNULL(td.subject11objective, 0)+IFNULL(td.subject11practical, 0) subject11ht, td.subject11gp, tabu.subject11ct," +
                    "tabu.subject12mark, td.subject12written, td.subject12objective, td.subject12practical, td.subject12Sba, IFNULL(td.subject12written, 0) + IFNULL(td.subject12objective, 0)+IFNULL(td.subject12practical, 0) subject12ht, td.subject12gp, tabu.subject12ct," +
                    "tabu.subject13mark, td.subject13written, td.subject13objective, td.subject13practical, td.subject13Sba, IFNULL(td.subject13written, 0) + IFNULL(td.subject13objective, 0)+IFNULL(td.subject13practical, 0) subject13ht, td.subject13gp, tabu.subject13ct," +
                    "tabu.subject14mark, td.subject14written, td.subject14objective, td.subject14practical, td.subject14Sba, IFNULL(td.subject14written, 0) + IFNULL(td.subject14objective, 0)+IFNULL(td.subject14practical, 0) subject14ht, td.subject14gp, tabu.subject14ct," +
                    "tabu.subject15mark, td.subject15written, td.subject15objective, td.subject15practical, td.subject15Sba, IFNULL(td.subject15written, 0) + IFNULL(td.subject15objective, 0)+IFNULL(td.subject15practical, 0) subject15ht, td.subject15gp, tabu.subject15ct," +
                    "tabu.subject16mark, td.subject16written, td.subject16objective, td.subject16practical, td.subject16Sba, IFNULL(td.subject16written, 0) + IFNULL(td.subject16objective, 0)+IFNULL(td.subject16practical, 0) subject16ht, td.subject16gp, tabu.subject16ct," +
                    "tabu.subject17mark, td.subject17written, td.subject17objective, td.subject17practical, td.subject17Sba, IFNULL(td.subject17written, 0) + IFNULL(td.subject17objective, 0)+IFNULL(td.subject17practical, 0) subject17ht, td.subject17gp, tabu.subject17ct," +
                    "tabu.subject18mark, td.subject18written, td.subject18objective, td.subject18practical, td.subject18Sba, IFNULL(td.subject18written, 0) + IFNULL(td.subject18objective, 0)+IFNULL(td.subject18practical, 0) subject18ht, td.subject18gp, tabu.subject18ct," +
                    "tabu.subject19mark, td.subject19written, td.subject19objective, td.subject19practical, td.subject19Sba, IFNULL(td.subject19written, 0) + IFNULL(td.subject19objective, 0)+IFNULL(td.subject19practical, 0) subject19ht, td.subject19gp, tabu.subject19ct," +
                    "tabu.subject20mark, td.subject20written, td.subject20objective, td.subject20practical, td.subject20Sba, IFNULL(td.subject20written, 0) + IFNULL(td.subject20objective, 0)+IFNULL(td.subject20practical, 0) subject20ht, td.subject20gp, tabu.subject20ct," +
                    "tabu.subject21mark, td.subject21written, td.subject21objective, td.subject21practical, td.subject21Sba, IFNULL(td.subject21written, 0) + IFNULL(td.subject21objective, 0)+IFNULL(td.subject21practical, 0) subject21ht, td.subject21gp, tabu.subject21ct," +
                    "tabu.subject22mark, td.subject22written, td.subject22objective, td.subject22practical, td.subject22Sba, IFNULL(td.subject22written, 0) + IFNULL(td.subject22objective, 0)+IFNULL(td.subject22practical, 0) subject22ht, td.subject22gp, tabu.subject22ct," +
                    "tabu.attendance_mark as attendanceMark," +
                    "tabu.total_obtain_mark as finalToal,tabu.term1mark, tabu.term2mark , tabu.grand_total_mark grandTotal," +
                    "tabu.section_str_position, tabu.class_str_position, tabu.failed_sub_counter, tabu.result_status,tabu.g_point," +
                    "tabu.l_grade,tabu.attendance_day as presentCount" +
                    " from tabulation tabu" +
                    " inner join tabulation_details td on tabu.id = td.tabulation_id" +
                    " inner join student std on std.id=tabu.student_id and std.student_status='NEW'"
        } else if (runningSchool == CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) {
            if (examTerm == ExamTerm.FINAL_TEST.key) {
                queryStrSelect = getIdealFinalTabulationReportCardQuery()
            } else {
                queryStrSelect = "select std.name,std.studentid,std.roll_no as rollNo," +
                        "tabu.subject0mark, td.subject0written, td.subject0objective, td.subject0gp, tabu.subject0ct," +
                        "tabu.subject1mark,td.subject1written,td.subject1objective, td.subject1gp, tabu.subject1ct," +
                        "tabu.subject2mark,td.subject2written,td.subject2objective, td.subject2gp, tabu.subject2ct," +
                        "tabu.subject3mark,td.subject3written,td.subject3objective, td.subject3gp, tabu.subject3ct," +
                        "tabu.subject4mark,td.subject4written, td.subject4objective,td.subject4practical, td.subject4gp, tabu.subject4ct," +
                        "tabu.subject5mark,td.subject5written, td.subject5objective,td.subject5practical, td.subject5gp, tabu.subject5ct," +
                        "tabu.subject6mark,td.subject6written, td.subject6objective,td.subject6practical, td.subject6gp, tabu.subject6ct," +
                        "tabu.subject7mark,td.subject7written, td.subject7objective,td.subject7practical, td.subject7gp, tabu.subject7ct," +
                        "tabu.subject8mark,td.subject8written, td.subject8objective,td.subject8practical, td.subject8gp, tabu.subject8ct," +
                        "tabu.subject9mark,td.subject9written, td.subject9objective,td.subject9practical, td.subject9gp, tabu.subject9ct," +
                        "tabu.subject10mark,td.subject10written,td.subject10objective,td.subject10practical, td.subject10gp, tabu.subject10ct," +
                        "tabu.subject11mark,td.subject11written,td.subject11objective,td.subject11practical, td.subject11gp, tabu.subject11ct," +
                        "tabu.subject12mark,td.subject12written,td.subject12objective,td.subject12practical, td.subject12gp, tabu.subject12ct," +
                        "tabu.subject13mark,td.subject13written,td.subject13objective,td.subject13practical, td.subject13gp, tabu.subject13ct," +
                        "tabu.subject14mark,td.subject14written,td.subject14objective,td.subject14practical, td.subject14gp, tabu.subject14ct," +
                        "tabu.subject15mark,td.subject15written,td.subject15objective,td.subject15practical, td.subject15gp, tabu.subject15ct," +
                        "tabu.subject16mark,td.subject16written,td.subject16objective, td.subject16gp, tabu.subject16ct," +
                        "tabu.subject17mark,td.subject17written,td.subject17objective, td.subject17gp, tabu.subject17ct," +
                        "tabu.subject18mark,td.subject18written,td.subject18objective, td.subject18gp, tabu.subject18ct," +
                        "tabu.subject19mark,td.subject19written,td.subject19objective, td.subject19gp, tabu.subject19ct," +
                        "tabu.total_obtain_mark as finalToal,tabu.term1mark, tabu.term2mark , tabu.grand_total_mark grandTotal," +
                        "tabu.section_str_position, tabu.class_str_position, tabu.failed_sub_counter, tabu.result_status,tabu.g_point,tabu.l_grade," +
                        "tabu.attendance_mark as attendanceMark,tabu.attendance_day as presentCount" +
                        " from tabulation tabu" +
                        " inner join tabulation_details td on tabu.id = td.tabulation_id" +
                        " inner join student std on std.id=tabu.student_id and std.student_status='NEW'"
            }
        }

        String queryStrWhere = " where tabu.exam_id=${examId} "

        return queryStrSelect + queryStrWhere + orderByStr
    }

    String getIdealFinalTabulationReportCardQuery() {
        String queryStrSelect = "select std.name, std.studentid, std.roll_no as rollNo," +
                "tabu.subject0mark, td.subject0written, td.subject0objective, td.subject0gp, tabu.subject0ct, avg.subject0cta, avg.subject0hall_obtain, avg.subject0hall, avg.subject0term, avg.subject0avg," +
                "tabu.subject1mark,td.subject1written,td.subject1objective, td.subject1gp, tabu.subject1ct, avg.subject1cta, avg.subject1hall_obtain, avg.subject1hall, avg.subject1term, avg.subject1avg," +
                "tabu.subject2mark,td.subject2written,td.subject2objective, td.subject2gp, tabu.subject2ct,avg.subject2cta, avg.subject2hall_obtain, avg.subject2hall, avg.subject2term, avg.subject2avg," +
                "tabu.subject3mark,td.subject3written,td.subject3objective, td.subject3practical, td.subject3gp, tabu.subject3ct,avg.subject3cta, avg.subject3hall_obtain, avg.subject3hall, avg.subject3term, avg.subject3avg," +
                "tabu.subject4mark,td.subject4written, td.subject4objective,td.subject4practical, td.subject4gp, tabu.subject4ct,avg.subject4cta, avg.subject4hall_obtain, avg.subject4hall, avg.subject4term, avg.subject4avg," +
                "tabu.subject5mark,td.subject5written, td.subject5objective,td.subject5practical, td.subject5gp, tabu.subject5ct,avg.subject5cta, avg.subject5hall_obtain, avg.subject5hall, avg.subject5term, avg.subject5avg," +
                "tabu.subject6mark,td.subject6written, td.subject6objective,td.subject6practical, td.subject6gp, tabu.subject6ct,avg.subject6cta, avg.subject6hall_obtain, avg.subject6hall, avg.subject6term, avg.subject6avg," +
                "tabu.subject7mark,td.subject7written, td.subject7objective,td.subject7practical, td.subject7gp, tabu.subject7ct,avg.subject7cta, avg.subject7hall_obtain, avg.subject7hall, avg.subject7term, avg.subject7avg," +
                "tabu.subject8mark,td.subject8written, td.subject8objective,td.subject8practical, td.subject8gp, tabu.subject8ct,avg.subject8cta, avg.subject8hall_obtain, avg.subject8hall, avg.subject8term, avg.subject8avg," +
                "tabu.subject9mark,td.subject9written, td.subject9objective,td.subject9practical, td.subject9gp, tabu.subject9ct,avg.subject9cta, avg.subject9hall_obtain, avg.subject9hall, avg.subject9term, avg.subject9avg," +
                "tabu.subject10mark,td.subject10written,td.subject10objective,td.subject10practical, td.subject10gp, tabu.subject10ct,avg.subject10cta, avg.subject10hall_obtain, avg.subject10hall, avg.subject10term, avg.subject10avg," +
                "tabu.subject11mark,td.subject11written,td.subject11objective,td.subject11practical, td.subject11gp, tabu.subject11ct,avg.subject11cta, avg.subject11hall_obtain, avg.subject11hall, avg.subject11term, avg.subject11avg," +
                "tabu.subject12mark,td.subject12written,td.subject12objective,td.subject12practical, td.subject12gp, tabu.subject12ct,avg.subject12cta, avg.subject12hall_obtain, avg.subject12hall, avg.subject12term, avg.subject12avg," +
                "tabu.subject13mark,td.subject13written,td.subject13objective,td.subject13practical, td.subject13gp, tabu.subject13ct,avg.subject13cta, avg.subject13hall_obtain, avg.subject13hall, avg.subject13term, avg.subject13avg," +
                "tabu.subject14mark,td.subject14written,td.subject14objective, td.subject14practical, td.subject14gp, tabu.subject14ct,avg.subject14cta, avg.subject14hall_obtain, avg.subject14hall, avg.subject14term, avg.subject14avg," +
                "tabu.subject15mark,td.subject15written,td.subject15objective, td.subject15practical, td.subject15gp, tabu.subject15ct,avg.subject15cta, avg.subject15hall_obtain, avg.subject15hall, avg.subject15term, avg.subject15avg," +
                "tabu.subject16mark,td.subject16written,td.subject16objective, td.subject16practical, td.subject16gp, tabu.subject16ct,avg.subject16cta, avg.subject16hall_obtain, avg.subject16hall, avg.subject16term, avg.subject16avg," +
                "tabu.subject17mark,td.subject17written,td.subject17objective, td.subject17practical, td.subject17gp, tabu.subject17ct,avg.subject17cta, avg.subject17hall_obtain, avg.subject17hall, avg.subject17term, avg.subject17avg," +
                "tabu.subject18mark,td.subject18written,td.subject18objective, td.subject18practical, td.subject18gp, tabu.subject18ct,avg.subject18cta, avg.subject18hall_obtain, avg.subject18hall, avg.subject18term, avg.subject18avg," +
                "tabu.subject19mark,td.subject19written,td.subject19objective, td.subject19practical, td.subject19gp, tabu.subject19ct,avg.subject19cta, avg.subject19hall_obtain, avg.subject19hall, avg.subject19term, avg.subject19avg," +
                "tabu.subject20mark,td.subject20written,td.subject20objective, td.subject20practical, td.subject20gp, tabu.subject20ct,avg.subject20cta, avg.subject20hall_obtain, avg.subject20hall, avg.subject20term, avg.subject20avg," +
                "tabu.subject21mark,td.subject21written,td.subject21objective, td.subject21practical, td.subject21gp, tabu.subject21ct,avg.subject21cta, avg.subject21hall_obtain, avg.subject21hall, avg.subject21term, avg.subject21avg," +
                "tabu.subject22mark,td.subject22written,td.subject22objective, td.subject22practical, td.subject22gp, tabu.subject22ct,avg.subject22cta, avg.subject22hall_obtain, avg.subject22hall, avg.subject22term, avg.subject22avg," +
                "tabu.subject23mark,td.subject23written,td.subject23objective, td.subject23practical, td.subject23gp, tabu.subject23ct,avg.subject23cta, avg.subject23hall_obtain, avg.subject23hall, avg.subject23term, avg.subject23avg," +
                "tabu.subject24mark,td.subject24written,td.subject24objective, td.subject24practical, td.subject24gp, tabu.subject24ct,avg.subject24cta, avg.subject24hall_obtain, avg.subject24hall, avg.subject24term, avg.subject24avg," +
                "tabu.subject25mark,td.subject25written,td.subject25objective, td.subject25practical, td.subject25gp, tabu.subject25ct,avg.subject25cta, avg.subject25hall_obtain, avg.subject25hall, avg.subject25term, avg.subject25avg," +
                "tabu.subject26mark,td.subject26written,td.subject26objective, td.subject26practical, td.subject26gp, tabu.subject26ct,avg.subject26cta, avg.subject26hall_obtain, avg.subject26hall, avg.subject26term, avg.subject26avg," +
                "tabu.subject27mark,td.subject27written,td.subject27objective, td.subject27practical, td.subject27gp, tabu.subject27ct,avg.subject27cta, avg.subject27hall_obtain, avg.subject27hall, avg.subject27term, avg.subject27avg," +
                "tabu.subject28mark,td.subject28written,td.subject28objective, td.subject28practical, td.subject28gp, tabu.subject28ct,avg.subject28cta, avg.subject28hall_obtain, avg.subject28hall, avg.subject28term, avg.subject28avg," +
                "tabu.attendance_mark as attendanceMark," +
                "tabu.total_obtain_mark as finalToal,tabu.term1mark, tabu.term2mark , tabu.grand_total_mark grandTotal," +
                "tabu.section_str_position, tabu.class_str_position, tabu.failed_sub_counter, tabu.result_status,tabu.g_point,tabu.l_grade," +
                "tabu.attendance_day as presentCount" +
                " from tabulation tabu" +
                " inner join tabulation_details td on tabu.id = td.tabulation_id" +
                " inner join tabulation_avg_mark avg on tabu.id = avg.tabulation_id" +
                " inner join student std on std.id=tabu.student_id and std.student_status='NEW'"

        return queryStrSelect
    }

}