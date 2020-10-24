<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
          Top Number Reader
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="top-progress-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="bookTopNReaderList" target="_blank">
                        <div class="form-group">
                            <label for="" class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date: new Date().minus(5), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="formDate" name="formDate"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="toDate" name="toDate"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookTopCategory" class="col-md-2 control-label">Book Category</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="bookTopCategory" name='bookTopCategory'
                                          noSelection="${['': 'Select Category...']}"
                                          from='${categoryList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookTopPublisher" class="col-md-2 control-label">Publisher</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookTopPublisher" name="bookTopPublisher" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookTopAuthor" class="col-md-2 control-label">Book author</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="bookTopAuthor" name="bookTopAuthor" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookTopLanguage" class="col-md-2 control-label">Language</label>
                            <div class="col-md-6">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                        <select class="selectpicker form-control" id="bookTopLanguage" name="bookTopLanguage">
                                                <option value="">Select language</option>
                                                <g:each in="${bookLanguageList}" var="language">
                                                    <option value="${language.name}">${language.name}</option>
                                                </g:each></select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bookTopPrintType" class="col-md-2 control-label">Print Type</label>
                            <div class="col-md-6">
                        <g:select class="form-control" id="bookTopPrintType" name='bookTopPrintType'
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
        $('#bookTopAuthor').select2({
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
        $('#bookTopPublisher').select2({
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

        $('.picKDate').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

    });
</script>