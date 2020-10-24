<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Employee List Report
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="empSalaryPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empSalaryPrintOptionType" name='empSalaryPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>

                            <div class="col-md-4">
                                <button type="button" id="employee-list-report-btn" class="btn btn-primary">Show Report</button>
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
        $('#employee-list-report-btn').click(function (e) {
            e.preventDefault();
            printOptionType = $('#empSalaryPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'employeeList','_blank')}?printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });
    });
</script>







