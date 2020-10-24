<%@ page import="com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Attendance Mark Entry</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Attendance Entry" SHOW_CREATE_BTN="YES" createButtonText="Get Attendance" SHOW_EXTRA_BTN1='${section?"YES":"NO"}' linkBtnText="Working Days"  SHOW_LINK_BTN="YES" extraBtn1Text="Change Section" SHOW_PRINT_BTN="YES" />
<g:if test="${flash.message}">
    <div class="alert alert-success">
        <h4 style="color: sienna">${flash.message}</h4>
    </div>
</g:if>
<div class="col-sm-12">
    <div class="panel"  id="create-attn-print" style="display: none;">
        <header class="panel-heading">
            <span class="panel-header-info">Print Attendance </span>
        </header>
        <div class="panel-body">
            <div class="form-horizontal" role="form">
                <div class="col-md-12" id="stu-manage-report-holder">
                    <div class="form-group">
                        <div class="col-md-3">
                            <g:select class=" form-control " id="printExamName" name='printExamName'
                                      tabindex="1"
                                      noSelection="${['': 'Select Exam...']}"
                                      from='${examNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-3">
                            <select name="printClassName"  id="printClassName" class="form-control" tabindex="3">
                                <option value="">Select Class...</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <select name="printSection"  id="printSection" class="form-control" tabindex="3">
                                <option value="">Select Section...</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <div class="btn-group btn-margin-left">
                                <a class="btn btn-primary print-btn-new" href="javascript:void(0);">
                                    <i class="fa fa fa-print"></i> View Report
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel"  id="get-attn-total" style="display: none;">
        <header class="panel-heading">
            <span class="panel-header-info">Update Attendance from Daily Attendance</span>
        </header>
        <div class="panel-body">
            <div class="form-horizontal" role="form">
                <div class="col-md-12" id="get-attendance-holder">
                    <div class="form-group">
                        <div class="col-md-4">
                            <g:select class=" form-control " id="getAttendanceExamName" name='getAttendanceExamName'
                                      tabindex="1"
                                      noSelection="${['': 'Select Exam...']}"
                                      from='${examNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-4">
                            <select name="getAttendanceClassName"  id="getAttendanceClassName" class="form-control" tabindex="3">
                                <option value="">Select Class...</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <select name="getAttendanceSection"  id="getAttendanceSection" class="form-control" tabindex="3">
                                <option value="">Select Section...</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-4">
                            <g:textField class="form-control datepicker" name="fromDate"
                                         tabindex="4" placeholder="From Date" required="required"/>
                        </div>

                        <div class="col-md-4">
                            <g:textField class="form-control datepicker" name="toDate"
                                         tabindex="4" placeholder="To Date" required="required"/>
                        </div>

                        <div class="col-md-4">
                            <div class="btn-group btn-margin-left">
                                <a class="btn btn-primary" id="get-attn-btn" href="javascript:void(0);">
                                    <i class="fa fa fa-print"></i> Update Attendance
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<g:if test="${section}">
    <div class="row" id="create-form-holder" style="display: none;">
</g:if>
<g:else>
    <div class="row" id="create-form-holder">
