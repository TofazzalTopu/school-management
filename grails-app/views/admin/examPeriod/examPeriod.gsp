<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Exam Period</title>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Exam Periods" SHOW_CREATE_BTN="YES" createButtonText="Add Exam Periods"/>


<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            %{--<header class="panel-heading">
            Heading goes here..
            <span class="tools pull-right">
                <a href="javascript:;" class="fa fa-chevron-down"></a>
                <a href="javascript:;" class="fa fa-cog"></a>
                <a href="javascript:;" class="fa fa-times"></a>
            </span>
        </header>--}%
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Exam Type</th>
                            <th>Name</th>
                            <th>Duration</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet">
                            <tr>
                                <td>${dataSet?.examType.value}</td>
                                <td>${dataSet?.name}</td>
                                <td>${dataSet?.duration} Minute(s)</td>
                                <td>
                                    <sec:access controller="examPeriod" action="edit">
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.id}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="green glyphicon glyphicon-edit"></span></a></span>
                                    </sec:access>
                                    <sec:access controller="examPeriod" action="delete">
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.id}"
                                                                             class="delete-reference"
                                                                             title="Delete"><span
                                                    class="green glyphicon glyphicon-trash"></span></a></span>
                                    </sec:access>
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
<div class="modal fade" id="examPeriodModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="id"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Exam Duration</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="examType" class="col-md-4 control-label">Exam Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="examType" name='examType' tabindex="3"
                                          noSelection="${['': 'Select Exam Type...']}"
                                          from='${com.grailslab.enums.ExamType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
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
                                <input type="number" value="40" min="20" max="180" step="10" name="duration" id="duration" class="form-control" required>
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
            "aaSorting": [0,'asc']
        });

        $("#createFormModal").submit(function() {
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'examPeriod', action: 'save')}",
                success: function (data) {
                    if(data.isError==true){
                        showErrorMsg(data.message);
                    }else {
                        showSuccessMsg(data.message);
                        window.location.href = "${g.createLink(controller: 'examPeriod',action: 'index')}";
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });



        $('.create-new-btn').click(function (e) {
            $('#examPeriodModal').modal('show');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'examPeriod',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#createFormModal');
                        $('#id').val(data.obj.id);
                        $('#examType').val(data.obj.examType.name);
                        $('#startOn').val(data.obj.startOn);
                        $('#duration').val(data.obj.duration);
                        $('#examPeriodModal').modal('show');
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
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'examPeriod',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'examPeriod',action: 'index')}";
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
        $('#examPeriodModal').on('hide.bs.modal', function (e) {
            window.location.href = "${g.createLink(controller: 'examPeriod',action: 'index')}";
        });
    });


</script>
</body>
</html>
