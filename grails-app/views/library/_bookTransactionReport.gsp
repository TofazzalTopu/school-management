<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Book Transaction Report
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="book-transaction-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="transaction" target="_blank">
                        <div class="form-group">
                            <label for="bookTransactionFor" class="col-md-2 control-label">Report For</label>
                            <div class="col-md-6">
                                <g:select class="form-control" noSelection="${['': 'Select Report Type...']}" id="bookTransactionFor" name='bookTransactionFor'
                                          from='${com.grailslab.enums.BookTransactionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="transactionCategory" class="col-md-2 control-label">Book Category</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="transactionCategory" name='transactionCategory'
                                          noSelection="${['': 'Select Category...']}"
                                          from='${categoryList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="transactionAuthor" class="col-md-2 control-label">Book author</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="transactionAuthor" name="transactionAuthor" tabindex="1" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="transactionLanguage" class="col-md-2 control-label">Language</label>

                            <div class="col-md-6">
                                <div class="form-group">
                                    <div class="col-md-12">
                                        <select class="selectpicker form-control" id="transactionLanguage" name="transactionLanguage">
                                            <option>select language</option>
                                            <option value="bangla">Bangla</option>
                                            <option value="english">English</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="transactionPublisher" class="col-md-2 control-label">Publisher</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="transactionPublisher" name="transactionPublisher" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="transactionPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="transactionPrintType" name='transactionPrintType'
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
<script>
    jQuery(function ($) {
        $('#transactionAuthor').select2({
            placeholder: "Search for a author [name]",
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

        $('#transactionPublisher').select2({
            placeholder: "Search for a publisher [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'publisherList')}",
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
    });

</script>