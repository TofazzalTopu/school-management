<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleAttandanceLayout"/>
    <title> Employee Attendance Reports</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Attendance Reports"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Daily Attendance Report
            </header>
            <div class="panel-body">
                <div class="row" id="studentAttenReport">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="empDailyAttnEndDay" class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date: new Date().minus(0), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empDailyAttnStartDay" name="empDailyAttnStartDay"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empDailyAttnEndDay" name="empDailyAttnEndDay"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="hrCategoryDailyAttn" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-6">
                                <g:select name="hrCategoryDailyAttn" id="hrCategoryDailyAttn" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Department']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="empAttnPresentStatus" class="col-md-2 control-label">Attendance Type</label>
                            <div class="col-md-6">
                                <select name="empAttnPresentStatus" id="empAttnPresentStatus" class="form-control" tabindex="4">
                                    <option value="all">All</option>
                                    <option value="present">Present</option>
                                    <option value="absent">Absent</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="empAttnPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="empAttnPrintOptionType" name='empAttnPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empAttnReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

</div>

<g:render template='/attn/employee/individualAttnEmployeeReport'/>
<g:render template='/attn/employee/attnEmployeeReport'/>
<g:render template='/attn/employee/attnEmployeeCountReport'/>

<script>
    var reportParam, startDate,endDate,academicYear,printOptionType,discount,reportSortType,reportType,empDailyAttnStartDay, reportType,empDailyAttnEndDay;

    jQuery(function ($) {

        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });
        $('.picKDate').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $('#empAttnReportBtn').click(function (e) {
            e.preventDefault();
            empDailyAttnStartDay = $("#empDailyAttnStartDay").val();
            empDailyAttnEndDay = $("#empDailyAttnEndDay").val();

            var  hrCategory = $('#hrCategoryDailyAttn').val();
            var   employeeStatus = $('#empAttnPresentStatus').val();
          
            printOptionType = $('#empAttnPrintOptionType').val();
            var isValidDate = empDailyAttnStartDay.replace('-','/') > empDailyAttnEndDay.replace('-','/');
            if (isValidDate == true) {
                bootbox.alert( {size: 'small', message: "Please select a valid date range"});
                return false;
            }
          
            reportParam ="${g.createLink(controller: 'attendanceReport',action: 'employeeAttnReport','_blank')}?rStartDate="+empDailyAttnStartDay+"&rEndDate="+empDailyAttnEndDay +"&hrCategory="+hrCategory+"&attndanceStatus="+employeeStatus+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });

    });
</script>


</body>
</html>