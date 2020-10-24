<%@ page import="com.grailslab.enums.BookTransactionType" %>

<html>
<head>
    <title>Books </title>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
</head>
<body>
<grailslab:breadCrumbActions controllerName="${params.controller}" breadCrumbTitleText="Manage Feedback Report"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Subject Wise Feedback
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="row" id="book-history-report-holder">
                        <g:form class="form-horizontal" role="form" controller="feedbackReport"
                                action="subjectFeedback" target="_blank" onsubmit="return addSubjectValidation()">
                            <div class="form-group">
                                <label for="className" class="col-md-2 control-label">Select Class</label>
                                <div class="col-md-6">
                                    <g:select class="form-control" id="className" name='className'
                                              noSelection="${['': 'Select Class']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="sectionName" class="col-md-2 control-label">Section</label>

                                <div class="col-md-6">
                                    <select name="sectionName" id="sectionName" class="form-control class-name-step" tabindex="3">
                                        <option value="">All Section</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="subjectName" class="col-md-2 control-label">Subject</label>
                                <div class="col-md-6">
                                    <select name="subjectName" id="subjectName" class="form-control class-name-step" tabindex="3">
                                        <option value="">All Subjects</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="weekNo" class="col-md-2 control-label">Week No</label>

                                <div class="col-md-6">
                                    <select name="weekNo" id="weekNo" class="form-control class-name-step" tabindex="3">
                                        <option value="">All Weeks</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="sortedBy" class="col-md-2 control-label">Sort By</label>

                                <div class="col-md-6">
                                    <select name="sortedBy" id="sortedBy" class="form-control class-name-step" tabindex="3">
                                        <option value="std.roll_no">Roll No</option>
                                        <option value="lfd.rating">Rating</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="feedbackPrintType" class="col-md-2 control-label">Print Type</label>

                                <div class="col-md-6">
                                    <g:select class="form-control" id="feedbackPrintType" name='feedbackPrintType'
                                              from='${com.grailslab.enums.PrintOptionType.values()}'
                                              optionKey="key" optionValue="value" required="required"></g:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button type="submit"
                                            class="btn btn-primary">Show Report</button>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </section>
    </div>
        <g:render template='/teacher/lesson/studentFeedbackReport'/>
        <g:render template='/teacher/lesson/averageFeedbackReport'/>
        <g:render template='/teacher/lesson/feedbackByStudentReport'/>

</div>
<script>
    function addSubjectValidation(){
        var clsName=$('#className').val();
        var clsSec=$('#sectionName').val();
        var subName=$('#subjectName').val();
        var weekNo=$('#weekNo').val();
        if(clsName && clsSec && subName && weekNo){
            return true;
        }else{
            alert("Please Select Class, Section,Subject Name and week No");
            return false;
        }
    }
    $(function ($) {

        var className, sectionName, classUrl, sectionListUrl, sectionWeekList;

    $('#className').on('change', function (e) {
        className =$('#className').val();
        if (className) {
            classUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className;
            loadClassSection(classUrl, '#sectionName', "#stu-manage-report-holder")
        }
        $('#sectionName').val("").trigger("change");
    });
    $('#sectionName').on('change', function (e) {
         sectionName = $('#sectionName').val();
        if(className!="" && sectionName!=""){
            sectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id="+sectionName+"&className="+className;
            sectionWeekList = "${g.createLink(controller: 'remote',action: 'lessonWeekList')}?id="+sectionName;
            loadSectionSubject(sectionListUrl,className,sectionName, $('#subjectName'), "#stu-manage-report-holder");
            loadSubjectWeek(sectionWeekList, $('#weekNo'), "#stu-manage-report-holder");
        }
        $('#subjectName').val("").trigger("change");
        $('#weekNo').val("").trigger("change");
    });
    });

</script>
</body>

</html>