<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Print Bar code
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="section-progress-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="printBarcode" target="_blank">
                        <div class="form-group">
                            <label for="barcodeBookCategory" class="col-md-2 control-label">Book Category</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="barcodeBookCategory" name='bookCategoryId'
                                          noSelection="${['': 'Select Category...']}"
                                          from='${categoryList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="barcodeBookPublisher" class="col-md-2 control-label">Publisher</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="barcodeBookPublisher" name="bookPublisherId" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="barcodeBookAuthor" class="col-md-2 control-label">Book author</label>

                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="barcodeBookAuthor" name="bookAuthorId" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="barcodeBookName" class="col-md-2 control-label">Select Book</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="barcodeBookName" name="bookNameId" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="barcodeBookName" class="col-md-2 control-label">Book Details</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="barcodeBookDetail" name="bookDetailId" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="barcodePrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                        <g:select class="form-control" id="barcodePrintType" name='barcodePrintType'
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

        $('#barcodeBookAuthor').select2({
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
        $('#barcodeBookPublisher').select2({
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
        $('#barcodeBookName').select2({
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
        $('#barcodeBookDetail').select2({
            placeholder: "Search for a book detail [name/book_id]",
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
    });

</script>