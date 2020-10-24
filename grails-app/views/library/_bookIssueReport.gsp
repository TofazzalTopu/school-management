<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Book Issue Report
        </header>

        <div class="panel-body">
            <div class="row">
                <div class="row" id="book-issue-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport" action="bookIssue" target="_blank">

                        <div class="form-group">
                            <label for="issueFrom" class="col-md-2 control-label">Select Issue From</label>
                            <div class="col-md-6">
                                <select name="issueFrom" id="issueFrom" class="form-control" tabindex="4">
                                    <option value="all">All</option>
                                    <option value="employee">Teacher</option>
                                    <option value="student">Student</option>

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
