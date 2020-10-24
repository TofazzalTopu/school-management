<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Attendance Summery Report
            </header>
            <div class="panel-body">
                <div class="row" id="summeryReport">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnsummeryStartDay" name="stdAttnsummeryStartDay"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnsummeryEndDay" name="stdAttnsummeryEndDay"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="reportType" class="col-md-2 control-label"> Report Type</label>
                            <div class="col-md-6">
                                <select name="reportType" id="reportType" class="form-control" tabindex="4">
                                    <option value="classWise">Class Wise Summary</option>
                                    <option value="sectionWise">Section  Wise Summary</option>
                                    <option value="DayWise">Day Wise Summary</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class=" form-control" id="stdAttnsummeryClsName" name='stdAttnsummeryClsName' tabindex="2"
                                          noSelection="${['': 'All Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="stdAttnsummerySecName" id="stdAttnsummerySecName" class="form-control" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="studentsummeryPrintOptionType" name='studentsummeryPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="stdAttnsummeryReportBtn" class="btn btn-primary">Show Report</button>
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
         $('#stdAttnsummeryReportBtn').click(function (e) {
             var flag=true;
             e.preventDefault();
             stdAttnStartDay = $("#stdAttnsummeryStartDay").val()
             stdAttnEndDay = $("#stdAttnsummeryEndDay").val()
             className = $('#stdAttnsummeryClsName').find("option:selected").val();
             section = $('#stdAttnsummerySecName').find("option:selected").val();
             reportType = $('#reportType').find("option:selected").val();
             printOptionType = $('#studentsummeryPrintOptionType').find("option:selected").val();

             if(stdAttnStartDay!='' && stdAttnEndDay!=''){
                 reportParam ="${g.createLink(controller: 'attendanceReport',action: 'studentAttnSummaryReport','_blank')}?rStartDate="+stdAttnStartDay+"&rEndDate="+stdAttnEndDay +"&className="+className+"&sectionName="+section+"&reportType="+reportType+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
             else{
                 bootbox.alert( {size: 'small', message: "please put Start date and End Date"})
             }
         });
     });
</script>