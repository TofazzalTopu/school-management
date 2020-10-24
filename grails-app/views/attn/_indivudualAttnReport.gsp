<div class="row">
<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Student Attendance Report
        </header>
        <div class="panel-body">
            <div class="row" id="individualReport">
                <div class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Date Range</label>
                        <div class="col-md-6">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${g.formatDate(date:new Date().minus(14),format:'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnIndiStartDay" name="stdAttnIndiStartDay"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${g.formatDate(date:new Date(), format:'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnIndiEndDay" name="stdAttnIndiEndDay"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="" class="col-md-2 control-label">Student Name</label>
                        <div class="col-md-6">
                            <input type="hidden" class="form-control" id="student" name="student" tabindex="5" />
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
                        <label for="" class="col-md-2 control-label">Reprort Type</label>
                        <div class="col-md-6">
                            <g:select class="form-control" id="stdAttnIndiPrintOptionType" name='stdAttnIndiPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" id="stdAttnIndiBtn" class="btn btn-primary">Show Report</button>
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

         $('#stdAttnIndiBtn').click(function (e) {
             e.preventDefault();
             stdAttnStartDay = $("#stdAttnIndiStartDay").val()
             stdAttnEndDay = $("#stdAttnIndiEndDay").val()
             student = $("#student").val()
             studentStatus = $('#attndanceStatus').find("option:selected").val();
             printOptionType = $('#stdAttnIndiPrintOptionType').find("option:selected").val();
             if( stdAttnStartDay=='' && stdAttnEndDay=='' || stdAttnStartDay!='' && stdAttnEndDay=='' || stdAttnStartDay=='' && stdAttnEndDay!='' ) {bootbox.alert({size: 'small', message: "please put Start date and End Date"})}
             if(student==''){ bootbox.alert( {size: 'small', message: "please put  student"})}
             if(stdAttnStartDay!='' && stdAttnEndDay!='' &&student!=''){reportParam ="${g.createLink(controller: 'attendanceReport',action: 'studentAttnIndividualReport','_blank')}?rStartDate="+stdAttnStartDay+"&rEndDate="+stdAttnEndDay +"&student="+student+"&attndanceStatus="+studentStatus+"&printOptionType="+printOptionType;
                 window.open(reportParam);}
         });

         $('#student').select2({
             placeholder: "Search for a Student [studentId/name/Father Name/mobile",
             allowClear: true,
             minimumInputLength:3,
             ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                 url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
                 dataType: 'json',
                 quietMillis: 250,
                 data: function (term, page) {
                     return {
                         q: term, // search term
                     };
                 },
                 results: function (data, page) { // parse the results into the format expected by Select2.
                     // since we are using custom formatting functions we do not need to alter the remote JSON data
                     return { results: data.items };
                 },
                 cache: true
             },
             formatResult: repoFormatResult, // omitted for brevity, see the source of this page
             formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
             dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
             escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
         });


     });

         function repoFormatResult(repo) {
             var markup = '<div class="row-fluid">' +
                     '<div class="span12">' + repo.name + '</div>' +
                     '</div>';
             return markup;
         }

         function repoFormatSelection(repo) {
             return repo.name;
         }

  </script>



