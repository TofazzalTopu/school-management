<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Employee Attendance</title>
    <meta name="layout" content="moduleAttandanceLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Employee Daily Attendance" SHOW_CREATE_BTN="YES" createButtonText="Add Attendance" SHOW_EXTRA_BTN1="YES" extraBtn1Text="Update Late Attendance" />
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Employee Daily Attendance
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>SL</th>
                                <th>Emp ID</th>
                                <th>Name</th>
                                <th>Designation</th>
                                <th>In Time</th>
                                <th>Out Time</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>
                                    <span class="col-md-2 no-padding"><a href="javascript:void(0)"
                                                                         referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-calender-day"
                                                                         title="Edit Attendance"><span
                                                class="fa fa-edit"></span></a></span>

                                    <span class="col-md-2 no-padding"><a href="javascript:void(0)"
                                                                         referenceId="${dataSet.DT_RowId}"
                                                                         class="delete"
                                                                         title="delete"><span
                                                class="fa fa-trash"></span></a></span>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="modal fade" id="mainModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Manual Attendance Entry</h4>
            </div>
            <div class="mainModal-confirm publish-result-content">
                <form class="form-horizontal" role="form" id="createFormModal">
                    <g:hiddenField name="id" />
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <label for="recordDate" class="col-md-3 control-label">Date</label>
                                <div class="col-md-8">
                                    <input class="form-control" type="text"  name="recordDate" id="recordDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                           tabindex="2"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="employeeId" class="col-md-3 control-label">Employee Name</label>
                                <div class="col-md-8">
                                    <input type="hidden" class="form-control" id="employeeId" name="employeeId" tabindex="5" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inTime" class="col-md-3 control-label">In  Time</label>
                                <div class="col-md-8">
                                    <g:textField id="inTime" class="form-control" name="inTime" required="required"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="outTime" class="col-md-3 control-label">Out Time</label>
                                <div class="col-md-8">
                                    <g:textField id="outTime" class="form-control" name="outTime" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="reason" class="col-md-3 control-label">Comments</label>
                                <div class="col-md-8">
                                    <textarea id="reason" name="reason" class="form-control" placeholder="Add your Comments"
                                              tabindex="3"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" id="dismiss" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="publish-result-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    </div>
                    <div class="modal-footer modal-refresh-processing" style="display: none;">
                        <i class="fa fa-refresh fa-spin text-center"></i>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
