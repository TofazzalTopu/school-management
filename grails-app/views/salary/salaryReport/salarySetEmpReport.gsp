<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleHRLayout"/>
    <title>Salary  Reports</title>
    <style>
    .form-group {
        margin-bottom: 5px;
    }
    </style>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Salary Reports "/>

<g:render template='/salary/salaryReport/employeeReport'/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Monthly Salary Reports
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="msAcademicYear" class="col-md-2 control-label">Year </label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="msAcademicYear" name='msAcademicYear'
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="msYearmonth" class="col-md-2 control-label">Month</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="msYearmonth" name='msYearmonth'
                                          from='${com.grailslab.enums.YearMonths.values()}'
                                          optionKey="key" optionValue="value"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="msHrCategory" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-4">
                                <g:select name="msHrCategory" id="msHrCategory" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Department']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="msReportName" class="col-md-2 control-label">Report Name</label>
                            <div class="col-md-4">
                                <select class="form-control" id="msReportName" name="msReportName">
                                    <option value="ss">Salary Sheet</option>
                                    <option value="bs">Bank Statement</option>
                                    <option value="pf">PF Statement</option>
                                    <option value="paySlip">Pay Slip</option>
                                    <option value="area">Area</option>
                                    <option value="xc">Extra Class</option>
                                    <option value="increment">Increment</option>
                                    <option value="attn">Attendance</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="msPrintOption" class="col-md-2 control-label">Print Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="msPrintOption" name='msPrintOption'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="msShowReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

</div>

<g:render template='/salary/salaryReport/salaryReport'/>
<g:render template='/salary/salaryReport/advanceSalaryReport'/>
<g:render template='/salary/salaryReport/dpsSalaryReport'/>
<g:render template='/salary/salaryReport/salaryPcReport'/>


<script>
    var reportParam, startDate,endDate,academicYear,printOptionType,discount,reportSortType,reportType,empDailyAttnStartDay, reportType,empDailyAttnEndDay;
    jQuery(function ($) {
        $('#msShowReportBtn').click(function (e) {
            e.preventDefault();
            var hrCategory = $('#msHrCategory').val();
            var academicYear = $('#msAcademicYear').val();
            var yearMonths = $('#msYearmonth').val();
            var reportName = $('#msReportName').val();
            var printOptionType = $('#msPrintOption').val();
            reportParam ="${g.createLink(controller: 'salaryReport',action: 'salReports','_blank')}?academicYear="+academicYear+"&yearMonths="+yearMonths+"&hrCategory="+hrCategory+"&printOptionType="+printOptionType+"&reportName="+reportName;
            window.open(reportParam);
            return false;
        });

    });
</script>

</body>
</html>