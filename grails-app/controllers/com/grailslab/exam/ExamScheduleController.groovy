package com.grailslab.exam

import com.grailslab.ClassNameService
import com.grailslab.command.ExamScheduleAllCommand
import com.grailslab.enums.ExamStatus
import com.grailslab.enums.ExamTerm
import com.grailslab.settings.ClassName
import com.grailslab.settings.Exam
import com.grailslab.settings.ShiftExam

import javax.servlet.http.HttpServletRequest

class ExamScheduleController {

    def springSecurityService
    def examScheduleService
    def classSubjectsService
    ClassNameService classNameService
    def examMarkService
    def examService
    def uploadService

    def index() { }

    def classSchedule(){
        ShiftExam shiftExam = ShiftExam.read(params.getLong('shiftExam'))
        ClassName clsName = classNameService.read(params.getLong('className'))
        def classExams = examService.classExamIdList(shiftExam, clsName, null)
        def examScheduleList = examScheduleService.classScheduleForEdit(classExams)
        render(view: '/exam/viewHTSchedule', model: [isExamPublished: true, examScheduleList:examScheduleList, className:clsName, examName: shiftExam.examName, shiftExamId: shiftExam.id])
        return
    }
    def examRoutineUpload() {
        //code for save File
        HttpServletRequest request = request
        def f = request.getFile("routineFile")
        if (!f.empty) {
            try {
                def uploadStatus = uploadService.singleFileUpload(request, "routineFile", "examroutine/final_routine")
                if(uploadStatus) {
                    flash.message = 'Upload Successsfully'
                    redirect(controller: 'calendar', action: 'examRoutine')
                    return
                }
            } catch (Exception e) {
                log.error("Upload Image" + e)
                flash.message = e.getMessage()
                redirect(controller: 'calendar', action: 'examRoutine')
                return
            }
        }
    }

    def addAllSchedule(){
        Long shiftExamId = params.getLong('id')
        ShiftExam shiftExam = ShiftExam.read(shiftExamId)
        if(shiftExam.examStatus==ExamStatus.PUBLISHED){
            flash.message= "${shiftExam.examName} result already published."
            redirect(action: 'index')
            return
        }
        String scheduleManageType = "CREATE"
        if (params.scheduleManageType && params.scheduleManageType == "UPDATE") {
            scheduleManageType = "UPDATE"
        }
        Long classNameId = params.getLong('className')
        ClassName className = ClassName.read(classNameId)

        List classExams = examService.classExamIdList(shiftExam, className, null)

        boolean isOtherActivity = false
        if (shiftExam.examTerm == ExamTerm.FINAL_TEST) {
            isOtherActivity = true
        }
        def allScheduleList = examScheduleService.classScheduleForAdd(className, isOtherActivity)

        render(view: '/exam/editAllSchedule',model: [scheduleManageType: scheduleManageType, className: className, shiftExam: shiftExam, allScheduleList: allScheduleList])
    }
    def editAllSchedule(){
        Long shiftExamId = params.getLong('id')
        ShiftExam shiftExam = ShiftExam.read(shiftExamId)
        if(shiftExam.examStatus==ExamStatus.PUBLISHED){
            flash.message= "${shiftExam.examName} result already published."
            redirect(action: 'index')
            return
        }
        Long classNameId = params.getLong('className')
        ClassName className = ClassName.read(classNameId)

        def classExams = examService.classExamIdList(shiftExam, className, null)
        def allScheduleList = examScheduleService.classScheduleForEdit(classExams)

        render(view: '/exam/editAllSchedule',model: [scheduleManageType: "UPDATE", className: className, shiftExam: shiftExam, allScheduleList: allScheduleList])
    }