</g:else>
<div class="col-sm-12">
    <div class="panel">
        <header class="panel-heading">
            <span class="panel-header-info">Attendance Entry</span>
        </header>
        <div class="panel-body">
            <div class="form-horizontal" role="form">
                <div class="col-md-12" id="stu-print-attn-holder">
                    <div class="form-group">
                        <div class="col-md-3">
                            <g:select class=" form-control " id="examName" name='examName'
                                      tabindex="1"
                                      noSelection="${['': 'Select Exam...']}"
                                      from='${examNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-3">
                            <g:select class=" form-control " id="className" name='className'
                                      tabindex="2"
                                      noSelection="${['': 'Select Class...']}"
                                      from='${classNameList}' value="${exam?.className?.id}"
                                      optionKey="id" optionValue="name"></g:select>

                        </div>
                        <div class="col-md-3">
                            <g:select class=" form-control " id="section" name='section'
                                      tabindex="2"
                                      noSelection="${['': 'Select Section...']}"
                                      from='${sectionNameList}' value="${exam?.id}"
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<g:if test="${section}">
    <div class="row" id="attendance-entry-holder">
        <div class="col-sm-12">
            <section class="panel">
                <header class="panel-heading">
                    <span class="panel-header-title">Class : </span> <span class="panel-header-info">${className},</span> <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section?.name},</span><span class="panel-header-title"> Total Working Days : </span><span class="panel-header-info">${workingDays}</span>
                </header>
                <div class="panel-body">
                    <form class="form-horizontal" id="create-form">
                        <g:hiddenField name="id" id="hiddenId"/>
                        <g:hiddenField name="section" id="attnSection" value="${section?.id}"/>
                        <g:hiddenField name="examId" id="examId" value="${examId}"/>
                        <div class="form-group col-md-6">
                            <div class="col-md-12">
                                <g:select class="form-control" id="student" name='student' tabindex="1"
                                          from='${studentList}'
                                          optionKey="id" optionValue="name" required=""></g:select>
                            </div>
                        </div>
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="text" name="attendDay" class="form-control" tabindex="3" id="attendDay"/>
                            </div>
                        </div>
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <button id="hall-mark-submit-btn" class="btn btn-primary" tabindex="4" type="submit">Add Attendance</button>
                            </div>
                        </div>
                    </form>
                </div>
            </section>
            <section class="panel">
                <div class="panel-body">
                    <grailslab:dataTable dataSet="${dataReturn}"
                                         tableHead="STD-ID, Student Name, Roll, Term 1 Attendance, Term 2 Attendance, Final Attendance, Total Attendance" actions="fa fa-pencil-square-o"></grailslab:dataTable>
                </div>
            </section>
        </div>
    </div>
</g:if>



