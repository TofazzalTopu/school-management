<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Asset</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Assets" SHOW_CREATE_BTN="YES"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-md-12">
        <div class="wrapper wrapper-content">
            <div class="ibox-content p-xl">
                <form class="form-horizontal" role="form" id="create-form">
                    <g:hiddenField name="id" id="assetId"/>

                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="col-md-4 control-label">Assets<span class="required">*</span></label>

                            <div class="col-md-6">
                                <g:textField class="form-control" id="name" name="name"
                                             placeholder="Insert Name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Select Account<span class="required">*</span></label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="account" name='account'
                                          tabindex="3"
                                          noSelection="${['': 'Select One...']}"
                                          from='${accountList}'
                                          optionKey="id" optionValue="nameWithCode"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Invoice Id</label>

                            <div class="col-md-6">
                                <input type="number" name="invoiceId"
                                       id="invoiceId" placeholder="Insert Invoice Id" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Reference Id</label>

                            <div class="col-md-6">
                                <input type="number" name="referenceId"
                                       id="referenceId" placeholder="Insert Reference Id" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Buy Date</label>

                            <div class="col-md-6">
                                <input type="text" name="buyDate"
                                       id="buyDate" placeholder="Insert Buy Date" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Cost</label>

                            <div class="col-md-6">
                                <input type="number" name="cost"
                                       id="cost" placeholder="Insert Cost" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert comment</label>

                            <div class="col-md-6">
                                <textarea id="comment" name="comment" class="form-control"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert depreciation</label>

                            <div class="col-md-6">
                                <input type="number" name="depreciation"
                                       id="depreciation" placeholder="Insert depreciation" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Last Depreciation</label>

                            <div class="col-md-6">
                                <input type="text" name="lastDepreciation"
                                       id="lastDepreciation" placeholder="Insert Last Depreciation" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Insert Current Value</label>

                            <div class="col-md-6">
                                <input type="number" name="currentValue"
                                       id="currentValue" placeholder="Insert Current Value" class="form-control">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary btn-submit" type="submit">Save</button>
                                <button class="btn btn-default cancel-btn" type="reset">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Account</th>
                            <th>Buy Date</th>
                            <th>Cost</th>
                            <th>Depreciation</th>
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
                                <td>
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="green glyphicon glyphicon-edit"></span></a></span>
                                        <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="delete-reference"
                                                                             title="Delete"><span
                                                    class="green glyphicon glyphicon-trash"></span></a></span>
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

    $('#buyDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    });
    $('#lastDepreciation').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    });

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
                account: {
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
                    url: "${createLink(controller: 'asset', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            $("#assetId").val('');
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
            "sAjaxSource": "${g.createLink(controller: 'asset',action: 'list')}",
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
            $("#assetId").val('');
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'asset',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#create-form');
                        var buyDate = data.obj.buyDate;
                        var lastDepreciation = data.obj.lastDepreciation;
                        $('#assetId').val(data.obj.id);
                        $('#account').val(data.obj.account.id);
                        $('#name').val(data.obj.name);
                        $('#invoiceId').val(data.obj.invoiceId);
                        $('#referenceId').val(data.obj.referenceId);
                        $('#buyDate').datepicker('setDate', new Date(buyDate));
                        $('#lastDepreciation').datepicker('setDate', new Date(lastDepreciation));
                        $("#cost").val(data.obj.cost);
                        $("#comment").val(data.obj.comment);
                        $("#depreciation").val(data.obj.depreciation);
                        $("#currentValue").val(data.obj.currentValue);
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

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'asset',action: 'changeStatus')}?id=" + referenceId,
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
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
