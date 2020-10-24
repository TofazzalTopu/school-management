<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Form Fee Collection</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Form Fee Collection"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-4">
                            <select name="quickFee" id="quickFee" class="form-control" tabindex="1">
                                <option value="1">Quick Fee 1</option>
                                <option value="2">Quick Fee 2</option>
                                <option value="2">Online Payment</option>
                            </select>
                        </div>
                        <div class="col-md-5">
                            <g:select tabindex="2" class="form-control" id="className"
                                      name='className'
                                      noSelection="${['': 'Select Class...']}"
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>

                        <div class="col-md-3">
                            <button class="btn btn-info" id="load-btn">Load Fees</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body">
                <div class="col-md-12" id="class-fee-list-holder" style="display: none;">
                    <div class="row">
                        <g:form class="cmxform form-horizontal" action="offlineFeeStep" method="POST" target="_blank">
                            <g:hiddenField name="cName" id="cName"/>
                            <div class="col-md-10 col-md-offset-1">
                                <div class="col-md-12">
                                    <div class="ibox float-e-margins">
                                        <div class="ibox-title">
                                            <h5>Select Fees</h5>
                                        </div>

                                        <div class="ibox-content">
                                            <div class="table-responsive">
                                                <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                                                    <thead>
                                                    <tr>
                                                        <th>Name</th>
                                                        <th>Pay Amount</th>
                                                        <th>Discount (%)</th>
                                                        <th>Action</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-md-12">
                                <div class="col-md-offset-11 col-md-1">
                                    <button class="btn btn-primary" type="submit" id="submit-fees-btn">Next</button>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var className,quickFee;
    $(document).ready(function() {
        $('#className').select2();

        $('#load-btn').click(function (e) {
            className = $('#className').val();
            quickFee = $('#quickFee').val();
            if(className != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadOfflineClassFee')}?classId=" + className+"&quickType=" + quickFee,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == true) {
                            $("#class-fee-list-holder").hide(500);
                            showErrorMsg(data.message);
                        } else {
                            $('#cName').val(className);
                            $("#list-table").find("tr:gt(0)").empty();
                            $.each(data.feeList, function (key, value) {
                                $('#list-table').append('<tr><td>' + value.name +'</td><td>' + value.amount +'</td><td>' + value.discount +'</td><td><input name="feeId" value="'+ value.id+'" type="checkbox"></td></tr>');
                            });
                            $("#class-fee-list-holder").show(500);
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
</html>