<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Book Entry</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Author" SHOW_CREATE_BTN="YES" createButtonText="Add New"/>
<grailslab:fullModal>
<grailslab:input required="true" name="name" label="Author Name"></grailslab:input>
<grailslab:input name="bengaliName" label="Bengali Name"></grailslab:input>
<grailslab:input name="country" label="Country"></grailslab:input>
</grailslab:fullModal>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Book Category List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>SL</th>
                                <th>Name</th>
                                <th>Bangla Name</th>
                                <th>Country</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
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
                    url: "${createLink(controller: 'bookAuthor', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
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

        var authorList = $('#list-table').DataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [1, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'bookAuthor',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'edit, delete'));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $('.create-new-btn').click(function (e) {
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

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'bookAuthor',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#bengaliName').val(data.obj.bengaliName);
                        $('#country').val(data.obj.country);
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

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'bookAuthor',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            authorList.draw(false);
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

</script>
</body>
</html>