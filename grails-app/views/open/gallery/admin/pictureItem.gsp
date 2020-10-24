<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Picture Gallery</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>

<body>



<grailslab:breadCrumbActions firstBreadCrumbText="Picture Gallery" firstBreadCrumbUrl="${g.createLink(controller: 'gallery',action: 'picture')}" breadCrumbTitleText="Album Items" SHOW_CREATE_BTN="YES" createButtonText="Add New Image to Album"/>

<grailslab:fullModal modalLabel="Add Image">

    <input type="hidden" name="openGallery" value="${params.album}"/>

    <input type="hidden" name="galleryType" value="${com.grailslab.enums.OpenContentType.PICTURE.key}"/>

    <grailslab:input name="name" label="Title" required="true"></grailslab:input>

    <grailslab:input type="number" name="sortOrder" label="Sort Position" value="1" required="true"></grailslab:input>

    <grailslab:textArea name="description" label="Description"></grailslab:textArea>

    <div class="form-group">

        <div class="col-md-4">

            <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px"

                         height="170px"/>

        </div>



        <div class="col-md-8">

            <label for="pImage" class="col-md-4 col-md-offset-1">Upload Image</label>



            <div class="col-md-7">

                <input type="file" name="pImage" id="pImage"/>

                <p>Miximum Size : 1m, Dimension: 1280 X 720</p>

            </div>

        </div>

    </div>

</grailslab:fullModal>

<section class="panel">

    <div class="panel-body">

        <div class="table-responsive">

            <table class="table table-striped table-hover table-bordered" id="list-table">

                <thead>

                <tr>

                    <th>Images</th>

                    <th>Title</th>

                    <th>Sort Position</th>

                    <th>Status</th>

                    <th>Description</th>

                    <th>Action</th>

                </tr>

                </thead>

                <tbody>

                <g:each in="${dataReturn}" var="dataSet">

                    <tr>

                        <td><img src="${imgSrc.fromIdentifierThumb(imagePath: dataSet[0])}" alt="Image" style="height: 40px; width: 55px"

                                 class="img-thumbnail"></td>

                        <td>${dataSet[1]}</td>

                        <td>${dataSet[2]}</td>

                        <td>${dataSet[3]}</td>

                        <td>${dataSet[4]}</td>

                        <td>



                            <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"

                                                                 class="reference-1" title="Edit"><span

                                        class="fa fa-pencil-square-o"></span></a></span>

                            <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"

                                                                 class="reference-2" title="Inactive"><span

                                        class="fa fa-retweet"></span></a></span>

                        </td>

                    </tr>

                </g:each>

                </tbody>

            </table>

        </div>

    </div>

</section>

<script>

    jQuery(function ($) {

        $('#list-table').dataTable({

            "iDisplayLength": 25

        });


        $("#pImage").change(function () {

            readImageURL(this);

        });

        var validator = $('#modalForm').validate({

            errorElement: 'span',

            errorClass: 'help-block',

            focusInvalid: false,

            rules: {

                name: {

                    required: true

                },

                sortOrder: {

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

                var formData = new FormData(form);

                $.ajax({

                    url: "${createLink(controller: 'gallery', action: 'saveItem')}",

                    type: 'post',

                    dataType: "json",

                    data: formData,

                    mimeType: 'multipart/form-data',

                    contentType: false,

                    cache: false,

                    processData: false,

                    success: function (data) {

                        $('#myModal .create-content .modal-refresh-processing').hide();

                        $('#myModal .create-success .modal-footer-action-btn').show();

                        if (data.isError == true) {

                            $('#myModal .create-success p.message-content').html(data.message);

                        } else {

                            $('#myModal .create-success .message-content').html(data.message);

                        }

                        $('#myModal .create-content').hide(1000);

                        $('#myModal .create-success').show(1000);

                        $('#myModal .create-content .modal-footer-action-btn').show();

                    },

                    failure: function (data) {

                    }

                })

            }

        });



        $('.create-new-btn').click(function (e) {

            $('#myModal .create-success').hide();

            $('#myModal .create-content').show();

            validator.resetForm();

            $('#ImagePreview').attr('src', '${resource(dir:'images', file: 'no-image.jpg')}');

            $("#hiddenId").val('');

            $('#myModal').modal('show');

            e.preventDefault();

        });



        $(".cancel-btn").click(function () {

            var validator = $("#modalForm").validate();

            validator.resetForm();

            $("#hiddenId").val('');

            $('#myModal').modal('hide');

            location.reload();

        });



        $('#list-table').on('click', 'a.reference-1', function (e) {

            var control = this;

            var referenceId = $(control).attr('referenceId');

            jQuery.ajax({

                type: 'POST',

                dataType: 'JSON',

                url: "${g.createLink(controller: 'gallery',action: 'editItem')}?id=" + referenceId,

                success: function (data, textStatus) {

                    if (data.isError == false) {

                        $('#hiddenId').val(data.obj.id);

                        $('#name').val(data.obj.name);

                        $('#description').val(data.obj.description);

                        $('#sortOrder').val(data.obj.sortOrder);

                        $('#ImagePreview').attr('src', '${resource(dir:'images', file: 'no-image.jpg')}');

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

                    url: "${g.createLink(controller: 'gallery',action: 'inactiveItem')}?id=" + referenceId,

                    success: function (data, textStatus) {

                        var confirmDel = confirm(data.message);

                        if (confirmDel == true) {

                            location.reload();

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

            var confirmDel = confirm("Are you sure?");

            if (confirmDel == true) {

                var control = this;

                var referenceId = $(control).attr('referenceId');

                jQuery.ajax({

                    type: 'POST',

                    dataType: 'JSON',

                    url: "${g.createLink(controller: 'gallery',action: 'deleteItem')}?id=" + referenceId,

                    success: function (data, textStatus) {

                        var confirmDel = confirm(data.message);

                        if (confirmDel == true) {

                            location.reload();

                        }

                    },

                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                    }

                });

            }

            e.preventDefault();

        });

    });



    function readImageURL(input) {

        if (input.files && input.files[0]) {

            var reader = new FileReader();

            reader.onload = function (e) {

                $('#ImagePreview').attr('src', e.target.result);

            };

            reader.readAsDataURL(input.files[0]);

        }

    }

</script>



</body>

</html>