%{--update late attendance modal --}%
<div class="modal fade" id="updateLateAttModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="updatelateAttModalLabel">Update Late Attendance Entry</h4>
            </div>
            <div class="mainModal-confirm publish-result-content">
                <form class="form-horizontal" role="form" id="updateLateAttModalForm">
                    <g:hiddenField name="id" />
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <label for="fromDate" class="col-md-3 control-label">From Time</label>
                                <div class="col-md-8">
                                    <input id="fromDate" class="input-sm form-control" type="text" value="" required="required"
                                           name="startDate"  placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                           tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="toDate" class="col-md-3 control-label">To Time</label>
                                <div class="col-md-8">
                                    <input id="toDate" class="input-sm form-control" type="text" value="" required="required"
                                           name="endDate"  placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                           tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="updateEmployeeId" class="col-md-3 control-label">Employee Name</label>
                                <div class="col-md-8">
                                    <input type="hidden" class="form-control" id="updateEmployeeId" name="employeeId" tabindex="5" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="hrCategory" class="col-md-3 control-label">All Category</label>
                                <div class="col-md-8">
                                    <g:select class="form-control " id="hrCategory" name='hrCategory' tabindex="2"
                                              noSelection="${['': 'All Catagory..']}"
                                              from='${hrCategoryList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" id="lateAttDismiss" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="submitBtn" class="btn btn-large btn-primary">Submit</button>
                    </div>
                    <div class="modal-footer modal-refresh-processing" style="display: none;">
                        <i class="fa fa-refresh fa-spin text-center"></i>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    var printParam, recordDate, hrCategory;
    jQuery(function ($) {
        var empAttnList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 date-holder'><'col-md-3 user-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'attnEmployee',action: 'list')}",

            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "attnRecordDate", "value": $('#attnRecordDate').val()},
                        {"name": "hrCategoryType", "value": $('#hrCategoryType').val()}
                );
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

        $('#recordDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate:'today',
            autoclose: true
        });
       $('#outTime, #inTime').timepicker({
            defaultTime: '',
        });

        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });
        $('#fromDate, #toDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate:'today',
            autoclose: true
        });
        $('.extra-btn-1').click(function (e) {
            $("#fromDate").datepicker('setDate', null);
            $("#toDate").datepicker('setDate', null);
            $('#hrCategory').val("");
            $('#updateLateAttModal .modal-footer-action-btns').show();
            $('#updateLateAttModal .modal-refresh-processing').hide();
            $('#updateLateAttModal').modal('show');
            e.preventDefault();
        });
        $("#updateLateAttModalForm").submit(function(e) {
            $('#updateLateAttModal .modal-footer-action-btns').hide();
            $('#updateLateAttModal .modal-refresh-processing').show();
            jQuery.ajax({
                type:'POST',
                dataType:'JSON',
                data: $("#updateLateAttModalForm").serialize(),
                url: "${g.createLink (controller: 'attnEmployee', action:'updateLateEntry')}",
                success: function(data){
                    if(data.isError == true){
                        showErrorMsg(data.message);
                    }else{
                        $('#updateLateAttModal').modal('hide');
                        empAttnList.draw(false);
                        showSuccessMsg(data.message);
                    }
                }
            });
            e.preventDefault();
        });


        $('#list-table_wrapper div.date-holder').html('<input id="attnRecordDate" class="form-control attnRecordDateField" placeholder="Enter Date" value="${lastRecord}" type="text" name="attnRecordDate">');
        $('#list-table_wrapper div.user-filter-holder').html('<select id="hrCategoryType" class="form-control" name="hrCategoryType"><option value="">All</option><g:each in="${hrCategoryList}" var="empType"><option value="${empType.id}">${empType.name}</option> </g:each></select>');
        $('#attnRecordDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('#attnRecordDate').change(function(e){
            showLoading("#data-table-holder");
            empAttnList.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#hrCategoryType').on('change', function (e) {
            showLoading("#data-table-holder");
            empAttnList.draw(false);
            hideLoading("#data-table-holder");
        });

        $('#employeeId, #updateEmployeeId').select2({
            placeholder: "Search for a Employee [employeeId/name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term // search term
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

        $('.create-new-btn').click(function (e) {
            $("#myModalLabel").html("Manual Attendance Entry");
            $("#publish-result-yes-btn").html("Submit");
            $("#id").val("");
            $("#recordDate").datepicker('setDate', null);
            $("#reason").val("");
            $("#recordDate").css("pointer-events","auto");
            $("#select2-chosen-1").empty().append("Search for a Employee [Name]");
            $("#s2id_employeeId").css("pointer-events","auto");

            $('#mainModal .modal-footer-action-btns').show();
            $('#mainModal .modal-refresh-processing').hide();
            $('#mainModal').modal('show');
            e.preventDefault();
        });
        $("#createFormModal").submit(function(e) {
            e.preventDefault();
            $('#mainModal .modal-footer-action-btns').hide();
            $('#mainModal .modal-refresh-processing').show();
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'attnEmployee', action: 'saveManualAttendance')}",
                success: function (data) {
                    if(data.isError=== false){
                        $('#mainModal').modal('hide');
                        empAttnList.draw(false);
                        showSuccessMsg(data.message);
                    }else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        });
        $('#list-table').on('click', 'a.edit-calender-day', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'attnEmployee',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    $("#myModalLabel").html("Update Attendance Entry");
                    $("#publish-result-yes-btn").html("Update");
                    $('#id').val(data.obj.id);
                    var attnDate=data.attnDate;
                    $('#recordDate').datepicker('setDate',new Date(attnDate)).css("pointer-events","none");
                    $("#select2-chosen-1").empty().append(data.employeeName);
                    $("#s2id_employeeId").css("pointer-events","none");
                    $('#reason').val(data.obj.remarks);
                    if (data.inTime) {
                        var d=  data.inTime;
                        $('#inTime').timepicker('setTime', d);
                    }
                    if (data.outTime) {
                        var dd=  data.outTime;
                        $('#outTime').timepicker('setTime', dd);
                    }
                    $('#mainModal .modal-footer-action-btns').show();
                    $('#mainModal .modal-refresh-processing').hide();
                    $('#mainModal').modal('show');
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });
        $('#list-table').on('click', 'a.delete', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'attnEmployee',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            empAttnList.draw(false);
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-calender-day" title="Edit Attendance">';
        actionButtons += '<span class="fa fa-edit"></span></a></span>';

        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete" title="delete">';
        actionButtons += '<span class="fa fa-trash"></span></a></span>';
        return actionButtons;
    }
</script>
</body>
</html>


