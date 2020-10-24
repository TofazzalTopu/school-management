<%@ page import="com.grailslab.enums.PrintOptionType; com.grailslab.gschoolcore.AcademicYear; com.grailslab.enums.ExamTerm" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Print Result</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Result Report"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Progress Report by Section
            </header>
            <div class="panel-body">
                <div class="row" id="section-progress-report-holder">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="academicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step" id="academicYear" name='academicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="examName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="examName" id="examName" class="form-control exam-name-step" tabindex="3">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="className" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="className" id="className" class="form-control class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="section" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="section" id="section" class="form-control section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="sectionProPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control exam-type-print" id="sectionProPrintOptionType" name='sectionProPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="section-progress-report-btn" class="btn btn-default">Dowmload Report</button>
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
                Progress Report by Student
            </header>
            <div class="panel-body">
                <div class="row" id="student-progress-report-holder">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="stAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-2" id="stAcademicYear" name='stAcademicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="stExamName" id="stExamName" class="form-control st-exam-name-step" tabindex="3">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="stClassName" id="stClassName" class="form-control st-class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="stSection" id="stSection" class="form-control st-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" id="student-holder" style="display: none;">
                            <label for="student" class="col-md-2 control-label">Student </label>
                            <div class="col-md-6">
                                <select id="student" name='student' class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="studentProPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control st-exam-type-print" id="studentProPrintOptionType" name='studentProPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="student-progress-report-btn" class="btn btn-default">Dowmload Report</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<g:render template='/exam/result/tabulationSheet'/>
<g:render template='/exam/result/topSheet'/>
<g:render template='/exam/result/meritList'/>
<script>
    var academicYear, examTerm, examName, section, clsSectionUrl, student, className, yearNameUrl, examClassUrl, examAsSectionListUrl, examId, printOptionType;
    jQuery(function ($) {
        $('#section-progress-report-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step',
                    onChange: function(value){
                        loadSprExamNames();
                    }
                },
                {
                    selector: '.exam-name-step',
                    requires: ['.academic-year-step'],
                    onChange: function (value) {
                        loadSprClassNames();
                    }
                },
                {
                    selector: '.class-name-step',
                    requires: ['.exam-name-step'],
                    onChange: function (value) {
                        loadSprSection();
                    }
                },
                {
                    selector: '.section-step',
                    requires: ['.class-name-step']
                },

                {
                    selector: '.exam-type-print',
                    requires: ['.section-step']
                }
            ]
        });
        function loadSprExamNames(){
            academicYear=$('#academicYear').find("option:selected").val();
            if(academicYear!=""){
                yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
                loadExamNames(yearNameUrl, $('#examName'),"#section-progress-report-holder");
            }
        }
        function loadSprClassNames(){
            examName =$('#examName').val();
            if (examName) {
                examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
                loadExamClass(examClassUrl, '#className', "#section-progress-report-holder")
            }
        }
        function loadSprSection(){
            examName =$('#examName').val();
            className =$('#className').find("option:selected").val();
            if(examName!="" && className!=""){
                examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className;
                loadExamAsClassSectionList(examAsSectionListUrl, $('#section'),"#section-progress-report-holder");
            }
            $('#section-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
        }

        $('#section').change(function () {
            var optionSelected = $(this).find("option:selected");
            section  = optionSelected.val();
            if(section == undefined || section == ""){
                $('#section-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
            }else{
                $('#section-progress-report-btn').removeClass('btn-default').addClass('btn-primary');
            }
        });
        $('#section-progress-report-btn').click(function (e) {
            examName =$('#examName').val();
            section = $('#section').val();
            printOptionType = $('#sectionProPrintOptionType').val();
            if((examName != "") && (section != "")){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'sectionReportCard','_blank')}/"+examName+"?examId="+section+"&printOptionType="+printOptionType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
        //Progress report by Student
        $('#student-progress-report-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-2',
                    onChange: function(value){
                     loadStExamNames();
                    }
                },
                {
                    selector: '.st-exam-name-step',
                    requires: ['.academic-year-step-2'],
                    onChange: function (value) {
                    loadStClassNames();
                    }
                },
                {
                    selector: '.st-class-name-step',
                    requires: ['.st-exam-name-step'],
                    onChange: function (value) {
                     loadStSection();
                    }
                },
                {
                    selector: '.st-section-step',
                    requires: ['.st-class-name-step'],
                    onChange: function (value) {
                        loadStudent();
                    }
                },

                {
                    selector: '.st-exam-type-print',
                    requires: ['.st-section-step']
                }
            ]
        });

        function loadStExamNames(){
            academicYear=$('#stAcademicYear').val();
            if(academicYear!=""){
                yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
                loadExamNames(yearNameUrl, $('#stExamName'), "#student-progress-report-holder");
            }
        }
        function loadStClassNames(){
            examName =$('#stExamName').val();
            if (examName) {
                examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
                loadExamClass(examClassUrl, '#stClassName', "#student-progress-report-holder")
            }
        }

        function loadStSection(){
            examName =$('#stExamName').val();
            className =$('#stClassName').val();
            if(examName!="" && className!=""){
                examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className;
                loadExamAsClassSectionList(examAsSectionListUrl, $('#stSection'),"#student-progress-report-holder");


            }
            $('#student-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
        }


        $('#stSection').change(function () {
            var optionSelected = $(this).find("option:selected");
            section  = optionSelected.val();
            if(section == undefined || section == ""){
                $('#student-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
            }else{
                $('#student-progress-report-btn').removeClass('btn-default').addClass('btn-primary');
            }
        });

        $('#student-progress-report-btn').click(function (e) {
            examName =$('#stExamName').val();
            section = $('#stSection').val();
            student = $('#student').val();
            printOptionType = $('#studentProPrintOptionType').val();
            if((examName != "") && (section != "")&& (student != "")){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'sectionReportCard','_blank')}/"+examName+"?examId="+section+"&student="+student+"&printOptionType="+printOptionType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });


        function loadStudent(){
            examName =$('#stExamName').val();
            section = $('#stSection').val();
            if(examName!="" && section !=""){
                showLoading("#student-progress-report-loading-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'student',action: 'examStudentList')}/"+section,
                    success: function (data, textStatus) {
                        hideLoading("#student-progress-report-loading-holder");
                        $('#student').select2("destroy");
                        var $select = $('#student');
                        $select.find('option').remove();
                        if (data.isError == false) {
                            $.each(data.studentList,function(key, value)
                            {
                                $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $('#student').select2().enable(true);
                            $('#student-holder').show(500);
                            $('#student-progress-report-btn').removeClass('btn-default').addClass('btn-primary');
                        } else {
                            $('#student-holder').hide(500);
                            $('#student-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
                            $( "#student-progress-report-loading-holder" ).isLoading( "hide" );
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else{
                $('#student-holder').hide(500);
                $('#student-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }
    });

</script>
</body>
</html>