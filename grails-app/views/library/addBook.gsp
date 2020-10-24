<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Book</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Add Book" SHOW_CREATE_BTN="YES" createButtonText="Add Book Item"/>
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
                                    <label for="bookDetails" class="col-md-2 control-label">Select Book*</label>
                                    <div class="col-md-4">
                                        <input type="hidden" class="form-control" id="bookDetails" name="bookDetails" tabindex="5" />
                                    </div>
                                    <label for="quantity" class="col-md-2 control-label">Quantity*</label>
                                    <div class="col-md-3">
                                        <input class="form-control" type="number" name="quantity" id="quantity" placeholder="quantity"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="form-group">

                                    <label for="source" class="col-md-2 control-label">Source*</label>
                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="source" id="source" placeholder="Source"
                                               tabindex="2"/>
                                    </div>
                                    <label for="price" class="col-md-2 control-label">Per Unit Price*</label>
                                    <div class="col-md-3">
                                        <input class="form-control" type="number" name="price" id="price" placeholder="Per Book Price"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="form-group">

                                    <label for="comments" class="col-md-2 control-label">Short Note</label>
                                    <div class="col-md-4">
                                        <textarea id="comments" name="comments" class="form-control"  placeholder="Add your Short Note"
                                                  tabindex="1"></textarea>
                                    </div>

                                    <label for="stockDate" class="col-md-2 control-label">Stock date*</label>
                                    <div class="col-md-3">
                                        <input class="form-control" type="text" name="stockDate" id="stockDate" placeholder="stockDate"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="form-group" id="barcodeHolder" style="display: none;">
                                    <label for="barcode" class="col-md-2 control-label">Barcode</label>
                                    <div class="col-md-4">
                                        <input class="form-control" type="text" name="barcode"  id="barcode" placeholder="barcode"
                                               tabindex="2"/>
                                    </div>
                                </div>

                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default cancel-btn" tabindex="8"
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
                Book Stock List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th >SL</th>
                            <th >Book ID</th>
                            <th >Name</th>
                            <th>Author</th>
                            <th>Price</th>
                            <th>Barcode</th>
                            <th>Stock Date</th>
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
    var bookStockTable;
    jQuery(function ($) {
        bookStockTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 category-filter-holder'><'col-md-3 status-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'bookStock',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "categoryName", "value": $('#categoryFilter').val()});
                aoData.push({"name": "bookStockStatus", "value": $('#statusFilter').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'editBook,delete'));
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
        $('#list-table_wrapper div.category-filter-holder').html('<select id="categoryFilter" class="form-control" name="categoryFilter"><option value="">All Category</option><g:each in="${categoryList}" var="bookCategory"><option value="${bookCategory.id}">${bookCategory.name}</option> </g:each></select>');
        $('#list-table_wrapper div.status-filter-holder').html('<select id="statusFilter" class="form-control" name="statusFilter"><option value="">All</option><g:each in="${bookStatusList}" var="bookStatus"><option value="${bookStatus.key}">${bookStatus.value}</option> </g:each></select>');

        $('#categoryFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            bookStockTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#statusFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            bookStockTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $("#bookDetails").val("")
            $("#quantity").val("").css("pointer-events","auto");
            $("#source").val("")
            $("#comments").val("")
            $("#select2-chosen-1").empty().append("Search for a Book [Name]");
            $("#s2id_bookDetails").css("pointer-events","auto");
            $("#name").focus();
            e.preventDefault();
        });
        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
            $("#create-form-holder").hide(500);
        });

        $("#create-form").bind("keypress", function (e) {
            if (e.keyCode === 13 || e.keyCode === 169) {
                return false;
            }
        });
        $("#create-form").submit(function(e) {
            $.ajax({
                url: "${createLink(controller: 'bookStock', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#create-form"));
                        bookStockTable.draw(false);
                        showSuccessMsg(data.message);
                        $("#create-form-holder").hide(500);
                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            e.preventDefault();
            return false;
        });

        $('#stockDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            $("#create-form-holder").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'bookStock',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var stockDate=data.obj.stockDate;

                        $("#form-submit-btn").html("Edit");
                        $("#barcodeHolder").show();
                        $("#barcode").val(data.obj.barcode);
                        $("#id").val(data.obj.id)
                        $('#hiddenId').val(data.obj.id);
                        $('#bookDetails').val(data.obj.bookDetails.id);
                        $("#select2-chosen-1").empty().append(data.bookName);
                        $("#s2id_bookDetails").css("pointer-events","none");
                        $('#quantity').val(1).css("pointer-events","none");
                        $('#source').val(data.obj.source);
                        $('#stockDate').datepicker('setDate', new Date(stockDate));
                        $('#price').val(data.obj.price);
                        $("#create-form-holder").show(500);
                        $("#name").focus();

                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();

        });

        $('#bookDetails').select2({
            placeholder: "Search for a Book [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'bookDetailList')}",
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
                    url: "${g.createLink(controller: 'bookStock',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            bookStockTable.draw(false);
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