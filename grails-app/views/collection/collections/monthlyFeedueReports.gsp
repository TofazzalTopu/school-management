<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Fee Pay/Due Report</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Fee Pay/Due Report"/>
<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                Monthly Fees
            </header>
            <div class="panel-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="monthlyClassName" class="col-md-3 control-label">Class Name*</label>
                        <div class="col-md-8">
                            <g:select tabindex="2" class="form-control" id="monthlyClassName"
                                      noSelection="${['': 'Select Class...']}"
                                      name='monthlyClassName'
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"/>
                        </div>
                    </div>

                    <div id="monthly-fee-name-holder" style="display: none;">
                        <div class="form-group">
                            <label for="monthlyFeeItems" class="col-md-3 control-label">Fee Name*</label>
                            <div class="col-md-8">
                                <select class="form-control" id="monthlyFeeItems" name="monthlyFeeItems" tabindex="2"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sMonth" class="col-md-3 control-label">From Month </label>
                            <div class="col-md-8">
                                <g:select tabindex="2" class="form-control" id="sMonth"
                                          name='sMonth'
                                          from='${com.grailslab.enums.YearMonths.values()}'
                                          optionKey="key" optionValue="value"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="eMonth" class="col-md-3 control-label">To Month </label>
                            <div class="col-md-5">
                                <g:select tabindex="2" class="form-control" id="eMonth" value="${formatDate(date: new Date().minus(30), format: 'MMMM').toUpperCase()}"
                                          name='eMonth'
                                          from='${com.grailslab.enums.YearMonths.values()}'
                                          optionKey="key" optionValue="value"/>
                            </div>
                            <div class="col-md-1">
                                <button class="btn btn-info" id="printMonthlyFeebtn">&nbsp;&nbsp; Print</button>
                            </div>
                            <div class="col-md-1">
                                <button class="btn btn-info" id="sendSmsMonthlyFeebtn">Send SMS</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var className, academicYear,feeItem;
    jQuery(function ($) {
        $('#monthlyClassName').select2().enable(true);
        $('#monthlyClassName').on('change', function (e) {
            className = $('#monthlyClassName').val();
            if(className != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadClassFee')}?monthlyFees=true&className="+className,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            var $select = $('#monthlyFeeItems');
                            $select.find('option').remove();
                            if(data.feeList){
                                $.each(data.feeList,function(key, value)
                                {
                                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                                });
                                $("#monthly-fee-name-holder").show();
                            }else {
                                $("#monthly-fee-name-holder").hide();
                            }

                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }else {
                $("#monthlyFeeItems").find('option').remove();
                $("#monthly-fee-name-holder").hide();
            }
            e.preventDefault();
        });

        $('#load-student-btn').click(function (e) {
            e.preventDefault();
        });
        $('#printMonthlyFeebtn').click(function (e) {
            var className=$('#monthlyClassName').val();
            var monthlyFeeItems=$('#monthlyFeeItems').val();
            var sMonth=$('#sMonth').val();
            var eMonth=$('#eMonth').val();
            var sectionParam = "${g.createLink(controller: 'accountsReport',action: 'monthlyFeePayReport')}?className="+className+"&feeItems="+monthlyFeeItems+"&sMonth="+sMonth+"&eMonth="+eMonth;
            window.open(sectionParam);
            e.preventDefault();
        });
    });
</script>
</body>
</html>