
<section class="panel">
    <header class="panel-heading">
        Tabulation Report
    </header>
    <div class="panel-body">
        <div class="row" id="tabulation-report-holder">
            <div class="form-horizontal" role="form">
                    <div class="form-group">
                        <div class="col-md-3">
                                <select name="taSection" id="taSection" class="form-control ta-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                    <g:each in="${sectionExamList}" var="dataSet">
                                        <option value="${dataSet.id}">${dataSet.sectionName}</option>
                                    </g:each>
                                </select>
                        </div>
                        <div class="col-md-3">
                            <select name="taSortBy" id="taSortBy" class="form-control ta-merit-step" tabindex="3">
                                <option value="rollNo">Roll No </option>
                                <option value="secMeritPos">Section Merit Position</option>
                                <option value="clsMeritPos">Class Merit Position</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <g:select class="form-control ta-exam-type-print" id="tabulationPrintOptionType" name='tabulationPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required"></g:select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-info" id="tabulation-print-btn">Print</button>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</section>