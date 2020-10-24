package com.grailslab

import com.grailslab.command.SchoolCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.StudentStatus
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.myapp.config.ConfigKey
import com.grailslab.settings.AcaYear
import com.grailslab.settings.ClassName
import com.grailslab.settings.School
import com.grailslab.stmgmt.Student
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.web.context.ServletContextHolder
import grails.web.servlet.mvc.GrailsParameterMap
import org.grails.web.json.JSONObject

@Transactional
class SchoolService {

    def springSecurityService
    def grailsApplication
    def studentService

    /*School defaultSchool(){
        Long DEFAULT_SCHOOL_ID = CommonUtils.DEFAULT_SCHOOL_ID
        School school = School.read(DEFAULT_SCHOOL_ID)
        return school
    }*/
    static final String[] sortColumns = ['id', 'name', 'address','email','contactNo','academicYear','nonMasking']
    LinkedHashMap schoolPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = School.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
//            eq("activeStatus", ActiveStatus.ACTIVE)
            if (sSearch) {
                or {
                    ilike('name', sSearch)

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
            results.each { School school ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                dataReturns.add([DT_RowId: school.id, 0: serial, 1: school.name, 2: school.address, 3: school.email, 4: school.contactNo, 5:school.academicYear?.value, 6:school.nonMasking, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }


    String getNonMaskingNumber() {
        return School.read(1000)?.nonMasking
    }

    def workingYear(ClassName className) {
        if (springSecurityService.isLoggedIn()) {
            def principal = springSecurityService.principal
            if (className && className.sectionType == "College") {
                return principal.collegeWorkingYear
            } else {
                return principal.academicYear
            }
        } else {
            if (className && className.sectionType == "College") {
                return getAcaWorkingYear(AcaYearType.COLLEGE.key)
            } else {
                return getAcaWorkingYear(AcaYearType.SCHOOL.key)
            }
        }
    }

    def schoolWorkingYear() {
        if (springSecurityService.isLoggedIn()) {
            def principal = springSecurityService.principal
            return principal.academicYear
        } else {
            return getAcaWorkingYear(AcaYearType.SCHOOL.key)
        }
    }

    AcademicYear schoolAdmissionYear() {
        if (springSecurityService.isLoggedIn()) {
            def principal = springSecurityService.principal
            return principal.admissionYear
        } else {
            return getAcaAdmissionYear(AcaYearType.SCHOOL.key)
        }
    }

    def collegeWorkingYear() {
        if (springSecurityService.isLoggedIn()) {
            def principal = springSecurityService.principal
            return principal.collegeWorkingYear
        } else {
            return getAcaWorkingYear(AcaYearType.COLLEGE.key)
        }
    }

    def workingYears(AcaYearType yearType = null) {
        if (springSecurityService.isLoggedIn()) {
            def principal = springSecurityService.principal
            if (yearType == AcaYearType.SCHOOL){
                return [principal.academicYear] as List
            } else if (yearType == AcaYearType.COLLEGE) {
                return [principal.collegeWorkingYear] as List
            } else {
                return [principal.academicYear, principal.collegeWorkingYear] as List
            }
        } else {
            if (yearType == AcaYearType.SCHOOL){
                return [getAcaWorkingYear(AcaYearType.SCHOOL.key)] as List
            } else if (yearType == AcaYearType.COLLEGE) {
                return [getAcaWorkingYear(AcaYearType.COLLEGE.key)] as List
            } else {
                return [getAcaWorkingYear(AcaYearType.SCHOOL.key), getAcaWorkingYear(AcaYearType.COLLEGE.key)] as List
            }
        }
    }

    List acaYearDropDownList(AcaYearType yearType) {
        List academicYearList = new ArrayList()
        List acaYearList
        if (!yearType || yearType == AcaYearType.SCHOOL) {
            acaYearList = AcaYear.findAllByActiveStatusAndYearType(ActiveStatus.ACTIVE, AcaYearType.SCHOOL.name(),[sort: "sortOrder",order:'desc'])
        } else if (yearType == AcaYearType.COLLEGE) {
            acaYearList = AcaYear.findAllByActiveStatusAndYearType(ActiveStatus.ACTIVE, AcaYearType.COLLEGE.name(),[sort: "sortOrder",order:'desc'])
        } else {
            acaYearList = AcaYear.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort: "sortOrder",order:'desc'])
        }
        acaYearList.each {
            academicYearList.add(["key": "Y$it.name", value: it.name])
        }
        return academicYearList
    }

    public String getUserRef(){
        def principal = springSecurityService?.principal
        String userRefStr = principal.userRef ? principal.userRef : principal.username
        return userRefStr
    }
    String reportLogoPath(){
        String schoolName = runningSchool()
        String imageFolder = ServletContextHolder.servletContext.getRealPath('/images') + File.separator
        if (schoolName) {
            imageFolder += schoolName + File.separator
        }
        return imageFolder
    }
    public String runningSchool(){
        return grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_RUNNING)
    }

    public String reportFileName(String reportName){
        String schoolName = runningSchool()
        return schoolName+File.separator+reportName
    }
    public String schoolSubreportDir() {
        String schoolName = runningSchool()
        return ServletContextHolder.servletContext.getRealPath('/reports') + File.separator+ schoolName + File.separator
    }

    //module wise sub folder for reports
    public String reportFileName(String module, String reportName) {
        return module + File.separator + reportName
    }
    public String subreportDir(String module) {
        return ServletContextHolder.servletContext.getRealPath('/reports') + File.separator+ module + File.separator
    }
    public String subreportDir() {
        return ServletContextHolder.servletContext.getRealPath('/reports') + File.separator
    }
    public String imageDir() {
        return ServletContextHolder.servletContext.getRealPath('/images') + File.separator
    }
    public String imageDir(String runningSchool) {
        String imageFolder = ServletContextHolder.servletContext.getRealPath('/images') + File.separator
        if (runningSchool) {
            imageFolder += runningSchool + File.separator
        }
        return imageFolder
    }
    public String storageImagePath(String imagePath) {
        return grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_IMAGE_PATH) + imagePath
    }
    public String storageFilePath(String filePath) {
        return grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_FILE_PATH) + filePath
    }

    Student loggedInStudent() {
        def principal = springSecurityService?.principal
        String userRefStr = principal.userRef
        if (userRefStr) {
            return Student.findByAcademicYearAndStudentStatusAndStudentID(schoolWorkingYear(), StudentStatus.NEW, userRefStr)
        }
        return null
    }

    Employee loggedInEmployee() {
        def principal = springSecurityService?.principal
        String userRefStr = principal.userRef
        if (userRefStr) {
            return Employee.findByEmpID(userRefStr)
        }
        return null
    }

    def getJsonValue(JSONObject json, String key) {
        if(json.has(key)) {
            return json[key]
        } else {
            return null
        }
    }
    boolean hasFeatureAccess() {
        if (runningSchool() != CommonUtils.NARAYANGANJ_IDEAL_SCHOOL) return true
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_SUPER_ADMIN,ROLE_SHIFT_INCHARGE")) return true
        return false
    }
    AcademicYear getAcaWorkingYear(String acaYearType){
        AcaYear acaYear = AcaYear.findByActiveStatusAndIsWorkingYearAndYearType(ActiveStatus.ACTIVE, Boolean.TRUE, acaYearType)
        if (acaYear) {
            return AcademicYear.valueOf("Y$acaYear.name")
        }
        return null
    }
    AcademicYear getAcaAdmissionYear(String acaYearType){
        AcaYear acaYear = AcaYear.findByActiveStatusAndIsAdmissionYearAndYearType(ActiveStatus.ACTIVE, Boolean.TRUE, acaYearType)
        if (acaYear) {
            return AcademicYear.valueOf("Y$acaYear.name")
        }
        return null
    }
    List<String> weeklyHolidays(){
        String weeklyHoliday = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_WEEKLY_HOLIDAY)
        return Arrays.asList(weeklyHoliday?.split("\\s*,\\s*"))
    }

    String getAttendanceCalMethod(){
        String calMethod = grailsApplication.config.getProperty(ConfigKey.GRAILSLAP_GSCHOOL_ATTN_CALC)
    }

    def saveOrUpdate(SchoolCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        result.put('isError', true)
        if (command.hasErrors()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }

        School school
        if (command.id) {
            school = School.get(command.id)
            if (!school) {
                result.put('message', 'School not found')
                return result
            }

            if (School.countByNameAndIdNotEqual(command.name, command.id) > 0) {
                result.put('message', "School with name $command.name already Exist")
                return result
            }

            school.properties = command.properties
            if (!school.validate()) {
                result.put('message', 'Please fill the form correctly')
                return result
            }

            school.save(flush: true)
            result.put('isError', false)
            result.put('message', 'School Updated successfully')
            return result
        }

        school = new School(command.properties)
        if (!command.validate()) {
            result.put('message', 'Please fill the form correctly')
            return result
        }

        int numOfSchool = School.countByName(command.name)
        if (numOfSchool > 0) {
            result.put('message', "School with name $command.name already Exist")
            return result
        }

        School savedCurr = school.save(flush: true)
        if (!savedCurr) {
            result.put('message', 'Please fill the form correctly')
            return result
        }
        result.put('isError', false)
        result.put('message', 'School Create successfully')
        return result
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        School school = School.get(id)
        if (!school) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        school.delete()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "School deleted successfully.")
        return result
    }
}
