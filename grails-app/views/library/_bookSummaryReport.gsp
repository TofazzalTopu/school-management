<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Book Summary Report
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="book-progress-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="bookSummary" target="_blank">
                        <div class="form-group">
                            <label for="summaryType" class="col-md-2 control-label">Select Type</label>
                            <div class="col-md-6">
                                <select name="summaryType" id="summaryType" class="form-control" tabindex="4">
                                    <option value="all">All</option>
                                    <option value="byCategory">By Category</option>
                                    <option value="byPublisher">By Publisher</option>
                                    <option value="byAuthor">By Author</option>

                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="printOptionType" class="col-md-2 control-label">Print Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="printOptionType" name='printOptionType'
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
