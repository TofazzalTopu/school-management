<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Manage Quick Fee </title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Quick Fee"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Name</th>
                            <th>Amount</th>
                            <th>Discount</th>
                            <th>Net Payable</th>
                            <th>Quick One</th>
                            <th>Quick Two</th>
                            <th>Online Payment</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[2]}</td>
                                <td>
                                        <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             referenceVal="${dataSet.quickFee1}" referenceType="quickFee1" class="edit-reference" title="Edit"><button
                                                    class="btn btn-xs ${dataSet.quickFee1 ? 'btn-success':'btn-warning'}">${dataSet.quickFee1 ? 'Remove':'Add'}</button></a></span>
                                </td>
                                <td>
                                        <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             referenceVal="${dataSet.quickFee2}"  referenceType="quickFee2" class="edit-reference" title="Edit"><button
                                                    class="btn btn-xs ${dataSet.quickFee2?'btn-success':'btn-warning'}">${dataSet.quickFee2?'Remove':'Add'} </button></a></span>
                                </td>

                                <td>
                                    <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                         referenceVal="${dataSet.quickFee3}"  referenceType="quickFee3" class="edit-reference" title="Edit"><button
                                                class="btn btn-xs ${dataSet.quickFee3?'btn-success':'btn-warning'}">${dataSet.quickFee3?'Remove':'Add'} </button></a></span>
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
        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 className-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'collections',action: 'manageQuickFeeList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(5)', nRow).html(getActionButtons5(nRow, aData));
                $('td:eq(6)', nRow).html(getActionButtons6(nRow, aData));
                $('td:eq(7)', nRow).html(getActionButtons7(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "className", "value": $('#className-filter').val()});
            },
            "aoColumns": [
                null,
                null,
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.className-filter-holder').html('<select id="className-filter" class="form-control" name="className-filter"><option value="">All Class Items</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#className-filter').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(true);
            hideLoading("#data-table-holder");
        });
        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var referenceType = $(control).attr('referenceType');
            var referenceVal = $(control).attr('referenceVal');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                data: { id: referenceId, quickItemName: referenceType,refValue:referenceVal },
                url: "${g.createLink(controller: 'collections',action: 'editQuick')}",
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        showSuccessMsg(data.message);
                        $('#list-table').DataTable().ajax.reload();
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

    });

    function getActionButtons5(nRow, aData) {
        var actionButtons = "";
        var buttonColour1=aData.quickFee1 == true ?'btn-success':'btn-warning';
        var buttonText1=aData.quickFee1 == true?'Remove':'Add';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceVal="' + aData.quickFee1 + '" referenceId="' + aData.DT_RowId + '" referenceType="quickFee1" class="edit-reference" title="Edit">';
        actionButtons += '<button class="btn btn-xs '+buttonColour1+'">'+buttonText1+'</button>';
        actionButtons += '</a></span>';
        return actionButtons;
    }
    function getActionButtons6(nRow, aData) {
        var actionButtons = "";
        var buttonColour2=aData.quickFee2 == true ?'btn-success':'btn-warning';
        var buttonText2=aData.quickFee2 == true?'Remove':'Add';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceVal="' + aData.quickFee2 + '" referenceId="' + aData.DT_RowId + '" referenceType="quickFee2" class="edit-reference" title="Edit">';
        actionButtons += '<button class="btn btn-xs '+buttonColour2+'">'+buttonText2+'</button>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

    function getActionButtons7(nRow, aData) {
        var actionButtons = "";
        var buttonColour3=aData.quickFee3 == true ?'btn-success':'btn-warning';
        var buttonText3=aData.quickFee3 == true?'Remove':'Add';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceVal="' + aData.quickFee3 + '" referenceId="' + aData.DT_RowId + '" referenceType="quickFee3" class="edit-reference" title="Edit">';
        actionButtons += '<button class="btn btn-xs '+buttonColour3+'">'+buttonText3+'</button>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
