<section class="panel">
    <header class="panel-heading">
        Merit Position
    </header>
    <div class="panel-body">
        <div class="row" id="section-progress-report-holder">
            <div class="form-horizontal" role="form">
                <div class="form-group">
                    <div class="col-md-2">
                        <g:select class="form-control" id="meritGroupName" name='meritGroupName' tabindex="3"
                                  noSelection="${['': ' General...']}"
                                  from='${com.grailslab.enums.GroupName.values()}'
                                  optionKey="key" optionValue="value"></g:select>
                    </div>
                    <div class="col-md-2">
                        <select name="meritSection" id="meritSection" class="form-control ta-section-step" tabindex="3">
                            <option value="">All Section</option>
                            %{--<g:each in="${sectionExamList}" var="dataSet">
                                <option value="${dataSet.id}">${dataSet.section?.name}</option>
                            </g:each>--}%
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="meritSortBy" id="meritSortBy" class="form-control ta-merit-step" tabindex="3">
                            <option value="rollNo">Roll No </option>
                            <option value="secMeritPos">Section Merit Position</option>
                            <option value="clsMeritPos">Class Merit Position</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <g:select class="form-control ta-exam-type-print" id="meritPrintOptionType" name='meritPrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value" required="required"></g:select>
                    </div>
                    <div class="col-md-2">
                        <button class="btn btn-info" id="merit-print-btn">Print</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>

    var groupName,



</script>