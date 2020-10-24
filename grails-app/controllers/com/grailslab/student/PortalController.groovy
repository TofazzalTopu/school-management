package com.grailslab.student

import com.grailslab.CommonUtils
import com.grailslab.accounting.*
import com.grailslab.command.ByClassStepCommand
import com.grailslab.enums.AcaYearType
import com.grailslab.enums.AccountType
import com.grailslab.enums.FeeIterationType
import com.grailslab.enums.FeeType
import com.grailslab.enums.PaymentType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.*
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import org.grails.web.json.JSONObject
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate

class PortalController {

    def lessonService
    def classSubjectsService
    def shiftExamService
    def studentService
    def collectionsService
    def sectionService
    def springSecurityService
    def sequenceGeneratorService
    def messageSource
    def schoolService
    def portalService
    def classNameService

    def index() {
        render (view: '/common/dashboard', layout:'moduleParentsLayout')
    }
    def studentList(){

        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        def classList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: 'pStudent', model: [academicYearList:academicYearList, workingYear:academicYear, classList:classList])

    }
    def lessonPlan() {
        Student student = schoolService.loggedInStudent()
        if (!student) {
            redirect(action: 'index')
            return
        }
        Section section = student.section

        def lessonWeekList = lessonService.lessonWeeksList(section)
        Integer loadWeek = null
        if(params.weekNo){
            loadWeek = params.getInt("weekNo")
        } else {
            LocalDate toDay = new LocalDate()
            if(toDay.getDayOfWeek()==DateTimeConstants.FRIDAY){
                toDay = toDay.minusDays(1)
            }else if(toDay.getDayOfWeek()==DateTimeConstants.SATURDAY){
                toDay = toDay.minusDays(2)
            }
            LessonWeek lessonWeek=lessonService.getWeek(toDay.toDate())
            if (lessonWeek) {
                loadWeek = lessonWeek.weekNumber
            } else {
                if (lessonWeekList) {
                    loadWeek = lessonWeekList.first().id
                } else {
                    loadWeek = 1
                }
            }
        }
        SubjectName loadSubject = null
        if(params.subjectId){
            loadSubject = SubjectName.read(params.getLong('subjectId'))
            if(!loadSubject){
                redirect(action: 'index')
                return
            }
        }
        ClassName className = section.className
        def subjectList = classSubjectsService.subjectDropDownList(className)

        render(view: 'lessonList', model: [section: section, subjectList: subjectList,loadSubject:loadSubject,lessonWeekList:lessonWeekList,loadWeek:loadWeek])
    }

    def chart(Long id){
        def examNameList = shiftExamService.examNameDropDown()
        if (!examNameList) {
            flash.message = "No result publish yet this year."
            render(view: 'chart', model: [examNameList: null])
            return
        }
        ShiftExam shiftExam
        if (id) {
            shiftExam = ShiftExam.read(id)
        } else {
            def sExam = examNameList.first()
            shiftExam = ShiftExam.read(sExam.id)
        }
        if (!shiftExam) {
            flash.message = "No result publish yet this year."
            render(view: 'chart', model: [examNameList: null])
            return
        }
        /*Student loggedStudent = schoolService.loggedInStudent()
        Exam stdExam = Exam.findByShiftExamAndSectionAndActiveStatusAndExamStatusInList(shiftExam, loggedStudent.section, ActiveStatus.ACTIVE, ExamStatus.resultPublishingList())
        if (!stdExam) {
            flash.message = "No result publish yet this year."
            render(view: 'chart', model: [examNameList: null])
            return
        }*/

        String subjects = "bangla, Englist, Mathematics, bangla, Englist, Mathematics, bangla, Englist, Mathematics, bangla, Englist, Mathematics"
        render(view: 'chart', model: [examNameList: examNameList, subjects: subjects])
    }

    def attendance(){
        AcademicYear academicYear = schoolService.workingYear(null)
        String currMonth = CommonUtils.getMonthName(new Date())
        render(view: 'stuAttendanceChart', layout: 'moduleParentsLayout', model: [currMonth: currMonth, academicYear: academicYear?.value])
    }

    def gradeChart(){
        def examNameList = shiftExamService.examNameDropDown()
        if (!examNameList) {
            flash.message = "No result publish yet this year."
            render(view: 'gradeChart', model: [examNameList: null])
            return
        }
        render(view: 'gradeChart', model: [examNameList: examNameList])
    }

    def resultComparison(){
        def examNameList = shiftExamService.examNameDropDown()
        if (!examNameList) {
            flash.message = "No result publish yet this year."
            render(view: 'resultComparison', model: [examNameList: null])
            return
        }
        render(view: 'resultComparison', model: [examNameList: examNameList])
    }

    def profile(){
        Student student = schoolService.loggedInStudent()
        Registration registration = student?.registration

        render(view: 'profile', model: [student:student, registration: registration])
    }

    def paymentList() {
        boolean paid = params.getBoolean('paid')
        Student student = schoolService.loggedInStudent()
        def studentFeeList = collectionsService.studentFeeList(student, null,null)
        List feeItemList = new ArrayList()
        List<FeePayments> feePaymentList
        FeePayments feePayments
        List itemDetails
        Invoice invoice
        if(paid){
            studentFeeList.each { FeeItems feeItems ->
                if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    feePaymentList = collectionsService.paymentItemDetails(student, feeItems)
                    if (feePaymentList) {
                        itemDetails = new ArrayList()
                        feePaymentList.each { detail ->
                            invoice = detail.invoice
                            itemDetails.add([item: detail.itemDetailName, amount: detail.amount,discount:detail.discount,netPayment:detail.netPayment,quantity:detail.quantity,totalPayment:detail.totalPayment,invoiceNo:invoice?.invoiceNo, invoiceDate:CommonUtils.getUiDateStr(invoice?.invoiceDate)])
                        }
                        feeItemList.add([item: feeItems.name, hasItemDetail: true, itemDetails: itemDetails])
                    }
                } else if (feeItems.iterationType == FeeIterationType.RECEIVE || feeItems.iterationType == FeeIterationType.YEARLY) {
                    feePayments = FeePayments.findByStudentAndFeeItemsAndActiveStatus(student, feeItems,ActiveStatus.ACTIVE)
                    if (feePayments) {
                        invoice=feePayments.invoice
                        feeItemList.add([item: feeItems.name, hasItemDetail: false, amount: feePayments.amount, discount:feePayments.discount,netPayment:feePayments.netPayment,quantity:feePayments.quantity,totalPayment:feePayments.totalPayment,invoiceNo:invoice?.invoiceNo, invoiceDate:CommonUtils.getUiDateStr(invoice?.invoiceDate)])
                    }
                }
            }
        }else {
            studentFeeList.each {FeeItems feeItems ->
                if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    feePaymentList = collectionsService.paymentItemDetails(student, feeItems)
                    if (feePaymentList) {
                        feePayments=feePaymentList.last()
                        itemDetails = new ArrayList()
                        feePaymentList.each { detail ->
                            itemDetails.add([item: 'monthName', amount: detail.amount,discount:detail.discount,netPayment:detail.netPayment])
                        }
                        feeItemList.add([item: feeItems.name, hasItemDetail: true, itemDetails: itemDetails])
                    }
                } else if (feeItems.iterationType == FeeIterationType.RECEIVE || feeItems.iterationType == FeeIterationType.YEARLY) {
                    feePayments = FeePayments.findByStudentAndFeeItemsAndActiveStatus(student, feeItems,ActiveStatus.ACTIVE)
                    if (!feePayments) {
                        feeItemList.add([item: feeItems.name, hasItemDetail: false, amount: feeItems.amount, discount:feeItems.discount,netPayment:feeItems.netPayable])
                    }
                }
            }

        }
        render(view: 'paymentList', model: [feeItemList: feeItemList])
    }

    def onlinePayment() {
        Student student = schoolService.loggedInStudent()
        Invoice invoice = Invoice.findByStudentAndActiveStatus(student, ActiveStatus.BKASH)
        if (invoice) {
            List invoiceShowList = new ArrayList()
            def invoiceDetailses = InvoiceDetails.findAllByInvoice(invoice)
            for (invoiceDetail in invoiceDetailses) {
                invoiceShowList.add([feeItem: invoiceDetail.feeItems.name, description: invoiceDetail.description, quantity: invoiceDetail.quantity, amount: invoiceDetail.amount, discount: invoiceDetail.discount, netPayment: invoiceDetail.netPayment, totalPayment: invoiceDetail.totalPayment])
            }
            render(view: 'onlinePayment', model: [isAccount: false, hasPending: true, student: student, invoiceId: invoice?.id, invoiceAmount: invoice.payment, invoiceShowList: invoiceShowList])
            return
        } else {
            String quickFee = '3'
            Integer feePaidCriteria = null
            List dataArray = new ArrayList()

            Double amount = 0
            Double discountAmount = 0
            Double discount = 0
            Double payable = 0
            Double totalPayable = 0
            Double totalPaid = 0


            Long feeItemId
            StudentDiscount studentDiscount
            FeePayments feePayments
            List<FeePayments> feePaymentList
            Integer quantity
            Double paymentAmount
            Boolean isAlreadyPaid = false
            def feeIdsList = collectionsService.studentFeeList(student, quickFee, feePaidCriteria)
            List previousPayments = new ArrayList()
            List<ItemsDetail> itemMonthList
            List itemDetails
            ItemsDetail lastPaidMonth
            ItemsDetail nextPaidMonth
            feeIdsList.each { FeeItems feeItems ->
                itemMonthList = null
                if (feeItems) {
                    if(!(student.scholarshipObtain && student.scholarshipPercent.doubleValue()==100)) {
                        amount = feeItems.amount
                        if(student.scholarshipObtain && student.scholarshipPercent.doubleValue()>=0){
                            discount = student.scholarshipPercent
                            if (discount > 0 && discount <= 100) {
                                discountAmount = amount * discount * 0.01
                            }
                            payable = amount - discountAmount
                        }else {
                            studentDiscount = StudentDiscount.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                            if (studentDiscount) {
                                discount = studentDiscount.discountPercent
                                if (discount > 0 && discount <= 100) {
                                    discountAmount = amount * discount * 0.01
                                }
                                payable = amount - discountAmount
                            } else {
                                discount = feeItems.discount
                                payable = feeItems.netPayable
                            }
                        }
                        //                paymentStatus = "Not Paid"
                        quantity = 1
                        paymentAmount = payable
                        isAlreadyPaid = false
                        if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                            itemMonthList = collectionsService.feeItemDetailsDDList(feeItems)
                            feePaymentList = collectionsService.paymentItemDetails(student, feeItems)
                            if (feePaymentList) {
                                if (feePaymentList.size() == itemMonthList.size()) {
                                    quantity = 0
                                    paymentAmount = 0
                                    payable = 0
                                    isAlreadyPaid = true
                                }else {
                                    lastPaidMonth = feePaymentList.last().itemsDetail
                                    int cOrder
                                    if(lastPaidMonth && lastPaidMonth.continuityOrder){
                                        cOrder = lastPaidMonth.continuityOrder+1
                                        nextPaidMonth = ItemsDetail.findByFeeItemsAndContinuityOrderAndActiveStatus(feeItems,cOrder,ActiveStatus.ACTIVE)
                                    }else {
                                        cOrder =lastPaidMonth.sortPosition+1
                                        nextPaidMonth = ItemsDetail.findByFeeItemsAndSortPositionAndActiveStatus(feeItems,cOrder,ActiveStatus.ACTIVE)
                                    }

                                }
                                itemDetails = new ArrayList()
                                feePaymentList.each { detail ->
                                    itemDetails.add([item: detail.itemsDetail.name, amount: detail.totalPayment])
                                }
                                previousPayments.add([item: feeItems.name, hasItemDetail: true, itemDetails: itemDetails])
                            }
                        } else if (feeItems.iterationType == FeeIterationType.YEARLY) {
                            feePayments = FeePayments.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                            if (feePayments) {
                                previousPayments.add([item: feeItems.name, amount: feePayments.totalPayment, hasItemDetail: false])
                                quantity = 0
                                paymentAmount = 0
                                payable = 0
                                isAlreadyPaid = true
                            }
                        } else if (feeItems.iterationType == FeeIterationType.RECEIVE) {
                            feePaymentList = FeePayments.findAllByStudentAndFeeItemsAndActiveStatus(student, feeItems,ActiveStatus.ACTIVE)
                            if (feePaymentList) {
                                totalPaid = 0
                                feePaymentList.each {payment ->
                                    totalPaid+= payment.totalPayment
                                }
                                previousPayments.add([item: feeItems.name, amount: totalPaid, hasItemDetail: false])
                            }
                        }
                        totalPayable += payable
                        dataArray.add([id: feeItems.id, name: "${feeItems.code} - ${feeItems.name}", quantity: quantity, amount: amount, discount: discount, payable: paymentAmount, netPayable: quantity * paymentAmount , isAlreadyPaid: isAlreadyPaid, itemMonthList: itemMonthList, nextPaidMonth: nextPaidMonth])
                    }
                }
                payable = 0
            }
            render(view: 'onlinePayment',  model: [isAccount: false, hasPending: false, dataArray: dataArray, student: student, totalPayable: totalPayable, previousPayments: previousPayments])
        }
    }

    def payInvoiceOnline(ByClassStepCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: 'alreadyPaidMsg')
            return
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        def feeIdsList = command.feeItemIds.split(',')
        Student student = command.studentId

        Long feeItemId
        FeeItems feeItems
        StudentDiscount studentDiscount

        String inputQnty
        String inputPayable

        Integer quantity
        Double amount
        Double discount
        Double discountAmount
        Double netPayment

        FeePayments feePayments
        List<ItemsDetail> paymentDetailList
        InvoiceDetails invoiceDetails
        ItemsDetail itemsDetail
        List<ItemsDetail> itemsDetailList
        String description
        List<FeePayments> paymentsList = new ArrayList<FeePayments>()
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<InvoiceDetails>()
        List invoiceShowList = new ArrayList()

        Double feeAmount
        Double feePayment
        Double totalFeeAmount =0
        Double totalFeePayment = 0
        List<String> itemNameList = new ArrayList<String>()
        List<String> alreadyPaidItems = new ArrayList<String>()
        Boolean isAlreadyFeePaid = false
        String discountText;

        feeIdsList.each { String idx ->
            description=''
            discountText=''
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            inputQnty = params."quantity${feeItemId}"
            inputPayable = params."payable${feeItemId}"
            quantity = 0
            if (inputQnty) {
                quantity = Integer.parseInt(inputQnty)
            }
            feeAmount = 0
            if (inputPayable) {
                feeAmount = Double.valueOf(inputPayable)
            }

            if (feeItems && quantity > 0 && feeAmount > 0) {
                amount = feeItems.amount
                discount = amount - feeAmount
                netPayment = amount * quantity
                feePayment = feeAmount * quantity

                if(discount >0 && schoolService.runningSchool() == 'bailyschool'){
                    discountText=" [less ${discount * quantity}]"
                }
                if (feeItems.iterationType == FeeIterationType.RECEIVE) {
                    feePayments = new FeePayments(activeStatus: ActiveStatus.BKASH,
                            student: student,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: amount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createOrUpdateBy,
                            academicYear: academicYear
                    )
                    invoiceDetails = new InvoiceDetails(student: student,
                            description: discountText, feeItems: feeItems,
                            quantity: quantity, amount: amount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createOrUpdateBy,
                            academicYear: academicYear
                    )

                    paymentsList.add(feePayments)
                    invoiceDetailsList.add(invoiceDetails)
                    invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                } else if (feeItems.iterationType == FeeIterationType.YEARLY) {
                    feePayments = FeePayments.findByStudentAndFeeItemsAndActiveStatus(student,feeItems,ActiveStatus.ACTIVE)
                    if (feePayments) {
                        isAlreadyFeePaid = true
                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid for ${feeItems.name}")
                    } else {
                        feePayments = new FeePayments(activeStatus: ActiveStatus.BKASH,
                                student: student,
                                feeItems: feeItems,
                                quantity: quantity,
                                amount: amount,
                                discount: discount,
                                netPayment: netPayment,
                                totalPayment: feePayment,
                                createdBy: createOrUpdateBy,
                                academicYear: academicYear
                        )
                        invoiceDetails = new InvoiceDetails(
                                student: student,
                                description: discountText,
                                feeItems: feeItems,
                                quantity: quantity,
                                amount: amount,
                                discount: discount,
                                netPayment: netPayment,
                                totalPayment: feePayment,
                                createdBy: createOrUpdateBy,
                                academicYear: academicYear
                        )
                        paymentsList.add(feePayments)
                        invoiceDetailsList.add(invoiceDetails)
                        invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                    }
                } else if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    Long feeMonth = params.getLong("feeMonth${feeItemId}")
                    ItemsDetail feeMonthFrom = ItemsDetail.read(feeMonth)
                    if(!feeMonthFrom){
                        isAlreadyFeePaid = true
                        alreadyPaidItems.add("Month Not Selected for ${feeItems.name}")
                    }
                    FeePayments feeMonthAlreadyPaid
                    List<FeePayments> feeMonthAlreadyPaidList
                    List<ItemsDetail> feeMonthList

                    Integer payMonthStart = 0
                    Integer payMonthEnd = 0
                    if(feeItems.feeType==FeeType.ACTIVATION){
                        if (quantity == 1) {
                            feeMonthAlreadyPaid = FeePayments.findByStudentAndItemsDetailAndActiveStatus(student, feeMonthFrom, ActiveStatus.ACTIVE)
                            if(feeMonthAlreadyPaid){
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feeMonthFrom. name}")
                            } else {
                                description = feeMonthFrom?.name+discountText
                                feePayments = new FeePayments(activeStatus: ActiveStatus.BKASH,
                                        student: student, feeItems: feeItems, itemsDetail: feeMonthFrom,
                                        itemDetailName: feeMonthFrom?.name,
                                        itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                        quantity: quantity, amount: amount, discount: discount,
                                        netPayment: netPayment, totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                paymentsList.add(feePayments)

                                invoiceDetails = new InvoiceDetails(
                                        student: student,
                                        feeItems: feeItems,
                                        description: description,
                                        quantity: quantity,
                                        amount: amount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                invoiceDetailsList.add(invoiceDetails)
                                invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                            }

                        } else {
                            payMonthStart = feeMonthFrom.continuityOrder
                            payMonthEnd = payMonthStart + (quantity - 1)
                            academicYear = schoolService.workingYear(student.className)
                            feeMonthList = ItemsDetail.findAllByAcademicYearAndActiveStatusAndFeeItemsAndContinuityOrderBetween(academicYear, ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd)
                            if(!feeMonthList || feeMonthList.size() != quantity){
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthAlreadyPaidList = FeePayments.findAllByStudentAndItemsDetailInListAndActiveStatus(student, feeMonthList, ActiveStatus.ACTIVE)
                                if(feeMonthAlreadyPaidList && feeMonthAlreadyPaidList.size()>0){
                                    isAlreadyFeePaid = true
                                    feeMonthAlreadyPaidList.each {FeePayments feePayments1 ->
                                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feePayments1.itemsDetail?.name}")
                                    }
                                } else {
                                    feeMonthList.each { ItemsDetail details ->
                                        feePayments = new FeePayments(
                                                activeStatus: ActiveStatus.BKASH,
                                                student: student, feeItems: feeItems,
                                                itemsDetail: details,
                                                itemDetailName: details?.name,
                                                itemDetailSortPosition: details?.sortPosition,
                                                quantity: 1, amount: amount,
                                                discount: amount - feeAmount,
                                                netPayment: amount,
                                                totalPayment: feeAmount,
                                                createdBy: createOrUpdateBy,
                                                academicYear: academicYear
                                        )
                                        paymentsList.add(feePayments)
                                    }
                                    description = "${feeMonthList.first().name} - ${feeMonthList.last().name}"+discountText
                                    invoiceDetails = new InvoiceDetails(
                                            student: student,
                                            feeItems: feeItems,
                                            description: description,
                                            quantity: quantity,
                                            amount: amount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: feePayment,
                                            createdBy: createOrUpdateBy,
                                            academicYear: academicYear
                                    )
                                    invoiceDetailsList.add(invoiceDetails)
                                    invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                                }
                            }
                        }
                        //new adjustment end
                    }else {                 // Feed that not require prior activation of student
                        if (quantity == 1) {
                            feeMonthAlreadyPaid = FeePayments.findByStudentAndItemsDetailAndActiveStatus(student, feeMonthFrom, ActiveStatus.ACTIVE)
                            if(feeMonthAlreadyPaid){
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feeMonthFrom. name}")
                            }else {
                                description = feeMonthFrom?.name+discountText
                                feePayments = new FeePayments(
                                        activeStatus: ActiveStatus.BKASH,
                                        student: student, feeItems: feeItems,
                                        itemsDetail: feeMonthFrom,
                                        itemDetailName: feeMonthFrom?.name,
                                        itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                        quantity: quantity,
                                        amount: amount, discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                paymentsList.add(feePayments)
                                invoiceDetails = new InvoiceDetails(
                                        student: student,
                                        feeItems: feeItems,
                                        description: description,
                                        quantity: quantity,
                                        amount: amount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                invoiceDetailsList.add(invoiceDetails)
                                invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                            }
                        } else {
                            payMonthStart = feeMonthFrom.sortPosition
                            payMonthEnd = payMonthStart + (quantity - 1)
                            feeMonthList = ItemsDetail.findAllByActiveStatusAndFeeItemsAndSortPositionBetween(ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd)
                            if(!feeMonthList || feeMonthList.size() != quantity){
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthAlreadyPaidList = FeePayments.findAllByStudentAndItemsDetailInListAndActiveStatus(student, feeMonthList, ActiveStatus.ACTIVE)
                                if(feeMonthAlreadyPaidList && feeMonthAlreadyPaidList.size()>0){
                                    isAlreadyFeePaid = true
                                    feeMonthAlreadyPaidList.each {FeePayments feePayments1 ->
                                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feePayments1.itemsDetail?.name}")
                                    }
                                } else {
                                    feeMonthList.each { ItemsDetail details ->
                                        feePayments = new FeePayments(
                                                activeStatus: ActiveStatus.BKASH,
                                                student: student,
                                                feeItems: feeItems,
                                                itemsDetail: details,
                                                itemDetailName: details?.name,
                                                itemDetailSortPosition: details?.sortPosition,
                                                quantity: 1, amount: amount,
                                                discount: amount - feeAmount,
                                                netPayment: amount,
                                                totalPayment: feeAmount,
                                                createdBy: createOrUpdateBy,
                                                academicYear: academicYear
                                        )
                                        paymentsList.add(feePayments)
                                    }
                                    description = "${feeMonthList.first().name} - ${feeMonthList.last().name}"+discountText
                                    invoiceDetails = new InvoiceDetails(
                                            student: student,
                                            feeItems: feeItems,
                                            description: description,
                                            quantity: quantity,
                                            amount: amount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: feePayment,
                                            createdBy: createOrUpdateBy,
                                            academicYear: academicYear
                                    )
                                    invoiceDetailsList.add(invoiceDetails)
                                    invoiceShowList.add([feeItem: feeItems.name, description: discountText, quantity: quantity, amount: amount, discount: discount, netPayment: netPayment, totalPayment: feePayment])
                                }
                            }
                        }
                    }
                }
                totalFeeAmount += netPayment
                totalFeePayment += feePayment
                itemNameList.add(feeItems.name)
            }
        }
        if (isAlreadyFeePaid) {
            flash.message = alreadyPaidItems.join(", ")
            render(view: 'alreadyPaidMsg')
            return
        }
        if (!paymentsList || paymentsList.size()==0) {
            flash.message = "No Item Selected to Pay"
            render(view: 'alreadyPaidMsg')
            return
        }

        Date invoiceDate = new Date().clearTime()
        /*if (!invoiceDate) {
            invoiceDate = new Date().clearTime()
        }*/

        Double totalDiscounts = totalFeeAmount - totalFeePayment
        String invDescription = itemNameList.join(", ")
        Invoice invoice = new Invoice(
                activeStatus: ActiveStatus.BKASH,
                paymentType: PaymentType.BKASH,
                student: student,
                invoiceNo: nextInvoice(),
                amount: totalFeeAmount.round(2),
                discount: totalDiscounts.round(2),
                payment: totalFeePayment.round(2),
                invoiceDate: invoiceDate,
                accountType: AccountType.INCOME,
                description: invDescription,
                createdBy: createOrUpdateBy,
                academicYear: academicYear
        )
        Invoice savedInvoice = collectionsService.saveInvoice(invoice, invoiceDetailsList, paymentsList)

        if (!savedInvoice) {
            flash.message = "Can't create invoice for [${student.studentID}] ${student.name}. Please contact to admin"
            render(view: 'alreadyPaidMsg')
            return
        }
        render(view: 'onlinePaymentVerification', model: [isAccount: false, student: student, invoiceId: savedInvoice?.id, invoiceAmount:savedInvoice.payment, invoiceShowList: invoiceShowList])
    }

    private String nextInvoice() {
        return sequenceGeneratorService.generateTenant("Invoice.invoiceNoSequence", 1, [invCode: "BK"], true)
    }


    def deleteOnlinePayment() {
        def invoiceId = params.invoiceId
        portalService.deleteInvoice(invoiceId)
        redirect(action: 'onlinePayment')
    }

    def verifyOnlinePayment() {

        String createOrUpdateBy = springSecurityService.principal?.username

        Double bkashAmount = 0.0
        JSONObject jsonTransaction
        String invoiceId = params.get('invoiceId')
        String transactionId = params.get('transactionId')
        Invoice invoiceExist = Invoice.findByTransactionId(transactionId)
        if (invoiceExist) {
            flash.message = "Already used this Transaction Id."
            redirect(action: 'onlinePayment')
            return
        }

        Invoice invoice = Invoice.findById(invoiceId)
        jsonTransaction = portalService.getTransactionDetails(transactionId)
        println(jsonTransaction)
        if (jsonTransaction != null) {
            String transactionStatus = "" + jsonTransaction.get("trxStatus");
            println(transactionStatus)
            //check if transaction status is Transaction Successful
            if (transactionStatus == '0000') {
                String amount = "" + jsonTransaction.get("amount")
                println(amount)
                if (amount != "") {
                    bkashAmount = Double.parseDouble(amount)
                }
            } else {
                flash.message = "Somehow your payment is pending, Contact with Administrator."
                redirect(action: 'onlinePayment')
            }
            if (bkashAmount >= invoice.payment) {
                def feePaymentsList = FeePayments.findAllByInvoice(invoice)
                feePaymentsList.each { FeePayments feePayments ->
                    feePayments.activeStatus = ActiveStatus.ACTIVE
                    feePayments.updatedBy = createOrUpdateBy
                    feePayments.save(flush: true)
                }

                invoice.activeStatus = ActiveStatus.ACTIVE
                invoice.transactionId = transactionId
                invoice.updatedBy = createOrUpdateBy
                invoice.save(flush: true)
                flash.message = "Congratulation You have successfully completed your payment."
                redirect(action: 'paymentList', params: [paid: true])
                return
            }
        } else {
            flash.message = "Somehow your payment is pending, Contact with Administrator."
            redirect(action: 'onlinePayment')
        }
    }
}