<script>

    $('.datepicker').datepicker({
        keyboardNavigation: false,
        format: 'dd/mm/yyyy',
        forceParse: false,
        autoclose: true
    });

    var shiftExam, className, section, examName, printOptionType, printParam, printSection, printExamName, printClassName;
        $('#student').select2();
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                attendDay: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 1200
                }

            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                showLoading("#create-form");
                $.ajax({
                    url: "${createLink(controller: 'previousTerm', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == true) {
                            showErrorMsg(data.message);
                            $('#attendDay').val('');
                        } else {
                            $("#student option:selected").remove();
                            $('#student').select2("destroy");
                            $('#student').select2().enable(true);
                            $('#list-table').DataTable().ajax.reload();
                            $('#attendDay').val('');
                            $('#id').val('');
                            $('#hiddenId').val('');
                        }
                        hideLoading("#create-form");
                    },
                    failure: function (data) {
                    }
                })
            }
        });
    $('.picKDate').datepicker({
        keyboardNavigation: false,
        todayBtn:true,
        format: 'dd/mm/yyyy',
        forceParse: false,
        autoclose: true
    });
        $('#examName').on('change', function (e) {
            examName =$('#examName').val();
            if (examName) {
                loadExamClass(examName, '#className', "#stu-manage-report-holder")
            }
            $('#className').val("").trigger("change");
        });
        $('#className').on('change', function (e) {
            examName =$('#examName').val();
            className = $('#className').val();
            if (className) {
                loadSectionExam(examName, className, '#section', "#stu-manage-report-holder")
            }
            $('#section').val("").trigger("change");
        });
        $('#section').on('change', function (e) {
            section =$('#section').val();
            if (section) {
                window.location.href = '${g.createLink(controller: 'previousTerm', action: 'attendance')}/' + section;
            }
        });


    $('#printExamName').on('change', function (e) {
        examName =$('#printExamName').val();
        if (examName) {
            loadExamClass(examName, '#printClassName', "#stu-print-attn-holder")
        }
        $('#printClassName').val("").trigger("change");
    });

    $('#printClassName').on('change', function (e) {
        examName =$('#printExamName').val();
        className = $('#printClassName').val();
        if (className) {
            loadSectionExam(examName, className, '#printSection', "#stu-print-attn-holder")
        }
        $('#printSection').val("").trigger("change");
    });

    $('.print-btn-new').click(function (e) {
        e.preventDefault();
        printSubject = $("#printSubject").val();
        var examId = $("#printSection").val();
        printOptionType = 'PDF';
        if (examId) {
            printParam = "${g.createLink(controller: 'previousTerm', action: 'attendanceReport','_blank')}/"+examId+"?printOptionType="+printOptionType;
            window.open(printParam);
        } else {
            alert("Please select subject to download report");
        }
        return false;
    });

    $('#getAttendanceExamName').on('change', function (e) {
        examName =$('#getAttendanceExamName').val();
        if (examName) {
            loadExamClass(examName, '#getAttendanceClassName', "#get-attendance-holder")
        }
        $('#getAttendanceClassName').val("").trigger("change");
    });

    $('#getAttendanceClassName').on('change', function (e) {
        examName =$('#getAttendanceExamName').val();
        className = $('#getAttendanceClassName').val();
        if (className) {
            loadSectionExam(examName, className, '#getAttendanceSection', "#get-attendance-holder")
        }
        $('#getAttendanceSection').val("").trigger("change");
    });


    $('#get-attn-btn').click(function (e) {
        e.preventDefault();
        examName =$('#getAttendanceExamName').val();
        className = $('#getAttendanceClassName').val();
        var sectionExamName = $('#getAttendanceSection').val();
        var fromDate = $("#fromDate").val(),
            toDate = $("#toDate").val();

        if (!examName || !className || !fromDate || !toDate) {
            alert("Please select exam, class, section, from date and to date to update Attendance");
        } else {
            showLoading("#get-attendance-holder");
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${createLink(controller: 'previousTerm', action: 'countAttendanceDays')}?className="+className+"&examName="+examName+"&sectionExam="+sectionExamName+"&fromDate="+fromDate+"&toDate="+toDate,
                success: function (data, textStatus) {
                    hideLoading("#get-attendance-holder");
                    if (data.isError == false) {
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }

        return false;
    });

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading":  ${totalCount?:0},
            "sServerMethod": "POST",
            "sAjaxSource": "${g.createLink(controller: 'previousTerm',action: 'listTermMark')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "id", "value": "${section?.id}"}, {"name": "examId", "value": "${examId}"});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'previousTerm',action: 'edit')}?id=" + referenceId+"&examId=${examId}",
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.preId);
                        $('#student').append('<option value="' + data.stdId + '" selected="selected">' + data.studentID + '</option>');
                        $('#student').select2("destroy");
                        $('#student').select2().enable(false);
                        $('#attendDay').val(data.obtainDay);
                        $('#myModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('.extra-btn-1').click(function (e) {
            $("#create-form-holder").toggle(500);
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            $("#link-url-btn").hide(500);
        });

        $('.link-url-btn').click(function (e) {
            window.location.href = "${g.createLink(controller: 'previousTerm',action: 'workDays', '_blank')}/";

        });


    $('.print-btn').click(function (e) {
        $("#create-attn-print").toggle(500);
        e.preventDefault();
    });
    $('.create-new-btn').click(function (e) {
        $("#get-attn-total").toggle(500);
        e.preventDefault();
    });

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
        function loadExamClass(examName, classCtrl, loadingCtrl){
            showLoading(loadingCtrl);
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName,
                success: function (data, textStatus) {
                    hideLoading(loadingCtrl);
                    if (data.isError == false) {
                        var $select = $(classCtrl);
                        $select.find("option:gt(0)").remove();
                        $.each(data.classNameList,function(key, value)
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
    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o');

    }

</script>
</body>
</html>