package com.grailslab.admin

import com.grailslab.CommonUtils
import com.grailslab.command.SubjectMappingCommand
import com.grailslab.enums.GroupName
import com.grailslab.enums.SubjectType
import com.grailslab.settings.ClassName
import com.grailslab.settings.ClassSubjects
import com.grailslab.settings.SubjectName
import grails.converters.JSON

class ClassSubjectsController {
    def messageSource
    def classSubjectsService
    def classNameService
    def subjectService
    def schoolService

    def index() {
        def classNameList = classNameService.allClassNames()
        List dataReturns = new ArrayList()
        List subjectNames
        String subjects
        classNameList.each { ClassName className ->
            subjectNames =  classSubjectsService.subjectsNameListForClassSubject(className)
            if(subjectNames){
                subjects = subjectNames.join(', ')
                dataReturns.add([id:className.id,sortPosition:className.sortPosition,className:className.name, subjects:subjects])
            } else {
                dataReturns.add([id:className.id,sortPosition:className.sortPosition,className:className.name, subjects:'No Subject'])
            }
        }
        render (view: '/admin/classSubject/classSubjectView', model: [dataReturns:dataReturns])
    }

    def list() {

        LinkedHashMap gridData
        String result
        if(!params.classId){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        ClassName className = ClassName.read(Long.parseLong(params.classId))
        if(!className){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        LinkedHashMap resultMap = classSubjectsService.classSubjectsPaginateList(params,className)
        int totalCount =resultMap.totalCount
        if(!resultMap || totalCount== 0){
            gridData = [iTotalRecords: 0, iTotalDisplayRecords: 0, aaData: []]
            result = gridData as JSON
            render result
            return
        }
        gridData = [iTotalRecords: totalCount, iTotalDisplayRecords: totalCount, aaData: resultMap.results]
        result = gridData as JSON
        render result
    }

    def subjects(Long id) {
        ClassName className = ClassName.read(id)
        if(!className){
            redirect(action: 'index')
        }

        def subjectTypeList
        if (className.allowOptionalSubject) {
            subjectTypeList = SubjectType.withOptional()
        } else {
            subjectTypeList = SubjectType.nonOptional()
        }
        LinkedHashMap resultMap =  classSubjectsService.classSubjectsPaginateList(params,className)

        def subjectList = classSubjectsService.availableSubjectsToAddClass(className)

        int totalCount = resultMap.totalCount
        if (!resultMap || totalCount == 0) {
            render (view: '/admin/classSubject/classSubjectMapping', model: [dataReturn: null, totalCount: 0, className:className, subjectList:subjectList, subjectTypeList: subjectTypeList])
            return
        }
        render (view: '/admin/classSubject/classSubjectMapping', model: [dataReturn: resultMap.results, totalCount: totalCount,className:className, subjectList:subjectList, subjectTypeList: subjectTypeList])
    }


    def editAllSubject(Long id){
        ClassName className = ClassName.read(id)
        if(!className){
            redirect(action: 'index')
        }
        def classSubjectList =  classSubjectsService.classSubjectsList(className)
        def subjectTypeList
        if (className.allowOptionalSubject) {
            subjectTypeList = SubjectType.withOptional()
        } else {
            subjectTypeList = SubjectType.nonOptional()
        }
        render(view: '/admin/classSubject/editAllSubject',model: [className: className, classSubjectList: classSubjectList, subjectTypeList: subjectTypeList])
    }

    def saveAllSubject(Long id) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        if (!params.classSubObjIds) {
            flash.message = "Class subject missing."
            redirect(action: 'subjects', id: id)
            return
        }

        List classSubIds
        if (params.classSubObjIds instanceof String) {
            classSubIds = new ArrayList()
            classSubIds.add(classSubIds)
        } else {
            classSubIds = params.classSubObjIds
        }

        Integer weightOnResult = 0
        Integer passMark = 0
        Integer sortOrder = 0

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
        classSubIds.each { clsSubId ->
            weightOnResult = 0
            passMark = 0
            sortOrder = 0
            isExtracurricular = false
            isOtherActivity = false
            isCtExam = false
            ctMark = 0
            ctMark2 = 0
            ctMark3 = 0
            ctEffMark = 0

            hallMark = 0
            hallEffMark = 0

            isHallPractical = false
            hallWrittenMark = 0
            hallPracticalMark = 0
            hallObjectiveMark = 0
            hallSbaMark = 0
            hallInput5 = 0

            isPassSeparately = false
            writtenPassMark = 0
            practicalPassMark = 0
            objectivePassMark = 0
            sbaPassMark = 0
            input5PassMark = 0
            classSubjects = ClassSubjects.get(Long.valueOf(clsSubId))
            if (classSubjects) {
                if (params."weightOnResult${clsSubId}") weightOnResult = params.getInt("weightOnResult$clsSubId")?:0
                if (params."passMark${clsSubId}") passMark = params.getInt("passMark$clsSubId")?:0
                if (params."sortOrder${clsSubId}") sortOrder = params.getInt("sortOrder$clsSubId")?:0

                if (params."isExtracurricular$clsSubId") {
                    isExtracurricular = true
                }
                if (params."isOtherActivity$clsSubId") {
                    isOtherActivity = true
                }
                if (params."isCtExam$clsSubId") {
                    isCtExam = true
                    if (params."ctMark$clsSubId") ctMark = params.getInt("ctMark$clsSubId")?:0
                    if (params."ctMark2$clsSubId") ctMark2 = params.getInt("ctMark2$clsSubId")?:0
                    if (params."ctMark3$clsSubId") ctMark3 = params.getInt("ctMark3$clsSubId")?:0
                    if (params."ctEffMark$clsSubId") ctEffMark = params.getInt("ctEffMark$clsSubId")?:0
                }

                if (params."hallEffMark$clsSubId") hallEffMark = params.getInt("hallEffMark$clsSubId")?:0
                if (params."isHallPractical$clsSubId") {
                    isHallPractical = true
                    if (params."hallWrittenMark$clsSubId") hallWrittenMark = params.getInt("hallWrittenMark$clsSubId")?:0
                    if (params."hallPracticalMark$clsSubId") hallPracticalMark = params.getInt("hallPracticalMark$clsSubId")?:0
                    if (params."hallObjectiveMark$clsSubId") hallObjectiveMark = params.getInt("hallObjectiveMark$clsSubId")?:0
                    if (params."hallSbaMark$clsSubId") hallSbaMark = params.getInt("hallSbaMark$clsSubId")?:0
                    if (params."hallInput5$clsSubId") hallInput5 = params.getInt("hallInput5$clsSubId")?:0
                    hallMark = hallWrittenMark + hallPracticalMark + hallObjectiveMark + hallSbaMark + hallInput5

                    if (params."isPassSeparately$clsSubId") {
                        isPassSeparately = true
                        if (params."writtenPassMark$clsSubId") writtenPassMark = params.getInt("writtenPassMark$clsSubId")?:0
                        if (params."practicalPassMark$clsSubId") practicalPassMark = params.getInt("practicalPassMark$clsSubId")?:0
                        if (params."objectivePassMark$clsSubId") objectivePassMark = params.getInt("objectivePassMark$clsSubId")?:0
                        if (params."sbaPassMark$clsSubId") sbaPassMark = params.getInt("sbaPassMark$clsSubId")?:0
                        if (params."input5PassMark$clsSubId") input5PassMark = params.getInt("input5PassMark$clsSubId")?:0
                    }

                } else {
                    if (params."hallMark$clsSubId") hallMark = params.getInt("hallMark$clsSubId")?:0
                }
                classSubjects.weightOnResult = weightOnResult
                classSubjects.passMark = passMark
                classSubjects.sortOrder = sortOrder
                classSubjects.isCtExam = isCtExam
                classSubjects.ctMark = ctMark
                classSubjects.ctMark2 = ctMark2
                classSubjects.ctMark3 = ctMark3
                classSubjects.ctEffMark = ctEffMark

                classSubjects.isOtherActivity = isOtherActivity
                classSubjects.isExtracurricular = isExtracurricular

                classSubjects.isHallExam = true
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

                classSubjectsService.saveMapping(classSubjects, true)
            }
        }
        flash.message = "Class subject mapping saved successfully"
        redirect(action: 'subjects', id: id)
    }

    def editSubject(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassSubjects classSubjects = ClassSubjects.read(id)
        if (!classSubjects) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        SubjectName subjectName = classSubjects.subject
        def alternativeSubjectList
        def selectedSubjectList
        if (classSubjects.subjectType == SubjectType.ALTERNATIVE) {
            alternativeSubjectList = subjectService.allAlterSubjectDropList(subjectName)
            selectedSubjectList = subjectService.getCommaSeparatedIdAsList(classSubjects.alternativeSubIds)
        }
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put(CommonUtils.OBJ,classSubjects)
        result.put('subjectName',subjectName.name)
        result.put('alternativeSubjectList',alternativeSubjectList)
        result.put('selectedSubjectList',selectedSubjectList)
        outPut = result as JSON
        render outPut
    }

    def inactive(Long id) {
        LinkedHashMap result = classSubjectsService.inactive(id)
        String output = result as JSON
        render output
    }

    def availableSubjectsToAdd(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        ClassName className = ClassName.read(id)
        if (!className) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            outPut = result as JSON
            render outPut
            return
        }
        GroupName groupName = null
        if (params.groupName) {
            groupName = GroupName.valueOf(params.groupName)
        }
        def subjectList = classSubjectsService.availableSubjectsToAddClass(className, groupName)
        result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
        result.put('subjectList',subjectList)
        outPut = result as JSON
        render outPut
    }

    def alternativeSubjectList(Long id){
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        SubjectName subjectName = SubjectName.read(id)
        if (!subjectName || !subjectName.hasChild) {
            result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
            result.put(CommonUtils.MESSAGE,"No Alternative subject added")
            outPut = result as JSON
            render outPut
            return
        }
        def alternativeSubList = subjectService.allAlterSubjectDropList(subjectName)
        if (alternativeSubList.size()>0) {
            String subjectIds = alternativeSubList.collect {it.id}.join(',')
            def alterSubList = subjectService.getCommaSeparatedIdAsList(subjectIds)
            result.put(CommonUtils.IS_ERROR,Boolean.FALSE)
            result.put('subjectList',alternativeSubList)
            result.put('selectedList',alterSubList)
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR,Boolean.TRUE)
        result.put(CommonUtils.MESSAGE,"No Alternative subject added")
        outPut = result as JSON
        render outPut
    }

    def saveSubject(SubjectMappingCommand subjectsCommand) {
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }

        LinkedHashMap result = classSubjectsService.saveOrUpdateSubject(subjectsCommand)
        String output = result as JSON
        render output
    }

}
