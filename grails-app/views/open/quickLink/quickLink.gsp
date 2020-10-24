<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Quick Link</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Quick Link" SHOW_CREATE_BTN="YES" createButtonText="Add New Link"/>
<grailslab:fullModal modalLabel="Manage Quick Link">
    <grailslab:input name="displayName" label="Display Name" required="true"></grailslab:input>
    <grailslab:select enums="true" name="urlType" label="Link Type" from="${quickLinkType}"></grailslab:select>
    <grailslab:input name="linkUrl" label="Link Url" required="true"></grailslab:input>
    <grailslab:input name="iconClass" label="Icon Class" value="icon-external-link" required="true"></grailslab:input>
    <grailslab:input type="number" name="sortIndex" label="Sort Index" value="1" required="true"></grailslab:input>
</grailslab:fullModal>
<grailslab:dataTable dataSet="${dataReturn}"
                     tableHead="SL, Display Name, Link Type, Link To, Sort Index, Icon Class, Status" actions="fa fa-pencil-square-o, fa fa-retweet"></grailslab:dataTable>

<script>
    jQuery(function ($) {
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
                    url: "${createLink(controller: 'quickLink', action: 'save')}",
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
            "sAjaxSource": "${g.createLink(controller: 'quickLink',action: 'list')}",
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

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'quickLink',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.obj.id);
                        $('#displayName').val(data.obj.displayName);
                        $('#urlType').val(data.obj.urlType);
                        $('#sortIndex').val(data.obj.sortIndex);
                        $('#linkUrl').val(data.obj.linkUrl);
                        $('#iconClass').val(data.obj.iconClass);
                        $('#myModal .create-success').hide();
                        $('#myModal .create-content').show();
                        $('#myModal').modal('show');
                    } else {
                        alert(data.message);
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
                    url: "${g.createLink(controller: 'quickLink',action: 'inactive')}?id=" + referenceId,
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