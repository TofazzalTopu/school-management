<div class="modal-dialog">
    <div class="modal-content">
        <form class="form-horizontal" role="form" id="createFormModal">
            <g:hiddenField name="id" id="id"/>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Salary Area Setup</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="form-group">
                        <label for="academicYear" class="col-md-3 control-label">Year</label>
                        <div class="col-md-7">
                            <g:select class="form-control" id="academicYear" name='academicYear'
                                      from='${com.grailslab.gschoolcore.AcademicYear.schoolYears()}'
                                      optionKey="key" optionValue="value"></g:select>

                        </div>

                    </div>
                    <div class="form-group">
                     <label for="yearMonths" class="col-md-3 control-label">Month</label>
                        <div class="col-md-7">
                            <g:select class="form-control" id="yearMonths" name='yearMonths'
                                      from='${com.grailslab.enums.YearMonths.values()}'
                                      optionKey="key" optionValue="value"></g:select>

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="employee" class="col-md-3 control-label">Employee</label>
                        <div class="col-md-7">
                            <input type="hidden" class="form-control" id="employee" name="employee" tabindex="2" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="amount" class="col-md-3 control-label">Amount</label>
                        <div class="col-md-7">
                            <input class="form-control" type="text" name="amount" id="amount" placeholder="Amount"
                                   tabindex="2"/>
                            </div>
                    </div>
                 </div>
            </div>
            <div class="modal-footer">
                <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                <button type="button" class="btn btn-primary" id="areaSetupBtn">Save</button>
            </div>
        </form>
    </div>
</div>
<script>
    jQuery( function($){
        $('#employee').select2({
            placeholder: "Search for a Employee [employeeId/name/designation]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
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