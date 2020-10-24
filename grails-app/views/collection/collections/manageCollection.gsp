<%@ page import="com.grailslab.enums.PrintOptionType; com.grailslab.enums.DateRangeType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Manage Collection </title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Collection Summary" SHOW_PRINT_BTN="YES"/>
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

<!-- Modal -->
<div class="modal fade" id="printModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="printModalLabel">Print Collection Summary</h4>
            </div>
            <div class="exam-create-confirm exam-create-content">
                <form class="form-horizontal" role="form" id="printFormModal">
                    <div class="modal-body">
                        <div class="row">
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Select Date<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="pDateRange" name='pDateRange'
                                                  from='${com.grailslab.enums.DateRangeType.values()}'
                                                  optionKey="key" optionValue="value" required="required"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Select Class<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="pClassName" name='pClassName'
                                                  noSelection="${['': 'All Class...']}"
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Print Type<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="printOptionType" name='printOptionType'
                                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                                  optionKey="key" optionValue="value" required="required"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Print<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <select name="reportType" id="reportType" class="form-control" required="required">
                                            <option value="invoiceOnly">Invoice</option>
                                            <option value="invoiceDetail">Invoice with Details</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Invoice Sorting<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <select name="reportSortType" id="reportSortType" class="form-control" required="required">
                                            <option value="byDate">By Date</option>
                                            <option value="byClassName">By Class</option>
                                        </select>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="print-schedule-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    var pDateRange, pClassName, printOptionType, reportType, sectionParam, reportSortType;
    jQuery(function ($) {
        var listTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-2 dateRange-filter-holder'><'col-md-2 startDate-filter-holder'><'col-md-3 endDate-filter-holder'><'col-md-2 className-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'desc'],
            "deferLoading": ${totalCount?:0},
            "fnServerParams": function ( aoData ) {
                aoData.push({ "name": "className", "value": $('#className-filter').val()},
                        { "name": "dateRange", "value": $('#dateRange-filter').val() },
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
        $('#list-table_wrapper div.dateRange-filter-holder').html('<select id="dateRange-filter" class="form-control" name="dateRange-filter"><g:each in="${DateRangeType.values()}" var="dateRange"><option value="${dateRange.key}">${dateRange.value}</option> </g:each></select>');
        $('#list-table_wrapper div.startDate-filter-holder').html('<input style="width:100%;" class="form-control date-picker-input" type="text" name="startDate-filter" id="startDate-filter" value="${toDateStr}" />');
        $('#list-table_wrapper div.endDate-filter-holder').html('To <input class="form-control date-picker-input" type="text" name="endDate-filter" id="endDate-filter" value="${toDateStr}"/>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="className-filter" class="form-control" name="className-filter"><option value="">All Class</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');

        $('.date-picker-input').datepicker({
            format: 'dd/mm/yyyy',
            setDate: new Date(),
            autoclose: true
        }).on('changeDate', function(e) {
            showLoading("#list-table");
            listTable.draw(true);
            hideLoading("#list-table");
        });
        $('#className-filter').on('change', function (e) {
            showLoading("#list-table");
            listTable.draw(true);
            hideLoading("#list-table");
        });
        $('#dateRange-filter').on('change', function (e) {
            showLoading("#list-table");
            listTable.draw(true);
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
                            listTable.draw(false);;
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

        $('.print-btn').click(function (e) {
            $('#printModal').modal('show');
            e.preventDefault();
        });

        $("#printFormModal").submit(function(e) {
            e.preventDefault();
            pDateRange=$('form#printFormModal #pDateRange').find("option:selected").val();
            pClassName =$('form#printFormModal #pClassName').find("option:selected").val();
            printOptionType =$('form#printFormModal #printOptionType').find("option:selected").val();
            reportType = $('form#printFormModal #reportType').val();
            reportSortType = $('form#printFormModal #reportSortType').val();
            $('#printModal').modal('hide');
            sectionParam ="${g.createLink(controller: 'accountsReport',action: 'invoiceSummary','_blank')}?pDateRange="+pDateRange+"&pClassName="+pClassName+"&printOptionType="+printOptionType+"&reportType="+reportType+"&reportSortType="+reportSortType;
            window.open(sectionParam);
            return false; // avoid to execute the actual submit of the form.
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
