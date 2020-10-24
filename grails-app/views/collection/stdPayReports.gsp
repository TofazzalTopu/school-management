<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Student Pay Reports</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Collection Report"/>

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
                                      optionKey="id" optionValue="name" />
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control" id="studentPaySectionName" name='studentPaySectionName' tabindex="4"
                                      noSelection="${['': 'All Section...']}"
                                      from=''
                                      optionKey="id" optionValue="name" />
                        </div>
                        <div class="form-group">
                            <g:select class="form-control student-pay-report-print" id="studentPayPrintOption" name='studentPayPrintOption'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required" />
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
                2. Student Pay Summary
            </header>
            <div class="panel-body">
                <div class="row" id="student-pay-summary-report-holder">
                    <form class="form-inline" role="form">
                        <div class="form-group" class="data_range">
                            <div class="input-daterange input-group">
                                <span class="input-group-addon">From</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="stdPayStartDate" name="stdPayStartDate"
                                             tabindex="1" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField value="${toDayStr}" class="input-sm form-control picKDate" id="stdPayEndDate" name="stdtPayEndDate"
                                             tabindex="2" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control" id="stdPayClassName" name='stdPayClassName' tabindex="4"
                                      noSelection="${['': 'All Class...']}"
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name" />
                        </div>
                        <div class="form-group">
                            <g:select class=" form-control" id="stdPaySectionName" name='stdPaySectionName' tabindex="4"
                                      noSelection="${['': 'All Section...']}"
                                      from=''
                                      optionKey="id" optionValue="name" />
                        </div>
                        <div class="form-group">
                            <g:select class="form-control student-pay-report-print" id="stdPayPrintOption" name='stdPayPrintOption'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value" required="required" />
                        </div>
                        <button type="button" id="student-pay-summary-report-btn" class="btn btn-primary">Show Report</button>
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
            loadSectionOnClassChange(classNameOnchange,'studentPaySectionName');
        });

        $('#stdPayClassName').on('change', function (e) {
            var classNameOnchange = $('#stdPayClassName').val();
            loadSectionOnClassChange(classNameOnchange,'stdPaySectionName');
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

        $('#student-pay-report-btn').click(function (e) {
            startDate=$('#studentPayStartDate').val();
            endDate=$('#studentPayEndDate').val();
            section = $('#studentPaySectionName').find("option:selected").val();
            className = $('#studentPayClassName').find("option:selected").val();
            reportType = 'withFeeHead';
            printOptionType = $('#studentPayPrintOption').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'studentFeePay','_blank')}?startDate="+startDate+"&endDate="+endDate+"&printOptionType="+printOptionType+"&reportType="+reportType+"&section="+section+"&pClassName="+className;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

        $('#student-pay-summary-report-btn').click(function (e) {
            startDate=$('#stdPayStartDate').val();
            endDate=$('#stdPayEndDate').val();
            section = $('#stdPaySectionName').find("option:selected").val();
            className = $('#stdPayClassName').find("option:selected").val();
            reportType = 'withoutFeeHead';
            printOptionType = $('#stdPayPrintOption').find("option:selected").val();
            if(startDate != "" && endDate !=""){
                var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'studentFeePay','_blank')}?startDate="+startDate+"&endDate="+endDate+"&printOptionType="+printOptionType+"&reportType="+reportType+"&section="+section+"&pClassName="+className;
                window.open(sectionParam);
            }else {
                alert("Both Start date and End date required");
            }
            e.preventDefault();
        });

    });
    function loadSectionOnClassChange(classNameOnchange,id) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#'+id);
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