package com.grailslab

import com.grailslab.command.SubjectMappingCommand
import com.grailslab.enums.GroupName
import com.grailslab.enums.SubjectType
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassSubjects
import com.grailslab.settings.Exam
import com.grailslab.settings.ExamSchedule
import com.grailslab.settings.SubjectName
import com.grailslab.viewz.StdClassSubjectView
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class ClassSubjectsService {
    def messageSource
    def springSecurityService
    def subjectService
    def examScheduleService


    ClassSubjects saveMapping(ClassSubjects obj, Boolean isEdit){

        ClassSubjects savedObj = obj.save()
        if (savedObj && (obj.subjectType == SubjectType.ALTERNATIVE)) {
            def alterSubList = subjectService.allSubjectInList(obj.alternativeSubIds)
            if (isEdit) {
                ClassSubjects ifDeletedBefore
                List<Long> alterSubListIds = alterSubList.collect {it.id}
                def preAddedSubList = ClassSubjects.findAllByParentSubIdAndSubjectType(savedObj.id, SubjectType.INUSE)
                for (addedInUse in preAddedSubList) {
                    if (!alterSubListIds.contains(addedInUse.subject.id)) {
                        if (addedInUse.activeStatus == ActiveStatus.ACTIVE){
                            addedInUse.activeStatus = ActiveStatus.DELETE
                            addedInUse.save()
                        }
                    } else {
                        addedInUse.isCtExam = obj.isCtExam
                        addedInUse.ctMark = obj.ctMark
                        addedInUse.ctMark2 = obj.ctMark2
                        addedInUse.ctMark3 = obj.ctMark3
                        addedInUse.ctEffMark = obj.ctEffMark
                        addedInUse.isHallExam = obj.isHallExam
                        addedInUse.hallMark = obj.hallMark
                        addedInUse.hallEffMark = obj.hallEffMark
                        addedInUse.isHallPractical = obj.isHallPractical
                        addedInUse.hallWrittenMark = obj.hallWrittenMark
                        addedInUse.hallPracticalMark = obj.hallPracticalMark
                        addedInUse.hallObjectiveMark = obj.hallObjectiveMark
                        addedInUse.hallSbaMark = obj.hallSbaMark
                        addedInUse.hallInput5 = obj.hallInput5
                        addedInUse.isPassSeparately = obj.isPassSeparately
                        addedInUse.writtenPassMark = obj.writtenPassMark
                        addedInUse.practicalPassMark = obj.practicalPassMark
                        addedInUse.objectivePassMark = obj.objectivePassMark
                        addedInUse.sbaPassMark = obj.sbaPassMark
                        addedInUse.input5PassMark = obj.input5PassMark
                        addedInUse.sortOrder = obj.sortOrder
                        addedInUse.passMark = obj.passMark
                        addedInUse.weightOnResult = obj.weightOnResult
                        addedInUse.groupName = obj.groupName
                        addedInUse.isExtracurricular = obj.isExtracurricular
                        addedInUse.isOtherActivity = obj.isOtherActivity
                        addedInUse.save()
                    }
                }
                alterSubListIds = preAddedSubList.collect {it.subject?.id}
                for (newSubAdded in alterSubList) {
                    if (!alterSubListIds.contains(newSubAdded.id)) {
                        new ClassSubjects(subject: newSubAdded, groupName: obj.groupName, className: obj.className, subjectType: SubjectType.INUSE, parentSubId: savedObj.id,
                                fullMark: obj.fullMark, passMark: obj.passMark, ctMark: obj.ctMark, ctMark2: obj.ctMark2, ctMark3: obj.ctMark3, ctEffMark: obj.ctEffMark, hallMark: obj.hallMark, hallEffMark: obj.hallEffMark,
                                weightOnResult: obj.weightOnResult, isCtExam: obj.isCtExam, isHallExam: obj.isHallExam, isExtracurricular: obj.isExtracurricular,
                                isOtherActivity: obj.isOtherActivity, isHallPractical: obj.isHallPractical, hallWrittenMark: obj.hallWrittenMark,
                                hallPracticalMark: obj.hallPracticalMark, hallObjectiveMark: obj.hallObjectiveMark, hallSbaMark: obj.hallSbaMark, hallInput5: obj.hallInput5,
                                sortOrder: obj.sortOrder, isPassSeparately: obj.isPassSeparately, writtenPassMark: obj.writtenPassMark, practicalPassMark: obj.practicalPassMark,
                                objectivePassMark: obj.objectivePassMark, sbaPassMark: obj.sbaPassMark, input5PassMark: obj.input5PassMark,
                                createdBy:obj.createdBy
                        ).save()
                    } else {
                        ifDeletedBefore = ClassSubjects.findBySubjectAndClassNameAndActiveStatusNotEqualAndParentSubId(newSubAdded, obj.className, ActiveStatus.ACTIVE, savedObj.id)
                        if (ifDeletedBefore) {
                            ifDeletedBefore.activeStatus = ActiveStatus.ACTIVE
                            ifDeletedBefore.save()
                        }
                    }
                }

                //handle edit case
            } else {
                for (subject in alterSubList) {
                    new ClassSubjects(subject: subject, groupName: obj.groupName, className: obj.className, subjectType: SubjectType.INUSE, parentSubId: savedObj.id,
                            fullMark: obj.fullMark, passMark: obj.passMark, ctMark: obj.ctMark, ctMark2: obj.ctMark2, ctMark3: obj.ctMark3, ctEffMark: obj.ctEffMark, hallMark: obj.hallMark, hallEffMark: obj.hallEffMark,
                            weightOnResult: obj.weightOnResult, isCtExam: obj.isCtExam, isHallExam: obj.isHallExam, isExtracurricular: obj.isExtracurricular,
                            isOtherActivity: obj.isOtherActivity, isHallPractical: obj.isHallPractical, hallWrittenMark: obj.hallWrittenMark,
                            hallPracticalMark: obj.hallPracticalMark, hallObjectiveMark: obj.hallObjectiveMark, hallSbaMark: obj.hallSbaMark, hallInput5: obj.hallInput5,
                            sortOrder: obj.sortOrder, isPassSeparately: obj.isPassSeparately, writtenPassMark: obj.writtenPassMark, practicalPassMark: obj.practicalPassMark,
                            objectivePassMark: obj.objectivePassMark, sbaPassMark: obj.sbaPassMark, input5PassMark: obj.input5PassMark,
                            createdBy:obj.createdBy
                    ).save(flush: true)
                }
            }
        }
        savedObj
    }

    static final String[] sortColumns = ['sortPosition','subjectName','subjectType']
    LinkedHashMap classSubjectsPaginateList(GrailsParameterMap params, ClassName className){
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.SORT_ORDER_ASC
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns,iSortingCol)
        List dataReturns = new ArrayList()
        def c = StdClassSubjectView.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
                eq("classNameId",className.id)
                'ne'("subjectType",SubjectType.INUSE)
            }
            if (sSearch) {
                or {
                    ilike('subjectName', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String subjectType
        String ctExam
        String hallExam
        String subjectName
        String ctEffective
        String hallEffective
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { StdClassSubjectView classSubjects ->
                ctExam = 'No Class Test'
                ctEffective = '-'
                if (classSubjects.isCtExam) {
                    ctExam = "CT: "+classSubjects.ctMark
                    if (classSubjects.ctMark2 > 0) {
                        ctExam += ", CT 2: "+classSubjects.ctMark2
                    }
                    if (classSubjects.ctMark3 > 0) {
                        ctExam += ", CT 3: "+classSubjects.ctMark3
                    }
                    ctEffective = classSubjects.ctEffMark +"%"
                }

                hallExam = "Written: "+classSubjects.hallMark
                if (classSubjects.isHallPractical) {
                    hallExam = "Wri: "+classSubjects.hallWrittenMark
                    if (classSubjects.hallObjectiveMark && classSubjects.hallObjectiveMark > 0){
                        hallExam += ", Obj: "+classSubjects.hallObjectiveMark
                    }
                    if (classSubjects.hallPracticalMark && classSubjects.hallPracticalMark > 0){
                        hallExam += ", Prac: "+classSubjects.hallPracticalMark
                    }
                    if (classSubjects.hallSbaMark && classSubjects.hallSbaMark > 0){
                        hallExam += ", SBA: "+classSubjects.hallSbaMark
                    }

                    if (classSubjects.hallInput5 && classSubjects.hallInput5 > 0){
                        hallExam += ", Input5: "+classSubjects.hallInput5
                    }

                }
                hallEffective = classSubjects.hallEffMark +'%'

                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                subjectName = classSubjects.subjectName
                if (classSubjects.subjectType == SubjectType.ALTERNATIVE) {
                    subjectName += " [ "+classSubjects.alternativeSubNames+" ]"
                }
                subjectType = classSubjects.subjectType?.value
                if(classSubjects.groupName){
                    subjectType += " [ "+ classSubjects.groupName +" ]"
                }
                // passMark = classSubjects.passMark? classSubjects.passMark +' %': ""

                dataReturns.add([DT_RowId: classSubjects.id, 0: serial, 1: subjectName, 2: subjectType,3:ctExam, 4: ctEffective, 5: hallExam, 6: hallEffective, 7: classSubjects.weightOnResult +' %', 8: ''])


            }

        }
        return [totalCount:totalCount,results:dataReturns]
    }

    def classSubjectsList(ClassName className){
        String sSortDir = CommonUtils.SORT_ORDER_ASC
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            and {
                eq("classNameId",className.id)
                'ne'("subjectType",SubjectType.INUSE)
            }
            order("sortPosition", sSortDir)
        }
        return results
    }

    //refactored
    def subjectsIdListForClassSubject(ClassName className, GroupName groupName = null) {
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            eq("classNameId", className.id)
            'ne'("subjectType",SubjectType.INUSE)
            if(groupName){
                or {
                    and {
                        isNull("groupName")
                    }
                    and {
                        eq("groupName", groupName.key)
                    }
                }
            }
            projections {
                property('subjectId')
            }
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    def availableSubjectsToAddClass(ClassName className, GroupName groupName = null) {
        def addedSubjectIdList
        def subjectList
        if (className.groupsAvailable) {
            addedSubjectIdList =  subjectsIdListForClassSubject(className, groupName)
            if(addedSubjectIdList && addedSubjectIdList.size>0){
                subjectList = subjectService.allMainSubjectNotInListDropDown(addedSubjectIdList)
            }else {
                subjectList = subjectService.allMainSubjectDropDownList()
            }
        } else {
            addedSubjectIdList =  subjectsIdListForClassSubject(className)
            if(addedSubjectIdList && addedSubjectIdList.size>0){
                subjectList = subjectService.allMainSubjectNotInListDropDown(addedSubjectIdList)
            }else {
                subjectList = subjectService.allMainSubjectDropDownList()
            }
        }
        subjectList
    }

    //refactor done
    def subjectTypeCompulsoryListStr(ClassName className, GroupName groupName=null) {
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            if(groupName){
                eq("classNameId", className.id)
                or {
                    and {
                        eq("subjectType", SubjectType.COMPULSORY)
                        isNull("groupName")
                    }
                    and {
                        eq("subjectType", SubjectType.COMPULSORY)
                        eq("groupName", groupName.key)
                    }
                }
            } else {
                and {
                    eq("classNameId", className.id)
                    eq("subjectType", SubjectType.COMPULSORY)
                }
            }
            projections {
                property 'subjectName', 'name'
            }
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        results?.join(', ')
    }
    //refactor done
    def subjectChooseOptComDDList(ClassName className, GroupName groupName=null) {
        List dataReturns = new ArrayList()
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            eq("classNameId", className.id)
            'in'("subjectType", SubjectType.chooseOptCom() as List)
            if(groupName){
                or {
                    and {
                        isNull("groupName")
                    }
                    and {
                        eq("groupName", groupName.key)
                    }
                }
            }
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        for (classSub in results) {
            dataReturns.add([id: classSub.subjectId, name: classSub.subjectName])
        }
        dataReturns
    }
    //refactor done
    def subjectDropDownList(ClassName className, GroupName groupName=null) {
        List dataReturns = new ArrayList()
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            eq("classNameId", className.id)
            'in'("subjectType", SubjectType.classUse() as List)
            if(groupName){
                or {
                    and {
                        isNull("groupName")
                    }
                    and {
                        eq("groupName", groupName.key)
                    }
                }
            }
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        for (classSub in results) {
            dataReturns.add([id: classSub.subjectId, name: classSub.subjectName])
        }
        dataReturns
    }


    //refactor done
    def subjectsNameListForClassSubject(ClassName className, GroupName groupName = null) {
        def c = StdClassSubjectView.createCriteria()
        def results = c.list() {
            eq("classNameId", className.id)
            'ne'("subjectType",SubjectType.INUSE)
            if(groupName){
                or {
                    and {
                        isNull("groupName")
                    }
                    and {
                        eq("groupName", groupName.key)
                    }
                }
            }
            projections {
                property('subjectName')
            }
            order("sortPosition", CommonUtils.SORT_ORDER_ASC)
        }
        return results
    }

    ClassSubjects getBySubject(ClassName className, SubjectName subjectName){
        return ClassSubjects.findByClassNameAndSubjectAndActiveStatus(className, subjectName, ActiveStatus.ACTIVE)
    }
    ClassSubjects getBySubject(ClassName className, GroupName groupName, SubjectName subjectName){
        def c = ClassSubjects.createCriteria()
        def results = c.get {
            eq("className", className)
            eq("subject", subjectName)
            eq("activeStatus", ActiveStatus.ACTIVE)
            or {
                isNull("groupName")
                eq("groupName", groupName)
            }
        }
        return results
    }

    def inactive(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username
        ClassSubjects classSubjects = ClassSubjects.get(id)
        if (!classSubjects) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        if (classSubjects.activeStatus.equals(ActiveStatus.INACTIVE)) {
            classSubjects.activeStatus = ActiveStatus.ACTIVE
        } else {
            classSubjects.activeStatus = ActiveStatus.INACTIVE
            if (classSubjects.subjectType == SubjectType.ALTERNATIVE) {
                def inuseSubjectList = ClassSubjects.findAllByActiveStatusAndParentSubId(ActiveStatus.ACTIVE, classSubjects.id)
                for (inUseSubject in inuseSubjectList) {
                    inUseSubject.activeStatus = ActiveStatus.DELETE
                    inUseSubject.updatedBy = createOrUpdate
                    inUseSubject.save(flush: true)
                }
            }
        }
        classSubjects.updatedBy = createOrUpdate
        classSubjects.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, "Subject Mark Deleted successfully.")
        return result
    }

    def saveOrUpdateSubject(SubjectMappingCommand subjectsCommand){
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdate = springSecurityService.principal.username

        if (subjectsCommand.hasErrors()) {
            def errorList = subjectsCommand?.errors?.allErrors?.collect{messageSource.getMessage(it,null)}
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
           return result
        }

        boolean isExtracurricular = false
        boolean isOtherActivity = false

        boolean isCtExam = false
        Integer ctMark = 0
        Integer ctMark2 = 0
        Integer ctMark3 = 0
        Integer ctEffMark = 0

        Integer hallMark = 0
        Integer hallEffMark = 0

        Boolean isHallPractical = false
        Integer hallWrittenMark = 0
        Integer hallPracticalMark = 0
        Integer hallObjectiveMark = 0
        Integer hallSbaMark = 0
        Integer hallInput5 = 0

        Boolean isPassSeparately = false
        Integer writtenPassMark = 0
        Integer practicalPassMark = 0
        Integer objectivePassMark = 0
        Integer sbaPassMark = 0
        Integer input5PassMark = 0

        ClassSubjects classSubjects
        String message
        Boolean isEdit = false

        if(subjectsCommand.id){
            classSubjects = ClassSubjects.get(subjectsCommand.id)
            if (!classSubjects) {
                result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
                result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
                return result
            }

            if(subjectsCommand.className.groupsAvailable){
                classSubjects.properties['groupName', 'subject', 'subjectType', 'weightOnResult','sortOrder','passMark','alternativeSubIds'] = subjectsCommand.properties
            } else {
                classSubjects.properties['subject', 'subjectType', 'weightOnResult','sortOrder','passMark','alternativeSubIds'] = subjectsCommand.properties
            }
            classSubjects.updatedBy = createOrUpdate
            isEdit = true
            message="Successfully Updated"
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        } else {
            classSubjects = new ClassSubjects(subjectsCommand.properties)
            message="Successfully added"
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        }

        if (subjectsCommand.isExtracurricular){
            isExtracurricular = true
        }
        if (subjectsCommand.isOtherActivity){
            isOtherActivity = true
        }
        if (subjectsCommand.isCtExam) {
            isCtExam = true
            ctMark = subjectsCommand.ctMark ?:0
            ctMark2 = subjectsCommand.ctMark2 ?:0
            ctMark3 = subjectsCommand.ctMark3 ?:0
            ctEffMark = subjectsCommand.ctEffMark ?:0
        }

        boolean isHallExam = true
        hallEffMark = subjectsCommand.hallEffMark ?:0

        if (subjectsCommand.isHallPractical) {
            isHallPractical = true
            hallWrittenMark = subjectsCommand.hallWrittenMark?:0
            hallPracticalMark = subjectsCommand.hallPracticalMark?:0
            hallObjectiveMark = subjectsCommand.hallObjectiveMark?:0
            hallSbaMark = subjectsCommand.hallSbaMark?:0
            hallInput5 = subjectsCommand.hallInput5?:0
            hallMark = hallWrittenMark + hallPracticalMark + hallObjectiveMark + hallSbaMark + hallInput5

            if (subjectsCommand.isPassSeparately) {
                isPassSeparately = true
                writtenPassMark = subjectsCommand.writtenPassMark ?:0
                practicalPassMark = subjectsCommand.practicalPassMark ?:0
                objectivePassMark = subjectsCommand.objectivePassMark ?:0
                sbaPassMark = subjectsCommand.sbaPassMark ?:0
                input5PassMark = subjectsCommand.input5PassMark ?:0
            }
        } else {
            hallMark = subjectsCommand.hallMark ?:0
        }

        classSubjects.isCtExam = isCtExam
        classSubjects.ctMark = ctMark
        classSubjects.ctMark2 = ctMark2
        classSubjects.ctMark3 = ctMark3
        classSubjects.ctEffMark = ctEffMark

        classSubjects.isOtherActivity = isOtherActivity
        classSubjects.isExtracurricular = isExtracurricular

        classSubjects.isHallExam = isHallExam
        classSubjects.hallMark = hallMark
        classSubjects.hallEffMark = hallEffMark

        classSubjects.isHallPractical = isHallPractical
        classSubjects.hallWrittenMark = hallWrittenMark
        classSubjects.hallPracticalMark = hallPracticalMark
        classSubjects.hallObjectiveMark = hallObjectiveMark
        classSubjects.hallSbaMark = hallSbaMark
        classSubjects.hallInput5 = hallInput5

        classSubjects.isPassSeparately = isPassSeparately
        classSubjects.writtenPassMark = writtenPassMark
        classSubjects.practicalPassMark = practicalPassMark
        classSubjects.objectivePassMark = objectivePassMark
        classSubjects.sbaPassMark = sbaPassMark
        classSubjects.input5PassMark = input5PassMark
        classSubjects.createdBy = createOrUpdate
        saveMapping(classSubjects, isEdit)

        result.put(CommonUtils.MESSAGE, message)
        return result
    }
}
