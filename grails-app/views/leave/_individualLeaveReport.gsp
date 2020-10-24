
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
               Individual Employees Leave Report
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="empLeaveAcademicYear" class="col-md-2 control-label">Year </label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empLeaveAcademicYear" name='empLeaveAcademicYear'
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="leaveEmployeeId" class="col-md-2 control-label" >Employee Name </label>
                            <div class="col-md-4">
                                <input type="hidden" class="form-control" id="leaveEmployeeId" name="leaveEmployeeId" tabindex="1"  />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="empleaveName" class="col-md-2 control-label">Leave Name</label>
                            <div class="col-md-4">
                                <g:select name="empleaveName" id="empleaveName" class="form-control"
                                          from="${leaveList}" noSelection="${['': 'All']}"
                                          optionKey="id" optionValue="name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="leavePrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="leavePrintOptionType" name='leavePrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empLeaveReportBtn" class="btn btn-primary">Show Report</button>
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
        $('#leaveEmployeeId').select2({
            placeholder: "Search for a Employee [employeeId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
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

        $('#empLeaveReportBtn').click(function (e) {
            e.preventDefault();
            var  empLeaveAcademicYear = $('#empLeaveAcademicYear').val();
            var  employee = $("#leaveEmployeeId").val()
            var  leaveName = $("#empleaveName").val()
            var printOptionType = $('#leavePrintOptionType').val();
            if(employee){
            var reportParam = "${g.createLink(controller: 'leaveReport',action: 'byEmployee','_blank')}?year=" + empLeaveAcademicYear + "&employee=" + employee + "&printOptionType=" + printOptionType+"&leaveName="+leaveName;
            window.open(reportParam);
            } else {
                alert("Please Select Employee Name")
            }
            return false;
        });

    });
</script>

</body>
</html>