<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title> Applicant List Reports</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Applicant  List  Reports"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Applicant  Report
            </header>
            <div class="panel-body">
                <div class="row" id="studentAttenReport">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="applicantEndDay" class="col-md-2 control-label">Date </label>
                            <div class="col-md-1">

                                <label class="custom-control custom-checkbox">
                                    <input type="checkbox" checked id="allApplicant" class="custom-control-input">
                                    <span class="custom-control-description" >ALL </span>
                                </label>
                            </div>

                            <div class="col-md-5" id="dateRange" style="display: none">
                                <div class="input-daterange input-group">
                                    <g:textField value="${g.formatDate(date: new Date().minus(0), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="applicantStartDay" name="applicantStartDay"
                                                 tabindex="1" placeholder="Start Date"  required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="applicantEndDay" name="applicantEndDay"
                                                 tabindex="2" placeholder="End Date"  required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="applicantClsName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class=" form-control"  id="applicantClsName" name='applicantClsName' tabindex="2"
                                          noSelection="${['': 'All Class']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="applicantType" class="col-md-2 control-label">Applicant Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="applicantType" name='applicantType'
                                          from='${com.grailslab.enums.ApplicantStatus.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="applicantReportType" class="col-md-2 control-label">Report type</label>
                            <div class="col-md-6">
                                <select name="applicantReportType" id="applicantReportType" class="form-control" tabindex="4">
                                    <option value="short">Short</option>
                                    <option value="listWithBroSis">List with Brother or Sister</option>
                                    <option value="details">Details</option>
                                    <option value="applyCount">Number Of Student</option>
                                </select>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="applicantPrintOptionType" class="col-md-2 control-label">Print</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="applicantPrintOptionType" name='applicantPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="applicantReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

</div>



<script>
    var reportParam, startDate,endDate,academicYear,className, applicantStatus,student,printOptionType,discount,reportSortType,reportType,applicantStartDay, reportType,applicantEndDay,applicantStartDay;

    jQuery(function ($) {
        $('#allApplicant').on('change', function(){
            if(this.checked){
                $("#dateRange").hide();

            } else {
                $("#dateRange").show();
            }
        });

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

        $('#applicantReportBtn').click(function (e) {
            e.preventDefault();
            applicantStartDay = $("#applicantStartDay").val();
            applicantEndDay = $("#applicantEndDay").val();
            className = $('#applicantClsName').val();

            applicantStatus = $('#applicantType').val();
            var   reportType = $('#applicantReportType').val();

            printOptionType = $('#applicantPrintOptionType').val();

            var checkedValue = $('#allApplicant:checked').val();
            var isValidDate = applicantStartDay.replace('-','/') > applicantEndDay.replace('-','/');
            if (!checkedValue && isValidDate == true) {
                bootbox.alert( {size: 'small', message: "Please select a valid date range"});
                return false;
            }
            reportParam ="${g.createLink(controller: 'onlineRegistrationReport',action: 'applicantFormReport','_blank')}?rStartDate="+applicantStartDay+"&rEndDate="+applicantEndDay +"&className="+className+"&applicantStatus="+applicantStatus+"&reportType="+reportType+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });

    });







</script>


</body>
</html>