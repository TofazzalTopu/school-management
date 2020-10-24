<%@ page import="com.grailslab.enums.LeaveApproveStatus" %>
<html>
<head>
    <title>leave List</title>
    <meta name="layout" content="moduleLeaveManagementLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Leave Application" SHOW_CREATE_BTN="YES" createButtonText="Add New"/>
<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <div class="row">
                                <input type="hidden" name="id" id="id"/>
                                <div class="form-group">
                                    <label for="applyDate" class="col-md-3 control-label">Apply Date <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <input class="form-control" type="text" value="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"  required="true" name="applyDate"  id="applyDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                               tabindex="2"/>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label for="employee" class="col-md-3 control-label">Employee  <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <input type="hidden" class="form-control"  id="employee" name="employee" tabindex="2" />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="leaveName" class="col-md-3 control-label">Leave Name <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="leaveName" name='leaveName'
                                                  noSelection="${['': 'Select Leave...']}"
                                                  from='${leaveList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <label for="applyType" class="col-md-3 control-label">Apply Type <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="applyType" name='applyType'
                                                  from="${com.grailslab.enums.LeaveApplyType.values()}" enums="true"
                                                  optionKey="key" optionValue="value"></g:select>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="payType" class="col-md-3 control-label">Pay Type <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="payType" name='payType'
                                                  from='${com.grailslab.enums.LeavePayType.paidUnPaidLeave()}' optionKey="key" optionValue="value"
                                                  required="required"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="approveStatus" class="col-md-3 control-label">Approve Status  <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="approveStatus" name='approveStatus'
                                                  from="${com.grailslab.enums.LeaveApproveStatus.statusForAdminApply()}" enums="true"
                                                  optionKey="key" optionValue="value"></g:select>

                                    </div>
                                </div>

                                <div class="form-group">

                                    <label class="col-md-3 control-label">
                                        Date Range
                                        <span class="required">*</span>
                                    </label>

                                    <div class="col-md-7" style="">
                                        <div class="form-group" id="data_range" class="form-control">
                                            <div class="input-daterange input-group" style="margin-right: 13px; margin-left: 13px;">
                                                <span class="input-group-addon">On</span>
                                                <input type="text" class="input-sm form-control" id="fromDate"
                                                       name="fromDate" tabindex="2"
                                                       placeholder="Start Date" required="required" value=""/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="toDate"
                                                       name="toDate" tabindex="3"
                                                       placeholder="End Date" required="required" value=""/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="reason" class="col-md-3 control-label">Reason/Description</label>
                                    <div class="col-md-7">
                                        <textarea name="reason" id="reason" style="margin: 0px; width: 636px; height: 120px;"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="argentContactNo" class="col-md-3 control-label">Argent Contract</label>
                                    <div class="col-md-7">
                                        <input class="form-control" type="text" name="argentContactNo" id="argentContactNo"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="supportedBy" class="col-md-3 control-label">Supported By</label>
                                    <div class="col-md-7">
                                        <input type="hidden" class="form-control" id="supportedBy" name="supportedBy" tabindex="2" />
                                    </div>
                                </div>
                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default cancel-btn" tabindex="8"
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

