<html>
<head>
    <title> Salary PC  Amount</title>
    <meta name="layout" content="moduleHRLayout"/>
<style>
.form-group {
    margin-bottom: 5px;
}
#medical {margin-top: 0px;}

</style>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText=" Salary PC  List"  SHOW_CREATE_BTN="YES" createButtonText="Add Salary PC Amount "/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
              Salary  PC  Amount List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr style="white-space: nowrap">

                            <th>SL</th>
                            <th>EmpId</th>
                            <th >Name</th>
                            <th >Designation</th>
                            <th >PC Amount</th>
                            <th >Due Amount</th>
                            <th >Monthly Installment </th>
                            <th >Status</th>
                            <th >Actions</th>

                        </tr>
                        </thead>
                        <tbody style="white-space: nowrap;">
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    jQuery(function ($) {
        var pcSetup = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-4 payType-filter-holder'><'col-md-2 month-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salPc',action: 'setupPcList')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "payType", "value": $('#payType').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(8)', nRow).html(getGridActionBtns(nRow, aData, 'delete , '));

                return nRow;
            },
            "aoColumns": [
                {"bSortable": false,"sClass": "center"},

                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.payType-filter-holder').html('<select id="payType" class="form-control col-md-4" name="payType"><option value="">All</option><option value="DUE">Due</option><option value="PAID">Paid</option></select>');
        $('#payType').on('change', function (e) {
            showLoading("#data-table-holder");
            pcSetup.draw(false);
            hideLoading("#data-table-holder");
        });
        var $advanceModal;
        $('.create-new-btn').on('click', function(){
            glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salPcSetup"])}',function($modal){
                $advanceModal=$modal
            })
        });
        $(document).on('click','#pcAmountbtn' ,function (e) {
            var objId = $("#objId").val();

            var employeeId = $("#employeeId").val();
            var installmentAmount = $("#installmentAmount").val();
             var payableAmount = $("#payableAmount").val();
             var description = $("#description").val();

            if(objId.length === 0 && employeeId.length ===0 ){
                alert('Please select Employee');
                return false;
            }

            if(installmentAmount.length ===0 || payableAmount.length===0){
                alert('Please fill up the form correctly');
                return false;
            }

            $.ajax({
                url: "${createLink(controller: 'salPc', action: 'save')}",
                type: 'post',
                dataType: "json",

                data: $advanceModal.find("#createFormModal").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        pcSetup.draw(false);
                        showSuccessMsg(data.message);
                        $advanceModal.modal('hide')
                    } else {
                        $advanceModal.find('.modal-refresh-processing').remove();
                        $advanceModal.find(".modal-footer").show();
                        showErrorMsg(data.message);
                    }
                },

                failure: function (data) {
                }

            })
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var confirmStr = "Are you want to delete this  ." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'salPc',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                pcSetup.draw(false);
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                }
            });
            e.preventDefault();
        })
    });
</script>
</body>
</html>