<%@ page import="com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Attendance Ct Mark Entry</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Attendance Entry" SHOW_PRINT_BTN="YES" SHOW_LINK_BTN='${section?"YES":"NO"}' linkBtnText="Input Another Subject"/>
<g:if test="${flash.message}">
    <div class="alert alert-success">
        <h4 style="color: sienna">${flash.message}</h4>
    </div>
</g:if>

<div class="row" id="printReportHolder" style="display: none">
    <div class="col-sm-12">
        <div class="panel">
            <header class="panel-heading">
                PRINT PREVIOUS CT MARK ENTRY
            </header>
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="stu-manage-report-print">
                        <div class="form-group">
                            <div class="col-md-3">
                                <g:select class=" form-control " id="printClassName" name='printClassName'
                                          tabindex="1"
                                          noSelection="${['': 'All Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                            <div class="col-md-3">
                                <select class=" form-control " id="printSection" name="printSection" tabindex="2">
                                    <option value="">Select Section...</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <select class=" form-control " id="printSubject" name="printSubject" tabindex="2">
                                    <option value="">Select Subject...</option>
                                </select>

                            </div>
                            <div class="btn-group btn-margin-left">
                                <a class="btn btn-primary print-btn-new" href="javascript:void(0);">
                                    <i class="fa fa fa-print"></i> View Report
                                </a>
                            </div>
                        </div>
                    </div>
                </form>
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
                    <span class="panel-header-info">Previous Ct Mark Entry </span>
                </header>
                <div class="panel-body">
                    <div class="form-horizontal" role="form">
                        <div class="col-md-12" id="stu-manage-report-holder">
                            <div class="form-group">
                                <div class="col-md-3">

                                    <g:select class=" form-control" id="className" name='className' tabindex="2"
                                              noSelection="${['': 'All Class...']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                                <div class="col-md-3">
                                    <select name="section"  id="section" class="form-control" tabindex="3">
                                        <option value="">Select Section...</option>
                                    </select>
                                </div>

                                <div class="col-md-3">
                                    <select name="subjectName" id="subjectName" class="form-control" tabindex="3">
                                        <option value="">Select Subject</option>
                                    </select>

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
                    <span class="panel-header-title">Class : </span> <span class="panel-header-info">${section.className.name},</span> <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section?.name},</span></span><span class="panel-header-title"> SUBJECT : </span><span class="panel-header-info">${subjectName?.name}</span>
                </header>
                <div class="panel-body">
                    <form class="form-horizontal" id="create-form">
                        <g:hiddenField name="id" id="hiddenId"/>
                        <g:hiddenField name="section" id="attnSection" value="${section?.id}"/>
                        <g:hiddenField name="subjectName" id="entrySubject" value="${subjectName?.id}"/>
                        <div class="form-group col-md-6">
                            <div class="col-md-12">
                                <g:select class="form-control" id="student" name='student' tabindex="1"
                                          from='${studentList}'
                                          optionKey="id" optionValue="name" required=""></g:select>
                            </div>
                        </div>
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="text" name="ctMark" class="form-control" tabindex="3" id="ctMark"/>
                            </div>
                        </div>
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <button id="hall-mark-submit-btn" class="btn btn-primary" tabindex="4" type="submit">Add Ct Mark</button>
                            </div>
                        </div>
                    </form>
                </div>
            </section>
            <section class="panel">
                <div class="panel-body">

                    <div class="table-responsive">
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                <tr>
                                    <th>Std-ID</th>
                                    <th>Student Name</th>
                                    <th>Roll</th>
                                    <th>Total Mark </th>
                                    <th>Edit</th>
                                </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>

                    </div>

                </div>
            </section>
        </div>
    </div>
</g:if>

<script>
    var ctEntryTable;
    jQuery(function ($) {
    ctEntryTable = $('#list-table').dataTable({
        "bAutoWidth": true,
        "bServerSide": true,
        "iDisplayLength": 25,
        "aaSorting": [0, 'desc'],
        "sServerMethod": "POST",
        "sAjaxSource": "${g.createLink(controller: 'previousTerm',action: 'ctListTermMark')}",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "id", "value": "${section?.id}"},
                    {"name": "subjectId", "value": "${subjectName?.id}"});
        },
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {
            if (aData.DT_RowId == undefined) {
                return true;
            }
            $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'editCt'));
            return nRow;
        },
        "aoColumns": [
            null,
            null,
            null,
            null,
            {"bSortable": false}
        ]
      });

        $('#student').select2();
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                ctMark: {
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
                    url: "${createLink(controller: 'previousTerm', action: 'saveCt')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == true) {
                            showErrorMsg(data.message);
                            ctEntryTable.draw(false);
                            $('#ctMark').val('');
                        } else {
                            $("#student option:selected").remove();
                            $('#student').select2("destroy");
                            $('#student').select2().enable(true);
                            $('#list-table').DataTable().ajax.reload();
                            $('#ctMark').val('');
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

        $('#className').on('change', function (e) {
            className =$('#className').val();
            if (className) {
                clsSectionUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className;
                loadClassSection(clsSectionUrl, $('#section'), "#stu-manage-report-holder");
            }
            $('#section').val("").trigger("change");
        });

        $('#section').on('change', function (e) {
            section =$('#section').val();
            className =$('#className').val();
            if (section) {
                classSubjectUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id=" + section+"&className="+className;
                loadSectionSubject(classSubjectUrl,className,section, $('#subjectName'), "#stu-manage-report-holder");
            }
            $('#subjectName').val("").trigger("change");
        });

        $('#subjectName').on('change', function (e) {
            section =$('#section').val();
            subjectName =$('#subjectName').val();
            if (subjectName) {
                window.location.href = '${g.createLink(controller: 'previousTerm',action: 'ctMark')}/' + section+"?subjectId="+subjectName;
            }
        });


        $('#printClassName').on('change', function (e) {
            printClassName =$('#printClassName').val();
            if (printClassName) {
                printClsSectionUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+printClassName;
                loadClassSection(printClsSectionUrl, $('#printSection'), "#stu-manage-report-holder");
            }
            $('#printSection').val("").trigger("change");
        });

        $('#printSection').on('change', function (e) {
            printSection =$('#printSection').val();
            printClassName =$('#printClassName').val();
            if (printSection) {
                printClassSubjectUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id=" + printSection+"&className="+printClassName;
                loadSectionSubject(printClassSubjectUrl,printClassName,printSection, $('#printSubject'), "#stu-manage-report-holder");
            }
            $('#printSubject').val("").trigger("change");
        });

        $('.print-btn-new').click(function (e) {
            e.preventDefault();
            printSubject = $("#printSubject").val();
            section = $("#printSection").val();
            printOptionType = 'PDF';
            if (printSubject) {
                printParam = "${g.createLink(controller: 'previousTerm', action: 'ctMarkReport','_blank')}/"+section+"?subjectName="+printSubject+"&printOptionType="+printOptionType;
                window.open(printParam);
            } else {
                alert("Please select subject to download report");
            }
            return false;
        });

        $('#list-table').on('click', 'a.editCt-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'previousTerm',action: 'editCt')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.preId);
                        $('#student').append('<option value="' + data.stdId + '" selected="selected">' + data.studentID + '</option>');
                        $('#student').select2("destroy");
                        $('#student').select2().enable(false);
                        $('#ctMark').val(data.obtainMark);
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

        $('.link-url-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            e.preventDefault();
        });

        $('.print-btn').click(function (e) {
            $("#printReportHolder").toggle(500);
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            $("#link-url-btn").hide(500);
        });

    });

</script>
</body>
</html>