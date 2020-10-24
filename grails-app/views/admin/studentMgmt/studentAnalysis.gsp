<%@ page import="com.grailslab.enums.PrintOptionType; com.grailslab.gschoolcore.AcademicYear" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Student Analysis</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student Analysis"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Analysis
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
                                <g:select class="form-control analysis-class-name-step" id="analysisClassName" name='analysisClassName' tabindex="2"
                                          noSelection="${['': 'All Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
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

                        <div class="form-group">
                            <label for="analysisReportType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <select name="analysisReportType" id="analysisReportType" class="form-control analysis-report-type-step" tabindex="9">
                                    <option value="byClass">By Class</option>
                                    <option value="bySection">By Section</option>
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
<script>
    var academicYear, analysisReportType, examName, section, shiftType, subject, student, className, yearNameUrl, examClassUrl, examAsSectionListUrl, examId, printOptionType, groupName, analysisShiftName;
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
                    selector: '.analysis-shift-name',
                    requires: ['.academic-year-step-analysis'],
                    onChange: function (value) {
                        loadAnalysisSection();
                    }
                },
                {
                    selector: '.analysis-class-name-step',
                    requires: ['.academic-year-step-analysis'],
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
                    requires: ['.analysis-class-name-step']
                },
                {
                    selector: '.analysis-report-type-step',
                    requires: ['.academic-year-step-analysis']
                },
                {
                    selector: '.analysis-exam-type-print',
                    requires: ['.academic-year-step-analysis']
                }
            ]
        });

        $('#analysis-sheet-report-btn').click(function (e) {
            academicYear=$('#analysisAcademicYear').val();
            analysisShiftName = $('#analysisShiftName').val();
            className = $('#analysisClassName').val();
            groupName = $('#analysisGroupName').val();
            section = $('#analysisSection').val();
            analysisReportType = $('#analysisReportType').val();
            printOptionType = $('#analysistabulationPrintOptionType').val();
            if(academicYear != ""){
                var sectionParam ="${g.createLink(controller: 'studentReport',action: 'analysis','_blank')}?academicYear="+academicYear+"&shift="+analysisShiftName+"&className="+className+"&groupName="+groupName+"&printOptionType="+printOptionType+"&analysisReportType="+analysisReportType+"&section="+section;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
    });

    function loadAnalysisExamNames(){
        academicYear=$('#analysisAcademicYear').val();
        if(academicYear!=""){
            $('#analysis-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
        } else {
            $('#analysis-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
        }
    }

    function loadAnalysisSection(){
        academicYear=$('#analysisAcademicYear').val();
        analysisShiftName =$('#analysisShiftName').val();
        className =$('#analysisClassName').val();
        groupName = $('#analysisGroupName').val();
        if(academicYear!="" && className!=""){
            examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'classSectionList')}?academicYear="+academicYear+"&className="+className+"&groupName="+groupName+"&shift="+analysisShiftName;
            loadClassSection(examAsSectionListUrl, $('#analysisSection'),"#analysis-list-holder");
        }
    }

</script>
</body>
</html>