<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Summery Report
            </header>
            <div class="panel-body">
                <div class="row" id="summeryReport">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="empAttnsummeryEndDay" class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empAttnsummeryStartDay" name="empAttnsummeryStartDay"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="empAttnsummeryEndDay" name="empAttnsummeryEndDay"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="hrCategoryAtnSummery" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-6">
                                <g:select name="hrCategoryAtnSummery"  id ='hrCategoryAtnSummery' class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Department']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>


                        <div class="form-group">
                            <label for="employeesummeryPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="employeesummeryPrintOptionType" name='employeesummeryPrintOptionType'
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
             empAttnsummeryStartDay = $("#empAttnsummeryStartDay").val()
             empAttnsummeryEndDay = $("#empAttnsummeryEndDay").val()
             var  hrCategory = $('#hrCategoryAtnSummery').val();
             printOptionType = $('#employeesummeryPrintOptionType').find("option:selected").val();

             if(empAttnsummeryStartDay!='' && empAttnsummeryEndDay!=''){
                 reportParam ="${g.createLink(controller: 'attendanceReport',action: 'employeeAttnSummeryReport','_blank')}?rStartDate="+empAttnsummeryStartDay+"&rEndDate="+empAttnsummeryEndDay +"&hrCategory="+hrCategory+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
             else{
                 bootbox.alert( {size: 'small', message: "please put Start date and End Date"})
             }
         });


     });



</script>