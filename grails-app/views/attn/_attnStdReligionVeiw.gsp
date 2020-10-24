<div class="row">
<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Student Religion  Summery Report
        </header>
        <div class="panel-body">
            <div class="row" id="studentAttenReport">
                <div class="form-horizontal" role="form">
                <div class="form-group">
                        <label for="" class="col-md-2 control-label">Attendance Category</label>
                        <div class="col-md-6">
                            <select name="stdAttnCategory2" id="stdAttnCategory2" class="form-control" tabindex="4">
                                <option value="primary">Primary</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Date Range</label>
                        <div class="col-md-6">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${g.formatDate(date: new Date().minus(30), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnBoardStartDay2" name="stdAttnBoardStartDay2"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnBoardEndDay2" name="stdAttnBoardEndDay2"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                    </div>
                 <div class="form-group">
                        <label for="" class="col-md-2 control-label">Reprort Type</label>
                        <div class="col-md-6">
                            <g:select class="form-control" id="stdAttnBoardPrintOptionType2" name='stdAttnBoardPrintOptionType2'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" id="stdAttnBoardReportBtn2" class="btn btn-primary">Show Report</button>
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
         $('#stdAttnBoardReportBtn2').click(function (e) {
             e.preventDefault();
             stdAttnStartDay = $("#stdAttnBoardStartDay2").val();
             stdAttnEndDay = $("#stdAttnBoardEndDay2").val();
             stdAttnCategory2 = $('#stdAttnCategory2').val();
             printOptionType = $('#stdAttnBoardPrintOptionType2').find("option:selected").val();
              if(stdAttnStartDay!='' && stdAttnEndDay!=''){
                 reportParam ="${g.createLink(controller: 'attendanceReport',action: 'studentAttnReligion','_blank')}?rStartDate="+stdAttnStartDay+"&rEndDate="+stdAttnEndDay +"&stdAttnCategory2="+stdAttnCategory2+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
             else{
                 bootbox.alert( {size: 'small', message: "Please put Start date and End Date"})
             }
         });

        });

</script>