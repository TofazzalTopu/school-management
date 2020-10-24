<%@ page contentType="text/html;charset=UTF-8" %>

<html>

<head>
    <title>Gallery Video Album</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>

<body>


<grailslab:breadCrumbActions breadCrumbTitleText="Video Gallery Album" SHOW_CREATE_BTN="YES" createButtonText="Add New Album"/>

<grailslab:fullModal modalLabel="Manage Video Album">

    <grailslab:input name="name" label="Album Name" required="true"></grailslab:input>

    <grailslab:input type="number" name="sortOrder" label="Sort Position" value="1" required="true"></grailslab:input>

    <grailslab:textArea name="description" label="Description"></grailslab:textArea>

</grailslab:fullModal>

<grailslab:dataTable dataSet="${dataReturn}"

                     tableHead="SL, Album Name, Sort Position, Status, Description" actions="fa fa-plus-square-o, fa fa-pencil-square-o,fa fa-retweet"></grailslab:dataTable>



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

                    url: "${createLink(controller: 'gallery', action: 'saveGallery')}?galleryType=${com.grailslab.enums.OpenContentType.VIDEO.key}",

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

            "sAjaxSource": "${g.createLink(controller: 'gallery',action: 'albumList')}?albumType=${com.grailslab.enums.OpenContentType.VIDEO.key}",

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

            var selectRow = $(this).parents('tr');

            var control = this;

            var referenceId = $(control).attr('referenceId');

            var url = "${g.createLink(controller: 'gallery',action: 'videoItem')}?album=" + referenceId;

            window.open(url);

            e.preventDefault();

        });



        $('#list-table').on('click', 'a.reference-2', function (e) {

            var control = this;

            var referenceId = $(control).attr('referenceId');

            jQuery.ajax({

                type: 'POST',

                dataType: 'JSON',

                url: "${g.createLink(controller: 'gallery',action: 'editGallery')}?id=" + referenceId,

                success: function (data, textStatus) {

                    if (data.isError == false) {

                        $('#hiddenId').val(data.obj.id);

                        $('#name').val(data.obj.name);

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

                    url: "${g.createLink(controller: 'gallery',action: 'inactive')}?id=" + referenceId,

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
