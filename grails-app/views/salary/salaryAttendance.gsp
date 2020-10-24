
<html>
<head>
    <title>Salary Attendance</title>
    <meta name="layout" content="moduleHRLayout"/>

    <style>
    .form-group {
        margin-bottom: 5px;
    }
    </style>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Salary Attendance" SHOW_CREATE_BTN="YES"
                             createButtonText="New Salary Attendance" SHOW_LINK_BTN="YES" linkBtnText="Load Attendance" SHOW_PRINT_BTN="YES" />

<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
    <section class="panel">
        <div class="panel-body">
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <form class="cmxform form-horizontal" id="create-form">
                        <g:hiddenField name="id"/>
                        <div class="row">
                            <div class="form-group">
                                <div class="col-md-4">
                                    <grailslab:select name="academicYear"  enums="true" label="Year"
                                                      from="${academicYearList}"></grailslab:select>
                                </div>
                                <div class="col-md-4">
                                    <grailslab:select name="yearMonths"  label="Month" enums="true"
                                                      from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                </div>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="workingDays" id="workingDays" placeholder="workingDays"
                                           tabindex="2"/>
                                </div>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="holidays" id="holidays" placeholder="holidays"
                                           tabindex="2"/>
                                </div>
                            </div>
                        <div class="form-group">

                                <label for="employee" class="col-md-1 control-label">Employee</label>
                                <div class="col-md-7">
                                    <input type="hidden" class="form-control" id="employee" name="employee" tabindex="2" />
                                </div>

                        </div>

                        <div class="form-group">
                            <label for="presentDays" class="col-md-1 control-label">Present</label>
                            <div class="col-md-2">
                                <input class="form-control" type="text" name="presentDays" id="presentDays"
                                       tabindex="2"/>
                            </div>

                            <label for="lateDays" class="col-md-1 control-label">Late</label>
                            <div class="col-md-2">
                                <input class="form-control" type="text" name="lateDays" id="lateDays"
                                       tabindex="2"/>
                            </div>

                            <label for="absentDays" class="col-md-1 control-label">Absent</label>
                            <div class="col-md-2">
                                <input class="form-control" type="text" name="absentDays" id="absentDays"
                                       tabindex="2"/>
                            </div>

                            <label for="leaveDays" class="col-md-1 control-label">Leave</label>
                            <div class="col-md-2">
                                <input class="form-control" type="text" name="leaveDays" id="leaveDays"
                                       tabindex="2"/>
                            </div>
                        </div>
                            <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default cancel-btn" tabindex="8"
                                            type="reset">Cancel</button>
                            </div>

                        </div>
                </div>

            </form>
            </div>
        </div>
    </section>

    </div>
</div>

