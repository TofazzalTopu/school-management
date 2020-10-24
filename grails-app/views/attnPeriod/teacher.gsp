<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleAttandanceLayout"/>
    <title>Teacher Attendance Period</title>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Teacher Attendance" SHOW_CREATE_BTN="YES"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Type</th>
                            <th>Status</th>
                            <th>Day</th>
                            <th>Start</th>
                            <th>End</th>
                            <th>Late Tolerance</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]} Minute(s)</td>
                                <td>
                                    <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet['DT_RowId']}"
                                                                         class="edit-reference" title="Edit"><span
                                                class="fa fa-pencil-square-o"></span></a></span>
                                    <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet['DT_RowId']}"
                                                                         class="status-change-reference"
                                                                         title="Delete"><span
                                                class="fa fa-remove"></span></a></span>
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
<!-- Modal -->
<div class="modal fade" id="attnPeriodModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="id" id="attnPeriodId"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Attendance Duration</h4>
                </div>
                <div class="modal-body">
                    <g:hiddenField name="attnType" id="attnType" value="TEACHER"/>
                    <div class="row">
                        <div class="form-group">
                            <label for="attnTypeName" class="col-md-4 control-label">Type</label>
                            <div class="col-md-4">
                                <g:textField name="attnTypeName" id="attnTypeName" class="form-control" required=""/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dayName" class="col-md-4 control-label">Status</label>
                            <div class="col-md-4">
                                <g:select name="workingStatus" id="workingStatus" from="${["OPEN","CLOSE"]}" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="startTime" class="col-md-4 control-label">Start Time</label>
                            <div class="col-md-4">
                                <g:textField id="startTime" class="form-control timepicker" name="startTime" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="endTime" class="col-md-4 control-label">End Time</label>
                            <div class="col-md-4">
                                <g:textField id="endTime" class="form-control timepicker" name="endTime" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lateTolerance" class="col-md-4 control-label">Late Tolerance (Minute)</label>
                            <div class="col-md-4">
                                <input type="number" value="30" min="0" max="60" step="5" name="lateTolerance" id="lateTolerance" class="form-control" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">Days</label>
                            <div class="col-md-6">
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="SUN" name="dayName" id="sundayBox" />
                                    Sunday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="MON" name="dayName" id="mondayBox" />
                                    Monday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="TUE" name="dayName" id="tuesdayBox" />
                                    Tuesday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="WED" name="dayName" id="wednesdayBox" />
                                    Wednesday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="THU" name="dayName" id="thursdayBox" />
                                    Thursday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="FRI" name="dayName" id="fridayBox" />
                                    Friday
                                </label></span>
                                <span><label class="checkbox-inline">
                                    <g:checkBox value="SAT" name="dayName" id="saturdayBox" />
                                    Saturday
                                </label></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitButton">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#startTime.timepicker').timepicker({
            defaultTime: '08:00 AM'
        });
        $('#endTime.timepicker').timepicker({
            defaultTime: '04:00 PM'
        });
        var oTable1 = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 className-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'attnPeriod',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "attnType", "value": "TEACHER"},{"name": "attnTypeName", "value": $("#listAttnTypeName").val()});
            },
            "aoColumns": [
                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.className-filter-holder').html('<select id="listAttnTypeName" class="form-control" name="listAttnTypeName"><option value="ALL">ALL</option><g:each in="${teacherType}" var="type"><option value="${type.key}">${type.value}</option> </g:each></select>');

        $('#listAttnTypeName').on('change', function (e) {
            $("#list-table").DataTable().draw(false);
        });

        $("#createFormModal").submit(function() {
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'attnPeriod', action: 'save')}",
                success: function (data) {
                    if(data.isError==true){
                        showErrorMsg(data.message);
                    }else {
                        $('#attnPeriodModal').modal('hide');
                        $('#list-table').DataTable().ajax.reload();
                        showSuccessMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });



        $('.create-new-btn').click(function (e) {
            $('#attnPeriodModal').modal('show');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'attnPeriod',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#createFormModal');
                        $('#attnPeriodId').val(data.obj.id);
                        $('#attnType').val(data.obj.attnType);
                        $('#attnTypeName').val(data.obj.attnTypeName);
                        $('#workingStatus').val(data.obj.workingStatus);
                        $('#startTime').val(data.obj.startTime);
                        $('#endTime').val(data.obj.endTime);
                        $('#lateTolerance').val(data.obj.lateTolerance);
                        setSelectedDays(data.obj.dayName);
                        $('#attnPeriodModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.status-change-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'attnPeriod',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
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

    function setSelectedDays(dayNames) {
        if (dayNames.includes("SUN")) {
            $("#sundayBox").prop("checked", true);
        }
        if (dayNames.includes("MON")) {
            $("#mondayBox").prop("checked", true);
        }
        if (dayNames.includes("TUE")) {
            $("#tuesdayBox").prop("checked", true);
        }
        if (dayNames.includes("WED")) {
            $("#wednesdayBox").prop("checked", true);
        }
        if (dayNames.includes("THU")) {
            $("#thursdayBox").prop("checked", true);
        }
        if (dayNames.includes("FRI")) {
            $("#fridayBox").prop("checked", true);
        }
        if (dayNames.includes("SAT")) {
            $("#saturdayBox").prop("checked", true);
        }
    }

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="fa fa-pencil-square-o"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="status-change-reference" title="Delete">';
        actionButtons += '<span class="fa fa-remove"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
