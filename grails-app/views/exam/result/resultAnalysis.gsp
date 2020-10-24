<%@ page import="com.grailslab.enums.PrintOptionType; com.grailslab.gschoolcore.AcademicYear; com.grailslab.enums.ExamTerm" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Analysis</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Result Analysis"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Result Analysis
            </header>
            <div class="panel-body">
                <div class="row" id="analysis-list-holder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="analysisAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-analysis" id="analysisAcademicYear" name='analysisAcademicYear' tabindex="1"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="analysisExamName" id="analysisExamName" class="form-control analysis-exam-name-step" tabindex="2">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisExamType" class="col-md-2 control-label">Exam Type</label>
                            <div class="col-md-6">
                                <g:select class=" form-control analysis-exam-type" id="analysisExamType" name='analysisExamType' tabindex="3"
                                          noSelection="${['': 'Select Exam Term...']}"
                                          from='${com.grailslab.enums.ExamType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisShiftName" class="col-md-2 control-label">Shift Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control analysis-shift-name" id="analysisShiftName" name='analysisShiftName' tabindex="4"
                                          noSelection="${['': 'All']}"
                                          from='${com.grailslab.enums.Shift.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="analysisClassName" id="analysisClassName" class="form-control analysis-class-name-step" tabindex="4">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisGroupName" class="col-md-2 control-label">Group</label>
                            <div class="col-md-6">
                                <g:select class="form-control analysis-group-name" id="analysisGroupName" name='analysisGroupName' tabindex="5"
                                          noSelection="${['': 'General']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="analysisSection" id="analysisSection" class="form-control analysis-section-step" tabindex="6">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" id="resultAnalysisSubjectHolder">
                            <label for="subject" class="col-md-2 control-label">Subject</label>
                            <div class="col-md-6">
                                <select class="form-control result-analysis-subject" id="subject" name='subject'>
                                    <option  value="">All Subject</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysisReportType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <select name="analysisReportType" id="analysisReportType" class="form-control analysis-report-type-step" tabindex="9">
                                    <option value="summaryReport">Summary</option>
                                    <option value="byClass">By Class</option>
                                    <option value="byGrade">By Grade</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="analysistabulationPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control analysis-exam-type-print" id="analysistabulationPrintOptionType" name='analysistabulationPrintOptionType' tabindex="10"
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="analysis-sheet-report-btn" class="btn btn-default">Dowmload Report</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Teacher Analysis
            </header>
            <div class="panel-body">
                <div class="row" id="teacher-analysis-list-holder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="teacherAnalysisAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-analysis-teacher" id="teacherAnalysisAcademicYear" name='teacherAnalysisAcademicYear' tabindex="1"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="teacherAnalysisExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="teacherAnalysisExamName" id="teacherAnalysisExamName" class="form-control teacher-analysis-exam-name-step" tabindex="2">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="teacherAnalysisExamType" class="col-md-2 control-label">Exam Type</label>
                            <div class="col-md-6">
                                <g:select class=" form-control teacher-analysis-exam-type" id="teacherAnalysisExamType" name='teacherAnalysisExamType' tabindex="3"
                                          noSelection="${['': 'Select Exam Term...']}"
                                          from='${com.grailslab.enums.ExamType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        %{--<div class="form-group">
                            <label for="teacherAnalysisShiftName" class="col-md-2 control-label">Shift Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control teacher-analysis-shift-name" id="teacherAnalysisShiftName" name='teacherAnalysisShiftName' tabindex="4"
                                          noSelection="${['': 'All']}"
                                          from='${com.grailslab.enums.Shift.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="teacherAnalysisClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="teacherAnalysisClassName" id="teacherAnalysisClassName" class="form-control teacher-analysis-class-name-step" tabindex="4">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="teacherAnalysisGroupName" class="col-md-2 control-label">Group</label>
                            <div class="col-md-6">
                                <g:select class="form-control teacher-analysis-group-name" id="teacherAnalysisGroupName" name='teacherAnalysisGroupName' tabindex="5"
                                          noSelection="${['': 'General']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>--}%
                        <div class="form-group">
                            <label for="teacherAnalysistabulationPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control teacher-analysis-exam-type-print" id="teacherAnalysistabulationPrintOptionType" name='teacherAnalysistabulationPrintOptionType' tabindex="10"
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="teacher-analysis-sheet-report-btn" class="btn btn-default">Dowmload Report</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    var academicYear, analysisReportType, examName, section, shiftType, subject, student, className, yearNameUrl, examClassUrl, examAsSectionListUrl, examId, printOptionType, groupName;
    jQuery(function ($) {
        //Tabulation  Top sheet show
        $('#analysis-list-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-analysis',
                    onChange: function(value){
                        loadAnalysisExamNames();
                    }
                },
                {
                    selector: '.analysis-exam-name-step',
                    requires: ['.academic-year-step-analysis'],
                    onChange: function (value) {
                        loadSubject();
                    }
                },
                {
                    selector: '.analysis-exam-type',
                    requires: ['.analysis-exam-name-step'],
                    onChange: function (value) {
                        loadanalysisClassNames();
                    }
                },
                {
                    selector: '.analysis-class-name-step',
                    requires: ['.analysis-exam-type'],
                    onChange: function (value) {
                        loadAnalysisSection();
                    }
                },
                {
                    selector: '.analysis-group-name',
                    requires: ['.analysis-class-name-step'],
                    onChange: function (value) {
                        loadAnalysisSection();
                    }
                },
                {
                    selector: '.analysis-section-step',
                    requires: ['.analysis-class-name-step'],
                    onChange: function (value) {
                        loadSubject();
                    }
                },

                {
                    selector: '.result-analysis-subject',
                    requires: ['.analysis-exam-name-step']
                },
                {
                    selector: '.analysis-report-type-step',
                    requires: ['.analysis-exam-type']
                },
                {
                    selector: '.analysis-exam-type-print',
                    requires: ['.analysis-exam-type']
                }
            ]
        });

        $('#analysis-sheet-report-btn').click(function (e) {
            academicYear=$('#analysisAcademicYear').val();
            examName = $('#analysisExamName').val();
            examType = $('#analysisExamType').val();
            shiftType = $('#analysisShiftName').val();
            classname = $('#analysisClassName').val();
            groupName = $('#analysisGroupName').val();
            section = $('#analysisSection').val();
            subject = $('#subject').val();
            analysisReportType = $('#analysisReportType').val();
            printOptionType = $('#analysistabulationPrintOptionType').val();
            if(academicYear != "" && examName != "" && examType != ""){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'resultAnalysis','_blank')}?academicYear="+academicYear+"&examName="+examName+"&examType="+examType+"&className="+classname+"&groupName="+groupName+"&examId="+section+"&subjectName="+subject+"&printOptionType="+printOptionType+"&analysisReportType="+analysisReportType+"&shift="+shiftType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });

        $('#teacher-analysis-list-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-analysis-teacher',
                    onChange: function(value){
                        loadTeacherAnalysisExamNames();
                    }
                },
                {
                    selector: '.teacher-analysis-exam-name-step',
                    requires: ['.academic-year-step-analysis-teacher'],
                    onChange: function(value){
                        loadTeacherAnalysisBtn();
                    }
                },
                {
                    selector: '.teacher-analysis-exam-type',
                    requires: ['.teacher-analysis-exam-name-step'],
                    onChange: function(value){
                        loadTeacherAnalysisBtn();
                    }
                },
                {
                    selector: '.teacher-analysis-exam-type-print',
                    requires: ['.teacher-analysis-exam-type'],
                    onChange: function(value){
                        loadTeacherAnalysisBtn();
                    }
                }
            ]
        });

        $('#teacher-analysis-sheet-report-btn').click(function (e) {
            academicYear=$('#teacherAnalysisAcademicYear').val();
            examName = $('#teacherAnalysisExamName').val();
            examType = $('#teacherAnalysisExamType').val();
            printOptionType = $('#teacherAnalysistabulationPrintOptionType').val();
            if(academicYear != "" && examName != "" && examType != ""){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'teacherAnalysis','_blank')}?academicYear="+academicYear+"&examName="+examName+"&examType="+examType+"&printOptionType="+printOptionType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
    });

    function loadSubject() {
        section = $('#analysisSection').val();
        examName = $('#analysisExamName').val();
        examType = $('#analysisExamType').val();
        classname = $('#analysisClassName').val();
        if (examName != "") {
            showLoading("#analysis-list-holder");
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'remote',action: 'examSubjectListForAnalysis')}/"+examName+"?exam="+section +"&examType=" + examType+"&className="+classname,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var $select = $('#subject');
                        $select.find('option:gt(0)').remove();
                        $.each(data.scheduleList, function (key, value) {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                    hideLoading("#analysis-list-holder");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
    }


    function loadTeacherAnalysisBtn(){
        examName =$('#teacherAnalysisExamType').val();
        if (examName) {
            $('#teacher-analysis-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
        } else {
            $('#teacher-analysis-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
        }
    }
    function loadAnalysisExamNames(){
        academicYear=$('#analysisAcademicYear').val();
        if(academicYear!=""){
            yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
            loadExamNames(yearNameUrl, $('#analysisExamName'),"#analysis-list-holder");
        }
    }
    function loadTeacherAnalysisExamNames(){
        academicYear=$('#teacherAnalysisAcademicYear').val();
        if(academicYear!=""){
            yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
            loadExamNames(yearNameUrl, $('#teacherAnalysisExamName'),"#teacher-analysis-list-holder");
        }
    }
    function loadanalysisClassNames(){
        examName =$('#analysisExamName').val();
        if (examName) {
            $('#analysis-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
            examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
            loadExamClass(examClassUrl, '#analysisClassName', "#analysis-list-holder")
        } else {
            $('#analysis-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
        }
    }

    function loadAnalysisSection(){
        examName =$('#analysisExamName').val();
        className =$('#analysisClassName').val();
        groupName = $('#analysisGroupName').val();
        if(examName!="" && className!=""){
            examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className+"&groupName="+groupName;
            loadExamAsClassSectionList(examAsSectionListUrl, $('#analysisSection'),"#analysis-list-holder");
        }
    }

</script>
</body>
</html>