<%@ page import="com.grailslab.settings.AcaYear" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Holiday</title>
    <meta name="layout" content="adminLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Academic Year" SHOW_CREATE_BTN="YES" createButtonText="Add Academic Year"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Add Academic Year
            </header>
            <div class="panel-body">
                <form class="cmxform form-horizontal" id="create-form">
                    <g:hiddenField name="id" id="acaYearId"/>

                    <div class="row">
                        <div class="form-group">
                            <label for="name" class="control-label col-md-4">Year </label><span
                                class="required">*</span>

                            <div class="col-md-8" id="size">
                                <select name="name" id="name" width="300" style="width: 300px;height: 40px" >
                                    <option value="">   -Choose Year    </option>
                                    <g:each in="${academicYearList}" var="academicYear">
                                        <option value="${academicYear.key}">${academicYear.value}</option>
                                    </g:each>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="yearType" class="control-label col-md-4"> Year Type</label>

                            <div class="col-md-7">
                                <select name="yearType" id="yearType" width="300"  style="width: 300px;height: 40px" >
                                    <option value="">     Choose Type Year   </option>
                                    <option value="School" >School</option>
                                    <option value="College" >College</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="sortOrder" class="control-label col-md-4"> Sort Order</label>

                            <div class="col-md-7">
                                <g:field type="number" id="sortOrder" name="sortOrder" min="1"/>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary btn-submit" tabindex="3" type="submit">Save</button>
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
                Aca Year List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-1">Serial</th>
                            <th class="col-md-2">Name</th>
                            <th class="col-md-2">Year Type</th>
                            <th class="col-md-2">Working Year</th>
                            <th class="col-md-2">Admission Year</th>
                            <th class="col-md-1">Order</th>
                            <th class="col-md-2">Action</th>
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
                                <td>
                                    <sec:access controller="acaYear" action="edit">
                                        <span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="green glyphicon glyphicon-edit"></span></a></span>
                                    </sec:access>
                                    <sec:access controller="acaYear" action="delete">
                                        <span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="delete-reference"
                                                                             title="Delete"><span
                                                    class="green glyphicon glyphicon-trash"></span></a></span>
                                    </sec:access>
                                    <sec:access controller="acaYear" action="updateWorkingYear">
                                        <span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="updateWorkingYear-reference"
                                                                             title="updateWorkingYear"><span
                                                    class="	glyphicon glyphicon-calendar"></span></a></span>
                                    </sec:access>

                                    <sec:access controller="acaYear" action="updateAdmissionYear">
                                        <span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="updateAdmissionYear-reference"
                                                                             title="updateAdmissionYear"><span
                                                    class="	glyphicon glyphicon-education"></span></a></span>
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
                var isSaveAction = $(".btn-submit").text(), action;
                if (isSaveAction == "Save") {
                    action = "save"
                } else {
                    action = "update"
                }
                $.ajax({
                    %{--url: "${createLink(controller: 'acaYear', action: '')}",--}%
                    url: action,
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

        var oTable1 = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 className-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'acaYear',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "acaYearType", "value": $('#acaYearType').val()});
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
        $('#list-table_wrapper div.className-filter-holder').html('<select id="acaYearType" class="form-control" name="acaYearType"><g:each in="${acaYearTypeList}" var="acaYearType"><option value="${acaYearType.value}" selected="${acaYearType.value == defaultYearType ? 'selected' : ''}">${acaYearType.value}</option> </g:each></select>');

        $('#acaYearType').on('change', function (e) {
            $("#list-table").DataTable().draw(false);
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(1000);
            $("#name").focus();
            $(".btn-submit").text("Save");
            e.preventDefault();
        });

        $(".cancel-btn").click(function(){
            var validator = $( "#create-form" ).validate();
            validator.resetForm();
            $("#acaYearId").val('');
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'acaYear',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#create-form');
                        $('#acaYearId').val(data.obj.id);
                        $('#name').val("Y"+data.obj.name);
                        $('#yearType').val(data.obj.yearType);
                        $('#sortOrder').val(data.obj.sortOrder);
                        $("#create-form-holder").show(1000);
                        $(".btn-submit").text("Update");
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
                    url: "${g.createLink(controller: 'acaYear',action: 'inactive')}?id=" + referenceId,
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

        $('#list-table').on('click', 'a.updateWorkingYear-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'acaYear',action: 'updateWorkingYear')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                           var table = $('#list-table').DataTable().ajax.reload();
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

        $('#list-table').on('click', 'a.updateAdmissionYear-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'acaYear',action: 'updateAdmissionYear')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            var table = $('#list-table').DataTable().ajax.reload();
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
        actionButtons += '<sec:access controller="acaYear" action="edit"><span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span></sec:access>';
        actionButtons += '<sec:access controller="acaYear" action="delete"><span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span></sec:access>';
        actionButtons += '<sec:access controller="acaYear" action="updateWorkingYear"><span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="updateWorkingYear-reference" title="updateWorkingYear">';
        actionButtons += '<span class="red glyphicon glyphicon-calendar"></span>';
        actionButtons += '</a></span></sec:access>';
        actionButtons += '<sec:access controller="acaYear" action="updateAdmissionYear"><span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="updateAdmissionYear-reference" title="updateAdmissionYear">';
        actionButtons += '<span class="red glyphicon glyphicon-education"></span>';
        actionButtons += '</a></span></sec:access>';
        return actionButtons;
    }

</script>

</body>
</html>


