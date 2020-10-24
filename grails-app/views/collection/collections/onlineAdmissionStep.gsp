<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Collection By Class Step 2</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Online Application" firstBreadCrumbUrl="${g.createLink(controller: 'collections',action: 'onlineAdmission')}" breadCrumbTitleText="Quick Admission"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <g:if test='${flash.message}'>
                <header class="panel-heading">
                    ${flash.message}
                </header>
            </g:if>
            <g:else>
                <header class="panel-heading">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-md-3 control-label">Admit Card : <span style="color: #009900;">${selectedApplicant?.applyNo ? selectedApplicant.applyNo : selectedApplicant.serialNo}</span></label>
                            <label class="col-md-5 control-label"
                                   style="text-align: left">Name : <span style="color: #009900;">${selectedApplicant?.name} [${selectedApplicant?.fathersName}]</span></label>
                            <label class="col-md-3 control-label"
                                   style="text-align: left">Section : <span style="color: #009900;">${section?.name} [ ${section?.className?.name} ]</span></label>
                        </div>
                    </div>
                </header>
                <div class="panel-body">
                    <div class="col-md-12" id="class-fee-list-holder">
                        <div class="row">
                            <form class="cmxform form-inline" role="form" method="post" id="create-form" action="${g.createLink(controller: 'collections',action: 'payOnlineInvoice')}">
                                <g:hiddenField name="admitCardNumber" id="admitCardNumber" value="${selectedApplicant?.serialNo}"/>
                                <g:hiddenField name="section" id="section" value="${section?.id}"/>
                                <div class="col-md-12">
                                    <div class="ibox float-e-margins">

                                        <div class="col-md-offset-1 col-md-9">
                                            <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                                                <thead>
                                                <tr>
                                                    <th class="col-md-3">Name</th>
                                                    <th class="col-md-2">Fee [discount]</th>
                                                    <th class="col-md-2">Payable</th>
                                                    <th class="col-md-2">Select Month</th>
                                                    <th class="col-md-1">Quantity</th>
                                                    <th class="text-right col-md-2">Net Payable</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <g:each in="${dataArray}" var="dataSet" status="i">
                                                    <tr id="${dataSet?.id}" class="alert alert-info">
                                                        <td>${dataSet?.name}
                                                            <input type="hidden" value="${dataSet?.id}" name="feeItemIds"/>
                                                        </td>
                                                        <td>${dataSet?.amount} ${dataSet?.discount >0 ? '[-'+ dataSet?.discount+'%]' : ''}</td>
                                                        <td>
                                                            <input type="text" class="payableKeyUp col-md-12 text-center"
                                                                   id="payable${dataSet?.id}"
                                                                   name="payable${dataSet?.id}" value="${dataSet?.payable}" style="padding-left: 2px;padding-right: 2px;"/>
                                                        </td>
                                                        <g:if test="${dataSet.itemMonthList}">
                                                            <td>
                                                                <g:select class="form-control" id="feeMonth${dataSet?.id}"
                                                                          name='feeMonth${dataSet?.id}'
                                                                          from='${dataSet.itemMonthList}' value="${dataSet.nextPaidMonth?.id}"
                                                                          optionKey="id" optionValue="name"></g:select>
                                                            </td>
                                                        </g:if>
                                                        <g:else>
                                                            <td>N/A</td>
                                                        </g:else>

                                                        <td><input type="text" class="quantityKeyUp col-md-12 text-center"
                                                                   id="quantity${dataSet?.id}"
                                                                   name="quantity${dataSet?.id}" value="${dataSet?.quantity}" style="padding-left: 2px;padding-right: 2px;"/>
                                                        </td>
                                                        <td><input type="text" readonly class="netPayable col-md-12 text-right"
                                                                   id="netPayable${dataSet?.id}"
                                                                   name="netPayable${dataSet?.id}"
                                                                   value="${dataSet?.netPayable}"/></td>
                                                    </tr>
                                                </g:each>
                                                <tr id="totalAmount" class="alert alert-success">
                                                    <td colspan="5" class="text-right">Total Amount</td>
                                                    <td class=" font-bold text-right" style="padding-right: 20px;">${totalPayable}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                            <button class="btn btn-danger" id="cancel-invoice-btn">Cancel</button>
                                            <div class="form-group">
                                                <label for="rollNo">Roll No</label>
                                                <input class="form-control" type="text" required="required" name="rollNo" id="rollNo" value="${rollNo}" placeholder="Roll no"/>
                                            </div>
                                            <div class="pull-right">
                                                <button type="button" class="btn btn-primary" id="submit-invoice-btn">Process Fee</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </g:else>
        </section>
    </div>
</div>


<script>
    var className, academicYear, section;
    jQuery(function ($) {
        $("#submit-invoice-btn").click(function()
        {
            showLoading("#class-fee-list-holder");
            $("#create-form")[0].submit();

        });
        $('#invoiceDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate: new Date(),
            autoclose: true
        });


        $('#cancel-invoice-btn').click(function (e) {
            var confirmDel = confirm("Are you sure cancel invoice?");
            if (confirmDel == true) {
                window.top.close();
            }
            e.preventDefault();
        });


    });

    function sumPayable() {
        var totalPayable = 0;
        var netPayable = 0;
        $('#list-table > tbody  > tr').each(function () {
            var rowId = $(this).attr('id');
            netPayable = Math.round($("#netPayable" + rowId).val());
            totalPayable += netPayable;
            netPayable = 0;
        });
        return totalPayable
    }
    $('.quantityKeyUp').keyup(function () {
        var $td = $(this).closest('tr').children('td');
        var rowId = $(this).closest('tr').attr('id');
        var quantity = 0;
        var netPayable = 0;
        var payable = 0;
        var totalPayable = 0;
        if (!isNaN($(this).val())) {
            quantity = $("#quantity" + rowId).val();
            payable = $("#payable" + rowId).val();
            netPayable = quantity * payable;
            $("#netPayable" + rowId).val(netPayable);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        } else {
            $("#netPayable" + rowId).val($td.eq(1).val());
            $("#quantity" + rowId).val(1);
            payable = $("#payable" + rowId).val();
            netPayable = payable;
            $("#netPayable" + rowId).val(netPayable);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        }
    });
    $('.payableKeyUp').keyup(function () {
        var $td = $(this).closest('tr').children('td');
        var rowId = $(this).closest('tr').attr('id');
        var quantity = 0;
        var netPayable = 0;
        var payable = 0;
        var totalPayable = 0;
        if (!isNaN($(this).val())) {
            quantity = $("#quantity" + rowId).val();
            payable = $("#payable" + rowId).val();
            netPayable = quantity * payable;
            $("#netPayable" + rowId).val(netPayable);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        } else {
            $("#netPayable" + rowId).val(0);
            $("#quantity" + rowId).val(0);
            $("#payable" + rowId).val(0);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        }
    });

</script>
</body>
</html>
</html>