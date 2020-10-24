<!DOCTYPE html>
<html>
<head>
    <title>Lesson Plan</title>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Report"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Lesson Plan By Week
            </header>
            <div class="panel-body">
                <div class="row" id="holderNo1">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="ssClassname" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control ss-classname-step" id="ssClassname" name='ssClassname' tabindex="4"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="ssSection" id="ssSection" class="form-control ss-section-step" tabindex="3" >
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSubjectName" class="col-md-2 control-label">Subject Name</label>
                            <div class="col-md-6">
                                <select name="ssSubjectName" id="ssSubjectName" class="form-control ss-subject-step" tabindex="3">
                                    <option value="">All Subject</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSubjectName" class="col-md-2 control-label">Week No</label>
                            <div class="col-md-6">
                                <g:select tabindex="2" class="form-control" id="ssWeekNo"
                                          name='ssWeekNo' value=""
                                          noSelection="${['': 'Working Week(s)...']}"
                                          from='${lessonWeekList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssPrintOptionType" class="col-md-2 control-label">Print</label>
                            <div class="col-md-6">
                                <g:select class="form-control ss-print-type" id="ssPrintOptionType" name='ssPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="downloadBtnNo1"
                                        class="btn btn-default">Download</button>
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
                Lesson Plan By Date
            </header>
            <div class="panel-body">
                <div class="row" id="holderNo2">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="ddClassname" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control dd-classname-step" id="ddClassname" name='ddClassname' tabindex="4"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ddSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="ddSection" id="ddSection" class="form-control dd-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ddSubjectName" class="col-md-2 control-label">Subject Name</label>
                            <div class="col-md-6">
                                <select name="ddSubjectName" id="ddSubjectName" class="form-control dd-subject-step" tabindex="3">
                                    <option value="">All Subject</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ddFromDate" class="col-md-2 control-label">From Date</label>

                            <div class="col-md-2">
                                <input class="form-control col-md-12" type="text" name="ddFromDate"
                                       id="ddFromDate" value="" tabindex="4"/>
                            </div>

                            <label for="ddToDate" class="col-md-1 control-label">To Date</label>

                            <div class="col-md-2">
                                <input class="form-control col-md-12" type="text" name="ddToDate"
                                       id="ddToDate" value="" tabindex="5"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ddPrintOptionType" class="col-md-2 control-label">Print</label>
                            <div class="col-md-6">
                                <g:select class="form-control dd-print-type" id="ddPrintOptionType" name='ddPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="downloadBtnNo2"
                                        class="btn btn-default">Download</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var className, section, subject, weekNo, printOptionType, reportParam, fromDate, toDate, url;
    jQuery(function ($) {
        $('#ddFromDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+365d',
            autoclose: true
        });

        $('#ddToDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+365d',
            autoclose: true
        });

        $('#holderNo1').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.ss-classname-step',
                    onChange: function (value) {
                        loadStudentSubSection('ssClassname','ssSection','holderNo1');
                        loadClassSubject('holderNo1','downloadBtnNo1','ssClassname','ssSubjectName');
                    }
                },
                {
                    selector: '.ss-section-step',
                    requires: ['.ss-classname-step'],
                    onChange: function (value) {
                        loadSectionSubject('holderNo1','downloadBtnNo1','ssClassname','ssSection','ssSubjectName');
                    }
                }
            ]
        });

        function loadStudentSubSection(frmId, toId, holderId){
            className =$('#'+frmId).val();
            if (className) {
                loadSection(className, '#'+toId, "#"+holderId)
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

        function loadSectionSubject(holderId, btnId, classId, sectionId, subjectId) {
            section = $('#'+sectionId).val();
            if (section) {
                url= "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id=" + section
                showLoading("#"+holderId);
            } else {
                className = $('#'+classId).val();
                if(className){
                    url= "${g.createLink(controller: 'remote',action: 'classSubjectList')}?id=" + className
                    showLoading("#"+holderId);
                }
            }
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: url,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var $select = $('#'+subjectId);
                        $select.find('option').remove();
                        $select.append('<option value="">Select Subject</option>');
                        $.each(data.subjectList, function (key, value) {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                        $('#'+btnId).removeClass('btn-default').addClass('btn-primary');
                    } else {
                        $('#'+btnId).removeClass('btn-primary').addClass('btn-default');
                        showErrorMsg(data.message);
                    }
                    hideLoading("#"+holderId);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });

        }

        function loadClassSubject(holderId, btnId, classId, subjectId) {
            className = $('#'+classId).val();
            if (className) {
                showLoading("#"+holderId);
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'remote',action: 'classSubjectList')}?id=" + className,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            var $select = $('#'+subjectId);
                            $select.find('option').remove();
                            $select.append('<option value="">Select Subject</option>');
                            $.each(data.subjectList, function (key, value) {
                                $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $('#'+btnId).removeClass('btn-default').addClass('btn-primary');
                        } else {
                            $('#'+btnId).removeClass('btn-primary').addClass('btn-default');
                            showErrorMsg(data.message);
                        }
                        hideLoading("#"+holderId);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else {
                $('#'+btnId).removeClass('btn-primary').addClass('btn-default');
            }
        }



        $('#ssClassname').on('change', function (e) {
            $('#ssSection').val("").trigger("change");
            $('#ssSubjectName').val("").trigger("change");
        });

        $('#ssSection').on('change', function (e) {
            $('#ssSubjectName').val("").trigger("change");
        });

        $('#holderNo2').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.dd-classname-step',
                    onChange: function (value) {
                        loadStudentSubSection('ddClassname','ddSection','holderNo2');
                        loadClassSubject('holderNo2','downloadBtnNo2','ddClassname','ddSubjectName');
                    }
                },
                {
                    selector: '.dd-section-step',
                    requires: ['.dd-classname-step'],
                    onChange: function (value) {
                        loadSectionSubject('holderNo2','downloadBtnNo2','ddClassname','ddSection','ddSubjectName');
                    }
                }
            ]
        });

        $('#ddClassname').on('change', function (e) {
            $('#ddSection').val("").trigger("change");
        });

        $('#ddSection').on('change', function (e) {
            $('#ddSubjectName').val("").trigger("change");
        });

        $('#downloadBtnNo1').click(function (e) {
            e.preventDefault();
            className = $('#ssClassname').val();
            section = $('#ssSection').val();
            subject = $('#ssSubjectName').val();
            weekNo = $('#ssWeekNo').val();
            printOptionType = $('#ssPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'lessonPlan',action: 'download','_blank')}?className="+className+"&section="+section+"&subject="+subject+"&weekNo="+weekNo+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });

        $('#downloadBtnNo2').click(function (e) {
            e.preventDefault();
            className = $('#ddClassname').val();
            section = $('#ddSection').val();
            subject = $('#ddSubjectName').val();
            fromDate = $('#ddFromDate').val();
            toDate = $('#ddToDate').val();
            printOptionType = $('#ddPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'lessonPlan',action: 'download','_blank')}?className="+className+"&section="+section+"&subject="+subject+"&startDate="+fromDate+"&endDate="+toDate+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });

    });

</script>
</body>
</html>