
<html>
<head>
    <title>Area setup</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Area" SHOW_CREATE_BTN="YES" createButtonText="Add area Amount"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
             Area List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="">SL NO</th>
                            <th>Employee-ID</th>
                            <th>Name</th>
                            <th>Amount</th>
                            <th>Edit | Delete </th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var areaSetupTable;
    jQuery(function ($) {
        areaSetupTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'areaSetup', action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
             $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete,"",""'));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push(
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()},
                        {"name": "yearMonths", "value": $('#filterYearMonths').val()}
                );
            },
            "aoColumns": [
                {"bSortable": false,"sClass": "center"},
                null,
                null,
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false}
            ]
        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.month-filter-holder').html('<select id="filterYearMonths" class="form-control" name="filterYearMonths"><g:each in="${com.grailslab.enums.YearMonths.values()}" var="yearMonths"><option value="${yearMonths.key}">${yearMonths.value}</option></g:each></select>');

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            areaSetupTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#filterYearMonths').on('change', function (e) {
            showLoading("#data-table-holder");
            areaSetupTable.draw(false);
            hideLoading("#data-table-holder");
        });

        var $areaSetup;
        $('.create-new-btn').on('click', function(){
            glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salAreaSetup"])}',function($modal){
                $areaSetup=$modal

            })
        });
        $(document).on('click','#areaSetupBtn' ,function (e) {
            var employeeId = $areaSetup.find("#employee").val();
            var amount = $areaSetup.find("#amount").val();
            if(employeeId.length ===0 ){
                bootbox.alert('Please select Employee');
                return false;
            }
            if(amount.length ===0){
                bootbox.alert('Please put amount');
                return false;
            }
            $.ajax({
                url: "${createLink(controller: 'areaSetup', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $areaSetup.find("#createFormModal").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        $areaSetup.find("#employee").val("");
                        $areaSetup.find("#select2-chosen-1").empty().append("Search for a Employee [Name]");
                        areaSetupTable.draw();
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            e.preventDefault();
        });
        $('#list-table').on('click', 'a.edit-reference', function (e){
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'areaSetup',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/salAreaSetup"])}',function($modal){
                            $modal.find("#id").val(data.obj.id)
                            $modal.find("#employee").val(data.obj.employee.id)
                            $modal.find("#academicYear").val(data.obj.academicYear.name)
                            $modal.find("#yearMonths").val(data.obj.yearMonths.name)
                            $modal.find(".select2-chosen").empty().append(data.employeeName);
                            $modal.find("#s2id_employee").css("pointer-events","none");
                            $modal.find("#amount").val(data.obj.amount);
                            $areaSetup=$modal

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
             var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'areaSetup',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                areaSetupTable.draw(false);
                                showSuccessMsg(data.message);

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