<%@ page import="com.grailslab.enums.AttendanceType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>School Attendance</title>
    <meta name="layout" content="moduleAttandanceLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="School Attendance" SHOW_CREATE_BTN="YES" createButtonText="Add Calender Day"/>

<!-- modal start -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Attendance update</h4>
            </div>
            <div class="updateModal-confirm publish-result-content">
                <form class="form-horizontal" role="form" id="updateForm">
                    <g:hiddenField name="id" />
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <label for="attendanceDate" class="col-md-3 control-label">Attendance date</label>
                                <div class="col-md-8">
                                    <g:textField id="attendanceDate" class="form-control" name="attendanceDate" readonly="readonly"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="dayType" class="col-md-3 control-label">Day type</label>
                                <div class="col-md-8">
                                    <g:select class="form-control" id="dayType" name='dayType'
                                              from='${com.grailslab.enums.AttendanceType.schoolDayTypes()}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="workingDay" class="col-md-3 control-label">Working day</label>
                                <div class="col-md-8">
                                    <textarea id="workingDay" name="workingDay" class="form-control"  placeholder="Add your Comments"
                                              tabindex="3"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" id="dismiss" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="updateSubmit" class="btn btn-large btn-primary">Update</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<grailslab:fullModal modalLabel="Add Calender Day for Attendance">
    <div class="col-md-12">
        <form class="form-horizontal" role="form" id="create-form">
            <div id="data_range" class="form-group">
                <div id="datepicker" class="input-daterange input-group">
                    <span class="input-group-addon">On</span>
                    <input id="startDate" class="input-sm form-control" type="text" value="${g.formatDate(format:"dd/MM/yyyy", date:lastRecordDate.plus(1))}" required="required"
                           placeholder="Start Date" tabindex="2" name="startDate">
                    <span class="input-group-addon">to</span>
                    <input id="endDate" class="input-sm form-control" type="text" value="${g.formatDate(format:"dd/MM/yyyy", date:lastRecordDate.plus(16))}" required="required"
                           placeholder="End Date" tabindex="3" name="endDate">
                </div>
            </div>
        </form>
    </div>
</grailslab:fullModal>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                School Calender Days
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Day Type</th>
                            <th>Working Day</th>
                            <th>Last Upload</th>
                            <th>Upload Count</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>



<script>
    jQuery(function ($) {
        var recordList = $('#list-table').DataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'recordDay',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(5)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                {"bSortable": false},
                null,
                null,
                {"bSortable": false}
            ]
        });

        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
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
//                $('#myModal .create-content .modal-footer-action-btn').hide();
//                $('#myModal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'recordDay', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        $('#myModal').modal('hide');
                        recordList.draw(false);
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('.create-new-btn').click(function (e) {
            validator.resetForm();
            $('#myModal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#modalForm").validate();
            validator.resetForm();
            $('#myModal').modal('hide');
        });

       $('#list-table').on('click', 'a.edit-calender-day', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
           jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'recordDay',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#id').val(data.obj.id);
                        $('#updateModal').resetForm();
                        $(".form-group").removeClass("has-erreturnDateror");
                        $('#attendanceDate').datepicker('setDate', new Date(data.attendanceDate)).css("pointer-events","none");
                        $('#dayType').val(data.obj.dayType.name);
                        $('#workingDay').val(data.obj.holidayName);
                        $('#updateModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });


        var updateFormValidator = $('#updateForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
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
                $.ajax({
                    url: "${createLink(controller: 'recordDay', action: 'update')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#updateForm").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            recordList.draw(false);
                            $('#updateModal').modal('hide');
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });



        $('#list-table').on('click', 'a.visit-emp-attendance', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var url = "${g.createLink(controller: 'attnEmployee',action: 'index')}?id=" + referenceId;
            window.open(url);
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.visit-std-attendance', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var url = "${g.createLink(controller: 'attnStudent',action: 'index')}?id=" + referenceId;
            window.open(url);
            e.preventDefault();
        });
    });
    $('#data_range .input-daterange').datepicker({
        keyboardNavigation: false,
        format: 'dd/mm/yyyy',
        forceParse: false,
        autoclose: true
    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-calender-day"  title="Edit Calender Day">';
        actionButtons += '<span class="fa fa-edit"></span></a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="visit-std-attendance" title="Student Attendance">';
        actionButtons += '<span class="fa fa-graduation-cap"></span></a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="visit-emp-attendance" title="Employee Attendance">';
        actionButtons += '<span class="fa fa-user"></span></a></span>';
        return actionButtons;
    }
</script>
</body>
</html>


