<html>
<head>
    <title>Feature Content</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Feature Content" SHOW_CREATE_BTN="YES" createButtonText="Add New Content"/>
<g:if test='${flash.message}'>
    <div class="errorHandler alert alert-danger">
        <i class="fa fa-remove-sign"></i>
        ${flash.message}
    </div>
</g:if>

<grailslab:fullModal label="Manage Designation">
    <grailslab:select enums="true" name="featureType" from="${openContentTypes}"></grailslab:select>
</grailslab:fullModal>
<grailslab:dataTable dataSet="${dataReturn}" tableHead="Order, Title, Icon, Status, Description" actions="fa fa-pencil-square-o,fa fa-retweet, fa fa-trash"></grailslab:dataTable>
<script>
    var featureType;
    jQuery(function ($) {
        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                featureType: {
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
                featureType = $('#featureType').val();
                window.location.href = "${g.createLink(controller: 'feature',action: 'create')}?featureType="+featureType;
            }
        });

        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 hrType-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'feature',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "openContentType", "value": $('#openContentType').val()});
            },
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
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.hrType-filter-holder').html('<select id="openContentType" class="form-control" name="openContentType"><g:each in="${openContentTypes}" var="openContent"><option value="${openContent.key}">${openContent.value}</option> </g:each></select>');
        $('#openContentType').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(true);
            hideLoading("#data-table-holder");
        });
        $('#featureType').on('change', function (e) {
            var featureType = $('#featureType').val();
            $('#openContentType').val(featureType);
            $('#openContentType').trigger("change");
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
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${g.createLink(controller: 'feature',action: 'edit')}?id="+referenceId;
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
                    url: "${g.createLink(controller: 'feature',action: 'inactive')}?id=" + referenceId,
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

        $('#list-table').on('click', 'a.reference-3', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure delete this content?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'feature',action: 'delete')}?id=" + referenceId,
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
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o,fa fa-retweet, fa fa-trash');
    }

</script>
</body>
</html>