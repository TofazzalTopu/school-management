<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Manage Fee Collection Day</title>
    <meta name="layout" content="moduleCollectionLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Fee Collection Day" SHOW_CREATE_BTN="YES" createButtonText="Add Collection Day"/>
<grailslab:fullModal modalLabel="Manage Collection Day">
    <div class="col-md-12">
        <form class="form-inline" role="form" id="create-form">
            <div class="form-group" id="data_range">
                <div class="input-daterange input-group" id="datepicker">
                    <span class="input-group-addon">On</span>
                    <g:textField class="input-sm form-control" id="loadRecordDate" name="loadRecordDate"
                                 tabindex="2" placeholder="Start Date" required="required"/>
                </div>
            </div>
        </form>
    </div>
</grailslab:fullModal>
<grailslab:dataTable dataSet="${dataReturn}"
                     tableHead="SL, Invoice Date, Status, Created By, Last Updated, Updated By" actions="fa fa-pencil-square-o, fa fa-retweet"></grailslab:dataTable>

<script>
    jQuery(function ($) {
        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });
        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                }, sortOrder: {
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
                $('#myModal .create-content .modal-footer-action-btn').hide();
                $('#myModal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'invoiceDay', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        formSuccess(data);
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
            "sAjaxSource": "${g.createLink(controller: 'invoiceDay',action: 'list')}",
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
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $('.create-new-btn').click(function (e) {
            $('#myModal .create-success').hide();
            $('#myModal .create-content').show();
            validator.resetForm();
            $("#hiddenId").val('');
            $('#myModal').modal('show');
            e.preventDefault();
        });
        $(".cancel-btn").click(function () {
            var validator = $("#modalForm").validate();
            validator.resetForm();
            $("#hiddenId").val('');
            $('#myModal').modal('hide');
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
                    url: "${g.createLink(controller: 'invoiceDay',action: 'reOpen')}?id=" + referenceId,
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
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o, fa fa-retweet');
    }
</script>
</body>
</html>