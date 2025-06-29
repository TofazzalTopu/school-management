<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Book List Report
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="section-progress-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="bookList" target="_blank">
                        <div class="form-group">
                            <label for="bookReportFor" class="col-md-2 control-label">Book Report For</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="bookReportFor" name='bookReportFor' noSelection="${['': 'All']}"
                                          from='${com.grailslab.enums.BookStockStatus.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookCategory" class="col-md-2 control-label">Book Category</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="bookCategory" name='bookCategory'
                                          noSelection="${['': 'Select Category...']}"
                                          from='${categoryList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookPublisher" class="col-md-2 control-label">Publisher</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookPublisher" name="bookPublisher" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookAuthor" class="col-md-2 control-label">Book author</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookAuthor" name="bookAuthor" tabindex="1" />
                                %{--<g:select class="form-control" id="author" name='author'
                                          noSelection="${['': 'Select author...']}"
                                          from='${authorList}'
                                          optionKey="id" optionValue="name"></g:select>--}%
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookLanguage" class="col-md-2 control-label">Language</label>
                            <div class="col-md-6">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                        <select class="selectpicker form-control" id="bookLanguage" name="bookLanguage">
                                                <option value="">Select language</option>
                                                <g:each in="${bookLanguageList}" var="language">
                                                    <option value="${language.name}">${language.name}</option>
                                                </g:each></select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookListPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                        <g:select class="form-control" id="bookListPrintType" name='bookListPrintType'
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
        $('#bookAuthor').select2({
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
        $('#bookPublisher').select2({
            placeholder: "Search for a publisher [name]",
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
    });
</script>