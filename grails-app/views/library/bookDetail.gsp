<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Book Details</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Book" SHOW_CREATE_BTN="YES" createButtonText="Add Book"/>

<grailslab:fullModal label="Category Entry">
    <grailslab:input name="name" label="Category Name" required="true"></grailslab:input>
    <grailslab:textArea name="description" label="Category Description"></grailslab:textArea>
</grailslab:fullModal>

<g:render template="/library/createPublisherModal"></g:render>
<g:render template="/library/createAuthorModal"></g:render>

<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <div class="row">
                                <input type="hidden" name="id" id="id"/>

                                <div class="form-group">
                                    <label for="name" class="col-md-2 control-label">Name*</label>

                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="name" id="bookName"
                                               placeholder="Name" required="true"
                                               tabindex="2"/>
                                    </div>

                                    <label for="category" class="col-md-2 control-label">Category*</label>

                                    <div class="col-md-3">
                                        <g:select class=" form-control" id="category" name='category' tabindex="1"
                                                  noSelection="${['': 'Select Category...']}"
                                                  from="${categoryList}"
                                                  optionKey="id" optionValue="name" required="required">
                                        </g:select>

                                    </div>

                                    <div class="col-md-1">
                                        <a class="btn btn-primary small pull-right create-category-btn"
                                           href="javascript:void(0);">
                                            <i class="fa fa-plus"></i>
                                        </a>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="title" class="col-md-2 control-label">Title</label>

                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="title" id="title"
                                               placeholder="Title/Bangla Name"
                                               tabindex="2"/>
                                    </div>
                                    <label for="bookPublisher" class="col-md-2 control-label">Publisher *</label>

                                    <div class="col-md-3">
                                        <input type="hidden" class="form-control" id="bookPublisher" required="required"
                                               name="bookPublisher" tabindex="2"/>
                                    </div>

                                    <div class="col-md-1">
                                        <a class="btn btn-primary small pull-right create-publisher-btn"
                                           href="javascript:void(0);">
                                            <i class="fa fa-plus"></i>
                                        </a>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="author" class="col-md-2 control-label">Author*</label>

                                    <div class="col-md-3">
                                        <input type="hidden" class="form-control" id="author" name="author"
                                               tabindex="2"/>
                                    </div>

                                    <div class="col-md-1">
                                        <a class="btn btn-primary small pull-right create-author-btn"
                                           href="javascript:void(0);">
                                            <i class="fa fa-plus"></i>
                                        </a>
                                    </div>

                                    <label for="language" class="col-md-2 control-label">Language*</label>

                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="language" id="language"
                                               placeholder="language" value="Bangla"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="addAuthor" class="col-md-2 control-label">Other Author(s)</label>

                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="addAuthor" id="addAuthor"
                                               placeholder="Add other author(s) Name(If any)"
                                               tabindex="2"/>
                                    </div>

                                        <label for="comments" class="col-md-2 control-label">Note</label>
                                        <div class="col-md-4">
                                            <textarea id="comments" name="comments" class="form-control"  placeholder="Add your short note" rows="1" tabindex="1"></textarea>
                                        </div>
                                </div>

                                <div class="form-group" id="book-stock-div" style="display: none;">
                                    <div class="form-group">
                                        <label for="quantity" class="col-md-2 control-label">Quantity*</label>

                                        <div class="col-md-4">
                                            <input class="form-control" type="number" name="quantity"
                                                   id="quantity" placeholder="quantity" tabindex="2"
                                                   required="required"/>
                                        </div>

                                        <label for="price" class="col-md-2 control-label">Price*</label>

                                        <div class="col-md-4">
                                            <input class="form-control" type="number" name="price" id="price"
                                                   placeholder="Per Book Price" tabindex="2" required="required"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="stockDate" class="col-md-2 control-label">Stock Date *</label>

                                        <div class="col-md-4">
                                            <input class="form-control" type="text" name="stockDate" id="stockDate"
                                                   placeholder="Stock date" required="required"
                                                   tabindex="2"/>
                                        </div>

                                        <label for="source" class="col-md-2 control-label">Source*</label>

                                        <div class="col-md-4">
                                            <input class="form-control" type="text" name="source" id="source"
                                                   placeholder="Source" tabindex="2" required="required"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default" id="cancel-btn" tabindex="8"
                                            type="reset">Cancel</button>
                                </div>

                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Book Details List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Sl</th>
                            <th >Name</th>
                            <th>Author</th>
                            <th>Publisher</th>
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
    var bookDetailsTable;
    jQuery(function ($) {

        bookDetailsTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-6 category-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'library',action: 'bookDetailList')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "categoryName", "value": $('#categoryFilter').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'editBook,delete'));
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

        $('#list-table_wrapper div.category-filter-holder').html('<div class="col-md-6"><select id="categoryFilter" class="form-control" name="categoryFilter"><option value="">All Category</option><g:each in="${categoryList}" var="bookCategory"><option value="${bookCategory.id}">${bookCategory.name}</option> </g:each></select></div>');

        $('#categoryFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            bookDetailsTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");

            $("#id").val("");
            $("#bookName").val("");
            $("#title").val("");
            $("#bookPublisher").val("");
            $("#author").val("");
            $("#addAuthor").val("");
            $("#language").val("");
            $("#quantity").val("");
            $("#select2-chosen-1").empty().append("Search for a Author");
            $("#s2id_author").css("pointer-events", "auto");
            $("#s2id_category").css("pointer-events", "auto");
            $("#select2-chosen-2").empty().append("Search for a Publisher");
            $("#s2id_bookPublisher").css("pointer-events", "auto");
            $("#bookName").focus();
            $("#book-stock-div").show();
            e.preventDefault();
        });

        $("#cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
            $("#book-stock-div").hide();
            $("#create-form-holder").hide(500);
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'library', action: 'bookDetailSave')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#create-form"));
                        bookDetailsTable.draw(false);
                        showSuccessMsg(data.message);
                        $("#create-form-holder").hide(500);
                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            });
            return false;
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            $("#create-form-holder").show(500);
            $("#bookName").focus();
            if($("#book-stock-div")){
                $("#book-stock-div").hide();
                var validator = $("#create-form").validate();
                validator.resetForm();
            }

            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'library',action: 'bookDetailEdit')}?id=" + referenceId,
                success: function (data, textStatus) {

                    if (data.isError == false) {
                        //   alert(JSON.stringify(data))
                        //  alert(data.obj.bookPublisher.id)
                        $("#form-submit-btn").html("Edit");
                        $("#id").val(data.obj.id);
                        $('#bookName').val(data.obj.name);
                        $('#hiddenId').val(data.obj.id);
                        $('#title').val(data.obj.title);
                        $('#category').val(data.obj.category.id);
                        $('#author').val(data.obj.author.id);
                        $("#select2-chosen-1").empty().append(data.bookAuthor);
                        $('#bookPublisher').val(data.obj.bookPublisher.id);
                        $("#select2-chosen-2").empty().append(data.bookPublisher);
                        $('#addAuthor').val(data.obj.addAuthor);
                        $('#language').val(data.obj.language);
                        $("#create-form-holder").show(500);
                        $("#bookName").focus();
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#author').select2({
            placeholder: "Search for a Author [name, country]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'authorTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        $('#bookPublisher').select2({
            placeholder: "Search for a Publisher [name, country]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'publisherTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
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
                    url: "${g.createLink(controller: 'library',action: 'bookDetailInactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            bookDetailsTable.draw(false);
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

        $('.create-category-btn').click(function (e) {
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

        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
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
                    url: "${createLink(controller: 'bookCategory', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            clearForm(form);
                            $("#category").append('<option selected value='+ data.bookCategory.id +'>' + data.bookCategory.name + '</option>');
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

        $('.create-publisher-btn').click(function (e) {
            publisherValidator.resetForm();
            $("#hiddenId").val('');
            $('#publisher-modal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var publisherValidator = $("#publisher-modal-form").validate();
            publisherValidator.resetForm();
            $("#hiddenId").val('');
            $('#publisher-modal').modal('hide');
        });

        var publisherValidator = $('#publisher-modal-form').validate({
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
                    url: "${createLink(controller: 'bookPublisher', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#publisher-modal-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            clearForm(form);
                            // $('#bookPublisher').val(data.publisherId).trigger('change');
                            $('#publisher-modal').modal('hide');
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

        var authorValidator = $('#author-modal-form').validate({
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
                    data: $("#author-modal-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            clearForm(form);
                            $('#author-modal').modal('hide');
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

        $('.create-author-btn').click(function (e) {
            authorValidator.resetForm();
            $("#hiddenId").val('');
            $('#author-modal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var authorValidator = $("#publisher-modal-form").validate();
            authorValidator.resetForm();
            $("#hiddenId").val('');
            $('#author-modal').modal('hide');
        });

       if(!$("#id").val()){
           $('#stockDate').datepicker({
               format: 'dd/mm/yyyy',
               autoclose: true
           });
       }

    });

</script>
</body>
</html>