<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Fee Pay/Due Report</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Month wise collection"/>
<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                Month wise Collection Report
            </header>
            <div class="panel-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="academicYear" class="col-md-3 control-label">Year*</label>
                        <div class="col-md-5">
                            <g:select class="form-control" id="academicYear" name='academicYear' tabindex="1"
                                      from='${academicYearList}' value="${workingYear?.key}"
                                      optionKey="key" optionValue="value"/>

                        </div>
                    </div>
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
                    <div class="form-group">
                        <label for="payStatus" class="col-md-3 control-label">Collection Type</label>
                        <div class="col-md-3">
                            <select name="payStatus" id="payStatus" class="form-control" tabindex="1">
                                <option value="feeItem">Fee Items</option>
                                <option value="feeItemWithCount">Fee Items with count</option>
                                <option value="dueDate">Due Date</option>
                                <option value="paidDate">Collection Date</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-info" id="printMonthlyFeebtn">&nbsp;&nbsp; Print</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var className;
    jQuery(function ($) {
        $('#monthlyClassName').select2().enable(true);

        $('#printMonthlyFeebtn').click(function (e) {
            var academicYear=$('#academicYear').val();
            var className=$('#monthlyClassName').val();
            var payStatus=$('#payStatus').val();
            var sectionParam = "${g.createLink(controller: 'accountsReport',action: 'monthWise')}?className="+className+"&payStatus="+payStatus+"&academicYear="+academicYear;
            window.open(sectionParam);
            e.preventDefault();
        });
    });
</script>
</body>
</html>