<div class="row" id="approveLeaveApplicationHolder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="approveLeaveApplication">
                            <div class="row">
                                <input type="hidden" name="id" id="hiddenId"/>
                                <div class="form-group">
                                    <label for="applyDate" class="col-md-3 control-label">Apply Date <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <input class="form-control" type="text"  required="true" name="applyDate"  id="applyDateEdit" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                               tabindex="2"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="employeeId" class="col-md-3 control-label">Employee  <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <input type="hidden" class="form-control"  id="employeeId" name="employee" tabindex="2" />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="leaveName" class="col-md-3 control-label">Leave Name <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="leaveNameEdit" name='leaveName'
                                                  noSelection="${['': 'Select Leave...']}"
                                                  from='${leaveList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="applyType" class="col-md-3 control-label">Apply Type <span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="applyTypeEdit" name='applyType'
                                                  from="${com.grailslab.enums.LeaveApplyType.values()}" enums="true"
                                                  optionKey="key" optionValue="value"></g:select>

                                    </div>
                                </div>

                                <div class="form-group">

                                    <label class="col-md-3 control-label">
                                        Leave From
                                        <span class="required">*</span>
                                    </label>
                                    <div class="col-md-7" style="">
                                        <div class="form-group" id="data_range_approve" class="form-control">
                                            <div class="input-daterange input-group" id="datepicker" style="margin-right: 13px; margin-left: 13px;">
                                                <span class="input-group-addon">On</span>
                                                <input type="text" class="input-sm form-control" id="fromDateEdit"
                                                       name="fromDate" tabindex="2"
                                                       placeholder="Start Date" required="required" value=""/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="toDateEdit"
                                                       name="toDate" tabindex="3"
                                                       placeholder="End Date" required="required" value=""/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="numOfDays" class="col-md-3 control-label">Number of Days</label>
                                    <div class="col-md-7">
                                        <input class="form-control" type="text"  name="numOfDays"  id="numOfDays" tabindex="2"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="reasonEdit" class="col-md-3 control-label">Reason/Description</label>
                                    <div class="col-md-7">
                                        <textarea name="reason" id="reasonEdit" style="margin: 0px; width: 636px; height: 120px;"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="payTypeEdit" class="col-md-3 control-label">Pay Type <span class="required">*</span></label>
                                    <div class="col-md-6">
                                        <g:select class="form-control" id="payTypeEdit" name='payType'
                                                  from='${com.grailslab.enums.LeavePayType.paidUnPaidLeave()}' optionKey="key" optionValue="value"
                                                  required="required"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="remarks" class="col-md-3 control-label">Comment/Feedback</label>
                                    <div class="col-md-7">
                                        <textarea name="remarks" id="remarks" style="margin: 0px; width: 636px; height: 120px;"></textarea>
                                    </div>
                                </div>
                                <div class="form-group pull-right">
                                    <input type="reset" class="btn btn-default cancel-btn-edit" value="Cancel">
                                    <button class="btn btn-danger" tabindex="7" id="rejectBtn">Reject</button>
                                    <button class="btn btn-primary" tabindex="8" id="approveBtn">Approve</button>
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
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Employee</th>
                            <th>Leave Name</th>
                            <th>Date</th>
                            <th># Days</th>
                            <th>Reason/Description</th>
                            <th>Status</th>
                            <th>Applied On</th>
                            <th class="col-md-1">Action</th>
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

