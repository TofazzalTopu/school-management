<%@ page import="com.grailslab.enums.BookTransactionType" %>

<html>
<head>
    <title>Books </title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Library Report"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Book History
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="row" id="book-history-report-holder">
                        <g:form class="form-horizontal" role="form" controller="libraryReport"
                                action="bookHistory" target="_blank">
                            <div class="form-group">
                                <label for="bookName" class="col-md-2 control-label">Select Book</label>
                                <div class="col-md-6">
                                    <input type="hidden" class="form-control" id="bookName" name="bookName" tabindex="1" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="fromDate" class="col-md-2 control-label">From Date</label>

                                <div class="col-md-6">
                                    <input  class="form-control" type="text"  name="fromDate" id="fromDate" placeholder="<g:formatDate date="${new java.util.Date().minus(180)}" format="dd/MM/yyyy" />"
                                            tabindex="2"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="toDate" class="col-md-2 control-label">To Date</label>

                                <div class="col-md-6">
                                    <input  class="form-control" type="text"  name="toDate" id="toDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                            tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="bookHistoryPrintType" class="col-md-2 control-label">Print Type</label>

                                <div class="col-md-6">
                                    <g:select class="form-control" id="bookHistoryPrintType" name='bookHistoryPrintType'
                                              from='${com.grailslab.enums.PrintOptionType.values()}'
                                              optionKey="key" optionValue="value" required="required"></g:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button type="submit"
                                            class="btn btn-primary">Show Report</button>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </section>
    </div>
        <g:render template='/library/bookIssueReport'/>
        <g:render template='/library/userHistoryReport'/>
        <g:render template='/library/bookSummaryReport'/>
        <g:render template='/library/bookListReport'/>
        <g:render template='/library/bookListReportBySource'/>
        <g:render template='/library/bookTopNReaderReport'/>
        <g:render template='/library/bookBarcodeReport'/>
</div>
<script>
    jQuery(function ($) {
        $('#category').select2();
        $('#book').select2();
        $('#author').select2();
        $('#bookDetails').select2();

    $('#bookName').select2({
        placeholder: "Search for a Book [name]",
        allowClear: true,
        minimumInputLength:3,
        ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
            url: "${g.createLink(controller: 'remote',action: 'bookNameTypeAheadList')}",
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
    $('#stuEmpID').select2({
        placeholder: "Search for a student [name]",
        allowClear: true,
        minimumInputLength:3,
        ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
            url: "${g.createLink(controller: 'remote',action: 'studentEmployeeTypeAheadList')}",
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


    $('#fromDate').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });
    $('#toDate').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });


    });
</script>
</body>

</html>