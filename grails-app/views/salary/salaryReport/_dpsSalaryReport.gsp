<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                DPS  Salary   Reports
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">


                        <div class="form-group">
                            <label for="hrCategorySalaryDps" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-6">
                                <g:select name="hrCategorySalaryDps" id="hrCategorySalaryDps" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Catagory']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="employeeIddps" class="col-md-2 control-label" >Employee Name </label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="employeeIddps" name="employeeIddps" tabindex="1"  />
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="empSalaryDpsPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empSalaryDpsPrintOptionType" name='empSalaryDpsPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empSalaryDpsReportBtn" class="btn btn-primary">Show Report</button>
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

        $('#employeeIddps').select2({
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
        $('#empSalaryDpsReportBtn').click(function (e) {
            e.preventDefault();
            var  hrCategory = $('#hrCategorySalaryDps').val();
            var  employee = $("#employeeIddps").val()
            printOptionType = $('#empSalaryDpsPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'dps','_blank')}?hrCategory="+hrCategory+"&employee="+employee+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });








    });



</script>