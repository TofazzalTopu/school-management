<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Fee Pay/Due Report</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Fee Pay/Due Report"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="className" class="col-md-3 control-label">Class Name*</label>
                        <div class="col-md-8">
                            <g:select tabindex="2" class="form-control" id="className"
                                      noSelection="${['': 'Select Class...']}"
                                      name='className'
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"/>
                        </div>
                    </div>

                    <div class="form-group" id="fee-name-holder" style="display: none;">
                        <label for="feeItems" class="col-md-3 control-label">Fee Name*</label>
                        <div class="col-md-8">
                            <select class="form-control" id="feeItems" name="feeItems" tabindex="2"></select>
                        </div>
                    </div>
                    <div class="form-group" id="item-detail-holder" style="display: none;">
                        <label for="itemDetail" class="col-md-3 control-label">Month Name * </label>
                        <div class="col-md-8">
                            <select class="form-control" id="itemDetail" name="itemDetail" tabindex="2"></select>
                        </div>
                    </div>
                    <div class="form-group" id="pay-status-holder" style="display: none;">
                        <label for="payStatus" class="col-md-3 control-label">Pay Status</label>
                        <div class="col-md-3">
                            <select name="payStatus" id="payStatus" class="form-control" tabindex="1">
                                <option value="due">Due</option>
                                <option value="paid">Paid</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-info" id="load-student-btn">Load Students</button>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-info" id="print-btn"><i class="fa fa fa-print"></i> &nbsp;&nbsp; Print</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body" id="student-datalist-holder" style="display: none;">
                <div class="col-md-12">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Section</th>
                                <th>STD ID</th>
                                <th>Name</th>
                                <th>Roll No</th>
                                <th>Amount</th>
                                <th>Discount</th>
                                <th>Net Pay</th>
                                <th>Invoice No</th>
                                <th>Paid On</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var className, academicYear,feeItem;
    jQuery(function ($) {
        $('#className').select2().enable(true);
        $('#className').on('change', function (e) {
            $("#item-detail-holder").find('option').remove();
            $('#student-datalist-holder').hide(500);
            className = $('#className').val();
            if(className != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadClassFee')}?className="+className,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            $('#feeItems').select2("destroy");
                            var $select = $('#feeItems');
                            $select.find('option').remove();
                            if(data.feeList){
                                $select.append('<option value="">Select Fee Item</option>');
                                $.each(data.feeList,function(key, value)
                                {
                                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                                });
                                $('#feeItems').select2().enable(true);
                                $("#fee-name-holder").show();
                                $("#item-detail-holder").hide();
                                $("#pay-status-holder").hide();
                            }else {
                                $("#fee-name-holder").hide();
                                $("#item-detail-holder").hide();
                                $("#pay-status-holder").hide();
                            }

                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }else {
                $("#fee-name-holder").find('option').remove();
                $("#fee-name-holder").hide();
                $("#item-detail-holder").hide();
                $("#pay-status-holder").hide();
            }
            e.preventDefault();
        });
        $('#feeItems').on('change', function (e) {
            feeItem = $('#feeItems').val();
            $('#student-datalist-holder').hide(500);
            if(feeItem != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadFeeItemsDetail')}?id="+feeItem,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            $('#itemDetail').select2("destroy");
                            var $select = $('#itemDetail');
                            $select.find('option').remove();
                            if(data.itemsDetail){
                                $.each(data.itemsDetail,function(key, value)
                                {
                                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                                });
                                $('#itemDetail').select2().enable(true);
                                $("#item-detail-holder").show();
                                $("#pay-status-holder").show();
                            }else{
                                $("#item-detail-holder").find('option').remove();
                                $("#item-detail-holder").hide();
                                $("#pay-status-holder").show();
                            }
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }else {
                $("#item-detail-holder").find('option').remove();
                $("#item-detail-holder").hide();
                $("#pay-status-holder").hide();
            }
            e.preventDefault();
        });

        $('#load-student-btn').click(function (e) {
            $('#list-table').DataTable().ajax.reload();
            $('#student-datalist-holder').show(500);
            e.preventDefault();
        });
        $('#print-btn').click(function (e) {

            var className=$('#className').val();
            var feeItems=$('#feeItems').val();
            var payStatus=$('#payStatus').val();
            var itemDetail=$('#itemDetail').val();
            var sectionParam = "${g.createLink(controller: 'accountsReport',action: 'feePayReport')}?className="+className+"&feeItems="+feeItems+"&payStatus="+payStatus+"&itemDetail="+itemDetail;
            window.open(sectionParam);
            e.preventDefault();
        });


        var table = $('#list-table').dataTable({
            "bAutoWidth": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": 0,
            "sAjaxSource": "${g.createLink(controller: 'collections',action: 'feePayStatusList')}",
            "fnServerParams": function (aoData) {
                aoData.push({ "name": "className", "value": $('#className').val() },{ "name": "feeItems", "value": $('#feeItems').val() },{ "name": "payStatus", "value": $('#payStatus').val() }, { "name": "itemDetail", "value": $('#itemDetail').val() });
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ]
        });

    });


</script>
</body>
</html>