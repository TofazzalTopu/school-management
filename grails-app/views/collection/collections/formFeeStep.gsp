<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Form Fee Collection Step 2</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Form Fee Collection" firstBreadCrumbUrl="${g.createLink(controller: 'collections',action: 'offlineFormFee')}" breadCrumbTitleText="Offline Admission Form Fee"/>
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
                    <span class="panel-header-title">Class Name :</span> <span class="panel-header-info">${className?.name},</span><span class="panel-header-title">Invoice Date :</span><span class="panel-header-info"><g:formatDate format="dd/MM/yyyy" date="${new java.util.Date()}"/></span>
                </header>
                <div class="panel-body">
                    <div class="col-md-11" id="class-fee-list-holder">
                        <div class="row">
                            <form class="cmxform form-inline" role="form" method="post" id="create-form" action="${g.createLink(controller: 'collections',action: 'offlineInvoice')}">
                                <g:hiddenField name="className" id="className" value="${className?.id}"/>
                                <div class="col-md-12">
                                    <div class="ibox float-e-margins">
                                        <div class="table-responsive">
                                            <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                                                <thead>
                                                <tr>
                                                    <th class="col-md-3">Name</th>
                                                    <th class="col-md-2">Pay Amount</th>
                                                    <th class="col-md-2">Discount (%)</th>
                                                    <th class="col-md-2">Payable</th>
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
                                                        <td>${dataSet?.amount}</td>
                                                        <td>${dataSet?.discount} %</td>
                                                        <td>${dataSet?.payable}</td>
                                                        <td><input type="text" class="quantityKeyUp col-md-10"
                                                                   id="quantity${dataSet?.id}"
                                                                   name="quantity${dataSet?.id}" value="${dataSet?.quantity}"/>
                                                        </td>
                                                        <td><input type="text" readonly class="netPayable col-md-10 text-right"
                                                                   id="netPayable${dataSet?.id}"
                                                                   name="netPayable${dataSet?.id}"
                                                                   value="${dataSet?.payable}"/></td>
                                                    </tr>
                                                </g:each>
                                                <tr id="totalAmount" class="alert alert-success">
                                                    <td colspan="5" class="text-right">Total Amount</td>
                                                    <td class=" font-bold text-right" style="padding-right: 20px;">${totalPayable}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <label for="name">Name</label>
                                                <input tabindex="1" name="name" id="name" class="form-control" type="text" placeholder="Full Name" required/>
                                            </div>
                                            <div class="form-group">
                                                <label for="mobile">Mobile No</label>
                                                <input tabindex="2" name="mobile" id="mobile" class="form-control" type="text" placeholder="Mobile No" required/>
                                            </div>
                                            <div class="form-group">
                                                <label for="fathersName">Father Name</label>
                                                <input tabindex="3" name="fathersName" id="fathersName" class="form-control" type="text" placeholder="Father Name"/>
                                            </div>
                                            <div class="form-group">
                                                <input tabindex="3" name="birthDate" id="birthDate" class="form-control" type="text" placeholder="Birth Date"/>
                                            </div>
                                        </div>

                                        <div class="pull-right">
                                            <button tabindex="4" type="button" class="btn btn-primary" id="submit-invoice-btn">Process Fee</button>
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
            $("#create-form").submit();

        });
        $('#birthDate').datepicker({
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
            payable = $td.eq(3).text();
            netPayable = quantity * payable;
            $("#netPayable" + rowId).val(netPayable);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        } else {
            $("#netPayable" + rowId).val($td.eq(2).val());
            $("#quantity" + rowId).val(1);
            payable = $td.eq(3).text();
            netPayable = payable;
            $("#netPayable" + rowId).val(netPayable);
            $("#totalAmount").remove();
            totalPayable = sumPayable();
            $('#list-table').append('<tr class="alert alert-success" id="totalAmount"><td class="text-right" colspan="5">Total Amount</td><td style="padding-right: 20px;" class=" font-bold text-right">' + totalPayable + '</td></tr>');
        }
    });
</script>
</body>
</html>
</html>