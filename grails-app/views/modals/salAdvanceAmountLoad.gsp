<div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="objId" id="objId"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Advance Amount Setup </h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="employeeId" class="col-md-3 control-label"  >Employee Name </label>
                            <div class="col-md-7">
                                    <input type="hidden" class="form-control" id="employeeId" name="employeeId" tabindex="5"  />
                                </div>
                        </div>

                        <div class="form-group" id="outStandingAmountHolder" style="display: none;">
                            <label for="outStandingAmount" class="col-md-3 control-label">OutStandingAmount</label>
                            <div class="col-md-4">
                                <input class="form-control" type="text" name="outStandingAmount"  id="outStandingAmount" placeholder="OutStanding Amount"
                                       tabindex="2"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="currentAmount" class="col-md-3 control-label">New LoanAmount</label>
                            <div class="col-md-7">
                                <input class="form-control" type="text" name="currentAmount"  id="currentAmount" placeholder="Loan Amount"
                                       tabindex="2"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="installmentAmount" class="col-md-3 control-label">MonthlyInstallment</label>
                            <div class="col-md-7">
                                <input class="form-control" type="text" name="installmentAmount" id="installmentAmount" placeholder="Installment Amount"
                                       tabindex="2"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="description" class="col-md-3 control-label">Description</label>
                            <div class="col-md-7">
                                <textarea id="description" name="description" class="form-control"  placeholder="Description"
                                          tabindex="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="advanceDate" class="col-md-3 control-label">Date</label>
                            <div class="col-md-7">

                                <input class="form-control" type="text"   name="advanceDate"  id="advanceDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                       tabindex="2"/>
                            </div>
                        </div>
                    </div>

                    </div>
                    <div class="modal-footer">
                        <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="button" class="btn btn-primary" id="advanceAmountbtn">Save</button>
                    </div>
            </form>
        </div>
    </div>


    <script>
        jQuery(function ($) {
        $('#employeeId').select2({
            placeholder: "Search for a Employee [employeeId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
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

            $('#advanceDate').datepicker({
                format: 'dd/mm/yyyy',
                endDate:'today',
                autoclose: true
            });
        });

        $('#adamount').numeric();

    </script>


