<html>
<head>
    <title>Designation Entry</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Designation" SHOW_CREATE_BTN="YES" createButtonText="Add Designation"/>
<grailslab:fullModal label="Manage Designation">
    <grailslab:select name="hrCategory" from="${hrCategory}"></grailslab:select>
    <grailslab:input required="true" name="name"></grailslab:input>
    <grailslab:input type="number" required="true" name="sortOrder"></grailslab:input>
</grailslab:fullModal>
<grailslab:dataTable dataSet="${dataReturn}" tableHead="Sort Order,Name, Category" actions="fa fa-pencil-square-o,fa fa-retweet"></grailslab:dataTable>
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
                },
                hrCategory: {
                    required: true
                },
                sortOrder: {
                    required: true,
                    number: true
                },
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
                    url: "${createLink(controller: 'hrDesignation', action: 'save')}",
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
            "sDom": "<'row'<'col-md-6 hrType-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'hrDesignation',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "hrCategoryType", "value": $('#hrCategoryType').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(3)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.hrType-filter-holder').html('<select id="hrCategoryType" class="form-control" name="hrCategoryType"><g:each in="${hrCategory}" var="empType"><option value="${empType.id}">${empType.name}</option> </g:each></select>');
        $('#hrCategoryType').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(true);
            hideLoading("#data-table-holder");
        });
        $('#hrCategory').on('change', function (e) {
         var hrCategory = $('#hrCategory').val();
         $('#hrCategoryType').val(hrCategory);
         $('#hrCategoryType').trigger("change");
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
                url: "${g.createLink(controller: 'hrDesignation',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#hrCategory').val(data.obj.hrCategory ? data.obj.hrCategory.id : '');
                        $('#sortOrder').val(data.obj.sortOrder);
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
                    url: "${g.createLink(controller: 'hrDesignation',action: 'inactive')}?id=" + referenceId,
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
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o,fa fa-retweet');
    }

</script>
</body>
</html>