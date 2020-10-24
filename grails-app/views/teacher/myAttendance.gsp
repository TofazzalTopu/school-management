<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>My Attendance</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="My Attendance"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                My Attendance Report
            </header>
            <div class="panel-body">
                <div class="row" id="individualReport">
                    <div class="form-horizontal" role="form">
                        <g:hiddenField name="employee" id="employee" value="${employeeId}"/>
                        <div class="form-group">
                            <label for="" class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date:new Date().minus(14),format:'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="employeeAttnIndiStartDay" name="employeeAttnIndiStartDay"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date:new Date(), format:'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="employeeAttnIndiEndDay" name="employeeAttnIndiEndDay"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="attndanceStatus" class="col-md-2 control-label">Attendance Type</label>
                            <div class="col-md-6">
                                <select name="attndanceStatus" id="attndanceStatus" class="form-control" tabindex="4">
                                    <option value="all">All</option>
                                    <option value="present">Present</option>
                                    <option value="absent">Absent</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="employeeAttnIndiPrintOptionType" name='employeeAttnIndiPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empAttnIndiBtn" class="btn btn-primary">Show Report</button>
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
        $('#empAttnIndiBtn').click(function (e) {
            e.preventDefault();
            var empAttnStartDay = $("#employeeAttnIndiStartDay").val();
            var empAttnEndDay = $("#employeeAttnIndiEndDay").val();
            var employee = $("#employee").val()
            var attndanceStatus = $('#attndanceStatus').find("option:selected").val();
            var printOptionType = $('#employeeAttnIndiPrintOptionType').find("option:selected").val();
            if (empAttnStartDay == '' && empAttnEndDay == '' || empAttnStartDay != '' && empAttnEndDay == '' || empAttnStartDay == '' && empAttnEndDay != '') {
                bootbox.alert({size: 'small', message: "please put Start date and End Date"})
            }
            if (employee == '') {
                bootbox.alert({size: 'small', message: "please put  employee"})
            }
            if (empAttnStartDay != '' && empAttnEndDay != '' && employee != '') {
                var reportParam = "${g.createLink(controller: 'attendanceReport',action: 'employeeAttnIndividualReport','_blank')}?rStartDate=" + empAttnStartDay + "&rEndDate=" + empAttnEndDay + "&employee=" + employee + "&attndanceStatus=" + attndanceStatus + "&printOptionType=" + printOptionType;
                window.open(reportParam);
            }
        });
    });
</script>


</body>
</html>