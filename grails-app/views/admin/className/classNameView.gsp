<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Class Name</title>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Class Name" SHOW_CREATE_BTN="YES" createButtonText="Add Class Name"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <csection class="panel">
            <div class="panel-body">
                <form class="form-horizontal" role="form" id="create-form">
                    <g:hiddenField name="id" id="classId"/>

                    <div class="row">
                        <div class="form-group">
                            <label class="col-md-2 control-label">Class Name<span class="required">*</span></label>

                            <div class="col-md-4">
                                <g:textField class="form-control" id="name" tabindex="1" name="name"
                                             placeholder="Enter Class Name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">Sort Position<span class="required">*</span></label>

                            <div class="col-md-6">
                                <input tabindex="2" type="number" min="1" max="50" step="1" name="sortPosition"
                                       id="sortPosition" placeholder="Order show in List" class="form-control">
                            </div>
                        </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Class Hour<span class="required">*</span></label>
                        <div class="col-md-6">
                            <g:select name="hrPeriod" id="hrPeriod" placeholder="School Hour" class="form-control"
                                      noSelection="${['': 'Select One...']}"
                                      from="${officeHourList}"
                                      optionKey="id"
                                      optionValue="name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">Section Type<span class="required">*</span></label>
                        <div class="col-md-6">
                            <g:select name="sectionType" id="sectionType" placeholder="School Hour" class="form-control"
                                      noSelection="${['': 'Select One...']}"
                                      from="${['Primary', 'High', 'College']}"/>
                        </div>
                    </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">Expected Number</label>

                            <div class="col-md-3">
                                <input tabindex="3" type="number" name="expectedNumber" value="60"
                                       id="expectedNumber" placeholder="Expected Number" class="form-control">
                            </div>
                            <label class="col-md-2 control-label" style="text-align: left">% Per Subject</label>
                        </div>
                        <div class="form-group">
                            <div class="subject-holder col-md-10 col-md-offset-2">
                                <label class="checkbox-inline ">
                                    <input tabindex="11" type="checkbox" title="Groups Available" name="groupsAvailable"
                                           id="groupsAvailable">Allow Groups
                                </label>
                                <label class="checkbox-inline">
                                    <input tabindex="9" type="checkbox" title="Allow Optional Subjects"
                                           name="allowOptionalSubject"
                                           id="allowOptionalSubject">Allow Optional Subjects
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary btn-submit" tabindex="11" type="submit">Save</button>
                                <button class="btn btn-default cancel-btn" tabindex="12" type="reset">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </csection>
    </div>
</div>
<grailslab:dataTable dataSet="${dataReturn}" tableHead="Sort Position, Name, Others, Section Type, Expected Number, School Hour"
                     actions="fa fa-pencil-square-o, fa fa-trash"></grailslab:dataTable>

<script>
    jQuery(function ($) {
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                },
                sortPosition: {
                    required: true,
                    number: true
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
                $.ajax({
                    url: "${createLink(controller: 'className', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            $("#classId").val('');
                            clearForm(form);
                            var table = $('#list-table').DataTable().ajax.reload();
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
        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'className',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#name").focus();
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#classId").val('');
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'className',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#classId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#expectedNumber').val(data.obj.expectedNumber);
                        $('#sortPosition').val(data.obj.sortPosition);
                        if (data.obj.hrPeriod){
                            $('#hrPeriod').val(data.obj.hrPeriod.id);
                        }
                        $('#sectionType').val(data.obj.sectionType);
                        $('#workingDays').val(data.obj.workingDays);
                        $("#allowOptionalSubject").prop("checked", data.obj.allowOptionalSubject);
                        $("#groupsAvailable").prop("checked", data.obj.groupsAvailable);
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


        $('#list-table').on('click', 'a.reference-2', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'className',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $('#list-table').DataTable().ajax.reload();
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
        return getActionBtnCustom(nRow, aData, 'fa fa-pencil-square-o, fa fa-trash');
    }

</script>
</body>
</html>
