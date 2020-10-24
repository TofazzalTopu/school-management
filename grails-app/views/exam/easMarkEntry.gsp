<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>EAS Mark Entry</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="EAS Mark Deduction"/>
<g:if test="${flash.message}">
    <div class="alert alert-success">
        <h4 style="color: sienna">${flash.message}</h4>
    </div>
</g:if>

<g:if test="${section}">
    <div class="row" id="create-form-holder" style="display: none;">
</g:if>
<g:else>
    <div class="row" id="create-form-holder">
</g:else>
        <div class="col-sm-12">
            <div class="panel">
                <header class="panel-heading">
                    <span class="panel-header-info">EAS Mark Deduction</span>
                </header>
                <div class="panel-body">
                    <div class="form-horizontal" role="form">
                        <div class="col-md-12" id="stu-manage-report-holder">
                            <div class="form-group">
                                <div class="col-md-3">
                                    <g:select class=" form-control" id="className" name='className' tabindex="2"
                                              noSelection="${['': 'All Class...']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                                <div class="col-md-3">
                                    <select name="section"  id="section" class="form-control" tabindex="3">
                                        <option value="">Select Section...</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<g:if test="${section}">
    <div class="row" id="attendance-entry-holder">
        <div class="col-sm-12">
            <section class="panel">
                <header class="panel-heading">
                    <span class="panel-header-title">Class : </span> <span class="panel-header-info">${section.className.name},</span> <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section?.name}</span></span>
                </header>
                <div class="panel-body">
                    <form class="form-horizontal" id="create-form">
                        <g:hiddenField name="id" id="hiddenId"/>
                        <g:hiddenField name="section" id="attnSection" value="${section?.id}"/>
                        <div class="form-group">
                            <div class="col-md-4">
                                <g:select class="form-control" id="student" name='student' tabindex="1"
                                          from='${studentList}'
                                          optionKey="id" optionValue="name" required="required"></g:select>
                            </div>
                        <div class="col-md-4">
                            <g:select class="form-control" id="addedBy" name='addedBy' tabindex="1"
                                      from='${employeeList}'
                                      optionKey="id" optionValue="name" required="required"></g:select>
                        </div>
                        <div class="col-md-3">
                            <input class="form-control" type="text" name="entryDate"
                                   id="entryDate" value="<g:formatDate format="dd/MM/yyyy" date="${new Date()}"/>"
                                   tabindex="4"/>
                        </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-2">
                                <input type="text" name="mark" class="form-control" tabindex="3" id="mark" placeholder="EAS Mark"/>
                            </div>
                            <div class="col-md-6">
                                <input type="text" name="description" class="form-control" tabindex="3" id="description" placeholder="Reason/Description"/>
                            </div>
                            <div class="col-md-2">
                                <button id="hall-mark-submit-btn" class="btn btn-primary" tabindex="4" type="submit">Deduct EAS Mark</button>
                            </div>
                        </div>

                    </form>
                </div>
            </section>
            <section class="panel">
                <div class="panel-body">
                    <div class="table-responsive">
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                <tr>
                                    <th>Std-ID</th>
                                    <th>Student Name</th>
                                    <th>Roll</th>
                                    <th>EAS Mark</th>
                                    <th>Reason/Description</th>
                                    <th>Added By</th>
                                    <th>Added date</th>
                                    <th>Actions</th>
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
</g:if>

<script>
    var easEntryTable;
    jQuery(function ($) {
    $('#entryDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: '+90d',
        autoclose: true
    });
    easEntryTable = $('#list-table').dataTable({
        "bAutoWidth": true,
        "bServerSide": true,
        "iDisplayLength": 25,
        "aaSorting": [0, 'desc'],
        "sServerMethod": "POST",
        "sAjaxSource": "${g.createLink(controller: 'eas',action: 'easList')}",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "id", "value": "${section?.id}"});
        },
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
            {"bSortable": false}
        ]
      });

        $('#student').select2();
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                ctMark: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 1200
                },
                description: {
                    required: true
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
                showLoading("#create-form");
                $.ajax({
                    url: "${createLink(controller: 'eas', action: 'saveEas')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        hideLoading("#create-form");
                        if (data.isError == true) {
                            showErrorMsg(data.message);
                        } else {
                            $('#list-table').DataTable().ajax.reload();
                            $("#student option:selected").remove();
                            $('#student').select2("destroy");
                            $('#student').select2().enable(true);
                            $('#mark').val('');
                            $('#description').val('');
                            $('#id').val('');
                            $('#hiddenId').val('');
                            showSuccessMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#className').on('change', function (e) {
            className =$('#className').val();
            if (className) {
                clsSectionUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className;
                loadClassSection(clsSectionUrl, $('#section'), "#stu-manage-report-holder");
            }
            $('#section').val("").trigger("change");
        });

        $('#section').on('change', function (e) {
            section =$('#section').val();
            className =$('#className').val();
            if (section) {
                window.location.href = '${g.createLink(controller: 'eas',action: 'easEntry')}/' + section;
            }
        });
        $('#list-table').on('click', 'a.changeStatus-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure delete EAS entry?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'eas',action: 'delete')}?id=" + referenceId,
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



    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-2 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="changeStatus-reference" title="Delete Exam">';
        actionButtons += '<span class="glyphicon glyphicon-trash">';
        actionButtons += '</a></span>';
        return actionButtons;
    }
</script>
</body>
</html>