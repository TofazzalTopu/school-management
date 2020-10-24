package com.grailslab

import com.grailslab.accounting.ChartOfAccount
import com.grailslab.accounting.FeeItems
import com.grailslab.accounting.Fees
import com.grailslab.accounting.ItemsDetail
import com.grailslab.command.ChartOfAccountsCommand
import com.grailslab.command.FeesCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class ChartOfAccountService {

    def schoolService
    def classNameService
    def messageSource
    def springSecurityService

    static final String[] sortColumns = ['id', 'acc.code', 'feeType', 'amount', 'discount', 'netPayable']

    LinkedHashMap feesPaginationList(GrailsParameterMap params, FeeAppliedType feeAppliedType) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : 1
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        ClassName className = null
        if (params.className) {
            className = ClassName.read(params.className.toLong())
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = Fees.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            createAlias('account', 'acc')
            and {
                eq("feeAppliedType", feeAppliedType)
                eq("activeStatus", ActiveStatus.ACTIVE)
                if (feeAppliedType == FeeAppliedType.CLASS_STD && className) {
                    eq("className", className)
                }
            }
            if (sSearch) {
                or {
                    ilike('acc.name', sSearch)
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
            String feeName
            String feeType
            Boolean hasDetailItem = false
            ChartOfAccount account
            ChartOfAccount pAccount
            results.each { Fees fees ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                if (fees.iterationType == FeeIterationType.DAILY || fees.iterationType == FeeIterationType.MONTHLY) {
                    hasDetailItem = true
                } else {
                    hasDetailItem = false
                }
                account = fees.account
                feeName = "${account?.code} - ${account?.name}"
                if (account.nodeType == NodeType.SECOND) {
                    pAccount = ChartOfAccount.read(account.parentId)
                    feeName = "${account?.code} - ${pAccount?.name} - ${account?.name}"
                }
                feeType = "${fees.feeType?.value} (${fees.iterationType?.value})"
                dataReturns.add([DT_RowId: fees.id, hasDetailItem: hasDetailItem, 0: serial, 1: feeName, 2: feeType, 3: fees.amount, 4: fees.discount, 5: fees.netPayable, 6: ''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def listAllItems() {
        List dataReturns = new ArrayList()
        def c = ChartOfAccount.createCriteria()
        def results = c.list() {
            and {
                'in'("accountType", AccountType.feeItems())
                eq("activeStatus", ActiveStatus.ACTIVE)
                'ne'("nodeType", NodeType.ROOT)
            }
            order("code", CommonUtils.SORT_ORDER_ASC)
        }
        String treeClassName
        String scholarShip="No"
        results.each { ChartOfAccount account ->
            if (account.nodeType == NodeType.FIRST) {
                treeClassName = "treegrid-${account.id}"
            } else if (account.nodeType == NodeType.SECOND || account.nodeType == NodeType.THIRD) {
                treeClassName = "treegrid-${account.id} treegrid-parent-${account.parentId}"
            } else {
                treeClassName = "treegrid-${account.id}"
            }
            if (account.scholarshipHead) {
                scholarShip = "Yes"
            } else {
                scholarShip = "No"
            }
            dataReturns.add([id: account.id, name: "${account.code} - ${account.name}", accountType: account.accountType?.value, scholarshipHead:scholarShip,code: account.code, nodeType: account?.nodeType?.key, parentId: account.parentId, hasChild: account.hasChild, allowChild: account.allowChild, allowEdit: account.allowEdit, treeClassName: treeClassName])
        }
        return [results: dataReturns]
    }

    def list(AccountType accountType) {
        List dataReturns = new ArrayList()
        def c = ChartOfAccount.createCriteria()
        def results = c.list() {
            and {
                eq("accountType", accountType)
                eq("activeStatus", ActiveStatus.ACTIVE)
                'ne'("nodeType", NodeType.ROOT)
            }
            order("code", CommonUtils.SORT_ORDER_ASC)
        }
        String treeClassName
        results.each { ChartOfAccount account ->
            if (account.nodeType == NodeType.FIRST) {
                treeClassName = "treegrid-${account.id}"
            } else if (account.nodeType == NodeType.SECOND || account.nodeType == NodeType.THIRD) {
                treeClassName = "treegrid-${account.id} treegrid-parent-${account.parentId}"
            } else {
                treeClassName = "treegrid-${account.id}"
            }
            dataReturns.add([id: account.id, name: "${account.code} - ${account.name}", accountType: account.accountType?.value, code: account.code, nodeType: account?.nodeType?.key, parentId: account.parentId, hasChild: account.hasChild, allowChild: account.allowChild, allowEdit: account.allowEdit, treeClassName: treeClassName])
        }
        return [results: dataReturns]
    }

    def feesDropDownList(def accountTypes, FeeAppliedType appliedType) {
        def c = ChartOfAccount.createCriteria()
        def results = c.list() {
            and {
                'in'("accountType", accountTypes)
                'in'("nodeType", [NodeType.FIRST, NodeType.SECOND] as List)
                eq("appliedType", appliedType)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("displayHead", Boolean.TRUE)
            }
            order('id', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        String name
        ChartOfAccount pAccount
        results.each { ChartOfAccount account ->
            name = "${account?.code} - ${account?.name}"
            if (account.nodeType == NodeType.SECOND) {
                pAccount = ChartOfAccount.read(account.parentId)
                name = "${account?.code} - ${pAccount?.name} - ${account?.name}"
            }
            dataReturns.add([id: account.id, name: name])
        }
        return dataReturns
    }

    def feesListForCollectionReport() {
        def c = ChartOfAccount.createCriteria()
        def results = c.list() {
            and {
                'in'("accountType", AccountType.feeMenuItems())
                'in'("nodeType", [NodeType.FIRST, NodeType.SECOND] as List)
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("displayHead", Boolean.TRUE)
            }
            order('code', CommonUtils.SORT_ORDER_ASC)
        }
        List dataReturns = new ArrayList()
        String name
        ChartOfAccount pAccount
        for (account in results){
            name = account.name
            if (account.nodeType == NodeType.SECOND) {
                pAccount = ChartOfAccount.read(account.parentId)
                name = "${pAccount?.name} - ${account?.name}"
            }
            dataReturns.add([id: account.id, account:account, name: name])
        }

        return dataReturns
    }


    def saveOrUpdateAccount(ChartOfAccountsCommand command) {

        LinkedHashMap result = new LinkedHashMap()
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        ChartOfAccount account
        String message
        ChartOfAccount ifExistAccount
        String createOrUpdateBy = springSecurityService.principal?.username
        if (command.id) {
            account = ChartOfAccount.get(command.id)
            if (!account) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            ifExistAccount = ChartOfAccount.findByCodeAndActiveStatusAndIdNotEqual(command.code, ActiveStatus.ACTIVE, command.id)
            if (ifExistAccount) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Chart Of Account Code Already Exist')
                return result
            }

            account.properties['code', 'name', 'appliedType', 'scholarshipHead', 'accountType'] = command.properties
            if (account.nodeType == NodeType.FIRST && command.allowSubHead) {
                account.allowChild = true
                account.displayHead = false
            } else {
                account.allowChild = false
                account.displayHead = true
            }
            account.updatedBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Account Head Updated successfully'
        } else {
            ifExistAccount = ChartOfAccount.findByCodeAndActiveStatus(command.code, ActiveStatus.ACTIVE)
            if (ifExistAccount) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Chart Of Account Code Already Exist')
                return result
            }
            ChartOfAccount paAccount

            if (command.parentId) {
                paAccount = ChartOfAccount.get(command.parentId)
            } else {
                paAccount = ChartOfAccount.findByAccountTypeAndNodeType(command.accountType, NodeType.ROOT)
            }

            if (!paAccount) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, 'Parent Account Not Found.')
                return result
            } else {
                if (!paAccount.hasChild) {
                    paAccount.hasChild = true
                }
            }

            NodeType childNode
            Boolean allowChild = true
            Boolean displayHead = false
            AccountType accountType = command.accountType

            if (paAccount.nodeType == NodeType.ROOT) {
                childNode = NodeType.FIRST
            } else {
                childNode = NodeType.SECOND
                accountType = paAccount.accountType
            }
            if (paAccount.nodeType == NodeType.ROOT && command.allowSubHead) {
                allowChild = true
                displayHead = false
            } else {
                allowChild = false
                displayHead = true
            }

            account = new ChartOfAccount(command.properties)
            account.nodeType = childNode
            account.allowChild = allowChild
            account.parentId = paAccount.id
            account.displayHead = displayHead
            account.accountType = accountType
            account.createdBy = createOrUpdateBy
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            message = 'Account head Added successfully'
        }

        account.save(flush: true)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def delete(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        ChartOfAccount account = ChartOfAccount.get(id)
        if (!account) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        ChartOfAccount parentAccount = ChartOfAccount.get(account.parentId)
        if (parentAccount) {
            def childAccount = ChartOfAccount.findByParentIdAndIdNotEqual(parentAccount.id, account.id)
            if (!childAccount) {
                parentAccount.hasChild = Boolean.FALSE
                parentAccount.save(flush: true)
            }
        }
        account.activeStatus = ActiveStatus.DELETE
        account.updatedBy = springSecurityService?.principal?.username
        account.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Chart Of Account Deleted Successfully.')
        return result
    }

    def activeFeeStatus(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        ItemsDetail itemsDetail = ItemsDetail.get(id)
        if (!itemsDetail) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        FeeItems feeItems = itemsDetail.feeItems
        if (feeItems.feeType != FeeType.ACTIVATION) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "You can't Inactivate ${feeItems.name}.")
            return result
        }
        String message
        if (itemsDetail.activeStatus.equals(ActiveStatus.INACTIVE)) {
            itemsDetail.activeStatus = ActiveStatus.ACTIVE
            message = "Item Activated Successfully"
        } else {
            itemsDetail.activeStatus = ActiveStatus.INACTIVE
            message = "Item inactive Successfully"
        }
        def allActiveItems = feeItems.itemsDetail.sort { it.sortPosition }
        int continuityOrder = 1
        allActiveItems.each { it ->
            if (it.activeStatus == ActiveStatus.ACTIVE) {
                it.continuityOrder = continuityOrder
                continuityOrder++
            }
        }
        feeItems.updatedBy = createOrUpdateBy
        feeItems.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def saveOrUpdateFees(FeesCommand command) {

        LinkedHashMap result = new LinkedHashMap()

        String createOrUpdateBy = springSecurityService.principal?.username
        AcademicYear academicYear = springSecurityService.principal?.academicYear

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            return result
        }

        Fees fees
        def ifAlreadyAdded = null
        def workingYears = schoolService.workingYears()
        if (command.id) {
            if (command.feeAppliedType == FeeAppliedType.CLASS_STD) {
                if (command.feeType == FeeType.ACTIVATION) {
                    ifAlreadyAdded = Fees.findAllByAccountAndClassNameAndActiveStatusAndAcademicYearInListAndIdNotEqual(command.account, command.className, ActiveStatus.ACTIVE, workingYears, command.id)
                } else {
                    ifAlreadyAdded = Fees.findAllByAccountAndClassNameAndActiveStatusAndIdNotEqual(command.account, command.className, ActiveStatus.ACTIVE, command.id)
                }
            } else if (command.feeAppliedType == FeeAppliedType.ALL_STD) {
                if (command.feeType == FeeType.ACTIVATION) {
                    ifAlreadyAdded = Fees.findAllByAccountAndFeeAppliedTypeAndActiveStatusAndAcademicYearInListAndIdNotEqual(command.account, FeeAppliedType.ALL_STD, ActiveStatus.ACTIVE, workingYears, command.id)
                } else {
                    ifAlreadyAdded = Fees.findAllByAccountAndFeeAppliedTypeAndActiveStatusAndIdNotEqual(command.account, FeeAppliedType.ALL_STD, ActiveStatus.ACTIVE, command.id)
                }
            }
        } else {
            if (command.feeAppliedType == FeeAppliedType.CLASS_STD) {
                if (command.feeType == FeeType.ACTIVATION) {
                    ifAlreadyAdded = Fees.findAllByAccountAndClassNameAndActiveStatusAndAcademicYearInList(command.account, command.className, ActiveStatus.ACTIVE, workingYears)
                } else {
                    ifAlreadyAdded = Fees.findAllByAccountAndClassNameAndActiveStatus(command.account, command.className, ActiveStatus.ACTIVE)
                }
            } else if (command.feeAppliedType == FeeAppliedType.ALL_STD) {
                if (command.feeType == FeeType.ACTIVATION) {
                    ifAlreadyAdded = Fees.findAllByAccountAndFeeAppliedTypeAndActiveStatusAndAcademicYearInList(command.account, FeeAppliedType.ALL_STD, ActiveStatus.ACTIVE, workingYears)
                } else {
                    ifAlreadyAdded = Fees.findAllByAccountAndFeeAppliedTypeAndActiveStatus(command.account, FeeAppliedType.ALL_STD, ActiveStatus.ACTIVE)
                }

            }
        }

        if (ifAlreadyAdded) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "${command.account.name} already added to ${command.className?.name}. You can edit or delete only.")
            return result
        }

        String message
        Double discountAmount = 0
        if (command.discount > 0 && command.discount <= 100) {
            discountAmount = command.amount * command.discount * 0.01
        }

        FeeItems feeItems
        ItemsDetail feeDetailItems
        ChartOfAccount account = command.account
        String accName
        ChartOfAccount pAccount

        def errorList
        if (command.id) {
            fees = Fees.get(command.id)
            if (!fees) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            if (fees.iterationType != command.iterationType) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, "Can't change Iteration Type ${fees.iterationType.value} to ${command.iterationType.value}.")
                return result
            }

            fees.properties = command.properties
            fees.netPayable = command.amount - discountAmount.round(2)
            fees.updatedBy = createOrUpdateBy
            if (fees.hasErrors() || !fees.save(flush: true)) {
                errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
                return result
            }

            try {
                //applied for class student start
                FeeItems oldItem = FeeItems.findByFees(fees)
                accName = "${account?.name}"
                if (account.nodeType == NodeType.SECOND) {
                    pAccount = ChartOfAccount.read(account.parentId)
                    accName = "${pAccount?.name} - ${account.name}"
                }
                if ((command.iterationType == FeeIterationType.RECEIVE) || (command.iterationType == FeeIterationType.YEARLY)) {
                    oldItem.chartOfAccount = account
                    oldItem.name = accName
                    oldItem.code = "${account?.code}"
                    oldItem.amount = fees.amount
                    oldItem.discount = fees.discount
                    oldItem.netPayable = fees.netPayable
                    oldItem.quickFee1 = fees.quickFee1
                    oldItem.quickFee2 = fees.quickFee2
                    oldItem.className = fees.className
                    oldItem.feeType = fees.feeType
                    oldItem.feeAppliedType = fees.feeAppliedType
                    oldItem.dueOnType = fees.dueOnType
                    oldItem.iterationType = fees.iterationType
                } else if (command.iterationType == FeeIterationType.MONTHLY) {
                    oldItem.chartOfAccount = account
                    oldItem.name = accName
                    oldItem.code = "${account?.code}"
                    oldItem.amount = fees.amount
                    oldItem.discount = fees.discount
                    oldItem.netPayable = fees.netPayable
                    oldItem.quickFee1 = fees.quickFee1
                    oldItem.quickFee2 = fees.quickFee2
                    oldItem.className = fees.className
                    oldItem.feeType = fees.feeType
                    oldItem.feeAppliedType = fees.feeAppliedType
                    oldItem.dueOnType = fees.dueOnType
                    oldItem.iterationType = fees.iterationType

                    oldItem.itemsDetail.each { ItemsDetail detail ->
                        detail.amount = fees.amount
                        detail.discount = fees.discount
                        detail.netPayable = fees.netPayable
                        detail.quickFee1 = fees.quickFee1
                        detail.quickFee2 = fees.quickFee2
                        detail.updatedBy = createOrUpdateBy
                        detail.save(flush: true)
                    }

                } else if (command.iterationType == FeeIterationType.DAILY) {
                    oldItem.chartOfAccount = account
                    oldItem.name = accName
                    oldItem.code = "${account?.code}"
                    oldItem.amount = fees.amount
                    oldItem.discount = fees.discount
                    oldItem.netPayable = fees.netPayable
                    oldItem.quickFee1 = fees.quickFee1
                    oldItem.quickFee2 = fees.quickFee2
                    oldItem.className = fees.className
                    oldItem.feeType = fees.feeType
                    oldItem.feeAppliedType = fees.feeAppliedType
                    oldItem.dueOnType = fees.dueOnType
                    oldItem.iterationType = fees.iterationType

                    oldItem.itemsDetail.each { ItemsDetail detail ->
                        detail.amount = fees.amount
                        detail.discount = fees.discount
                        detail.netPayable = fees.netPayable
                        detail.quickFee1 = fees.quickFee1
                        detail.quickFee2 = fees.quickFee2
                        detail.updatedBy = createOrUpdateBy
                        detail.save(flush: true)
                    }
                }
                oldItem.updatedBy = createOrUpdateBy
                if (oldItem.hasErrors() || !oldItem.save(flush: true)) {
                    errorList = feeItems?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                    message = errorList?.join('\n')
                    result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                    result.put(CommonUtils.MESSAGE, message)
                    return result
                }

            } catch (DataIntegrityViolationException e) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, e.getMessage())
                return result
            }
            message = 'Class Fees Updated successfully'
        } else {
            fees = new Fees(command.properties)
            message ='Class Fees Create successfully'
            fees.netPayable = command.amount - discountAmount.round(2)
            fees.createdBy = createOrUpdateBy
            fees.academicYear = academicYear
            if (fees.hasErrors() || !fees.save(flush: true)) {
                errorList = fees?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                message = errorList?.join('\n')
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }

            //applied for class studentd start
            accName = "${account?.name}"
            if(account.nodeType==NodeType.SECOND){
                pAccount = ChartOfAccount.read(account.parentId)
                accName = "${pAccount?.name} - ${account.name}"
            }
            int itemSortPosition =1
            if ((command.iterationType == FeeIterationType.RECEIVE) || (command.iterationType == FeeIterationType.YEARLY)) {
//                if(command.feeType!=FeeType.ACTIVATION){
                feeItems = new FeeItems(fees: fees, chartOfAccount: account, name: accName, code: "${account.code}",
                        amount: fees.amount, discount: fees.discount, netPayable: fees.netPayable,
                        quickFee1: fees.quickFee1, quickFee2: fees.quickFee2,
                        feeItemType: FeeItemType.FEE, className: fees.className, feeType: fees.feeType, createdBy: createOrUpdateBy, academicYear: academicYear,
                        feeAppliedType: fees.feeAppliedType, dueOnType: fees.dueOnType, iterationType: fees.iterationType)
//                }
                //Not implement else case as for now

            } else if (command.iterationType == FeeIterationType.MONTHLY) {
                feeItems = new FeeItems(fees: fees, chartOfAccount: account, name: accName, code: "${account.code}", amount: fees.amount,
                        discount: fees.discount, netPayable: fees.netPayable, quickFee1: fees.quickFee1, quickFee2: fees.quickFee2,
                        feeItemType: FeeItemType.FEE, className: fees.className, feeType: fees.feeType,
                        feeAppliedType: fees.feeAppliedType, dueOnType: fees.dueOnType,
                        iterationType: fees.iterationType, createdBy: createOrUpdateBy, academicYear: academicYear)
                if (account.appliedType == FeeAppliedType.CLASS_STD) {
                    if (fees.className.sectionType == 'College') {
                        YearMonths.collegeMonths().each { monthValue ->
                            feeDetailItems = new ItemsDetail(name: "${monthValue.value}",
                                    amount: fees.amount, discount: fees.discount, netPayable: fees.netPayable,
                                    quickFee1: fees.quickFee1, quickFee2: fees.quickFee2, sortPosition: itemSortPosition,
                                    continuityOrder: itemSortPosition, createdBy: createOrUpdateBy, academicYear : academicYear)
                            feeItems.addToItemsDetail(feeDetailItems)
                            itemSortPosition++
                        }
                    } else {
                        YearMonths.values().each { monthValue ->
                            feeDetailItems = new ItemsDetail(name: "${monthValue.value}",
                                    amount: fees.amount,
                                    discount: fees.discount,
                                    netPayable: fees.netPayable,
                                    quickFee1: fees.quickFee1,
                                    quickFee2: fees.quickFee2,
                                    sortPosition: itemSortPosition,
                                    continuityOrder: CommonUtils.monthOrder(monthValue),
                                    createdBy: createOrUpdateBy,
                                    academicYear: academicYear
                            )
                            feeItems.addToItemsDetail(feeDetailItems)
                            itemSortPosition++
                        }
                    }
                } else {
                    YearMonths.values().each { monthValue ->
                        feeDetailItems = new ItemsDetail(name: "${monthValue.value}",
                                amount: fees.amount,
                                discount: fees.discount,
                                netPayable: fees.netPayable,
                                quickFee1: fees.quickFee1,
                                quickFee2: fees.quickFee2, sortPosition: itemSortPosition,
                                continuityOrder: CommonUtils.monthOrder(monthValue),
                                createdBy: createOrUpdateBy, academicYear: academicYear)
                        feeItems.addToItemsDetail(feeDetailItems)
                        itemSortPosition++
                    }
                }

            } else if (command.iterationType == FeeIterationType.DAILY) {
//                if(command.feeType!=FeeType.ACTIVATION){
                feeItems = new FeeItems(fees: fees, chartOfAccount: account, name: accName, code: "${account.code}", amount: fees.amount,
                        discount: fees.discount, netPayable: fees.netPayable,
                        quickFee1: fees.quickFee1, quickFee2: fees.quickFee2,
                        feeItemType: FeeItemType.FEE, className: fees.className, feeType: fees.feeType,
                        feeAppliedType: fees.feeAppliedType, dueOnType: fees.dueOnType, iterationType:
                        fees.iterationType, createdBy: createOrUpdateBy, academicYear: academicYear)
                WeekDays.values().each { monthValue ->
                    feeDetailItems = new ItemsDetail(name: "${monthValue.value}",
                            amount: fees.amount, discount: fees.discount,
                            netPayable: fees.netPayable, quickFee1: fees.quickFee1,
                            quickFee2: fees.quickFee2, sortPosition: itemSortPosition,
                            continuityOrder: dayOrder(monthValue),
                            createdBy: createOrUpdateBy, academicYear : academicYear
                    )
                    feeItems.addToItemsDetail(feeDetailItems)
                    itemSortPosition++
                }
//                }
            }
            //applied for class studentd end
            if (feeItems.hasErrors() || !feeItems.save(flush: true)) {
                errorList = feeItems?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                message = errorList?.join('\n')
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, message)
                return result
            }
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, message)
        return result
    }

    def deleteFees(Long id) {

        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal?.username
        Fees fees = Fees.get(id)
        if (!fees) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }

        FeeItems oldItem = FeeItems.findByFees(fees)
        if (oldItem) {
            oldItem.activeStatus = ActiveStatus.DELETE
            oldItem.save(flush: true)
        }
        fees.activeStatus = ActiveStatus.DELETE
        fees.updatedBy = createOrUpdateBy
        fees.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Item Deleted successfully.")
        return result
    }
}
