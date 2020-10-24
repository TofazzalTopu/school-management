<div class="col-md-12">
    <div class="ibox float-e-margins">
        <div class="col-md-3">
            <p style="color: royalblue; font-size: large;">Previous Payments</p>
            <div class="ibox-content">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped table-condensed">
                        <tbody>
                        <tr></tr>
                        <g:each in="${previousPayments}" var="payment">
                            <g:if test="${payment.hasItemDetail}">
                                <tr><td colspan="2" style="color: red;">${payment.item}</td></tr>
                                <g:each in="${payment.itemDetails}" var="itemDetails">
                                    <tr><td>${itemDetails.item}</td><td>${itemDetails.amount}</td></tr>
                                </g:each>
                                <tr><td colspan="2">&nbsp;</td></tr>
                            </g:if>
                            <g:else>
                                <tr><td>${payment.item}</td><td>${payment.amount}</td></tr>
                            </g:else>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="ibox-content">
                <div class="table-responsive">
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
                            <tr id="${dataSet?.id}" class="${dataSet.isAlreadyPaid?'alert alert-danger':'alert alert-info'}">
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
                                                  optionKey="id" optionValue="name"/>
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
                </div>
                <button class="btn btn-danger" id="cancel-invoice-btn">Cancel</button>
                <div class="form-group">
                    <label for="invoiceDate">Invoice Date: <g:formatDate format="yyyy-MM-dd" date="${new java.util.Date()}" /></label>
%{--                    <input class="form-control" type="text" name="invoiceDate" id="invoiceDate" placeholder="<g:formatDate format="yyyy-MM-dd" date="${new java.util.Date()}" />"/>--}%
                </div>
                <div class="pull-right">
                    <button type="submit" class="btn btn-primary" id="submit-invoice-btn">Process Fee</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var className, academicYear, section;
    jQuery(function ($) {
        jQuery('#create-form').submit( function(e){
            e.preventDefault();
            showLoading("#class-fee-list-holder");
            // note the [0] array access:
            if ( true ) jQuery('#create-form')[0].submit();
        });

        $('#invoiceDate').datepicker({
            format: 'dd/mm/yyyy',
//            endDate: '7d',
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