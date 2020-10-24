<html>
<head>
    <title>Book Issue&Return</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>
<grailslab:breadCrumbActions  breadCrumbTitleText="Book Issue and Return" />
<div class="row" id="issue-form-holder">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                Book Issue
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="issue-form">
                            <div class="row">
                                <input type="hidden" name="id" id="issue-id"/>
                                <div class="row">
                                    <div class="form-group">
                                        <label for="stdEmpAcademicYr" class="col-md-2 control-label">Academic Year</label>
                                        <div class="col-md-9">
                                            <g:select tabindex="1" class="form-control" id="stdEmpAcademicYr" name='stdEmpAcademicYr'
                                                      from='${workingYearList}'
                                                      optionKey="key" optionValue="value" required="required"></g:select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="issueBook" class="col-md-2 control-label">Select Book</label>
                                        <div class="col-md-9">
                                            <input type="hidden" class="form-control" id="issueBook"  name="book" tabindex="2" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="issueStdEmpId" class="col-md-2 control-label">Student/Employee/Member</label>
                                        <div class="col-md-9">
                                            <input type="hidden" class="form-control" id="issueStdEmpId" name="stdEmpId" tabindex="3" />
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">Issue Date</label>
                                    <div class="col-md-9">
                                        <input  class="form-control date-pick" type="text"  name="issueDate" id="issuingDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                                />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="issueReturnDate" class="col-md-2 control-label">Return Date</label>
                                    <div class="col-md-9">
                                        <input  class="form-control date-pick" type="text"  name="returnDate" id="issueReturnDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                                />
                                    </div>
                                </div>
                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="4" id="issue-form-submit-btn" type="submit">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row" id="return-form-holder">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                Book Return
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="return-form">
                            <div class="row">
                                <input type="hidden" name="id" id="return-id"/>
                                <div class="row">
                                    <div class="form-group">
                                        <label for="returnBook" class="col-md-2 control-label">Select Book</label>
                                        <div class="col-md-9">
                                            <input type="hidden" class="form-control select-book"  name="book" id="returnBook" tabindex="1" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">Student/Employee/Member</label>
                                        <div class="col-md-9">
                                            <input  class="form-control" type="text"  id="returnStdEmpId" name="stdEmpId" placeholder="student/employee [name]" readonly/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-2 control-label">Issue Date</label>
                                    <div class="col-md-9">
                                        <input  class="form-control" type="text"  name="issueDate" id="returnIssueDate" placeholder="Issued Date" readonly/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="returningDate" class="col-md-2 control-label">Return Date</label>
                                    <div class="col-md-9">
                                        <input  class="form-control" type="text"  name="returnDate" id="returningDate" placeholder="Returned Date" readonly/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="submissionDate" class="col-md-2 control-label">Submission Date</label>
                                    <div class="col-md-9">
                                        <input  class="form-control" type="text"  name="submissionDate" id="submissionDate" placeholder="Submission Date" readonly/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="fine" class="col-md-2 control-label">Fine</label>
                                    <div class="col-md-9">
                                        <input  class="form-control" type="number"  name="fine" id="fine" placeholder="0.00"/>
                                    </div>
                                </div>
                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="2" id="return-form-submit-btn" type="submit">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var bookName, bookNameBarcode;
    jQuery(function ($) {
        $("#issue-form").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'bookTransaction', action: 'bookIssueSave')}",
                type: 'post',
                dataType: "json",
                data: $("#issue-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        resetForm();
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            });
            return false;
        });
        $("#return-form").submit(function(e) {
            e.preventDefault();
        });

        $('.date-pick').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $('#issueBook').select2({
            placeholder: "Search for a Book [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'bookIssueNameList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, stockStatus:'ADDED'
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
        $('#returnBook').select2({
            placeholder: "Search for a Book [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'bookIssueNameList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, stockStatus:'OUT'
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

        $('#issueStdEmpId').select2({
            placeholder: "Search for a student/employee [name]",
            allowClear: true,
            minimumInputLength:3,
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
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });
        $('#issueStdEmpId').change(function() {
            var allowedDays = $('#issueStdEmpId').select2('data').allowedDays;
            var currentDate = moment().format("DD/MM/YYYY");
            var returnDate = currentDate;
            if(allowedDays) {
                returnDate = moment(currentDate, "DD/MM/YYYY").add(allowedDays, 'days');
            }
            $('#issueReturnDate').val(moment(returnDate).format('DD/MM/YYYY'));
            $('#issuingDate').val(moment().format("DD/MM/YYYY"));
        });

        $('#returnBook').change(function() {
            var control = this;
            var referenceBook = $(control).val();
            $.ajax({
                url: "${createLink(controller: 'bookTransaction', action: 'bookReturn')}?id="+referenceBook,
                type: 'post',
                dataType: "json",
                success: function (data) {
                   $('#returnStdEmpId').val(data);
                },
                failure: function (data) {
                }
            });

        });
        var resetForm = function () {
            $('.date-pick').datepicker('setDate', null);
            $("#select2-chosen-1").empty().append("Search for a Book  [Name]");
            $("#s2id_book").css("pointer-events","auto");
            $("#select2-chosen-2").empty().append("Search for a student/employee [name]");
            $("#s2id_stdEmpId").css("pointer-events","auto");
        };

    });
</script>
</body>
</html>