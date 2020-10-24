<div class="row">
<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
          Employee Attandance  Status Report
        </header>
        <div class="panel-body">
            <div class="row" id="studentAttenReport">
                <div class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Date Range</label>
                        <div class="col-md-6">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${g.formatDate(date: new Date().minus(14), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empAttnCountStartDay" name="empAttnCountStartDay"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empAttnCountEndDay" name="empAttnCountEndDay"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="hrCategoryCount" class="col-md-2 control-label">Department Name</label>
                        <div class="col-md-6">
                            <g:select name="hrCategoryCount" id="hrCategoryCount" class="form-control"
                                      from="${hrCategoryList}"
                                      noSelection="${['': 'All Department']}" optionKey="id"
                                      optionValue="name"/>

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Report Type</label>
                        <div class="col-md-6">
                            <g:select class="form-control" id="empAttnCountPrintOptionType" name='empAttnCountPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" id="empCountReportBtn" class="btn btn-primary">Show Report</button>
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
         $('#empCountReportBtn').click(function (e) {
             e.preventDefault();
             empAttnCountStartDay = $("#empAttnCountStartDay").val();
             empAttnCountEndDay = $("#empAttnCountEndDay").val();
             var  hrCategory = $("#hrCategoryCount").val();
             printOptionType = $('#empAttnCountPrintOptionType').val();
             if(empAttnCountStartDay!='' && empAttnCountEndDay!=''){
                 reportParam ="${g.createLink(controller: 'attendanceReport',action: 'employeeCountReport','_blank')}?rStartDate="+empAttnCountStartDay+"&rEndDate="+empAttnCountEndDay +"&hrCategory="+hrCategory+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
             else{
                 bootbox.alert( {size: 'small', message: "please put Start date and End Date"})
             }


         });

        });

</script>