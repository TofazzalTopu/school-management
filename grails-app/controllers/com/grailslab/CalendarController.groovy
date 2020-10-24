package com.grailslab


import grails.converters.JSON

class CalendarController {
    def calenderService
    def springSecurityService
    def noticeBoardService
    def classNameService
    def classRoutineService
    def schoolService
    def uploadService

    def index() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        render (view: '/open/calendar/index', layout: layoutName)
    }

    def notice() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def noticeList = noticeBoardService.noticeBoard()
        render(view: '/open/calendar/notice', layout: layoutName, model: [noticeList:noticeList])
    }

    def classRoutine() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def classNameList = classNameService.allClassNames()
        render(view: '/open/classRoutine', layout: layoutName, model: [classNameList: classNameList])
    }

    def examRoutine() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        render(view: '/open/examRoutine', layout: layoutName)
    }

    def routineEvents(){
        List publicUrlEvents = classRoutineService.getRoutineAsEvents(params)
        String output = publicUrlEvents as JSON
        render output
    }
    def publicEvents(){
        Date sStartFrom = CommonUtils.calendarEventDateMonthViewFormat(params.start, true)
        if (!sStartFrom) sStartFrom = CommonUtils.calendarEventDateWeekViewFormat(params.start, true)
        Date sStartEnd = CommonUtils.calendarEventDateMonthViewFormat(params.end, false)
        if (!sStartEnd) sStartEnd = CommonUtils.calendarEventDateWeekViewFormat(params.end, false)
        List publicUrlEvents = calenderService.getPublicEvents(sStartFrom, sStartEnd)
        String output = publicUrlEvents as JSON
        render output
    }

    def downloadFile() {
        if ( params.identifier == null) {
            flash.message = "File not found."
            redirect (action:'index')
        } else {
            byte[] buffer = uploadService.getFileInByte(params.identifier, response)
            if(buffer){
                response.outputStream << buffer
                response.outputStream.flush()
            }  else {
                flash.message = "File not found."
                redirect (action:'index')
            }
        }
    }

    def examRoutineFile() {
        String filePath = '/examroutine/final_routine.jpg'
        byte[] buffer = uploadService.getSingleFileInByte(filePath, response)
        if(buffer){
            response.outputStream << buffer
            response.outputStream.flush()
        }
    }
}
