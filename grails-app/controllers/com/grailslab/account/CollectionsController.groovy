package com.grailslab.account

import com.grailslab.CommonUtils
import com.grailslab.accounting.*
import com.grailslab.command.*
import com.grailslab.core.SequenceGeneratorService
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.stmgmt.RegForm
import com.grailslab.stmgmt.RegOnlineRegistration
import com.grailslab.stmgmt.Registration
import com.grailslab.stmgmt.Student
import grails.converters.JSON

class CollectionsController {

    def springSecurityService
    def classNameService
    def sectionService
    def studentService
    def collectionsService
    def messageSource
    def feeItemsService
    def schoolService
    def registrationService
    def subjectService
    def admissionFormService
    def onlineRegistrationService
    SequenceGeneratorService sequenceGeneratorService


    def index() {
        render(view: '/common/dashboard', layout: 'moduleCollectionLayout')
    }

    def loadByClassFee(Long classId) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String quickFeeType = params.quickType
        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        } else {
            academicYear = schoolService.schoolWorkingYear()
        }

        String outPut
        ClassName className = ClassName.read(classId)
        if (!className || className.activeStatus != ActiveStatus.ACTIVE) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        def studentList = studentService.classStudentsList(className, academicYear)
        if (!studentList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No student to collect fee.")
            outPut = result as JSON
            render outPut
            return
        }

        def feeList = collectionsService.quickFeeList(className, quickFeeType, academicYear)
        if (!feeList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No Fee to collect.")
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('studentList', studentList)
        result.put('feeList', feeList)
        outPut = result as JSON
        render outPut
    }

    def byClassStep(ByClassCommand command) {

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/byClassStep')
            return
        }

        Student student = command.student
        List dataArray = new ArrayList()

        Double amount = 0
        Double discountAmount = 0
        Double discount = 0
        Double payable = 0
        Double totalPayable = 0
        Double totalPaid = 0



        Long feeItemId
        FeeItems feeItems
        StudentDiscount studentDiscount
        FeePayments feePayments

        Integer quantity
        Double paymentAmount
        Boolean isAlreadyPaid = false
        List<ItemsDetail> itemMonthList
        List previousPayments = new ArrayList()
        List<FeePayments> feePaymentList
        List itemDetails
        ItemsDetail lastPaidMonth
        ItemsDetail nextPaidMonth

        def feeIdsList = command.feeId.split(',')
        feeIdsList.each { String idx ->
            itemMonthList = null
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            if (feeItems) {
                if (!(student.scholarshipObtain && student.scholarshipPercent.doubleValue() == 100)) {
                    amount = feeItems.amount
                    if (student.scholarshipObtain && student.scholarshipPercent.doubleValue() >= 0) {
                        discount = student.scholarshipPercent
                        if (discount > 0 && discount <= 100) {
                            discountAmount = amount * discount * 0.01
                        }
                        payable = amount - discountAmount
                    } else {
                        studentDiscount = StudentDiscount.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                        if (studentDiscount) {
                            discount = studentDiscount.discountPercent
                            discountAmount = studentDiscount.amount
                            payable = amount - discountAmount
                        } else {
                            discount = feeItems.discount
                            payable = feeItems.netPayable
                        }
                    }

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
                            } else {
                                lastPaidMonth = feePaymentList.last().itemsDetail
                                int cOrder
                                if (lastPaidMonth && lastPaidMonth.continuityOrder) {
                                    cOrder = lastPaidMonth.continuityOrder + 1
                                    nextPaidMonth = ItemsDetail.findByFeeItemsAndContinuityOrderAndActiveStatus(feeItems, cOrder, ActiveStatus.ACTIVE)
                                } else {
                                    cOrder = lastPaidMonth.sortPosition + 1
                                    nextPaidMonth = ItemsDetail.findByFeeItemsAndSortPositionAndActiveStatus(feeItems, cOrder, ActiveStatus.ACTIVE)
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
                        feePaymentList = FeePayments.findAllByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                        if (feePaymentList) {
                            totalPaid = 0
                            feePaymentList.each { payment ->
                                totalPaid += payment.totalPayment
                            }
                            previousPayments.add([item: feeItems.name, amount: totalPaid, hasItemDetail: false])
                        }
                    }
                    totalPayable += payable
                    dataArray.add([id: feeItems.id, name: "${feeItems.code} - ${feeItems.name}", quantity: quantity, amount: amount, discount: discount, payable: paymentAmount, netPayable: paymentAmount, isAlreadyPaid: isAlreadyPaid, itemMonthList: itemMonthList, nextPaidMonth: nextPaidMonth])
                }
            }
            payable = 0
        }
        render(view: '/collection/collections/byClassStep', model: [dataArray: dataArray, student: student, totalPayable: totalPayable, previousPayments: previousPayments])
    }


    def byStudent() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList
        if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_HIGH_SCHOOL) {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        } else {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        }

        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        String toDateStr = CommonUtils.getUiDateStrForPicker(new Date())
        if (!classNameList) {
            render(view: '/collection/collections/byStudent2', model: [dataReturn: null, totalCount: 0, workingYear: academicYear, academicYearList: academicYearList, classNameList: null, toDateStr: toDateStr])
            return
        }
        LinkedHashMap resultMap = collectionsService.manageCollectionPaginationList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/collection/collections/byStudent2', model: [dataReturn: null, totalCount: 0, workingYear: academicYear, academicYearList: academicYearList, classNameList: classNameList, toDateStr: toDateStr])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/collection/collections/byStudent2', model: [dataReturn: resultMap.results, totalCount: totalCount, workingYear: academicYear, academicYearList: academicYearList, classNameList: classNameList, toDateStr: toDateStr])
    }

    def byStudentStep2(ByStudentCommand command) {
        /*if (!request.method.equals('POST')) {
            redirect(action: 'quickFee', params: [id:1])
            return
        }*/

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            redirect(action: 'byStudent')
            return
        }
        Student student = studentService.findByStudentId(command.student, command.academicYear)

        List dataArray = new ArrayList()

        Double amount = 0
        Double discountAmount = 0
        Double discount = 0
        Double payable = 0
        Double totalPayable = 0
        Double totalPaid = 0


        Long feeItemId
