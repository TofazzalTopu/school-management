<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Advance Salary   Reports
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">
                     <div class="form-group">
                            <label for="hrCategorySalaryAdvance" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-6">
                                <g:select name="hrCategorySalaryAdvance" id="hrCategorySalaryAdvance" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Catagory']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="employeeIdAdvance" class="col-md-2 control-label" >Employee Name </label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="employeeIdAdvance" name="employeeIdAdvance" tabindex="1"  />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="empAdvancePaymentStatus" class="col-md-2 control-label">Payment Status</label>
                            <div class="col-md-4">
                                <select class="form-control" id="empAdvancePaymentStatus" name="empAdvancePaymentStatus">
                                    <option value="DUE">Due</option>
                                    <option value="PAID">Paid</option>
                                    <option value="ALL">All</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="empAdvancePrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empAdvancePrintOptionType" name='empAdvancePrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empAdvanceSetReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

</div>

<script>
    var reportParam, startDate,endDate,academicYear,printOptionType,discount,reportSortType,reportType,empDailyAttnStartDay, reportType,empDailyAttnEndDay;

    jQuery(function ($) {
        $('#employeeIdAdvance').select2({
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

        $('#empAdvanceSetReportBtn').click(function (e) {
            e.preventDefault();
            var  hrCategory = $('#hrCategorySalaryAdvance').val();
            var  employee = $('#employeeIdAdvance').val();
            var  advancePaymentStatus = $('#empAdvancePaymentStatus').val();
            printOptionType = $('#empAdvancePrintOptionType').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'advance','_blank')}?hrCategory="+hrCategory+"&employee="+employee+"&printOptionType="+printOptionType+"&advancePaymentStatus="+advancePaymentStatus;
            window.open(reportParam);
            return false;
        });
    });

</script>