    def saveAllSchedule(ExamScheduleAllCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        ShiftExam shiftExam = ShiftExam.read(params.getLong("shiftExamId"))
        List classSubIds = new ArrayList()
        Map groupScheduleMap = [:]
        String clsSubId
        Integer numOfCtExam = shiftExam.ctExam ?:0
        if (command.classSubObjId0) {
            clsSubId = command.classSubObjId0.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId0, clsSubId, command.weightOnResult0 ?:0, command.passMark0 ?:0, command.sortOrder0 ?:0,
                    command.isExtracurricular0 ?:false, numOfCtExam, command.isCtExam0 ?:false, command.ct1Mark0 ?:0, command.ct2Mark0 ?:0, command.ct3Mark0 ?:0, command.ctEffMark0 ?:0,
                    command.hallMark0 ?:0, command.hallEffMark0 ?:0, command.isHallPractical0 ?:false, command.hallWrittenMark0 ?:0, command.hallPracticalMark0 ?:0,
                    command.hallObjectiveMark0 ?:0, command.hallSbaMark0 ?:0, command.hallInput50 ?:0, command.isPassSeparately0 ?:false, command.writtenPassMark0 ?:0,
                    command.practicalPassMark0 ?:0, command.objectivePassMark0 ?:0, command.sbaPassMark0 ?:0, command.input5PassMark0 ?:0)
        }
        if (command.classSubObjId1) {
            clsSubId = command.classSubObjId1.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId1, clsSubId, command.weightOnResult1 ?:0, command.passMark1 ?:0, command.sortOrder1 ?:0,
                    command.isExtracurricular1 ?:false, numOfCtExam, command.isCtExam1 ?:false, command.ct1Mark1 ?:0, command.ct2Mark1 ?:0, command.ct3Mark1 ?:0, command.ctEffMark1 ?:0,
                    command.hallMark1 ?:0, command.hallEffMark1 ?:0, command.isHallPractical1 ?:false, command.hallWrittenMark1 ?:0, command.hallPracticalMark1 ?:0,
                    command.hallObjectiveMark1 ?:0, command.hallSbaMark1 ?:0, command.hallInput51 ?:0, command.isPassSeparately1 ?:false, command.writtenPassMark1 ?:0,
                    command.practicalPassMark1 ?:0, command.objectivePassMark1 ?:0, command.sbaPassMark1 ?:0, command.input5PassMark1 ?:0)
        }
        if (command.classSubObjId2) {
            clsSubId = command.classSubObjId2.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId2, clsSubId, command.weightOnResult2 ?:0, command.passMark2 ?:0, command.sortOrder2 ?:0,
                    command.isExtracurricular2 ?:false, numOfCtExam, command.isCtExam2 ?:false, command.ct1Mark2 ?:0, command.ct2Mark2 ?:0, command.ct3Mark2 ?:0, command.ctEffMark2 ?:0,
                    command.hallMark2 ?:0, command.hallEffMark2 ?:0, command.isHallPractical2 ?:false, command.hallWrittenMark2 ?:0, command.hallPracticalMark2 ?:0,
                    command.hallObjectiveMark2 ?:0, command.hallSbaMark2 ?:0, command.hallInput52 ?:0, command.isPassSeparately2 ?:false, command.writtenPassMark2 ?:0,
                    command.practicalPassMark2 ?:0, command.objectivePassMark2 ?:0, command.sbaPassMark2 ?:0, command.input5PassMark2 ?:0)
        }
        if (command.classSubObjId3) {
            clsSubId = command.classSubObjId3.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId3, clsSubId, command.weightOnResult3 ?:0, command.passMark3 ?:0, command.sortOrder3 ?:0,
                    command.isExtracurricular3 ?:false, numOfCtExam, command.isCtExam3 ?:false, command.ct1Mark3 ?:0, command.ct2Mark3 ?:0, command.ct3Mark3 ?:0, command.ctEffMark3 ?:0,
                    command.hallMark3 ?:0, command.hallEffMark3 ?:0, command.isHallPractical3 ?:false, command.hallWrittenMark3 ?:0, command.hallPracticalMark3 ?:0,
                    command.hallObjectiveMark3 ?:0, command.hallSbaMark3 ?:0, command.hallInput53 ?:0, command.isPassSeparately3 ?:false, command.writtenPassMark3 ?:0,
                    command.practicalPassMark3 ?:0, command.objectivePassMark3 ?:0, command.sbaPassMark3 ?:0, command.input5PassMark3 ?:0)
        }
        if (command.classSubObjId4) {
            clsSubId = command.classSubObjId4.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId4, clsSubId, command.weightOnResult4 ?:0, command.passMark4 ?:0, command.sortOrder4 ?:0,
                    command.isExtracurricular4 ?:false, numOfCtExam, command.isCtExam4 ?:false, command.ct1Mark4 ?:0, command.ct2Mark4 ?:0, command.ct3Mark4 ?:0, command.ctEffMark4 ?:0,
                    command.hallMark4 ?:0, command.hallEffMark4 ?:0, command.isHallPractical4 ?:false, command.hallWrittenMark4 ?:0, command.hallPracticalMark4 ?:0,
                    command.hallObjectiveMark4 ?:0, command.hallSbaMark4 ?:0, command.hallInput54 ?:0, command.isPassSeparately4 ?:false, command.writtenPassMark4 ?:0,
                    command.practicalPassMark4 ?:0, command.objectivePassMark4 ?:0, command.sbaPassMark4 ?:0, command.input5PassMark4 ?:0)
        }
        if (command.classSubObjId5) {
            clsSubId = command.classSubObjId5.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId5, clsSubId, command.weightOnResult5 ?:0, command.passMark5 ?:0, command.sortOrder5 ?:0,
                    command.isExtracurricular5 ?:false, numOfCtExam, command.isCtExam5 ?:false, command.ct1Mark5 ?:0, command.ct2Mark5 ?:0, command.ct3Mark5 ?:0, command.ctEffMark5 ?:0,
                    command.hallMark5 ?:0, command.hallEffMark5 ?:0, command.isHallPractical5 ?:false, command.hallWrittenMark5 ?:0, command.hallPracticalMark5 ?:0,
                    command.hallObjectiveMark5 ?:0, command.hallSbaMark5 ?:0, command.hallInput55 ?:0, command.isPassSeparately5 ?:false, command.writtenPassMark5 ?:0,
                    command.practicalPassMark5 ?:0, command.objectivePassMark5 ?:0, command.sbaPassMark5 ?:0, command.input5PassMark5 ?:0)
        }
        if (command.classSubObjId6) {
            clsSubId = command.classSubObjId6.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId6, clsSubId, command.weightOnResult6 ?:0, command.passMark6 ?:0, command.sortOrder6 ?:0,
                    command.isExtracurricular6 ?:false, numOfCtExam, command.isCtExam6 ?:false, command.ct1Mark6 ?:0, command.ct2Mark6 ?:0, command.ct3Mark6 ?:0, command.ctEffMark6 ?:0,
                    command.hallMark6 ?:0, command.hallEffMark6 ?:0, command.isHallPractical6 ?:false, command.hallWrittenMark6 ?:0, command.hallPracticalMark6 ?:0,
                    command.hallObjectiveMark6 ?:0, command.hallSbaMark6 ?:0, command.hallInput56 ?:0, command.isPassSeparately6 ?:false, command.writtenPassMark6 ?:0,
                    command.practicalPassMark6 ?:0, command.objectivePassMark6 ?:0, command.sbaPassMark6 ?:0, command.input5PassMark6 ?:0)
        }
        if (command.classSubObjId7) {
            clsSubId = command.classSubObjId7.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId7, clsSubId, command.weightOnResult7 ?:0, command.passMark7 ?:0, command.sortOrder7 ?:0,
                    command.isExtracurricular7 ?:false, numOfCtExam, command.isCtExam7 ?:false, command.ct1Mark7 ?:0, command.ct2Mark7 ?:0, command.ct3Mark7 ?:0, command.ctEffMark7 ?:0,
                    command.hallMark7 ?:0, command.hallEffMark7 ?:0, command.isHallPractical7 ?:false, command.hallWrittenMark7 ?:0, command.hallPracticalMark7 ?:0,
                    command.hallObjectiveMark7 ?:0, command.hallSbaMark7 ?:0, command.hallInput57 ?:0, command.isPassSeparately7 ?:false, command.writtenPassMark7 ?:0,
                    command.practicalPassMark7 ?:0, command.objectivePassMark7 ?:0, command.sbaPassMark7 ?:0, command.input5PassMark7 ?:0)
        }
        if (command.classSubObjId8) {
            clsSubId = command.classSubObjId8.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId8, clsSubId, command.weightOnResult8 ?:0, command.passMark8 ?:0, command.sortOrder8 ?:0,
                    command.isExtracurricular8 ?:false, numOfCtExam, command.isCtExam8 ?:false, command.ct1Mark8 ?:0, command.ct2Mark8 ?:0, command.ct3Mark8 ?:0, command.ctEffMark8 ?:0,
                    command.hallMark8 ?:0, command.hallEffMark8 ?:0, command.isHallPractical8 ?:false, command.hallWrittenMark8 ?:0, command.hallPracticalMark8 ?:0,
                    command.hallObjectiveMark8 ?:0, command.hallSbaMark8 ?:0, command.hallInput58 ?:0, command.isPassSeparately8 ?:false, command.writtenPassMark8 ?:0,
                    command.practicalPassMark8 ?:0, command.objectivePassMark8 ?:0, command.sbaPassMark8 ?:0, command.input5PassMark8 ?:0)
        }

        if (command.classSubObjId9) {
            clsSubId = command.classSubObjId9.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId9, clsSubId, command.weightOnResult9 ?:0, command.passMark9 ?:0, command.sortOrder9 ?:0,
                    command.isExtracurricular9 ?:false, numOfCtExam, command.isCtExam9 ?:false, command.ct1Mark9 ?:0, command.ct2Mark9 ?:0, command.ct3Mark9 ?:0, command.ctEffMark9 ?:0,
                    command.hallMark9 ?:0, command.hallEffMark9 ?:0, command.isHallPractical9 ?:false, command.hallWrittenMark9 ?:0, command.hallPracticalMark9 ?:0,
                    command.hallObjectiveMark9 ?:0, command.hallSbaMark9 ?:0, command.hallInput59 ?:0, command.isPassSeparately9 ?:false, command.writtenPassMark9 ?:0,
                    command.practicalPassMark9 ?:0, command.objectivePassMark9 ?:0, command.sbaPassMark9 ?:0, command.input5PassMark9 ?:0)
        }
        if (command.classSubObjId10) {
            clsSubId = command.classSubObjId10.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId10, clsSubId, command.weightOnResult10 ?:0, command.passMark10 ?:0, command.sortOrder10 ?:0,
                    command.isExtracurricular10 ?:false, numOfCtExam, command.isCtExam10 ?:false, command.ct1Mark10 ?:0, command.ct2Mark10 ?:0, command.ct3Mark10 ?:0, command.ctEffMark10 ?:0,
                    command.hallMark10 ?:0, command.hallEffMark10 ?:0, command.isHallPractical10 ?:false, command.hallWrittenMark10 ?:0, command.hallPracticalMark10 ?:0,
                    command.hallObjectiveMark10 ?:0, command.hallSbaMark10 ?:0, command.hallInput510 ?:0, command.isPassSeparately10 ?:false, command.writtenPassMark10 ?:0,
                    command.practicalPassMark10 ?:0, command.objectivePassMark10 ?:0, command.sbaPassMark10 ?:0, command.input5PassMark10 ?:0)
        }
        if (command.classSubObjId11) {
            clsSubId = command.classSubObjId11.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId11, clsSubId, command.weightOnResult11 ?:0, command.passMark11 ?:0, command.sortOrder11 ?:0,
                    command.isExtracurricular11 ?:false, numOfCtExam, command.isCtExam11 ?:false, command.ct1Mark11 ?:0, command.ct2Mark11 ?:0, command.ct3Mark11 ?:0, command.ctEffMark11 ?:0,
                    command.hallMark11 ?:0, command.hallEffMark11 ?:0, command.isHallPractical11 ?:false, command.hallWrittenMark11 ?:0, command.hallPracticalMark11 ?:0,
                    command.hallObjectiveMark11 ?:0, command.hallSbaMark11 ?:0, command.hallInput511 ?:0, command.isPassSeparately11 ?:false, command.writtenPassMark11 ?:0,
                    command.practicalPassMark11 ?:0, command.objectivePassMark11 ?:0, command.sbaPassMark11 ?:0, command.input5PassMark11 ?:0)
        }
        if (command.classSubObjId12) {
            clsSubId = command.classSubObjId12.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId12, clsSubId, command.weightOnResult12 ?:0, command.passMark12 ?:0, command.sortOrder12 ?:0,
                    command.isExtracurricular12 ?:false, numOfCtExam, command.isCtExam12 ?:false, command.ct1Mark12 ?:0, command.ct2Mark12 ?:0, command.ct3Mark12 ?:0, command.ctEffMark12 ?:0,
                    command.hallMark12 ?:0, command.hallEffMark12 ?:0, command.isHallPractical12 ?:false, command.hallWrittenMark12 ?:0, command.hallPracticalMark12 ?:0,
                    command.hallObjectiveMark12 ?:0, command.hallSbaMark12 ?:0, command.hallInput512 ?:0, command.isPassSeparately12 ?:false, command.writtenPassMark12 ?:0,
                    command.practicalPassMark12 ?:0, command.objectivePassMark12 ?:0, command.sbaPassMark12 ?:0, command.input5PassMark12 ?:0)
        }
        if (command.classSubObjId13) {
            clsSubId = command.classSubObjId13.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId13, clsSubId, command.weightOnResult13 ?:0, command.passMark13 ?:0, command.sortOrder13 ?:0,
                    command.isExtracurricular13 ?:false, numOfCtExam, command.isCtExam13 ?:false, command.ct1Mark13 ?:0, command.ct2Mark13 ?:0, command.ct3Mark13 ?:0, command.ctEffMark13 ?:0,
                    command.hallMark13 ?:0, command.hallEffMark13 ?:0, command.isHallPractical13 ?:false, command.hallWrittenMark13 ?:0, command.hallPracticalMark13 ?:0,
                    command.hallObjectiveMark13 ?:0, command.hallSbaMark13 ?:0, command.hallInput513 ?:0, command.isPassSeparately13 ?:false, command.writtenPassMark13 ?:0,
                    command.practicalPassMark13 ?:0, command.objectivePassMark13 ?:0, command.sbaPassMark13 ?:0, command.input5PassMark13 ?:0)
        }
        if (command.classSubObjId14) {
            clsSubId = command.classSubObjId14.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId14, clsSubId, command.weightOnResult14 ?:0, command.passMark14 ?:0, command.sortOrder14 ?:0,
                    command.isExtracurricular14 ?:false, numOfCtExam, command.isCtExam14 ?:false, command.ct1Mark14 ?:0, command.ct2Mark14 ?:0, command.ct3Mark14 ?:0, command.ctEffMark14 ?:0,
                    command.hallMark14 ?:0, command.hallEffMark14 ?:0, command.isHallPractical14 ?:false, command.hallWrittenMark14 ?:0, command.hallPracticalMark14 ?:0,
                    command.hallObjectiveMark14 ?:0, command.hallSbaMark14 ?:0, command.hallInput514 ?:0, command.isPassSeparately14 ?:false, command.writtenPassMark14 ?:0,
                    command.practicalPassMark14 ?:0, command.objectivePassMark14 ?:0, command.sbaPassMark14 ?:0, command.input5PassMark14 ?:0)
        }
        if (command.classSubObjId15) {
            clsSubId = command.classSubObjId15.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId15, clsSubId, command.weightOnResult15 ?:0, command.passMark15 ?:0, command.sortOrder15 ?:0,
                    command.isExtracurricular15 ?:false, numOfCtExam, command.isCtExam15 ?:false, command.ct1Mark15 ?:0, command.ct2Mark15 ?:0, command.ct3Mark15 ?:0, command.ctEffMark15 ?:0,
                    command.hallMark15 ?:0, command.hallEffMark15 ?:0, command.isHallPractical15 ?:false, command.hallWrittenMark15 ?:0, command.hallPracticalMark15 ?:0,
                    command.hallObjectiveMark15 ?:0, command.hallSbaMark15 ?:0, command.hallInput515 ?:0, command.isPassSeparately15 ?:false, command.writtenPassMark15 ?:0,
                    command.practicalPassMark15 ?:0, command.objectivePassMark15 ?:0, command.sbaPassMark15 ?:0, command.input5PassMark15 ?:0)
        }
        if (command.classSubObjId16) {
            clsSubId = command.classSubObjId16.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId16, clsSubId, command.weightOnResult16 ?:0, command.passMark16 ?:0, command.sortOrder16 ?:0,
                    command.isExtracurricular16 ?:false, numOfCtExam, command.isCtExam16 ?:false, command.ct1Mark16 ?:0, command.ct2Mark16 ?:0, command.ct3Mark16 ?:0, command.ctEffMark16 ?:0,
                    command.hallMark16 ?:0, command.hallEffMark16 ?:0, command.isHallPractical16 ?:false, command.hallWrittenMark16 ?:0, command.hallPracticalMark16 ?:0,
                    command.hallObjectiveMark16 ?:0, command.hallSbaMark16 ?:0, command.hallInput516 ?:0, command.isPassSeparately16 ?:false, command.writtenPassMark16 ?:0,
                    command.practicalPassMark16 ?:0, command.objectivePassMark16 ?:0, command.sbaPassMark16 ?:0, command.input5PassMark16 ?:0)
        }
        if (command.classSubObjId17) {
            clsSubId = command.classSubObjId17.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId17, clsSubId, command.weightOnResult17 ?:0, command.passMark17 ?:0, command.sortOrder17 ?:0,
                    command.isExtracurricular17 ?:false, numOfCtExam, command.isCtExam17 ?:false, command.ct1Mark17 ?:0, command.ct2Mark17 ?:0, command.ct3Mark17 ?:0, command.ctEffMark17 ?:0,
                    command.hallMark17 ?:0, command.hallEffMark17 ?:0, command.isHallPractical17 ?:false, command.hallWrittenMark17 ?:0, command.hallPracticalMark17 ?:0,
                    command.hallObjectiveMark17 ?:0, command.hallSbaMark17 ?:0, command.hallInput517 ?:0, command.isPassSeparately17 ?:false, command.writtenPassMark17 ?:0,
                    command.practicalPassMark17 ?:0, command.objectivePassMark17 ?:0, command.sbaPassMark17 ?:0, command.input5PassMark17 ?:0)
        }
        if (command.classSubObjId18) {
            clsSubId = command.classSubObjId18.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId18, clsSubId, command.weightOnResult18 ?:0, command.passMark18 ?:0, command.sortOrder18 ?:0,
                    command.isExtracurricular18 ?:false, numOfCtExam, command.isCtExam18 ?:false, command.ct1Mark18 ?:0, command.ct2Mark18 ?:0, command.ct3Mark18 ?:0, command.ctEffMark18 ?:0,
                    command.hallMark18 ?:0, command.hallEffMark18 ?:0, command.isHallPractical18 ?:false, command.hallWrittenMark18 ?:0, command.hallPracticalMark18 ?:0,
                    command.hallObjectiveMark18 ?:0, command.hallSbaMark18 ?:0, command.hallInput518 ?:0, command.isPassSeparately18 ?:false, command.writtenPassMark18 ?:0,
                    command.practicalPassMark18 ?:0, command.objectivePassMark18 ?:0, command.sbaPassMark18 ?:0, command.input5PassMark18 ?:0)
        }
        if (command.classSubObjId19) {
            clsSubId = command.classSubObjId19.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId19, clsSubId, command.weightOnResult19 ?:0, command.passMark19 ?:0, command.sortOrder19 ?:0,
                    command.isExtracurricular19 ?:false, numOfCtExam, command.isCtExam19 ?:false, command.ct1Mark19 ?:0, command.ct2Mark19 ?:0, command.ct3Mark19 ?:0, command.ctEffMark19 ?:0,
                    command.hallMark19 ?:0, command.hallEffMark19 ?:0, command.isHallPractical19 ?:false, command.hallWrittenMark19 ?:0, command.hallPracticalMark19 ?:0,
                    command.hallObjectiveMark19 ?:0, command.hallSbaMark19 ?:0, command.hallInput519 ?:0, command.isPassSeparately19 ?:false, command.writtenPassMark19 ?:0,
                    command.practicalPassMark19 ?:0, command.objectivePassMark19 ?:0, command.sbaPassMark19 ?:0, command.input5PassMark19 ?:0)
        }
        if (command.classSubObjId20) {
            clsSubId = command.classSubObjId20.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId20, clsSubId, command.weightOnResult20 ?:0, command.passMark20 ?:0, command.sortOrder20 ?:0,
                    command.isExtracurricular20 ?:false, numOfCtExam, command.isCtExam20 ?:false, command.ct1Mark20 ?:0, command.ct2Mark20 ?:0, command.ct3Mark20 ?:0, command.ctEffMark20 ?:0,
                    command.hallMark20 ?:0, command.hallEffMark20 ?:0, command.isHallPractical20 ?:false, command.hallWrittenMark20 ?:0, command.hallPracticalMark20 ?:0,
                    command.hallObjectiveMark20 ?:0, command.hallSbaMark20 ?:0, command.hallInput520 ?:0, command.isPassSeparately20 ?:false, command.writtenPassMark20 ?:0,
                    command.practicalPassMark20 ?:0, command.objectivePassMark20 ?:0, command.sbaPassMark20 ?:0, command.input5PassMark20 ?:0)
        }
        if (command.classSubObjId21) {
            clsSubId = command.classSubObjId21.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId21, clsSubId, command.weightOnResult21 ?:0, command.passMark21 ?:0, command.sortOrder21 ?:0,
                    command.isExtracurricular21 ?:false, numOfCtExam, command.isCtExam21 ?:false, command.ct1Mark21 ?:0, command.ct2Mark21 ?:0, command.ct3Mark21 ?:0, command.ctEffMark21 ?:0,
                    command.hallMark21 ?:0, command.hallEffMark21 ?:0, command.isHallPractical21 ?:false, command.hallWrittenMark21 ?:0, command.hallPracticalMark21 ?:0,
                    command.hallObjectiveMark21 ?:0, command.hallSbaMark21 ?:0, command.hallInput521 ?:0, command.isPassSeparately21 ?:false, command.writtenPassMark21 ?:0,
                    command.practicalPassMark21 ?:0, command.objectivePassMark21 ?:0, command.sbaPassMark21 ?:0, command.input5PassMark21 ?:0)
        }
        if (command.classSubObjId22) {
            clsSubId = command.classSubObjId22.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId22, clsSubId, command.weightOnResult22 ?:0, command.passMark22 ?:0, command.sortOrder22 ?:0,
                    command.isExtracurricular22 ?:false, numOfCtExam, command.isCtExam22 ?:false, command.ct1Mark22 ?:0, command.ct2Mark22 ?:0, command.ct3Mark22 ?:0, command.ctEffMark22 ?:0,
                    command.hallMark22 ?:0, command.hallEffMark22 ?:0, command.isHallPractical22 ?:false, command.hallWrittenMark22 ?:0, command.hallPracticalMark22 ?:0,
                    command.hallObjectiveMark22 ?:0, command.hallSbaMark22 ?:0, command.hallInput522 ?:0, command.isPassSeparately22 ?:false, command.writtenPassMark22 ?:0,
                    command.practicalPassMark22 ?:0, command.objectivePassMark22 ?:0, command.sbaPassMark22 ?:0, command.input5PassMark22 ?:0)
        }
        if (command.classSubObjId23) {
            clsSubId = command.classSubObjId23.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId23, clsSubId, command.weightOnResult23 ?:0, command.passMark23 ?:0, command.sortOrder23 ?:0,
                    command.isExtracurricular23 ?:false, numOfCtExam, command.isCtExam23 ?:false, command.ct1Mark23 ?:0, command.ct2Mark23 ?:0, command.ct3Mark23 ?:0, command.ctEffMark23 ?:0,
                    command.hallMark23 ?:0, command.hallEffMark23 ?:0, command.isHallPractical23 ?:false, command.hallWrittenMark23 ?:0, command.hallPracticalMark23 ?:0,
                    command.hallObjectiveMark23 ?:0, command.hallSbaMark23 ?:0, command.hallInput523 ?:0, command.isPassSeparately23 ?:false, command.writtenPassMark23 ?:0,
                    command.practicalPassMark23 ?:0, command.objectivePassMark23 ?:0, command.sbaPassMark23 ?:0, command.input5PassMark23 ?:0)
        }
        if (command.classSubObjId24) {
            clsSubId = command.classSubObjId24.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId24, clsSubId, command.weightOnResult24 ?:0, command.passMark24 ?:0, command.sortOrder24 ?:0,
                    command.isExtracurricular24 ?:false, numOfCtExam, command.isCtExam24 ?:false, command.ct1Mark24 ?:0, command.ct2Mark24 ?:0, command.ct3Mark24 ?:0, command.ctEffMark24 ?:0,
                    command.hallMark24 ?:0, command.hallEffMark24 ?:0, command.isHallPractical24 ?:false, command.hallWrittenMark24 ?:0, command.hallPracticalMark24 ?:0,
                    command.hallObjectiveMark24 ?:0, command.hallSbaMark24 ?:0, command.hallInput524 ?:0, command.isPassSeparately24 ?:false, command.writtenPassMark24 ?:0,
                    command.practicalPassMark24 ?:0, command.objectivePassMark24 ?:0, command.sbaPassMark24 ?:0, command.input5PassMark24 ?:0)
        }
        if (command.classSubObjId25) {
            clsSubId = command.classSubObjId25.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId25, clsSubId, command.weightOnResult25 ?:0, command.passMark25 ?:0, command.sortOrder25 ?:0,
                    command.isExtracurricular25 ?:false, numOfCtExam, command.isCtExam25 ?:false, command.ct1Mark25 ?:0, command.ct2Mark25 ?:0, command.ct3Mark25 ?:0, command.ctEffMark25 ?:0,
                    command.hallMark25 ?:0, command.hallEffMark25 ?:0, command.isHallPractical25 ?:false, command.hallWrittenMark25 ?:0, command.hallPracticalMark25 ?:0,
                    command.hallObjectiveMark25 ?:0, command.hallSbaMark25 ?:0, command.hallInput525 ?:0, command.isPassSeparately25 ?:false, command.writtenPassMark25 ?:0,
                    command.practicalPassMark25 ?:0, command.objectivePassMark25 ?:0, command.sbaPassMark25 ?:0, command.input5PassMark25 ?:0)
        }
        if (command.classSubObjId26) {
            clsSubId = command.classSubObjId26.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId26, clsSubId, command.weightOnResult26 ?:0, command.passMark26 ?:0, command.sortOrder26 ?:0,
                    command.isExtracurricular26 ?:false, numOfCtExam, command.isCtExam26 ?:false, command.ct1Mark26 ?:0, command.ct2Mark26 ?:0, command.ct3Mark26 ?:0, command.ctEffMark26 ?:0,
                    command.hallMark26 ?:0, command.hallEffMark26 ?:0, command.isHallPractical26 ?:false, command.hallWrittenMark26 ?:0, command.hallPracticalMark26 ?:0,
                    command.hallObjectiveMark26 ?:0, command.hallSbaMark26 ?:0, command.hallInput526 ?:0, command.isPassSeparately26 ?:false, command.writtenPassMark26 ?:0,
                    command.practicalPassMark26 ?:0, command.objectivePassMark26 ?:0, command.sbaPassMark26 ?:0, command.input5PassMark26 ?:0)
        }
        if (command.classSubObjId27) {
            clsSubId = command.classSubObjId27.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId27, clsSubId, command.weightOnResult27 ?:0, command.passMark27 ?:0, command.sortOrder27 ?:0,
                    command.isExtracurricular27 ?:false, numOfCtExam, command.isCtExam27 ?:false, command.ct1Mark27 ?:0, command.ct2Mark27 ?:0, command.ct3Mark27 ?:0, command.ctEffMark27 ?:0,
                    command.hallMark27 ?:0, command.hallEffMark27 ?:0, command.isHallPractical27 ?:false, command.hallWrittenMark27 ?:0, command.hallPracticalMark27 ?:0,
                    command.hallObjectiveMark27 ?:0, command.hallSbaMark27 ?:0, command.hallInput527 ?:0, command.isPassSeparately27 ?:false, command.writtenPassMark27 ?:0,
                    command.practicalPassMark27 ?:0, command.objectivePassMark27 ?:0, command.sbaPassMark27 ?:0, command.input5PassMark27 ?:0)
        }
        if (command.classSubObjId28) {
            clsSubId = command.classSubObjId28.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId28, clsSubId, command.weightOnResult28 ?:0, command.passMark28 ?:0, command.sortOrder28 ?:0,
                    command.isExtracurricular28 ?:false, numOfCtExam, command.isCtExam28 ?:false, command.ct1Mark28 ?:0, command.ct2Mark28 ?:0, command.ct3Mark28 ?:0, command.ctEffMark28 ?:0,
                    command.hallMark28 ?:0, command.hallEffMark28 ?:0, command.isHallPractical28 ?:false, command.hallWrittenMark28 ?:0, command.hallPracticalMark28 ?:0,
                    command.hallObjectiveMark28 ?:0, command.hallSbaMark28 ?:0, command.hallInput528 ?:0, command.isPassSeparately28 ?:false, command.writtenPassMark28 ?:0,
                    command.practicalPassMark28 ?:0, command.objectivePassMark28 ?:0, command.sbaPassMark28 ?:0, command.input5PassMark28 ?:0)
        }
        if (command.classSubObjId29) {
            clsSubId = command.classSubObjId29.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId29, clsSubId, command.weightOnResult29 ?:0, command.passMark29 ?:0, command.sortOrder29 ?:0,
                    command.isExtracurricular29 ?:false, numOfCtExam, command.isCtExam29 ?:false, command.ct1Mark29 ?:0, command.ct2Mark29 ?:0, command.ct3Mark29 ?:0, command.ctEffMark29 ?:0,
                    command.hallMark29 ?:0, command.hallEffMark29 ?:0, command.isHallPractical29 ?:false, command.hallWrittenMark29 ?:0, command.hallPracticalMark29 ?:0,
                    command.hallObjectiveMark29 ?:0, command.hallSbaMark29 ?:0, command.hallInput529 ?:0, command.isPassSeparately29 ?:false, command.writtenPassMark29 ?:0,
                    command.practicalPassMark29 ?:0, command.objectivePassMark29 ?:0, command.sbaPassMark29 ?:0, command.input5PassMark29 ?:0)
        }
        if (command.classSubObjId30) {
            clsSubId = command.classSubObjId30.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId30, clsSubId, command.weightOnResult30 ?:0, command.passMark30 ?:0, command.sortOrder30 ?:0,
                    command.isExtracurricular30 ?:false, numOfCtExam, command.isCtExam30 ?:false, command.ct1Mark30 ?:0, command.ct2Mark30 ?:0, command.ct3Mark30 ?:0, command.ctEffMark30 ?:0,
                    command.hallMark30 ?:0, command.hallEffMark30 ?:0, command.isHallPractical30 ?:false, command.hallWrittenMark30 ?:0, command.hallPracticalMark30 ?:0,
                    command.hallObjectiveMark30 ?:0, command.hallSbaMark30 ?:0, command.hallInput530 ?:0, command.isPassSeparately30 ?:false, command.writtenPassMark30 ?:0,
                    command.practicalPassMark30 ?:0, command.objectivePassMark30 ?:0, command.sbaPassMark30 ?:0, command.input5PassMark30 ?:0)
        }
        if (command.classSubObjId31) {
            clsSubId = command.classSubObjId31.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId31, clsSubId, command.weightOnResult31 ?:0, command.passMark31 ?:0, command.sortOrder31 ?:0,
                    command.isExtracurricular31 ?:false, numOfCtExam, command.isCtExam31 ?:false, command.ct1Mark31 ?:0, command.ct2Mark31 ?:0, command.ct3Mark31 ?:0, command.ctEffMark31 ?:0,
                    command.hallMark31 ?:0, command.hallEffMark31 ?:0, command.isHallPractical31 ?:false, command.hallWrittenMark31 ?:0, command.hallPracticalMark31 ?:0,
                    command.hallObjectiveMark31 ?:0, command.hallSbaMark31 ?:0, command.hallInput531 ?:0, command.isPassSeparately31 ?:false, command.writtenPassMark31 ?:0,
                    command.practicalPassMark31 ?:0, command.objectivePassMark31 ?:0, command.sbaPassMark31 ?:0, command.input5PassMark31 ?:0)
        }
        if (command.classSubObjId32) {
            clsSubId = command.classSubObjId32.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId32, clsSubId, command.weightOnResult32 ?:0, command.passMark32 ?:0, command.sortOrder32 ?:0,
                    command.isExtracurricular32 ?:false, numOfCtExam, command.isCtExam32 ?:false, command.ct1Mark32 ?:0, command.ct2Mark32 ?:0, command.ct3Mark32 ?:0, command.ctEffMark32 ?:0,
                    command.hallMark32 ?:0, command.hallEffMark32 ?:0, command.isHallPractical32 ?:false, command.hallWrittenMark32 ?:0, command.hallPracticalMark32 ?:0,
                    command.hallObjectiveMark32 ?:0, command.hallSbaMark32 ?:0, command.hallInput532 ?:0, command.isPassSeparately32 ?:false, command.writtenPassMark32 ?:0,
                    command.practicalPassMark32 ?:0, command.objectivePassMark32 ?:0, command.sbaPassMark32 ?:0, command.input5PassMark32 ?:0)
        }
        if (command.classSubObjId33) {
            clsSubId = command.classSubObjId33.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId33, clsSubId, command.weightOnResult33 ?:0, command.passMark33 ?:0, command.sortOrder33 ?:0,
                    command.isExtracurricular33 ?:false, numOfCtExam, command.isCtExam33 ?:false, command.ct1Mark33 ?:0, command.ct2Mark33 ?:0, command.ct3Mark33 ?:0, command.ctEffMark33 ?:0,
                    command.hallMark33 ?:0, command.hallEffMark33 ?:0, command.isHallPractical33 ?:false, command.hallWrittenMark33 ?:0, command.hallPracticalMark33 ?:0,
                    command.hallObjectiveMark33 ?:0, command.hallSbaMark33 ?:0, command.hallInput533 ?:0, command.isPassSeparately33 ?:false, command.writtenPassMark33 ?:0,
                    command.practicalPassMark33 ?:0, command.objectivePassMark33 ?:0, command.sbaPassMark33 ?:0, command.input5PassMark33 ?:0)
        }
        if (command.classSubObjId34) {
            clsSubId = command.classSubObjId34.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId34, clsSubId, command.weightOnResult34 ?:0, command.passMark34 ?:0, command.sortOrder34 ?:0,
                    command.isExtracurricular34 ?:false, numOfCtExam, command.isCtExam34 ?:false, command.ct1Mark34 ?:0, command.ct2Mark34 ?:0, command.ct3Mark34 ?:0, command.ctEffMark34 ?:0,
                    command.hallMark34 ?:0, command.hallEffMark34 ?:0, command.isHallPractical34 ?:false, command.hallWrittenMark34 ?:0, command.hallPracticalMark34 ?:0,
                    command.hallObjectiveMark34 ?:0, command.hallSbaMark34 ?:0, command.hallInput534 ?:0, command.isPassSeparately34 ?:false, command.writtenPassMark34 ?:0,
                    command.practicalPassMark34 ?:0, command.objectivePassMark34 ?:0, command.sbaPassMark34 ?:0, command.input5PassMark34 ?:0)
        }
        if (command.classSubObjId35) {
            clsSubId = command.classSubObjId35.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId35, clsSubId, command.weightOnResult35 ?:0, command.passMark35 ?:0, command.sortOrder35 ?:0,
                    command.isExtracurricular35 ?:false, numOfCtExam, command.isCtExam35 ?:false, command.ct1Mark35 ?:0, command.ct2Mark35 ?:0, command.ct3Mark35 ?:0, command.ctEffMark35 ?:0,
                    command.hallMark35 ?:0, command.hallEffMark35 ?:0, command.isHallPractical35 ?:false, command.hallWrittenMark35 ?:0, command.hallPracticalMark35 ?:0,
                    command.hallObjectiveMark35 ?:0, command.hallSbaMark35 ?:0, command.hallInput535 ?:0, command.isPassSeparately35 ?:false, command.writtenPassMark35 ?:0,
                    command.practicalPassMark35 ?:0, command.objectivePassMark35 ?:0, command.sbaPassMark35 ?:0, command.input5PassMark35 ?:0)
        }
        if (command.classSubObjId36) {
            clsSubId = command.classSubObjId36.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId36, clsSubId, command.weightOnResult36 ?:0, command.passMark36 ?:0, command.sortOrder36 ?:0,
                    command.isExtracurricular36 ?:false, numOfCtExam, command.isCtExam36 ?:false, command.ct1Mark36 ?:0, command.ct2Mark36 ?:0, command.ct3Mark36 ?:0, command.ctEffMark36 ?:0,
                    command.hallMark36 ?:0, command.hallEffMark36 ?:0, command.isHallPractical36 ?:false, command.hallWrittenMark36 ?:0, command.hallPracticalMark36 ?:0,
                    command.hallObjectiveMark36 ?:0, command.hallSbaMark36 ?:0, command.hallInput536 ?:0, command.isPassSeparately36 ?:false, command.writtenPassMark36 ?:0,
                    command.practicalPassMark36 ?:0, command.objectivePassMark36 ?:0, command.sbaPassMark36 ?:0, command.input5PassMark36 ?:0)
        }
        if (command.classSubObjId37) {
            clsSubId = command.classSubObjId37.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId37, clsSubId, command.weightOnResult37 ?:0, command.passMark37 ?:0, command.sortOrder37 ?:0,
                    command.isExtracurricular37 ?:false, numOfCtExam, command.isCtExam37 ?:false, command.ct1Mark37 ?:0, command.ct2Mark37 ?:0, command.ct3Mark37 ?:0, command.ctEffMark37 ?:0,
                    command.hallMark37 ?:0, command.hallEffMark37 ?:0, command.isHallPractical37 ?:false, command.hallWrittenMark37 ?:0, command.hallPracticalMark37 ?:0,
                    command.hallObjectiveMark37 ?:0, command.hallSbaMark37 ?:0, command.hallInput537 ?:0, command.isPassSeparately37 ?:false, command.writtenPassMark37 ?:0,
                    command.practicalPassMark37 ?:0, command.objectivePassMark37 ?:0, command.sbaPassMark37 ?:0, command.input5PassMark37 ?:0)
        }
        if (command.classSubObjId38) {
            clsSubId = command.classSubObjId38.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId38, clsSubId, command.weightOnResult38 ?:0, command.passMark38 ?:0, command.sortOrder38 ?:0,
                    command.isExtracurricular38 ?:false, numOfCtExam, command.isCtExam38 ?:false, command.ct1Mark38 ?:0, command.ct2Mark38 ?:0, command.ct3Mark38 ?:0, command.ctEffMark38 ?:0,
                    command.hallMark38 ?:0, command.hallEffMark38 ?:0, command.isHallPractical38 ?:false, command.hallWrittenMark38 ?:0, command.hallPracticalMark38 ?:0,
                    command.hallObjectiveMark38 ?:0, command.hallSbaMark38 ?:0, command.hallInput538 ?:0, command.isPassSeparately38 ?:false, command.writtenPassMark38 ?:0,
                    command.practicalPassMark38 ?:0, command.objectivePassMark38 ?:0, command.sbaPassMark38 ?:0, command.input5PassMark38 ?:0)
        }
        if (command.classSubObjId39) {
            clsSubId = command.classSubObjId39.toString()
            classSubIds.add(clsSubId)
            examScheduleService.addScheduleMarkToMap(groupScheduleMap, command.classSubObjId39, clsSubId, command.weightOnResult39 ?:0, command.passMark39 ?:0, command.sortOrder39 ?:0,
                    command.isExtracurricular39 ?:false, numOfCtExam, command.isCtExam39 ?:false, command.ct1Mark39 ?:0, command.ct2Mark39 ?:0, command.ct3Mark39 ?:0, command.ctEffMark39 ?:0,
                    command.hallMark39 ?:0, command.hallEffMark39 ?:0, command.isHallPractical39 ?:false, command.hallWrittenMark39 ?:0, command.hallPracticalMark39 ?:0,
                    command.hallObjectiveMark39 ?:0, command.hallSbaMark39 ?:0, command.hallInput539 ?:0, command.isPassSeparately39 ?:false, command.writtenPassMark39 ?:0,
                    command.practicalPassMark39 ?:0, command.objectivePassMark39 ?:0, command.sbaPassMark39 ?:0, command.input5PassMark39 ?:0)
        }
        ClassName className = ClassName.read(params.getLong("classNameId"))
        List<Exam> classExamList = examService.classExamList(shiftExam, className, null)

        String scheduleManageType = params.scheduleManageType
        if (scheduleManageType && scheduleManageType == "UPDATE") {
            examScheduleService.updateAllSchedule(className, classExamList, groupScheduleMap, classSubIds)
        } else {
            examScheduleService.saveAllSchedule(className, classExamList, groupScheduleMap, classSubIds)
        }
        redirect(controller: 'exam',  action: 'classExams', id: shiftExam.id)
    }
}
