<%@ page import="com.grailslab.enums.ExamTerm; com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Mark Entry  </title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Mark Entry"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Subject wise student list for Mark Entry
            </header>
            <div class="panel-body">
                <div class="row" id="subjectStudentHolder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="ssClassname" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control ss-classname-step" id="ssClassname" name='ssClassname' tabindex="4"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="ssSection" id="ssSection" class="form-control ss-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSubjectName" class="col-md-2 control-label">Subject Name</label>
                            <div class="col-md-6">
                                <select name="ssSubjectName" id="ssSubjectName" class="form-control ss-subject-step" tabindex="3">
                                    <option value="">Select Subject</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssPrintOptionType" class="col-md-2 control-label">Print</label>
                            <div class="col-md-6">
                                <g:select class="form-control ss-print-type" id="ssPrintOptionType" name='ssPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="downloadStudentSubjectBtn"
                                        class="btn btn-default">Download</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

 <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Special Mark Entry (Enter same Mark for all student)
            </header>
            <div class="panel-body">
                <div class="row" id="massMarkEntryHolder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="meExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <g:select class=" form-control me-exam-name-step" id="meExamName" name='meExamName'
                                          tabindex="4"
                                          noSelection="${['': 'Select Exam...']}"
                                          from='${examNameList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="meClassName"  id="meClassName" class="form-control me-class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="meSection" id="meSection" class="form-control me-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meExamType" class="col-md-2 control-label">Entry For</label>
                            <div class="col-md-6">
                                <g:select class=" form-control me-exam-type-step" id="meExamType" name='meExamType'
                                          tabindex="4"
                                          noSelection="${['': 'Select Exam Term...']}"
                                          from='${com.grailslab.enums.ExamType.values()}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>

                        <div class="form-group" id="me-subject-holder" style="display: none;">
                            <label for="meSubject" class="col-md-2 control-label">Subject</label>
                            <div class="col-md-3">
                                <select class="form-control" id="meSubject" name='meSubject'></select>
                            </div>
                            <label for="meSubject" class="col-md-1 control-label">Mark</label>
                            <div class="col-md-2">
                                <input type="text" name="markObtain" class="form-control" tabindex="3" id="markObtain"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="me-mark-entry-btn"
                                        class="btn btn-default">Entry Mark</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
    <script>
        var shiftExam, section, examTerm, examName, examType, subject, className, attendanceSection,examClassUrl,examAsSectionListUrl, markObtain;
        jQuery(function ($) {

            $('#subjectStudentHolder').cascadingDropdown({
                selectBoxes: [
                    {
                        selector: '.ss-classname-step',
                        onChange: function (value) {
                            loadStudentSubSection();
                            loadClassSubject();
                        }
                    },
                    {
                        selector: '.ss-section-step',
                        requires: ['.ss-classname-step'],
                        onChange: function (value) {
                            loadSectionSubject();
                        }
                    },
                    {
                        selector: '.ss-subject-step',
                        requires: ['.ss-classname-step']
                    },
                    {
                        selector: '.ss-print-type',
                        requires: ['.ss-classname-step']
                    }
                ]
            });
            $('#ssClassname').on('change', function (e) {
                $('#ssSection').val("").trigger("change");
            });

            function loadStudentSubSection(){
                className =$('#ssClassname').val();
                if (className) {
                    loadSection(className, '#ssSection', "#subjectStudentHolder")
                }
            }
            function loadClassSubject() {
                className =$('#ssClassname').val();
                if (className) {
                    showLoading("#subjectStudentHolder");
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'remote',action: 'classSubjectList')}?id=" + className,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                var $select = $('#ssSubjectName');
                                $select.find('option').remove();
                                $.each(data.subjectList, function (key, value) {
                                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                                });
                                $('#downloadStudentSubjectBtn').removeClass('btn-default').addClass('btn-primary');
                            } else {
                                $('#downloadStudentSubjectBtn').removeClass('btn-primary').addClass('btn-default');
                                showErrorMsg(data.message);
                            }
                            hideLoading("#subjectStudentHolder");
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                } else {
                    $('#downloadStudentSubjectBtn').removeClass('btn-primary').addClass('btn-default');
                }
            }
            function loadSectionSubject() {
                section = $('#ssSection').val();
                if (section) {
                    showLoading("#subjectStudentHolder");
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id=" + section,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                var $select = $('#ssSubjectName');
                                $select.find('option').remove();
                                $.each(data.subjectList, function (key, value) {
                                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                                });
                                $('#downloadStudentSubjectBtn').removeClass('btn-default').addClass('btn-primary');
                            } else {
                                $('#downloadStudentSubjectBtn').removeClass('btn-primary').addClass('btn-default');
                                showErrorMsg(data.message);
                            }
                            hideLoading("#subjectStudentHolder");
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                } else {
                    $('#downloadStudentSubjectBtn').removeClass('btn-primary').addClass('btn-default');
                }
            }
            $('#downloadStudentSubjectBtn').click(function (e) {
                e.preventDefault();
                className = $('#ssClassname').val();
                section = $('#ssSection').val();
                subject = $('#ssSubjectName').val();
                printOptionType = $('#ssPrintOptionType').val();
                reportParam ="${g.createLink(controller: 'examReport',action: 'subjectStudent','_blank')}?className="+className+"&section="+section+"&subjectName="+subject+"&printOptionType="+printOptionType;
                window.open(reportParam);
            });

            function loadptrmBtn() {
                section = $('#pterm-section').val();
                examTerm = $('#pterm-examTerm').val();
                if ((section != "") && (examTerm != "") ) {
                    $('#pterm-mark-entry-btn').removeClass('btn-default').addClass('btn-primary');
                }else {
                    $('#pterm-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
                }
            }
            $('#pterm-mark-entry-btn').click(function (e) {
                section = $('#pterm-section').val();
                examTerm = $('#pterm-examTerm').val();
                if ((section != "") && (examTerm != "")) {
                        window.location.href = '${g.createLink(controller: 'previousTerm',action: 'index')}?id=' + section+"&examTerm="+examTerm;
                }
                e.preventDefault();
            });


//         Special Mark Entry
            $('#massMarkEntryHolder').cascadingDropdown({
                selectBoxes: [
                    {
                        selector: '.me-exam-name-step',
                        onChange: function (value) {
                            loadMeExamClasses();
                        }
                    },
                    {
                        selector: '.me-class-name-step',
                        requires: ['.me-exam-name-step'],
                        onChange: function (value) {
                            loadMeSection();
                        }
                    },
                    {
                        selector: '.me-section-step',
                        requires: ['.me-class-name-step'],
                        onChange: function (value) {
                            loadMeSubject();
                        }
                    },
                    {
                        selector: '.me-exam-type-step',
                        requires: ['.me-section-step'],
                        onChange: function (value) {
                            loadMeSubject();
                        }
                    }
                ]
            });
            $('#meExamName').on('change', function (e) {
                $('#meClassName').val("").trigger("change");
            });
            $('#meClassName').on('change', function (e) {
                $('#meSection').val("").trigger("change");
            });
            $('#meSection').on('change', function (e) {
                $('#meExamType').val("").trigger("change");
            });

            $('#me-mark-entry-btn').click(function (e) {
                section = $('#meSection').val();
                examTerm = $('#meExamTerm').val();
                examType = $('#meExamType').val();
                subject = $('#meSubject').val();
                markObtain = $('#markObtain').val();
                if (subject != "" && markObtain != "" ) {
                    var confirmDel = confirm("Are you sure insert "+markObtain+" for all student for the selected subject");
                    if (confirmDel == true) {
                        showLoading("#massMarkEntryHolder");
                        jQuery.ajax({
                            type: 'POST',
                            dataType: 'JSON',
                            url: "${g.createLink(controller: 'examMark',action: 'specialMarkEntry')}?id=" + subject+"&examType="+examType+"&markObtain="+markObtain,
                            success: function (data, textStatus) {
                                if (data.isError == false) {
                                    showSuccessMsg(data.message);
                                } else {
                                    showErrorMsg(data.message);
                                }
                                hideLoading("#massMarkEntryHolder");
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                            }
                        });
                    }
                }
                e.preventDefault();
            });
        });
        function loadMeExamClasses(){
            examName =$('#meExamName').val();
            if (examName) {
                examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
                loadExamClass(examClassUrl, '#meClassName', "#massMarkEntryHolder")
            }
             else {
                $('#subject-holder').hide(500);
                $('#me-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }
        function loadMeSection(){

            examName =$('#meExamName').val();
            className =$('#meClassName').val();
            if(examName!="" && className!=""){
                examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=new&examName="+examName+"&className="+className;
                loadExamAsClassSectionList(examAsSectionListUrl, $('#meSection'),"#massMarkEntryHolder");
            }
             else {
                $('#subject-holder').hide(500);
                $('#me-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }
        function loadMeSubject() {
            section = $('#meSection').val();
            examName = $('#meExamName').val();
            examType = $('#meExamType').val();
            if (section != "" && examName != "" && examType != "") {
                showLoading("#massMarkEntryHolder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'remote',action: 'examSubjectList')}/"+examName+"?exam="+section +"&examType=" + examType,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $('#meSubject').select2("destroy");
                            var $select = $('#meSubject');
                            $select.find('option').remove();
                            $.each(data.scheduleList, function (key, value) {
                                $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $('#meSubject').select2().enable(true);
                            $('#me-subject-holder').show(500);
                            $('#me-mark-entry-btn').removeClass('btn-default').addClass('btn-primary');
                        } else {
                            $('#me-subject-holder').hide(500);
                            $('#me-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
                            showErrorMsg(data.message);
                        }
                        hideLoading("#massMarkEntryHolder");
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else {
                $('#me-subject-holder').hide(500);
                $('#me-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }


        function loadSection(className, sectionCtrl, loadingCtrl){
            showLoading(loadingCtrl);
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className,
                success: function (data, textStatus) {
                    hideLoading(loadingCtrl);
                    if (data.isError == false) {
                        var $select = $(sectionCtrl);
                        $select.find("option:gt(0)").remove();
                        $.each(data.sectionList,function(key, value)
                        {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }

        function loadSectionExam(examName, className, sectionCtrl, loadingCtrl){
            showLoading(loadingCtrl);
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'remote',action: 'sectionExamList')}?className="+className+"&examName="+examName,
                success: function (data, textStatus) {
                    hideLoading(loadingCtrl);
                    if (data.isError == false) {
                        var $select = $(sectionCtrl);
                        $select.find("option:gt(0)").remove();
                        $.each(data.sectionNameList,function(key, value)
                        {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                    $('#manageStumarkBtn').removeClass('btn-primary').addClass('btn-default');
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
    </script>
</body>
</html>