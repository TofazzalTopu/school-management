<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Collection By Student</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Collection By Student"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <g:if test='${flash.message}'>
                <header class="panel-heading">
                    <span class="alert alert-danger">${flash.message}</span>
                </header>
            </g:if>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-8 col-md-offset-1">
                        <div class="row">
                            <g:form class="cmxform form-horizontal" action="byStudentStep2" method="GET" target="_blank">
                                <div class="form-group">
                                    <div class="col-md-2">
                                        <g:select class="form-control" id="academicYear" name='academicYear' tabindex="1"
                                                  from='${academicYearList}' value="${workingYear?.key}"
                                                  optionKey="key" optionValue="value" />

                                    </div>
                                    <div class="col-md-5">
                                        <select name="quickFee" id="quickFee" class="form-control" tabindex="1">
                                            <option value="">All Fees</option>
                                            <option value="1">Quick Fee 1</option>
                                            <option value="2">Quick Fee 2</option>
                                            <option value="3">Online Payment</option>
                                        </select>
                                    </div>
                                    <div class="col-md-5">
                                        <select name="feePaidCriteria" id="feePaidCriteria" class="form-control" tabindex="1">
                                            <option value="">All Items</option>
                                            <option value="1">Not Paid Items</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-10">
                                        <input type="hidden" class="form-control" id="student" name="student" tabindex="3" />
                                    </div>

                                    <div class="col-md-2">
                                        <button class="btn btn-primary" type="submit" id="submit-fees-btn">Next</button>
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
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Invoice No</th>
                            <th>Student</th>
                            <th>Class-Section</th>
                            <th>Amount</th>
                            <th>Discount</th>
                            <th>Net Pay</th>
                            <th>Date</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>
                                    <span class="col-md-6 no-padding"><a href="javascript:void(0)" referenceId="${dataSet.DT_RowId}"
                                                                         class="delete-reference"
                                                                         title="Delete"><span
                                                class="green glyphicon glyphicon-trash"></span></a></span>
                                    <span class="col-md-6 no-padding"><a href="javascript:void(0)" referenceId="${dataSet.invoiceNo}"
                                                                         class="print-reference"
                                                                         title="Print Invoice"><span
                                                class="green glyphicon glyphicon-print"></span></a></span>
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


<script>
    jQuery(function ($) {
        var $listTable = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-1 reload-btn-holder'><'col-md-3 startDate-filter-holder'><'col-md-3 endDate-filter-holder'><'col-md-2 className-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'desc'],
            "deferLoading": ${totalCount?:0},
            "fnServerParams": function ( aoData ) {
                aoData.push({ "name": "className", "value": $('#className-filter').val()},
                        { "name": "startDate", "value": $('#startDate-filter').val() },
                        { "name": "endDate", "value": $('#endDate-filter').val() });
            },
            "sAjaxSource": "${g.createLink(controller: 'collections',action: 'listManage')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.reload-btn-holder').html('<div class="btn-group btn-margin-left"><a class="btn btn-info btn-collection-reload" href="javascript:void(0);" title="Reload"><i class="glyphicon glyphicon-repeat"></i></a></div>');
        $('#list-table_wrapper div.startDate-filter-holder').html('<input style="width:100%;" class="form-control date-picker-input" type="text" name="startDate-filter" id="startDate-filter" value="${toDateStr}" />');
        $('#list-table_wrapper div.endDate-filter-holder').html('To  <input class="form-control date-picker-input" type="text" name="endDate-filter" id="endDate-filter" value="${toDateStr}"/>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="className-filter" class="form-control" name="className-filter"><option value="">All Class</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');

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
            placeholder: "Search for a Student [studentId/name/Class Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadListWithStdId')}",
                dataType: 'json',
                quietMillis: 500,
                data: function (term, page) {
                    return {
                        q: term, // search term
                        academicYear: $('#academicYear').val(), // search term
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


        $('#feePaidCriteria').select2();
        $('#quickFee').select2();
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