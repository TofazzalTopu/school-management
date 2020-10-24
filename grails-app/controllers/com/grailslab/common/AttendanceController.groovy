package com.grailslab.common

import com.grailslab.CommonUtils
import com.grailslab.enums.AvailableRoles
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.viewz.StdEmpView
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import org.joda.time.DateTime

class AttendanceController {

    def schoolService
    def springSecurityService
    def recordDayService

    def index() {
        AcademicYear academicYear = schoolService.workingYear(null)
        String currMonth = CommonUtils.getMonthName(new Date())
        if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.SUPER_ADMIN.value())) {
            render(view: 'attendanceChart', layout: 'adminLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAnyGranted(AvailableRoles.ADMIN.value())) {
            render(view: 'attendanceChart', layout: 'adminLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.ACCOUNTS.value())) {
            render(view: 'attendanceChart', layout: 'moduleCollectionLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.LIBRARY.value())) {
            render(view: 'attendanceChart', layout: 'moduleLibraryLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.HR.value())) {
            render(view: 'attendanceChart', layout: 'moduleHRLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.TEACHER.value())) {
            render(view: 'attendanceChart', layout: 'adminLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.ORGANIZER.value())) {
            render(view: 'attendanceChart', layout: 'moduleWebLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        } else if (SpringSecurityUtils.ifAllGranted(AvailableRoles.SCHOOL_HEAD.value())) {
            render(view: 'attendanceChart', layout: 'adminLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
            return
        }
       /* render(view: 'attendanceChart', layout: 'moduleParentsLayout')*/

    }
    def getAttendance(String currMonth) {
        if (!currMonth) {
            currMonth = CommonUtils.getMonthName(new Date()).toUpperCase()
        }
        YearMonths yearMonths = YearMonths.valueOf(currMonth)
        DateTime todaysDate = new DateTime().withMonthOfYear(yearMonths.serial)
        Date fDate = todaysDate.dayOfMonth().withMinimumValue().toDate()
        Date lDate = todaysDate.dayOfMonth().withMaximumValue().toDate()
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        def userDetails = springSecurityService.principal
        List attnList = new ArrayList()
        if (userDetails.userRef) {
            StdEmpView stdEmpView = StdEmpView.findByStdEmpNo(userDetails.userRef)
            if (stdEmpView) {
                def attnRecordDay = recordDayService.recordDayList(fDate, lDate)
                if (stdEmpView.objType == "employee") {
                    attnList.add([y: '01/04', a: 09.35])
                    attnList.add([y: '02/04', a: 09.35])
                    attnList.add([y: '03/04', a: 09.35])
                    attnList.add([y: '04/04', a: 09.35])
                    attnList.add([y: '05/04', a: 09.35])
                    attnList.add([y: '06/04', a: 09.35])
                    attnList.add([y: '07/04', a: 09.35])
                }
            }

        }
        result.put('attndanceList', attnList)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactive Successfully')
        outPut = result as JSON
        render outPut
    }
}
