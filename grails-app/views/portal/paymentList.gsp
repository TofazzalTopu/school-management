<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Profile</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Payment History"/>
    <div class="panel-body">
        <g:if test="${flash.message}">
            <div class="message" style="display: block"><h3>${flash.message}</h3></div>
        </g:if>
        <div class="col-md-12" id="student-pay-list-show-holder">
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
                    <tbody>
                        <g:each in="${feeItemList}" var="itemList">
                            <g:if test="${itemList.hasItemDetail}">
                                <tr>
                                    <td class="col-md-3">${itemList.item}</td>
                                </tr>
                                <g:each in="${itemList.itemDetails}" var="itemDetailsList">
                                    <tr>
                                        <td class="col-md-3">${itemDetailsList.item}</td>
                                        <td class="col-md-1">${itemDetailsList.amount}</td>
                                        <td class="col-md-1">${itemDetailsList.discount}</td>
                                        <td class="col-md-1">${itemDetailsList.netPayment}</td>
                                        <td class="col-md-1">${itemDetailsList.quantity}</td>
                                        <td class="text-right col-md-1">${itemDetailsList.totalPayment}</td>
                                        <td class="text-center col-md-2">${itemDetailsList.invoiceNo}</td>
                                        <td class="text-center col-md-2">${itemDetailsList.invoiceDate}</td>
                                    </tr>
                                </g:each>
                                <tr><td colspan="8"></td></tr>
                            </g:if>
                            <g:else>
                                <tr>
                                    <td class="col-md-3">${itemList.item}</td>
                                    <td class="col-md-1">${itemList.amount}</td>
                                    <td class="col-md-1">${itemList.discount}</td>
                                    <td class="col-md-1">${itemList.netPayment}</td>
                                    <td class="col-md-1">${itemList.quantity}</td>
                                    <td class="text-right col-md-1">${itemList.totalPayment}</td>
                                    <td class="text-center col-md-2">${itemList.invoiceNo}</td>
                                    <td class="text-center col-md-2">${itemList.invoiceDate}</td>
                                </tr>
                            </g:else>
                        </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
