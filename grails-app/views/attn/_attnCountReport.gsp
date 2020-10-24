<div class="row">
<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
          Attendance Status Report
        </header>
        <div class="panel-body">
            <div class="row" id="studentAttenReport">
                <div class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Date Range</label>
                        <div class="col-md-6">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${g.formatDate(date: new Date().minus(14), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnCountStartDay" name="stdAttnCountStartDay"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnCountEndDay" name="stdAttnCountEndDay"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Class Name</label>
                        <div class="col-md-6">
                            <g:select class=" form-control" id="stdAttnCountClsName" name='stdAttnCountClsName' tabindex="2"
                                      noSelection="${['': 'Select Class...']}"
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Section Name</label>
                        <div class="col-md-6">
                            <select name="stdCountSecName" id="stdCountSecName" class="form-control" tabindex="3">
                                <option value="">All Section</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Report Type</label>
                        <div class="col-md-6">
                            <g:select class="form-control" id="stdAttnCountPrintOptionType" name='stdAttnCountPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" id="stdCountReportBtn" class="btn btn-primary">Show Report</button>
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
         $('#stdCountReportBtn').click(function (e) {
             var flag=true;
             e.preventDefault();
             stdAttnCountStartDay = $("#stdAttnCountStartDay").val()
             stdAttnCountEndDay = $("#stdAttnCountEndDay").val()
             className = $('#stdAttnCountClsName').find("option:selected").val();
             section = $('#stdCountSecName').find("option:selected").val();
             studentStatus = $('#stdAttnPresentStatus').find("option:selected").val();
             printOptionType = $('#stdAttnPrintOptionType').find("option:selected").val();
             if(stdAttnCountStartDay.replace('-','/') > stdAttnCountEndDay.replace('-','/') && className.length ==0){
                 bootbox.alert( {size: 'small', message: "please select className"})
                 flag=false
             }

             else if(stdAttnCountStartDay!='' && stdAttnCountEndDay!='' && flag){
                 reportParam ="${g.createLink(controller: 'attendanceReport',action: 'studentCountReport','_blank')}?rStartDate="+stdAttnCountStartDay+"&rEndDate="+stdAttnCountEndDay +"&className="+className+"&sectionName="+section+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
             else{
                 bootbox.alert( {size: 'small', message: "Please put Start date and End Date"})
             }
         });

        });

</script>