<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Category wise Book List</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Online Book"
                             firstBreadCrumbUrl="${g.createLink(controller: 'onlineLibrary',action: 'bookCategory')}"
                             breadCrumbTitleText="Category Book List" SHOW_CREATE_BTN="YES"
                             createButtonText="Add New Book to Category"/>

<grailslab:fullModal modalLabel="Add Book" enctype="multipart/form-data" controller="onlineLibrary" action="saveBook" method="post">
    <input type="hidden" id="hiddenId" name="id" value="" />
    <input type="hidden" name="categoryId" value="${params.categoryId}" />
    <grailslab:input name="bookName" label="Book Name" required="true"></grailslab:input>
    <div class="form-group">
        <label class="col-md-4 control-label">Pdf <span class="required">*</span></label>

        <div class="col-md-8">
            <input type="file" name="pdfFile" id="pdfFile"/>
            <p>Maximum Size : 15mb, Only Pdf file</p>
        </div>
    </div>
    <grailslab:textArea name="description" label="Description"></grailslab:textArea>
</grailslab:fullModal>

<section class="panel">
    <div class="panel-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover table-bordered" id="list-table">
                <thead>
                <tr>
                    <th>Serial</th>
                    <th>Book Name</th>
                    <th>Link</th>
                    <th>Status</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${dataReturn}" var="dataSet">
                    <tr>
                        <td>${dataSet[0]}</td>
                        <td><g:link controller="onlineLibrary" action="downloadFile" params="[identifier: dataSet[2]]" target="_blank">${dataSet[1]} <i class="fa fa-download"></i></g:link></td>
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

        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                bookName: {
                    required: true
                },
                pdfFile: {
                    required: true
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            }
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
            location.reload();
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'onlineLibrary',action: 'editBook')}?id=" + referenceId + "&categoryId=" + ${params.categoryId},
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.obj.id);
                        $('#bookName').val(data.obj.bookName);
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

        $('#list-table').on('click', 'a.reference-2', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'onlineLibrary',action: 'inactiveBook')}?id=" + referenceId + "&categoryId=" + ${params.categoryId},
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
</script>
</body>
</html>