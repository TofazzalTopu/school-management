<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Admission for Online Applicants</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Online Applicant Admission"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-4">
                            <select name="quickFee" id="quickFee" class="form-control" tabindex="1">
                                <option value="">All Fees</option>
                                <option value="1">Quick Fee 1</option>
                                <option value="2">Quick Fee 2</option>
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
                        <g:form class="cmxform form-horizontal" action="onlineAdmissionStep" method="GET" target="_blank">
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
                                <div class="col-md-offset-1 col-md-4">
                                    <select id="section" name='section' class="form-control"
                                            required="required"></select>
                                </div>
                                <div class="col-md-5">
                                    <input type="hidden" class="form-control" id="selectedApplicant" name="selectedApplicant" tabindex="3" required="required" />
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
    var className,quickFee;
    jQuery(function ($) {
        $('#className').select2();
        $('#quickFee').select2();

        $('#selectedApplicant').select2({
            placeholder: "Search for a Student [SL/Admit Card No/name/mobile",
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'applicantTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
                        className:$('#className').val(),
                        applicantStatus:"Selected"
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });
        $('#load-btn').click(function (e) {
            className = $('#className').val();
            quickFee = $('#quickFee').val();
            if(className != ""){
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'loadOnlineAdmissionFee')}?classId=" + className+"&quickType=" + quickFee,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == true) {
                            $("#class-fee-list-holder").hide(500);
                            showErrorMsg(data.message);
                        } else {
                            var $selectSection = $('#section');
                            $selectSection.find('option').remove();
                            $.each(data.sectionList, function (key, value) {
                                $selectSection.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
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