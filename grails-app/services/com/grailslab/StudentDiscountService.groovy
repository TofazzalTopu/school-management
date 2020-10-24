package com.grailslab

import com.grailslab.accounting.FeeItems
import com.grailslab.accounting.StudentDiscount
import com.grailslab.command.StudentScholarshipCommand
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.Section
import com.grailslab.stmgmt.Student
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class StudentDiscountService {
    def schoolService
    def springSecurityService
    def studentDiscountService
    def messageSource

    static final String[] sortColumns = ['id', 'std.studentID', 'std.section', 'fItem.code', 'discountPercent']

    LinkedHashMap studentDiscountPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_DESC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        //Search string, use or logic to all fields that required to include
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def workingYears = schoolService.workingYears()
        Section section
        ClassName className
        if (params.sectionName) {
            section = Section.read(params.getLong('sectionName'))
        }
        if (params.className) {
            className = ClassName.read(params.getLong('className'))
        }

        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = StudentDiscount.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('student', 'std')
            createAlias('feeItems', 'fItem')
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (academicYear) {
                    eq("academicYear", academicYear)
                } else {
                    'in'("academicYear", workingYears)
                }
                if (section) {
                    eq("std.section", section)
                }
                if (className) {
                    eq("std.className", className)
                }

            }
            if (sSearch) {
                or {
                    ilike('std.studentID', sSearch)
                    ilike('std.name', sSearch)
                    ilike('fItem.name', sSearch)
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
            Student student
            FeeItems feeItems
            Double amount = 0
            Double discountAmount = 0
            Double netPayable = 0
            results.each { StudentDiscount studentDiscount ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                student = studentDiscount.student
                feeItems = studentDiscount.feeItems
                amount = feeItems?.amount
                discountAmount = studentDiscount.amount
                netPayable = amount - discountAmount
                dataReturns.add([DT_RowId: studentDiscount.id, 0: serial, 1: "${student.studentID} - ${student.name}", 2: "${student.section.name} - ${student.className.name}", 3: "${feeItems.code} - ${feeItems.name}", 4: amount, 5: "$studentDiscount.amount ($studentDiscount.discountPercent %)", 6: netPayable, 7: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    StudentDiscount byStudentAndFeeItem(Student student, FeeItems feeItems, Long discountId = null) {
        StudentDiscount discount = null
        if (discountId) { //update Discount case
            discount = StudentDiscount.findByStudentAndFeeItemsAndActiveStatusAndIdNotEqual(student, feeItems, ActiveStatus.ACTIVE, discountId)
        } else {
            discount = StudentDiscount.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
        }
        return discount
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        StudentDiscount studentDiscount = StudentDiscount.get(id)
        if (!studentDiscount) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Discount not found")
            return result
        }
        studentDiscount.activeStatus = ActiveStatus.INACTIVE
        studentDiscount.updatedBy = springSecurityService.principal?.username
        studentDiscount.save()
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Discount deleted successfully.")
        return result
    }

    def saveOrUpdate(Long discountId, Student student, FeeItems feeItems, Double amount) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username

        Double feeAmount = 0
        Double discountPercentage = 0

        StudentDiscount discount
        String message
        if (discountId) {
            discount = StudentDiscount.get(discountId)
            if (!discount) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }
            feeItems = discount.feeItems
            feeAmount = feeItems.amount
            discountPercentage = Math.round((amount * 100)/feeAmount)

            discount.amount = amount
            discount.discountPercent = discountPercentage
            discount.updatedBy = createOrUpdateBy
            message = "Discount updated successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)

        } else {
            discount = studentDiscountService.byStudentAndFeeItem(student, feeItems)
            if (discount) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Already ${discount.discountPercent}% discount added. You can Edit.")
                return result
            }
            feeAmount = feeItems.amount
            discountPercentage = Math.round((amount * 100)/feeAmount)

            discount = new StudentDiscount(student: student, discountPercent: discountPercentage, amount: amount, feeItems: feeItems, createdBy: createOrUpdateBy, schoolId: null,
                    academicYear: student.academicYear, activeStatus: ActiveStatus.ACTIVE)
            message = "Discount added successfully."
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }
        discount.save()
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def saveScholarship(StudentScholarshipCommand command) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService?.principal.username

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Student student = command.student
        if (!student) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        student.scholarshipObtain = true
        student.scholarshipType = command.scholarshipType
        student.updatedBy = createOrUpdateBy
        student.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Scholarship added successfully")
        return result
    }

    def scholarshipDelete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        Student student = Student.get(id)
        if (!student) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "scholarship not found")
            return result
        }
        student.scholarshipObtain = false
        student.updatedBy = springSecurityService.principal?.username
        student.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "scholarship deleted successfully.")
        return result
    }
}