<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Salary Setup
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="hrCategorySalarySetUp" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-6">
                                <g:select name="hrCategorySalarySetUp" id="hrCategorySalarySetUp" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Catagory']}" optionKey="id"
                                          optionValue="name"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="empSalaryPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empSalaryPrintOptionType" name='empSalaryPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empSalarySetReportBtn" class="btn btn-primary">Show Report</button>
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
        $('#empSalarySetReportBtn').click(function (e) {
            e.preventDefault();
            var  hrCategory = $('#hrCategorySalarySetUp').val();
            printOptionType = $('#empSalaryPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'setUp','_blank')}?hrCategory="+hrCategory+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });
    });
</script>







