<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Manage Exam</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Exam & Schedule" SHOW_CREATE_BTN="YES" createButtonText="Create Exam"/>

<div class="modal fade" id="examCreateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Create Exam for Selected Classes</h4>
            </div>
            <div class="exam-create-confirm exam-create-content">
                <form class="form-horizontal" role="form" id="createFormModal">
                    <g:hiddenField name="id" id="id"/>
                    <g:hiddenField name="academicYear" id="academicYear" value="${workingYear.key}"/>
                    <div class="modal-body">
                        <div class="row">

                            <div class="form-group">
                                <label for="examName" class="col-md-3 control-label">Exam Name</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control" name="examName"  id="examName"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="ctExam" class="col-md-3 control-label">CT in Exam</label>
                                <div class="col-md-7">
                                    <select class="form-control" id="ctExam" name="ctExam">
                                        <option value="1">1 CT</option>
                                        <option value="2">2 CT</option>
                                        <option value="3">3 CT</option>
                                        <option value="">No CT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3  control-label">Exam Type<span class="required">*</span></label>
                                <div class="col-md-7">
                                    <g:select class="form-control" id="examTerm" name='examTerm' required="required"
                                              noSelection="${['': 'Select Exam Type...']}"
                                              from='${com.grailslab.enums.ExamTerm.type()}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                            </div>
                            <div class="form-group" id="tabu-mark-holder">
                                <label for="weightOnResult" class="col-md-4 control-label">Count in Final Result</label>
                                <div class="col-md-2">
                                    <input type="text" name="weightOnResult" id="weightOnResult" class="form-control">
                                </div>
                                <label class="col-md-1 control-label" style="text-align:left;padding-left:1px;">% </label>
                                <div class="col-md-3">
                                    <input type="text" id="resultPublishOn" tabindex="3" name="resultPublishOn" required="required" placeholder="Result Publish On" class="form-control" />
                                </div>
                            </div>

                            <div class="form-group" id="data_range">
                                <label for="weightOnResult" class="col-md-3 control-label">Exam Period</label>
                                <div class="col-md-7">
                                    <div class="input-daterange input-group" id="datepicker">

                                        <span class="input-group-addon">Start</span>
                                        <g:textField class="input-sm form-control" id="periodStart" name="periodStart"
                                                     tabindex="4" placeholder=" Period Start" required="required"/>
                                        <span class="input-group-addon">End</span>
                                        <g:textField class="input-sm form-control" id="periodEnd" name="periodEnd"
                                                     tabindex="5" placeholder=" Period End" required="required"/>
                                    </div>
                                </div>
                            </div>
                            <h3 style="text-align: center">Select Classes</h3>
                            <div class="form-group">
                                <div class="col-md-8 col-md-offset-2">
                                    <g:each in="${classNameList}">
                                        <label class="checkbox-inline">
                                            <input tabindex="6" type="checkbox" title="Class Name" name="classIds" value="${it.id}"
                                                   id="classId${it.id}" checked>${it.name}
                                        </label>
                                    </g:each>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="exam-create-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    </div>
                    <div class="modal-footer modal-refresh-processing" style="display: none;">
                        <i class="fa fa-refresh fa-spin text-center"></i>
                    </div>
                </form>
            </div>
            <div class="exam-create-success exam-create-content" style="display: none;">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-2">
                            <asset:image src="share-modal-icon.jpg" width="60" height="60"/>
                        </div>
                        <div class="col-md-10">
                            <p class="message-content"></p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btns">
                    <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row" id="datalist-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Exam List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th># CT</th>
                            <th>Mark Entry</th>
                            <th>Exam Period</th>
                            <th>Final Term Mark</th>
                            <th >Action</th>
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
                                <td>
                                    <span class="col-md-6 no-padding">
                                        <a href="<g:createLink controller="exam" action="classExams"
                                                                    params='[id: "${dataSet.DT_RowId}"]'/>"
                                                title="Manage Schedule">Class Schedule</a>
                                    </span>
                                    <span class="col-md-3 no-padding">
                                        <a href="" referenceId="${dataSet.DT_RowId}" class="lock-reference" title="Lock/Unlock Exam">
                                            <span class="glyphicon glyphicon-refresh"></span></a>
                                    </span>
                                    <span class="col-md-3 no-padding">
                                        <a href="" referenceId="${dataSet.DT_RowId}" class="edit-reference" title="Edit">
                                            <span class="glyphicon glyphicon-edit"></span></a>
                                    </span>
                                    <span class="col-md-3 no-padding">
                                        <a href="" referenceId="${dataSet.DT_RowId}" class="changeStatus-reference" title="Delete Exam">
                                            <span class="glyphicon glyphicon-trash"></span></a>
                                    </span>
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
<script>
    var shift,examTerm,examType,sections,className, groupName,resultPublishOn ,periodStart ,periodEnd ,classIds;
    jQuery(function ($) {
        $('#resultPublishOn').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('.create-new-btn').click(function (e) {
            $('#examCreateModal').modal('show');
            $("#examName").focus();
            e.preventDefault();

        });
        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $("#createFormModal").submit(function() {
            $('#examCreateModal .modal-footer-action-btns').hide();
            $('#examCreateModal .modal-refresh-processing').show();
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${createLink(controller: 'exam', action: 'save')}",
                success: function (data) {
                    $('#examCreateModal .modal-refresh-processing').hide();
                    $('#examCreateModal .modal-footer-action-btns').show();
                    if(data.isError==true){
                        $('div#examCreateModal p.message-content').html(data.message);
                    }else {
                        $('div#examCreateModal p.message-content').html(data.message);
                    }
                    $('#examCreateModal .exam-create-confirm').hide(1000);
                    $('#examCreateModal .exam-create-success').show(1000);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });

         $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'asc'],
            "sAjaxSource": "${g.createLink(controller: 'exam',action: 'list')}?resultType=${resultType}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(5)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push(
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()}
                );
            },
            "aoColumns": [
                null,
                null,
                null,
                { "bSortable": false },
                null,
                { "bSortable": false }
            ]
        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key == workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#filterAcademicYear').on('change', function (e) {
            showLoading('#list-table')
            $("#list-table").DataTable().draw(false);
            hideLoading("#list-table");
        });

        $(".cancel-btn").click(function () {
            $('#examCreateModal').modal('hide');
        });
        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'exam',action: 'editExams')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var resultPublishOn =data.obj.resultPublishOn;
                        var periodStart=data.obj.periodStart;
                        var periodEnd=data.obj.periodEnd;
                        classIds = data.obj.classIds;
                        clearForm('#createFormModal');
                        $('#id').val(data.obj.id);
                        $('#examName').val(data.obj.examName);
                        $('#ctExam').val(data.obj.ctExam);
                        $('#examTerm').val(data.obj.examTerm.name);
                        $('#weightOnResult').val(data.obj.weightOnResult);
                        $('#resultPublishOn').datepicker('setDate', new Date(resultPublishOn));
                        $('#periodStart').datepicker('setDate', new Date(periodStart));
                        $('#periodEnd').datepicker('setDate', new Date(periodEnd));

                        if (classIds) {
                            var classArrays = classIds.split(",");
                            $.each(classArrays,function(i){
                                $("#classId"+classArrays[i]).prop('checked',true);
                            });
                        }
                        $('#examCreateModal').modal('show');
                        $("#examName").focus();

                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.changeStatus-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure delete Exam?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'exam',action: 'deleteShiftExam')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
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
        $('#list-table').on('click', 'a.lock-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure want to Lock/Unlock Exam?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'exam',action: 'lockShiftExam')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().draw(false);
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

        $('#examCreateModal').on('hide.bs.modal', function (e) {
            if (e.target === this)
            {
                %{--window.location.href = "${g.createLink(controller: 'exam',action: 'index')}";--}%
                $("#list-table").DataTable().draw(false);
            }
        });

    });

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="${g.createLink(controller: "exam",action: 'classExams')}/' + aData.DT_RowId + '" title="Class Schedule">';
        actionButtons += 'Class Schedule';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="lock-reference" title="Lock/Unlock Exam">';
        actionButtons += '<span class="glyphicon glyphicon-refresh">';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href=" " referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit" >';
        actionButtons += '<span  class="glyphicon glyphicon-edit">';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="changeStatus-reference" title="Delete Exam">';
        actionButtons += '<span class="glyphicon glyphicon-trash">';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>