<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Collection Online Form Fee</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Online Admit Card Fee Collection" SHOW_PRINT_BTN="YES" printBtnText="Download"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="row">
                            <g:form class="cmxform form-horizontal" action="byStudentStep2" method="GET" target="_blank">

                                <div class="form-group">
                                    <div class="col-md-2">
                                        <g:select class=" form-control"  id="applicantClsName" name='applicantClsName' tabindex="2"
                                                  noSelection="${['': 'All Class']}"
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                   </div>
                                    <div class="col-md-7">
                                        <input type="hidden" class="form-control" id="student" name="student" tabindex="3" />
                                    </div>

                                    <div class="col-md-1">
                                        <button class="btn btn-primary print-admit-btn" title="Print Admit">Print Admit Card</button>
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
                            <th>Class Name</th>
                            <th>Form No</th>
                            <th>Name</th>
                            <th>Father's Name</th>
                            <th>Mobile</th>
                            <th>Birth Date</th>
                            <th>Amount Paid</th>
                            <th>Admit No</th>
                            <th>Date</th>
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
    jQuery(function ($) {
        var $listTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 reload-btn-holder'><'col-md-3 startDate-filter-holder'><'col-md-3 endDate-filter-holder'><'col-md-2 className-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'desc'],
            "fnServerParams": function ( aoData ) {
                aoData.push({ "name": "className", "value": $('#className-filter').val()},
                        { "name": "startDate", "value": $('#startDate-filter').val() },
                        { "name": "endDate", "value": $('#endDate-filter').val() });
            },
            "sAjaxSource": "${g.createLink(controller: 'onlineRegistration',action: 'admitlist')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(10)', nRow).html(getGridActionBtns(nRow, aData, 'admit, '));
           //  $('td:eq(9)', nRow).html(getActionButtons(nRow, aData));
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
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.reload-btn-holder').html('<div class="btn-group btn-margin-left"><a class="btn btn-info btn-admit-reload" href="javascript:void(0);" title="Reload"><i class="glyphicon glyphicon-repeat"></i></a></div>');
        $('#list-table_wrapper div.startDate-filter-holder').html('<input style="width:100%;" class="form-control date-picker-input" type="text" name="startDate-filter" id="startDate-filter"  value="${toDateStr}" />');
        $('#list-table_wrapper div.endDate-filter-holder').html('To  <input class="form-control date-picker-input" type="text" name="endDate-filter" id="endDate-filter" value="${toDateStr}"/>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="className-filter" class="form-control" name="className-filter"><option value="">All Class</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');


        $('.date-picker-input').datepicker({
            format: 'dd/mm/yyyy',
            setDate: new Date(),
            autoclose: true
        }).on('changeDate', function(e) {
            showLoading("#list-table");
            $listTable.draw(true);
            hideLoading("#list-table");
        });
        $('#className-filter').on('change', function (e) {
            showLoading("#list-table");
            $listTable.draw(true);
            hideLoading("#list-table");
        });

        $('.btn-admit-reload').click(function (e) {
            showLoading("#list-table");
            $listTable.draw(true);
            hideLoading("#list-table");
            e.preventDefault();
        });
        $('.print-admit-btn').click(function (e) {
            e.preventDefault();
            var regOnlineId =$('#student').val();
            var  printParam = "${g.createLink(controller: 'onlineRegistrationReport',action: 'admitCard')}?regOnlineId="+regOnlineId;
            window.open(printParam);
            return false;
        });
        $('.print-btn').click(function (e) {
            e.preventDefault();
            var applicantStartDay = $("#startDate-filter").val();
            var applicantEndDay = $("#endDate-filter").val();
            var className = $("#className-filter").val();
            var applicantStatus = "AdmitCard";
           // var reportType = "short";
            var printOptionType = "PDF";
            var reportParam ="${g.createLink(controller: 'onlineRegistrationReport',action: 'collectionReport','_blank')}?fromDate="+applicantStartDay+"&toDate="+applicantEndDay +"&className="+className+"&applicantStatus="+applicantStatus+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });

        $('#list-table').on('click', 'a.admit-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var  printParam = "${g.createLink(controller: 'onlineRegistrationReport',action: 'admitCard')}?regOnlineId="+referenceId;
            window.open(printParam);
            return false;
        })

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
                        applicantStatus:"Apply",
                        className:$("#applicantClsName").val()
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

</script>
</body>
</html>
</html>