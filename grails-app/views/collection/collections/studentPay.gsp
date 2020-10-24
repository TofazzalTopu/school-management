<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Student Pay/Due</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student Pay/Due Status" SHOW_PRINT_BTN="YES"/>
<div class="row" id="student-pay-status-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <g:if test='${flash.message}'>
                        <span class="alert alert-danger">${flash.message}</span>
                </g:if>
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-2">
                            <g:select class="form-control" id="academicYear" name='academicYear' tabindex="1"
                                      from='${academicYearList}' value="${workingYear?.key}"
                                      optionKey="key" optionValue="value"></g:select>

                        </div>
                        <div class="col-md-2">
                            <select name="payStatus" id="payStatus" class="form-control" tabindex="1">
                                <option value="paid">Paid</option>
                                <option value="due">Due</option>
                            </select>
                        </div>
                        <div class="col-md-5">
                            <input type="hidden" class="form-control" id="student" name="student" tabindex="2" />
                        </div>

                        <div class="col-md-1">
                            <button class="btn btn-primary" type="button" id="student-pay-status-btn">Load Fee(s)</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body">
                <div class="col-md-12" id="student-pay-list-show-holder" style="display: none;">
                    <div class="table-responsive">
                        <table class="table tree table-bordered table-striped table-condensed" id="pay-list-table">
                            <thead>
                            <tr>
                                <th class="col-md-3">Name</th>
                                <th class="col-md-1">Amount</th>
                                <th class="col-md-1">Discount(%)</th>
                                <th class="col-md-1">Payable</th>
                                <th class="col-md-1">Quantity</th>
                                <th class="text-right col-md-1">Payment</th>
                                <th class="text-center col-md-2">INV No</th>
                                <th class="text-center col-md-2">Date</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div class="col-md-12" id="student-due-list-show-holder" style="display: none;">
                    <div class="table-responsive">
                        <table class="table tree table-bordered table-striped table-condensed" id="due-list-table">
                            <thead>
                            <tr>
                                <th class="col-md-3">Name</th>
                                <th class="col-md-3">Amount</th>
                                <th class="col-md-3">Discount(%)</th>
                                <th class="col-md-3">Net Payable</th>
                            </tr>
                            </thead>
                        </table>
                    </div>

                </div>
            </div>
        </section>
    </div>
</div>
<script>
    var payStatus, student, academicYear;
    jQuery(function ($) {
        $('#payStatus').select2();

        $('#student-pay-status-btn').click(function (e) {
            academicYear = $('#academicYear').val();
            student = $('#student').val();
            payStatus = $('#payStatus').val();
            if(student != ""){
                $("#pay-list-table").find("tr:gt(0)").empty();
                $("#due-list-table").find("tr:gt(0)").empty();
                $("#student-pay-list-show-holder").hide(500);
                $("#student-due-list-show-holder").hide(500);
                showLoading("#student-pay-status-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType:'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'studentPayResponse')}?payStatus="+payStatus+"&student="+student+"&academicYear="+academicYear,
                    success: function (data) {
                        hideLoading("#student-pay-status-holder");
                        if(data.isError==true){
                            showErrorMsg(data.message);
                        }else {
                            if(data.payStatus==='paid'){
                                $('#pay-list-table').append('<tbody>');
                                $.each(data.feeItemList,function(key, value)
                                {
                                    if(value.hasItemDetail==true){
                                        $('#pay-list-table').append('<tr><td colspan="8" ">'+value.item+'</td></tr>');
                                        $.each(value.itemDetails,function(ikey, iValue)
                                        {
                                            $('#pay-list-table').append('<tr><td>'+iValue.item+'</td><td>'+iValue.amount+'</td><td>'+iValue.discount+'%</td><td>'+iValue.netPayment+'</td><td>'+iValue.quantity+'</td><td>'+iValue.totalPayment+'</td><td>'+iValue.invoiceNo+'</td><td>'+iValue.invoiceDate+'</td></tr>');
                                        });
                                        $('#pay-list-table').append('<tr><td colspan="8" "></td></tr>');
                                    }else{
                                        $('#pay-list-table').append('<tr><td>'+value.item+'</td><td>'+value.amount+'</td><td>'+value.discount+'%</td><td>'+value.netPayment+'</td><td>'+value.quantity+'</td><td>'+value.totalPayment+'</td><td>'+value.invoiceNo+'</td><td>'+value.invoiceDate+'</td></tr>');
                                    }
                                });
                                $('#pay-list-table').append('</tbody>');
                                $("#student-pay-list-show-holder").show(500);
                            }else {
                                $('#due-list-table').append('<tbody>');
                                $.each(data.feeItemList,function(key, value)
                                {
                                    if(value.hasItemDetail==true){
                                        $('#due-list-table').append('<tr><td colspan="4" ">'+value.item+'</td></tr>');
                                        $.each(value.itemDetails,function(ikey, iValue)
                                        {
                                            $('#due-list-table').append('<tr><td>'+iValue.item+'</td><td>'+iValue.amount+'</td><td>'+iValue.discount+'%</td><td>'+iValue.netPayment+'</td></tr>');
                                        });
                                        $('#due-list-table').append('<tr><td colspan="4" "></td></tr>');
                                    }else{
                                        $('#due-list-table').append('<tr><td>'+value.item+'</td><td>'+value.amount+'</td><td>'+value.discount+'%</td><td>'+value.netPayment+'</td></tr>');
                                    }
                                });
                                $('#due-list-table').append('</tbody>');
                                $("#student-due-list-show-holder").show(500);
                            }
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }else{
                alert("Please Select Student to view report")
            }
            e.preventDefault();
        });

        $('#student').select2({
            placeholder: "Search for a Student [studentId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadListWithStdId')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
                        academicYear: $('#academicYear').val(), // search term
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
        function repoFormatResult(repo) {
            var markup = '<div class="row-fluid">' +
                    '<div class="span12">' + repo.name + '</div>' +
                    '</div>';
            return markup;
        }

        function repoFormatSelection(repo) {
            return repo.name;
        }
    });

</script>
</body>
</html>
</html>