<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Pc Amount Report
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="pcHrCategory" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-4">
                                <g:select name="pcHrCategory" id="pcHrCategory" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Department']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="employeeId" class="col-md-2 control-label" >Employee Name </label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="employeeId" name="employeeId" tabindex="1"  />
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empSalaryPcPrintOptionType" name='empSalaryPcPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empSalaryPcReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
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

        $('#empSalaryPcReportBtn').click(function (e) {
            e.preventDefault();
            var  hrCategory = $('#pcHrCategory').val();
            var  employee = $("#employeeId").val()
            printOptionType = $('#empSalaryPcPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'pcReport','_blank')}?hrCategory="+hrCategory+"&employee="+employee+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });

    });

</script>