<script>
    jQuery(function ($) {
        var leaveSetupTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'leaveApproval', action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                var applyStatus = aData.DT_Status;
                if (applyStatus == "${LeaveApproveStatus.Approved.key}") {
                    $('td:eq(8)', nRow).html(getGridActionBtns(nRow, aData, ', print, ,'));
                }
                if (applyStatus == "${LeaveApproveStatus.Applied.key}") {
                    $('td:eq(8)', nRow).html(getGridActionBtns(nRow, aData, 'reply, print,delete,'));
                }
                if (applyStatus == "${LeaveApproveStatus.Rejected.key}") {

                    $('td:eq(8)', nRow).html(getGridActionBtns(nRow, aData, 'print, ,'));
                }
                if (applyStatus == "${LeaveApproveStatus.Draft.key}") {

                    $('td:eq(8)', nRow).html(getGridActionBtns(nRow, aData, 'edit, print, delete,'));
                }
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "approveStatus", "value": $('#typeFilter').val()});
            },

            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false},
                {"bSortable": false}
            ]
        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="typeFilter" class="form-control" name="typeFilter"><g:each in="${com.grailslab.enums.LeaveApproveStatus.values()}" var="type"><option value="${type.key}" ${type.equals(LeaveApproveStatus.Applied) ? "Selected" : ""}>${type.value}</option> </g:each></select>');

        $('#typeFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            leaveSetupTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('#employee').select2({
            placeholder: "Search for a Employee [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
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

       $('#employeeId').select2({
            placeholder: "Search for [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
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

        $('#supportedBy').select2({
            placeholder: "Supported By",
            allowClear: true,
            minimumInputLength:3,
            ajax: {
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
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

        $(".cancel-btn-edit").click(function () {
            var validator = $("#approveLeaveApplication").validate();
            validator.resetForm();
            $("#hiddenId").val('');
            $("#approveLeaveApplicationHolder").hide(500);
        });

        $('#toDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#fromDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $('#applyDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $('#toDateEdit').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#fromDateEdit').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });


        $('#applyDateEdit').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
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
                url: "${createLink(controller: 'leaveApproval', action: 'saveApplication')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#create-form"));
                        showSuccessMsg(data.message);
                        leaveSetupTable.draw(false);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            return false;
        });
        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $("#employee").val("")
            $("#argentContactNo").val("")
            $("#reason").val("")
            $("#select2-chosen-1").empty().append("Search for a Employee [Name]");
            $("#select2-chosen-3").empty().append("Search for a Employee [Name]");
            $("#s2id_employee").css("pointer-events","auto");
            e.preventDefault();
        });

        $('#approveBtn').on('click', function (e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'leaveApproval', action: 'saveApplication')}?approveType=Approve",
                type: 'post',
                dataType: "json",
                data: $("#approveLeaveApplication").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#approveLeaveApplication"));
                        leaveSetupTable.draw(false);
                        $("#approveLeaveApplicationHolder").hide(500);
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            return false;
        });
        $('#rejectBtn').on('click', function (e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'leaveApproval', action: 'saveApplication')}?approveType=reject",
                type: 'post',
                dataType: "json",
                data: $("#approveLeaveApplication").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#approveLeaveApplication"));
                        $("#approveLeaveApplicationHolder").hide(500);
                        leaveSetupTable.draw(false);
                        showSuccessMsg(data.message);
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
             var control = this;
             var referenceId = $(control).attr('referenceId');
                 jQuery.ajax({
                     type: 'POST',
                     dataType: 'JSON',
                     url: "${g.createLink(controller: 'leaveApproval',action: 'edit')}?id=" + referenceId,
                     success: function (data, textStatus) {
                         if (data.isError == false) {
                             var fromDate = data.obj.fromDate;
                             var toDate = data.obj.toDate;

                             var applyDate;
                             if (data.obj.applyDate) {
                                 applyDate = data.obj.applyDate;
                             } else {
                                 applyDate = data.obj.fromDate;
                             }

                             $("#form-submit-btn").html("Update");
                             $("#id").val(data.obj.id);
                             $("#employee").val(data.obj.employee.id);
                             $("#supportedBy").val(data.obj.supportedBy);

                             $('#argentContactNo').val(data.obj.argentContactNo);
                             $('#reason').val(data.obj.reason);
                             $('#leaveName').val(data.obj.leaveName.id);
                             $('#applyType').val(data.obj.applyType.name);
                             $('#approveStatus').val(data.obj.approveStatus.name);
                             $('#payType').val(data.obj.payType.name);
                             $('#fromDate').datepicker('setDate', new Date(fromDate));
                             $('#toDate').datepicker('setDate', new Date(toDate));
                             $('#applyDate').datepicker('setDate', new Date(applyDate));
                             $("#select2-chosen-1").empty().append(data.employee);
                             $("#select2-chosen-2").empty().append(data.supportedBy);

                             $("#s2id_employee").css("pointer-events","none");
                             $("#s2id_supportedBy").css("pointer-events","none");
                             $("#create-form-holder").show(500);
                         } else {
                            showErrorMsg(data.message);
                         }
                     },
                 error: function (XMLHttpRequest, textStatus, errorThrown) {
                 }
                 });
             e.preventDefault();

         });

         $('#list-table').on('click', 'a.reply-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'leaveApproval',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var fromDate = data.obj.fromDate;
                        var toDate = data.obj.toDate;

                        var applyDate;
                        if (data.obj.applyDate) {
                            applyDate = data.obj.applyDate;
                        } else {
                            applyDate = data.obj.fromDate;
                        }

                        $("#hiddenId").val(data.obj.id);
                        $("#employeeId").val(data.obj.employee.id);
                        $('#reasonEdit').val(data.obj.reason);
                        $('#numOfDays').val(data.obj.numberOfDay).css("pointer-events","none");
                        $('#leaveNameEdit').val(data.obj.leaveName.id);
                        $('#applyTypeEdit').val(data.obj.applyType.name);
                        $('#payTypeEdit').val(data.obj.payType.name);
                        $('#fromDateEdit').datepicker('setDate', new Date(fromDate));
                        $('#toDateEdit').datepicker('setDate', new Date(toDate));
                        $('#applyDateEdit').datepicker('setDate', new Date(applyDate));
                        $("#select2-chosen-2").empty().append(data.employee);
                        $("#s2id_employeeId").css("pointer-events","none");
                        $("#approveLeaveApplicationHolder").show(500);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();

        });

        $('#list-table').on('click', 'a.print-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var url = "${g.createLink(controller: 'leaveApproval',action: 'addLeaveApplication')}?id=" + referenceId;
            window.open(url);
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var confirmStr = "Are you want to delete this  ." +
                "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'leaveApproval',action: 'inactive')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                leaveSetupTable.draw(false);
                                showSuccessMsg(data.message);
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
        })
    });


</script>


</body>
</html>