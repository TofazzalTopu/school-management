<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Collection By Class</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Fee Collection by Class"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-3">
                            <g:select class="form-control" id="academicYear" name='academicYear' tabindex="1"
                                      from='${academicYearList}' value="${workingYear}"
                                      optionKey="key" optionValue="value"></g:select>

                        </div>
                        <div class="col-md-3">
                            <select name="quickFee" id="quickFee" class="form-control" tabindex="2">
                                <option value="">All Fees</option>
                                <option value="1">Quick Fee 1</option>
                                <option value="2">Quick Fee 2</option>
                                <option value="3">Online Payment Fee</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <g:select tabindex="3" class="form-control" id="className"
                                      name='className'
                                      noSelection="${['': 'Select Class...']}"
                                      from='${classNameList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>

                        <div class="col-md-2">
                            <button class="btn btn-info" id="load-btn">Load Fees</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body">
                <div class="col-md-12" id="class-fee-list-holder" style="display: none;">
                    <div class="row">
                        <g:form class="cmxform form-horizontal" action="byClassStep" method="GET" target="_blank">
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
                                <div class="col-md-6 col-md-offset-5">
                                    <select id="student" name='student' class="form-control"
                                            required="required"></select>
                                </div>

                                <div class="col-md-1">
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
    var className,quickFee,academicYear;
    jQuery(function ($) {
        $('#className').select2();
        $('#load-btn').click(function (e) {
            academicYear = $('#academicYear').val();
            className = $('#className').val();
            quickFee = $('#quickFee').val();

            if(className != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadByClassFee')}?academicYear="+academicYear+ "&classId=" + className+"&quickType=" + quickFee,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == true) {
                            $("#class-fee-list-holder").hide(500);
                            showErrorMsg(data.message);
                        } else {

                            var $selectStudent = $('#student');
                            $selectStudent.find('option').remove();
                            $.each(data.studentList, function (key, value) {
                                $selectStudent.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $('#student').select2().enable(true);
                            $("#list-table").find("tr:gt(0)").remove();
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