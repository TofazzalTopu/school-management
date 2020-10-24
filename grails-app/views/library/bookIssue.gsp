<html>
<head>
    <title>Book Issue</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Book Issue" SHOW_CREATE_BTN="YES" createButtonText="Book Issue"/>

<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <div class="row">
                                <input type="hidden" name="id" id="id"/>

                                <div class="row">
                                    <div class="form-group">
                                        <label for="stdEmpAcademicYr" class="col-md-3 control-label">Academic Year</label>

                                        <div class="col-md-6">
                                            <g:select class="form-control" id="stdEmpAcademicYr" name='stdEmpAcademicYr'
                                                      from='${workingYearList}'
                                                      optionKey="key" optionValue="value" required="required"></g:select>
                                        </div>
                                        <div class="col-md-3"></div>
                                    </div>

                                    <div class="form-group">
                                        <label for="book" class="col-md-3 control-label">Select Book</label>

                                        <div class="col-md-6">
                                            <input type="hidden" class="form-control" id="book" name="book"
                                                   tabindex="1"/>
                                        </div>

                                        <div class="col-md-3"></div>
                                    </div>

                                    <div class="form-group">
                                        <label for="stdEmpId" class="col-md-3 control-label">Student/Employee/Member</label>
                                        <div class="col-md-6">
                                            <input type="hidden" class="form-control" id="stdEmpId" name="stdEmpId" tabindex="2" />
                                        </div>
                                    </div>
                                    <div class="col-md-3"></div>
                                </div>

                                <div class="form-group">
                                    <label for="issueDate" class="col-md-3 control-label">Issue Date</label>

                                    <div class="col-md-2">
                                        <input class="form-control" type="text" name="issueDate" id="issueDate"
                                               placeholder="<g:formatDate date="${new java.util.Date()}"
                                                                          format="dd/MM/yyyy"/>"
                                               tabindex="2"/>
                                    </div>

                                    <label for="returnDate" class="col-md-2 control-label">Return Date</label>

                                    <div class="col-md-2">
                                        <input class="form-control" type="text" name="returnDate" id="returnDate"
                                               placeholder="<g:formatDate date="${new java.util.Date()}"
                                                                          format="dd/MM/yyyy"/>"
                                               tabindex="2"/>
                                    </div>

                                    <div class="col-md-3"></div>
                                </div>

                                <div class="form-group">
                                    <div class="col-md-3"></div>
                                    <div class="col-md-6">
                                        <div class="pull-right">
                                            <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                            <button class="btn btn-default cancel-btn" tabindex="8"
                                                    type="reset">Cancel</button>
                                        </div>
                                    </div>
                                    <div class="col-md-3"></div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<grailslab:fullModal modalLabel="Book Receive" formId="2ndForm" hiddenId="receiveId" labelId="2ndLabel" modalId="2ndModal">
    <grailslab:input name="bookIssueName" required="true" label="Book Name"></grailslab:input>
    <grailslab:input name="bookIssueDate" required="true" label="Issue Date"></grailslab:input>
    <grailslab:input name="bookReturnDate" required="true" label="Return Date"></grailslab:input>
    <grailslab:input name="submissionDate" required="true" label="Submission Date"></grailslab:input>
    <grailslab:input type="number"  step="any" name="fine" required="true" label="Fine"></grailslab:input>
