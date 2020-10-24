
<html>
<head>
    <title> Salary Advance amount</title>
    <meta name="layout" content="moduleHRLayout"/>

</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText=" Salary Advance Amount List"  SHOW_CREATE_BTN="YES" createButtonText="Add Salary Advance Amount "/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
              Salary  Advance  Amount List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>SL</th>
                                <th>Emp ID</th>
                                <th>Name</th>
                                <th>Designation</th>
                                <th>#Loan Amount</th>
                                <th>#Paid Amount</th>
                                <th>#Due Amount</th>
                                <th>Installment</th>
                                <th>Description</th>
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
    var masterSetup;
    jQuery(function ($) {
        masterSetup = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salAdvance',action: 'setupList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(9)', nRow).html(getGridActionBtns(nRow, aData, 'edit,loan, ,delete , '));

                return nRow;
            },
            "fnServerParams": function (aoData) {
            },
            "aoColumns": [
                {"bSortable": false,"sClass": "center"},
                null,
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

        var $advanceModal;
        $('.create-new-btn').on('click', function(){
            glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salAdvanceAmountLoad"])}',function($modal){
                $advanceModal=$modal
            })
        });

        $(document).on('click','#advanceAmountbtn' ,function (e) {
            var objId = $("#objId").val();

            var employeeId = $("#employeeId").val();
            var installmentAmount = $("#installmentAmount").val();
             var currentAmount = $("#currentAmount").val();
             var description = $("#description").val();

            if(objId.length === 0 && employeeId.length ===0 ){
                alert('Please select Employee');
                return false;
            }

            if(installmentAmount.length ===0 || currentAmount.length===0||description.length===0){
                alert('Please fill up the form correctly');
                return false;
            }

            $.ajax({
                url: "${createLink(controller: 'salAdvance', action: 'save')}",
                type: 'post',
                dataType: "json",

                data: $advanceModal.find("#createFormModal").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        masterSetup.draw();
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

        $('#list-table').on('click', 'a.edit-reference', function (e){
            $("#createFormModal").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salAdvance',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salAdvanceAmountLoad"])}',function($modal){
                            var advanceDate = data.obj.advanceDate;
                            $modal.find("#objId").val(data.obj.id);
                            $modal.find("#installmentAmount").val(data.obj.installmentAmount);
                            $modal.find("#currentAmount").val(data.obj.currAmount);
                            $modal.find("#description").val(data.obj.description);
                            $modal.find('#advanceDate').datepicker('setDate', new Date(advanceDate));
                            $modal.find(".select2-chosen").empty().append(data.employeeName);
                            $("#employeeId").select2("readonly", true);
                            $advanceModal=$modal
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

        $('#list-table').on('click', 'a.loan-reference', function (e){
            $("#createFormModal").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salAdvance',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salAdvanceAmountLoad"])}',function($modal){
                            var advanceDate = data.obj.advanceDate;
                            $("#outStandingAmountHolder").show();
                            $modal.find("#outStandingAmount").val(data.obj.outStandingAmount);
                            $modal.find("#installmentAmount").val(data.obj.installmentAmount);
                            $modal.find("#objId").val(data.obj.id);
                            $modal.find(".select2-chosen").empty().append(data.employeeName);
                            $("#employeeId").select2("readonly", true);
                            $advanceModal=$modal
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
                        url: "${g.createLink(controller: 'salAdvance',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                masterSetup.draw(false);
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