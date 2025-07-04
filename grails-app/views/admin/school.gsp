<%@ page import="com.grailslab.settings.School" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create School</title>
    <meta name="layout" content="adminLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Class Room" SHOW_CREATE_BTN="YES" createButtonText="Add School"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Add School
            </header>
            <div class="panel-body">
                <form class="cmxform form-horizontal" id="create-form">
                    <g:hiddenField name="id" id="schoolId"/>

                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="control-label col-md-4">School Name</label><span
                                class="required">*</span>

                            <div class="col-md-7">
                                <g:textField class="form-control" id="name" tabindex="1" name="name"
                                             placeholder="Enter School Name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="address" class="control-label col-md-4">Address</label>

                            <div class="col-md-7">
                                <g:textField class="form-control" id="address" tabindex="1" name="address"
                                             placeholder="Enter School Address"/>
                            </div>
                        </div>
                    <div class="form-group">
                        <label for="email" class="control-label col-md-4">Email</label>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="email" tabindex="1" name="email"
                                         placeholder="Enter Email"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="contactNo" class="control-label col-md-4">Contact</label>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="contactNo" tabindex="1" name="contactNo"
                                         placeholder="Enter Contact"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="academicYear" class="control-label col-md-4">Academic Year</label>

                        <div class="col-md-7">
                            <g:select class=" form-control mesAcademicYearStep" id="academicYear" name='academicYear'
                                      noSelection="${['': 'Select Year...']}"
                                      from='${academicYearList}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="nonMasking" class="control-label col-md-4">NonMasking</label>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="nonMasking" tabindex="1" name="nonMasking"
                                         placeholder="Enter NonMasking number"/>
                        </div>
                    </div>
                    </div>

                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary" tabindex="3" type="submit">Save</button>
                                <button class="btn btn-default cancel-btn" tabindex="4"
                                        type="reset">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Class Room List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-1">Serial</th>
                            <th class="col-md-2">Name</th>
                            <th class="col-md-2">Address</th>
                            <th class="col-md-2">Email</th>
                            <th class="col-md-2">Contact No</th>
                            <th class="col-md-1">Academic Year</th>
                            <th class="col-md-1">NOnMasking</th>
                            <th class="col-md-1">Action</th>
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
                                    <sec:access controller="classRoom" action="edit">
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="green glyphicon glyphicon-edit"></span></a></span>
                                    </sec:access>
                                    <sec:access controller="classRoom" action="delete">
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
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

<script>
    jQuery(function ($) {

        var oTable1 = $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'school',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true
                }
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
                    url: "${createLink(controller: 'school', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
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
        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(1000);
            $("#name").focus();
            e.preventDefault();
        });

        $(".cancel-btn").click(function(){
            var validator = $( "#create-form" ).validate();
            validator.resetForm();
            $("#schoolId").val('');
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'school',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#create-form');
                        $('#schoolId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#address').val(data.obj.address);
                        $('#email').val(data.obj.email);
                        $('#contactNo').val(data.obj.contactNo);
                        if (data.obj.academicYear){
                            $('#academicYear').val(data.obj.academicYear.name);
                        }
                        $('#nonMasking').val(data.obj.nonMasking);
                        $("#create-form-holder").show(1000);
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
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'school',action: 'inactive')}?id=" + referenceId,
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
    function getActionButtons(nRow, aData) {
        return getEditDeleteBtn(nRow, aData);
    }


</script>

</body>
</html>