</grailslab:fullModal>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Book Issue  Lists
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th >Name</th>
                            <th>Book ID</th>
                            <th>Book Name</th>
                            <th>Author</th>
                            <th>Issue Date</th>
                            <th>Return Date</th>
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
    var bookName, bookNameBarcode;
    jQuery(function ($) {

        var bookIssuTable = $('#list-table').DataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "order": [0, "desc" ],
            "sAjaxSource": "${g.createLink(controller: 'bookTransaction',action: 'bookIssueList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'edit,return,confirm,delete,'));
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

        $("#create-form").bind("keypress", function (e) {
            if (e.keyCode === 13 || e.keyCode === 169) {
                return false;
            }
        });

        $("#2ndForm").bind("keypress", function (e) {
            if (e.keyCode === 13 || e.keyCode === 169) {
                return false;
            }
        });
        $('.create-new-btn').click(function (e) {
            $("#create-br-form-holder").hide(500);
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#select2-chosen-1").empty().append("Search for a Book  [Name]");
            $("#s2id_book").css("pointer-events","auto");
            $("#select2-chosen-2").empty().append("Search for a student/employee [name]");
            $("#s2id_stdEmpId").css("pointer-events","auto");
            e.preventDefault();
        });


        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
            $("#create-form-holder").hide(500);
        });

        $('#issueDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#bookIssueDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#bookReturnDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#returnDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#submissionDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'bookTransaction', action: 'bookIssueSave')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        clearForm($("#create-form"));
                        $('#book').val('');
                        bookIssuTable.draw(false);
                        showSuccessMsg(data.message);
                        $("#create-form-holder").hide(500);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            return false;
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            $("#create-form-holder").show(500);
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'bookTransaction',action: 'issueEdit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var returnDate = data.obj.returnDate;
                        var issueDate = data.obj.issueDate;
                        $("#form-submit-btn").html("Edit");
                        $("#id").val(data.obj.id);
                        $('#stdEmpAcademicYr').val(data.borrowingYear.name);
                        $('#issueDate').datepicker('setDate', new Date(issueDate));
                        $('#returnDate').datepicker('setDate', new Date(returnDate));
                        $('#book').val(data.obj.book.id);
                        $('#stdEmpId').val(data.borrowerId);
                        $("#select2-chosen-1").empty().append(data.borrowerBookName);
                        $("#select2-chosen-2").empty().append(data.borrowerName);
                        $("#s2id_book").css("pointer-events","none");
                        $("#s2id_stdEmpId").css("pointer-events","none");
                        $('#squarespaceModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();

        });

        $('#list-table').on('click', 'a.return-reference', function (e) {
            var fine;
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'bookTransaction',action: 'issueReturn')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#2ndModal .create-success').hide();
                        $('#2ndModal .create-content').show();
                        $('#modalForm').resetForm();
                        $(".form-group").removeClass("has-erreturnDateror");
                        $("#2ndLabel").html(data.borrowerName);
                        $("#fine").val('0');
                        $('#bookIssueName').val(data.borrowerBookName).css("pointer-events","none");
                        $('#bookIssueDate').datepicker('setDate', new Date(data.bookIssueDate)).css("pointer-events","none");
                        $('#bookReturnDate').datepicker('setDate', new Date(data.bookReturnDate)).css("pointer-events","none");
                        $('#submissionDate').datepicker('setDate', new Date());
                        $("#receiveId").val(referenceId);
                        $('#2ndModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.confirm-reference', function (e) {
            var fine;
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'bookTransaction',action: 'bookReturnSave')}?id=" + referenceId,
                success: function (data) {
                    if (data.isError == false) {
                        bookIssuTable.draw(false);
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                    bookIssuTable.draw(false);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
         }
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
                    url: "${g.createLink(controller: 'bookTransaction',action: 'issueDelete')}?id=" + referenceId,
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
        $('#book').select2({
            placeholder: "Search for a Book [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'bookIssueNameList')}",
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

        $('#stdEmpId').select2({
            placeholder: "Search for a student/employee [name]",
            allowClear: true,
            minimumInputLength: 3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'stdEmpGuardianTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return {results: data.items};
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) {
                return m;
            } // we do not want to escape markup since we are displaying html in results
        });

        $('#stdEmpId').change(function() {
            var allowedDays = $('#stdEmpId').select2('data').allowedDays;
            var currentDate = moment().format("DD/MM/YYYY");
            var returnDate = currentDate;
            if(allowedDays) {
                 returnDate = moment(currentDate, "DD/MM/YYYY").add(allowedDays, 'days');
            }
            $('#returnDate').val(moment(returnDate).format('DD/MM/YYYY'));
            $('#issueDate').val(moment().format("DD/MM/YYYY"));
        });

        $('#2ndForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                submissionDate: {
                    required: true
                },
                fine: {
                    required: true,
                    number:true
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
                $('#2ndModal .create-content .modal-footer-action-btn').hide();
                $('#2ndModal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'bookTransaction', action: 'bookReturnSave')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#2ndForm").serialize(),
                    success: function (data) {
                        $('#2ndModal .create-content .modal-refresh-processing').hide();
                        $('#2ndModal .create-success .modal-footer-action-btn').show();
                        if (data.isError == true) {
                            $('#2ndModal .create-success p.message-content').html(data.message);
                        } else {
                            $('#2ndModal .create-success .message-content').html(data.message);
                        }
                        bookIssuTable.draw(false);
                        $('#2ndModal .create-content').hide(1000);
                        $('#2ndModal .create-success').show(1000);
                        $('#2ndModal .create-content .modal-footer-action-btn').show();
                    },
                    failure: function (data) {
                    }
                })
            }
        });
    });


</script>
</body>
</html>