//        FeeItems feeItems
        StudentDiscount studentDiscount
        FeePayments feePayments
        List<FeePayments> feePaymentList
//        String paymentStatus
        Integer quantity
        Double paymentAmount
        Boolean isAlreadyPaid = false

        def feeIdsList = collectionsService.studentFeeList(student, command.quickFee, command.feePaidCriteria, command.academicYear)
        List previousPayments = new ArrayList()
        List<ItemsDetail> itemMonthList
        List itemDetails
        ItemsDetail lastPaidMonth
        ItemsDetail nextPaidMonth
        feeIdsList.each { FeeItems feeItems ->
            itemMonthList = null
            if (feeItems) {
                if (!(student.scholarshipObtain && student.scholarshipPercent.doubleValue() == 100)) {
                    amount = feeItems.amount
                    if (student.scholarshipObtain && student.scholarshipPercent.doubleValue() >= 0) {
                        discount = student.scholarshipPercent
                        if (discount > 0 && discount <= 100) {
                            discountAmount = amount * discount * 0.01
                        }
                        payable = amount - discountAmount
                    } else {
                        studentDiscount = StudentDiscount.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                        if (studentDiscount) {
                            discount = studentDiscount.discountPercent
                            discountAmount = studentDiscount.amount
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
                            } else {
                                lastPaidMonth = feePaymentList.last().itemsDetail
                                int cOrder
                                if (lastPaidMonth && lastPaidMonth.continuityOrder) {
                                    cOrder = lastPaidMonth.continuityOrder + 1
                                    nextPaidMonth = ItemsDetail.findByFeeItemsAndContinuityOrderAndActiveStatus(feeItems, cOrder, ActiveStatus.ACTIVE)
                                } else {
                                    cOrder = lastPaidMonth.sortPosition + 1
                                    nextPaidMonth = ItemsDetail.findByFeeItemsAndSortPositionAndActiveStatus(feeItems, cOrder, ActiveStatus.ACTIVE)
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
                        quantity = 0
//                        paymentAmount = 0
                        payable = 0
                    } else if (feeItems.iterationType == FeeIterationType.RECEIVE) {
                        feePaymentList = FeePayments.findAllByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                        if (feePaymentList) {
                            totalPaid = 0
                            feePaymentList.each { payment ->
                                totalPaid += payment.totalPayment
                            }
                            previousPayments.add([item: feeItems.name, amount: totalPaid, hasItemDetail: false])
                        }
                        quantity = 0
//                        paymentAmount = 0
                        payable = 0
                    }
                    totalPayable += payable
                    dataArray.add([id: feeItems.id, name: "${feeItems.code} - ${feeItems.name}", quantity: quantity, amount: amount, discount: discount, payable: paymentAmount, netPayable: quantity * paymentAmount, isAlreadyPaid: isAlreadyPaid, itemMonthList: itemMonthList, nextPaidMonth: nextPaidMonth])
                }
            }
            payable = 0
        }
        render(view: '/collection/collections/byStudentStep2', model: [dataArray: dataArray, student: student, totalPayable: totalPayable, previousPayments: previousPayments])
    }

    def payInvoice(ByClassStepCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        String createOrUpdateBy = springSecurityService.principal?.username
        def feeIdsList = command.feeItemIds.split(',')
        Student student = command.studentId
        AcademicYear academicYear = student.academicYear

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

        Double feeAmount
        Double feePayment
        Double totalFeeAmount = 0
        Double totalFeePayment = 0
        List<String> itemNameList = new ArrayList<String>()
        List<String> alreadyPaidItems = new ArrayList<String>()
        Boolean isAlreadyFeePaid = false
        String discountText;

        feeIdsList.each { String idx ->
            description = ''
            discountText = ''
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

                if (discount > 0 && schoolService.runningSchool() == 'bailyschool') {
                    discountText = " [less ${discount * quantity}]"
                }
                if (feeItems.iterationType == FeeIterationType.RECEIVE) {
                    feePayments = new FeePayments(
                            student: student,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: feeAmount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            academicYear: academicYear,
                            createdBy: createOrUpdateBy
                    )
                    invoiceDetails = new InvoiceDetails(
                            student: student,
                            description: discountText,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: feeAmount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createOrUpdateBy,
                            academicYear: academicYear
                    )
                    paymentsList.add(feePayments)
                    invoiceDetailsList.add(invoiceDetails)
                } else if (feeItems.iterationType == FeeIterationType.YEARLY) {
                    feePayments = FeePayments.findByStudentAndFeeItemsAndActiveStatus(student, feeItems, ActiveStatus.ACTIVE)
                    if (feePayments) {
                        isAlreadyFeePaid = true
                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid for ${feeItems.name}")
                    } else {
                        feePayments = new FeePayments(
                                student: student,
                                feeItems: feeItems,
                                quantity: quantity,
                                amount: feeAmount,
                                discount: discount,
                                netPayment: netPayment,
                                totalPayment: feePayment,
                                academicYear: academicYear,
                                createdBy: createOrUpdateBy
                        )
                        invoiceDetails = new InvoiceDetails(
                                student: student,
                                description: discountText,
                                feeItems: feeItems,
                                quantity: quantity,
                                amount: feeAmount,
                                discount: discount,
                                netPayment: netPayment,
                                totalPayment: feePayment,
                                createdBy: createOrUpdateBy,
                                academicYear: academicYear
                        )
                        paymentsList.add(feePayments)
                        invoiceDetailsList.add(invoiceDetails)
                    }
                } else if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    Long feeMonth = params.getLong("feeMonth${feeItemId}")
                    ItemsDetail feeMonthFrom = ItemsDetail.read(feeMonth)
                    if (!feeMonthFrom) {
                        isAlreadyFeePaid = true
                        alreadyPaidItems.add("Month Not Selected for ${feeItems.name}")
                    }
                    FeePayments feeMonthAlreadyPaid
                    List<FeePayments> feeMonthAlreadyPaidList
                    List<ItemsDetail> feeMonthList

                    Integer payMonthStart = 0
                    Integer payMonthEnd = 0
                    if (feeItems.feeType == FeeType.ACTIVATION) {
                        if (quantity == 1) {
                            feeMonthAlreadyPaid = FeePayments.findByStudentAndItemsDetailAndActiveStatus(student, feeMonthFrom, ActiveStatus.ACTIVE)
                            if (feeMonthAlreadyPaid) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feeMonthFrom.name}")
                            } else {
                                description = feeMonthFrom?.name + discountText
                                feePayments = new FeePayments(
                                        student: student,
                                        feeItems: feeItems,
                                        itemsDetail: feeMonthFrom,
                                        itemDetailName: feeMonthFrom?.name,
                                        itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                        quantity: quantity, amount: feeAmount,
                                        discount: discount,
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
                                        amount: feeAmount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                invoiceDetailsList.add(invoiceDetails)
                            }

                        } else {
                            payMonthStart = feeMonthFrom.continuityOrder
                            payMonthEnd = payMonthStart + (quantity - 1)
                            feeMonthList = ItemsDetail.findAllByActiveStatusAndFeeItemsAndContinuityOrderBetween(ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd, [max: 15, sort: "sortPosition", order: "asc"])
                            if (!feeMonthList || feeMonthList.size() != quantity) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthAlreadyPaidList = FeePayments.findAllByStudentAndItemsDetailInListAndActiveStatus(student, feeMonthList, ActiveStatus.ACTIVE)
                                if (feeMonthAlreadyPaidList && feeMonthAlreadyPaidList.size() > 0) {
                                    isAlreadyFeePaid = true
                                    feeMonthAlreadyPaidList.each { FeePayments feePayments1 ->
                                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feePayments1.itemsDetail?.name}")
                                    }
                                } else {
                                    feeMonthList.each { ItemsDetail details ->
                                        feePayments = new FeePayments(
                                                student: student,
                                                feeItems: feeItems,
                                                itemsDetail: details,
                                                itemDetailName: details?.name,
                                                itemDetailSortPosition: details?.sortPosition,
                                                quantity: 1, amount: feeAmount,
                                                discount: amount - feeAmount,
                                                netPayment: amount,
                                                totalPayment: feeAmount,
                                                createdBy: createOrUpdateBy,
                                                academicYear: academicYear
                                        )
                                        paymentsList.add(feePayments)
                                    }
                                    description = "${feeMonthList.first().name} - ${feeMonthList.last().name}" + discountText
                                    invoiceDetails = new InvoiceDetails(
                                            student: student,
                                            feeItems: feeItems,
                                            description: description,
                                            quantity: quantity,
                                            amount: feeAmount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: feePayment,
                                            createdBy: createOrUpdateBy,
                                            academicYear: academicYear
                                    )
                                    invoiceDetailsList.add(invoiceDetails)
                                }
                            }
                        }
                        //new adjustment end
                    } else {                 // Feed that not require prior activation of student
                        if (quantity == 1) {
                            feeMonthAlreadyPaid = FeePayments.findByStudentAndItemsDetailAndActiveStatus(student, feeMonthFrom, ActiveStatus.ACTIVE)
                            if (feeMonthAlreadyPaid) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feeMonthFrom.name}")
                            } else {
                                description = feeMonthFrom?.name + discountText
                                feePayments = new FeePayments(
                                        student: student,
                                        feeItems: feeItems,
                                        itemsDetail: feeMonthFrom,
                                        itemDetailName: feeMonthFrom?.name,
                                        itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                        quantity: quantity,
                                        amount: feeAmount,
                                        discount: discount,
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
                                        amount: feeAmount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createOrUpdateBy,
                                        academicYear: academicYear
                                )
                                invoiceDetailsList.add(invoiceDetails)
                            }
                        } else {
                            payMonthStart = feeMonthFrom.sortPosition
                            payMonthEnd = payMonthStart + (quantity - 1)
                            feeMonthList = ItemsDetail.findAllByActiveStatusAndFeeItemsAndSortPositionBetween(ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd, [max: 15, sort: "sortPosition", order: "asc"])
                            if (!feeMonthList || feeMonthList.size() != quantity) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthAlreadyPaidList = FeePayments.findAllByStudentAndItemsDetailInListAndActiveStatus(student, feeMonthList, ActiveStatus.ACTIVE)
                                if (feeMonthAlreadyPaidList && feeMonthAlreadyPaidList.size() > 0) {
                                    isAlreadyFeePaid = true
                                    feeMonthAlreadyPaidList.each { FeePayments feePayments1 ->
                                        alreadyPaidItems.add("[${student.studentID}] ${student.name} has already paid ${feeItems.name} for ${feePayments1.itemsDetail?.name}")
                                    }
                                } else {
                                    feeMonthList.each { ItemsDetail details ->
                                        feePayments = new FeePayments(
                                                student: student,
                                                feeItems: feeItems,
                                                itemsDetail: details,
                                                itemDetailName: details?.name,
                                                itemDetailSortPosition: details?.sortPosition,
                                                quantity: 1, amount: feeAmount,
                                                discount: amount - feeAmount,
                                                netPayment: amount,
                                                totalPayment: feeAmount,
                                                createdBy: createOrUpdateBy,
                                                academicYear: academicYear
                                        )
                                        paymentsList.add(feePayments)
                                    }
                                    description = "${feeMonthList.first().name} - ${feeMonthList.last().name}" + discountText
                                    invoiceDetails = new InvoiceDetails(
                                            student: student,
                                            feeItems: feeItems,
                                            description: description,
                                            quantity: quantity,
                                            amount: feeAmount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: feePayment,
                                            createdBy: createOrUpdateBy,
                                            academicYear: academicYear
                                    )
                                    invoiceDetailsList.add(invoiceDetails)
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
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        if (!paymentsList || paymentsList.size() == 0) {
            flash.message = "No Item Selected to Pay"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        Date invoiceDate = command.invoiceDate
        if (!invoiceDate) {
            invoiceDate = new Date().clearTime()
        }

        Double totalDiscounts = totalFeeAmount - totalFeePayment
        String invDescription = itemNameList.join(", ")
        Invoice invoice = new Invoice(
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
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        redirect(controller: 'accountsReport', action: 'printInvoice', params: [invoiceNo: savedInvoice?.invoiceNo])
    }

    //by class adjustment started
    def byClass() {
        AcademicYear workingYear = schoolService.workingYear(null)
        def academicYearList
        if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_HIGH_SCHOOL) {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        } else {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        }

        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        render(view: '/collection/collections/byClass', model: [workingYear: workingYear, academicYearList: academicYearList, classNameList: classNameList])
    }

    def manage() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        String toDateStr = CommonUtils.getUiDateStrForPicker(new Date())
        if (!classNameList) {
            render(view: '/collection/collections/manageCollection', model: [dataReturn: null, totalCount: 0, classNameList: null, toDateStr: toDateStr])
            return
        }
        LinkedHashMap resultMap = collectionsService.manageCollectionPaginationList(params)
        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/collection/collections/manageCollection', model: [dataReturn: null, totalCount: 0, classNameList: classNameList, toDateStr: toDateStr])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/collection/collections/manageCollection', model: [dataReturn: resultMap.results, totalCount: totalCount, classNameList: classNameList, toDateStr: toDateStr])
    }

    def listManage() {
        LinkedHashMap gridData
        String result

        LinkedHashMap resultMap = collectionsService.manageCollectionPaginationList(params)
        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def edit(Long id) {

        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        Invoice invoice = Invoice.read(id)

        if (!invoice) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.OBJ, invoice)
        outPut = result as JSON
        render outPut
    }

    def delete(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = collectionsService.delete(id)
        String output = result as JSON
        render output
    }

    private String nextInvoice() {
        return sequenceGeneratorService.generateTenant("Invoice.invoiceNoSequence", 1, [invCode: "INV-"], true)
    }

    //quick fee adjustment start
    def manageQuickFee() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.ALL)
        if (!classNameList) {
            render(view: '/collection/collections/manageQuickFee', model: [dataReturn: null, totalCount: 0, classNameList: null])
            return
        }
        LinkedHashMap resultMap = feeItemsService.manageQuickFeeList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            render(view: '/collection/collections/manageQuickFee', model: [dataReturn: null, totalCount: 0, classNameList: classNameList])
            return
        }
        int totalCount = resultMap.totalCount
        render(view: '/collection/collections/manageQuickFee', model: [dataReturn: resultMap.results, totalCount: totalCount, classNameList: classNameList])
    }

    def manageQuickFeeList() {

        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = feeItemsService.manageQuickFeeList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result

    }

    def editQuick(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = collectionsService.editQuick(id, params)
        String output = result as JSON
        render output
    }
    //quick fee adjustment done

    def feePay() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/collection/collections/feePay', model: [classNameList: classNameList])
    }

    def monthlyFeeDues() {
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/collection/collections/monthlyFeedueReports', model: [classNameList: classNameList])
    }

    def monthWise() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList
        if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_HIGH_SCHOOL) {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        } else {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        }
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        render(view: '/collection/collections/monthlyWiseReport', model: [classNameList: classNameList, academicYearList: academicYearList, workingYear: academicYear])
    }

    def loadClassFee(loadClassFeeCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }

        def feeList
        if (command.monthlyFees) {
            feeList = collectionsService.classMonthlyFeeDDList(command.className, null)
        } else {
            feeList = collectionsService.classFeeDDList(command.className, null)
        }
        if (!feeList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${command.className?.name} has No Fee added.")
            outPut = result as JSON
            render outPut
            return
        }
        if (command.allFeeDue) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put('feeList', feeList)
            def monthlyFeeList = collectionsService.classMonthlyFeeDDList(command.className, null)
            if (monthlyFeeList) {
                String feeIds = monthlyFeeList.collect { it.id }.join(',')
                result.put('selectedList', subjectService.getCommaSeparatedIdAsList(feeIds))
            }
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('feeList', feeList)
        outPut = result as JSON
        render outPut
        return
    }

    def loadFeeItemsDetail(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        FeeItems feeItems = FeeItems.read(id)
        if (!feeItems || feeItems.activeStatus != ActiveStatus.ACTIVE) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        def itemsDetail = null
        if (feeItems.iterationType != FeeIterationType.MONTHLY) {
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put("itemsDetail", itemsDetail)
            outPut = result as JSON
            render outPut
            return
        }

        itemsDetail = collectionsService.feeItemDetailsDDList(feeItems)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put("itemsDetail", itemsDetail)
        outPut = result as JSON
        render outPut
        return
    }

    def feePayStatusList() {
        LinkedHashMap gridData
        String result
        String payStatus = params.payStatus
        LinkedHashMap resultMap
        if (payStatus && payStatus == "paid") {
            resultMap = collectionsService.feePayPaginationList(params)
        } else {
            List<Long> feeItemPaidStudentIdsList = collectionsService.feeItemPaidStudentIdsList(params)
            resultMap = studentService.feeItemNotPaidPaginateList(params, feeItemPaidStudentIdsList)
        }


        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def studentPay() {
        AcademicYear academicYear = schoolService.workingYear(null)
        def academicYearList
        if (schoolService.runningSchool() == CommonUtils.NARAYANGANJ_HIGH_SCHOOL) {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.ALL)
        } else {
            academicYearList = schoolService.acaYearDropDownList(AcaYearType.SCHOOL)
        }
        render(view: '/collection/collections/studentPay', model: [academicYearList: academicYearList, workingYear: academicYear])
    }

    def studentPayResponse(StudentPayCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = collectionsService.studentPayResponse(command)
        String ouput = result as JSON
        render ouput
    }

    // -- offline --
    def offlineFormFee() {
        def classNameList = admissionFormService.classesDropDown()
        render(view: '/collection/collections/formFee', model: [classNameList: classNameList])
    }

    // -- online --
    def onlineFormFee() {
        def classNameList = admissionFormService.classesDropDown()
        render(view: '/collection/collections/onlineFormFee', model: [classNameList: classNameList])
    }

    def admitCardPrint() {
        String toDateStr = CommonUtils.getUiDateStrForPicker(new Date())
        def classNameList = admissionFormService.classesDropDown()
        render(view: '/collection/collections/admitCard', model: [classNameList: classNameList, toDateStr: toDateStr])
    }

    def listApplicantCollection() {
        LinkedHashMap gridData
        String result
        LinkedHashMap resultMap = onlineRegistrationService.collectionPaginateList(params)

        if (!resultMap || resultMap.totalCount == 0) {
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        int totalCount = resultMap.totalCount
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }


    def loadOfflineClassFee(Long classId) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String quickFeeType = params.quickType

        AcademicYear academicYear
        if (params.academicYear) {
            academicYear = AcademicYear.valueOf(params.academicYear)
        } else {
            academicYear = schoolService.schoolAdmissionYear()
        }

        String outPut
        ClassName className = ClassName.read(classId)
        if (!className || className.activeStatus != ActiveStatus.ACTIVE) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        def feeList = collectionsService.quickFeeList(className, quickFeeType, academicYear)
        if (!feeList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No Fee to collect.")
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('feeList', feeList)
        outPut = result as JSON
        render outPut
    }

    def offlineFeeStep(FormFeeCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'quickFee', params: [id: 1])
            return
        }

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        def feeIdsList = command.feeId.split(',')

        List dataArray = new ArrayList()

        Double amount = 0
        Double discountAmount = 0
        Double discount = 0
        Double payable = 0
        Double totalPayable = 0


        Long feeItemId
        FeeItems feeItems
        Integer quantity
        Double paymentAmount
        List itemDetails

        feeIdsList.each { String idx ->
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            if (feeItems) {
                amount = feeItems.amount
                discount = feeItems.discount
                payable = feeItems.netPayable

                quantity = 1
                paymentAmount = payable
                totalPayable += payable
                dataArray.add([id: feeItems.id, name: "${feeItems.code} - ${feeItems.name}", quantity: quantity, amount: amount, discount: discount, payable: paymentAmount])
            }
            payable = 0
        }
        render(view: '/collection/collections/formFeeStep', model: [dataArray: dataArray, className: command.cName, totalPayable: totalPayable])
    }


    def offlineInvoice(FormFeeInvoiceCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        RegForm regForm = RegForm.findByAcademicYearAndClassNameAndActiveStatus(academicYear, command.className, ActiveStatus.ACTIVE)
        if (!regForm) {
            redirect(action: 'index')
            return
        }

        def feeIdsList = command.feeItemIds.split(',')

        Long feeItemId
        FeeItems feeItems

        String inputQnty

        Integer quantity
        Double amount
        Double discount
        Double discountAmount
        Double netPayment
        String description
        Double feeAmount = 0
        Double feePayment = 0
        Double totalFeeAmount = 0
        Double totalFeePayment = 0
        List<String> itemNameList = new ArrayList<String>()

        feeIdsList.each { String idx ->
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            inputQnty = params."quantity${feeItemId}"
            quantity = 0
            if (inputQnty) {
                quantity = Integer.parseInt(inputQnty)
            }
            if (feeItems && quantity > 0) {
                feeAmount = 0
                feePayment = 0

                amount = feeItems.amount
                discount = feeItems.discount
                netPayment = feeItems.netPayable

                discountAmount = 0

                feeAmount = amount * quantity
                feePayment = netPayment * quantity

                if (feeItems.iterationType == FeeIterationType.RECEIVE || feeItems.iterationType == FeeIterationType.YEARLY) {
                    totalFeeAmount += feeAmount
                    totalFeePayment += feePayment
                    itemNameList.add(feeItems.name)
                }
            }
        }
        String invDescription = itemNameList.join(", ")

        RegOnlineRegistration formApply = new RegOnlineRegistration(command.properties)
        formApply.feeAmount = totalFeeAmount.round(2)
        formApply.payment = totalFeePayment.round(2)
        formApply.description = invDescription
        formApply.serialNo = onlineRegistrationService.nextOnlineRegistrationSerial(regForm.academicYear.value, regForm.serialPrefix)
        formApply.applyNo = onlineRegistrationService.nextOnlineRegistrationApplyNo(regForm.academicYear.value, regForm.applicationPrefix)
        formApply.invoiceDate = new Date().clearTime()
        formApply.academicYear = academicYear
        formApply.applicantStatus = ApplicantStatus.Apply
        formApply.regFormId = regForm.id

        RegOnlineRegistration savedApply = formApply.save(flush: true)

        if (!savedApply) {
            flash.message = "Can't create invoice for ${command.name}. Please contact to admin"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        redirect(controller: 'accountsReport', action: 'printFormInvoice', params: [invoiceNo: savedApply.applyNo])
    }
    // -- offline --

    // -- online from fee --
    def loadOnlineAdmissionFee(Long classId) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String quickFeeType = params.quickType
        String outPut
        ClassName className = ClassName.read(classId)
        if (!className || className.activeStatus != ActiveStatus.ACTIVE) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }

        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        def sectionList = sectionService.classSectionsDDList(className, academicYear)
        if (!sectionList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No section added $academicYear.value.")
            outPut = result as JSON
            render outPut
            return
        }

        def feeList = collectionsService.quickFeeList(className, quickFeeType, academicYear)
        if (!feeList) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${className.name} has No Fee to collect.")
            outPut = result as JSON
            render outPut
            return
        }

        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('sectionList', sectionList)
        result.put('feeList', feeList)
        outPut = result as JSON
        render outPut
    }


    def onlineAdmission() {
        def classNameList = admissionFormService.classesDropDown()
        render(view: '/collection/collections/onlineAdmission', model: [classNameList: classNameList])
    }

    def onlineAdmissionStep(OnlineAdmissionCommand command) {
        /*if (!request.method.equals('POST')) {
           redirect(action: 'quickFee', params: [id:1])
           return
       }*/

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        if (command.selectedApplicant.applicantStatus == ApplicantStatus.Admitted) {
            flash.message = "Applicant Already admited to class"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        if (command.selectedApplicant.applicantStatus != ApplicantStatus.Selected) {
            flash.message = "Applicant Not Selected"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        List dataArray = new ArrayList()

        Double amount = 0
        Double discountAmount = 0
        Double discount = 0
        Double payable = 0
        Double totalPayable = 0
        Double totalPaid = 0



        Long feeItemId
        FeeItems feeItems

        Integer quantity
        Double paymentAmount
        List<ItemsDetail> itemMonthList
        List<FeePayments> feePaymentList
        List itemDetails

        def feeIdsList = command.feeId.split(',')
        feeIdsList.each { String idx ->
            itemMonthList = null
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            if (feeItems) {
                amount = feeItems.amount
                discount = feeItems.discount
                payable = feeItems.netPayable
                quantity = 1
                paymentAmount = payable
                if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    itemMonthList = collectionsService.feeItemDetailsDDList(feeItems)
                }
                totalPayable += payable
                dataArray.add([id: feeItems.id, name: "${feeItems.code} - ${feeItems.name}", quantity: quantity, amount: amount, discount: discount, payable: paymentAmount, netPayable: quantity * paymentAmount, itemMonthList: itemMonthList])
            }
            payable = 0
        }
        Integer rollNo = studentService.nextRoll(command.section)
        render(view: '/collection/collections/onlineAdmissionStep', model: [dataArray: dataArray, selectedApplicant: command.selectedApplicant, section: command.section, rollNo: rollNo, totalPayable: totalPayable])
    }

    def payOnlineInvoice(OnlineAdmissionStepCommand command) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message = errorList?.join('. ')
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        AcademicYear academicYear = schoolService.schoolAdmissionYear()
        RegOnlineRegistration onlineRegistration = RegOnlineRegistration.findBySerialNoAndAcademicYear(command.admitCardNumber, academicYear)
        if (!onlineRegistration) {
            flash.message = "Applicant not found"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        if (onlineRegistration.applicantStatus == ApplicantStatus.Admitted) {
            flash.message = "Applicant Already admit to class"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        if (onlineRegistration.applicantStatus != ApplicantStatus.Selected) {
            flash.message = "Applicant Not Selected"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        String createBy = springSecurityService?.principal?.username
        String studentNo = registrationService.nextStudentNo()
        Registration registration = new Registration(academicYear: academicYear, createdBy: createBy,
                studentID: studentNo, name: onlineRegistration.name, studentStatus: StudentStatus.ADMISSION,
                fathersName: onlineRegistration.fathersName, mothersName: onlineRegistration.mothersName, religion: onlineRegistration.religion,
                presentAddress: onlineRegistration.presentAddress, permanentAddress: onlineRegistration.permanentAddress,
                email: onlineRegistration.email, mobile: onlineRegistration.mobile, fathersProfession: onlineRegistration.fathersProfession,
                mothersProfession: onlineRegistration.mothersProfession, fathersIncome: onlineRegistration.fathersIncome, birthDate: onlineRegistration.birthDate,
                imagePath: onlineRegistration.imagePath, gender: onlineRegistration.gender)

        if (!registration.save(flush: true)) {
            flash.message = "Unable to Complete Admission due to Online form incomplete transfer. Please contact with Admin. Admit Card Number: ${command.admitCardNumber}"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        Student student = new Student(academicYear: academicYear, createdBy: createBy, name: onlineRegistration.name, studentID: studentNo, registration: registration, section: command.section, className: command.section.className, rollNo: command.rollNo).save(flush: true)
        if (!student) {
            flash.message = "Unable to Complete Admission due to admit in class. Please contact with Admin. Admit Card Number: ${command.admitCardNumber}"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        onlineRegistration.applicantStatus = ApplicantStatus.Admitted
        onlineRegistration.save(flush: true)


        def feeIdsList = command.feeItemIds.split(',')
//        Student student = command.studentId

        Long feeItemId
        FeeItems feeItems
        StudentDiscount studentDiscount

        String inputQnty
        String inputFixedAmount
        Boolean isInputAmount
        Double userInputAmount

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

        Double feeAmount = 0
        Double feePayment = 0
        Double totalFeeAmount = 0
        Double totalFeePayment = 0
        List<String> itemNameList = new ArrayList<String>()
        List<String> alreadyPaidItems = new ArrayList<String>()
        Boolean isAlreadyFeePaid = false


        String discountText;

        feeIdsList.each { String idx ->
            isInputAmount = false
            description = ''
            discountText = ''
            feeItemId = Long.parseLong(idx)
            feeItems = FeeItems.read(feeItemId)
            inputQnty = params."quantity${feeItemId}"
            quantity = 0
            if (inputQnty) {
                if (inputQnty.startsWith("#")) {
                    inputFixedAmount = inputQnty.substring(inputQnty.lastIndexOf("#") + 1)
                    userInputAmount = Double.valueOf(inputFixedAmount)
                    discountText = " Discounted Amount"
                    isInputAmount = true
                    quantity = 1
                } else {
                    quantity = Integer.parseInt(inputQnty)
                }
            }
            if (feeItems && quantity > 0) {
                feeAmount = 0
                feePayment = 0

                amount = feeItems.amount
                discount = feeItems.discount
                netPayment = feeItems.netPayable

                feeAmount = amount * quantity
                feePayment = netPayment * quantity
                //in case of Payment amount inserted by Account
                if (isInputAmount) {
                    feeAmount = amount * quantity
                    feePayment = userInputAmount
                }

                if (feeItems.iterationType == FeeIterationType.RECEIVE) {
                    feePayments = new FeePayments(
                            student: student,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: amount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createBy,
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
                            createdBy: createBy,
                            academicYear: academicYear
                    )
                    paymentsList.add(feePayments)
                    invoiceDetailsList.add(invoiceDetails)
                } else if (feeItems.iterationType == FeeIterationType.YEARLY) {
                    feePayments = new FeePayments(
                            academicYear: academicYear,
                            student: student,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: amount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createBy
                    )
                    invoiceDetails = new InvoiceDetails(
                            academicYear: academicYear,
                            student: student,
                            description: discountText,
                            feeItems: feeItems,
                            quantity: quantity,
                            amount: amount,
                            discount: discount,
                            netPayment: netPayment,
                            totalPayment: feePayment,
                            createdBy: createBy
                    )
                    paymentsList.add(feePayments)
                    invoiceDetailsList.add(invoiceDetails)
                } else if (feeItems.iterationType == FeeIterationType.MONTHLY) {
                    Long feeMonth = params.getLong("feeMonth${feeItemId}")
                    ItemsDetail feeMonthFrom = ItemsDetail.read(feeMonth)
                    if (!feeMonthFrom) {
                        isAlreadyFeePaid = true
                        alreadyPaidItems.add("Month Not Selected for ${feeItems.name}")
                    }
                    List<FeePayments> feeMonthAlreadyPaidList
                    List<ItemsDetail> feeMonthList

                    Integer payMonthStart = 0
                    Integer payMonthEnd = 0
                    if (feeItems.feeType == FeeType.ACTIVATION) {
                        if (quantity == 1) {
                            description = feeMonthFrom?.name + discountText
                            feePayments = new FeePayments(
                                    academicYear: academicYear,
                                    student: student,
                                    feeItems: feeItems,
                                    itemsDetail: feeMonthFrom,
                                    itemDetailName: feeMonthFrom?.name,
                                    itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                    quantity: 1, amount: amount,
                                    discount: discount,
                                    netPayment: netPayment,
                                    totalPayment: netPayment,
                                    createdBy: createBy
                            )
                            paymentsList.add(feePayments)

                            invoiceDetails = new InvoiceDetails(
                                    academicYear: academicYear,
                                    student: student,
                                    feeItems: feeItems,
                                    description: description,
                                    quantity: quantity,
                                    amount: amount,
                                    discount: discount,
                                    netPayment: netPayment,
                                    totalPayment: feePayment,
                                    createdBy: createBy
                            )
                            invoiceDetailsList.add(invoiceDetails)
                        } else {
                            payMonthStart = feeMonthFrom.continuityOrder
                            payMonthEnd = payMonthStart + (quantity - 1)
                            feeMonthList = ItemsDetail.findAllByActiveStatusAndFeeItemsAndContinuityOrderBetween(ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd, [max: 15, sort: "sortPosition", order: "asc"])
                            if (!feeMonthList || feeMonthList.size() != quantity) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthList.each { ItemsDetail details ->
                                    feePayments = new FeePayments(
                                            academicYear: academicYear,
                                            student: student, feeItems: feeItems,
                                            itemsDetail: details,
                                            itemDetailName: details?.name,
                                            itemDetailSortPosition: details?.sortPosition,
                                            quantity: 1, amount: amount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: netPayment,
                                            createdBy: createBy
                                    )
                                    paymentsList.add(feePayments)
                                }
                                description = "${feeMonthList.first().name} - ${feeMonthList.last().name}" + discountText
                                invoiceDetails = new InvoiceDetails(
                                        academicYear: academicYear,
                                        student: student,
                                        feeItems: feeItems,
                                        description: description,
                                        quantity: quantity,
                                        amount: amount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createBy
                                )
                                invoiceDetailsList.add(invoiceDetails)
                            }
                        }
                        //new adjustment end
                    } else {                 // Feed that not require prior activation of student
                        if (quantity == 1) {
                            description = feeMonthFrom?.name + discountText
                            feePayments = new FeePayments(
                                    academicYear: academicYear,
                                    student: student,
                                    feeItems: feeItems,
                                    itemsDetail: feeMonthFrom,
                                    itemDetailName: feeMonthFrom?.name,
                                    itemDetailSortPosition: feeMonthFrom?.sortPosition,
                                    quantity: 1, amount: amount,
                                    discount: discount,
                                    netPayment: netPayment,
                                    totalPayment: netPayment,
                                    createdBy: createBy
                            )
                            paymentsList.add(feePayments)
                            invoiceDetails = new InvoiceDetails(
                                    academicYear: academicYear,
                                    student: student,
                                    feeItems: feeItems,
                                    description: description,
                                    quantity: quantity,
                                    amount: amount,
                                    discount: discount,
                                    netPayment: netPayment,
                                    totalPayment: feePayment,
                                    createdBy: createBy
                            )
                            invoiceDetailsList.add(invoiceDetails)
                        } else {
                            payMonthStart = feeMonthFrom.sortPosition
                            payMonthEnd = payMonthStart + (quantity - 1)
                            feeMonthList = ItemsDetail.findAllByActiveStatusAndFeeItemsAndSortPositionBetween(ActiveStatus.ACTIVE, feeItems, payMonthStart, payMonthEnd, [max: 15, sort: "sortPosition", order: "asc"])
                            if (!feeMonthList || feeMonthList.size() != quantity) {
                                isAlreadyFeePaid = true
                                alreadyPaidItems.add("[${student.studentID}] ${student.name} has no available ${quantity} month due")
                            } else {
                                feeMonthList.each { ItemsDetail details ->
                                    feePayments = new FeePayments(
                                            academicYear: academicYear,
                                            student: student,
                                            feeItems: feeItems,
                                            itemsDetail: details,
                                            itemDetailName: details?.name,
                                            itemDetailSortPosition: details?.sortPosition,
                                            quantity: 1, amount: amount,
                                            discount: discount,
                                            netPayment: netPayment,
                                            totalPayment: netPayment,
                                            createdBy: createBy
                                    )
                                    paymentsList.add(feePayments)
                                }
                                description = "${feeMonthList.first().name} - ${feeMonthList.last().name}" + discountText
                                invoiceDetails = new InvoiceDetails(
                                        academicYear: academicYear,
                                        student: student,
                                        feeItems: feeItems,
                                        description: description,
                                        quantity: quantity,
                                        amount: amount,
                                        discount: discount,
                                        netPayment: netPayment,
                                        totalPayment: feePayment,
                                        createdBy: createBy
                                )
                                invoiceDetailsList.add(invoiceDetails)
                            }
                        }
                    }
                }
                totalFeeAmount += feeAmount
                totalFeePayment += feePayment
                itemNameList.add(feeItems.name)
            }
        }
        if (isAlreadyFeePaid) {
            flash.message = alreadyPaidItems.join(", ")
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        if (!paymentsList || paymentsList.size() == 0) {
            flash.message = "No Item Selected to Pay"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }

        Date invoiceDate = command.invoiceDate
        if (!invoiceDate) {
            invoiceDate = new Date().clearTime()
        }

        Double totalDiscounts = totalFeeAmount - totalFeePayment
        String invDescription = itemNameList.join(", ")
        Invoice invoice = new Invoice(
                academicYear: academicYear,
                student: student,
                invoiceNo: nextInvoice(),
                amount: totalFeeAmount.round(2),
                discount: totalDiscounts.round(2),
                payment: totalFeePayment.round(2),
                invoiceDate: invoiceDate,
                accountType: AccountType.INCOME,
                description: invDescription,
                createdBy: createBy
        )
        Invoice savedInvoice = collectionsService.saveInvoice(invoice, invoiceDetailsList, paymentsList)

        if (!savedInvoice) {
            flash.message = "Can't create invoice for [${student.studentID}] ${student.name}. Please contact to admin"
            render(view: '/collection/collections/alreadyPaidMsg')
            return
        }
        redirect(controller: 'accountsReport', action: 'printInvoice', params: [invoiceNo: savedInvoice?.invoiceNo])
    }
    // -- online from fee --
}





