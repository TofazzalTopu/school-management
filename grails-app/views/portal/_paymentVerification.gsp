<div class="row">
    <div class="col-md-6">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title"><g:if test="${student != null}">${student?.name}, </g:if></span><span class="panel-header-info">Your Payment is waiting for your verification!</span>
            </header>
            <div class="panel-body">
                <div class="col-md-12" id="class-fee-list-holder">
                    <div class="row">
                        <p>Pay your amount with bKash to our Merchant Account No: 01993309253 and confirm your transaction ID below within 2 days. </p>
                        <div class="border-small">
                            <form id="verify_bkash_form" method="POST" >
                                <g:hiddenField name="invoiceId" id="invoiceId" value="${invoiceId}"/>
                                <g:hiddenField name="invoiceAmount" id="invoiceAmount" value="${invoiceAmount}"/>
                                <div class="form-group">
                                    <g:if test="${flash.message}">
                                        <div class="message" style="display: block"><h3>${flash.message}</h3></div>
                                    </g:if>
                                    <div style="font-size:17px; color: #F79331;margin:10px 0;">Our Merchant Account No: <b>01993309253</b></div>
                                    <label for="transaction_id" style="font-weight: normal">Please enter your bKash Transaction ID</label>
                                    <div class="input-group">
                                        <input class="form-control" id="transaction_id" name="transaction_id" placeholder="Transaction ID" type="text">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default" id="btn_verify_bkash"onclick="verifyOnlinePayment('${invoiceId}', ${isAccount})">Verify Transaction</button>
                                        </span>
                                    </div>
                                </div>
                            </form>
                            <!-- Button trigger modal -->
                            <a href="javascript:void(0)" data-toggle="modal" data-target="#myModal">How to pay using bKash <i class="fa fa-question-circle"></i></a>

                            <!-- Modal -->
                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="myModalLabel">How to pay using bKash</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div style="width: 99%;">
                                                <p>You can make payments from your bKash Wallet to any "Merchant" who accepts "bKash Payment". For example, if you want to pay after shopping:</p>
                                                <ul>
                                                    <li>Go to your bKash Mobile Menu by dialing *247#</li>
                                                    <li>Choose "Payment"</li>
                                                    <li>Enter the Merchant bKash Wallet Number you want to pay to</li>
                                                    <li>Enter the amount you want to pay</li>
                                                    <li>Enter reference - The mobile number used while booking the ticket</li>
                                                    <li>Enter the Counter Number 1</li>
                                                    <li>Now enter your bKash Mobile Menu PIN to confirm</li>
                                                </ul>
                                                <p>Done! You will receive a confirmation message from bKash.</p>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <div class="col-md-6">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Invoice Amount</span> <span class="panel-header-info">${invoiceAmount}</span>
                <span class="rightFloat" title="Delete Record"  onclick="deleteOnlinePayment('${invoiceId}', ${isAccount})"><button type="button">X</button></span>
            </header>
            <div class="panel-body">
                <div class="col-md-12" id="invoice-list-holder">
                    <div class="table-responsive">
                        <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                            <thead>
                            <tr>
                                <th class="col-md-2">Name</th>
                                <th class="col-md-2">Fee</th>
                                <th class="col-md-2">Payable</th>
                                <th class="col-md-1">Quantity</th>
                                <th class="text-right col-md-2">Net Payable</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${invoiceShowList}" var="showInvoice" status="i">
                                <tr>
                                    <td>${showInvoice.feeItem}</td>
                                    <td>${showInvoice?.amount} ${showInvoice?.discount >0 ? '[-'+ showInvoice?.discount+'%]' : ''}</td>
                                    <td>${showInvoice?.netPayment}</td>
                                    <td>${showInvoice?.quantity}</td>
                                    <td>${showInvoice?.totalPayment}</td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    function deleteOnlinePayment(invoiceId, isAccount) {
        if(confirm("Are you sure want to delete this transaction?")){
            if(isAccount) {
                window.location = "${g.createLink(controller: 'bkash',action: 'deleteOnlinePayment')}?invoiceId="+invoiceId;
            } else {
                window.location = "${g.createLink(controller: 'portal',action: 'deleteOnlinePayment')}?invoiceId="+invoiceId;
            }
        }
    }
    function verifyOnlinePayment(invoiceId, isAccount) {
        var transactionId = $("#transaction_id").val();
        if(transactionId){
            if(confirm("Are you sure want to verify this transaction?")){
                if(isAccount) {
                    window.location = "${g.createLink(controller: 'bkash',action: 'verifyOnlinePayment')}?invoiceId="+invoiceId + "&transactionId="+ transactionId;
                } else {
                    window.location = "${g.createLink(controller: 'portal',action: 'verifyOnlinePayment')}?invoiceId="+invoiceId + "&transactionId="+ transactionId;
                }
            }
        }
    }
    $("#transaction_id").on('change', function() {
        if($(this).val()) {
            $("#btn_verify_bkash").removeClass("btn-default").addClass("btn-primary");
        } else {
            $("#btn_verify_bkash").removeClass("btn-primary").addClass("btn-default");
        }
    });
</script>