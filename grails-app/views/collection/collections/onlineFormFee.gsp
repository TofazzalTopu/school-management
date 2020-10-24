<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Collection Online Form Fee</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Online Form Fee"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-8 col-md-offset-1">
                        <div class="row">
                            <g:form class="cmxform form-horizontal" action="byStudentStep2" method="GET" target="_blank">

                                <div class="form-group">
                                    <div class="col-md-3">
                                        <select name="quickFee" id="quickFee" class="form-control" tabindex="1">
                                            <option value="1">Quick Fee 1</option>
                                            <option value="2">Quick Fee 2</option>
                                            <option value="2">Online Payment</option>
                                        </select>
                                   </div>
                                    <div class="col-md-6">
                                        <input type="hidden" class="form-control" id="student" name="student" tabindex="3" />
                                    </div>

                                    <div class="col-md-2">
                                        <button class="btn btn-primary" type="submit" id="submit-fees-btn">Next</button>
                                    </div>

                                    <div class="col-md-1">
                                        <button class="btn btn-primary" type="submit" id="admit-fees-btn">Admit</button>
                                    </div>

                                </div>

                            </g:form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Invoice No</th>
                            <th>Form No</th>
                            <th>Name</th>
                            <th>Class Name</th>
                            <th>Fathers Name</th>
                            <th>Mobile</th>
                            <th>Birth Date</th>
                            <th>Admit No</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>





<script>

    var  academicYear;
    jQuery(function ($) {
        $('#list-table_wrapper div.class-filter-holder').html('<select id="classFilter" class="form-control" name="classFilter"><option value="">Select Class</option> <g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.status-filter-holder').html('<select id="statusFilter" class="form-control" name="statusFilter"><option value="">Select Applicant Status</option> <g:each in="${com.grailslab.enums.ApplicantStatus.values()}" var="applicantStatus"><option value="${applicantStatus.key}" ${applicantStatus.key == com.grailslab.enums.ApplicantStatus.Apply.key?"selected":""}>${applicantStatus.value}</option> </g:each></select>');

        $('.date-picker-input').datepicker({
            format: 'dd/mm/yyyy',
            setDate: new Date(),
            autoclose: true
        }).on('changeDate', function(e) {
            showLoading("#list-table");
            $listTable.DataTable().draw(true);
            hideLoading("#list-table");
        });
        $('#className-filter').on('change', function (e) {
            showLoading("#list-table");
            $listTable.DataTable().draw(true);
            hideLoading("#list-table");
        });
        $('#dateRange-filter').on('change', function (e) {
            showLoading("#list-table");
            $listTable.DataTable().draw(true);
            hideLoading("#list-table");
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'collections',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            $listTable.DataTable().row(selectRow).remove().draw(false);
                            showSuccessMsg(data.message);
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

        $('#list-table').on('click', 'a.print-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                sectionParam ="${g.createLink(controller: 'accountsReport',action: 'printInvoice','_blank')}?invoiceNo="+referenceId;
                window.open(sectionParam);
                return false; // avoid to execute the actual submit of the form.
            }
            e.preventDefault();
        });

        $('.btn-collection-reload').click(function (e) {
            showLoading("#list-table");
            $listTable.DataTable().draw(true);
            hideLoading("#list-table");
            e.preventDefault();
        });

        $('#student').select2({
            placeholder: "Search for a Student [serialNo/name/father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'applicantTypeAheadList')}",
                dataType: 'json',
                quietMillis: 500,
                data: function (term, page) {
                    return {
                        q: term, // search term
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

    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-6 no-padding"><a href="javascript:void(0)" referenceId="' + aData.invoiceNo + '" class="print-reference" title="Print Invoice">';
        actionButtons += '<span class="red glyphicon glyphicon-print"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }
</script>
</body>
</html>
</html>