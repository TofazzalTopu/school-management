<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleHRLayout"/>
    <title>Hr Attendance Period</title>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Hr Attendance" SHOW_CREATE_BTN="YES"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Name</th>
                            <th>Duration</th>
                            <th>Late Tolerance</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${i+1}</td>
                                <td>${dataSet?.name}</td>
                                <td>${dataSet?.periodRange}</td>
                                <td>${dataSet?.lateTolerance} Minute(s)</td>
                                <td>
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.id}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="fa fa-pencil-square-o"></span></a></span>
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.id}"
                                                                             class="status-change-reference"
                                                                             title="Delete"><span
                                                    class="fa fa-retweet"></span></a></span>
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
<div class="modal fade" id="hrPeriodModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="id"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Attendance Duration</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="col-md-4 control-label">Name</label>
                            <div class="col-md-4">
                                <g:textField id="name" class="form-control" name="name" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="startOn" class="col-md-4 control-label">Start Time</label>
                            <div class="col-md-4">
                                <g:textField id="startOn" class="form-control timepicker" name="startOn" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="duration" class="col-md-4 control-label">Duration (Minute)</label>
                            <div class="col-md-4">
                                <input type="number" value="240" min="60" max="480" step="10" name="duration" id="duration" class="form-control" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lateTolerance" class="col-md-4 control-label">Late Tolerance (Minute)</label>
                            <div class="col-md-4">
                                <input type="number" value="30" min="0" max="60" step="5" name="lateTolerance" id="lateTolerance" class="form-control" required>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitButton">Save changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('.timepicker').timepicker({
            defaultTime: '08:00 AM'
        });
        var oTable1 = $('#list-table').dataTable({
            "iDisplayLength": 25,
            "aaSorting": [0,'desc']
        });

        $("#createFormModal").submit(function() {
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'hrPeriod', action: 'save')}",
                success: function (data) {
                    if(data.isError==true){
                        showErrorMsg(data.message);
                    }else {
                        showSuccessMsg(data.message);
                        window.location.href = "${g.createLink(controller: 'hrPeriod',action: 'index')}";
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });



        $('.create-new-btn').click(function (e) {
            $('#hrPeriodModal').modal('show');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'hrPeriod',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#createFormModal');
                        $('#id').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#startOn').val(data.obj.startOn);
                        $('#duration').val(data.obj.duration);
                        $('#lateTolerance').val(data.obj.lateTolerance);
                        $('#hrPeriodModal').modal('show');
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
                    url: "${g.createLink(controller: 'hrPeriod',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'hrPeriod',action: 'index')}";
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
        $('#hrPeriodModal').on('hide.bs.modal', function (e) {
            window.location.href = "${g.createLink(controller: 'hrPeriod',action: 'index')}";
        });
    });


</script>
</body>
</html>
