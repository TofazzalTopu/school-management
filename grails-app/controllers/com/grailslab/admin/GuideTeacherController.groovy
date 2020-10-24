package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrStaffCategory
import com.grailslab.report.CusJasperReportDef
import com.grailslab.settings.GuideTeacher
import com.grailslab.viewz.GuideTeacherView
import grails.converters.JSON
import com.grailslab.enums.*

class GuideTeacherController {
    def schoolService
    def guideTeacherService
    def baseService
    def jasperService

    def index() {
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        AcademicYear academicYear = schoolService.workingYear(null)
        LinkedHashMap resultMap = guideTeacherService.guideTeacherPaginateList(params)
        def hrStaffCategoryList = HrStaffCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: 'sortOrder'])
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/guideTeacher/index', model: [hrCategoryList: hrStaffCategoryList, academicYearList: academicYearList, dataReturn: null, totalCount: 0, workingYear: academicYear])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/guideTeacher/index', model: [hrCategoryList: hrStaffCategoryList, academicYearList: academicYearList, dataReturn: resultMap.results, totalCount: totalCount, workingYear: academicYear])
    }

    def list() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = guideTeacherService.guideTeacherPaginateList(params)
        if(!resultMap || resultMap.totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount =resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def saveOrUpdate() {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut

        Long employeeId = params.getLong('employee')
        String studentIds = params.studentIds
        String academicYear = params.academicYear
        if (!employeeId || !studentIds){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Please select Teacher, academic year and student ids")
            outPut = result as JSON
            render outPut
            return
        }

        Long objId = params.getLong('id')
        if (objId) {
            /*GuideTeacher guideTeacher = GuideTeacher.get(objId)
            if (!guideTeacher) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Item not found")
                outPut = result as JSON
                render outPut
                return
            }
            guideTeacher.employeeId = employeeId
            guideTeacher.studentIds = studentIds
            guideTeacher.academicYear= "Y2020"*/
            GuideTeacher savedGuide = guideTeacherService.update(objId, employeeId, "Y2020", studentIds)
            println(savedGuide.id)
        } else {
            GuideTeacher savedGuide = new GuideTeacher(employeeId: employeeId, studentIds: studentIds, academicYear: "Y2020", activeStatus: "ACTIVE").save(flash: true)
            println(savedGuide.id)
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Guide Teacher added successfully")
        outPut = result as JSON
        render outPut
    }

    def edit(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        GuideTeacherView guideTeacher = GuideTeacherView.read(id)
        if (!guideTeacher) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,guideTeacher)
        outPut = result as JSON
        render outPut

    }
    def printStudentList(Long id){
        String hrCategory
        Long hrStaffCatId = params.getLong('hrCategory')
        if (hrStaffCatId) {
            HrStaffCategory staffCategory = HrStaffCategory.read(hrStaffCatId)
            if (staffCategory) {
                hrCategory = staffCategory.keyName
            }
        }

        String printOptionType = params.printOptionType
        String academicYear = params.academicYear
        if (!academicYear) academicYear = "Y2020"

        String reportFileName = "guideTeacher"
        String sqlParam = getGuideTeacherQueryStr(academicYear, hrCategory, id)

        Map paramsMap = new LinkedHashMap()
        paramsMap.put('REPORT_LOGO', schoolService.reportLogoPath())
        paramsMap.put('SUBREPORT_DIR', schoolService.subreportDir())
        paramsMap.put('sqlParam', sqlParam)
        paramsMap.put('year', "2020")
        paramsMap.put('schoolName', grailsApplication.config.getProperty("app.school.name"))
        paramsMap.put('schoolAddress', grailsApplication.config.getProperty("app.report.address.line1"))

        CusJasperReportDef jasperReport = new CusJasperReportDef()
        jasperReport.outputFilename = "guide_teacher"
        jasperReport.reportName = reportFileName
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
    private String getGuideTeacherQueryStr(String academicYear, String hrCategory, Long employeeId) {
        String conStr = " and tec.academic_year = '${academicYear}'"
        if (employeeId) {
            conStr += " and emp.id = ${employeeId}"
        }
        if (hrCategory) {
            conStr += " and hr_staff_category like '%${hrCategory}%'"
        }

        String querySql = """
            select tec.academic_year, tec.student_ids, emp.empid, emp.hr_staff_category, emp.name, emp.mobile
                from guide_teacher tec
                inner join employee emp on tec.employee_id = emp.id
                where  tec.active_status = 'ACTIVE' ${conStr}
                order by emp.sort_order asc;
        """
        return querySql
    }
}
