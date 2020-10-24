<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Book List Report By Source
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="section-progress-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="sourceBookList" target="_blank">
                        <div class="form-group">
                            <label for="bookSourceReportFor" class="col-md-2 control-label">Book Report For</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="bookSourceReportFor" name='bookSourceReportFor'
                                    noSelection="${['': 'All']}"
                                          from='${com.grailslab.enums.BookStockStatus.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSourceCategory" class="col-md-2 control-label">Book Category</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="bookSourceCategory" name='bookSourceCategory'
                                          noSelection="${['': 'Select Category...']}"
                                          from='${categoryList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSourcePublisher" class="col-md-2 control-label">Publisher</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookSourcePublisher" name="bookSourcePublisher" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSourceAuthor" class="col-md-2 control-label">Book author</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookSourceAuthor" name="bookSourceAuthor" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSource" class="col-md-2 control-label">Source</label>
                            <div class="col-md-6">
                                <select id="bookSource" class="form-control" name="bookSource">
                                <option value="">select source</option>
                                <g:each in="${bookSourceList}" var="source">
                                    <option value="${source.name}">${source.name}</option>
                                </g:each></select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSourceLanguage" class="col-md-2 control-label">Language</label>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <div class="col-md-12">
                                        <select id="bookSourceLanguage" class="form-control" name="bookSourceLanguage">
                                        <option value="">Select language</option>
                                        <g:each in="${bookLanguageList}" var="language">
                                            <option value="${language.name}">${language.name}</option>
                                        </g:each></select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookSourceListPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                        <g:select class="form-control" id="bookSourceListPrintType" name='bookSourceListPrintType'
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

        $('#bookSourceAuthor').select2({
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
        $('#bookSourcePublisher').select2({
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