<div class="row" id="load-emp-attendance-holder" style="display: none;">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="form-horizontal" role="form" id="empAttendanceLoadForm">
                            <div class="form-group">
                                <div class="col-md-3">
                                    <grailslab:select name="academicYear"  enums="true" label="Year"
                                                      from="${academicYearList}"></grailslab:select>
                                </div>
                                <div class="col-md-3">
                                    <grailslab:select name="yearMonths"  label="Month" enums="true"
                                                      from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                </div>
                                <div class="col-md-4">
                                    <button class="btn btn-primary" tabindex="7" id="yearmonthBtn" type="submit">Load</button>
                                    <button class="btn btn-default" tabindex="8" id="load-emp-attn-cancel"
                                            type="reset">Cancel</button>
                                </div>
                            </div>
                        </form>
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
                Monthly Attendance List
                <span class="tools pull-right">
                <a href="javascript:void(0)" class="btn btn-danger btn-sm" id="attnDeletebtn" style="color: white" title="delete all">
                    <span class="glyphicon glyphicon-trash"></span> Delete all
                </a>
                </span>
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-1">SL NO</th>
                            <th class="col-md-1">ID</th>
                            <th class="col-md-1">EMP ID</th>
                            <th class="col-md-2">Name</th>
                            <th class="col-md-1" STYLE="white-space: nowrap">Designation</th>
                            <th class="col-md-1">WDay</th>
                            <th class="col-md-1">Holidays</th>
                            <th class="col-md-1">Present</th>
                            <th class="col-md-1">Late</th>
                            <th class="col-md-1">Absent</th>
                            <th class="col-md-1">Leave</th>
                            <th class="col-md-1">Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var salAttnTable;
    jQuery(function ($) {

        salAttnTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salaryAttendance',action: 'list')}",

            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(10)', nRow).html(getGridActionBtns(nRow, aData, 'edit, delete, print'));
                return nRow;
            },

            "fnServerParams": function (aoData) {
                aoData.push(
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()},
                        {"name": "yearMonths", "value": $('#filterYearMonths').val()}
                );
                },
            "aoColumns": [
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center", "targets" : 1, "visible": false, "searchable": false},
                null,
                null,
                null,
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false}
            ]
        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.month-filter-holder').html('<select id="filterYearMonths" class="form-control" name="filterYearMonths"><g:each in="${com.grailslab.enums.YearMonths.values()}" var="yearMonths"><option value="${yearMonths.key}">${yearMonths.value}</option></g:each></select>')

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            salAttnTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#filterYearMonths').on('change', function (e) {
            showLoading("#data-table-holder");
            salAttnTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('.create-new-btn').click(function (e) {

            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $('#academicYear').attr('readonly', false);
            $("#academicYear").css("pointer-events","auto");
            $('#yearMonths').attr('readonly', false);
            $("#yearMonths").css("pointer-events","auto");
            $("#academicYear").val("")
            $("#yearMonths").val("")
            $("#workingDays").val("")
            $("#holidays").val("")

            $("#presentDays").val("")
            $("#lateDays").val("")
            $("#absentDays").val("")
            $("#leaveDays").val("")
            $("#select2-chosen-1").empty().append("Search for a Employee [employeeId/name/designation]");
            $("#s2id_employee").css("pointer-events","auto");
            $("#name").focus();
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
            $("#create-form-holder").hide(500);
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'salaryAttendance', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#create-form"));
                        salAttnTable.draw(false);
                        showSuccessMsg(data.message);
                        $("#create-form-holder").hide(500);

                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            return false;
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            $("#create-form-holder").hide(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salaryAttendance',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $("#form-submit-btn").html("Edit");
                        $("#id").val(data.obj.id)
                        $('#academicYear').val(data.obj.academicYear.name)
                        $('#yearMonths').val(data.obj.yearMonths.name)
                        $('#academicYear').attr('readonly', true);
                        $("#academicYear").css("pointer-events","none");
                        $('#yearMonths').attr('readonly', true);
                        $("#yearMonths").css("pointer-events","none");
                        $("#employee").val(data.obj.employee.id)
                        $("#select2-chosen-1").empty().append(data.employeeName);
                        $("#s2id_employee").css("pointer-events","none");

                        $("#workingDays").val(data.obj.workingDays)
                        $("#holidays").val(data.obj.holidays)
                        $("#presentDays").val(data.obj.presentDays)
                        $("#lateDays").val(data.obj.lateDays)
                        $("#absentDays").val(data.obj.absentDays)
                        $("#leaveDays").val(data.obj.leaveDays)
                        $("#create-form-holder").show(500);
                        $("#name").focus();

                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();

        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var confirmStr = "Are you want to delete this  ." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'salaryAttendance',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                salAttnTable.draw(false);
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                }
            });
            e.preventDefault();
        });


        $('#list-table').on('click', 'a.print-reference', function (e) {
            e.preventDefault();
            var tr = $(this).closest('tr');
            var row = salAttnTable.row(tr);
            var employee = row.data()[1];
            var year = $('#filterAcademicYear').val();
            var month = $('#filterYearMonths').val();
            var reportParam = "${g.createLink(controller: 'attendanceReport',action: 'employeeAttnIndividualReport','_blank')}?year=" + year + "&month=" + month + "&employee=" + employee + "&attndanceStatus=all&printOptionType=PDF" + "&rStartDate=01/01/2019&rEndDate=01/01/2019";
            window.open(reportParam);
            return false;
        });


        $('#employee').select2({
            placeholder: "Search for a Employee [employeeId/name/designation]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        var arr = [ "workingDays","holidays","presentDays","lateDays","absentDays","leaveDays"];
        $.each( arr, function( i, val ) {
            var ids= "#"+val
            $(ids).numeric();
        });

        $('.link-url-btn').on('click', function(){
            $("#load-emp-attendance-holder").toggle(500);
        });
        $("#load-emp-attn-cancel").click(function () {
            $("#load-emp-attendance-holder").hide(500);
        });
        $("#empAttendanceLoadForm").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'salaryAttendance', action: 'loadAttendance')}",
                type: 'post',
                dataType: "json",
                data: $("#empAttendanceLoadForm").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#empAttendanceLoadForm"));
                        salAttnTable.draw(false);
                        showSuccessMsg(data.message);
                        $("#load-emp-attendance-holder").hide(500);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            return false;
        });

        $('.print-btn').click(function (e) {
            e.preventDefault();
            var  academicYear = $('#filterAcademicYear').find("option:selected").val();
            var  yearMonths = $('#filterYearMonths').find("option:selected").val();
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'attendance','_blank')}?academicYear="+academicYear+"&yearMonths="+yearMonths;
            window.open(printParam);
            return false;
        });
        $("#attnDeletebtn").click(function(){
            if (salAttnTable.data().count() ) {
                var confirmStr = "Are you want to delete all  ." +
                        "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
                bootbox.confirm(confirmStr, function(clickAction) {
                 if(clickAction){
                    $.ajax({
                        url: "${createLink(controller: 'salaryAttendance', action: 'attnDeleteAll')}",
                        type: 'post',
                        dataType: "json",
                        data: {academicYear:$('#filterAcademicYear').val(), yearMonths:$('#filterYearMonths').val()},
                        success: function (data) {
                            if (data.isError == false) {
                                salAttnTable.draw();
                                showSuccessMsg(data.message);

                            } else {

                                showErrorMsg(data.message);
                            }

                        },
                        failure: function (data) {
                        }
                    })}
                })

            }else{
                bootbox.alert("Data is not available");
            }
        })
    });
</script>
</body>
</html>