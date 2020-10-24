<!DOCTYPE html>
<html>
<head>
    <title>Dps Amount SetUp</title>
    <meta name="layout" content="moduleHRLayout"/>
    <style>
    .form-group {
        margin-bottom: 5px;
    }
    #medical {margin-top: 0px;}

    </style>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="DPS Amount List"  SHOW_CREATE_BTN="YES" createButtonText="DPS Amount" />
<div class="row" id="create-form-holder" style="display: none;">
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
               DPS Amount List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr style="white-space: nowrap">
                            <th>SL</th>
                            <th >Emp-ID</th>
                            <th >Emp-Name</th>
                            <th >Designation</th>
                            <th >School Contribution</th>
                            <th >Own Contribution</th>
                            <th >Total Balance</th>
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
    var dpsSetup;
    jQuery(function ($) {
        dpsSetup = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salDps',action: 'dpsAmountList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'editDps,delete'));

                return nRow;
            },
            "fnServerParams": function (aoData) {
            },
            "aoColumns": [

                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        var $dpsLoad;
        $('.btn.btn-primary').on('click', function(){
            glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/dpsLoad"])}',function($modal){
                $dpsLoad=$modal
            })
        });
        $(document).on('click','#dpsBtn' ,function () {
            $.ajax({
                url: "${createLink(controller: 'salDps', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $dpsLoad.find("#createFormModal").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        dpsSetup.draw();
                        showSuccessMsg(data.message);
                        $dpsLoad.modal('hide')
                    } else {
                        $dpsLoad.find('.modal-refresh-processing').remove();
                        $dpsLoad.find(".modal-footer").show();
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }

            })

        })

        $('#list-table').on('click', 'a.editDps-reference', function (e){
            $("#createFormModal").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salDps',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/dpsLoad"])}',function($modal){
                            $("#openingBalanceHolder").show();
                            $("#ownContributionBalanceHolder").show();
                            $modal.find("#objId").val(data.obj.id);
                            $modal.find("#openInsAmount").val(data.obj.openInsAmount);
                            $modal.find("#totalInsAmount").val(data.obj.totalInsAmount).attr("readonly", true);
                            $modal.find("#openOwnAmount").val(data.obj.openOwnAmount);
                            $modal.find("#totalOwnAmount").val(data.obj.totalOwnAmount).attr("readonly", true);
                            $modal.find("#reason").val(data.obj.description);
                            $modal.find(".select2-chosen").empty().append(data.employeeName);
                            $("#employeeId").select2("readonly", true);
                            $dpsLoad=$modal
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
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
                        url: "${g.createLink(controller: 'salDps',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                dpsSetup.draw(false);
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
