package myschool

class UrlMappings {

    static mappings = {

        "/teacher/$action?/$id?(.${format})?"(controller: 'teacher')
        "/teacher/markEntry/$action?/$id?(.${format})?"(controller: 'markEntry')
        "/teacher/previousTerm/$action?/$id?(.${format})?"(controller: 'previousTerm')
        "/teacher/esbp/$action?/$id?(.${format})?"(controller: 'eas')


        "/admin/$action?/$id?(.${format})?"(controller: 'admin')
        "/admin/user/$action?/$id?(.${format})?"(controller: 'user')
        "/admin/className/$action?/$id?(.${format})?"(controller: 'className')
        "/admin/classPeriod/$action?/$id?(.${format})?"(controller: 'classPeriod')
        "/admin/examPeriod/$action?/$id?(.${format})?"(controller: 'examPeriod')
        "/admin/classRoom/$action?/$id?(.${format})?"(controller: 'classRoom')
        "/admin/section/$action?/$id?(.${format})?"(controller: 'section')
        "/admin/gradePoints/$action?/$id?(.${format})?"(controller: 'gradePoints')
        "/admin/profession/$action?/$id?(.${format})?"(controller: 'profession')
        "/admin/subject/$action?/$id?(.${format})?"(controller: 'subject')
        "/admin/lessonWeek/$action?/$id?(.${format})?"(controller: 'lessonWeek')
        "/admin/notice/$action?/$id?(.${format})?"(controller: 'noticeBoard')
        "/admin/acaYear/$action?/$id?(.${format})?"(controller: 'acaYear')
        "/admin/school/$action?/$id?(.${format})?"(controller: 'school')
        "/admin/guideTeacher/$action?/$id?(.${format})?"(controller: 'guideTeacher')

        //Public website content
        "/web/organizer/$action?/$id?(.${format})?"(controller: 'organizer')
        "/web/content/$action?/$id?(.${format})?"(controller: 'allContent')
        "/web/feature/$action?/$id?(.${format})?"(controller: 'feature')
        "/web/timeLine/$action?/$id?(.${format})?"(controller: 'timeLine')
        "/web/mgmtVoice/$action?/$id?(.${format})?"(controller: 'mgmtVoice')
        "/web/success/$action?/$id?(.${format})?"(controller: 'successStory')
        "/web/quickLink/$action?/$id?(.${format})?"(controller: 'quickLink')
        "/web/slider/$action?/$id?(.${format})?"(controller: 'sliderImage')
        "/web/gallery/$action?/$id?(.${format})?"(controller: 'gallery')
        "/web/festival/$action?/$id?(.${format})?"(controller: 'festival')


//    student mgmt
        "/stmgst/registration/$action?/$id?(.${format})?"(controller: 'registration')
        "/stmgst/student/$action?/$id?(.${format})?"(controller: 'student')
        "/stmgst/college/$action?/$id?(.${format})?"(controller: 'college')
        "/stmgst/report/$action?/$id?(.${format})?"(controller: 'studentReport')
        "/stmgst/classSubjects/$action?/$id?(.${format})?"(controller: 'classSubjects')
        "/stmgst/studentSubjects/$action?/$id?(.${format})?"(controller: 'studentSubjects')
        "/stmgst/transferSubjects/$action?/$id?(.${format})?"(controller: 'transferSubjects')
        "/stmgst/classRoutine/$action?/$id?(.${format})?"(controller: 'classRoutine')

        "/sms/message/$action?/$id?(.${format})?"(controller: 'messaging')

        "/exam/$action?/$id?(.${format})?"(controller: 'exam')
        "/exam/schedule/$action?/$id?(.${format})?"(controller: 'examSchedule')
        "/exam/mark/$action?/$id?(.${format})?"(controller: 'examMark')
        "/exam/result/$action?/$id?(.${format})?"(controller: 'result')
        "/exam/tabulation/$action?/$id?(.${format})?"(controller: 'tabulation')
        "/exam/report/$action?/$id?(.${format})?"(controller: 'examReport')

//    applicants mgmt
        "/applicant/$action?/$id?(.${format})?"(controller: 'admissionForm')
        "/applicant/form/$action?/$id?(.${format})?"(controller: 'onlineRegistration')
        "/applicant/report/$action?/$id?(.${format})?"(controller: 'onlineRegistrationReport')

//    accounts
        "/collection/$action?/$id?(.${format})?"(controller: 'collections')
        "/collection/chart/$action?/$id?(.${format})?"(controller: 'chart')
        "/collection/discount/$action?/$id?(.${format})?"(controller: 'discount')
        "/collection/invoiceDay/$action?/$id?(.${format})?"(controller: 'invoiceDay')
        "/collection/bkash/$action?/$id?(.${format})?"(controller: 'bkash')
        "/collection/report/$action?/$id?(.${format})?"(controller: 'accountsReport')

//     hrm
        "/hrm/$action?/$id?(.${format})?"(controller: 'hr')
        "/hrm/settings/$action?/$id?(.${format})?"(controller: 'hrSettings')
        "/hrm/employee/$action?/$id?(.${format})?"(controller: 'employee')
        "/hrm/salarySetup/$action?/$id?(.${format})?"(controller: 'salarySetup')
        "/hrm/hrCategory/$action?/$id?(.${format})?"(controller: 'hrCategory')
        "/hrm/designation/$action?/$id?(.${format})?"(controller: 'hrDesignation')
        "/hrm/officeHour/$action?/$id?(.${format})?"(controller: 'hrPeriod')
        "/hrm/salaryReport/$action?/$id?(.${format})?"(controller: 'salaryReport')

//        Salary
        "/salary/areaSetup/$action?/$id?(.${format})?"(controller: 'areaSetup')
        "/salary/extraClass/$action?/$id?(.${format})?"(controller: 'extraClass')
        "/salary/setup/$action?/$id?(.${format})?"(controller: 'salarySetup')
        "/salary/attendance/$action?/$id?(.${format})?"(controller: 'salaryAttendance')
        "/salary/advance/$action?/$id?(.${format})?"(controller: 'salAdvance')
        "/salary/pc/$action?/$id?(.${format})?"(controller: 'salPc')
        "/salary/dps/$action?/$id?(.${format})?"(controller: 'salDps')
        "/salary/increment/$action?/$id?(.${format})?"(controller: 'salIncrement')
        "/salary/bonus/$action?/$id?(.${format})?"(controller: 'salaryBonus')

//   library
        "/library/$action?/$id?(.${format})?"(controller: 'library')
        "/library/libraryMember/$action?/$id?(.${format})?"(controller: 'libraryMember')
        "/library/category/$action?/$id?(.${format})?"(controller: 'bookCategory')
        "/library/author/$action?/$id?(.${format})?"(controller: 'bookAuthor')
        "/library/publisher/$action?/$id?(.${format})?"(controller: 'bookPublisher')
        "/library/stock/$action?/$id?(.${format})?"(controller: 'bookStock')
        "/library/transaction/$action?/$id?(.${format})?"(controller: 'bookTransaction')
        "/library/report/$action?/$id?(.${format})?"(controller: 'libraryReport')
        "/library/setting/$action?/$id?(.${format})?"(controller: 'librarySetting')
        "/library/onlineLibrary/$action?/$id?(.${format})?"(controller: 'onlineLibrary')

//   lesson & Feedback
        "/lessonPlan/lesson/$action?/$id?(.${format})?"(controller: 'lesson')
        "/lessonPlan/feedback/$action?/$id?(.${format})?"(controller: 'feedback')
        "/lessonPlan/report/$action?/$id?(.${format})?"(controller: 'feedbackReport')

        //Attendance Plugin
        "/attn/holiday/$action?/$id?(.${format})?"(controller: 'holiday')
        "/attn/recordDay/$action?/$id?(.${format})?"(controller: 'recordDay')
        "/attn/student/$action?/$id?(.${format})?"(controller: 'attnStudent')
        "/attn/employee/$action?/$id?(.${format})?"(controller: 'attnEmployee')
        "/attn/report/$action?/$id?(.${format})?"(controller: 'attendanceReport')
        "/attn/attnPeriod/$action?/$id?(.${format})?"(controller: 'attnPeriod')

        // For Parent Login
        "/portal/$action?/$id?(.${format})?"(controller: 'portal')

        "/LeaveMgmt/name/$action?/$id?(.${format})?"(controller: 'leaveName')
        "/LeaveMgmt/template/$action?/$id?(.${format})?"(controller: 'leaveTemplate')
        "/LeaveMgmt/approval/$action?/$id?(.${format})?"(controller: 'leaveApproval')
        "/LeaveMgmt/report/$action?/$id?(.${format})?"(controller: 'leaveReport')

        "/profile/$action?/$id?(.${format})?"(controller: 'profile')
        "/attendance/$action?/$id?(.${format})?"(controller: 'attendance')
        "/leave/$action?/$id?(.${format})?"(controller: 'leave')


        "/"(controller: 'home', action: 'index')
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        "500"(view: '/error')
    }
}
