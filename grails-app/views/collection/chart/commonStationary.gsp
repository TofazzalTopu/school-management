<%@ page import="com.grailslab.enums.FeeType; com.grailslab.enums.FeeIterationType; com.grailslab.enums.DueOnType; com.grailslab.enums.FeeAppliedType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Common Stationary</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Common Stationary" SHOW_CREATE_BTN="YES"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Name & Code</th>
                            <th>Amount</th>
                            <th>Discount</th>
                            <th>Net Payable</th>
                            <th>Action</th>
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
                                        <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="add-reference" title="Add"><span
                                                    class="green glyphicon glyphicon-plus-sign"></span></a></span>
                                        <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="edit-reference" title="Edit"><span
                                                    class="green glyphicon glyphicon-edit"></span></a></span>
                                        <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
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
<!-- Modal -->
<div class="modal fade" id="stationaryAddModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField id="stationaryId" name="id"/>
                <g:hiddenField name="feeAppliedType" value="${FeeAppliedType.ALL_STD.key}"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Stationary</h4>
                </div>

                <div class="modal-body">
                    <div class="row">

                        <div class="form-group">
                            <label for="account" class="col-md-4 control-label">Account Head *</label>

                            <div class="col-md-7">
                                <g:select class="form-control" id="account" name='account' required="required"
                                          noSelection="${['': 'Select One...']}"
                                          from='${commonStationaryList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="account" class="col-md-4 control-label">Stationary Name *</label>

                            <div class="col-md-7">
                                <input type="text" class="form-control" id="name" name="name" required="required"
                                       placeholder="Insert Stationary Name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label">Code*</label>

                            <div class="col-md-7">
                                <input type="number" class="form-control" id="code" name="code" required="required"
                                       placeholder="Insert code"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="account" class="col-md-4 control-label">Description</label>

                            <div class="col-md-7">
                                <textarea class="form-control" id="description" name="description"
                                          placeholder="Insert Stationary Description"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="amount" class="col-md-3 control-label">Amount *</label>

                            <div class="col-md-4">
                                <input type="number" class="form-control" id="amount" name="amount" required="required"
                                       placeholder="Insert amount"/>
                            </div>

                            <label for="discount" class="col-md-3 control-label">Discount(%) *</label>

                            <div class="col-md-2">
                                <input type="number" class="form-control" id="discount" name="discount"
                                       required="required"
                                       value="0" placeholder="Insert Discount"/>
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Fee Type *</label>

                            <div class="col-md-7 radio">
                                <g:each in="${FeeType.values()}" var="fees">
                                    <label>
                                        <input id="${fees.key}" type="radio" name="feeType"
                                               value="${fees.key}">
                                        ${fees.value}
                                    </label>
                                </g:each>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Fee Due Type *</label>

                            <div class="col-md-7 radio">
                                <g:each in="${DueOnType.values()}" var="dues">
                                    <label>
                                        <input id="${dues.key}" type="radio" name="dueOnType"
                                               value="${dues.key}">
                                        ${dues.value}
                                    </label>
                                </g:each>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">Iteration Type *</label>

                            <div class="col-md-7 radio">
                                <g:each in="${FeeIterationType.values()}" var="feeIter">
                                    <label>
                                        <input id="${feeIter.key}" type="radio" name="iterationType"
                                               value="${feeIter.key}">
                                        ${feeIter.value}
                                    </label>
                                </g:each>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button class="btn  btn-default" id="cancelButton" data-dismiss="modal"
                            aria-hidden="true">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitButton">Save changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#className').select2();
        $('#account').select2();
        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "fnServerParams": function (aoData) {
                aoData.push({"name": "feeAppliedType", "value": "${FeeAppliedType.ALL_STD.key}"});
            },
            "sAjaxSource": "${g.createLink(controller: 'chart',action: 'stationaryList')}",
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
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $("#createFormModal").submit(function () {
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'chart', action: 'saveStationary')}",
                success: function (data) {
                    if (data.isError == false) {

                        if ($('#stationaryId').val() != '') {
                            $('#stationaryAddModal').modal('hide');
                        }
                        $('#name').val('');
                        $('#code').val('');
                        $('#description').val('');
                        $('#amount').val('');
                        $('#discount ').val('0');
                        var table = $('#list-table').DataTable().ajax.reload();
                        $('#stationaryId').val('');
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });


        $('.create-new-btn').click(function (e) {
            $('#createFormModal').resetForm();
            $('#' + '${FeeType.COMPULSORY.key}').prop('checked', true);
            $('#' + '${DueOnType.ADVANCE.key}').prop('checked', true);
            $('#' + '${FeeIterationType.RECEIVE.key}').prop('checked', true);
            $('#stationaryId').val('');
            $('#stationaryAddModal').modal('show');
            e.preventDefault();
        });

        $('#cancelButton').click(function (e) {
            $('#stationaryAddModal').modal('hide');
            $('#createFormModal').resetForm();
            $('#stationaryId').val('');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'chart',action: 'editStationary')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#stationaryId').val('');
                        $('#createFormModal').resetForm();
                        var feeType = data.obj.feeType.name;
                        var dueOnType = data.obj.dueOnType.name;
                        var iterationType = data.obj.iterationType.name;
                        $('#stationaryId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#code').val(data.obj.code);
                        $('#amount').val(data.obj.amount);
                        $('#discount').val(data.obj.discount);
                        $('#description').val(data.obj.description);
                        $('#' + feeType).prop('checked', true);
                        $('#' + dueOnType).prop('checked', true);
                        $('#' + iterationType).prop('checked', true);

                        $('#account').select2("destroy");
                        $('#account').val(data.obj.account.id);
                        $('#account').select2().enable(true);
                        $('#stationaryAddModal').modal('show');
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
                    url: "${g.createLink(controller: 'chart',action: 'deleteStationary')}?id=" + referenceId,
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

        $('#list-table').on('click', 'a.add-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.open("${g.createLink(controller: 'stock',action: 'index')}?id=" + referenceId);
            e.preventDefault();
        });
    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="add-reference" title="Add">';
        actionButtons += '<span class="green glyphicon glyphicon-plus-sign"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
