<%@ page contentType="text/html;charset=UTF-8" %>

<html>

<head>
    <title>Virtual Library</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>

<body>


<grailslab:breadCrumbActions breadCrumbTitleText="Online Book Category" SHOW_CREATE_BTN="YES" createButtonText="Add New Category"/>

<grailslab:fullModal modalLabel="Manage Book Category">

    <grailslab:input name="categoryName" label="Category Name" required="true"></grailslab:input>

    <grailslab:input type="number" name="sortOrder" label="Sort Position" value="1" required="true"></grailslab:input>

    <grailslab:textArea name="description" label="Description"></grailslab:textArea>

</grailslab:fullModal>

<grailslab:dataTable dataSet="${dataReturn}" tableHead="SL, Category Name, Sort Position, Status, Description"
                     actions="fa fa-plus-square-o, fa fa-pencil-square-o,fa fa-retweet">
</grailslab:dataTable>



<script>

    jQuery(function ($) {

        var validator = $('#modalForm').validate({

            errorElement: 'span',

            errorClass: 'help-block',

            focusInvalid: false,

            rules: {

                categoryName: {

                    required: true,

                    maxlength: 200

                }, sortOrder: {

                    required: true,

                    number: true

                }, description: {

                    required: true,


                },

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

                    url: "${createLink(controller: 'onlineLibrary', action: 'sveBookCategory')}",

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



        $('#list-table').dataTable({

            "bAutoWidth": true,

            "bServerSide": true,

            "iDisplayLength": 25,

            "deferLoading": ${totalCount?:0},

            "sAjaxSource": "${g.createLink(controller: 'onlineLibrary',action: 'bookCategoryList')}",

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



        $('#list-table').on('click', 'a.reference-1', function (e) {

            var selectRow = $(this).parents('tr');

            var control = this;

            var referenceId = $(control).attr('referenceId');

            var url = "${g.createLink(controller: 'onlineLibrary',action: 'categoryBookList')}?categoryId=" + referenceId;

            window.open(url);

            e.preventDefault();

        });



        $('#list-table').on('click', 'a.reference-2', function (e) {

            var control = this;

            var referenceId = $(control).attr('referenceId');

            jQuery.ajax({

                type: 'POST',

                dataType: 'JSON',

                url: "${g.createLink(controller: 'onlineLibrary',action: 'editCategory')}?id=" + referenceId,

                success: function (data, textStatus) {

                    if (data.isError == false) {

                        $('#hiddenId').val(data.obj.id);

                        $('#categoryName').val(data.obj.categoryName);

                        $('#sortOrder').val(data.obj.sortOrder);

                        $('#description').val(data.obj.description);

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



        $('#list-table').on('click', 'a.reference-3', function (e) {

            var selectRow = $(this).parents('tr');

            var confirmDel = confirm("Are you sure?");

            if (confirmDel == true) {

                var control = this;

                var referenceId = $(control).attr('referenceId');

                jQuery.ajax({

                    type: 'POST',

                    dataType: 'JSON',

                    url: "${g.createLink(controller: 'onlineLibrary',action: 'inactive')}?id=" + referenceId,

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

        return getActionBtnCustom(nRow, aData,'fa fa-plus-square-o, fa fa-pencil-square-o,fa fa-retweet');

    }

</script>

</body>

</html>
