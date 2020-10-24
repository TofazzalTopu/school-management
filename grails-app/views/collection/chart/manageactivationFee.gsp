<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title> Manage Activation  Fees</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'chart', action: 'classFees')}" firstBreadCrumbText="Class Fee" breadCrumbTitleText="Manage Activation Fee"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Fee Name</th>
                            <th>Month</th>
                            <th>Amount</th>
                            <th>Discount</th>
                            <th>Net Pay</th>
                            <th>Now Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${dataSet?.feeName}</td>
                                <td>${dataSet?.name}</td>
                                <td>${dataSet?.amount}</td>
                                <td>${dataSet?.discount}</td>
                                <td>${dataSet?.netPayable}</td>
                                <td>${dataSet?.status}</td>
                                <td>
                                    <span class="col-md-10 no-padding"><a href="#" referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-reference" title="Manage Activation Fee"><button
                                                class="btn btn-xs ${dataSet.status =='Active' ? 'btn-success':'btn-warning'}">${dataSet.status =='Active' ? 'Do Inactivate':'Do Activate'}</button></a></span>
                                </td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<!-- Modal -->

<script>
    jQuery(function ($) {
        $('#list-table').dataTable({
            "aaSorting": [0, 'asc'],
            "paging": false
        });
        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'chart',action: 'activeFeeStatus')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'chart',action: 'manageActivationFees', params: [id: feeId])}";
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
    });


</script>
</body>
</html>
