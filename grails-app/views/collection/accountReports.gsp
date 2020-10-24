<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Collection Reports</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Collection Report"/>
<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                1. Collection Summary
            </header>
            <div class="panel-body">
                <div class="row" id="collectionSummary-report-holder">
                    <form class="form-inline" role="form">
                        <div class="form-group" class="data_range">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="startDateCollection" name="startDate"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="endDateCollection" name="endDate"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <select name="reportSortType" id="reportSortTypeCollection" class="form-control" required="required">
                                <option value="byDate">By Fee</option>
                                <option value="byClassName">By Class</option>
                            </select>

                        </div>
                        <div class="form-group">
                            <g:select class="form-control collections-byhead-report-print" id="collectionsByBeadPrintOptionTypeCollection" name='collectionsByBeadPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required"/>
                        </div>

                        <button type="button" id="collections-report-btn" class="btn btn-primary">Show Report</button>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                2. Student Pay Report
            </header>
            <div class="panel-body">
                <div class="row" id="student-pay-report-holder">
                    <form class="form-inline" role="form">
                        <div class="form-group" class="data_range">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="studentPayStartDate" name="studentPayStartDate"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="studentPayEndDate" name="studentPayEndDate"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control" id="studentPayClassName" name='studentPayClassName' tabindex="4"
                                      noSelection="${['': 'All Class...']}"
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"/>
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control" id="studentPaySectionName" name='studentPaySectionName' tabindex="4"
                                      noSelection="${['': 'All Section...']}"
                                      from=''
                                      optionKey="id" optionValue="name"/>
                        </div>
                        <div class="form-group">
                            <select name="studentPayReportType" id="studentPayReportType" class="form-control" required="required">
                                <option value="withFeeHead">With Fee Head</option>
                                <option value="withoutFeeHead">Without Fee Head</option>
                            </select>

                        </div>
                        <div class="form-group">
                            <g:select class="form-control student-pay-report-print" id="studentPayPrintOption" name='studentPayPrintOption'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required"/>
                        </div>
                        <button type="button" id="student-pay-report-btn" class="btn btn-primary">Show Report</button>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                4. Invoice Summary
            </header>
            <div class="panel-body">
                <div class="row" id="invoiceSummary-report-holder">
                    <form class="form-inline" role="form">
                        <div class="form-group" class="data_range">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="startDateInvoice" name="startDate"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="endDateInvoice" name="endDate"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control collections-byhead-class-step" id="byheadClassNameInvoice" name='byheadClassName' tabindex="4"
                                      noSelection="${['': 'All Class...']}"
                                      from='${ classNameList}'
                                      optionKey="id" optionValue="name"/>
                        </div>
                        <div class="form-group">
                            <select name="reportSortType" id="reportSortTypeInvoice" class="form-control" required="required">
                                <option value="byDate">By Date</option>
                                <option value="byClassName">By Class</option>
                            </select>

                        </div>
                        <div class="form-group">
                            <g:select class="form-control collections-byhead-report-print" id="collectionsByBeadPrintOptionTypeInvoice" name='collectionsByBeadPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required"/>
                        </div>
                        <div class="form-group">
                            <g:select class="form-control collections-byhead-report-discount" id="collectionsByHeaddiscountInvoice" name='collectionsByHeaddiscount'
                                      from='${['with':'With Discount','without':'Without Discount']}'
                                      optionKey="key" optionValue="value" required="required"/>
                        </div>
                        <button type="button" id="collections-byhead-report-invoice-btn" class="btn btn-primary">Show</button>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var startDate,endDate,academicYear,className, examTerm,section,student,printOptionType,discount,reportSortType,reportType;
    jQuery(function ($) {

        $('#studentPayClassName').on('change', function (e) {
            var classNameOnchange = $('#studentPayClassName').val();
            loadSectionOnClassChange(classNameOnchange);
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

        $('#collections-byhead-report-btn').click(function (e) {
            startDate=$('#startDate').val();
            endDate=$('#endDate').val();
            className =$('#byheadClassName').find("option:selected").val();
            discount =$('#collectionsByHeaddiscount').find("option:selected").val();
            printOptionType = $('#collectionsByBeadPrintOptionType').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'collectionByHead','_blank')}?startDate="+startDate+"&endDate="+endDate+"&pClassName="+className+"&printOptionType="+printOptionType+"&discount="+discount;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

        $('#collections-byhead-report-invoice-btn').click(function (e) {
            startDate=$('#startDateInvoice').val();
            endDate=$('#endDateInvoice').val();
            className =$('#byheadClassNameInvoice').find("option:selected").val();
            discount =$('#collectionsByHeaddiscountInvoice').find("option:selected").val();
            reportSortType = $('#reportSortTypeInvoice').find("option:selected").val();
            printOptionType = $('#collectionsByBeadPrintOptionTypeInvoice').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'invoiceSummary','_blank')}?startDate="+startDate+"&endDate="+endDate+"&pClassName="+className+"&printOptionType="+printOptionType+"&discount="+discount+"&reportSortType="+reportSortType;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

        $('#collections-report-btn').click(function (e) {
            startDate=$('#startDateCollection').val();
            endDate=$('#endDateCollection').val();
            reportSortType = $('#reportSortTypeCollection').find("option:selected").val();
            printOptionType = $('#collectionsByBeadPrintOptionTypeCollection').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'feeCollectionSummaryByClass','_blank')}?startDate="+startDate+"&endDate="+endDate+"&printOptionType="+printOptionType+"&reportSortType="+reportSortType;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

        $('#student-pay-report-btn').click(function (e) {
            startDate=$('#studentPayStartDate').val();
            endDate=$('#studentPayEndDate').val();
            section = $('#studentPaySectionName').find("option:selected").val();
            className = $('#studentPayClassName').find("option:selected").val();
            reportType = $('#studentPayReportType').find("option:selected").val();
            printOptionType = $('#studentPayPrintOption').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'studentFeePay','_blank')}?startDate="+startDate+"&endDate="+endDate+"&printOptionType="+printOptionType+"&reportType="+reportType+"&section="+section+"&pClassName="+className;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

    });
    function loadSectionOnClassChange(classNameOnchange) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#studentPaySectionName');
                    $select.find('option').remove();
                    $select.append('<option value="">All Section</option>');
                    $.each(data.sectionList, function (key, value) {
                        $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>
</body>
</html>